package calculators.search;

import cars.Car;

public class DefaultSearch implements ISearch {
    @Override
    public float additionalValue(Car car, int tGain, int aGain, int hGain) {
        return 0;
    }
}
