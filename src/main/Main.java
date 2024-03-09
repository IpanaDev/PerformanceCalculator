package main;

import config.ConfigFile;
import cars.CarLoader;
import init.VaultInit;
import performance.PartsLoader;
import ui.UI;
import utils.Benchmark;
import vaultlib.GameIdHelper;
import vaultlib.core.db.Database;
import vaultlib.core.db.DatabaseOptions;
import vaultlib.core.db.DatabaseType;
import vaultlib.support.world.WorldProfile;

import java.io.*;

public class Main {
    public static Database DB;
    
    public static void main(String[] args) throws Exception {
        UI.init();
        UI.INSTANCE.statusMenu().init();
        UI.INSTANCE.statusMenu().setStatus("Loading...");
        Benchmark.create("Initialize Config", ConfigFile::init);
        Benchmark.create("Initialize VLT", () -> {
            VaultInit.init();
            File gameDir = new File(String.valueOf(ConfigFile.GAME_LOCATION.value()));
            File data = new File(gameDir, ".data\\b2d5f170c62d6e37ac67c04be2235249");
            String gameId = GameIdHelper.WORLD.name();
            DB = new Database(new DatabaseOptions(gameId, DatabaseType.X86Database));
            String directory = data.getAbsolutePath() + "\\GLOBAL";
            WorldProfile profile = new WorldProfile();
            profile.LoadFiles(DB, directory, "attributes.bin", "commerce.bin");
            DB.CompleteLoad();
        });
        Benchmark.create("Initialize Parts", PartsLoader::init);
        Benchmark.create("Initialize Cars", CarLoader::init);
        System.gc();
        UI.INSTANCE.performanceMenu().init();
    }
}
