package ui.elements.label;

import calculators.priority.Priority;
import ui.UI;
import ui.menu.Menu;
import ui.menu.impl.PerformanceMenu;

import javax.swing.*;

public class PriorityLabel {
    private int index;
    private JLabel[] childLabels;

    public PriorityLabel(PerformanceMenu menu, int x2, int y2, int width2, int height2) {
        this.createChildLabels(menu.gui(), x2,y2, width2, height2);
        this.updateChildLabels(menu);
        this.index = menu.selectedPriority.ordinal();
        int offset = 20;
        menu.gui().createButton("<", x2 - offset, y2, offset, offset, l -> {
            index--;
            if (index < 0) {
                index = Priority.values().length - 1;
            }
            menu.selectedPriority = Priority.values()[index];
            this.updateChildLabels(menu);
        });
        menu.gui().createButton(">", x2 + width2 + offset, y2, offset, offset, l -> {
            index++;
            if (index >= Priority.values().length) {
                index = 0;
            }
            menu.selectedPriority = Priority.values()[index];
            this.updateChildLabels(menu);
        });
    }

    private void createChildLabels(UI ui, int x, int y, int width, int height) {
        this.childLabels = new JLabel[4];
        JLabel l = ui.createLabel("Priority", x+9,y+4, width, height);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        int offset = y+25;
        for (int i = 0; i < this.childLabels.length; i++) {
            this.childLabels[i] = ui.createLabel("", x+9,offset, width, height);
            this.childLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            offset+=15;
        }
    }

    public void updateChildLabels(PerformanceMenu menu) {
        for (JLabel label : this.childLabels) {
            label.setText("");
        }
        String[] split = menu.selectedPriority.fullName().split("-");
        for (int i = 0; i < split.length; i++) {
            this.childLabels[i].setText(split[i]);
        }
    }
}