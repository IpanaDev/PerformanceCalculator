package ui.menu.impl;

import calculators.PerfCalculator;
import calculators.Result;
import calculators.priority.Priority;
import cars.Car;
import cars.Cars;
import cars.Overall;
import performance.PerfPart;
import performance.ValueFilter;
import ui.UI;
import ui.elements.label.PriorityLabel;
import ui.elements.label.ResultLabel;
import ui.elements.label.TopSpeedLabel;
import ui.menu.Menu;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.Locale;

public class PerformanceMenu extends Menu {
    public JComboBox<Car> selectedCar;
    public JComboBox<Overall> selectedClass;
    public JComboBox<ValueFilter> selectedParts;
    public PriorityLabel selectedPriorityLabel;
    public Priority selectedPriority = Priority.STAT_T_A_H;
    private ResultLabel resultLabel;
    private TopSpeedLabel topSpeedLabel;

    public PerformanceMenu(UI ui) {
        super(ui);
    }

    @Override
    public void init() throws IllegalAccessException {
        gui().getContentPane().removeAll();
        setupButtons();
        gui().getContentPane().repaint();
    }

    private void setupButtons() {
        //Test
        JButton find = gui().createButton("Find",20,520, 80, 25, null);
        find.setToolTipText("Find the best combination.");
        find.addActionListener(action -> {
            try {
                find.setText("Searching");
                gui().getContentPane().update(gui().getContentPane().getGraphics());
                boolean customParts = selectedParts.getModel().getSelectedItem() == ValueFilter.VALUES;
                Result result;
                if (!customParts) {
                    result = PerfCalculator.findFast(
                            (Car) selectedCar.getModel().getSelectedItem(),
                            (Overall) selectedClass.getModel().getSelectedItem(),
                            (ValueFilter) selectedParts.getModel().getSelectedItem(),
                            selectedPriority);
                } else {
                    result = PerfCalculator.find(
                            (Car) selectedCar.getModel().getSelectedItem(),
                            new PerfPart[]{
                                    (PerfPart) resultLabel.engineParts.getModel().getSelectedItem(),
                                    (PerfPart) resultLabel.turboParts.getModel().getSelectedItem(),
                                    (PerfPart) resultLabel.transmissionParts.getModel().getSelectedItem(),
                                    (PerfPart) resultLabel.suspensionParts.getModel().getSelectedItem(),
                                    (PerfPart) resultLabel.brakeParts.getModel().getSelectedItem(),
                                    (PerfPart) resultLabel.tireParts.getModel().getSelectedItem()});
                }
                resultLabel.setLabelText(0, result.engine().name());
                resultLabel.setLabelText(1, result.turbo().name());
                resultLabel.setLabelText(2, result.trans().name());
                resultLabel.setLabelText(3, result.suspension().name());
                resultLabel.setLabelText(4, result.brakes().name());
                resultLabel.setLabelText(5, result.tires().name());
                resultLabel.setLabelText(6, String.valueOf(result.rating()));
                resultLabel.setLabelText(7, "T: "+(int)result.topSpeed()+", A: "+(int)result.acceleration()+", H: "+(int)result.handling());
                resultLabel.setLabelText(8, "T: "+result.tGain()+", A: "+result.aGain()+", H: "+result.hGain());
                resultLabel.setLabelText(9, result.costString());
                resultLabel.setLabelText(10, "Took "+result.time() +"ms");
                topSpeedLabel.calcAndSet(result, (Car) selectedCar.getModel().getSelectedItem());
                find.setText("Find");
                gui().getContentPane().repaint();
            } catch (IllegalAccessException | NoSuchFieldException | ParseException e) {
                throw new RuntimeException(e);
            }
        });
        JTextField searchCar = gui().createTextField("", 20, 20, 380, 25);
        this.selectedCar = gui().createComboBox(Cars.CARS.toArray(new Car[0]), 600, 20, 300, 25,23);
        this.selectedClass = gui().createComboBox(Overall.values(), 20, 50, 100, 25,23);
        this.selectedParts = gui().createComboBox(ValueFilter.values(), 140, 50, 100, 25,23);
        this.selectedPriorityLabel = new PriorityLabel(this, 280, 50, 80, 15);
        this.resultLabel = new ResultLabel(this, 20, 230, 380, 36);
        this.topSpeedLabel = new TopSpeedLabel(gui(), 400, 230, 380, 36);
        this.selectedParts.addActionListener(l -> {
            this.selectedPriorityLabel.updateTab(this);
            this.resultLabel.handleVisibility(this);
        });
        searchCar.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                gui().repaint();
                selectedCar.removeAllItems();
                String finalText = searchCar.getText();
                if (e.getKeyChar() != 8 && e.getKeyChar() != 127) {
                    finalText += e.getKeyChar();
                }
                if (finalText.isEmpty()) {
                    for (Car car : Cars.CARS) {
                        selectedCar.addItem(car);
                    }
                    return;
                }
                finalText = finalText.toLowerCase(Locale.ENGLISH);
                for (Car car : Cars.CARS) {
                    if (car.fullName().toLowerCase(Locale.ENGLISH).contains(finalText)) {
                        selectedCar.addItem(car);
                    }
                }
            }
            @Override public void keyPressed(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        });
        gui().createLabel("You should delete cars.txt whenever an update released", 605, 530, 300, 20);
    }
}
