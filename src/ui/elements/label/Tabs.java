package ui.elements.label;

public enum Tabs {
    STAT("Stat", "T", "A", "H"),
    NODE("Node Gain", "T", "A", "H"),
    TOP_SPEED_KMH("Speed KMH");

    private String name;
    private String[] fieldNames;
    public static final Tabs[] VALUES = Tabs.values();

    Tabs(String name, String... fieldNames) {
        this.name = name;
        this.fieldNames = fieldNames;
    }

    public String fullName() {
        return name;
    }

    public String[] fieldNames() {
        return fieldNames;
    }
}
