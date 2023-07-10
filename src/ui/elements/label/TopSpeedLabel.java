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
        System.out.println("t: "+tGain+", a: "+aGain+", h: "+hGain);
        if (tGain < 0) {
            tGain *= -(aGain+hGain)/300;
        }

        if (aGain < 0) {
            aGain *= -(tGain+hGain)/25;
        }

        if (hGain < 0) {
            hGain *= -(tGain+aGain)/100;
        }
        double commonDivisor = 150 + tGain + aGain + hGain;
        double FINAL_DRIVE = (tGain*car.cFINAL_DRIVE[0] + aGain*car.cFINAL_DRIVE[1] + hGain*car.cFINAL_DRIVE[2] + car.cFINAL_DRIVE[3]) / commonDivisor;
        double RPM = (tGain*car.cRPM[0] + aGain*car.cRPM[1] + hGain*car.cRPM[2] + car.cRPM[3]) / commonDivisor;
        double RIM_SIZE = (tGain*car.RIM_SIZE[0] + aGain*car.RIM_SIZE[1] + hGain*car.RIM_SIZE[2] + car.RIM_SIZE[3]) / commonDivisor;
        double SECTION_WIDTH = (tGain*car.SECTION_WIDTH[0] + aGain*car.SECTION_WIDTH[1] + hGain*car.SECTION_WIDTH[2] + car.SECTION_WIDTH[3]) / commonDivisor;
        double ASPECT_RATIO = (tGain*car.ASPECT_RATIO[0] + aGain*car.ASPECT_RATIO[1] + hGain*car.ASPECT_RATIO[2] + car.ASPECT_RATIO[3]) / commonDivisor;
        double TyreCircumference = Math.PI * (RIM_SIZE * 25.4 + ((SECTION_WIDTH * ASPECT_RATIO) / 100.0) * 2);

        for (int i = 2; i < car.gearRatio().length; i++) {
            double gearRatio = car.gearRatio()[i];
            if (gearRatio <= 0) {
                jLabels[i-2].setText("");
                continue;
            }
            double speedKMH = (((RPM / gearRatio / FINAL_DRIVE) * TyreCircumference) / 1000000) * 60;
            setSpeed(jLabels[i-2], speedKMH);
        }
    }
}
