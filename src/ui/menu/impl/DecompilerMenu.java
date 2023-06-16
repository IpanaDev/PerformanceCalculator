package ui.menu.impl;

import ui.UI;
import ui.menu.Menu;

import javax.swing.*;

public class DecompilerMenu extends Menu {
    private JLabel label;

    public DecompilerMenu(UI ui) {
        super(ui);
    }

    @Override
    public void init() throws IllegalAccessException {
        gui().getContentPane().removeAll();
        label = gui().createLabel("", 365, 200, 250, 55);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        gui().getContentPane().repaint();
    }

    public void setStatus(String text) {
        this.label.setText(text);
        gui().getContentPane().repaint();
    }
}
