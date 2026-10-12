package me.ipana.car.value;

public class Blender {
    public static float blend(float hatConstants, float h, float a, float t, float value0, float value1, float value2, float value3) {
        return hatConstants * value0
                + h * value1
                + a * value2
                + t * value3;
    }
    public static float blend(int hGain, int aGain, int tGain, FloatValue value) {
        float sum = tGain + aGain + hGain;
        float scale = 3f / (300f + 2f * sum);

        float tp = Math.max(0f, tGain);
        float ap = Math.max(0f, aGain);
        float hp = Math.max(0f, hGain);

        float t = tp * scale;
        float a = ap * scale;
        float h = hp * scale;
        float hatConstant = 1f - (tp + ap + hp) * scale;
        return blend(hatConstant, h, a, t, value);
    }
    public static float blend(float hatConstants, float h, float a, float t, FloatValue value) {
        return blend(hatConstants, h, a, t, value.array[0], value.array[1], value.array[2], value.array[3]);
    }
    public static  float blend(float hatConstants, float h, float a, float t, FloatArrayValue value, int i) {
        return blend(hatConstants, h, a, t, value.array[i], value.array[value.length + i], value.array[value.length * 2 + i], value.array[value.length * 3 + i]);
    }
    public static  float blendFront(float hatConstants, float h, float a, float t, AxlePairValue[] value) {
        return blend(hatConstants, h, a, t, value[0].Front, value[1].Front, value[2].Front, value[3].Front);
    }
    public static  float blendRear(float hatConstants, float h, float a, float t, AxlePairValue[] value) {
        return blend(hatConstants, h, a, t, value[0].Rear, value[1].Rear, value[2].Rear, value[3].Rear);
    }
}
