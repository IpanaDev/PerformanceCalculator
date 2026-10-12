package me.ipana.calculator;

public class SumCache {
    public static final float[] INV_1 = new float[300];
    public static final float[] INV_2 = new float[300];
    static {
        for (int i = 0; i < INV_1.length; i++) {
            INV_1[i] = 1f / (150 + i);
        }
        for (int i = 0; i < INV_2.length; i++) {
            INV_2[i] = 1.5f / (150f + i);
        }
    }
}
