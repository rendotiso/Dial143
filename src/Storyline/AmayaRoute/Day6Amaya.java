package Storyline.AmayaRoute;

import Storyline.*;
import Entities.Character;

public class Day6Amaya implements DayInterface {
    
    private static final SceneEntry[] MORNING = {
        // 0
        SceneEntry.narrator("The office feels different today. Decorations are set up, music plays softly and everyone seems more relaxed than usual.", "MorningOffice.jpg"),
        // 1
        SceneEntry.dialogue("Cloma", "Free food and no calls? This is my kind of event.", "single:Cloma_Smile", "MorningOffice.jpg"),
        // 2
        SceneEntry.dialogue("Celeres", "Don't get too comfortable. They'll probably make us do something later.", "single:Celeres_Casual", "MorningOffice.jpg"),
        // 3
        SceneEntry.dialogue("Amaya", "Come on, just enjoy it for now.", "single:Amaya_Smile", "MorningOffice.jpg"),
        // 4
        SceneEntry.narrator("She smiles… but it feels a little forced.", "MorningOffice.jpg"),
        // 5
        SceneEntry.narrator("You notice it again. The same tired look behind her smile.", "MorningOffice.jpg"),
        // 6
        SceneEntry.dialogue("Rosario", "Everyone, make sure to participate. This is for the team, after all.", "single:Rosario_Casual", "MorningOffice.jpg"),
        // 7
        SceneEntry.narrator("The events start: games, small activities, laughter filling the room. Amaya tries to keep up, but you can tell… She's not fully there.", "MorningOffice.jpg"),
        // 8 — CHOICE (Stay with Amaya / Focus on the event)
        SceneEntry.choice(),
    };
    
    private static final SceneEntry[] EVENING = {
        // 0
        SceneEntry.narrator("As you went to the circle of the group, you noticed Amaya saw something on her phone and tense up.", "EventStage.jpg"),
        // 1
        SceneEntry.dialogue("Amaya", "I have to go.", "single:Amaya_Sad", "EventStage.jpg"),
        // 2
        SceneEntry.dialogue("Celeres", "What?", "single:Celeres_Surprised", "EventStage.jpg"),
        // 3
        SceneEntry.dialogue("Rosario", "Is there something wrong?", "single:Rosario_Casual", "EventStage.jpg"),
        // 4
        SceneEntry.dialogue("Cloma", "Is it your mother?", "single:Cloma_Casual", "EventStage.jpg"),
        // 5
        SceneEntry.dialogue("Amaya", "What? You! How did you know! Did everyone know?", "single:Amaya_Mad", "EventStage.jpg"),
        // 6
        SceneEntry.narrator("Everyone tensed up, not saying a word. The silent confirmation was all she needed.", "EventStage.jpg"),
        // 7
        SceneEntry.dialogue("Amaya", "…", "single:Amaya_Sad", "EventStage.jpg"),
        // 8
        SceneEntry.narrator("Her eyes felt betrayed, but she simply runs away without a single word out.", "EventStage.jpg"),
        // 9
        SceneEntry.narrator("You then follow her without thinking.", "EventStage.jpg"),
        // 10
        SceneEntry.narrator("Past the desks. Past the break room. Past Rosario, who calls your name.", "EveningOffice.jpg"),
        // 11
        SceneEntry.narrator("Out the back door. Into the alley behind the building.", "Alley.jpg"),
        // 12
        SceneEntry.narrator("And then you see her.", "Alley.jpg"),
        // 13
        SceneEntry.dialogue("Amaya", "(voice breaking) I can't do this anymore.", "single:Amaya_Crying", "Alley.jpg"),
        // 14
        SceneEntry.narrator("You sit down next to her. Not touching. Just present.", "Alley.jpg"),
        // 15
        SceneEntry.narrator("She doesn't say anything else.", "Alley.jpg"),
        // 16
        SceneEntry.narrator("But she doesn't tell you to leave either.", "Alley.jpg"),
        // 17
        SceneEntry.narrator("You see her phone notification.", "Alley.jpg"),
        // 18
        SceneEntry.narrator("(Brother: Mom's results came back. She has stage 3 cancer.)", "Alley.jpg"),
        // 19
        SceneEntry.narrator("You didn't say a word about what you saw.", "Alley.jpg"),
        // 20
        SceneEntry.narrator("The two of you sit in the alley, the flickering light above, the distant sound of the party still going on inside. A great divide between their joys and her pains.", "Alley.jpg"),
        // 21
        SceneEntry.narrator("Amaya cries.", "Alley.jpg"),
        // 22
        SceneEntry.narrator("And for the first time, she doesn't hide it.", "Alley.jpg"),
    };
    
    // ── Company Event Scenes (shared structure, inserted between morning and evening) ──
    private static final SceneEntry[] COMPANY_EVENT = {
        // 0
        SceneEntry.narrator("Employees slowly arrive, chatting and enjoying the relaxed atmosphere.", "EventStage.jpg"),
        // 1
        SceneEntry.narrator("You walk inside and immediately spot Amaya waving at you.", "EventStage.jpg"),
        // 2
        SceneEntry.dialogue("Amaya", "Hey! Over here!", "single:Amaya_Smile", "EventStage.jpg"),
        // 3
        SceneEntry.dialogue("{name}", "Wow… the office looks so different without everyone stressing over work.", "none", "EventStage.jpg"),
        // 4
        SceneEntry.dialogue("Amaya", "I know, right? I almost forgot what a normal day feels like.", "single:Amaya_Casual", "EventStage.jpg"),
        // 5
        SceneEntry.narrator("Cloma walks over, holding a clipboard but smiling warmly.", "EventStage.jpg"),
        // 6
        SceneEntry.dialogue("Cloma", "I'm glad you came! HR worked really hard organizing this event.", "single:Cloma_Smile", "EventStage.jpg"),
        // 7
        SceneEntry.dialogue("{name}", "It looks great, Cloma.", "none", "EventStage.jpg"),
        // 8
        SceneEntry.dialogue("Cloma", "Thank you! Just relax and enjoy today.", "single:Cloma_Smile", "EventStage.jpg"),
        // 9
        SceneEntry.narrator("Nearby, you see Rosario standing with his arms crossed, observing everything.", "EventStage.jpg"),
        // 10
        SceneEntry.dialogue("Amaya", "(whispers) Even at a party, he looks like he's supervising us.", "single:Amaya_Casual", "EventStage.jpg"),
        // 11
        SceneEntry.dialogue("Rosario", "I heard that.", "single:Rosario_Casual", "EventStage.jpg"),
        // 12
        SceneEntry.dialogue("Amaya", "Eep—!", "single:Amaya_Surprised", "EventStage.jpg"),
        // 13
        SceneEntry.dialogue("Rosario", "This is still a company event. Try not to cause trouble.", "single:Rosario_Casual", "EventStage.jpg"),
        // 14
        SceneEntry.narrator("Suddenly, someone walks past slowly with headphones around his neck. It's Celeres.", "EventStage.jpg"),
        // 15
        SceneEntry.dialogue("{name}", "Celeres? I didn't expect to see you here.", "none", "EventStage.jpg"),
        // 16
        SceneEntry.dialogue("Celeres", "HR said attendance was important.", "single:Celeres_Casual", "EventStage.jpg"),
        // 17
        SceneEntry.dialogue("Cloma", "(laughs softly) I might have mentioned that.", "single:Cloma_Smile", "EventStage.jpg"),
        // 18
        SceneEntry.dialogue("Rosario", "Mophead actually showed up.", "single:Rosario_Casual", "EventStage.jpg"),
        // 19
        SceneEntry.dialogue("Celeres", "I'm only here for the free food.", "single:Celeres_Casual", "EventStage.jpg"),
        // 20
        SceneEntry.dialogue("Amaya", "Honestly… same.", "single:Amaya_Smile", "EventStage.jpg"),
        // 21
        SceneEntry.narrator("After a while, everyone gathers near the stage.", "EventStage.jpg"),
        // 22
        SceneEntry.narrator("Sir Khai, the CEO of the company, walks forward with a microphone.", "EventStage.jpg"),
        // 23
        SceneEntry.dialogue("Sir Khai", "Good afternoon, everyone.", "single:Khai_Smile", "EventStage.jpg"),
        // 24
        SceneEntry.narrator("Everyone responds.", "EventStage.jpg"),
        // 25
        SceneEntry.dialogue("Sir Khai", "Today isn't about work. Today is about appreciating the people who make this company run successfully.", "single:Khai_Smile", "EventStage.jpg"),
        // 26
        SceneEntry.dialogue("Sir Khai", "But before we enjoy the rest of the event, we will recognize employees who performed exceptionally well this week.", "single:Khai_Smile", "EventStage.jpg"),
        // 27
        SceneEntry.dialogue("Sir Khai", "The award for Outstanding Technical Support goes to… Celeres!", "single:Khai_Smile", "EventStage.jpg"),
        // 28
        SceneEntry.dialogue("Celeres", "(sleepy smile) Thanks… I guess.", "single:Celeres_Smile", "EventStage.jpg"),
        // 29
        SceneEntry.dialogue("Sir Khai", "Next, the Most Reliable Staff award goes to… Amaya!", "single:Khai_Smile", "EventStage.jpg"),
        // 30
        SceneEntry.dialogue("Amaya", "Wow! Thank you, Sir!", "single:Amaya_Smile", "EventStage.jpg"),
        // 31
        SceneEntry.dialogue("Sir Khai", "Next, the Exemplary HR Employee award goes to… Miss Cloma!", "single:Khai_Smile", "EventStage.jpg"),
        // 32
        SceneEntry.dialogue("Cloma", "I'm honored. Thank you.", "single:Cloma_Smile", "EventStage.jpg"),
        // 33
        SceneEntry.dialogue("Sir Khai", "And the Leadership Excellence award goes to… Rosario!", "single:Khai_Smile", "EventStage.jpg"),
        // 34
        SceneEntry.dialogue("Rosario", "(slightly flustered) Thank you… I appreciate it.", "single:Rosario_Blushing", "EventStage.jpg"),
        // 35 — Award scene (conditional - will be handled by choice based on PP)
        SceneEntry.choice(), // Award branch
        // 36 — After awards
        SceneEntry.narrator("The afternoon slowly turns into evening.", "EventStage.jpg"),
        // 37
        SceneEntry.narrator("Music plays softly while coworkers laugh, eat, and talk.", "EventStage.jpg"),
        // 38
        SceneEntry.narrator("For once, the office feels peaceful.", "EventStage.jpg"),
        // 39
        SceneEntry.narrator("You glance at your coworkers—Amaya joking around, Cloma chatting happily, Rosario trying to look serious, and Celeres quietly listening to music.", "EventStage.jpg"),
        // 40
        SceneEntry.dialogue("{name}", "(mind) Maybe days like this are what make the long work weeks worth it.", "none", "EventStage.jpg"),
    };
    
    // ── Choices ───────────────────────────────────────────────────────────────
    
    // MORNING scene 8 — Stay with Amaya vs Focus on the event
    private static final ChoiceEntry[] CHOICES_M8 = {
        new ChoiceEntry("Stay with Amaya", 0, 15, Character.AMAYA,
            SceneEntry.dialogue("{name}", "Hey… you don't have to force yourself.", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "Huh? I'm not forcing anything.", "single:Amaya_Casual", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "You don't look okay.", "none", "MorningOffice.jpg"),
            SceneEntry.narrator("She pauses, then sighs softly.", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "You really notice everything, don't you?", "single:Amaya_Casual", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "Only when it comes to you.", "none", "MorningOffice.jpg"),
            SceneEntry.narrator("She looks away, slightly flustered.", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "I'm fine… Really.", "single:Amaya_Blushing", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "You don't always have to be.", "none", "MorningOffice.jpg"),
            SceneEntry.narrator("She doesn't respond, but she stayed beside you.", "MorningOffice.jpg")
        ),
        new ChoiceEntry("Focus on the event", 0, -10, Character.AMAYA,
            SceneEntry.dialogue("{name}", "You should try to enjoy this. It's a break from work.", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "Yeah… I know.", "single:Amaya_Sad", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "Come on, let's join them.", "none", "MorningOffice.jpg"),
            SceneEntry.narrator("She forces a smile.", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "Sure.", "single:Amaya_Smile", "MorningOffice.jpg")
        ),
    };
    
    // COMPANY EVENT scene 35 — Award based on Performance Points (HIGH vs LOW)
    // This will be determined dynamically by the game engine based on PP value
    private static final ChoiceEntry[] CHOICES_AWARD_HIGH = {
        new ChoiceEntry("Accept Award", 0, 0,
            SceneEntry.dialogue("Sir Khai", "And this week's 'Outstanding Employee' award goes to… {name}!", "single:Khai_Smile", "EventStage.jpg"),
            SceneEntry.narrator("The room fills with applause.", "EventStage.jpg"),
            SceneEntry.dialogue("Amaya", "Wait—that's you!", "single:Amaya_Smile", "EventStage.jpg"),
            SceneEntry.dialogue("Cloma", "Congratulations!", "single:Cloma_Smile", "EventStage.jpg"),
            SceneEntry.dialogue("Rosario", "(nods slightly) Good work. I expected nothing less.", "single:Rosario_Smile", "EventStage.jpg"),
            SceneEntry.narrator("You walk to the stage.", "EventStage.jpg"),
            SceneEntry.dialogue("Sir Khai", "Your hard work and dedication did not go unnoticed. Keep it up.", "single:Khai_Smile", "EventStage.jpg"),
            SceneEntry.dialogue("{name}", "Thank you, Sir Khai.", "none", "EventStage.jpg"),
            SceneEntry.narrator("When you return—", "EventStage.jpg"),
            SceneEntry.dialogue("Amaya", "Drinks are on you tonight!", "single:Amaya_Smile", "EventStage.jpg"),
            SceneEntry.dialogue("Cloma", "You deserve it.", "single:Cloma_Smile", "EventStage.jpg"),
            SceneEntry.dialogue("Celeres", "…Nice work.", "single:Celeres_Smile", "EventStage.jpg"),
            SceneEntry.dialogue("Rosario", "Don't let the award get to your head.", "single:Rosario_Casual", "EventStage.jpg"),
            SceneEntry.dialogue("Amaya", "He's just jealous.", "single:Amaya_Smile", "EventStage.jpg"),
            SceneEntry.dialogue("Rosario", "I am not.", "single:Rosario_Casual", "EventStage.jpg"),
            SceneEntry.narrator("Everyone laughs.", "EventStage.jpg")
        ),
    };
    
    private static final ChoiceEntry[] CHOICES_AWARD_LOW = {
        new ChoiceEntry("No Award", 0, 0,
            SceneEntry.narrator("Sir Khai finishes announcing the award winners.", "EventStage.jpg"),
            SceneEntry.narrator("Your name wasn't called.", "EventStage.jpg"),
            SceneEntry.dialogue("Amaya", "Hey… don't feel bad.", "single:Amaya_Casual", "EventStage.jpg"),
            SceneEntry.dialogue("Cloma", "There's always next week.", "single:Cloma_Smile", "EventStage.jpg"),
            SceneEntry.dialogue("Rosario", "Improvement takes time. Keep working hard.", "single:Rosario_Smile", "EventStage.jpg"),
            SceneEntry.dialogue("Celeres", "Awards are loud anyway.", "single:Celeres_Casual", "EventStage.jpg"),
            SceneEntry.dialogue("{name}", "Thank you, guys! I should do my very best next time.", "none", "EventStage.jpg"),
            SceneEntry.narrator("They all smile.", "EventStage.jpg")
        ),
    };
    
    @Override
    public SceneEntry[] getMorningScenes() { return MORNING; }
    @Override
    public SceneEntry[] getEveningScenes() { return EVENING; }
    
    /**
     * Returns the company event scenes that play between morning and evening.
     * This is called separately by the game engine.
     */
    public SceneEntry[] getCompanyEventScenes() { return COMPANY_EVENT; }
    
    @Override
    public ChoiceEntry[] getChoicesForScene(int sceneIndex, String segment) {
        return switch (segment) {
            case "MORNING" -> switch (sceneIndex) {
                case 8 -> CHOICES_M8;
                default -> null;
            };
            case "COMPANY_EVENT" -> switch (sceneIndex) {
                case 35 -> {
                    // Dynamic award based on PP
                    // This should be handled by the game engine checking mainPanel.getPP()
                    // For now, return null and let engine decide
                    yield null;
                }
                default -> null;
            };
            default -> null;
        };
    }
}