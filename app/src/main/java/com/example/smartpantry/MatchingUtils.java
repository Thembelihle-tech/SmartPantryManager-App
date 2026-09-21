package com.example.smartpantry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MatchingUtils {
    private static final Map<String, Object[]> UNIT_TABLE = new HashMap<>();
    static {
        UNIT_TABLE.put("g", new Object[]{"g", 1.0});
        UNIT_TABLE.put("kg", new Object[]{"g", 1000.0});
        UNIT_TABLE.put("ml", new Object[]{"ml", 1.0});
        UNIT_TABLE.put("l", new Object[]{"ml", 1000.0});
        UNIT_TABLE.put("pcs", new Object[]{"pcs", 1.0});
        UNIT_TABLE.put("", new Object[]{"pcs", 1.0});
        UNIT_TABLE.put("c", new Object[]{"cup", 1.0});
        UNIT_TABLE.put("cup", new Object[]{"cup", 1.0});
        UNIT_TABLE.put("tsp", new Object[]{"tsp", 1.0});
        UNIT_TABLE.put("tbsp", new Object[]{"tsp", 2.0});
    }

    private static final Set<String> COOKING_OILS = new HashSet<>(Arrays.asList(
            "olive oil", "sunflower oil", "vegetable oil", "canola oil", "cooking oil"));

    private static final double EPSILON =1e-9;

    public static String normalizeName(String rawName) {
        String n = rawName.toLowerCase(Locale.ROOT)
                .replaceAll("[^\\p{L}\\p{N}]+", " ")
                .trim();
        if (n.endsWith("ies") && n.length() > 4) {
            n = n.substring(0, n.length() - 3) + "y";
        } else if ((n.endsWith("oes") || n.endsWith("ches") || n.endsWith("shes")) && n.length() > 4) {
            n = n.substring(0, n.length() - 2);
        } else if (n.endsWith("s") && !n.endsWith("ss") && n.length() > 3) {
            n = n.substring(0, n.length() - 1);
        }
        if (n.endsWith("ie") && n.length() > 3) {
            n = n.substring(0, n.length() - 2) + "y";
        }
        return n;
    }
    private static String normalizeUnit(String unit) {
        return unit == null ? "" : unit.trim().toLowerCase(Locale.ROOT);
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

    public static boolean namesMatch(String pantryName, String requiredName){
        String have = normalizeName(pantryName);
        String need = normalizeName(requiredName);
        if(have.isEmpty() || need.isEmpty()) return false;
        if(have.equals(need)) return true;
        return need.equals("oil") && COOKING_OILS.contains(have);
    }

    public static boolean pantryCovers(List<Ingredients> pantry, RecipeIngredients required) {
        if(pantry == null || required == null) return false;
        String requiredFamily = baseUnitFamily(required.getUnit());
        double requiredBase = toBaseQuantity(required.getQuantity(), required.getUnit());
        double haveInSameFamily = 0;

        for(Ingredients item : pantry){
            if(item == null || !namesMatch(item.getName(), required.getName())) continue;

            if(baseUnitFamily(item.getUnit()).equals(requiredFamily)){
                haveInSameFamily += toBaseQuantity(item.getQuantity(), item.getUnit());
            }else if(item.getQuantity() > 0){
                return true;
            }
        }
        return haveInSameFamily + EPSILON >= requiredBase;
    }
    public static boolean recipeIsCovered(Recipes recipe, List<Ingredients> pantry) {
        if (recipe == null) return false;
        for (RecipeIngredients required : recipe.getIngredients()) {
            if (!pantryCovers(pantry, required)) return false;
        }
        return true;
    }
}
