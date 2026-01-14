package me.ipana.ui.elements;

import me.ipana.calculator.result.Result;
import me.ipana.car.Car;
import me.ipana.ui.GraphUI;
import me.ipana.ui.elements.label.ResultLabel;
import me.ipana.utils.MathUtil;
import utils.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JGraphMenu extends JPanel {
    public ResultLabel resultLabel;
    public int graphX = 150;
    public int graphY = 530;
    public int graphW = 700;
    public int graphH = 500;
    public boolean renderDetails, prevRenderDetails;
    public FinalNode[] finalNodes;
    public float RPM;
    public float FINAL_DRIVE;
    public float TyreCircumference;
    public float COEFFICIENT;
    public float WHEEL_RADIUS;
    public float MASS;
    public float MAX_SPEED;
    public int mouseX, mouseY;

    public JGraphMenu(ResultLabel resultLabel) {
        this.resultLabel = resultLabel;
        this.setupStaticValues();
        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                renderDetails = mouseX >= graphX && graphX + graphW >= mouseX && mouseY >= graphY - graphH && graphY >= mouseY;
                if (renderDetails != prevRenderDetails || renderDetails) {
                    repaint(graphX, graphY-graphH, graphX+graphW, graphY);
                }
                prevRenderDetails = renderDetails;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);


        g.setColor(Color.WHITE);
        g.drawLine(graphX, graphY, graphX + graphW, graphY);
        g.drawLine(graphX, graphY - graphH, graphX, graphY);
        g.drawString("Acceleration", graphX - 30, graphY - graphH - 3);
        g.drawString("Speed", graphX + graphW + 3, graphY + 5);

        g.drawString("Gears:", graphX - 40, graphY + 15);
        g.drawString("1.", graphX - 3, graphY + 15);
        float accelX = graphX;
        float accelY = graphY - graphH;

        float xOff = graphW / MAX_SPEED;
        float yOff = graphH / Y(0).value();
        int lastGear = 1;
        float accelAt = -1;
        int speedAt = -1;
        int detailX = -1;
        int detailY = -1;
        for (int i = 0; i < MAX_SPEED; i++) {
            Pair<Integer, Float> pair = Y(i);
            if (lastGear != pair.key()) {
                g.setColor(Color.GRAY);
                g.drawLine((int)accelX, graphY - graphH, (int)accelX, graphY);
                g.setColor(Color.WHITE);
                g.drawString(pair.key() +".", (int)accelX - 3, graphY + 15);
                lastGear = pair.key();
            }

            float lastAccelX = accelX;
            float lastAccelY = accelY;
            accelX += xOff;
            accelY = graphY - pair.value() * yOff;
            g.setColor(GraphUI.MAIN_COLOR);
            g.drawLine((int) accelX, (int) accelY, (int) lastAccelX, (int) lastAccelY);

            if (mouseX > lastAccelX && mouseX <= accelX) {
                accelAt = pair.value();
                speedAt = i;
                detailX = (int)accelX;
                detailY = (int)accelY;
            }
        }
        if (accelAt != -1) {
            g.setColor(Color.WHITE);
            g.drawOval(detailX - 2,detailY - 2, 5, 5);

            detailX = Math.min(detailX, 790);
            detailY = Math.min(detailY, 519);
            g.setColor(GraphUI.MAIN_COLOR);
            g.drawRect(detailX, detailY + 20, 130, 20);
            g.setColor(GraphUI.BUTTON_COLOR);
            g.fillRect(detailX+1, detailY + 21, 129, 19);

            g.setColor(Color.WHITE);
            g.drawString(MathUtil.roundUp(accelAt, 3)+" m/s² @ "+speedAt+" Km/h", detailX + 2, detailY + 35);
        }
    }

    private void setupStaticValues() {
        Result result = resultLabel.result;
        Car car = resultLabel.car;
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
        RPM = car.RPM.calculate(h, a, t, divisor);
        float TORQUE_SPLIT = car.TORQUE_SPLIT.calculate(h, a, t, divisor);
        float WHEEL_RADIUS_F = car.RIM_SIZE_FRONT.calculate(h, a, t, divisor) + car.SECTION_WIDTH_FRONT.calculate(h, a, t, divisor) * car.ASPECT_RATIO_FRONT.calculate(h, a, t, divisor) / 50;
        float WHEEL_RADIUS_R = car.RIM_SIZE_REAR.calculate(h, a, t, divisor) + car.SECTION_WIDTH_REAR.calculate(h, a, t, divisor) * car.ASPECT_RATIO_REAR.calculate(h, a, t, divisor) / 50;
        WHEEL_RADIUS = TORQUE_SPLIT <= 0 ? WHEEL_RADIUS_R : TORQUE_SPLIT >= 1 ? WHEEL_RADIUS_F : Math.min(WHEEL_RADIUS_F, WHEEL_RADIUS_R);
        MASS = car.MASS.calculate(h, a, t, divisor);
        COEFFICIENT = car.COEFFICIENT.calculate(h, a, t, divisor);
        TyreCircumference = (float) (Math.PI * WHEEL_RADIUS);
        float MAX_GEAR_RATIO = car.GEAR_RATIO[car.MAX_GEAR_INDEX].calculate(h, a, t, divisor);
        MAX_SPEED = (RPM / MAX_GEAR_RATIO / FINAL_DRIVE) * TyreCircumference * 0.00006f;
        this.finalNodes = new FinalNode[7];
        for (int i = 2; i < 9; i++) {
            FinalNode finalNode = new FinalNode();
            finalNode.GEAR_RATIO = car.GEAR_RATIO[i].calculate(h, a, t, divisor);
            if (finalNode.GEAR_RATIO <= 0) {
                continue;
            }
            finalNode.ENGINE_TORQUE = car.TORQUE[i].calculate(h, a, t, divisor);
            finalNode.GEAR_EFFICIENCY = car.GEAR_EFFICIENCY[i].calculate(h, a, t, divisor);
            finalNode.WHEEL_TORQUE = finalNode.ENGINE_TORQUE * finalNode.GEAR_RATIO * FINAL_DRIVE * finalNode.GEAR_EFFICIENCY;
            finalNode.speedKMH = (RPM / finalNode.GEAR_RATIO / FINAL_DRIVE) * TyreCircumference * 0.00006f;
            finalNodes[i-2] = finalNode;
        }
    }

    private Pair<Integer, Float> Y(float speed) {
        float lastSpeed = 0;
        for (int i = 0; i < finalNodes.length; i++) {
            FinalNode finalNode = finalNodes[i];
            if (finalNode == null) {
                break;
            }
            if (speed >= lastSpeed && speed <= finalNode.speedKMH) {
                float AIR_DENSITY = 1f;
                float FRONTAL_AREA = 1f;
                float VELOCITY = speed * speed / 12.96f;
                float AIR_RESISTANCE = AIR_DENSITY * FRONTAL_AREA * COEFFICIENT * VELOCITY * 0.5f;
                float FORCE = finalNode.WHEEL_TORQUE / (WHEEL_RADIUS / 1000) - AIR_RESISTANCE;
                float ACCELERATION = FORCE / MASS;
                return new Pair<>(i + 1, ACCELERATION);
            }
            //setSpeed(jLabels[i - 2], MathUtil.roundUp(speedKMH, 3), MathUtil.roundUp(ACCELERATION, 3));
            lastSpeed = finalNode.speedKMH;
        }
        return new Pair<>(0, 0f);
    }

    public static class FinalNode {
        public float GEAR_RATIO;
        public float GEAR_EFFICIENCY;
        public float ENGINE_TORQUE;
        public float WHEEL_TORQUE;
        public float speedKMH;
    }
}
