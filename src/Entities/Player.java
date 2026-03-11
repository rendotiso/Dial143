package Entities;

public class Player {
    private String name = "";
    private String gender = "";
    private String pronoun = "";
    
    public Player() {}
    
    public Player(String name, String gender, String pronoun) {
        this.name = name;
        this.gender = gender;
        this.pronoun = pronoun;
    }
    
    public void set(String name, String gender, String pronoun) {
        this.name = name;
        this.gender = gender;
        this.pronoun = pronoun;
    }
    
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getPronoun() { return pronoun; }
    
    public void setName(String name) { this.name = name; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPronoun(String pronoun) { this.pronoun = pronoun; }
}