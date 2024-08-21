package ui.elements.label;

import calculators.result.Result;
import cars.Car;
import performance.*;
import ui.SearchOptions;
import ui.UI;
import ui.menu.impl.PerformanceMenu;

import javax.swing.*;
import java.util.ArrayList;

public class ResultLabel {
    public Car car;
    public Result result;
    public SearchOptions searchOptions;
    public ArrayList<JLabel> labels;
    public JComboBox<PerfPart> engineParts;
    public JComboBox<PerfPart> turboParts;
    public JComboBox<PerfPart> transmissionParts;
    public JComboBox<PerfPart> suspensionParts;
    public JComboBox<PerfPart> brakeParts;
    public JComboBox<PerfPart> tireParts;

    public ResultLabel(PerformanceMenu menu, int x, int y, int width, int height) {
        this.labels = new ArrayList<>();
        String[] labelNames = {"Engine","Turbo","Transmission","Suspension","Brakes","Tires","Rating","Stats","Node Gains","Cost","Nos Level","Time"};
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
        engineParts = menu.gui().createComboBox(PartsLoader.fromType(Type.ENGINE).VALUES, x, posY, cWidth, cHeight,10);
        turboParts = menu.gui().createComboBox(PartsLoader.fromType(Type.FORCED_INDUCTION).VALUES, x, posY+height, cWidth, cHeight,10);
        transmissionParts = menu.gui().createComboBox(PartsLoader.fromType(Type.TRANSMISSION).VALUES, x, posY+height*2, cWidth, cHeight,10);
        suspensionParts = menu.gui().createComboBox(PartsLoader.fromType(Type.SUSPENSION).VALUES, x, posY+height*3, cWidth, cHeight,10);
        brakeParts = menu.gui().createComboBox(PartsLoader.fromType(Type.BRAKES).VALUES, x, posY+height*4, cWidth, cHeight,10);
        tireParts = menu.gui().createComboBox(PartsLoader.fromType(Type.TIRES).VALUES, x, posY+height*5, cWidth, cHeight,10);
        handleVisibility(menu);
    }

    public void setLabelText(int index, String text) {
        this.labels.get(index).setText(text);
    }

    public void handleVisibility(PerformanceMenu menu) {
        boolean customParts = menu.selectedParts.getModel().getSelectedItem() == ValueFilter.CUSTOM;
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

    public void setCar(Car car) {
        this.car = car;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public void setSearchOptions(SearchOptions searchOptions) {
        this.searchOptions = searchOptions;
    }
}
