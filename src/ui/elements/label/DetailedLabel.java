package ui.elements.label;

import calculators.result.Result;
import cars.Car;
import ui.UI;
import utils.MathUtil;

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
        float WHEEL_RADIUS = RIM_SIZE + SECTION_WIDTH * ASPECT_RATIO / 50;
        float MASS = car.MASS.calculate(h, a, t, divisor);
        float COEFFICIENT = car.COEFFICIENT.calculate(h, a, t, divisor);
        float TyreCircumference = (float) (Math.PI * WHEEL_RADIUS);
        float lastSpeed = 0;
        for (int i = 2; i < 9; i++) {
            float GEAR_RATIO =  car.GEAR_RATIO[i].calculate(h, a, t, divisor);
            if (GEAR_RATIO <= 0) {
                jLabels[i - 2].setText("");
                continue;
            }
            float ENGINE_TORQUE = car.TORQUE[i].calculate(h, a, t, divisor);
            float GEAR_EFFICIENCY = car.GEAR_EFFICIENCY[i].calculate(h, a, t, divisor);
            float WHEEL_TORQUE = ENGINE_TORQUE * GEAR_RATIO * FINAL_DRIVE * GEAR_EFFICIENCY;
            float speedKMH = (RPM / GEAR_RATIO / FINAL_DRIVE) * TyreCircumference * 0.00006f;
            /*
                ρ = air density [kg/m3] ≈ 1.202 kg/m3, at sea level and at 15o C
                Af = car frontal area [m2] ≈ 1.2 : 3.2 m2, for small and mid size cars
                Cd = coefficient of aerodynamics resistance (drag coefficient) ≈ 0.2 : 0.5 for cars
                v = car relative velocity [km/h]
                vcar = car velocity [km/h] (vcar = v, at stand still wind, vwind = 0)
                vwind = wind velocity [km/h]
                AR = 0.5 * p * Af * Cd * (v/3.6)^2
            */
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
