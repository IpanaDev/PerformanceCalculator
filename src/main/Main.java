package main;

import config.ConfigFile;
import decompiler.AttributeDecompiler;
import cars.Cars;
import decompiler.LangDecompiler;
import ui.UI;

import java.io.*;
public class Main {

    public static void main(String[] args) throws IllegalAccessException, IOException, InterruptedException {
        File file = new File("cars.txt");
        UI.init();
        ConfigFile.init();
        if (!file.exists()) {
            UI.INSTANCE.decompilerMenu().init();
            LangDecompiler.start();
            AttributeDecompiler.start(file);
        }
        Cars.init();
        UI.INSTANCE.performanceMenu().init();
    }
}
