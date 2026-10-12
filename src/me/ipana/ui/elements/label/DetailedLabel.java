package me.ipana.ui.elements.label;

import me.ipana.calculator.SumCache;
import me.ipana.calculator.result.Result;
import me.ipana.car.Car;
import me.ipana.car.value.AxlePairValue;
import me.ipana.car.value.Blender;
import me.ipana.car.value.FloatArrayValue;
import me.ipana.car.value.FloatValue;
import me.ipana.ui.UI;
import me.ipana.utils.MathUtil;

import javax.swing.*;

public class DetailedLabel {
    private JLabel[] jLabels;

    public DetailedLabel(UI ui, int x, int y, int width, int height) {
        String[] labelNames = {"First","Second","Third","Fourth","Fifth","Sixth","Seventh"};
        this.jLabels = new JLabel[labelNames.length];
        int posY = y;
        for (int i = 0; i < labelNames.length; i++) {
            JLabel label = ui.createLabel("",x,posY, width/2-20, height);
            label.setBorder(BorderFactory.createTitledBorder(UI.BUTTON_BORDER, labelNames[i]));
            jLabels[i] = label;
            posY += height;
        }
    }

    private void setSpeed(JLabel jLabel, float speed, float acceleration) {
        jLabel.setText(speed+" Km/h    |    "+acceleration+" m/s²");
    }

    public void calcAndSet(Result result, Car car) {
        int tGain = result.engine().tGain() + result.turbo().tGain() + result.trans().tGain() + result.suspension().tGain() + result.brakes().tGain() + result.tires().tGain();
        int aGain = result.engine().aGain() + result.turbo().aGain() + result.trans().aGain() + result.suspension().aGain() + result.brakes().aGain() + result.tires().aGain();
        int hGain = result.engine().hGain() + result.turbo().hGain() + result.trans().hGain() + result.suspension().hGain() + result.brakes().hGain() + result.tires().hGain();
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
        float TORQUE_SPLIT = Blender.blend(hatConstant, h, a, t, car.torqueSplit);
        float WHEEL_RADIUS_F = Blender.blendFront(hatConstant, h, a, t, car.rimSize) + Blender.blendFront(hatConstant, h, a, t, car.sectionWidth) * Blender.blendFront(hatConstant, h, a, t, car.aspectRatio) / 50;
        float WHEEL_RADIUS_R = Blender.blendRear(hatConstant, h, a, t, car.rimSize) + Blender.blendRear(hatConstant, h, a, t, car.sectionWidth) * Blender.blendRear(hatConstant, h, a, t, car.aspectRatio) / 50;
        float WHEEL_RADIUS = TORQUE_SPLIT <= 0 ? WHEEL_RADIUS_R : TORQUE_SPLIT >= 1 ? WHEEL_RADIUS_F : Math.min(WHEEL_RADIUS_F, WHEEL_RADIUS_R);
        float MASS = Blender.blend(hatConstant, h, a, t, car.mass);
        float COEFFICIENT = Blender.blend(hatConstant, h, a, t, car.coefficient);
        float TyreCircumference = (float) (Math.PI * WHEEL_RADIUS);
        float lastSpeed = 0;
        for (int i = 2; i < 9; i++) {
            float GEAR_RATIO =  Blender.blend(hatConstant, h, a, t, car.gearRatio, i);
            if (GEAR_RATIO <= 0) {
                jLabels[i - 2].setText("");
                continue;
            }
            float ENGINE_TORQUE = Blender.blend(hatConstant, h, a, t, car.torque, i);
            float GEAR_EFFICIENCY = Blender.blend(hatConstant, h, a, t, car.gearEfficiency, i);
            float WHEEL_TORQUE = ENGINE_TORQUE * GEAR_RATIO * FINAL_DRIVE * GEAR_EFFICIENCY;
            float speedKMH = (RPM / GEAR_RATIO / FINAL_DRIVE) * TyreCircumference * 0.00006f;
            float AVG_SPEED = (speedKMH + lastSpeed) / 2;
            float AIR_DENSITY = 1f;
            float FRONTAL_AREA = 1f;
            float VELOCITY = AVG_SPEED * AVG_SPEED / 12.96f;
            float AIR_RESISTANCE = AIR_DENSITY * FRONTAL_AREA * COEFFICIENT * VELOCITY * 0.5f;
            float FORCE = WHEEL_TORQUE / (WHEEL_RADIUS / 1000) - AIR_RESISTANCE;
            float ACCELERATION = FORCE / MASS;
            setSpeed(jLabels[i - 2], MathUtil.roundUp(speedKMH, 3), MathUtil.roundUp(ACCELERATION, 3));
            lastSpeed = speedKMH;
        }
    }
}
