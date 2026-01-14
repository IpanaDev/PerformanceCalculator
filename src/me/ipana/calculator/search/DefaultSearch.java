package me.ipana.calculator.search;

import me.ipana.car.Car;

public class DefaultSearch implements ISearch {
    @Override
    public float additionalValue(Car car, int tGain, int aGain, int hGain) {
        return 0;
    }
}
