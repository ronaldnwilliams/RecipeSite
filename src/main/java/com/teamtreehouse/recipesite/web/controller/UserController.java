package com.teamtreehouse.recipesite.web.controller;

import com.teamtreehouse.recipesite.dao.RoleDao;
import com.teamtreehouse.recipesite.model.Recipe;
import com.teamtreehouse.recipesite.model.User;
import com.teamtreehouse.recipesite.service.RecipeService;
import com.teamtreehouse.recipesite.service.UserService;
import com.teamtreehouse.recipesite.web.FlashMessage;
import org.h2.jdbc.JdbcSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RecipeService recipeService;

    @Autowired
    RoleDao roleDao;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm(Model model, HttpServletRequest request) {
        try {
            if (request.getHeader("referer").equalsIgnoreCase("http://localhost:8080/signup")) {
                model.addAttribute("flash", new FlashMessage("Successfully signed up! Please log in.", FlashMessage.Status.SUCCESS));
            } else {
                Object flash = request.getSession().getAttribute("flash");
                model.addAttribute("flash", flash);
                request.getSession().removeAttribute("flash");
            }
        } catch (Exception ex) {
            // "flash" session attribute must not exist...do nothing and proceed normally
        }
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        try {
            request.getSession().invalidate();
        } catch (Exception e) {}
        return "login";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        if (!model.containsAttribute("user")) {
            User user = new User();
            model.addAttribute("user", user);
        }
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String addUser(@Valid User user, BindingResult result, HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/signup";
        }
        user.setEnabled(true);
        user.setRole(roleDao.findByName("ROLE_USER"));
        try {
            userService.save(user);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes
                    .addFlashAttribute("flash", new FlashMessage("Username already exists", FlashMessage.Status.FAILURE));
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/signup";
        }
        return "redirect: /login";
    }

    @RequestMapping("/profile/{username}")
    public String profile(@PathVariable String username, Model model, UsernamePasswordAuthenticationToken principal) {
        try {
            User user = userService.findByUsername(((User) principal.getPrincipal()).getUsername());
            model.addAttribute("user", user);
            if (user.getUsername().equalsIgnoreCase(username)) {
                List<Recipe> favRecipes = new ArrayList<>();
                recipeService.findAll().forEach(recipe -> {
                    if (user.getFavorites().contains(recipe)) {
                        favRecipes.add(recipe);
                    }
                });
                model.addAttribute("favRecipes", favRecipes);
            }
        } catch (NullPointerException e) {}
        String userProfile = userService.findByUsername(username).getUsername();
        List<Recipe> userRecipes = new ArrayList<>();
        recipeService.findAll().forEach(recipe -> {
            if (recipe.getCreator().getUsername().equalsIgnoreCase(username)) {
                userRecipes.add(recipe);
            }
        });
        model.addAttribute("userProfile", userProfile);
        model.addAttribute("recipes", userRecipes);
        return "profile";
    }
}
