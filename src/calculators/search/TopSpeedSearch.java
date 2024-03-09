package calculators.search;

import cars.Car;

public class TopSpeedSearch implements ISearch {
    @Override
    public float additionalValue(Car car, int tGain, int aGain, int hGain) {
        float t = tGain;
        float a = aGain;
        float h = hGain;
        if (tGain < 0) {
            float m = 150.0f / (150 + tGain);
            a *= m;
            h *= m;
            t = 0;
        }
        if (aGain < 0) {
            float m = 150.0f / (150 + aGain);
            t *= m;
            h *= m;
            a = 0;
        }
        if (hGain < 0) {
            float m = 150.0f / (150 + hGain);
            t *= m;
            a *= m;
            h = 0;
        }
        float divisor = 150 + t + a + h;
        float FINAL_DRIVE = car.FINAL_DRIVE.calculate(h, a, t, divisor);
        float RPM = car.RPM.calculate(h, a, t, divisor);
        float RIM_SIZE = car.RIM_SIZE.calculate(h, a, t, divisor);
        float SECTION_WIDTH = car.SECTION_WIDTH.calculate(h, a, t, divisor);
        float ASPECT_RATIO = car.ASPECT_RATIO.calculate(h, a, t, divisor);
        float GEAR_RATIO = car.GEAR_RATIO[car.MAX_GEAR_INDEX].calculate(h, a, t, divisor);
        float TyreCircumference = RIM_SIZE + SECTION_WIDTH * ASPECT_RATIO / 50;
        return (RPM / GEAR_RATIO / FINAL_DRIVE) * TyreCircumference;
    }
}
