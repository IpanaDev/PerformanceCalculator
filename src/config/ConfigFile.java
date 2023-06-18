package config;

import ui.UI;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class ConfigFile {
    public static final File CONFIG_FILE = new File("config.txt");
    public static final ConfigField GAME_LOCATION = new ConfigField("Game Location","");
    public static final ConfigField USE_CACHE = new ConfigField("Use Cache",true);
    private static final ArrayList<ConfigField> CONFIG_FIELDS = new ArrayList<>();

    static {
        CONFIG_FIELDS.add(GAME_LOCATION);
        CONFIG_FIELDS.add(USE_CACHE);
    }

    public static void init() throws IOException, IllegalAccessException, InterruptedException {
        load();
        if (!CONFIG_FILE.exists() || invalidGameFolder()) {
            save("C:\\", true);
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
        File data = new File(gameFolder, ".data\\b2d5f170c62d6e37ac67c04be2235249");
        return CONFIG_FILE.exists() && (!nfsw.exists() || !GLOBAL.exists() || !data.exists());
    }

    public static void load() throws IOException {
        if (CONFIG_FILE.exists()) {
            InputStreamReader fileReader = new InputStreamReader(Files.newInputStream(CONFIG_FILE.toPath()), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("=");
                fieldFromName(split[0]).value = split[1];
            }
            reader.close();
            fileReader.close();
        }
    }

    public static void save(Object... values) throws IOException {
        CONFIG_FILE.createNewFile();
        Writer writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(CONFIG_FILE.toPath()), StandardCharsets.UTF_8));
        for (int i = 0; i < values.length; i++) {
            ConfigField field = CONFIG_FIELDS.get(i);
            field.value = values[i];
            writer.write(field.name+"="+field.value+"\n");
        }
        writer.close();
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


    static class ConfigField {
        String name;
        Object value;

        public ConfigField(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }
}
