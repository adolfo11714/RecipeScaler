package com.adolfomartinez.recipescaler.io;

import com.adolfomartinez.recipescaler.model.Ingredient;
import com.adolfomartinez.recipescaler.model.MeasurementUnit;
import com.adolfomartinez.recipescaler.model.Recipe;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RecipeTextFileReader {

    private static final Pattern INGREDIENT_PATTERN =
            Pattern.compile("^(.+):\\s*([0-9]+(?:\\.[0-9]+)?)\\s+(.+)$");

    private RecipeTextFileReader() {
    }

    public static Recipe readRecipe(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        if (lines.size() < 4) {
            throw new IllegalArgumentException("Recipe file is incomplete.");
        }

        String recipeName = parseHeaderValue(lines.get(0), "Recipe Name:");
        int baseServings;
        try {
            baseServings = Integer.parseInt(parseHeaderValue(lines.get(1), "Base Servings:").trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Base servings must be a whole number.");
        }
        if (baseServings <= 0) {
            throw new IllegalArgumentException("Base servings must be greater than 0.");
        }

        List<Ingredient> ingredients = new ArrayList<>();
        for (String line : lines) {
            if (!line.startsWith("- ")) {
                continue;
            }
            String ingredientLine = line.substring(2).trim();
            Matcher matcher = INGREDIENT_PATTERN.matcher(ingredientLine);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid ingredient line: " + line);
            }
            String ingredientName = matcher.group(1).trim();
            double amount;
            try {
                amount = Double.parseDouble(matcher.group(2).trim());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid amount in line: " + line);
            }
            MeasurementUnit unit = unitFromLabel(matcher.group(3).trim());
            ingredients.add(new Ingredient(ingredientName, amount, unit));
        }

        return new Recipe(recipeName, baseServings, ingredients);
    }

    private static String parseHeaderValue(String line, String prefix) {
        if (!line.startsWith(prefix)) {
            throw new IllegalArgumentException("Missing header: " + prefix);
        }
        return line.substring(prefix.length()).trim();
    }

    private static MeasurementUnit unitFromLabel(String unitLabel) {
        for (MeasurementUnit unit : MeasurementUnit.values()) {
            if (unit.toString().equalsIgnoreCase(unitLabel) || unit.name().equalsIgnoreCase(unitLabel)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Unknown measurement unit: " + unitLabel);
    }
}
