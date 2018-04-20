package com.teamtreehouse.recipesite.web.controller;

import com.teamtreehouse.recipesite.model.Ingredient;
import com.teamtreehouse.recipesite.model.Recipe;
import com.teamtreehouse.recipesite.model.User;
import com.teamtreehouse.recipesite.service.IngredientService;
import com.teamtreehouse.recipesite.service.RecipeService;
import com.teamtreehouse.recipesite.service.UserService;
import com.teamtreehouse.recipesite.web.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @Autowired
    private IngredientService ingredientService;

    @RequestMapping(value = {"/", "/index", "/recipes"})
    public String index(
            @RequestParam(name ="category", defaultValue = "All Categories", required = false) String category,
            @RequestParam(name="searchTerm", required = false) String searchTerm,
            @RequestParam(name="searchField", defaultValue = "description", required = false) String searchField,
            Model model, Principal principal) {
        // Check for a logged in user
        User user = checkForUser((UsernamePasswordAuthenticationToken) principal);

        // get recipes based on search term, search field, and category
        if (searchTerm != null) {
            List<Recipe> recipes = getByCategory(getRecipesFromSearch(searchTerm, searchField), category);
            model.addAttribute("recipes", recipes);

        } else {
            List<Recipe> recipes = getByCategory(recipeService.findAll(), category);
            model.addAttribute("recipes", recipes);
        }

        // get the category
        Category categoryGiven = Category.valueOf(category.replace(" ", "").toUpperCase());

        if (user != null) {
            model.addAttribute("user", user);
        }
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("searchField", searchField);
        model.addAttribute("categories", Category.values());
        model.addAttribute("categoryGiven", categoryGiven);
        return "index";
    }

    private List<Recipe> getRecipesFromSearch(String searchTerm, String searchField) {
        if (searchField.equalsIgnoreCase("description")) {
            return recipeService.findByDescriptionIgnoreCaseContaining(searchTerm);
        } else {
            List<Recipe> ingredientRecipes = new ArrayList<>();
            List<Ingredient> ingredients = ingredientService.findByItem(searchTerm);
            ingredients.forEach(i -> {
                recipeService.findRecipeByIngredients(i).forEach(r -> {
                    if (!ingredientRecipes.contains(r)) {
                        ingredientRecipes.add(r);
                    }
                });
            });
            return ingredientRecipes;
        }
    }

    private List<Recipe> getByCategory(List<Recipe> recipes, String category) {
        if (category.equalsIgnoreCase("All Categories") || category.equalsIgnoreCase("allcategories") || category == "") {
            return recipes;
        } else {
            List<Recipe> categoryRecipes = new ArrayList<>();
            recipes.forEach(recipe -> {
                if (recipe.getCategory().getCategory().equalsIgnoreCase(category)) {
                    categoryRecipes.add(recipe);
                }
            });
            return categoryRecipes;
        }
    }

    @RequestMapping("/recipes/image/{recipeId}.jpg")
    @ResponseBody
    public byte[] recipeImage(@PathVariable Long recipeId) {
        return recipeService.findById(recipeId).getBytes();
    }

    @RequestMapping("/recipes/details/{recipeId}")
    public String recipeDetails(@PathVariable Long recipeId, Model model, Principal principal) {
        User user = checkForUser((UsernamePasswordAuthenticationToken) principal);
        Recipe recipe = recipeService.findById(recipeId);
        if (user != null) {
            model.addAttribute("user", user);
        }
        model.addAttribute("recipe", recipe);
        return "detail";
    }

    @RequestMapping(value = "/recipes/add")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String addRecipeForm(Model model, Principal principal) {
        User user = checkForUser((UsernamePasswordAuthenticationToken) principal);
        if (!model.containsAttribute("recipe")) {
            Recipe recipe = new Recipe();
            recipe.getIngredients().add(new Ingredient("", "", ""));
            String[] steps = {""};
            recipe.setSteps(steps);
            model.addAttribute("recipe", recipe);
        }
        if (user != null) {
            model.addAttribute("user", user);
        }
        model.addAttribute("categories", Category.values());
        model.addAttribute("action", String.format("/recipes"));
        model.addAttribute("heading", "Recipe Creator");
        return "form";
    }


    @RequestMapping(value = "/recipes", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String addRecipe(@Valid Recipe recipe, BindingResult result,
                            @RequestParam(required = false) MultipartFile file, Principal principal,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.recipe", result);
            redirectAttributes.addFlashAttribute("recipe", recipe);
            if (file != null) {
                redirectAttributes.addFlashAttribute("file", file);
            }
            return "redirect:/recipes/add";
        }
        // get and set user
        User user = checkForUser((UsernamePasswordAuthenticationToken) principal);
        recipe.setCreator(user);
        // get and save all ingredients
        recipe.getIngredients().forEach(i -> ingredientService.save(i));
        // check for uploaded image
        if (file == null) {
            recipeService.save(recipe);
        } else {
            recipeService.save(recipe, file);
        }
        return String.format("redirect:/recipes/details/%s", recipe.getId());
    }

    @RequestMapping("/recipes/{recipeId}/edit")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String editRecipe(@PathVariable Long recipeId, Model model, Principal principal) {
        if (!model.containsAttribute("recipe")) {
            Recipe recipe = recipeService.findById(recipeId);
            model.addAttribute("recipe", recipe);
        }
        User user = checkForUser((UsernamePasswordAuthenticationToken) principal);
        model.addAttribute("user", user);
        model.addAttribute("categories", Category.values());
        model.addAttribute("action", "/recipes/update");
        model.addAttribute("heading", "Recipe Editor");
        return "form";
    }

    @RequestMapping(value = "/recipes/update", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String updateRecipe(@Valid Recipe recipe, BindingResult result,
                               @RequestParam(required = false) MultipartFile file,
                               UsernamePasswordAuthenticationToken principal, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.recipe", result);
            redirectAttributes.addFlashAttribute("recipe", recipe);
            if (file != null) {
                redirectAttributes.addFlashAttribute("file", file);
            }
            return String.format("redirect:/recipes/%s/edit", recipe.getId());
        }
        User user = checkForUser((UsernamePasswordAuthenticationToken) principal);
        recipe.getIngredients().forEach(i -> ingredientService.save(i));
        if (file == null) {
            try {
                recipe.setBytes(recipeService.findById(recipe.getId()).getBytes());
                recipeService.save(recipe);
            } catch (NullPointerException e) {
                recipeService.save(recipe);
            }
        } else {
            recipeService.save(recipe, file);
        }
        return String.format("redirect:/recipes/details/%s", recipe.getId());
    }


    @RequestMapping(value = "/recipes/{recipeId}/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String deleteRecipe(@PathVariable Long recipeId, HttpServletRequest request) {
        Recipe recipe = recipeService.findById(recipeId);
        recipeService.delete(recipe);
        return "redirect:/";
    }

    @RequestMapping(value = "/recipes/{recipeId}/favorite", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String favorite(@PathVariable Long recipeId, HttpServletRequest request, Principal principal) {
        User user = checkForUser((UsernamePasswordAuthenticationToken) principal);
        Recipe recipe = recipeService.findById(recipeId);
        userService.toggleFavorite(user, recipe);
        userService.save(user);
        return String.format("redirect:%s", request.getHeader("referer"));
    }

    private User checkForUser(UsernamePasswordAuthenticationToken principal) {
        try {
            return userService.findByUsername(((User) principal.getPrincipal()).getUsername());
        } catch (NullPointerException e) {}
        return null;
    }


}
