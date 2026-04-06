package Storyline.AmayaRoute;

import Storyline.*;
import Entities.Character;

public class Day7Amaya implements DayInterface {
    
    private static final SceneEntry[] MORNING = {
        
    };
    
    private static final SceneEntry[] EVENING = {
    };
    
    // ── Good Ending (60+ LP) ───────────────────────────────────────────────────
    public static SceneEntry[] getGoodEnding() {
        return new SceneEntry[] {
            SceneEntry.narrator("The morning feels quieter than usual. It was the weekend, and the event slowly made everyone exhausted the day after. They were all exhausted from the cheering, but you were exhausted for another reason.", "ParkMorning.jpg"),
            SceneEntry.narrator("After everything that happened last night…", "ParkMorning.jpg"),
            SceneEntry.narrator("You can't stop thinking about her.", "ParkMorning.jpg"),
            SceneEntry.narrator("When you arrive at the location you and Amaya promised to meet up, you see Amaya waiting.", "ParkMorning.jpg"),
            SceneEntry.narrator("She's sitting at the park bench, her hands folded in front of her.", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "Hey.", "single:Amaya_Casual", "ParkMorning.jpg"),
            SceneEntry.dialogue("{name}", "Hey.", "none", "ParkMorning.jpg"),
            SceneEntry.narrator("Brief silence, but not uncomfortable.", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "About yesterday…", "single:Amaya_Casual", "ParkMorning.jpg"),
            SceneEntry.dialogue("{name}", "You don't have to explain.", "none", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "No… I want to.", "single:Amaya_Casual", "ParkMorning.jpg"),
            SceneEntry.narrator("She takes a breath. A long one.", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "I called my mom this morning. We talked. For real. Not about bills or shifts or overtime. We talked about… me.", "single:Amaya_Casual", "ParkMorning.jpg"),
            SceneEntry.dialogue("{name}", "What did you say?", "none", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "(small laugh) Everything. How tired I am. How I've been drowning. How I can't remember the last time I did something that wasn't for someone else.", "single:Amaya_Smile", "ParkMorning.jpg"),
            SceneEntry.dialogue("{name}", "And the art?", "none", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "(eyes watering) I told her I miss it. That I want to draw again. That I can't remember the last time I drew something that made me happy.", "single:Amaya_Sad", "ParkMorning.jpg"),
            SceneEntry.dialogue("{name}", "What did she say?", "none", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "(voice cracking) She said she was sorry. That she never wanted me to give up my dream for her. That she'd rather struggle with the bills than watch me disappear.", "single:Amaya_Crying", "ParkMorning.jpg"),
            SceneEntry.narrator("Amaya wipes her eyes with the back of her hand.", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "My dad's business is still struggling. But we're going to figure it out. Together. Not just me carrying everything.", "single:Amaya_Smile", "ParkMorning.jpg"),
            SceneEntry.dialogue("{name}", "That's… really good, Amaya.", "none", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "I couldn't have done it without you pushing me.", "single:Amaya_Smile", "ParkMorning.jpg"),
            SceneEntry.dialogue("{name}", "You would have gotten there eventually.", "none", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "(shaking her head) No. I wouldn't have. I was too scared. Too proud. I kept telling myself I was fine when I was falling apart.", "single:Amaya_Sad", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "I'm not used to letting people see me like that. Crying. Broken. Needing help.", "single:Amaya_Sad", "ParkMorning.jpg"),
            SceneEntry.dialogue("{name}", "You don't have to do everything alone.", "none", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "Yeah. I'm starting to realize that.", "single:Amaya_Smile", "ParkMorning.jpg"),
            SceneEntry.dialogue("{name}", "You don't have to be strong all the time.", "none", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "Then… can I rely on you?", "single:Amaya_Blushing", "ParkMorning.jpg"),
            SceneEntry.dialogue("{name}", "You already are.", "none", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "(laughing, wet) You really changed, you know that?", "single:Amaya_Smile", "ParkMorning.jpg"),
            SceneEntry.dialogue("{name}", "Maybe… or maybe I just finally caught up to you.", "none", "ParkMorning.jpg"),
            SceneEntry.narrator("She reaches out and takes your hand. Her fingers are cold, but they steady against yours.", "ParkMorning.jpg"),
            SceneEntry.dialogue("Amaya", "Stay with me… okay?", "single:Amaya_Blushing", "ParkMorning.jpg"),
            SceneEntry.dialogue("{name}", "I'm not going anywhere.", "none", "ParkMorning.jpg"),
            SceneEntry.narrator("For the first time in a long while…", "ParkMorning.jpg"),
            SceneEntry.narrator("Her smile reaches her eyes.", "ParkMorning.jpg"),
            SceneEntry.narrator("And this time… you walk forward together.", "ParkMorning.jpg"),
            SceneEntry.narrator("★ AMAYA GOOD ENDING ★", "EndingGood.jpg")
        };
    }
    
    // ── Neutral Ending (40-59 LP) ─────────────────────────────────────────────
    public static SceneEntry[] getNeutralEnding() {
        return new SceneEntry[] {
            SceneEntry.narrator("The morning feels quieter than usual. It was the weekend, and the event slowly made everyone exhausted the day after. They were all exhausted from the cheering, but you were exhausted for another reason.", "MorningOffice.jpg"),
            SceneEntry.narrator("After everything that happened last night…", "MorningOffice.jpg"),
            SceneEntry.narrator("You can't stop thinking about her.", "MorningOffice.jpg"),
            SceneEntry.narrator("But, no chats were made and days passed by like normal. Too normal.", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "Morning.", "single:Amaya_Casual", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "Morning.", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "About yesterday…", "single:Amaya_Casual", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "Yeah?", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "Sorry you had to see that. I was just… tired.", "single:Amaya_Sad", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "It's okay.", "none", "MorningOffice.jpg"),
            SceneEntry.narrator("She nods. You can see her putting the mask back on, piece by piece.", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "I think I'll be fine.", "single:Amaya_Smile", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "That's good.", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "Thanks… for being there.", "single:Amaya_Smile", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "Anytime.", "none", "MorningOffice.jpg"),
            SceneEntry.narrator("The words feel right…", "MorningOffice.jpg"),
            SceneEntry.narrator("But something is missing.", "MorningOffice.jpg"),
            SceneEntry.narrator("You watch her walk to her desk. Her shoulders are straight. Her smile is in place.", "MorningOffice.jpg"),
            SceneEntry.narrator("But you notice – she doesn't look back at you.", "MorningOffice.jpg"),
            SceneEntry.dialogue("Cloma", "(passing by) Hey, you two. Event photos are up. You look miserable in half of them, Amaya.", "single:Cloma_Smile", "MorningOffice.jpg"),
            SceneEntry.dialogue("Amaya", "(forced laugh) That's just my face.", "single:Amaya_Smile", "MorningOffice.jpg"),
            SceneEntry.narrator("She walks away to look at the photos. You follow, but there's distance between you now. Not physical. Something else.", "MorningOffice.jpg"),
            SceneEntry.dialogue("Celeres", "(quietly, to you) Did something happen?", "single:Celeres_Casual", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "I don't know. Maybe.", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Celeres", "She's been quiet all morning. More than usual.", "single:Celeres_Casual", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "She said she's fine.", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Celeres", "(doubtful) …Yeah. She always says that.", "single:Celeres_Sad", "MorningOffice.jpg"),
            SceneEntry.narrator("You want to say something. Ask her again. Push harder.", "MorningOffice.jpg"),
            SceneEntry.narrator("But you don't.", "MorningOffice.jpg"),
            SceneEntry.narrator("You both return to work like nothing changed.", "MorningOffice.jpg"),
            SceneEntry.narrator("The calls come. The customers yell. You smile through it all.", "MorningOffice.jpg"),
            SceneEntry.narrator("And at the end of the day, you walk home separately.", "MorningOffice.jpg"),
            SceneEntry.narrator("Maybe… you were close to something more.", "MorningOffice.jpg"),
            SceneEntry.narrator("But not quite enough.", "MorningOffice.jpg"),
            SceneEntry.narrator("★ AMAYA NEUTRAL ENDING ★", "EndingNeutral.jpg")
        };
    }
    
    // ── Bad Ending (Below 40 LP) ──────────────────────────────────────────────
    public static SceneEntry[] getBadEnding() {
        return new SceneEntry[] {
            SceneEntry.narrator("The morning feels quieter than usual. It was the weekend, and the event slowly made everyone exhausted the day after. They were all exhausted from the cheering, but you were exhausted for another reason.", "MorningOffice.jpg"),
            SceneEntry.narrator("After everything that happened last night…", "MorningOffice.jpg"),
            SceneEntry.narrator("You can't stop thinking about her.", "MorningOffice.jpg"),
            SceneEntry.narrator("But, no chats were made and when Monday arrived. The office feels colder today.", "MorningOffice.jpg"),
            SceneEntry.narrator("… Amaya isn't here.", "MorningOffice.jpg"),
            SceneEntry.narrator("Her desk is clean. Empty. Like she was never there at all.", "MorningOffice.jpg"),
            SceneEntry.dialogue("Cloma", "(approaching you carefully) You didn't hear?", "single:Cloma_Sad", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "Hear what?", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Celeres", "(appearing behind Cloma) She filed for leave. Indefinite.", "single:Celeres_Sad", "MorningOffice.jpg"),
            SceneEntry.dialogue("Cloma", "Family reasons. Her mom's condition got worse. She says she is going to work closer to her family from now on. Be there for the months she can have with them.", "single:Cloma_Sad", "MorningOffice.jpg"),
            SceneEntry.dialogue("{name}", "Did she say anything? To me?", "none", "MorningOffice.jpg"),
            SceneEntry.dialogue("Celeres", "…No. I'm sorry.", "single:Celeres_Sad", "MorningOffice.jpg"),
            SceneEntry.narrator("Days pass. No messages. No calls. Just silence.", "MorningOffice.jpg"),
            SceneEntry.narrator("You think back to every moment you could've said something. Asked something. Done something.", "MorningOffice.jpg"),
            SceneEntry.narrator("But you didn't.", "MorningOffice.jpg"),
            SceneEntry.narrator("You watched her smile fade. You watched her shoulders slump. You watched her drown.", "MorningOffice.jpg"),
            SceneEntry.narrator("And, still you said nothing.", "MorningOffice.jpg"),
            SceneEntry.narrator("So she's gone.", "MorningOffice.jpg"),
            SceneEntry.narrator("★ AMAYA BAD ENDING ★", "EndingBad.jpg")
        };
    }
    
    @Override
    public SceneEntry[] getMorningScenes() { return MORNING; }
    @Override
    public SceneEntry[] getEveningScenes() { return EVENING; }
    
    @Override
    public ChoiceEntry[] getChoicesForScene(int sceneIndex, String segment) {
        return null;
    }
}