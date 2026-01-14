package me.ipana.ui.menu.impl;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class NumberDocument extends PlainDocument {
    public NumberDocument() {
    }

    public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
        if (this.isNumeric(var2)) {
            super.insertString(var1, var2, var3);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private boolean isNumeric(String var1) {
        try {
            Long.valueOf(var1);
            return true;
        } catch (NumberFormatException var3) {
            return false;
        }
    }
}