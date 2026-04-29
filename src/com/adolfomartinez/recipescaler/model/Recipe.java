package com.adolfomartinez.recipescaler.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recipe {
    private final String name;
    private final int baseServings;
    private final List<Ingredient> ingredients;

    public Recipe(String name, int baseServings, List<Ingredient> ingredients) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Recipe name is required.");
        }
        if (baseServings <= 0) {
            throw new IllegalArgumentException("Base servings must be greater than 0.");
        }
        if (ingredients == null) {
            throw new IllegalArgumentException("Ingredients list is required.");
        }

        this.name = name.trim();
        this.baseServings = baseServings;
        this.ingredients = new ArrayList<>(ingredients);
    }

    public String getName() {
        return name;
    }

    public int getBaseServings() {
        return baseServings;
    }

    public List<Ingredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }
}
