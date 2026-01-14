package me.ipana.ui;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import me.ipana.config.ConfigFile;
import me.ipana.main.Main;
import me.ipana.ui.elements.JGraphMenu;
import me.ipana.ui.elements.label.ResultLabel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

public class GraphUI extends JFrame {
    public static Color MAIN_COLOR;
    public static Color BUTTON_COLOR;
    public static Border BUTTON_BORDER;
    public static GraphUI INSTANCE;

    static {
        String[] main = String.valueOf(ConfigFile.COLOR.value()).split(",");
        String[] button = String.valueOf(ConfigFile.BUTTON_COLOR.value()).split(",");
        MAIN_COLOR = new Color(Integer.parseInt(main[0]), Integer.parseInt(main[1]), Integer.parseInt(main[2]));
        BUTTON_COLOR = new Color(Integer.parseInt(button[0]), Integer.parseInt(button[1]), Integer.parseInt(button[2]));
        BUTTON_BORDER = BorderFactory.createLineBorder(MAIN_COLOR, 1, false);
    }

    public GraphUI() {
        System.setProperty("sun.java2d.d3d", "false");
        System.setProperty("sun.java2d.ddoffscreen", "false");
        System.setProperty("sun.java2d.noddraw", "true");
        ImageIcon imageIcon = new ImageIcon(Main.class.getResource("logo.png"));
        this.setIconImage(imageIcon.getImage());
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(930, 600);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);// this also destroys current program
    }

    public void setupButtons(ResultLabel resultLabel) {
        int y = 2;
        int yOff = 20;
        createLabel("Search Options:", 2, y+=yOff, 100, 20);
        createLabel(resultLabel.searchOptions.overall.fullName(), 2, y+=yOff, 150, 20);
        createLabel(resultLabel.searchOptions.valueFilter.fullName(), 2, y+=yOff, 150, 20);
        createLabel(resultLabel.searchOptions.priority.fullName(), 2, y+=yOff, 150, 20); y+=yOff;

        createLabel("Tune:", 2, y+=yOff, 100, 20);
        createLabel(resultLabel.result.engine().name(), 2, y+=yOff, 150, 20);
        createLabel(resultLabel.result.turbo().name(), 2, y+=yOff, 150, 20);
        createLabel(resultLabel.result.trans().name(), 2, y+=yOff, 150, 20);
        createLabel(resultLabel.result.suspension().name(), 2, y+=yOff, 150, 20);
        createLabel(resultLabel.result.brakes().name(), 2, y+=yOff, 150, 20);
        createLabel(resultLabel.result.tires().name(), 2, y+=yOff, 150, 20);

        this.createGraphMenu(resultLabel);
        this.setTitle("Detailed Graph: "+resultLabel.car.fullName());
        this.setVisible(true);
    }



    public static void init(ResultLabel resultLabel) throws IllegalAccessException {
        FlatMacDarkLaf.setup();
        INSTANCE = new GraphUI();
        INSTANCE.setupButtons(resultLabel);
    }

    public JPanel createGraphMenu(ResultLabel resultLabel) {
        JGraphMenu jGraphMenu = new JGraphMenu(resultLabel);
        jGraphMenu.setBounds(0,0,this.getWidth(),this.getHeight());
        this.add(jGraphMenu);
        return jGraphMenu;
    }

    public JTextField createTextField(String text, int x, int y, int width, int height) {
        JTextField jTextField = new JTextField(text);
        jTextField.setBorder(BUTTON_BORDER);
        jTextField.setBackground(BUTTON_COLOR);
        jTextField.setForeground(MAIN_COLOR.brighter());
        jTextField.setBounds(x, y, width, height);
        jTextField.setCaretColor(MAIN_COLOR);
        this.add(jTextField);
        return jTextField;
    }
    public JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel jLabel = new JLabel(text);
        jLabel.setBounds(x,y,width,height);
        jLabel.setForeground(MAIN_COLOR.brighter());
        this.add(jLabel);
        return jLabel;
    }
    public <T> JComboBox<T> createComboBox(T[] values, int x, int y, int width, int height, int rowCount) {
        JComboBox<T> jComboBox = new JComboBox<>(values);
        jComboBox.setBounds(x,y,width,height);
        jComboBox.setBorder(BUTTON_BORDER);
        jComboBox.setBackground(BUTTON_COLOR);
        jComboBox.setForeground(MAIN_COLOR.brighter());
        jComboBox.putClientProperty("JComboBox.isPopDown", true);
        jComboBox.setMaximumRowCount(rowCount);
        this.add(jComboBox);
        return jComboBox;
    }

    public JCheckBox createCheckBox(String text, int x, int y, int width, int height) {
        JCheckBox jCheckBox = new JCheckBox(text);
        jCheckBox.setBorder(BUTTON_BORDER);
        jCheckBox.setBackground(BUTTON_COLOR);
        jCheckBox.setForeground(MAIN_COLOR.brighter());
        jCheckBox.setBounds(x, y, width, height);
        jCheckBox.setFocusPainted(false);
        this.add(jCheckBox);
        return jCheckBox;
    }

    public JButton createButton(String text, int x, int y, int width, int height, ActionListener l) {
        JButton jButton = new JButton(text);
        jButton.addActionListener(l);
        jButton.setBorder(BUTTON_BORDER);
        jButton.setBackground(BUTTON_COLOR);
        jButton.setForeground(MAIN_COLOR.brighter());
        jButton.setBounds(x, y, width, height);
        jButton.setFocusPainted(false);
        this.add(jButton);
        return jButton;
    }
}
