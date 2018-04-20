package com.teamtreehouse.recipesite.service;

import com.teamtreehouse.recipesite.dao.RecipeDao;
import com.teamtreehouse.recipesite.model.Ingredient;
import com.teamtreehouse.recipesite.model.Recipe;
import com.teamtreehouse.recipesite.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceTest {

    private List<Recipe> recipes;
    private Ingredient ingredient;

    @Mock
    private RecipeDao recipeDao;

    @InjectMocks
    private RecipeService recipeService = new RecipeServiceImpl();

    @Before
    public void setUp() throws Exception {
        User user1 = new User();
        User user2 = new User();
        ingredient = new Ingredient("item1", "condition1", "1");
        List<Ingredient> ingredients = Arrays.asList(
                ingredient
        );
        Recipe recipe1 = new Recipe();
        recipe1.setDescription("beef");
        recipe1.setIngredients(ingredients);
        Recipe recipe2 = new Recipe();
        recipe2.setDescription("beef");
        recipe2.setIngredients(ingredients);
        Recipe recipe3 = new Recipe();
        recipe3.setDescription("chicken");
        recipes = Arrays.asList(
                recipe1,
                recipe2,
                recipe3
        );

    }

    @Test
    public void findById_ShouldReturnOne() throws Exception {
        when(recipeDao.findRecipeById(1L)).thenReturn(new Recipe());

        assertThat(recipeService.findById(1L), instanceOf(Recipe.class));
        verify(recipeDao).findRecipeById(1L);
    }

    @Test
    public void findAll_ShouldReturnThree() throws Exception {
        when(recipeDao.findAll()).thenReturn(recipes);

        assertEquals("findAll should return three recipes", 3, recipeService.findAll().size());
        verify(recipeDao).findAll();
    }


    @Test
    public void findByDescriptionIgnoreCaseContaining_ShouldReturnTwo() throws Exception {
        when(recipeDao.findByDescriptionIgnoreCaseContaining("beef")).thenReturn(recipes.subList(0,2));

        assertEquals("findByDescriptionIgnoreCaseContaining should return two recipes",
                2,
                recipeService.findByDescriptionIgnoreCaseContaining("beef").size());
        verify(recipeDao).findByDescriptionIgnoreCaseContaining("beef");
    }

    @Test
    public void findRecipeByIngredients_ShouldReturnTwo() throws Exception {
        when(recipeDao.findRecipeByIngredients(ingredient)).thenReturn(recipes.subList(0,2));

        assertEquals("findRecipeByIngredients should return two recipes",
                2,
                recipeService.findRecipeByIngredients(ingredient).size());
        verify(recipeDao).findRecipeByIngredients(ingredient);
    }
}
