package me.ipana.calculator.search;

import me.ipana.calculator.SumCache;
import me.ipana.car.Car;
import me.ipana.car.value.Blender;

public class TopSpeedSearch implements ISearch {
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
        float RPM = Blender.blend(hatConstant, h, a, t, car.rpm);
        float GEAR_RATIO = Blender.blend(hatConstant, h, a, t, car.gearRatio, car.MAX_GEAR_INDEX);
        float TORQUE_SPLIT = Blender.blend(hatConstant, h, a, t, car.torqueSplit);
        float WHEEL_RADIUS_F = Blender.blendFront(hatConstant, h, a, t, car.rimSize) + Blender.blendFront(hatConstant, h, a, t, car.sectionWidth) * Blender.blendFront(hatConstant, h, a, t, car.aspectRatio) / 50;
        float WHEEL_RADIUS_R = Blender.blendRear(hatConstant, h, a, t, car.rimSize) + Blender.blendRear(hatConstant, h, a, t, car.sectionWidth) * Blender.blendRear(hatConstant, h, a, t, car.aspectRatio) / 50;
        float TyreCircumference = TORQUE_SPLIT <= 0 ? WHEEL_RADIUS_R : TORQUE_SPLIT >= 1 ? WHEEL_RADIUS_F : Math.min(WHEEL_RADIUS_F, WHEEL_RADIUS_R);
        return (RPM / GEAR_RATIO / FINAL_DRIVE) * TyreCircumference;
    }
}
