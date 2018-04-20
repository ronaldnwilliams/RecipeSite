package com.teamtreehouse.recipesite.dao;

import com.teamtreehouse.recipesite.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface RoleDao extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
