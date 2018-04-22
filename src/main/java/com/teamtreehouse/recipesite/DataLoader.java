package com.teamtreehouse.recipesite;

import com.teamtreehouse.recipesite.dao.IngredientDao;
import com.teamtreehouse.recipesite.dao.RecipeDao;
import com.teamtreehouse.recipesite.dao.RoleDao;
import com.teamtreehouse.recipesite.dao.UserDao;
import com.teamtreehouse.recipesite.model.Ingredient;
import com.teamtreehouse.recipesite.model.Recipe;
import com.teamtreehouse.recipesite.model.Role;
import com.teamtreehouse.recipesite.model.User;
import com.teamtreehouse.recipesite.web.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    IngredientDao ingredientDao;

    @Autowired
    RecipeDao recipeDao;

    @Override
    public void run(ApplicationArguments args) {
        Role adminRole = new Role("ROLE_ADMIN");
        roleDao.save(adminRole);

        Role userRole = new Role("ROLE_USER");
        roleDao.save(userRole);

        User userAdmin = new User();
        userAdmin.setUsername("RonaldW");
        userAdmin.setPassword("password");
        userAdmin.setEnabled(true);
        userAdmin.setRole(adminRole);
        userDao.save(userAdmin);

        User user = new User();
        user.setUsername("LoretteW");
        user.setPassword("password");
        user.setEnabled(true);
        user.setRole(userRole);
        userDao.save(user);

        User authenticatedUser = userDao.findByUsername("RonaldW");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticatedUser, null, authenticatedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String steps[] = {"This is step 1.", "This is step 2.", "And FINALLY! Step 3."};

        Ingredient ingredient1 = new Ingredient("item1", "condition1", "1");
        ingredientDao.save(ingredient1);

        Ingredient ingredient2 = new Ingredient("item2", "condition2", "2");
        ingredientDao.save(ingredient2);

        Ingredient ingredient3 = new Ingredient("item3", "condition3", "3");
        ingredientDao.save(ingredient3);

        Recipe recipe1 = null;
        try {
            recipe1 = new Recipe(
                    "Baked Alaskan Salmon with Asparagus",
                    "Some Alaskan Salmon that you bake with Asparagus.",
                    Category.DINNER,
                    Files.readAllBytes(Paths.get("./src/main/resources/static/assets/images/salmon.jpg")),
                    "30",
                    "30",
                    steps,
                    ingredientDao.findAll().subList(0,2),
                    user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipeDao.save(recipe1);

        Recipe recipe2 = null;
        try {
            recipe2 = new Recipe(
                    "Brioche French Toast with Nutella",
                    "Yummmy dessert toast covered in egg wash and topped with nutella and seasonal fruit.",
                    Category.DESSERT,
                    Files.readAllBytes(Paths.get("./src/main/resources/static/assets/images/frenchtoast.jpg")),
                    "15",
                    "15",
                    steps,
                    ingredientDao.findAll().subList(0,2),
                    user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipeDao.save(recipe2);

        Recipe recipe3 = null;
        try {
            recipe3 = new Recipe(
                    "Simple Miso Soup with Clams and Sake",
                    "This is really simple soup that has clams and sake.",
                    Category.LUNCH,
                    Files.readAllBytes(Paths.get("./src/main/resources/static/assets/images/miso.jpg")),
                    "25",
                    "25",
                    steps,
                    ingredientDao.findAll().subList(0,2),
                    userAdmin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipeDao.save(recipe3);

        Recipe recipe4 = null;
        try {
            recipe4 = new Recipe(
                    "Paleo Squash Noodles with Chicken Breast",
                    "This is a low carb dinner with Squash noodles and Chicken breast.",
                    Category.DINNER,
                    Files.readAllBytes(Paths.get("./src/main/resources/static/assets/images/chicken.jpg")),
                    "35",
                    "40",
                    steps,
                    ingredientDao.findAll(),
                    user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipeDao.save(recipe4);

    }
}
