package config;

import main.Main;
import ui.UI;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class ConfigFile {
    public static final File CONFIG_FILE = new File("config.txt");
    private static final ArrayList<ConfigField> CONFIG_FIELDS = new ArrayList<>();
    public static final ConfigField GAME_LOCATION = new ConfigField("Game Location","");
    public static final ConfigField DEV_MODE = new ConfigField("Dev Mode",false);
    public static final ConfigField COLOR = new ConfigField("UI Color","125,98,255");
    public static final ConfigField BUTTON_COLOR = new ConfigField("Button Color","50,50,50");

    public static void init() throws IOException, IllegalAccessException, InterruptedException {
        load();
        UI.onConfigLoad();
        if (!CONFIG_FILE.exists() || invalidGameFolder()) {
            save(GAME_LOCATION, "C:\\");
            UI.INSTANCE.configMenu().init();
        }
        while (invalidGameFolder()) {
            Thread.sleep(1);
        }
    }

    public static boolean invalidGameFolder() {
        String gameFolder = String.valueOf(valueFromName("Game Location"));
        File nfsw = new File(gameFolder, "nfsw.exe");
        File GLOBAL = new File(gameFolder, "GLOBAL");
        File data = new File(gameFolder, Main.SERVER_DATA);
        return CONFIG_FILE.exists() && (!nfsw.exists() || !GLOBAL.exists() || !data.exists());
    }

    public static void load() throws IOException {
        if (CONFIG_FILE.exists()) {
            InputStreamReader fileReader = new InputStreamReader(Files.newInputStream(CONFIG_FILE.toPath()), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(fileReader);

            String line;
            int lineCount = 0;
            boolean needsUpdate = false;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("=");
                ConfigField field = fieldFromName(split[0]);
                if (field != null) {
                    field.value = split[1];
                } else {
                    needsUpdate = true;
                }
                lineCount++;
            }
            if (!needsUpdate && lineCount < CONFIG_FIELDS.size()) {
                needsUpdate = true;
            }
            if (needsUpdate) {
                save();
            }
            reader.close();
            fileReader.close();
        }
    }
    public static void save() throws IOException {
        CONFIG_FILE.delete();
        CONFIG_FILE.createNewFile();
        Writer writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(CONFIG_FILE.toPath()), StandardCharsets.UTF_8));
        for (ConfigField field : CONFIG_FIELDS) {
            writer.write(field.name + "=" + field.value + "\n");
        }
        writer.close();
    }
    public static void save(ConfigField configField, Object value) throws IOException {
        if (configField != null) {
            configField.value = value;
        }
        save();
    }

    public static Object valueFromName(String name) {
        return fieldFromName(name).value;
    }

    public static ConfigField fieldFromName(String name) {
        for (ConfigField field : CONFIG_FIELDS) {
            if (field.name.equals(name)) {
                return field;
            }
        }
        return null;
    }


    public static class ConfigField {
        String name;
        Object value;

        public ConfigField(String name, Object value) {
            this.name = name;
            this.value = value;
            CONFIG_FIELDS.add(this);
        }

        public Object value() {
            return value;
        }
    }
}
