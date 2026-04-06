package Storyline;

/**
 * A single scene entry: who speaks, what they say, which sprite(s) to show,
 * and which background to display.
 */
public class SceneEntry {

    public static final String TYPE_DIALOGUE = "DIALOGUE";
    public static final String TYPE_CHOICE   = "CHOICE";
    public static final String TYPE_IDENTITY = "IDENTITY_CREATION";
    public static final String TYPE_NARRATOR = "NARRATOR";
    public static final String TYPE_ROUTE    = "ROUTE";

    public final String type;
    public final String speaker;
    public final String text;
    public final String spriteSpec;
    public final String background;

    private SceneEntry(String type, String speaker, String text,
                       String spriteSpec, String background) {
        this.type       = type;
        this.speaker    = speaker;
        this.text       = text;
        this.spriteSpec = spriteSpec;
        this.background = background;
    }

    public static SceneEntry dialogue(String speaker, String text,
                                      String spriteSpec, String background) {
        return new SceneEntry(TYPE_DIALOGUE, speaker, text, spriteSpec, background);
    }

    public static SceneEntry narrator(String text, String background) {
        return new SceneEntry(TYPE_NARRATOR, "Narrator", text, "none", background);
    }

    public static SceneEntry choice() {
        return new SceneEntry(TYPE_CHOICE, "", "", "", "");
    }

    public static SceneEntry identityCreation() {
        return new SceneEntry(TYPE_IDENTITY, "", "", "", "");
    }
    
    public static SceneEntry routeSelection() {
        return new SceneEntry(TYPE_ROUTE, "", "", "", "");
    }
}