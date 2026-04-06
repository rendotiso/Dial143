package Entities;

import java.util.HashMap;
import java.util.Map;

public class Character {
    
    // ── Route character name constants ───────────────────────────────────────
    public static final String AMAYA = "Amaya";
    public static final String ROSARIO = "Rosario";
    public static final String CLOMA = "Cloma";
    public static final String CELERES = "Celeres";
    
    // ── LP storage ───────────────────────────────────────────────────────────
    private Map<String, Integer> lpMap = new HashMap<>();
    
    public void add(String character, int amount) {
        int current = get(character);
        lpMap.put(character, current + amount);
    }
    
    public int get(String character) {
        return lpMap.getOrDefault(character, 0);
    }
    
    public boolean meetsThreshold(String character, int threshold) {
        return get(character) >= threshold;
    }
    
    public boolean anyMeetsThreshold(int threshold) {
        for (int value : lpMap.values()) {
            if (value >= threshold) return true;
        }
        return false;
    }
    
    public void reset() {
        lpMap.clear();
    }
    
    // ── Get all LP for debugging ─────────────────────────────────────────────
    public Map<String, Integer> getAll() {
        return new HashMap<>(lpMap);
    }
}