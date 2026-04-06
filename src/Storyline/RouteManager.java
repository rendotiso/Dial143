package Storyline;

import Entities.Character;

/**
 * Base class for all character routes.
 * Manages per-character LP, route state, and route-specific logic.
 */
public abstract class RouteManager {

    public static final int ROUTE_LP_THRESHOLD = 15; // minimum LP to unlock a route option

    // ── Route identification ──────────────────────────────────────────────────
    private final String routeName;
    private final int lpThresholdGood;
    private final int lpThresholdNeutral;
    
    // ── Per-character LP tracking (shared across all potential routes) ────────
    private final Character characterLP = new Character();
    
    // ── Active route state ────────────────────────────────────────────────────
    private RouteManager activeRoute = null;
    private String activeCharacter = null;

    // ── Constructor ───────────────────────────────────────────────────────────
    
    public RouteManager(String routeName, int lpThresholdGood, int lpThresholdNeutral) {
        this.routeName = routeName;
        this.lpThresholdGood = lpThresholdGood;
        this.lpThresholdNeutral = lpThresholdNeutral;
    }
    
    // ── Getters for route properties ──────────────────────────────────────────
    
    public String getRouteName() { return routeName; }
    public int getLpThresholdGood() { return lpThresholdGood; }
    public int getLpThresholdNeutral() { return lpThresholdNeutral; }
    
    // ── Abstract methods (implemented by each character route) ────────────────
    
    /** Returns the DayInterface script for a specific day in this route. */
    public abstract DayInterface getDayScript(int day);
    
    /** Returns the total number of days in this route (usually 7). */
    public abstract int getTotalDays();
    
    /** Returns the ending scene based on final LP. */
    public abstract SceneEntry getEndingScene(int finalLP, int currentDay, SceneManager.Segment segment);
    
    /** Returns whether this route has started (Love Meter becomes visible). */
    public boolean isRouteStarted(int day, SceneManager.Segment segment) {
        return day >= 3 && segment == SceneManager.Segment.EVENING;
    }
    
    // ── Per-character LP management (static-like, shared across all) ──────────
    
    public Character getCharacterLP() { return characterLP; }
    
    /** Add LP to a specific character. Used by choices with lpCharacter set. */
    public void addCharacterLP(String character, int amount) {
        characterLP.add(character, amount);
    }
    
    /** Returns LP for a specific character. */
    public int getLPForCharacter(String character) {
        return characterLP.get(character);
    }
    
    /** Returns LP of the currently active route's character. */
    public int getActiveLP() {
        return activeCharacter != null ? characterLP.get(activeCharacter) : 0;
    }
    
    // ── Active route management ───────────────────────────────────────────────
    
    public boolean hasActiveRoute() { return activeRoute != null; }
    public RouteManager getActiveRoute() { return activeRoute; }
    public String getActiveCharacter() { return activeCharacter; }
    
    /**
     * Called when the player picks a character route on Day 3 Evening.
     * Returns the Day 3 script for that route so the scene can continue.
     */
    public DayInterface selectRoute(RouteManager route, String characterName) {
        this.activeRoute = route;
        this.activeCharacter = characterName;
        return route.getDayScript(3);
    }
    
    public void clearRoute() {
        activeRoute = null;
        activeCharacter = null;
    }
    
    // ── Route unlock logic ────────────────────────────────────────────────────
    
    /** True if the player has enough LP for this character's route option. */
    public boolean isRouteUnlocked(String character) {
        return characterLP.meetsThreshold(character, ROUTE_LP_THRESHOLD);
    }
    
    /** True if NO character meets the threshold — secret "No one" option appears. */
    public boolean allRoutesLocked() {
        return !characterLP.anyMeetsThreshold(ROUTE_LP_THRESHOLD);
    }
    
    // ── Reset ─────────────────────────────────────────────────────────────────
    
    public void reset() {
        characterLP.reset();
        activeRoute = null;
        activeCharacter = null;
    }
}