package com.example.smartpantry;

import java.util.HashMap;
import java.util.Map;
public class MatchingUtils {
    private static final Map<String, Object[]> UNIT_TABLE = new HashMap<>();
    static {
        UNIT_TABLE.put("g", new Object[]{"g", 1.0});
        UNIT_TABLE.put("kg", new Object[]{"g", 1000.0});
        UNIT_TABLE.put("ml", new Object[]{"ml", 1.0});
        UNIT_TABLE.put("l", new Object[]{"ml", 1000.0});
        UNIT_TABLE.put("pcs", new Object[]{"pcs", 1.0});
        UNIT_TABLE.put("", new Object[]{"pcs", 1.0});
        UNIT_TABLE.put("", new Object[]{"c", 1.0});
    }

    public static String normalizeName(String rawName) {
        if (rawName == null) return "";
        String n = rawName.trim().toLowerCase();
        if (n.endsWith("es") && n.length() > 4) {
            n = n.substring(0, n.length() - 2);
        } else if (n.endsWith("s") && n.length() > 3) {
            n = n.substring(0, n.length() - 1);
        if (n.equals("tomatoe")) n = "tomato";
        if (n.equals("potatoe")) n = "potato";
        return n;
    }
    private static String normalizeUnit(String unit) {
        return unit == null ? "" : unit.trim().toLowerCase();
    }
    public static double toBaseQuantity(double quantity, String unit) {
        Object[] entry = UNIT_TABLE.get(normalizeUnit(unit));
        if (entry == null) return quantity;
        return quantity * (double) entry[1];
    }

    public static String baseUnitFamily(String unit) {
        Object[] entry = UNIT_TABLE.get(normalizeUnit(unit));
        return entry == null ? normalizeUnit(unit) : (String) entry[0];
    }

    public static boolean pantryCovers(Ingredient pantryItem, RecipeIngredient required) {
        if (pantryItem == null) return false;
        boolean namesMatch = normalizeName(pantryItem.getName())
                .equals(normalizeName(required.getName()));
        if (!namesMatch) return false;
        String pantryFamily = baseUnitFamily(pantryItem.getUnit());
        String requiredFamily = baseUnitFamily(required.getUnit());

        if (!pantryFamily.equals(requiredFamily)) {
            return pantryItem.getQuantity() > 0;
        }
        double pantryBase = toBaseQuantity(pantryItem.getQuantity(), pantryItem.getUnit());
        double requiredBase = toBaseQuantity(required.getQuantity(), required.getUnit());
        return pantryBase >= requiredBase;
    }
}
