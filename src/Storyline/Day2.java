package Storyline;

public class Day2 implements DayInterface {

    // ── Morning ───────────────────────────────────────────────────────────────

    private static final SceneEntry[] MORNING = {
        // 0
        SceneEntry.narrator(
            "You went off to work earlier than usual. After the previous day's interactions, work seemed like more of a breeze than you expected it to be.",
            "StreetMorning.jpg"),
        // 1
        SceneEntry.narrator(
            "Your body still aches from the first day, but something feels different today. Lighter. Maybe it's the thought of seeing familiar faces.",
            "StreetMorning.jpg"),
        // 2
        SceneEntry.narrator(
            "You decide to grab breakfast first before heading in.",
            "ConvenienceStore.jpg"),
        // 3
        SceneEntry.dialogue("Cloma", "How's your first day of work?",
            "single:Cloma_Casual", "ConvenienceStore.jpg"),
        // 4 — CHOICE (who are you? / hello)
        SceneEntry.choice(),
        // 5 — after choice, Cloma continues
        SceneEntry.choice(), // second choice (noted / embarrassed)
        // 6
        SceneEntry.narrator(
            "After the short conversation, you finish your breakfast and head to the office building across the street. The morning air feels cooler now, but something about Cloma's warmth stays with you.",
            "BuildingMorning.jpg"),
        // 7
        SceneEntry.narrator(
            "You settle into your cubicle and log into your computer. Everything seems normal at first, until the screen freezes. Then it goes black and spams you with error messages.",
            "MorningOffice.jpg"),
        // 8
        SceneEntry.narrator(
            "You try restarting. Nothing. You try again. Still nothing.",
            "MorningOffice.jpg"),
        // 9
        SceneEntry.dialogue("{name}", "Of course. Second day, and my computer already hates me.",
            "none", "MorningOffice.jpg"),
        // 10
        SceneEntry.narrator(
            "With no other choice, you get up and head toward the IT department.",
            "MorningOffice.jpg"),
        // 11
        SceneEntry.narrator(
            "The IT department is quieter than your cubicle area. The only sounds are the hum of computers and the occasional click of a keyboard. In the corner, you spot him.",
            "ITDepartment.jpg"),
        // 12
        SceneEntry.narrator(
            "A man with a nametag, 'Celeres,' looks sleepy and nonchalant, barely paying attention to anything. His eyes are half-lidded, and he stares at his screen like it personally offended him.",
            "ITDepartment.jpg"),
        // 13
        SceneEntry.narrator(
            "Celeres finally notices you standing there. His expression doesn't change much, but he straightens slightly.",
            "ITDepartment.jpg"),
        // 14
        SceneEntry.dialogue("Celeres", "You look new. Are you a new hire?",
            "single:Celeres_Casual", "ITDepartment.jpg"),
        // 15 — CHOICE (ask for help directly / just inform the issue)
        SceneEntry.choice(),
    };

    // ── Evening ───────────────────────────────────────────────────────────────

    private static final SceneEntry[] EVENING = {
        // 0
        SceneEntry.narrator(
            "The evening hours arrived and everyone quietly packed up their items to leave. As you were ready to join the messy crowd of employees, you accidentally bumped into a man's chest.",
            "EveningOffice.jpg"),
        // 1
        SceneEntry.dialogue("Celeres", "..!",
            "single:Celeres_Surprised", "EveningOffice.jpg"),
        // 2
        SceneEntry.dialogue("{name}", "Sorry! I didn't see you.",
            "none", "EveningOffice.jpg"),
        // 3
        SceneEntry.dialogue("Celeres", "It's alright. PC, was it?",
            "single:Celeres_Casual", "EveningOffice.jpg"),
        // 4
        SceneEntry.dialogue("{name}", "(to self) I don't think I have told him my name before.",
            "none", "EveningOffice.jpg"),
        // 5 — CHOICE (question / surprised)
        SceneEntry.choice(),
        // 6
        SceneEntry.dialogue("Celeres", "Also, you're the only one who came to IT today that didn't microwave fish in the break room. So. Memorable.",
            "single:Celeres_Casual", "EveningOffice.jpg"),
        // 7
        SceneEntry.dialogue("{name}", "Low bar.",
            "none", "EveningOffice.jpg"),
        // 8
        SceneEntry.dialogue("Celeres", "Very low. Watch where you're going next time, PC.",
            "single:Celeres_Casual", "EveningOffice.jpg"),
        // 9
        SceneEntry.dialogue("{name}", "I'll try. Goodnight, Celeres.",
            "none", "EveningOffice.jpg"),
        // 10
        SceneEntry.dialogue("Celeres", "(already walking) Night.",
            "single:Celeres_Casual", "EveningOffice.jpg"),
    };

    // ── Choices ───────────────────────────────────────────────────────────────

    // MORNING 4 — respond to Cloma's greeting
    private static final ChoiceEntry[] CHOICES_M4 = {
        new ChoiceEntry("Who are you?", 0, 0,
            SceneEntry.dialogue("{name}", "Um, sorry? But who are you again?",
                "none", "ConvenienceStore.jpg"),
            SceneEntry.dialogue("Cloma",
                "Oh! You forgot me already. So I will introduce myself again! By the way, I am Cloma, the HR here in the company.",
                "single:Cloma_Smile", "ConvenienceStore.jpg")
        ),
        new ChoiceEntry("Hello!", 5, 0,
            SceneEntry.dialogue("{name}", "Oh, hello, Miss Cloma! I'm the one you hired. Thanks for that!",
                "none", "ConvenienceStore.jpg"),
            SceneEntry.dialogue("Cloma",
                "You remembered me! It seems like everything went well.",
                "single:Cloma_Smile", "ConvenienceStore.jpg"),
            SceneEntry.dialogue("{name}",
                "My first day was nervous, but somehow I slowly adjusted to the environment here because they are so approachable.",
                "none", "ConvenienceStore.jpg"),
            SceneEntry.dialogue("Cloma",
                "Glad to hear that! If you have something to ask in terms of the company's policies, you can go to my office.",
                "single:Cloma_Casual", "ConvenienceStore.jpg")
        ),
    };

    // MORNING 5 — response to Cloma's offer (after both branches converge)
    private static final ChoiceEntry[] CHOICES_M5 = {
        new ChoiceEntry("Noted!", 3, 0,
            SceneEntry.dialogue("{name}", "Noted, Miss Cloma! Thank you so much! I really appreciate you.",
                "none", "ConvenienceStore.jpg"),
            SceneEntry.dialogue("Cloma",
                "No worries! Well, I'll be heading out now! See you around PC! Take care of yourself today, alright? Don't overwork yourself.",
                "single:Cloma_Smile", "ConvenienceStore.jpg")
        ),
        new ChoiceEntry("Embarrassed", 2, 0,
            SceneEntry.dialogue("{name}", "I'm sorry, but I will just ask my coworkers about it so that I won't bother you.",
                "none", "ConvenienceStore.jpg"),
            SceneEntry.dialogue("Cloma",
                "If you say so. But remember, I don't consider questions a bother. That's literally my job. See you around, PC.",
                "single:Cloma_Casual", "ConvenienceStore.jpg"),
            SceneEntry.dialogue("{name}", "See you around, Miss Cloma! Thank you!",
                "none", "ConvenienceStore.jpg")
        ),
    };

    // MORNING 15 — response to Celeres in IT department
    private static final ChoiceEntry[] CHOICES_M15 = {
        new ChoiceEntry("Ask for help directly", 5, 0,
            SceneEntry.dialogue("{name}", "I am. I heard I'll ask for help here on fixing my computer. The computer keeps freezing.",
                "none", "ITDepartment.jpg"),
            SceneEntry.dialogue("{name}", "If it's alright. Can you check it?",
                "none", "ITDepartment.jpg"),
            SceneEntry.dialogue("Celeres", "Alright. First, did you restart the computer?",
                "single:Celeres_Casual", "ITDepartment.jpg"),
            SceneEntry.dialogue("{name}", "Yes, I did.",
                "none", "ITDepartment.jpg"),
            SceneEntry.dialogue("Celeres", "Okay then.",
                "single:Celeres_Casual", "ITDepartment.jpg"),
            SceneEntry.narrator(
                "Celeres follows you to your workstation. He taps a few keys, runs some diagnostics, and within minutes, the issue is resolved.",
                "MorningOffice.jpg"),
            SceneEntry.dialogue("Celeres", "It's fixed. Outdated drivers. Workstation 420, one of the old ones.",
                "single:Celeres_Casual", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "Thank you, Sir Celeres!",
                "none", "MorningOffice.jpg")
        ),
        new ChoiceEntry("Just inform the issue", 2, 0,
            SceneEntry.dialogue("{name}", "Ah, yes. I am. My computer keeps freezing.",
                "none", "ITDepartment.jpg"),
            SceneEntry.dialogue("Celeres", "Freezing? Workstation number?",
                "single:Celeres_Casual", "ITDepartment.jpg"),
            SceneEntry.dialogue("{name}", "420.",
                "none", "ITDepartment.jpg"),
            SceneEntry.dialogue("Celeres", "Okay then. I'll come by. Eventually.",
                "single:Celeres_Casual", "ITDepartment.jpg"),
            SceneEntry.dialogue("{name}", "I'll leave now, then.",
                "none", "ITDepartment.jpg"),
            SceneEntry.narrator(
                "A brief silence enters the department after you immediately run from the awkward encounter, not daring to last another second there.",
                "ITDepartment.jpg"),
            SceneEntry.dialogue("Celeres", "…",
                "single:Celeres_Casual", "ITDepartment.jpg"),
            SceneEntry.dialogue("Celeres", "Am I that intimidating?",
                "single:Celeres_Sad", "ITDepartment.jpg"),
            SceneEntry.narrator(
                "Thirty minutes passed, and Celeres appeared at your cubicle, just like he promised. He looks the same. Like he never moved.",
                "MorningOffice.jpg"),
            SceneEntry.narrator(
                "He taps a few keys, runs some diagnostics, and within minutes, the issue is resolved.",
                "MorningOffice.jpg"),
            SceneEntry.dialogue("Celeres", "It's fixed. Outdated drivers.",
                "single:Celeres_Casual", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "Um. Thank you, Sir Celeres. Sorry for bothering you earlier. You seemed busy.",
                "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Celeres", "… I wasn't busy. Don't worry.",
                "single:Celeres_Casual", "MorningOffice.jpg"),
            SceneEntry.dialogue("Celeres", "…",
                "single:Celeres_Casual", "MorningOffice.jpg"),
            SceneEntry.dialogue("Celeres", "Also, Celeres is fine.",
                "single:Celeres_Casual", "MorningOffice.jpg"),
            SceneEntry.dialogue("Celeres", "Just tell me directly if any issues come up.",
                "single:Celeres_Casual", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "I will. Thanks again.",
                "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Celeres", "Yeah. Okay. Bye..",
                "single:Celeres_Casual", "MorningOffice.jpg"),
            SceneEntry.narrator(
                "He leaves faster than you've ever seen him move. You could swear he was almost... embarrassed?",
                "MorningOffice.jpg")
        ),
    };

    // EVENING 5 — response to Celeres knowing PC's name
    private static final ChoiceEntry[] CHOICES_E5 = {
        new ChoiceEntry("Question", 0, 0,
            SceneEntry.dialogue("{name}", "How'd you know? I think I have never told you my name before.",
                "none", "EveningOffice.jpg"),
            SceneEntry.dialogue("Celeres",
                "Cloma told me about you. She's a college friend. Started talking for who knows how long on how it's great to have a newcomer she can mentor.",
                "single:Celeres_Casual", "EveningOffice.jpg")
        ),
        new ChoiceEntry("Surprised", 3, 0,
            SceneEntry.dialogue("{name}", "You've heard about me before?",
                "none", "EveningOffice.jpg"),
            SceneEntry.dialogue("Celeres",
                "You know Cloma? She's my college friend. She went up to me hours ago, talking about you. That's when I learned you and the person she mentioned were one and the same.",
                "single:Celeres_Casual", "EveningOffice.jpg"),
            SceneEntry.dialogue("{name}", "Ah, that makes a lot of sense.",
                "none", "EveningOffice.jpg"),
            SceneEntry.dialogue("Celeres", "That and your station number. 420. I remember numbers.",
                "single:Celeres_Casual", "EveningOffice.jpg")
        ),
    };

    // ── DayInterface implementation ───────────────────────────────────────────

    @Override
    public SceneEntry[] getMorningScenes() { return MORNING; }

    @Override
    public SceneEntry[] getEveningScenes() { return EVENING; }

    @Override
    public ChoiceEntry[] getChoicesForScene(int sceneIndex, String segment) {
        return switch (segment) {
            case "MORNING" -> switch (sceneIndex) {
                case 4  -> CHOICES_M4;
                case 5  -> CHOICES_M5;
                case 15 -> CHOICES_M15;
                default -> null;
            };
            case "EVENING" -> switch (sceneIndex) {
                case 5 -> CHOICES_E5;
                default -> null;
            };
            default -> null;
        };
    }
}