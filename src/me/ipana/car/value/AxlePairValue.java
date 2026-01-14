package me.ipana.car.value;

import vaultlib.frameworks.AxlePair;

public class AxlePairValue {
    public float Front, Rear;

    public AxlePairValue(AxlePair axlePair) {
        this(axlePair.Front, axlePair.Rear);
    }

    public AxlePairValue(float Front, float Rear) {
        this.Front = Front;
        this.Rear = Rear;
    }
}
