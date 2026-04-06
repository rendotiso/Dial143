package Storyline;

public interface DayInterface {

    SceneEntry[] getMorningScenes();
    SceneEntry[] getEveningScenes();
    ChoiceEntry[] getChoicesForScene(int sceneIndex, String segment);
}