package com.teamtreehouse.recipesite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamtreehouse.recipesite.web.Category;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @JsonIgnore
    private byte[] bytes;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull(message = "please select a category")
    private Category category;
    private LocalDateTime dateUploaded = LocalDateTime.now();

    @NotBlank
    private String prepTime;

    @NotBlank
    private String cookTime;

    @Size(min = 1)
    private String[] steps;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Ingredient> ingredients = new ArrayList<>();

    @ManyToOne
    private User creator;

    public Recipe() {}

    public Recipe(String name, String description, Category category, byte[] image, String prepTime, String cookTime, String[] steps, List<Ingredient> ingredients, User creator) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.bytes = image;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.steps = steps;
        this.ingredients = ingredients;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(LocalDateTime dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String[] getSteps() {
        return steps;
    }

    public void setSteps(String[] steps) {
        this.steps = steps;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }


}
