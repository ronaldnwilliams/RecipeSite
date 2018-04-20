package com.teamtreehouse.recipesite.service;

import com.teamtreehouse.recipesite.model.Ingredient;
import com.teamtreehouse.recipesite.model.Recipe;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {
    List<Recipe> findAll();
    Recipe findById(Long id);
    List<Recipe> findByDescriptionIgnoreCaseContaining(String searchTerm);
    List<Recipe> findRecipeByIngredients(Ingredient ingredient);
    void save(Recipe recipe);
    void save(Recipe recipe, MultipartFile file);
    void delete(Recipe recipe);
}
