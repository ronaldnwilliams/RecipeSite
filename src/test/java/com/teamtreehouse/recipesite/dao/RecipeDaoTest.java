package com.teamtreehouse.recipesite.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.teamtreehouse.recipesite.Application;
import com.teamtreehouse.recipesite.model.Ingredient;
import com.teamtreehouse.recipesite.model.Recipe;
import com.teamtreehouse.recipesite.web.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Application.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class RecipeDaoTest {

    @Autowired
    private RecipeDao recipeDao;

    @Autowired
    private IngredientDao ingredientDao;

    @Autowired RoleDao roleDao;

    @Test
    public void findRecipeById_ShouldReturnRecipe() throws Exception {
        assertThat(recipeDao.findRecipeById(1L), notNullValue(Recipe.class));
    }

    @Test
    public void findAll_ShouldReturnFour() throws Exception {
        assertThat(recipeDao.findAll(), hasSize(5));
    }

    @Test
    public void findByDescriptionIgnoreCaseContaining_ShouldReturnOne() throws Exception {
        assertThat(recipeDao.findByDescriptionIgnoreCaseContaining("salmon"), hasSize(1));
    }

    @Test
    public void findRecipeByIngredients_ShouldReturnOne() throws Exception {
        Ingredient ingredient = ingredientDao.findByItem("item3").get(0);
        assertThat(recipeDao.findRecipeByIngredients(ingredient), hasSize(1));
    }

    @Test
    @WithMockUser
    public void save_ShouldPersistEntity() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setCookTime("15");
        recipe.setPrepTime("15");
        recipe.setDescription("Description");
        recipe.setName("Recipe");
        recipe.setCategory(Category.LUNCH);
        recipeDao.save(recipe);
        assertThat(recipeDao.findRecipeById(recipe.getId()), notNullValue(Recipe.class));
    }
}