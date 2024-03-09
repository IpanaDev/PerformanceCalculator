package calculators.search;

import cars.Car;

public class AccelerationSearch implements ISearch {
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
        float RIM_SIZE = car.RIM_SIZE.calculate(h, a, t, divisor);
        float SECTION_WIDTH = car.SECTION_WIDTH.calculate(h, a, t, divisor);
        float ASPECT_RATIO = car.ASPECT_RATIO.calculate(h, a, t, divisor);
        float GEAR_RATIO = car.GEAR_RATIO[car.MAX_GEAR_INDEX].calculate(h, a, t, divisor);
        float ENGINE_TORQUE = car.TORQUE[car.MAX_GEAR_INDEX].calculate(h, a, t, divisor);
        float MASS = car.MASS.calculate(h, a, t, divisor);
        float COEFFICIENT = car.COEFFICIENT.calculate(h, a, t, divisor);
        float WHEEL_RADIUS = RIM_SIZE + SECTION_WIDTH * ASPECT_RATIO / 50;
        float WHEEL_TORQUE = ENGINE_TORQUE * GEAR_RATIO * FINAL_DRIVE;
        if (car.GEAR_EFFICIENCY != null) {
            WHEEL_TORQUE *= car.GEAR_EFFICIENCY[car.MAX_GEAR_INDEX].calculate(h, a, t, divisor);
        }
        /*
            ρ = air density [kg/m3] ≈ 1.202 kg/m3, at sea level and at 15o C
            Af = car frontal area [m2] ≈ 1.2 : 3.2 m2, for small and mid size cars
            Cd = coefficient of aerodynamics resistance (drag coefficient) ≈ 0.2 : 0.5 for cars
            v = car relative velocity [km/h]
            vcar = car velocity [km/h] (vcar = v, at stand still wind, vwind = 0)
            vwind = wind velocity [km/h]
            AR = 0.5 * p * Af * Cd * (v/3.6)^2

            p and Af is unknown, but they are static no need to include in iteration
         */
        float FORCE = WHEEL_TORQUE / (WHEEL_RADIUS / 1000) - COEFFICIENT;
        return FORCE / MASS;
    }
}
