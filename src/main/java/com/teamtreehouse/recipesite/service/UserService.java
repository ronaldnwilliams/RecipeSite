package com.teamtreehouse.recipesite.service;

import com.teamtreehouse.recipesite.model.Recipe;
import com.teamtreehouse.recipesite.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    void save(User user);
    void toggleFavorite(User user, Recipe recipe);
}