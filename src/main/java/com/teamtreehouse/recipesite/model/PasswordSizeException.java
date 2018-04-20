package com.teamtreehouse.recipesite.model;

public class PasswordSizeException extends RuntimeException {
    public PasswordSizeException() {
        super("must be 6 to 100 characters");
    }
}
