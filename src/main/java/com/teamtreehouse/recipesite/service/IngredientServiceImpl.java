package com.teamtreehouse.recipesite.service;

import com.teamtreehouse.recipesite.dao.IngredientDao;
import com.teamtreehouse.recipesite.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    private IngredientDao ingredientDao;


    @Override
    public Ingredient findById(Long id) {
        return ingredientDao.findIngredientById(id);
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredientDao.findAll();
    }

    @Override
    public List<Ingredient> findByItem(String item) {
        return ingredientDao.findByItem(item);
    }

    @Override
    public void save(Ingredient ingredient) {
        ingredientDao.save(ingredient);
    }

    @Override
    public void delete(Ingredient ingredient) {
        ingredientDao.save(ingredient);
    }
}
