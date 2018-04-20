package com.teamtreehouse.recipesite.service;

import com.teamtreehouse.recipesite.dao.RecipeDao;
import com.teamtreehouse.recipesite.model.Ingredient;
import com.teamtreehouse.recipesite.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private RecipeDao recipeDao;


    @Override
    public List<Recipe> findAll() {
        List<Recipe> recipes = recipeDao.findAll();
        sortRecipesByName(recipes);
        return recipes;
    }

    private void sortRecipesByName(List<Recipe> recipes) {
        try {
            recipes.sort((r1, r2) -> r1.getName().compareToIgnoreCase(r2.getName()));
        } catch (NullPointerException e) {}
    }

    @Override
    public Recipe findById(Long id) {
        Recipe recipe = recipeDao.findRecipeById(id);
        if (recipe == null) {
            throw new RecipeNotFoundException();
        }
        return recipe;
    }

    @Override
    public List<Recipe> findByDescriptionIgnoreCaseContaining(String searchTerm) {
        List<Recipe> recipes = recipeDao.findByDescriptionIgnoreCaseContaining(searchTerm);
        sortRecipesByName(recipes);
        return recipes;
    }

    @Override
    public List<Recipe> findRecipeByIngredients(Ingredient ingredient) {
        return recipeDao.findRecipeByIngredients(ingredient);
    }

    @Override
    public void save(Recipe recipe) {
        recipeDao.save(recipe);
    }

    @Override
    public void save(Recipe recipe, MultipartFile file) {
        try {
            recipe.setBytes(file.getBytes());
            recipeDao.save(recipe);
        } catch (IOException e) {
            System.err.println("Unable to get byte array from uploaded file.");
            recipeDao.save(recipe);
        }
    }

    @Override
    public void delete(Recipe recipe) {
        recipeDao.delete(recipe);
    }
}
