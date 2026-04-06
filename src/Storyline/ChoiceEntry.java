package Storyline;

/**
 * One selectable option shown during a CHOICE scene.
 *
 * After the player picks this choice, any scenes in {@code subScenes} play
 * in order before the main scene list continues.
 */
public class ChoiceEntry {

    public final String       label;
    public final int          ppReward;
    public final int          lpReward;
    public final SceneEntry[] subScenes;   // dialogue that plays after this choice is picked

    /** Choice with no follow-up sub-scenes. */
    public ChoiceEntry(String label, int ppReward, int lpReward) {
        this(label, ppReward, lpReward, new SceneEntry[0]);
    }

    /** Choice with follow-up sub-scenes played before advancing. */
    public ChoiceEntry(String label, int ppReward, int lpReward, SceneEntry... subScenes) {
        this.label     = label;
        this.ppReward  = ppReward;
        this.lpReward  = lpReward;
        this.subScenes = subScenes != null ? subScenes : new SceneEntry[0];
    }
}