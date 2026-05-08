package com.adolfomartinez.recipescaler.io;

import com.adolfomartinez.recipescaler.model.Ingredient;
import com.adolfomartinez.recipescaler.model.Recipe;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class RecipeTextFormatter {

    private static final DecimalFormat EXPORT_AMOUNT_FORMAT;

    static {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);
        EXPORT_AMOUNT_FORMAT = new DecimalFormat("#0.###", symbols);
        EXPORT_AMOUNT_FORMAT.setGroupingUsed(false);
    }

    private RecipeTextFormatter() {
    }

    public static String sanitizeFileName(String recipeName) {
        String sanitized = recipeName.replaceAll("[^a-zA-Z0-9-_ ]", "").trim().replace(" ", "_");
        return sanitized.isEmpty() ? "recipe" : sanitized;
    }

    // Headers, blank line, and one line per ingredient using unit labels
    public static String formatExport(Recipe recipe) {
        String ln = System.lineSeparator();
        StringBuilder sb = new StringBuilder();
        sb.append("Recipe Name: ").append(recipe.getName()).append(ln);
        sb.append("Base Servings: ").append(recipe.getBaseServings()).append(ln);
        sb.append(ln);
        sb.append("Ingredients:").append(ln);
        for (Ingredient ingredient : recipe.getIngredients()) {
            sb.append("- ")
                    .append(ingredient.getName())
                    .append(": ")
                    .append(EXPORT_AMOUNT_FORMAT.format(ingredient.getAmount()))
                    .append(' ')
                    .append(ingredient.getUnit())
                    .append(ln);
        }
        return sb.toString();
    }
}
