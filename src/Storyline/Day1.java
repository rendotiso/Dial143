package Storyline;

public class Day1 implements DayInterface {

    // ── Morning ───────────────────────────────────────────────────────────────
    // 0   narrator  – upbeat day
    // 1   narrator  – first job
    // 2   narrator  – pat creases / look at ID
    // 3   identityCreation
    // 4   narrator  – enter building, pride and worry
    // 5   narrator  – sees Rosario approaching
    // 6   dialogue  – ??? "Hello…? Are you the new person?"
    // 7   CHOICE    – respond to Rosario greeting
    // 8   dialogue  – Rosario "Since this is your first day…"
    // 9   narrator  – walking to elevator
    // 10  dialogue  – Rosario "Do you have any questions?"
    // 11  CHOICE    – question for Rosario
    // 12  dialogue  – Rosario "We're here. Welcome…"
    // 13  narrator  – massive room, cubicles, noise
    // 14  dialogue  – Rosario "Your cubicle is here…"

    private static final SceneEntry[] MORNING = {
        // 0
        SceneEntry.narrator(
            "Your day started as normal. However, this day was one of the ones where you were much more upbeat than usual.",
            "StreetMorning.jpg"),
        // 1
        SceneEntry.narrator(
            "It was now time to start your very first job after graduation. It was not an easy one to get hold of, and the starting pay was enough to pay off bills and necessities.",
            "BuildingMorning.jpg"),
        // 2
        SceneEntry.narrator(
            "You pat off any creases from your office wear, and finally took one last look at your company ID.",
            "BuildingMorning.jpg"),
        // 3
        SceneEntry.identityCreation(),
        // 4
        SceneEntry.narrator(
            "Finally, you began to enter the building with both pride and worry. Pride, as you know, you're one of the many with a 9-8 job. Worry, as you may not find any coworkers to get along with.",
            "BuildingMorning.jpg"),
        // 5
        SceneEntry.narrator(
            "You walked around confused, but then saw a man nearing his 20's waiting nearby. He has a strict demeanor masked with a neutral expression. The moment his eyes landed on you, the end of his lips immediately rose to create a kind expression, and he started walking to your location.",
            "MorningElevator.jpg"),
        // 6
        SceneEntry.dialogue("???", "Hello…? Are you the new person I'll be handling?",
            "single:Rosario_Smile", "MorningElevator.jpg"),
        // 7 — CHOICE
        SceneEntry.choice(),
        // 8
        SceneEntry.dialogue("Rosario",
            "Since this is your first day, I'm assigned to help you work around here.",
            "single:Rosario_Casual", "MorningElevator.jpg"),
        // 9
        SceneEntry.narrator(
            "As you walk together to the elevator, you curiously look up to Rosario.",
            "MorningElevator.jpg"),
        // 10
        SceneEntry.dialogue("Rosario", "Do you have any questions?",
            "single:Rosario_Casual", "MorningElevator.jpg"),
        // 11 — CHOICE
        SceneEntry.choice(),
        // 12
        SceneEntry.dialogue("Rosario",
            "We're here. Welcome to Khai G. Call Center Agency, Mr./Ms. {name}.",
            "single:Rosario_Smile", "MorningOffice.jpg"),
        // 13
        SceneEntry.narrator(
            "You stepped into a massive room filled with cubicles. Many overbearing sounds filled your ears: keyboard clicks, phones ringing, and countless conversations spoken. You have no choice but to adapt to this environment.",
            "MorningOffice.jpg"),
        // 14
        SceneEntry.dialogue("Rosario",
            "Your cubicle is here. Please sit down, and I'll guide you through the process.",
            "single:Rosario_Casual", "MorningOffice.jpg"),
    };

    // ── Evening ───────────────────────────────────────────────────────────────
    // 0   dialogue  – {name} exhausted
    // 1   dialogue  – ??? "PC? Is that you?!"
    // 2   narrator  – recognises Amaya
    // 3   dialogue  – {name} "It can't be? Amaya?"
    // 4   dialogue  – Amaya excited
    // 5   dialogue  – Amaya offers gift
    // 6   narrator  – transition to 6/70
    // 7   dialogue  – {name} "6/70? I didn't know…"
    // 8   dialogue  – Amaya "It's one of the many perks…"
    // 9   dialogue  – Amaya "Here, a coffee!"
    // 10  CHOICE    – respond to coffee
    // 11  dialogue  – Amaya "Man, I never thought I'd see you…"
    // 12  dialogue  – {name} feels like high school
    // 13  dialogue  – Amaya "I miss the old times… any questions?"
    // 14  CHOICE    – question for Amaya
    // 15  dialogue  – Amaya farewell

    private static final SceneEntry[] EVENING = {
        // 0
        SceneEntry.dialogue("{name}",
            "Well, I'm exhausted. I never thought I'd handle that many calls.",
            "none", "EveningOffice.jpg"),
        // 1
        SceneEntry.dialogue("???", "PC? Is that you?!",
            "none", "EveningOffice.jpg"),
        // 2
        SceneEntry.narrator(
            "A familiar voice enters your ears. It must've been your childhood friend, Amaya.",
            "EveningOffice.jpg"),
        // 3
        SceneEntry.dialogue("{name}", "It can't be? Amaya?",
            "none", "EveningOffice.jpg"),
        // 4
        SceneEntry.dialogue("Amaya",
            "{name}! You got the job! Why didn't you tell me!",
            "single:Amaya_Smile", "EveningOffice.jpg"),
        // 5
        SceneEntry.dialogue("Amaya",
            "Hey, since it's your first day, I'll buy ya a gift? How about that?",
            "single:Amaya_Smile", "EveningOffice.jpg"),
        // 6
        SceneEntry.narrator(
            "You and Amaya make your way outside to a nearby convenience store called 6/70.",
            "StreetEvening.jpg"),
        // 7
        SceneEntry.dialogue("{name}",
            "6/70? I didn't know it was close to our company.",
            "none", "ConvenienceStore.jpg"),
        // 8
        SceneEntry.dialogue("Amaya",
            "It's one of the many perks of working around here.",
            "single:Amaya_Casual", "ConvenienceStore.jpg"),
        // 9
        SceneEntry.dialogue("Amaya",
            "Here, a coffee! Every office worker needs one!",
            "single:Amaya_Smile", "ConvenienceStore.jpg"),
        // 10 — CHOICE
        SceneEntry.choice(),
        // 11
        SceneEntry.dialogue("Amaya",
            "Man, I never thought I'd see you back here in all of these places.",
            "single:Amaya_Casual", "ConvenienceStore.jpg"),
        // 12
        SceneEntry.dialogue("{name}",
            "Yep, I felt like we're back in High School. Buying food here late at night and just talking.",
            "none", "ConvenienceStore.jpg"),
        // 13
        SceneEntry.dialogue("Amaya",
            "I miss the old times. Since it's your first day, do you have any questions for me?",
            "single:Amaya_Casual", "ConvenienceStore.jpg"),
        // 14 — CHOICE
        SceneEntry.choice(),
        // 15
        SceneEntry.dialogue("Amaya",
            "Anyways, I'm glad to see you again! Since we work on the same floor, don't forget to chat me up! I've missed you, man! I'll head home now. See you later, {name}!",
            "single:Amaya_Smile", "ConvenienceStore.jpg"),
    };

    // ── Choices ───────────────────────────────────────────────────────────────

    // MORNING 7 — respond to Rosario's greeting
    private static final ChoiceEntry[] CHOICES_M7 = {
        new ChoiceEntry("Who are you?", 0, 3,
            SceneEntry.dialogue("{name}", "Sorry? Who are you?",
                "none", "MorningElevator.jpg"),
            SceneEntry.dialogue("Rosario",
                "I'm Rosario, your supervisor. I assume that your name is {name}?",
                "single:Rosario_Smile", "MorningElevator.jpg")
        ),
        new ChoiceEntry("The one and only.", 0, 0,
            SceneEntry.dialogue("{name}", "I'm {name}, the one and only.",
                "none", "MorningElevator.jpg"),
            SceneEntry.dialogue("Rosario",
                "Um, okay then… I'm Rosario, your supervisor and team leader. Nice to meet you too.",
                "single:Rosario_Casual", "MorningElevator.jpg")
        ),
    };

    // MORNING 11 — question for Rosario in elevator
    private static final ChoiceEntry[] CHOICES_M11 = {
        new ChoiceEntry("What's my role?", 0, 0,
            SceneEntry.dialogue("{name}", "What exactly is my role here?",
                "none", "MorningElevator.jpg"),
            SceneEntry.dialogue("Rosario",
                "You'll be working as an Inbound agent. You'll handle customer calls from inquiries to technical support.",
                "single:Rosario_Casual", "MorningElevator.jpg")
        ),
        new ChoiceEntry("What's the team like?", 0, 0,
            SceneEntry.dialogue("{name}", "So what's the team like? Who will I be working with?",
                "none", "MorningElevator.jpg"),
            SceneEntry.dialogue("Rosario",
                "They're nice and competent. The last newcomer we had was someone who got hired 2 months ago.",
                "single:Rosario_Casual", "MorningElevator.jpg")
        ),
    };

    // EVENING 10 — respond to coffee
    private static final ChoiceEntry[] CHOICES_E10 = {
        new ChoiceEntry("Tease", 0, 3,
            SceneEntry.dialogue("{name}",
                "You want me to die from a heart attack? You know I'm not a workaholic like you.",
                "none", "ConvenienceStore.jpg"),
            SceneEntry.dialogue("Amaya",
                "I try to be a nice person for once, and you return it to me like this! I'm hurt!",
                "single:Amaya_Casual", "ConvenienceStore.jpg")
        ),
        new ChoiceEntry("Grateful", 0, 5,
            SceneEntry.dialogue("{name}", "Thank you! I actually needed this.",
                "none", "ConvenienceStore.jpg"),
            SceneEntry.dialogue("Amaya",
                "Whoa? You're grateful for once. You're always welcome, by the way.",
                "single:Amaya_Smile", "ConvenienceStore.jpg")
        ),
    };

    // EVENING 14 — question for Amaya
    private static final ChoiceEntry[] CHOICES_E14 = {
        new ChoiceEntry("Who is Sir Rosario?", 8, 0,    // +8 tracked as PP for Rosario affinity
            SceneEntry.dialogue("{name}",
                "I'm curious about our Team Leader. Do you know anything about him?",
                "none", "ConvenienceStore.jpg"),
            SceneEntry.dialogue("Amaya",
                "Oh, him? He's actually very kind! We hang out sometimes after our shifts since we go the same route back home.",
                "single:Amaya_Casual", "ConvenienceStore.jpg"),
            SceneEntry.dialogue("Amaya",
                "But don't tell anyone — I'm kind of scared of him. He's very strict about our job. Treats it like it's his lifeline.",
                "single:Amaya_Casual", "ConvenienceStore.jpg")
        ),
        new ChoiceEntry("How long have you worked here?", 0, 8,
            SceneEntry.dialogue("{name}",
                "How long have you worked here? Seems like you've been here longer than I have.",
                "none", "ConvenienceStore.jpg"),
            SceneEntry.dialogue("Amaya",
                "I got the job actually 2 months ago. I'm just here for the job experience before I pursue a profession using my IT bachelor's.",
                "single:Amaya_Casual", "ConvenienceStore.jpg"),
            SceneEntry.dialogue("Amaya",
                "It felt like forever to do any job hunting. I don't want to experience it again…",
                "single:Amaya_Sad", "ConvenienceStore.jpg")
        ),
    };

    // ── DayInterface implementation ───────────────────────────────────────────

    @Override public SceneEntry[] getMorningScenes() { return MORNING; }
    @Override public SceneEntry[] getEveningScenes() { return EVENING; }

    @Override
    public ChoiceEntry[] getChoicesForScene(int sceneIndex, String segment) {
        return switch (segment) {
            case "MORNING" -> switch (sceneIndex) {
                case 7  -> CHOICES_M7;
                case 11 -> CHOICES_M11;
                default -> null;
            };
            case "EVENING" -> switch (sceneIndex) {
                case 10 -> CHOICES_E10;
                case 14 -> CHOICES_E14;
                default -> null;
            };
            default -> null;
        };
    }
}