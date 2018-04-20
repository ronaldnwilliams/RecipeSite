package com.teamtreehouse.recipesite.web;

public enum Category {
    ALLCATEGORIES ("All Categories"),
    BREAKFAST ("Breakfast"),
    LUNCH ("Lunch"),
    DINNER ("Dinner"),
    DESSERT ("Dessert")
    ;

    private final String category;

    Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }
}
