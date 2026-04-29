package com.adolfomartinez.recipescaler;

public enum MeasurementUnit {
    TEASPOON("tsp"),
    TABLESPOON("tbsp"),
    CUP("cup"),
    FLUID_OUNCE("fl oz"),
    PINT("pt"),
    QUART("qt"),
    GALLON("gal"),
    MILLILITER("mL"),
    LITER("L"),
    GRAM("g"),
    KILOGRAM("kg"),
    OUNCE("oz"),
    POUND("lb"),
    PIECE("piece"),
    PINCH("pinch");

    private final String label;

    MeasurementUnit(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
