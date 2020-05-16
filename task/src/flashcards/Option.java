package flashcards;

import java.util.Arrays;

/**
 * Created by djames
 * 16/05/2020  8:21 PM
 */
public enum Option {

    ADD("add"),
    REMOVE("remove"),
    IMPORT("import"),
    EXPORT("export"),
    ASK("ask"),
    LOG("log"),
    HARDEST_CARD("hardest card"),
    RESET_STATS("reset stats"),
    EXIT("exit");

    private final String value;

    Option(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Option getByValue(String value) {
        return Arrays.stream(values())
                .filter(o -> o.getValue().equalsIgnoreCase(value))
                .findFirst().get();
    }
}
