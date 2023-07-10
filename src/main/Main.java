package main;

import config.ConfigFile;
import decompiler.AttributeDecompiler;
import cars.Cars;
import decompiler.LangDecompiler;
import decompiler.PartDecompiler;
import performance.Parts;
import ui.UI;

import java.io.*;
public class Main {

    public static void main(String[] args) throws IllegalAccessException, IOException, InterruptedException {
        File cars = new File("cars.txt");
        File parts = new File("parts");
        UI.init();
        ConfigFile.init();
        if (!cars.exists()) {
            UI.INSTANCE.decompilerMenu().init();
            LangDecompiler.start();
            AttributeDecompiler.start(parts, cars);
        }
        if (!parts.exists()) {
            UI.INSTANCE.decompilerMenu().init();
            PartDecompiler.start(parts);
        }
        Parts.init();
        Cars.init();
        UI.INSTANCE.performanceMenu().init();
    }
}
