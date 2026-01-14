package me.ipana.ui.menu.impl;

import me.ipana.ui.UI;
import me.ipana.ui.menu.Menu;

import javax.swing.*;

public class StatusMenu extends Menu {
    private JLabel label;

    public StatusMenu(UI ui) {
        super(ui);
    }

    @Override
    public void init() throws IllegalAccessException {
        gui().getContentPane().removeAll();
        label = gui().createLabel("", 0, 200, 930, 55);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        gui().getContentPane().repaint();
    }

    public void setStatus(String text) {
        this.label.setText(text);
        gui().getContentPane().repaint();
    }
}
