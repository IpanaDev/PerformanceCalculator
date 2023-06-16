package ui.elements.label;

import calculators.Result;
import cars.Car;
import ui.UI;

import javax.swing.*;
import java.lang.reflect.Field;

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
        for (int i = 2; i < car.gearRatio().length; i++) {
            double gearRatio = car.gearRatio()[i];
            if (gearRatio <= 0) {
                jLabels[i-2].setText("");
                continue;
            }
            int tGain = Math.abs(result.engine().tGain() + result.turbo().tGain() + result.trans().tGain() + result.suspension().tGain() + result.brakes().tGain() + result.tires().tGain());
            int aGain = Math.abs(result.engine().aGain() + result.turbo().aGain() + result.trans().aGain() + result.suspension().aGain() + result.brakes().aGain() + result.tires().aGain());
            int hGain = Math.abs(result.engine().hGain() + result.turbo().hGain() + result.trans().hGain() + result.suspension().hGain() + result.brakes().hGain() + result.tires().hGain());
            double tFinalDrive = car.finalDrives()[1];
            double aFinalDrive = car.finalDrives()[2];
            double hFinalDrive = car.finalDrives()[3];
            double sFinalDrive = car.finalDrives()[0];
            double multiplier = 1.5;
            double divider = multiplier-1;
            double funnyNumber = multiplier*100;
            double commonDivisor = funnyNumber + tGain + aGain + hGain;
            double finalDrive = (tGain*(tFinalDrive*multiplier - sFinalDrive*divider) + aGain*(aFinalDrive*multiplier - sFinalDrive*divider) + hGain*(hFinalDrive*multiplier - sFinalDrive*divider) + sFinalDrive*funnyNumber) / commonDivisor;
            double speedKMH = 0;
            switch (car.drivetrain()) {
                case REAR_WHEEL: {
                    double TyreCircumference = Math.PI * (car.wheelSize()[1] * 25.4 + ((car.wheelWidth()[1] * car.wheelRatio()[1]) / 100.0) * 2);
                    speedKMH = (((car.RPM() / gearRatio / finalDrive) * TyreCircumference) / 1000000) * 60;
                    break;
                }
                case FRONT_WHEEL: {
                    double TyreCircumference = Math.PI * (car.wheelSize()[0] * 25.4 + ((car.wheelWidth()[0] * car.wheelRatio()[0]) / 100.0) * 2);
                    speedKMH = (((car.RPM() / gearRatio / finalDrive) * TyreCircumference) / 1000000) * 60;
                    break;
                }
                case FOUR_WHEEL: {
                    double TyreCircumference = Math.PI * (car.wheelSize()[0] * 25.4 + ((car.wheelWidth()[0] * car.wheelRatio()[0]) / 100.0) * 2);
                    double TyreCircumference2 = Math.PI * (car.wheelSize()[1] * 25.4 + ((car.wheelWidth()[1] * car.wheelRatio()[1]) / 100.0) * 2);
                    speedKMH = Math.min((((car.RPM() / gearRatio / finalDrive) * TyreCircumference) / 1000000) * 60, (((car.RPM() / gearRatio / finalDrive) * TyreCircumference2) / 1000000) * 60);
                    break;
                }
                case ALL_WHEEL: {
                    double TyreCircumference = Math.PI * (car.wheelSize()[0] * 25.4 + ((car.wheelWidth()[0] * car.wheelRatio()[0]) / 100.0) * 2);
                    double TyreCircumference2 = Math.PI * (car.wheelSize()[1] * 25.4 + ((car.wheelWidth()[1] * car.wheelRatio()[1]) / 100.0) * 2);
                    speedKMH = Math.max((((car.RPM() / gearRatio / finalDrive) * TyreCircumference) / 1000000) * 60, (((car.RPM() / gearRatio / finalDrive) * TyreCircumference2) / 1000000) * 60);
                    break;
                }
            }
            setSpeed(jLabels[i-2], speedKMH);
        }
    }
}
