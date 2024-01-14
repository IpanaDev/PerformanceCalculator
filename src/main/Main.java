package main;

import config.ConfigFile;
import decompiler.AttributeDecompiler;
import cars.Cars;
import decompiler.LangDecompiler;
import decompiler.PartDecompiler;
import performance.Parts;
import ui.UI;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Main {
    
    public static void main(String[] args) throws IllegalAccessException, IOException, InterruptedException, NoSuchFieldException {
        File cars = new File("cars.txt");
        File parts = new File("parts");
        UI.init();
        ConfigFile.init();
        UI.INSTANCE.decompilerMenu().init();
        if (!cars.exists()) {
            LangDecompiler.start();
            AttributeDecompiler.start(parts, cars);
        }
        if (!parts.exists()) {
            PartDecompiler.start(parts);
        }
        UI.INSTANCE.decompilerMenu().setStatus("Loading...");
        Parts.init();
        Cars.init();
        System.gc();
        UI.INSTANCE.performanceMenu().init();

    }
}
