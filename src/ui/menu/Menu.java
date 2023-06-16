package ui.menu;

import ui.UI;

public abstract class Menu {
    private UI ui;

    public Menu(UI ui) {
        this.ui = ui;
    }

    public abstract void init() throws IllegalAccessException;

    public UI gui() {
        return ui;
    }
}
