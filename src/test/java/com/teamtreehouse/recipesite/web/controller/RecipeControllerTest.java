package com.teamtreehouse.recipesite.web.controller;

import com.teamtreehouse.recipesite.model.Ingredient;
import com.teamtreehouse.recipesite.model.Recipe;
import com.teamtreehouse.recipesite.service.IngredientService;
import com.teamtreehouse.recipesite.service.RecipeService;
import com.teamtreehouse.recipesite.service.UserService;
import com.teamtreehouse.recipesite.web.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class RecipeControllerTest {
    private MockMvc mockMvc;
    private Validator mockValidator = mock(Validator.class);

    @InjectMocks
    private RecipeController controller;

    @Mock
    private RecipeService recipeService;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setValidator(mockValidator)
                .build();
    }

    @Test
    public void index_ShouldIncludeRecipesInModel() throws Exception {
        List<Recipe> recipes = Arrays.asList(
            recipeBuilder(1L)
        );
        when(recipeService.findAll()).thenReturn(recipes);

        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attribute("recipes", recipes));
        verify(recipeService).findAll();
    }

    @Test
    public void index_ShouldReturnRecipesWithGivenCategory() throws Exception {
        List<Recipe> recipes = Arrays.asList(
                recipeBuilder(1L)
        );
        when(recipeService.findAll()).thenReturn(recipes);

        mockMvc.perform(get("/")
                .param("category", "dinner"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("recipes", recipes));
        verify(recipeService).findAll();
    }

    @Test
    public void index_ShouldReturnRecipesWithMatchingDescriptionGivenSearchTerm() throws Exception {
        List<Recipe> recipes = Arrays.asList(
                recipeBuilder(1L)
        );
        when(recipeService.findByDescriptionIgnoreCaseContaining(recipes.get(0).getDescription())).thenReturn(recipes);

        mockMvc.perform(get("/")
                .param("searchTerm", "description"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("recipes", recipes));
        verify(recipeService).findByDescriptionIgnoreCaseContaining(recipes.get(0).getDescription());
    }

    @Test
    public void index_ShouldReturnRecipesWithMatchingIngredientsGivenSearchTerm() throws Exception {
        List<Recipe> recipes = Arrays.asList(
                recipeBuilder(1L)
        );
        when(ingredientService.findByItem("Name")).thenReturn(recipes.get(0).getIngredients());
        when(recipeService.findRecipeByIngredients(recipes.get(0).getIngredients().get(0))).thenReturn(recipes);

        mockMvc.perform(get("/")
                .param("searchTerm", "Name")
                .param("searchField", "ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("recipes", recipes));
        verify(ingredientService).findByItem("Name");
        verify(recipeService).findRecipeByIngredients(recipes.get(0).getIngredients().get(0));
    }

    @Test
    public void recipeImage_ShouldReturnRecipeBytes() throws Exception {
        Recipe recipe = recipeBuilder(1L);
        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(get("/recipes/image/1.jpg"))
                .andReturn().getResponse().getContentAsByteArray().equals(recipe.getBytes());
        verify(recipeService).findById(1L);
    }

    @Test
    public void recipeDetails_ShouldIncludeRecipeInModel() throws Exception {
        Recipe recipe = recipeBuilder(1L);
        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(get("/recipes/details/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("detail"))
                .andExpect(model().attribute("recipe", recipe));
        verify(recipeService).findById(1L);
    }

    @Test
    @WithMockUser
    public void addRecipeForm_ShouldRenderRecipeForm() throws Exception {
        mockMvc.perform(get("/recipes/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"));
    }

    @Test
    @WithMockUser
    public void addRecipe_ShouldRedirectToNewRecipeDetails() throws Exception {
        doAnswer(invocation -> {
            Recipe recipe = (Recipe) invocation.getArguments()[0];
            recipe.setId(1L);
            return null;
        }).when(recipeService).save(any(Recipe.class));

        mockMvc.perform(post("/recipes"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/details/1"));
        verify(recipeService).save(any(Recipe.class));
    }

    @Test
    @WithMockUser
    public void editRecipe_ShouldIncludeRequestedRecipeInModel() throws Exception {
        Recipe recipe = recipeBuilder(1L);
        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(get("/recipes/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attribute("recipe", recipe));
        verify(recipeService).findById(1L);
    }

    @Test
    @WithMockUser
    public void updateRecipe_ShouldRedirectToEditedRecipeDetails() throws Exception {
        doAnswer(invocation -> {
            Recipe recipe = (Recipe) invocation.getArguments()[0];
            recipe.setId(1L);
            return null;
        }).when(recipeService).save(any(Recipe.class));

        mockMvc.perform(post("/recipes/update"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/details/1"));
        verify(recipeService).save(any(Recipe.class));
    }

    @Test
    @WithMockUser
    public void deleteRecipe_ShouldRedirectToIndex() throws Exception {
        when(recipeService.findById(1L)).thenReturn(recipeBuilder(1L));
        doNothing().when(recipeService).delete(any(Recipe.class));

        mockMvc.perform(post("/recipes/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(recipeService).findById(1L);
    }

    @Test
    @WithMockUser
    public void favorite_ShouldRedirectToReferer() throws Exception {
        Recipe recipe = recipeBuilder(1L);
        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(post("/recipes/1/favorite")
            .header("referer", "/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(recipeService).findById(1L);
    }


    private Ingredient ingredientBuilder() {
        Ingredient ingredient = new Ingredient();
        ingredient.setItem("Name");
        ingredient.setCondition("Condition");
        ingredient.setQuantity("1");
        return ingredient;
    }

    private Recipe recipeBuilder(Long id) {
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName("recipe");
        recipe.setDescription("description");
        recipe.setBytes(new byte[]{1,2,3});
        recipe.setIngredients(Arrays.asList(ingredientBuilder()));
        recipe.setCookTime("15");
        recipe.setPrepTime("15");
        recipe.setCategory(Category.DINNER);
        recipe.setSteps(new String[]{"Step1", "Step2"});
        return recipe;
    }
}