package ui.elements.label;

import calculators.Result;
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
        int tGain = result.engine().tGain() + result.turbo().tGain() + result.trans().tGain() + result.suspension().tGain() + result.brakes().tGain() + result.tires().tGain();
        int aGain = result.engine().aGain() + result.turbo().aGain() + result.trans().aGain() + result.suspension().aGain() + result.brakes().aGain() + result.tires().aGain();
        int hGain = result.engine().hGain() + result.turbo().hGain() + result.trans().hGain() + result.suspension().hGain() + result.brakes().hGain() + result.tires().hGain();
        System.out.println("t: " + tGain + ", a: " + aGain + ", h: " + hGain);
        if (tGain < 0) {
            tGain *= -(aGain + hGain) / 300;
        }

        if (aGain < 0) {
            aGain *= -(tGain + hGain) / 25;
        }

        if (hGain < 0) {
            hGain *= -(tGain + aGain) / 100;
        }
        int commonDivisor = 150 + tGain + aGain + hGain;
        double FINAL_DRIVE = partCalc(tGain, aGain, hGain, commonDivisor, car.FINAL_DRIVE);
        double RPM = partCalc(tGain, aGain, hGain, commonDivisor, car.cRPM);
        double RIM_SIZE = partCalc(tGain, aGain, hGain, commonDivisor, car.RIM_SIZE);
        double SECTION_WIDTH = partCalc(tGain, aGain, hGain, commonDivisor, car.SECTION_WIDTH);
        double ASPECT_RATIO = partCalc(tGain, aGain, hGain, commonDivisor, car.ASPECT_RATIO);
        double TyreCircumference = Math.PI * (RIM_SIZE * 25.4 + ((SECTION_WIDTH * ASPECT_RATIO) / 50));
        for (int i = 2; i < 9; i++) {
            double gearRatio = car.gearRatio().length > 9 ? partCalc(tGain, aGain, hGain, commonDivisor, car.GEAR_RATIO[i]) : car.gearRatio()[i];
            if (gearRatio <= 0) {
                jLabels[i - 2].setText("");
                continue;
            }
            double speedKMH = ((RPM / gearRatio / FINAL_DRIVE) * TyreCircumference) * 0.00006;
            setSpeed(jLabels[i - 2], speedKMH);
        }
    }
    private double partCalc(int tGain, int aGain, int hGain, int commonDivisor, double[] array) {
        return array.length == 1 ? array[0] : (tGain*array[0] + aGain*array[1] + hGain*array[2] + array[3]) / commonDivisor;
    }
}
