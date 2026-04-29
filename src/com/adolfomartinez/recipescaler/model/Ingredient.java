package com.adolfomartinez.recipescaler.model;

public class Ingredient {
    private final String name;
    private final double amount;
    private final MeasurementUnit unit;

    public Ingredient(String name, double amount, MeasurementUnit unit) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ingredient name is required.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Ingredient amount must be greater than 0.");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Ingredient unit is required.");
        }

        this.name = name.trim();
        this.amount = amount;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public MeasurementUnit getUnit() {
        return unit;
    }
}
