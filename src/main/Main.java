package main;

import config.ConfigFile;
import cars.CarLoader;
import init.VaultInit;
import performance.PartsLoader;
import ui.UI;
import utils.Benchmark;
import vaultlib.GameIdHelper;
import vaultlib.LoaderType;
import vaultlib.core.db.Database;
import vaultlib.core.db.DatabaseOptions;
import vaultlib.core.db.DatabaseType;
import vaultlib.support.world.WorldProfile;

import javax.swing.*;
import java.io.*;

public class Main {
    public static final String BUILD = "1.9";
    public static Database DB;
    
    public static void main(String[] args) {
        try {
            UI.init();
            Benchmark.create("Initialize Config", ConfigFile::init);
            UI.INSTANCE.statusMenu().init();
            UI.INSTANCE.statusMenu().setStatus("Loading...");
            Benchmark.create("Initialize VLT", () -> {
                String gameId = GameIdHelper.WORLD.name();
                DB = new Database(new DatabaseOptions(gameId, DatabaseType.X86Database));
                VaultInit.init(DB.Options.Type);
                File gameDir = new File(String.valueOf(ConfigFile.GAME_LOCATION.value()));
                File data = new File(gameDir, ".data\\b2d5f170c62d6e37ac67c04be2235249");
                String directory = data.getAbsolutePath() + "\\GLOBAL";
                WorldProfile profile = new WorldProfile();
                profile.LoadFiles(DB, directory, LoaderType.MAIN, "attributes.bin", "commerce.bin");
                DB.CompleteLoad();
            });
            Benchmark.create("Initialize Parts", PartsLoader::init);
            Benchmark.create("Initialize Cars", CarLoader::init);
            System.gc();
            UI.INSTANCE.performanceMenu().init();
        } catch (Exception e) {
            e.printStackTrace();
            StringBuilder builder = new StringBuilder();
            builder.append(e).append(System.lineSeparator());
            for (StackTraceElement element : e.getStackTrace()) {
                builder.append(element.toString()).append(System.lineSeparator());
            }
            error(builder.toString(), "Error");
        }
    }
    public static void error(String msg,String title) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }
}
