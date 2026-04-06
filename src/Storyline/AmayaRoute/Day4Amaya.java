package Storyline.AmayaRoute;

import Storyline.*;
import Entities.Character;

public class Day4Amaya implements DayInterface {
    
    private static final SceneEntry[] MORNING = {
        // 0
        SceneEntry.narrator("The next day, the usual buzz of the call center feels a little louder, a little more chaotic.", "MorningOffice.jpg"),
        // 1
        SceneEntry.narrator("You notice Amaya on a particularly difficult call. Her 'customer service voice' is perfect, but when she hangs up, the smile vanishes.", "MorningOffice.jpg"),
        // 2
        SceneEntry.narrator("For a moment, she just stares at her monitor, her shoulders slumped.", "MorningOffice.jpg"),
        // 3
        SceneEntry.narrator("Suddenly, before you could grab her attention, a voice calls you out.", "MorningOffice.jpg"),
        // 4
        SceneEntry.dialogue("Rosario", "{name}, come here for a second.", "single:Rosario_Casual", "MorningOffice.jpg"),
        // 5
        SceneEntry.dialogue("{name}", "Yes, sir?", "none", "MorningOffice.jpg"),
        // 6
        SceneEntry.dialogue("Rosario", "I want you to check these papers for me. Just double-check any margin errors.", "single:Rosario_Casual", "MorningOffice.jpg"),
        // 7
        SceneEntry.narrator("Before Rosario could hand the papers to you, Amaya suddenly intervenes.", "MorningOffice.jpg"),
        // 8
        SceneEntry.dialogue("Amaya", "I'll do it!", "single:Amaya_Smile", "MorningOffice.jpg"),
        // 9
        SceneEntry.narrator("Amaya grabs the papers and does a quick skim. She neatly pats the paper down right after.", "MorningOffice.jpg"),
        // 10
        SceneEntry.dialogue("Amaya", "I can do this in a week. No, lesser than that! You'll raise my pay if I do, right?", "single:Amaya_Smile", "MorningOffice.jpg"),
        // 11
        SceneEntry.dialogue("Rosario", "Amaya... I wasn't asking for volunteers. And definitely not asking you to take on more work.", "single:Rosario_Casual", "MorningOffice.jpg"),
        // 12
        SceneEntry.dialogue("Amaya", "Oh, please! I can do this! You know me, always looking for overtime!", "single:Amaya_Smile", "MorningOffice.jpg"),
        // 13
        SceneEntry.narrator("Rosario looks at you, but with a sudden sign of… disappointment and hurt? But you remain unsure of what his emotions imply.", "MorningOffice.jpg"),
        // 14
        SceneEntry.dialogue("Rosario", "Amaya, you've worked more than enough. It's time to rest.", "single:Rosario_Casual", "MorningOffice.jpg"),
        // 15
        SceneEntry.dialogue("Amaya", "But! {name}, you know I can do this right?", "single:Amaya_Sad", "MorningOffice.jpg"),
        // 16 — CHOICE
        SceneEntry.choice(),
        // 17
        SceneEntry.narrator("After a lengthy day for what felt like months have passed by, your shift for the day finally ends. "
                + "Rosario, Amaya, and you walk toward the exit together.", "EveningOffice.jpg"),
        // 18
        SceneEntry.dialogue("Rosario", "{name}, walk with me for a bit? I wanted to talk about the team project.", "double:Rosario_Smile:Amaya_Mad", "EveningOffice.jpg"),
        // 19
        SceneEntry.narrator("Amaya suddenly squishes herself between you and Rosario.", "EveningOffice.jpg"),
        // 20
        SceneEntry.dialogue("Amaya", "Actually, {name} and I were going to grab something to eat. Right, PC?", "double:Rosario_Casual:Amaya_Mad", "EveningOffice.jpg"),
        // 21
        SceneEntry.dialogue("{name}", "We were?","double:Rosario_Smile:Amaya_Mad", "EveningOffice.jpg"),
        // 22
        SceneEntry.dialogue("Amaya", "Yeah. We were.","double:Rosario_Smile:Amaya_Mad", "EveningOffice.jpg"),
        // 23
        SceneEntry.dialogue("Rosario", "Amaya, I wasn't trying to steal them.", "double:Rosario_Smile:Amaya_Mad", "EveningOffice.jpg"),
        // 24
        SceneEntry.dialogue("Amaya", "I know. But we have plans.", "double:Rosario_Casual:Amaya_Mad", "EveningOffice.jpg"),
        // 25
        SceneEntry.narrator("The walk is tense. Amaya stays close to you, her shoulder brushing yours every few steps. "
                + "Any time Rosario speaks, she finds a reason to respond first.", "StreetEvening.jpg"),
        // 26
        SceneEntry.dialogue("Rosario", "{name}, how do you feel about the new workflow software?","double:Rosario_Casual:Amaya_Mad", "StreetEvening.jpg"),
        // 27
        SceneEntry.dialogue("Amaya", "They told me it's fine. Right, {name}?", "double:Rosario_Casual:Amaya_Mad", "StreetEvening.jpg"),
        // 28
        SceneEntry.dialogue("{name}", "I… yeah, it's fine.", "none", "StreetEvening.jpg"),
        // 29
        SceneEntry.dialogue("Rosario", "I was asking {name}, Amaya.", "single:Rosario_Casual", "StreetEvening.jpg"),
        // 30
        SceneEntry.dialogue("Amaya", "Still, you got your answer didn't you?", "single:Amaya_Mad", "StreetEvening.jpg"),
        // 31
        SceneEntry.narrator("Eventually, Amaya's phone buzzes. She glances at it, and her face falls.", "StreetEvening.jpg"),
        // 32
        SceneEntry.dialogue("{name}", "Everything okay?", "none", "StreetEvening.jpg"),
        // 33
        SceneEntry.dialogue("Amaya", "Yeah. I just… I have to go.", "single:Amaya_Sad", "StreetEvening.jpg"),
        // 34
        SceneEntry.narrator("She turns to Rosario, her eyes sharp.", "StreetEvening.jpg"),
        // 35
        SceneEntry.dialogue("Amaya", "Don't keep {pronoun_objective} too long.", "single:Amaya_Mad", "StreetEvening.jpg"),
        // 36
        SceneEntry.dialogue("Rosario", "Wouldn't dream of it.", "single:Rosario_Casual", "StreetEvening.jpg"),
        // 37
        SceneEntry.narrator("She leaves quickly, her footsteps echoing down the street. You watch her go until she disappears around a corner.", "StreetEvening.jpg"),
        // 38
        SceneEntry.dialogue("Rosario", "She really cares about you, you know.", "single:Rosario_Casual", "StreetEvening.jpg"),
        // 39
        SceneEntry.dialogue("{name}", "She's just protective. We've known each other since high school.", "single:Rosario_Smile", "StreetEvening.jpg"),
        // 40
        SceneEntry.dialogue("Rosario", "No. That was jealousy.", "single:Rosario_Smile", "StreetEvening.jpg"),
        // 41
        SceneEntry.dialogue("{name}", "What?", "single:Rosario_Smile", "StreetEvening.jpg"),
        // 42
        SceneEntry.dialogue("Rosario", "{name}… can I be honest with you?", "single:Rosario_Sad", "StreetEvening.jpg"),
        // 43
        SceneEntry.dialogue("{name}", "Of course.",  "single:Rosario_Sad", "StreetEvening.jpg"),
        // 44
        SceneEntry.dialogue("Rosario", "I hope you let Amaya be herself more. The real her.", "single:Rosario_Casual", "StreetEvening.jpg"),
        // 45
        SceneEntry.narrator("He pauses, choosing his words carefully.", "StreetEvening.jpg"),
        // 46
        SceneEntry.dialogue("Rosario", "She's so overworked and barely focuses on herself anymore. That smile she wears? It's been fake for a long time. "
                + "I've seen it every day for months now.", "single:Rosario_Sad", "StreetEvening.jpg"),
        // 47
        SceneEntry.dialogue("{name}", "You've noticed too?", "none", "StreetEvening.jpg"),
        // 48
        SceneEntry.dialogue("Rosario", "Everyone has. But no one says anything because she pushes them away.", "single:Rosario_Casual", "StreetEvening.jpg"),
        // 49
        SceneEntry.narrator("Rosario looks at you, really looks at you.", "StreetEvening.jpg"),
        // 50
        SceneEntry.dialogue("Rosario", "Whatever she showed you tonight. The way she expressed her jealousy. "
                + "That's something I've seen before. Her raw emotional self.", "single:Rosario_Casual", "StreetEvening.jpg"),
        // 51
        SceneEntry.dialogue("{name}", "Why are you telling me this?", "none", "StreetEvening.jpg"),
        // 52
        SceneEntry.dialogue("Rosario", "Because I like you, {name}. But she needs you more than I do. She doesn't know this but, she reminds me "
                + "of myself before I had the chance to choose my life the way I want it to be.", "single:Rosario_Sad", "StreetEvening.jpg"),
        // 53
        SceneEntry.narrator("He nods goodbye and walks away, leaving you alone with your thoughts and the echo of his confession.", "StreetEvening.jpg"),
        // 54
        SceneEntry.narrator("You stand there for a long moment.", "StreetEvening.jpg"),
        // 55
        SceneEntry.narrator("Somewhere across town, Amaya is walking home alone.", "StreetEvening.jpg"),
        // 56
        SceneEntry.narrator("And for the first time, you wonder if anyone has ever walked with her.", "StreetEvening.jpg"),
    };
    
    private static final SceneEntry[] EVENING = {
        SceneEntry.narrator("Day 4 Evening - Amaya Route", "EveningOffice.jpg")
    };
    
    // ── Choices ───────────────────────────────────────────────────────────────
    
    // MORNING scene 16 — Tell Amaya to rest vs Let Amaya do it
    private static final ChoiceEntry[] CHOICES_M16 = {
        new ChoiceEntry("Tell Amaya to rest", -10, 0, Character.AMAYA,
            SceneEntry.dialogue("{name}", "Amaya, stop.", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "(blinking) What?", "single:Amaya_Surprised", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "You need to rest. I mean it.", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "I don't need—", "single:Amaya_Sad", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "I'll handle the papers. Go take a break. Please.", "none", "MorningOffice.jpg"),
            SceneEntry.narrator("Amaya stares at you. Her mouth opens, then closes.", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "…Fine.", "single:Amaya_Sad", "MorningOffice.jpg"),
            SceneEntry.narrator("She walks away, but you catch her glancing back at you and Rosario talking. Her jaw tightens.", "MorningOffice.jpg"),
            SceneEntry.dialogue("Rosario", "(quietly) That was kind of you.", "single:Rosario_Smile", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "Someone had to say it.", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Rosario", "(small smile) Yeah. Someone did.", "single:Rosario_Smile", "MorningOffice.jpg"),
            SceneEntry.narrator("But as you take the papers from Rosario, his fingers brush yours. He doesn't pull away immediately.", "MorningOffice.jpg"),
            SceneEntry.dialogue("Rosario", "Let's go over them together. In my office.", "single:Rosario_Smile", "MorningOffice.jpg")
        ),
        new ChoiceEntry("Let Amaya do this", 10, 0, Character.AMAYA,
            SceneEntry.dialogue("{name}", "She's capable. If she wants to do it, let her.", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "(brightening) Thank you, PC!", "single:Amaya_Smile", "MorningOffice.jpg"),
            SceneEntry.dialogue("Rosario", "(sighing) …If you say so.", "single:Rosario_Casual", "MorningOffice.jpg"),
            SceneEntry.narrator("Amaya takes the papers, practically beaming. But as she walks away, Rosario touches your arm.", "MorningOffice.jpg"),
            SceneEntry.dialogue("Rosario", "You know she's already working sixty hours a week, right?", "single:Rosario_Casual", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "She seems fine.", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Rosario", "...does she?", "single:Rosario_Sad", "MorningOffice.jpg"),
            SceneEntry.narrator("You look at Amaya. She's already back at her desk, head down, shoulders tight.", "MorningOffice.jpg"),
            SceneEntry.narrator("Her smile from earlier is gone.", "MorningOffice.jpg")
        ),
    };
    
    @Override
    public SceneEntry[] getMorningScenes() { return MORNING; }
    @Override
    public SceneEntry[] getEveningScenes() { return EVENING; }
    
    @Override
    public ChoiceEntry[] getChoicesForScene(int sceneIndex, String segment) {
        if ("MORNING".equals(segment) && sceneIndex == 16) {
            return CHOICES_M16;
        }
        return null;
    }
}