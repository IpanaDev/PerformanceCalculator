package ui.elements.label;

import performance.*;
import ui.UI;
import ui.menu.impl.PerformanceMenu;

import javax.swing.*;
import java.util.ArrayList;

public class ResultLabel {
    public ArrayList<JLabel> labels;
    public JComboBox<PerfPart> engineParts;
    public JComboBox<PerfPart> turboParts;
    public JComboBox<PerfPart> transmissionParts;
    public JComboBox<PerfPart> suspensionParts;
    public JComboBox<PerfPart> brakeParts;
    public JComboBox<PerfPart> tireParts;

    public ResultLabel(PerformanceMenu menu, int x, int y, int width, int height) {
        this.labels = new ArrayList<>();
        String[] labelNames = {"Engine","Turbo","Transmission","Suspension","Brakes","Tires","Rating","Top Speed","Acceleration","Handling","Cost","Time"};
        int posX = x;
        int posY = y;
        for (int i = 0; i < labelNames.length; i++) {
            JLabel label = menu.gui().createLabel("", posX, posY, width / 2 - 20, height);
            label.setBorder(BorderFactory.createTitledBorder(UI.BUTTON_BORDER, labelNames[i]));
            labels.add(label);
            posY+=height;
            if (i == 5) {
                posX = x+width/2;
                posY = y;
            }
        }
        posY = y+5;
        int cWidth = width/2-20;
        int cHeight = height-10;
        engineParts = menu.gui().createComboBox(Parts.fromType(Type.ENGINE).VALUES, x, posY, cWidth, cHeight,5);
        turboParts = menu.gui().createComboBox(Parts.fromType(Type.FORCED_INDUCTION).VALUES, x, posY+height, cWidth, cHeight,5);
        transmissionParts = menu.gui().createComboBox(Parts.fromType(Type.TRANSMISSION).VALUES, x, posY+height*2, cWidth, cHeight,5);
        suspensionParts = menu.gui().createComboBox(Parts.fromType(Type.SUSPENSION).VALUES, x, posY+height*3, cWidth, cHeight,5);
        brakeParts = menu.gui().createComboBox(Parts.fromType(Type.BRAKES).VALUES, x, posY+height*4, cWidth, cHeight,5);
        tireParts = menu.gui().createComboBox(Parts.fromType(Type.TIRES).VALUES, x, posY+height*5, cWidth, cHeight,5);
        handleVisibility(menu);
    }

    public void setLabelText(int index, String text) {
        this.labels.get(index).setText(text);
    }

    public void handleVisibility(PerformanceMenu menu) {
        boolean customParts = menu.selectedParts.getModel().getSelectedItem() == ValueFilter.VALUES;
        for (int i = 0; i < 6; i++) {
            labels.get(i).setVisible(!customParts);
        }
        engineParts.setVisible(customParts);
        turboParts.setVisible(customParts);
        transmissionParts.setVisible(customParts);
        suspensionParts.setVisible(customParts);
        brakeParts.setVisible(customParts);
        tireParts.setVisible(customParts);
    }
}
