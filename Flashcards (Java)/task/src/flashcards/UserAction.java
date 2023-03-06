package flashcards;

public enum UserAction {
    ADD("add"),
    REMOVE("remove"),
    IMPORT("import"),
    EXPORT("export"),
    ASK("ask"),
    EXIT("exit"),
    LOG("log"),
    HARDEST_CARD("hardest card"),
    RESET_STATS("reset stats");

    private final String action;

    UserAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
