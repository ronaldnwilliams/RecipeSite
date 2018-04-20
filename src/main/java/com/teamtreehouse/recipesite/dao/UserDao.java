package com.teamtreehouse.recipesite.dao;

import com.teamtreehouse.recipesite.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface UserDao extends CrudRepository<User, Long> {
    User findByUsername(String username);
}

