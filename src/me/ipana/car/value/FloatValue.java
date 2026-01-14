package me.ipana.car.value;

public class FloatValue {
    public float[] array;

    public FloatValue() {
        this.array = new float[4];
    }

    public void put(int i, float value) {
        array[i] = value;
    }
}
