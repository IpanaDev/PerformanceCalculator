package me.ipana.ui.menu.impl;

import me.ipana.config.ConfigFile;
import me.ipana.ui.UI;
import me.ipana.ui.menu.Menu;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.io.IOException;

import static me.ipana.config.ConfigFile.GAME_LOCATION;
import static me.ipana.ui.UI.*;

public class ConfigMenu extends Menu {
    public ConfigMenu(UI ui) {
        super(ui);
    }

    @Override
    public void init() throws IllegalAccessException {
        gui().getContentPane().removeAll();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setBackground(BUTTON_COLOR);
        jFileChooser.setForeground(MAIN_COLOR.brighter());

        JButton button = gui().createButton("Select", 340,200,250, 55, null);
        button.setBorder(BorderFactory.createTitledBorder(UI.BUTTON_BORDER, "Please select your nfsw.exe"));
        ((TitledBorder)button.getBorder()).setTitlePosition(TitledBorder.ABOVE_TOP);
        button.addActionListener(l -> {
            int response = jFileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                try {
                    ConfigFile.save(GAME_LOCATION, jFileChooser.getSelectedFile().getParentFile().getAbsolutePath());
                    if (ConfigFile.invalidGameFolder()) {
                        ((TitledBorder)button.getBorder()).setTitle("Selected folder doesn't have WUGG");
                        gui().getContentPane().repaint();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                ((TitledBorder)button.getBorder()).setTitle("Please select your nfsw.exe");
                gui().getContentPane().repaint();
            }
        });
        gui().getContentPane().repaint();
    }
}
