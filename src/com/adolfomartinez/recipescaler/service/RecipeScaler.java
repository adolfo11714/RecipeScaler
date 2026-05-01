package com.adolfomartinez.recipescaler.service;

import com.adolfomartinez.recipescaler.model.Ingredient;
import com.adolfomartinez.recipescaler.model.Recipe;
import java.util.ArrayList;
import java.util.List;

public final class RecipeScaler {

    private static final int SCALE_DECIMAL_PLACES = 2;

    private RecipeScaler() {
    }

    // Scales each ingredient from referenceServings (batch the amounts apply to) up or down to targetServings
    public static List<Ingredient> scaleForServings(
            Recipe recipe, int referenceServings, int targetServings) {
        if (referenceServings <= 0) {
            throw new IllegalArgumentException("Reference servings must be greater than 0.");
        }
        if (targetServings <= 0) {
            throw new IllegalArgumentException("Desired servings must be greater than 0.");
        }
        double factor = (double) targetServings / (double) referenceServings;
        List<Ingredient> out = new ArrayList<>();
        for (Ingredient ing : recipe.getIngredients()) {
            double amt = roundPlaces(ing.getAmount() * factor, SCALE_DECIMAL_PLACES);
            if (amt <= 0) {
                throw new IllegalArgumentException("Scaled amount too small for ingredient: " + ing.getName());
            }
            out.add(new Ingredient(ing.getName(), amt, ing.getUnit()));
        }
        return out;
    }

    public static double scalingFactor(int referenceServings, int targetServings) {
        if (referenceServings <= 0) {
            throw new IllegalArgumentException("Reference servings must be greater than 0.");
        }
        if (targetServings <= 0) {
            throw new IllegalArgumentException("Desired servings must be greater than 0.");
        }
        return (double) targetServings / (double) referenceServings;
    }

    private static double roundPlaces(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
