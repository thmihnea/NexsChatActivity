package by.thmihnea.sql;

public enum TableType {

    /**
     * TableType enumerators which
     * allow us to have more structurally
     * well written code in our {@link SQLUtil} class.
     */
    PLAYER_DATA("player_data"),
    DATA_MESSAGES("data_messages");

    /**
     * Table name.
     */
    private final String name;

    /**
     * Constructor for each enum
     * value. {@link Enum}
     *
     * @param name Name of the table.
     */
    TableType(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the table.
     *
     * @return {@link String}
     */
    public String getName() {
        return this.name;
    }
}
