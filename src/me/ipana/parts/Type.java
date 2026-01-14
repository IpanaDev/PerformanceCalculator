package me.ipana.parts;

import java.util.Locale;

public enum Type {
    ENGINE, FORCED_INDUCTION, TRANSMISSION, SUSPENSION, BRAKES, TIRES;

    private String fileName;
    public static final Type[] VALUES = values();

    Type() {
        this.fileName = this.name().toLowerCase(Locale.ENGLISH).replace("_","");
    }

    public String fileName() {
        return fileName;
    }
}
