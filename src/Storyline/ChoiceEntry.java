package Storyline;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChoiceEntry {
    public final String              label;
    public final int                 ppReward;
    public final Map<String, Integer> lpRewards;   // character → LP gain
    public final SceneEntry[]        subScenes;

    // ── Backwards-compatible: single character LP ──────────────────────────

    public ChoiceEntry(String label, int ppReward, int lpReward) {
        this(label, ppReward, Collections.emptyMap(), new SceneEntry[0]);
    }

    public ChoiceEntry(String label, int ppReward, int lpReward, String lpCharacter) {
        this(label, ppReward, singleLP(lpCharacter, lpReward), new SceneEntry[0]);
    }

    public ChoiceEntry(String label, int ppReward, int lpReward, SceneEntry... subScenes) {
        this(label, ppReward, Collections.emptyMap(), subScenes);
    }

    public ChoiceEntry(String label, int ppReward, int lpReward,
                       String lpCharacter, SceneEntry... subScenes) {
        this(label, ppReward, singleLP(lpCharacter, lpReward), subScenes);
    }

    // ── New: multi-character LP ────────────────────────────────────────────

    public ChoiceEntry(String label, int ppReward,
                       Map<String, Integer> lpRewards, SceneEntry... subScenes) {
        this.label     = label;
        this.ppReward  = ppReward;
        this.lpRewards = Collections.unmodifiableMap(lpRewards);
        this.subScenes = subScenes != null ? subScenes : new SceneEntry[0];
    }

    // ── Helper ─────────────────────────────────────────────────────────────

    private static Map<String, Integer> singleLP(String character, int amount) {
        if (character == null) return Collections.emptyMap();
        Map<String, Integer> m = new HashMap<>();
        m.put(character, amount);
        return m;
    }

    /** Convenience builder for inline multi-LP declarations. */
    public static Map<String, Integer> lp(Object... pairs) {
        if (pairs.length % 2 != 0) throw new IllegalArgumentException("lp() needs even args");
        Map<String, Integer> m = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            m.put((String) pairs[i], (Integer) pairs[i + 1]);
        }
        return m;
    }
}