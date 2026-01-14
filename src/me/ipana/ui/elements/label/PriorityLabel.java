package me.ipana.ui.elements.label;

import me.ipana.calculator.priority.Priority;
import me.ipana.ui.menu.impl.PerformanceMenu;

import javax.swing.*;

public class PriorityLabel {
    private int tabIndex;
    private JLabel tabName;
    private Tab[] tabs;

    public PriorityLabel(PerformanceMenu menu, int x2, int y2, int width2, int height2) {
        this.createChildLabels(menu, x2,y2, width2, height2);
        this.updateTab(menu);
        this.tabIndex = menu.selectedPriority.ordinal();
        int offset = 20;
        menu.gui().createButton("<", x2 - offset, y2, offset, offset, l -> {
            tabIndex--;
            if (tabIndex < 0) {
                tabIndex = Tabs.VALUES.length - 1;
            }
            this.updateTab(menu);
        });
        menu.gui().createButton(">", x2 + width2 + offset, y2, offset, offset, l -> {
            tabIndex++;
            if (tabIndex >= Tabs.VALUES.length) {
                tabIndex = 0;
            }
            this.updateTab(menu);
        });
    }

    private void createChildLabels(PerformanceMenu menu, int x, int y, int width, int height) {
        JLabel l = menu.gui().createLabel("Priority", x+9,y+4, width, height);
        l.setHorizontalAlignment(SwingConstants.CENTER);

        tabName = menu.gui().createLabel("", x+9,y+25, width, height);
        tabName.setHorizontalAlignment(SwingConstants.CENTER);

        Tabs[] TABS = Tabs.VALUES;
        this.tabs = new Tab[TABS.length];
        for (int i = 0; i < tabs.length; i++) {
            Tab tab = tabs[i] = new Tab();
            tab.pLabels = new PLabel[TABS[i].fieldNames().length];
            int offset = y+42;
            for (int j = 0; j < tab.pLabels.length; j++) {
                tab.pLabels[j] = new PLabel(tab, menu, j, TABS[i].fieldNames()[j], x+9, offset, width, height);
                offset += 17;
            }
        }
    }

    public void setPos(PLabel pLabel, int off) {
        pLabel.label.setLocation(pLabel.label.getX(), pLabel.label.getY()+off);
        pLabel.up.setLocation(pLabel.up.getX(), pLabel.up.getY()+off);
        pLabel.down.setLocation(pLabel.down.getX(), pLabel.down.getY()+off);
    }

    public void updateTab(PerformanceMenu menu) {
        this.tabIndex = Tabs.VALUES[tabIndex].ordinal();
        this.tabName.setText(Tabs.VALUES[tabIndex].fullName());
        this.setVisibility();
        this.updatePriority(menu);
    }

    public void updatePriority(PerformanceMenu menu) {
        StringBuilder builder = new StringBuilder();
        Tabs TAB_TYPE = Tabs.VALUES[tabIndex];
        builder.append(TAB_TYPE.name()).append("_");
        for (int i = 0; i < tabs[tabIndex].pLabels.length; i++) {
            PLabel pLabel = fromIndex(tabs[tabIndex], i);
            if (pLabel == null) {
                continue;
            }
            builder.append(pLabel.labelName).append("_");
        }
        builder.deleteCharAt(builder.length()-1);
        menu.selectedPriority = Priority.valueOf(builder.toString());
        menu.gui().getContentPane().repaint();
    }

    public void setVisibility() {
        for (int i = 0; i < tabs.length; i++) {
            Tab tab = tabs[i];
            for (PLabel pLabel : tab.pLabels) {
                boolean state = i == tabIndex;
                pLabel.label.setEnabled(state);
                pLabel.label.setVisible(state);
                pLabel.up.setEnabled(state);
                pLabel.up.setVisible(state);
                pLabel.down.setEnabled(state);
                pLabel.down.setVisible(state);
            }
        }
    }

    class Tab {
        PLabel[] pLabels;
    }

    private PLabel fromIndex(Tab tab, int index) {
        for (PLabel pLabel : tab.pLabels) {
            if (index == pLabel.index) {
                return pLabel;
            }
        }
        return null;
    }

    class PLabel {
        String labelName;
        JLabel label;
        JButton up, down;
        int index;

        public PLabel(Tab parentTab, PerformanceMenu menu, int indexInList, String labelName, int x, int y, int width, int height) {
            this.index = indexInList;
            this.labelName = labelName;
            label = menu.gui().createLabel(labelName, x, y, width, height);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            int yOff = 17;
            up = menu.gui().createButton("↑", x+width-1, y, 15, 15, l -> {
                if (index > 0) {
                    PLabel above = fromIndex(parentTab,index-1);
                    if (above == null) {
                        return;
                    }
                    setPos(above, yOff);
                    setPos(this, -yOff);
                    above.index++;
                    index--;
                    updatePriority(menu);
                }
            });
            down = menu.gui().createButton("↓", x+width+16, y, 15, 15, l -> {
                if (index < parentTab.pLabels.length-1) {
                    PLabel below = fromIndex(parentTab,index+1);
                    if (below == null) {
                        return;
                    }
                    setPos(below, -yOff);
                    setPos(this, yOff);
                    below.index--;
                    index++;
                    updatePriority(menu);
                }
            });
        }
    }
}