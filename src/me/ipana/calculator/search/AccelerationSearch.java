package me.ipana.calculator.search;

import me.ipana.calculator.SumCache;
import me.ipana.car.Car;
import me.ipana.car.value.Blender;

public class AccelerationSearch implements ISearch {
    @Override
    public float additionalValue(Car car, int tGain, int aGain, int hGain) {
        float scale = SumCache.INV_2[tGain + aGain + hGain];

        float tp = Math.max(0f, tGain);
        float ap = Math.max(0f, aGain);
        float hp = Math.max(0f, hGain);

        float t = tp * scale;
        float a = ap * scale;
        float h = hp * scale;
        float hatConstant = 1f - (tp + ap + hp) * scale;
        float FINAL_DRIVE = Blender.blend(hatConstant, h, a, t, car.finalGear);
        float TORQUE_SPLIT = Blender.blend(hatConstant, h, a, t, car.torqueSplit);
        float WHEEL_RADIUS_F = Blender.blendFront(hatConstant, h, a, t, car.rimSize) + Blender.blendFront(hatConstant, h, a, t, car.sectionWidth) * Blender.blendFront(hatConstant, h, a, t, car.aspectRatio) / 50;
        float WHEEL_RADIUS_R = Blender.blendRear(hatConstant, h, a, t, car.rimSize) + Blender.blendRear(hatConstant, h, a, t, car.sectionWidth) * Blender.blendRear(hatConstant, h, a, t, car.aspectRatio) / 50;
        float WHEEL_RADIUS = TORQUE_SPLIT <= 0 ? WHEEL_RADIUS_R : TORQUE_SPLIT >= 1 ? WHEEL_RADIUS_F : Math.min(WHEEL_RADIUS_F, WHEEL_RADIUS_R);
        float MASS = Blender.blend(hatConstant, h, a, t, car.mass);
        float COEFFICIENT = Blender.blend(hatConstant, h, a, t, car.coefficient);
        float ACCELERATION = 0;
        for (int i = 4; i < 9; i++) {//Skipping first & second gear
            float GEAR_RATIO =  Blender.blend(hatConstant, h, a, t, car.gearRatio, i);
            if (GEAR_RATIO == 0)
                break;
            float ENGINE_TORQUE = Blender.blend(hatConstant, h, a, t, car.torque, i);
            float GEAR_EFFICIENCY = Blender.blend(hatConstant, h, a, t, car.gearEfficiency, i);
            float WHEEL_TORQUE = ENGINE_TORQUE * GEAR_RATIO * FINAL_DRIVE * GEAR_EFFICIENCY;
            float FORCE = WHEEL_TORQUE / (WHEEL_RADIUS / 1000) - COEFFICIENT;
            ACCELERATION += FORCE / MASS;
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
        return ACCELERATION;
    }
}
