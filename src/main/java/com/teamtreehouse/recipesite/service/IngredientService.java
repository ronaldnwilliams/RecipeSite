package com.teamtreehouse.recipesite.service;

import com.teamtreehouse.recipesite.model.Ingredient;

import java.util.List;

public interface IngredientService {
    Ingredient findById(Long id);
    List<Ingredient> findAll();
    List<Ingredient> findByItem(String item);
    void save(Ingredient ingredient);
    void delete(Ingredient ingredient);
}
