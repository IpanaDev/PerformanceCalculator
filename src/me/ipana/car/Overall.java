package me.ipana.car;

public enum Overall {
    E_CLASS("E Class", 50, 249),
    D_CLASS("D Class",250,399),
    C_CLASS("C Class",400,499),
    B_CLASS("B Class",500,599),
    A_CLASS("A Class",600,749),
    S1_CLASS("S1 Class",750,849),
    S2_CLASS("S2 Class",850, 1000),
    CUSTOM("Custom OA",0, 0);

    Overall(String name, int min, int max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }
    private String name;
    private int min, max;

    public String fullName() {
        return name;
    }
    @Override
    public String toString() {
        return fullName();
    }
    public int min() {
        return min;
    }

    public int max() {
        return max;
    }
    public void max(int max) {
        this.max = max;
    }
}
