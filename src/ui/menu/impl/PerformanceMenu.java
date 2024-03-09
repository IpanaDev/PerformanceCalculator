package ui.menu.impl;

import calculators.PerfCalculator;
import calculators.priority.Priority;
import calculators.result.Result;
import cars.Car;
import cars.CarLoader;
import cars.Overall;
import performance.PerfPart;
import performance.ValueFilter;
import ui.UI;
import ui.elements.label.PriorityLabel;
import ui.elements.label.ResultLabel;
import ui.elements.label.DetailedLabel;
import ui.menu.Menu;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.Locale;

public class PerformanceMenu extends Menu {
    public JComboBox<Car> selectedCar;
    public JComboBox<Overall> selectedClass;
    public JComboBox<ValueFilter> selectedParts;
    public PriorityLabel selectedPriorityLabel;
    public Priority selectedPriority = Priority.NODE_T_A_H;
    private ResultLabel resultLabel;
    private DetailedLabel detailedLabel;


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
            if (find.getText().equals("Find")) {
                find.setText("Searching");
                new Thread(() -> {
                    try {
                        boolean customParts = selectedParts.getModel().getSelectedItem() == ValueFilter.CUSTOM;
                        Result result;
                        Car car = (Car) selectedCar.getModel().getSelectedItem();
                        if (!customParts) {
                            result = new PerfCalculator().findMultiThread(
                                    car,
                                    (Overall) selectedClass.getModel().getSelectedItem(),
                                    (ValueFilter) selectedParts.getModel().getSelectedItem(),
                                    selectedPriority);
                        } else {
                            result = new PerfCalculator().find(
                                    car,
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
                        resultLabel.setLabelText(7, "T: " + (int) result.tStat() + ", A: " + (int) result.aStat() + ", H: " + (int) result.hStat());
                        resultLabel.setLabelText(8, "T: " + result.tGain() + ", A: " + result.aGain() + ", H: " + result.hGain());
                        resultLabel.setLabelText(9, result.costString());
                        resultLabel.setLabelText(10, car.nosLevel());
                        resultLabel.setLabelText(11, "Took " + result.time() + "ms");
                        detailedLabel.calcAndSet(result, (Car) selectedCar.getModel().getSelectedItem());
                        find.setText("Find");
                    } catch (IllegalAccessException | NoSuchFieldException | ParseException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        });
        JTextField searchCar = gui().createTextField("", 20, 20, 380, 25);
        JTextField customOA = gui().createTextField("", 212, 472, 167, 25);
        customOA.setDocument(new NumberDocument());
        customOA.setHorizontalAlignment(SwingConstants.CENTER);
        customOA.setVisible(false);
        this.selectedCar = gui().createComboBox(CarLoader.CARS.toArray(new Car[0]), 600, 20, 300, 25,23);
        this.selectedClass = gui().createComboBox(Overall.values(), 20, 50, 100, 25,23);
        this.selectedClass.addActionListener(l -> {
            customOA.setVisible(selectedClass.getSelectedItem() == Overall.CUSTOM);
        });
        this.selectedParts = gui().createComboBox(ValueFilter.values(), 140, 50, 100, 25,23);
        this.selectedPriorityLabel = new PriorityLabel(this, 280, 50, 80, 15);
        this.resultLabel = new ResultLabel(this, 20, 250, 380, 36);
        this.detailedLabel = new DetailedLabel(gui(), 400, 250, 380, 36);
        this.selectedParts.addActionListener(l -> {
            this.selectedPriorityLabel.updateTab(this);
            this.resultLabel.handleVisibility(this);
        });
        customOA.addPropertyChangeListener(l -> {
            try {
                Overall.CUSTOM.max(Integer.parseInt(customOA.getText()));
            } catch (NumberFormatException e) {}
        });
        customOA.addActionListener(l -> {
            try {
                Overall.CUSTOM.max(Integer.parseInt(customOA.getText()));
            } catch (NumberFormatException e) {}
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
                    for (Car car : CarLoader.CARS) {
                        selectedCar.addItem(car);
                    }
                    return;
                }
                finalText = finalText.toLowerCase(Locale.ENGLISH);
                for (Car car : CarLoader.CARS) {
                    if (car.fullName().toLowerCase(Locale.ENGLISH).contains(finalText)) {
                        selectedCar.addItem(car);
                    }
                }
            }
            @Override public void keyPressed(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        });
    }
}
