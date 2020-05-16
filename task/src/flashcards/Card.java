package flashcards;

/**
 * Created by djames
 * 16/05/2020  8:21 PM
 */
public class Card {
    private String name;
    private String definition;
    private int mistakes = 0;

    public Card(String name, String definition, int mistakes) {
        this.name = name;
        this.definition = definition;
        this.mistakes = mistakes;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return this.definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getMistakes() {
        return this.mistakes;
    }

    public void addMistake() {
        this.mistakes += 1;
    }

    public void setMistakes(int additionalMistakes) {
        this.mistakes = additionalMistakes;
    }

    public void resetMistakes() {
        this.mistakes = 0;
    }
}
