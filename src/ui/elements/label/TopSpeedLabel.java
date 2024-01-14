package ui.elements.label;

import calculators.result.Result;
import cars.Car;
import ui.UI;

import javax.swing.*;

public class TopSpeedLabel {
    private JLabel[] jLabels;

    public TopSpeedLabel(UI ui, int x, int y, int width, int height) {
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

    private void setSpeed(JLabel jLabel, double speed) {
        jLabel.setText(speed+" Km/h");
    }

    public void calcAndSet(Result result, Car car) {
        int t = result.engine().tGain() + result.turbo().tGain() + result.trans().tGain() + result.suspension().tGain() + result.brakes().tGain() + result.tires().tGain();
        int a = result.engine().aGain() + result.turbo().aGain() + result.trans().aGain() + result.suspension().aGain() + result.brakes().aGain() + result.tires().aGain();
        int h = result.engine().hGain() + result.turbo().hGain() + result.trans().hGain() + result.suspension().hGain() + result.brakes().hGain() + result.tires().hGain();
        double tGain = t;
        double aGain = a;
        double hGain = h;
        if (t < 0) {
            double m = 150.0 / (150 + t);
            aGain *= m;
            hGain *= m;
            tGain = 0;
        }
        if (a < 0) {
            double m = 150.0 / (150 + a);
            tGain *= m;
            hGain *= m;
            aGain = 0;
        }
        if (h < 0) {
            double m = 150.0 / (150 + h);
            tGain *= m;
            aGain *= m;
            hGain = 0;
        }
        double commonDivisor = 150 + tGain + aGain + hGain;
        double FINAL_DRIVE = car.FINAL_DRIVE.calculate(tGain, aGain, hGain, commonDivisor);
        double RPM = car.cRPM.calculate(tGain, aGain, hGain, commonDivisor);
        double RIM_SIZE = car.RIM_SIZE.calculate(tGain, aGain, hGain, commonDivisor);
        double SECTION_WIDTH = car.SECTION_WIDTH.calculate(tGain, aGain, hGain, commonDivisor);
        double ASPECT_RATIO = car.ASPECT_RATIO.calculate(tGain, aGain, hGain, commonDivisor);

        double TyreCircumference = Math.PI * (RIM_SIZE * 25.4 + ((SECTION_WIDTH * ASPECT_RATIO) / 50));
        for (int i = 2; i < 9; i++) {
            double gearRatio =  car.GEAR_RATIO[i].calculate(tGain, aGain, hGain, commonDivisor);
            if (gearRatio <= 0) {
                jLabels[i - 2].setText("");
                continue;
            }
            double speedKMH = (RPM / gearRatio / FINAL_DRIVE) * TyreCircumference * 0.00006;
            setSpeed(jLabels[i - 2], speedKMH);
        }
    }
}
