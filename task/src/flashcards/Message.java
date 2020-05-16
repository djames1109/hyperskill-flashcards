package flashcards;

/**
 * Created by djames
 * 16/05/2020  8:22 PM
 */
public enum Message {
    DEFINITION_ALREADY_EXISTS("The definition \"%s\" already exists."),
    CARD_EXISTS("The card \"%s\" already exists."),
    CARD_ADDED("The pair (\"%s\":\"%s\") has been added."),
    CARD_NOT_FOUND("Can't remove \"%s\": there is no such card."),
    CARDS_REMOVED("The card has been removed."),
    CARDS_LOADED("%d cards have been loaded."),
    CARDS_SAVED("%d cards have been saved."),
    FILE_NOT_FOUND("File not found."),
    CORRECT_ANSWER("Correct answer"),
    DIFFERENT_ANSWER("Wrong answer. The correct one is \"%s\", you've just written the definition of \"%s\"."),
    WRONG_ANSWER("Wrong answer. The correct one is \"%s\".");
    private final String template;

    Message(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return this.template;
    }
}
