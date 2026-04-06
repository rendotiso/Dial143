package Storyline.AmayaRoute;

import Storyline.*;
import Storyline.DayInterface;

public class AmayaRoute extends RouteManager {
    
    private static final String ROUTE_NAME = "Amaya";
    private static final int THRESHOLD_GOOD = 60;
    private static final int THRESHOLD_NEUTRAL = 40;
    
    public AmayaRoute() {
        super(ROUTE_NAME, THRESHOLD_GOOD, THRESHOLD_NEUTRAL);
    }
    
    @Override
    public DayInterface getDayScript(int day) {
        return switch (day) {
            case 1 -> new Day1();
            case 2 -> new Day2();
            case 3 -> new Day3Amaya(); 
            case 4 -> new Day4Amaya();
            case 5 -> new Day5Amaya();
            case 6 -> new Day6Amaya();
            case 7 -> new Day7Amaya();
            default -> new Day1();
        };
    }
    
    @Override
    public int getTotalDays() {
        return 7;
    }
    
    @Override
    public SceneEntry getEndingScene(int finalLP, int currentDay, 
                                      SceneManager.Segment segment) {
        if (currentDay == 7 && segment == SceneManager.Segment.EVENING) {
            if (finalLP >= THRESHOLD_GOOD) {
                return createGoodEnding();
            } else if (finalLP >= THRESHOLD_NEUTRAL) {
                return createNeutralEnding();
            } else {
                return createBadEnding();
            }
        }
        return null;
    }
    
    private SceneEntry createGoodEnding() {
        return SceneEntry.narrator(
            "Amaya Good Ending - She pursues her art dreams and finds happiness with you.",
            "EndingGood.jpg"
        );
    }
    
    private SceneEntry createNeutralEnding() {
        return SceneEntry.narrator(
            "Amaya Neutral Ending - You remain friends, but she's still stuck in her routine.",
            "EndingNeutral.jpg"
        );
    }
    
    private SceneEntry createBadEnding() {
        return SceneEntry.narrator(
            "Amaya Bad Ending - She leaves to help her family, and you never see her again.",
            "EndingBad.jpg"
        );
    }
}