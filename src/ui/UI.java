package ui;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import main.Main;
import ui.menu.impl.ConfigMenu;
import ui.menu.impl.DecompilerMenu;
import ui.menu.impl.PerformanceMenu;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

public class UI extends JFrame {
    public static final Color MAIN_COLOR = new Color(125, 98, 255);
    public static final Color BUTTON_COLOR = new Color(50, 50, 50);
    public static final Border BUTTON_BORDER = BorderFactory.createLineBorder(MAIN_COLOR, 1, false);
    private PerformanceMenu performanceMenu = new PerformanceMenu(this);
    private ConfigMenu configMenu = new ConfigMenu(this);
    private DecompilerMenu decompilerMenu = new DecompilerMenu(this);
    public static UI INSTANCE;

    public UI() throws IllegalAccessException {
        System.setProperty("sun.java2d.d3d", "false");
        System.setProperty("sun.java2d.ddoffscreen", "false");
        System.setProperty("sun.java2d.noddraw", "true");
        this.setTitle("WUGG Performance Calculator");
        ImageIcon imageIcon = new ImageIcon(Main.class.getResource("logo.png"));
        this.setIconImage(imageIcon.getImage());
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(930, 600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// this also destroys current program
        this.setVisible(true);

    }

    public static void init() throws IllegalAccessException {
        FlatMacDarkLaf.setup();
        INSTANCE = new UI();
    }

    public PerformanceMenu performanceMenu() {
        return performanceMenu;
    }

    public ConfigMenu configMenu() {
        return configMenu;
    }

    public DecompilerMenu decompilerMenu() {
        return decompilerMenu;
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
