package me.ipana.calculator.search;

import me.ipana.car.Car;

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
        float GEAR_RATIO = car.GEAR_RATIO[car.MAX_GEAR_INDEX].calculate(h, a, t, divisor);
        float TORQUE_SPLIT = car.TORQUE_SPLIT.calculate(h, a, t, divisor);
        float WHEEL_RADIUS_F = car.RIM_SIZE_FRONT.calculate(h, a, t, divisor) + car.SECTION_WIDTH_FRONT.calculate(h, a, t, divisor) * car.ASPECT_RATIO_FRONT.calculate(h, a, t, divisor) / 50;
        float WHEEL_RADIUS_R = car.RIM_SIZE_REAR.calculate(h, a, t, divisor) + car.SECTION_WIDTH_REAR.calculate(h, a, t, divisor) * car.ASPECT_RATIO_REAR.calculate(h, a, t, divisor) / 50;
        float TyreCircumference = TORQUE_SPLIT <= 0 ? WHEEL_RADIUS_R : TORQUE_SPLIT >= 1 ? WHEEL_RADIUS_F : Math.min(WHEEL_RADIUS_F, WHEEL_RADIUS_R);
        return (RPM / GEAR_RATIO / FINAL_DRIVE) * TyreCircumference;
    }
}
