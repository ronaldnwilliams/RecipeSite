package com.teamtreehouse.recipesite.dao;

import com.teamtreehouse.recipesite.model.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientDao extends CrudRepository<Ingredient, Long> {
    Ingredient findIngredientById(Long id);
    List<Ingredient> findByItem(String item);
    List<Ingredient> findAll();

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    <S extends Ingredient> S save(S entity);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    <S extends Ingredient> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(Ingredient entity);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteAll(Iterable<? extends Ingredient> entities);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void deleteAll();
}
