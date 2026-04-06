package Entities;

public class ActiveEffects {

    private int ppMultiplierPercent  = 0;
    private int lpMultiplierPercent  = 0;
    private int timerBonusSeconds    = 0;
    private int hintsRemaining       = 0;

    public void apply(Item.EffectType type, int value) {
        switch (type) {
            case PP_MULTIPLIER       -> ppMultiplierPercent += value;
            case TIMER_BOOST         -> timerBonusSeconds   += value;
            case HINT_PER_CALL       -> hintsRemaining      += value;
            case LP_MULTIPLIER_DAILY -> lpMultiplierPercent += value;
            case LP_FLAT, NONE       -> {}
        }
    }

    public int scalePP(int raw) {
        return raw + (int)(raw * ppMultiplierPercent / 100.0);
    }

    public int scaleLP(int raw) {
        return raw + (int)(raw * lpMultiplierPercent / 100.0);
    }

    public int     getTimerBonus()     { return timerBonusSeconds; }
    public int     getHintsRemaining() { return hintsRemaining; }
    public boolean hasHint()           { return hintsRemaining > 0; }
    public int     getPpMultiplier()   { return ppMultiplierPercent; }
    public int     getLpMultiplier()   { return lpMultiplierPercent; }

    public void consumeHint() {
        if (hintsRemaining > 0) hintsRemaining--;
    }

    public void resetPerCall() {
        timerBonusSeconds = 0;
        hintsRemaining    = 0;
    }

    public void resetPerDay() {
        ppMultiplierPercent = 0;
        timerBonusSeconds   = 0;
        hintsRemaining      = 0;
        // lpMultiplierPercent NOT reset — Desk Plant persists
    }

    public void reset() {
        ppMultiplierPercent = 0;
        lpMultiplierPercent = 0;
        timerBonusSeconds   = 0;
        hintsRemaining      = 0;
    }
}