package decompiler;

import config.ConfigFile;
import ui.UI;

import java.io.*;
public class LangDecompiler {

    public static void start() throws IOException, InterruptedException {
        UI.INSTANCE.decompilerMenu().setStatus("Unpacking LANG files...");
        File languagesFile = new File("languages");
        if (!languagesFile.exists()) {
            languagesFile.mkdir();
        }
        String gamePath = String.valueOf(ConfigFile.valueFromName("Game Location"));
        String s = String.valueOf('"');
        String jsonToolPath = s+"decompiler tools\\lang\\jsontool.exe"+s;
        String in = s + gamePath+"\\.data\\b2d5f170c62d6e37ac67c04be2235249\\LANGUAGES" + s;
        String out = s+"languages"+s;
        System.currentTimeMillis();
        long ms = System.currentTimeMillis();
        new ProcessBuilder().command(jsonToolPath, "unpack", in, out).start().waitFor();
        System.out.println("Took "+(System.currentTimeMillis()-ms)+"ms to unpack lang files");
    }
}
