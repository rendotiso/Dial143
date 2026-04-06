package Storyline;

import Entities.Character;

public class Day3 implements DayInterface {

    private static final SceneEntry[] MORNING = {
        //0
        SceneEntry.narrator("The morning sun barely filters through the office windows. The building is already alive with quiet energy.", "MorningElevator.jpg"),
        //1
        SceneEntry.dialogue("{name}", "These floors seem to never end.", "none", "MorningElevator.jpg"),
       // 2
        SceneEntry.narrator("You muttered to yourself quietly. Thanks to the company floor plan photo Amaya sent you a day before, you navigated the company floors easily this time.", "MorningElevator.jpg"),
        //3
        SceneEntry.narrator("You head to the break room to have a quick snack before you start your later shift hours.", "BreakRoom.jpg"),
       //4
        SceneEntry.narrator("At the break room. As you were snacking on a cheap steamed bun, Miss Cloma was seen walking towards the break room. Many men and women alike view her with awe.", "BreakRoom.jpg"),
        //5
        SceneEntry.narrator("Miss Cloma, without a care for all the attention, sees you, and her face brightens at a familiar face.", "BreakRoom.jpg"),
        //6
        SceneEntry.dialogue("Cloma", "Good morning, {name}! Ready to start your day?", "single:Cloma_Smile", "BreakRoom.jpg"),
        SceneEntry.choice(), // MORNING 7
        //8
        SceneEntry.narrator("The break room door opens quietly. Sir Celeres slips in, heading straight for the coffee machine. He hasn't noticed you yet.", "BreakRoom.jpg"),
       //9
        SceneEntry.dialogue("{name}", "Ah! It's Sir Celeres, should I say hi?", "none", "BreakRoom.jpg"),
        SceneEntry.choice(), // MORNING 10
       //11
        SceneEntry.narrator("Rosario and Amaya are seen walking together. Rosario shows an uncharacteristic smile towards Amaya, who is simply listening attentively.", "BreakRoom.jpg"),
        //12
        SceneEntry.narrator("Amaya, who looks around her environment as she walks, sees your face and shouts your name to greet you.", "BreakRoom.jpg"),
        //13
        SceneEntry.narrator("Rosario questions Amaya's behavior and goes to check where she is looking. However, instead of your face, he sees Celeres. Rosario, suddenly, masks a face with unusual anger.", "BreakRoom.jpg"),
       //14
        SceneEntry.dialogue("Rosario", "Celeres.", "single:Rosario_Mad", "BreakRoom.jpg"),
        //15
        SceneEntry.dialogue("Celeres", "… Hi Rosario.", "single:Celeres_Sad", "BreakRoom.jpg"),
        //16
        SceneEntry.dialogue("Amaya", "Oh, not again.", "single:Amaya_Sad", "BreakRoom.jpg"),
       //17
        SceneEntry.dialogue("Rosario", "Good morning, {name}", "single:Rosario_Smile", "BreakRoom.jpg"),
       //18
        SceneEntry.narrator("Even you can deliberately tell he purposely ignored Celeres by gretting you.", "BreakRoom.jpg"),
        //19
        SceneEntry.dialogue("{name}", "Good morning, sir.", "none", "BreakRoom.jpg"),
        //20
        SceneEntry.dialogue("{name}", "Should I pry?", "none", "BreakRoom.jpg"),
        //21
        SceneEntry.choice(), // MORNING 21
       //22
        SceneEntry.narrator("After the sudden interaction between Celeres and Rosario, the room remains cold and quiet. No one spoke up, nor did they bother to cut the tension in the room.", "BreakRoom.jpg"),
        //23
        SceneEntry.narrator("Cloma returns with her desired snack and drink: an iced coffee and some crackers. Whatever eased-up stance "
                + "she had before disappears at the sudden appearance of a new company.", "BreakRoom.jpg"),
      //24
        SceneEntry.dialogue("Cloma", "Good morning, Celeres, Amaya, and Sir Rosario!", "single:Cloma_Smile", "BreakRoom.jpg"),
        //25
        SceneEntry.dialogue("Celeres", "Hi Cloma. Seems like you're here early once again.", "single:Celeres_Smile", "BreakRoom.jpg"),
        //26
        SceneEntry.dialogue("Cloma", "It's always best to be punctual. I'm surprised you're here, Earl, too.", "single:Cloma_Casual", "BreakRoom.jpg"),
        //27
        SceneEntry.dialogue("Rosario", "Good morning, Miss Cloma. How's the HR Department?", "single:Rosario_Casual", "BreakRoom.jpg"),
        //28
        SceneEntry.dialogue("Cloma", "As usual, it can get.", "single:Cloma_Casual", "BreakRoom.jpg"),
        //29
        SceneEntry.dialogue("Amaya", "May I have some snacks, Cloma? I'm hungry.", "single:Amaya_Sad", "BreakRoom.jpg"),
        //30
        SceneEntry.dialogue("Rosario", "I told you to stop overworking yourself. Rest and food are important for your health.", "single:Rosario_Casual", "BreakRoom.jpg"),
        //31
        SceneEntry.dialogue("Cloma", "Sorry, I only had enough for myself.", "single:Cloma_Sad", "BreakRoom.jpg"),
        //32
        SceneEntry.dialogue("Celeres", "You're doing that again, Amaya?", "single:Celeres_Casual", "BreakRoom.jpg"),
        SceneEntry.choice(), // MORNING 33
        //34
        SceneEntry.narrator("Rosario clears his throat, drawing everyone's attention.", "BreakRoom.jpg"),
        SceneEntry.dialogue("Rosario", "Ehem. Since you're all here... there's an announcement coming later today, "
                + "but I'll give you a heads-up.", "single:Rosario_Casual", "BreakRoom.jpg"),
        SceneEntry.narrator("Everyone began to listen to Rosario's voice.", "BreakRoom.jpg"),
        SceneEntry.dialogue("Rosario", "The annual Call Center Excellence Awards Night is next month. It's a formal"
                + " ceremony recognizing top-performing agents and teams for outstanding customer service and sales.", "single:Rosario_Casual", "BreakRoom.jpg"),
        SceneEntry.dialogue("Amaya", "… Is it mandatory to attend?", "single:Amaya_Sad", "BreakRoom.jpg"),
        SceneEntry.dialogue("Rosario", "Yes, I just realized it'll be a first for you, too, Amaya.", "single:Rosario_Smile", "BreakRoom.jpg"),
        SceneEntry.dialogue("Cloma", "It's a significant event. Winners get bonuses, recognition, and sometimes... other opportunities.", "single:Cloma_Smile", "BreakRoom.jpg"),
        SceneEntry.dialogue("Celeres", "The IT department never wins.", "single:Celeres_Sad", "BreakRoom.jpg"),
        SceneEntry.dialogue("Rosario", "I wonder why. IT doesn't handle calls.", "single:Rosario_Madal", "BreakRoom.jpg"),
        SceneEntry.dialogue("Celeres", "…We support those who do.", "single:Celeres_Sadl", "BreakRoom.jpg"),
        SceneEntry.dialogue("Cloma", "Want to be my plus one, Celeres dear?", "single:Cloma_Smile", "BreakRoom.jpg"),
        SceneEntry.dialogue("Celeres", "No.", "single:Celeres_Mad", "BreakRoom.jpg"),
        SceneEntry.dialogue("Cloma", "Boo. You're no fun.", "single:Cloma_Smile", "BreakRoom.jpg"),
        SceneEntry.dialogue("Amaya", "You can have partners in the event?", "single:Amaya_Smile", "BreakRoom.jpg"),
        SceneEntry.dialogue("Rosario", "No, not necessarily. But this event is treated like it's straight out of a ball for an odd reason.", "single:Rosario_Smile", "BreakRoom.jpg"),
        SceneEntry.dialogue("Amaya", "Oh! Shift starts at 10. We should head down.", "single:Amaya_Casual", "BreakRoom.jpg"),
        SceneEntry.dialogue("Rosario", "Right. I'll head out first.", "single:Rosario_Casual", "BreakRoom.jpg"),
        SceneEntry.dialogue("Celeres", "…Good luck today.", "single:Celeres_Casual", "BreakRoom.jpg"),
        SceneEntry.dialogue("Amaya", "See you later, Cloma! Celeres!", "single:Amaya_Smile", "BreakRoom.jpg"),
        SceneEntry.dialogue("{name}", "…", "none", "BreakRoom.jpg"),
        SceneEntry.dialogue("{name}", "Time to work.", "none", "MorningElevator.jpg"),
        SceneEntry.narrator("Your third shift hasn't even started, and already the day feels... different.", "MorningOffice.jpg"),
    };

    private static final SceneEntry[] EVENING = {
        SceneEntry.narrator("The shift ends. Your headset clicks off. The last caller's voice still echoes faintly in your ears.", "EveningOffice.jpg"),
        SceneEntry.narrator("You pack slowly. Too slow. Everyone else has gone home.", "EveningOffice.jpg"),
        SceneEntry.narrator("You step outside. The air is cold. The street is empty.", "StreetEvening.jpg"),
        SceneEntry.narrator("You walk home alone.", "StreetEvening.jpg"),
        SceneEntry.dialogue("{name}", "Three days. Just three days here.", "none", "StreetEvening.jpg"),
        SceneEntry.dialogue("{name}", "And already... Everything feels different.", "none", "StreetEvening.jpg"),
        SceneEntry.dialogue("{name}", "I've been ignoring it. It's wrong, but I can't help it. Lately, I noticed that I've been thinking a lot about this person.", "none", "StreetEvening.jpg"),
        SceneEntry.dialogue("{name}", "All I could think about is…", "none", "StreetEvening.jpg"),
        SceneEntry.routeSelection(),
    };

    // ── Choices ───────────────────────────────────────────────────────────────

    // MORNING 7 — respond to Cloma (LP goes to Cloma)
    private static final ChoiceEntry[] CHOICES_M7 = {
        new ChoiceEntry("Formal Reply", 2, 0, Character.CLOMA,
            SceneEntry.dialogue("{name}", "Good morning, Miss Cloma. Yes, preparing for my shift.", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Cloma", "Professional. I appreciate that. How are you adjusting so far?", "single:Cloma_Smile", "BreakRoom.jpg"),
            SceneEntry.dialogue("{name}", "Slowly getting there. Everyone's been helpful.", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Cloma", "Good. That's how it should be.", "single:Cloma_Smile", "BreakRoom.jpg")
        ),
        new ChoiceEntry("Honest Reply", 5, 0, Character.CLOMA,
            SceneEntry.dialogue("{name}", "Good morning, Miss Cloma. The morning just started, but I'm already exhausted.", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Cloma", "Days are never over at the Agency.", "single:Cloma_Smile", "BreakRoom.jpg"),
            SceneEntry.dialogue("Cloma", "I'll buy some snacks. If I may ask, is it all right if you take care of my handbag?", "single:Cloma_Casual", "BreakRoom.jpg"),
            SceneEntry.dialogue("{name}", "Yes, of course!", "none", "BreakRoom.jpg")
        ),
    };

    // MORNING 10 — greet or ignore Celeres (LP goes to Celeres)
    private static final ChoiceEntry[] CHOICES_M10 = {
        new ChoiceEntry("Greet", 5, 0, Character.CELERES,
            SceneEntry.dialogue("{name}", "Hi, Sir Celeres! How's your day?", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Celeres", "Oh. Um. Hi. I'm good.", "single:Celeres_Casual", "BreakRoom.jpg"),
            SceneEntry.dialogue("{name}", "Just getting coffee?", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Celeres", "Always. It's my third already. Don't tell anyone.", "single:Celeres_Casual", "BreakRoom.jpg"),
            SceneEntry.dialogue("{name}", "Your secret's safe with me.", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Celeres", "Um, call me Celeres as I told you to. Sir is too formal, I'm just an IT support guy.", "single:Celeres_Casual", "BreakRoom.jpg")
        ),
        new ChoiceEntry("Ignore", 0, 0,
            SceneEntry.dialogue("{name}", "(looking down at phone) ...Maybe later. I don't want to bother him.", "none", "BreakRoom.jpg"),
            SceneEntry.narrator("Celeres gets his coffee quietly. For a moment, he glances in your direction, as if expecting something. You don't look up.", "BreakRoom.jpg")
        ),
    };

    // MORNING 20 — ask about Rosario/Celeres tension
    private static final ChoiceEntry[] CHOICES_M21 = {
        new ChoiceEntry("Ask Rosario", 0, 2, Character.ROSARIO,
            SceneEntry.dialogue("{name}", "Sir... is everything okay between you and Celeres?", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Rosario", "That's not something you need to concern yourself with.", "single:Rosario_Casual", "BreakRoom.jpg"),
            SceneEntry.dialogue("{name}", "Sorry, sir, I didn't mean to overstep…", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Rosario", "It's fine. Let's not dwell on it further.", "single:Rosario_Casual", "BreakRoom.jpg")
        ),
        new ChoiceEntry("Ask Amaya", 0, 2, Character.AMAYA,
            SceneEntry.dialogue("{name}", "What's that about? Between them?", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Amaya", "Sir Rosario hates him. No one knows why. It's been like this forever.", "single:Amaya_Casual", "BreakRoom.jpg"),
            SceneEntry.dialogue("{name}", "Has anyone ever asked?", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Amaya", "Some did, but Rosario shuts it down. Every time. So now we just... let it happen.", "single:Amaya_Sad", "BreakRoom.jpg"),
            SceneEntry.dialogue("{name}", "That's sad.", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Amaya", "Yeah. It kind of is. I feel bad for Celeres. He doesn't even know why Sir Rosario hates him.", "single:Amaya_Sad", "BreakRoom.jpg")
        ),
        new ChoiceEntry("Ask Celeres", 0, 5, Character.CELERES,
            SceneEntry.dialogue("{name}", "Hey. You okay?", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Celeres", "...I'm used to it.", "single:Celeres_Sad", "BreakRoom.jpg"),
            SceneEntry.dialogue("{name}", "That doesn't mean it's okay.", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Celeres", "...No one's ever asked me that before.", "single:Celeres_Sad", "BreakRoom.jpg"),
            SceneEntry.dialogue("{name}", "Asked what?", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Celeres", "If I'm okay. They just watch.", "single:Celeres_Sad", "BreakRoom.jpg"),
            SceneEntry.dialogue("{name}", "Well... I'm asking. For real.", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Celeres", "...I'm okay. Thank you. For asking.", "single:Celeres_Smile", "BreakRoom.jpg")
        ),
        new ChoiceEntry("Do nothing", 0, 0,
            SceneEntry.dialogue("{name}", "(to yourself) Not my business. Better stay out of it.", "none", "BreakRoom.jpg")
        ),
    };

    // MORNING 32 — offer snacks / cash / reprimand Amaya
    private static final ChoiceEntry[] CHOICES_M33 = {
        new ChoiceEntry("Offer snacks to Amaya",
            5,  // PP reward
            ChoiceEntry.lp(
                Character.AMAYA,   5,
                Character.ROSARIO, 3,
                Character.CELERES, 2,
                Character.CLOMA,   5
            ),
            SceneEntry.dialogue("{name}", "Here, Amaya. I have an extra. Take it.", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Amaya", "Wait, really?! You're my hero!", "single:Amaya_Smile", "BreakRoom.jpg"),
            SceneEntry.dialogue("Cloma", "That's very kind of you.", "single:Cloma_Smile", "BreakRoom.jpg"),
            SceneEntry.dialogue("Narrator", "Rosario smiles quietly.", "single:Rosario_Smile", "BreakRoom.jpg"),  
            SceneEntry.dialogue("Narrator", "Celeres does the same.", "single:Celeres_Smile", "BreakRoom.jpg"), 
            SceneEntry.dialogue("Amaya", "Thank you! You're the best! Like, actually the best.", "single:Amaya_Smile", "BreakRoom.jpg")
        ),
        new ChoiceEntry("Offer cash to Amaya", -50, 5, Character.AMAYA,
            SceneEntry.dialogue("{name}", "Here, Amaya. Go buy something for yourself.", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Amaya", "¡No, no! You don't have to!", "single:Amaya_Casual", "BreakRoom.jpg"),
            SceneEntry.dialogue("{name}", "Go on. Before I change my mind.", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Amaya", "Okay then! Thank you!", "single:Amaya_Smile", "BreakRoom.jpg"),
            SceneEntry.dialogue("Rosario", "You're enabling her bad habits.", "single:Rosario_Casual", "BreakRoom.jpg"),
            SceneEntry.dialogue("Celeres", "But it's nice of you.", "single:Celeres_Casual", "BreakRoom.jpg"),
            SceneEntry.dialogue("Cloma", "She's like a stray kitten.", "single:Cloma_Casual", "BreakRoom.jpg")
        ),
        new ChoiceEntry("Reprimand Amaya", 0, 2, Character.AMAYA,
            SceneEntry.dialogue("{name}", "Even now? Seriously, you need to stop doing that.", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("{name}", "Next time, you need to eat before you come here early.", "none", "BreakRoom.jpg"),
            SceneEntry.dialogue("Amaya", "I know…", "single:Amaya_Sad", "BreakRoom.jpg")
        ),
    };

    @Override
    public SceneEntry[] getMorningScenes() { return MORNING; }
    @Override
    public SceneEntry[] getEveningScenes() { return EVENING; }

    @Override
    public ChoiceEntry[] getChoicesForScene(int sceneIndex, String segment) {
        return switch (segment) {
            case "MORNING" -> switch (sceneIndex) {
                case 7 -> CHOICES_M7;
                case 10 -> CHOICES_M10;
                case 20 -> CHOICES_M21;
                case 32 -> CHOICES_M33;
                default -> null;
            };
            default -> null;
        };
    }
}