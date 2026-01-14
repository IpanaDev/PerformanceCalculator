package me.ipana.car.value;

import vaultlib.core.types.EAReflection.Float;

import java.util.List;

public class FloatArrayValue {
    public float[] array;
    public int length;

    public FloatArrayValue(int length) {
        this.length = length;
        this.array = new float[4*length];
    }

    public void put(int index, List<Float> value) {
        if (value.size() > length) {
            throw new RuntimeException("FLOAT ARRAY TYPE LENGTH MISMATCH ("+value.size()+", "+length+")");
        }
        for (int i = 0; i < value.size(); i++) {
            array[index*length + i] = value.get(i).GetValue();
        }
    }
}
