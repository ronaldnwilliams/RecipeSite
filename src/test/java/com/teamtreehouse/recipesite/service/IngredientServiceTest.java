package com.teamtreehouse.recipesite.service;

import com.teamtreehouse.recipesite.dao.IngredientDao;
import com.teamtreehouse.recipesite.model.Ingredient;
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
public class IngredientServiceTest {

    private List<Ingredient> ingredients;

    @Mock
    private IngredientDao ingredientDao;

    @InjectMocks
    private IngredientService ingredientService = new IngredientServiceImpl();

    @Before
    public void setUp() throws Exception {
        Ingredient ingredient1 = new Ingredient("item1", "condition1", "1");
        Ingredient ingredient2 = new Ingredient("item2", "condition2", "2");
        ingredients = Arrays.asList(
                ingredient1,
                ingredient2
        );
    }

    @Test
    public void findById_ShouldReturnOne() throws Exception {
        when(ingredientDao.findIngredientById(1L)).thenReturn(new Ingredient());

        assertThat(ingredientService.findById(1L), instanceOf(Ingredient.class));
        verify(ingredientDao).findIngredientById(1L);
    }

    @Test
    public void findAll_ShouldReturnTwo() throws Exception {
        when(ingredientDao.findAll()).thenReturn(ingredients);

        assertEquals("findAll should return two ingredients",
                2,
                ingredientService.findAll().size());
        verify(ingredientDao).findAll();
    }

    @Test
    public void findByItem_ShouldReturnOne() throws Exception {
        when(ingredientDao.findByItem("item1")).thenReturn(ingredients.subList(0,1));

        assertEquals("findByItem should return one ingredient",
                1,
                ingredientService.findByItem("item1").size());
        verify(ingredientDao).findByItem("item1");
    }
}