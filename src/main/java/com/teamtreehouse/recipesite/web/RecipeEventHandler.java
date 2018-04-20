package com.teamtreehouse.recipesite.web;

import com.teamtreehouse.recipesite.dao.UserDao;
import com.teamtreehouse.recipesite.model.Recipe;
import com.teamtreehouse.recipesite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Recipe.class)
public class RecipeEventHandler {
    private final UserDao users;

    @Autowired
    public RecipeEventHandler(UserDao users) {
        this.users = users;
    }

    @HandleBeforeCreate
    public void addCreatorBasedOnLoggedInUser(Recipe recipe) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = users.findByUsername(username);
        recipe.setCreator(user);
    }
}
