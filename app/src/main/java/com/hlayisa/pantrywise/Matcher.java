package com.hlayisa.pantrywise;

import java.util.*;

public final class Matcher {
    private Matcher() { }
    public static String name(String input) {
        String s = input.toLowerCase(Locale.ROOT).trim().replaceAll("\\s+", " ");
        Map<String,String> aliases = new HashMap<>();
        aliases.put("tomatoes", "tomato"); aliases.put("potatoes", "potato");
        aliases.put("eggs", "egg"); aliases.put("onions", "onion");
        aliases.put("carrots", "carrot"); aliases.put("bananas", "banana");
        aliases.put("apples", "apple"); aliases.put("yoghurt", "yogurt");
        aliases.put("oat", "oats"); aliases.put("slices of bread", "bread");
        return aliases.containsKey(s) ? aliases.get(s) : s;
    }
    public static String unit(String input) {
        String s = input.toLowerCase(Locale.ROOT).trim();
        switch(s) {
            case "gram": case "grams": return "g";
            case "kilogram": case "kilograms": return "kg";
            case "litre": case "litres": case "liter": case "liters": return "l";
            case "millilitre": case "millilitres": case "milliliter": case "milliliters": return "ml";
            case "tablespoon": case "tablespoons": return "tbsp";
            case "teaspoon": case "teaspoons": return "tsp";
            case "piece": case "pieces": case "items": return "item";
            case "slices": return "slice";
            default: return s;
        }
    }
    private static String family(String unit) {
        switch (unit(unit)) {
            case "g": case "kg": return "mass";
            case "ml": case "l": case "tbsp": case "tsp": return "volume";
            default: return unit(unit);
        }
    }
    private static double base(Stock s) {
        switch(unit(s.unit)) {
            case "kg": case "l": return s.quantity * 1000;
            case "tbsp": return s.quantity * 15;
            case "tsp": return s.quantity * 5;
            default: return s.quantity;
        }
    }
    private static Map<String,Double> totals(List<Stock> values) {
        Map<String,Double> result = new HashMap<>();
        for (Stock s : values) {
            if (Double.isNaN(s.quantity) || Double.isInfinite(s.quantity) || s.quantity <= 0)
                throw new IllegalArgumentException("Quantity must be finite and positive");
            String key = name(s.name) + "|" + family(s.unit);
            result.put(key, result.getOrDefault(key, 0.0) + base(s));
        }
        return result;
    }
    public static boolean qualifies(Meal meal, List<Stock> pantry) {
        if (meal.requirements.isEmpty()) return false;
        // Aggregate both sides so duplicate entries cannot hide a shortfall.
        Map<String,Double> available = totals(pantry);
        for (Map.Entry<String,Double> need : totals(meal.requirements).entrySet()) {
            if (available.getOrDefault(need.getKey(), 0.0) + 1e-9 < need.getValue()) return false;
        }
        return true;
    }
}
