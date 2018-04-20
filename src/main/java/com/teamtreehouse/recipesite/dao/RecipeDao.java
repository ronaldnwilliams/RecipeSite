package com.teamtreehouse.recipesite.dao;

import com.teamtreehouse.recipesite.model.Ingredient;
import com.teamtreehouse.recipesite.model.Recipe;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeDao extends PagingAndSortingRepository<Recipe, Long> {

    Recipe findRecipeById(Long id);
    List<Recipe> findAll();
    List<Recipe> findByDescriptionIgnoreCaseContaining(String searchTerm);
    List<Recipe> findRecipeByIngredients(Ingredient ingredient);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    <S extends Recipe> S save(S entity);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    <S extends Recipe> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or @recipeDao.findOne(#id)?.creator?.username == authentication.name")
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or #recipe.creator?.username == authentication.name")
    void delete(@Param("recipe") Recipe entity);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteAll(Iterable<? extends Recipe> entities);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteAll();
}
