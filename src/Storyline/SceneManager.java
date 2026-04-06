package Storyline;

import javax.swing.SwingUtilities;
import java.util.ArrayDeque;
import java.util.Deque;

public class SceneManager {

    // ── Delegate ──────────────────────────────────────────────────────────────

    public interface SceneManagerDelegate {
        void showBackground(String filename);
        void showSprite(String spriteSpec);
        void showDialogue(String speaker, String text);
        void showNarrator(String text);
        void showChoices(ChoiceEntry[] choices, Runnable onChosen);
        void showIdentityCreation(Runnable onComplete);
        void addPP(int amount);
        void addLP(int amount);
        void onMorningComplete();
        void onEveningComplete();
        void waitForPreload();
    }

    // ── Segment ───────────────────────────────────────────────────────────────

    public enum Segment { MORNING, EVENING }

    // ── State ─────────────────────────────────────────────────────────────────

    private DayInterface         dayScript;
    private SceneManagerDelegate delegate;
    private Segment              segment    = Segment.MORNING;
    private int                  sceneIndex = 0;

    private final Deque<SceneEntry> subSceneQueue = new ArrayDeque<>();

    // ── Constructor ───────────────────────────────────────────────────────────

    public SceneManager(DayInterface dayScript, SceneManagerDelegate delegate) {
        this.dayScript = dayScript;
        this.delegate  = delegate;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void setDayScript(DayInterface dayScript) {
        this.dayScript = dayScript;
    }

    public void start(Segment segment, int startIndex) {
        this.segment    = segment;
        this.sceneIndex = startIndex;
        subSceneQueue.clear();
        delegate.waitForPreload();
        loadScene(sceneIndex);
    }

    /** Called on every player click. Drains sub-scenes before advancing main index. */
    public void advanceScene() {
        if (!subSceneQueue.isEmpty()) {
            playEntry(subSceneQueue.poll());
        } else {
            sceneIndex++;
            loadScene(sceneIndex);
        }
    }

    /** Called by InteractionPanel after a choice is selected. */
    public void onChoicePicked(ChoiceEntry chosen) {
        delegate.addPP(chosen.ppReward);
        delegate.addLP(chosen.lpReward);

        subSceneQueue.clear();
        if (chosen.subScenes != null) {
            for (SceneEntry s : chosen.subScenes) subSceneQueue.add(s);
        }

        if (!subSceneQueue.isEmpty()) {
            playEntry(subSceneQueue.poll());
        } else {
            SwingUtilities.invokeLater(this::advanceScene);
        }
    }

    public int     getCurrentSceneIndex() { return sceneIndex; }
    public Segment getCurrentSegment()    { return segment;    }

    // ── Internal ──────────────────────────────────────────────────────────────

    private void loadScene(int index) {
        SceneEntry[] scenes = getCurrentScenes();

        if (index >= scenes.length) {
            if (segment == Segment.MORNING) delegate.onMorningComplete();
            else                            delegate.onEveningComplete();
            return;
        }

        playEntry(scenes[index]);
    }

    private void playEntry(SceneEntry scene) {
        switch (scene.type) {

            case SceneEntry.TYPE_NARRATOR -> {
                delegate.showBackground(scene.background);
                delegate.showSprite("none");
                delegate.showNarrator(scene.text);
            }

            case SceneEntry.TYPE_DIALOGUE -> {
                delegate.showBackground(scene.background);
                delegate.showSprite(scene.spriteSpec);
                delegate.showDialogue(scene.speaker, scene.text);
            }

            case SceneEntry.TYPE_IDENTITY -> {
                delegate.showIdentityCreation(this::advanceScene);
            }

            case SceneEntry.TYPE_CHOICE -> {
                ChoiceEntry[] choices = dayScript.getChoicesForScene(sceneIndex, segment.name());
                if (choices == null) {
                    advanceScene();
                    return;
                }
                delegate.showChoices(choices, () ->
                    SwingUtilities.invokeLater(this::advanceScene));
            }

            default -> advanceScene();
        }
    }

    private SceneEntry[] getCurrentScenes() {
        return switch (segment) {
            case MORNING -> dayScript.getMorningScenes();
            case EVENING -> dayScript.getEveningScenes();
        };
    }
}