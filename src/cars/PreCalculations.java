package cars;

import cars.value.*;

public class PreCalculations {
    public static IValue preCalculationsTire(int tireIndex, AxlePairValue[] array) {
        boolean dynamic = false;
        AxlePairValue axlePair1 = array[0];
        for (int i = 1; i < array.length; i++) {
            AxlePairValue axlePair2 = array[i];
            if (axlePair2 != null && (tireIndex == 0 ? axlePair1.Front != axlePair2.Front : axlePair1.Rear != axlePair2.Rear)) {
                dynamic = true;
                break;
            }
        }

        float firstNode = tireIndex == 0 ? axlePair1.Front : axlePair1.Rear;
        if (dynamic) {
            float[] NEW_ARRAY = new float[4];
            for (int i = 0; i < NEW_ARRAY.length - 1; i++) {
                float node = tireIndex == 0 ? array[i + 1].Front : array[i + 1].Rear;
                NEW_ARRAY[i] = 1.5f*node - 0.5f*firstNode;
            }
            NEW_ARRAY[3] = 150*firstNode;
            return new DynamicValue(NEW_ARRAY);
        } else {
            return new StaticValue(firstNode);
        }
    }
    public static IValue preCalculations(FloatValue floatValue) {
        boolean dynamic = false;
        float f1 = floatValue.array[0];
        for (int i = 1; i < floatValue.array.length; i++) {
            float f2 = floatValue.array[i];
            if (f1 != f2) {
                dynamic = true;
                break;
            }
        }
        if (dynamic) {
            float[] NEW_ARRAY = new float[4];
            for (int i = 0; i < NEW_ARRAY.length-1; i++) {
                NEW_ARRAY[i] = 1.5f*floatValue.array[i+1] - 0.5f*floatValue.array[0];
            }
            NEW_ARRAY[3] = floatValue.array[0]*150;
            return new DynamicValue(NEW_ARRAY);
        } else {
            return new StaticValue(floatValue.array[0]);
        }
    }
    public static IValue[] preCalculationsArray(FloatArrayValue floatArrayValue) {
        boolean dynamic = false;
        for (int j = 0; j < floatArrayValue.length; j++) {
            float f1 = floatArrayValue.array[j];
            for (int i = 1; i < 4; i++) {
                float f2 = floatArrayValue.array[floatArrayValue.length * i + j];
                if (f1 != f2) {
                    dynamic = true;
                    break;
                }
            }
        }
        if (dynamic) {
            float[][] NEW_ARRAY = new float[floatArrayValue.length][4];
            for (int i = 0; i < NEW_ARRAY.length; i++) {
                for (int j = 0; j < 3; j++) {
                    NEW_ARRAY[i][j] = floatArrayValue.array[(j + 1) * floatArrayValue.length + i] * 1.5f - floatArrayValue.array[i] * 0.5f;
                }
            }
            for (int i = 0; i < NEW_ARRAY.length; i++) {
                NEW_ARRAY[i][3] = floatArrayValue.array[i] * 150;
            }
            DynamicValue[] dynamicValues = new DynamicValue[9];
            for (int i = 0; i < dynamicValues.length; i++) {
                dynamicValues[i] = new DynamicValue(NEW_ARRAY[i]);
            }
            return dynamicValues;
        } else {
            StaticValue[] staticValues = new StaticValue[9];
            for (int i = 0; i < staticValues.length; i++) {
                staticValues[i] = new StaticValue(floatArrayValue.array[i]);
            }
            return staticValues;
        }
    }
}
