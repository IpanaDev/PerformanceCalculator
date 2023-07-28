package decompiler;

import config.ConfigFile;
import ui.UI;
import utils.Hasher;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

public class OldAttributeDecompiler {
    public static final String[] P_NAMES = {"tuner","exotic","muscle","addon_tuner","addon_exotic","addon_muscle"};
    public static final String[] T_NAMES = {"tuner","exotic","muscle"};
    public static final String[] E_NAMES = {"default"};

    public static void start(File partsFile, File parsedFile) throws IOException, InterruptedException {
        File attributeFiles = new File("attributes");
        unpackAttributes(attributeFiles);
        File languagesFile = new File("languages");
        File jsonFile = new File(languagesFile, "English_Global.json");

        InputStreamReader fileReader = new InputStreamReader(Files.newInputStream(jsonFile.toPath()), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        parsedFile.createNewFile();
        Writer writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(parsedFile.toPath()), StandardCharsets.UTF_8));
        long ms = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder((int) Math.pow(2, 16));
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(":");
            if (line.startsWith("  \"GM_CAR_")) {
                String vltName = split[0].substring(10, split[0].length() - 1).toLowerCase(Locale.ENGLISH);
                String fullName = split[1].substring(2, split[1].length() - 2);
                UI.INSTANCE.decompilerMenu().setStatus("Parsing ATTRIBUTE files (cars/"+fullName+")");
                Fields P_FIELD = new Fields(
                        new Field(int.class, FilterType.HYPHEN,"TopSpeed",4,2),
                        new Field(int.class, FilterType.HYPHEN,"Acceleration",4,2),
                        new Field(int.class, FilterType.HYPHEN,"Handling",4,2));
                String[] pValues = findValues("pvehicle", vltName, false, P_FIELD, P_NAMES);
                if (pValues != null) {
                    Fields E_FIELD = new Fields(
                            new Field(int.class, FilterType.COLON, "RED_LINE"));
                    Fields TIRE_FIELD = new Fields(
                            new Field(int.class, FilterType.COLON_ARRAY, "ASPECT_RATIO", 2),
                            new Field(int.class, FilterType.COLON_ARRAY, "SECTION_WIDTH", 2),
                            new Field(int.class, FilterType.COLON_ARRAY, "RIM_SIZE", 2));
                    Fields TRANS_FIELD = new Fields(
                            new Field(double.class, FilterType.HYPHEN, "GEAR_RATIO", 9,2, true),
                            new Field(double.class, FilterType.COLON, "TORQUE_SPLIT"),
                            new Field(double.class, FilterType.COLON, "FINAL_GEAR"));
                    String[] engineValues = findValues("engine", vltName, false, E_FIELD, E_NAMES);
                    String[] tireValues = findValues("tires", vltName, false, TIRE_FIELD, T_NAMES);
                    String[] transValues = findValues("transmission", vltName, false, TRANS_FIELD, E_NAMES);
                    builder.append(vltName).append(",").append(fullName).append(",");
                    for (String values : pValues) {
                        builder.append(values).append(",");
                    }
                    for (String values : engineValues) {
                        builder.append(values).append(",");
                    }
                    Fields SUB_ENGINE_FIELD = new Fields(
                            new Field(int.class, FilterType.COLON, "RED_LINE", 0, 0, true));
                    builder.append(findSubValues(vltName, "engine",SUB_ENGINE_FIELD, E_NAMES));
                    for (String values : tireValues) {
                        builder.append(values).append(",");
                    }
                    Fields SUB_TIRE_FIELD = new Fields(
                            new Field(int.class, FilterType.COLON_ARRAY, "ASPECT_RATIO", 2),
                            new Field(int.class, FilterType.COLON_ARRAY, "SECTION_WIDTH", 2),
                            new Field(int.class, FilterType.COLON_ARRAY, "RIM_SIZE", 2));
                    builder.append(findSubValues(vltName,"tires",SUB_TIRE_FIELD,T_NAMES));
                    for (String values : transValues) {
                        builder.append(values).append(",");
                    }
                    Fields SUB_TRANS_FIELD = new Fields(
                            new Field(double.class, FilterType.COLON, "FINAL_GEAR", 0, 0, true));
                    builder.append(findSubValues(vltName, "transmission", SUB_TRANS_FIELD, E_NAMES));
                    builder.delete(builder.length()-1, builder.length());
                    builder.append("\n");
                    //System.out.println(builder);
                }
            }
        }
        writer.write(builder.toString());
        System.out.println("Took "+(System.currentTimeMillis()-ms)+"ms to parse attributes");
        writer.close();
        reader.close();
        fileReader.close();
        if (partsFile.exists()) {
            deleteFile(attributeFiles);
        }
        deleteFile(languagesFile);
    }

    public static void unpackAttributes(File attributeFiles) throws IOException, InterruptedException {
        if (!attributeFiles.exists()) {
            UI.INSTANCE.decompilerMenu().setStatus("Unpacking ATTRIBUTE files...");
            String gamePath = String.valueOf(ConfigFile.valueFromName("Game Location"));
            File globalFile = new File(gamePath,".data\\b2d5f170c62d6e37ac67c04be2235249\\GLOBAL");
            File gcVaults = new File(gamePath + "\\GLOBAL\\gc.vaults");
            File newGcVaults = new File(globalFile, "gc.vaults");
            if (newGcVaults.exists()) {
                deleteFile(newGcVaults);
            }
            if (!newGcVaults.exists()) {
                gcVaults.mkdir();
            }
            Files.copy(gcVaults.toPath(), newGcVaults.toPath(), StandardCopyOption.REPLACE_EXISTING);
            for (File vaults : gcVaults.listFiles()) {
                File newVaults = new File(newGcVaults, vaults.getName());
                Files.copy(vaults.toPath(), newVaults.toPath());
            }
            String s = String.valueOf('"');
            String in = s + gamePath + "\\.data\\b2d5f170c62d6e37ac67c04be2235249\\GLOBAL" + s;
            String out = s + "attributes" + s;
            String attribulator = s + "decompiler tools\\attribulator\\Attribulator.CLI.exe" + s;
            long ms = System.currentTimeMillis();
            new ProcessBuilder().command(attribulator, "unpack", "-i", in, "-o", out, "-p", "WORLD", "-f", "yml").inheritIO().start().waitFor();
            System.out.println("Took " + (System.currentTimeMillis() - ms) + "ms to unpack attributes");
            deleteFile(newGcVaults);
        }
    }

    private static String findSubValues(String vltName, String file, Fields fields, String... parentNames) throws IOException {
        Fields SUB_FILE_VLT = new Fields(
                new Field(String.class, FilterType.COLON, "Name"));
        String[] fileVLT = findValues(file, vltName, false, SUB_FILE_VLT, parentNames);
        //fuck wuggie
        String subFileVLT = fileVLT[0].replace("_rocket","");
        String[] subFile = new String[]{"_t","_a","_h"};
        StringBuilder builder = new StringBuilder();
        for (String s : subFile) {
            String[] subFileValues = findValues(file, subFileVLT+s, false, fields, subFileVLT);
            if (subFileValues == null) {
                subFileValues = findValues(file, Hasher.HashVLT_MEMORY_STRING(subFileVLT+s), false, fields, subFileVLT);
            }
            if (subFileValues != null) {
                for (String sub : subFileValues) {
                    builder.append(sub).append(",");
                }
            } else {
                for (int i = 0; i < fields.valueSize; i++) {
                    builder.append("0,");
                }
                System.out.println("sub" + file + "still null for "+vltName+s+", "+Hasher.HashVLT_MEMORY_STRING(subFileVLT+s));
            }
        }
        return builder.toString();
    }

    private static void deleteFile(File file) throws IOException {
        if (file.listFiles() != null && file.listFiles().length > 0) {
            for (File child : file.listFiles()) {
                deleteFile(child);
            }
        }
        Files.delete(file.toPath());
    }

    private static String[] findValues(String file, String from, boolean parentSearch, Fields fields, String... parentNames) throws IOException {
        InputStreamReader fileReader = new InputStreamReader(Files.newInputStream(new File("attributes\\main\\attributes\\db\\" + file+".yml").toPath()), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        boolean startReading = false;
        String parentName = null;
        while ((line = reader.readLine()) != null) {
            if (line.contains("- ParentName:")) {
                parentName = line.split(":")[1].substring(1);
            }
            if (parentName != null && line.startsWith("  Name:")) {
                String vltName = line.split(":")[1].substring(1);
                boolean check = false;
                for (String name : parentNames) {
                    if (parentName.equals(name)) {
                        check = true;
                        break;
                    }
                }
                if (vltName.equals(from) && (parentSearch ? check || from.contains("_") : check)) {
                    startReading = true;
                }
            }
            if (startReading) {
                String[] split = line.split(":");
                if (split.length == 2 || line.charAt(line.length()-1) == ':') {
                    for (Field field : fields.fields) {
                        String names = field.fieldName;
                        if (split[0].contains(names)) {
                            for (int i = 0; i < field.skipCount; i++) {
                                line = reader.readLine();
                            }
                            switch (field.type) {
                                case COLON: {
                                    field.setValue(0, line.split(":")[1].substring(1));
                                    break;
                                }
                                case COLON_ARRAY: {
                                    for (int i = 0; i < field.valueCount; i++) {
                                        String value = reader.readLine().split(":")[1].substring(1);
                                        field.setValue(i, value);
                                    }
                                    break;
                                }
                                case HYPHEN: {
                                    for (int i = 0; i < field.valueCount; i++) {
                                        String newLine = reader.readLine();
                                        String[] newSplit = newLine.split("-");
                                        if (newSplit.length == 2) {
                                            String value = newSplit[1].substring(1);
                                            field.setValue(i, value);
                                        }
                                    }
                                    break;
                                }
                            }
                            if (!fields.hasAnyNullValues()) {
                                reader.close();
                                fileReader.close();
                                return fields.toStringArray();
                            }
                        }
                    }
                }
            }
        }
        reader.close();
        fileReader.close();
        if (file.equals("pvehicle")) {
            //System.out.printf("Couldn't find %s values for %s\n", file, from);
            return null;
        }
        String[] s = findValues("pvehicle", from, false, new Fields(new Field(String.class, FilterType.COLON_ARRAY, file, 1, 3)), P_NAMES);
        String parentVLT;
        if (s != null && (parentVLT = s[0]) != null) {
            String[] vlt = findValues(file, parentVLT, true, fields, parentNames);
            if (vlt != null) {
                return vlt;
            } else {
                //System.out.printf("Parent %s null for %s, %s\n", file, from, parentVLT);
                return null;
            }
        } else {
            //System.out.printf("Couldn't find %s values for %s\n", file, from);
        }
        return null;
    }

    static class Fields {
        Field[] fields;
        int valueSize;

        Fields(Field... fields) {
            this.fields = fields;
            for (Field field : fields) {
                valueSize += Math.max(1, field.valueCount);
            }
        }

        public boolean hasAnyNullValues() {
            for (Field field : fields) {
                for (Object o : field.values) {
                    if (o == null && !field.ignoreNull) {
                        return true;
                    }
                }
            }
            return false;
        }

        public String[] toStringArray() {
            String[] values = new String[valueSize];
            int i = 0;
            for (Field field : fields) {
                for (Object o : field.values) {
                    if (field.genericClass == int.class) {
                        if (o == null) {
                            o = 0;
                        }
                    } else if (field.genericClass == double.class) {
                        if (o == null) {
                            o = 0.0;
                        }
                    }
                    values[i] = String.valueOf(o);
                    i++;
                }
            }
            return values;
        }
    }

    static class Field {
        String fieldName;
        FilterType type;
        int valueCount;
        int skipCount;
        boolean ignoreNull;
        Object[] values;
        Class<?> genericClass;

        <T> Field(Class<T> clazz, FilterType type, String fieldName) {
            this.type = type;
            this.fieldName = fieldName;
            this.genericClass = clazz;
            this.values = new Object[1];
        }
        <T> Field(Class<T> clazz, FilterType type, String fieldName, int valueCount) {
            this(clazz, type, fieldName);
            this.valueCount = valueCount;
            this.values = new Object[Math.max(1, this.valueCount)];
        }
        <T> Field(Class<T> clazz, FilterType type, String fieldName, int valueCount, int skipCount) {
            this(clazz, type, fieldName, valueCount);
            this.skipCount = skipCount;
        }
        <T> Field(Class<T> clazz, FilterType type, String fieldName, int valueCount, int skipCount, boolean ignoreNull) {
            this(clazz, type, fieldName, valueCount, skipCount);
            this.ignoreNull = ignoreNull;
        }

        public void setValue(int index, String string) {
            if (genericClass == int.class) {
                this.values[index] = Integer.parseInt(string);
            } else if (genericClass == double.class) {
                this.values[index] = Double.parseDouble(string);
            } else if (genericClass == String.class) {
                this.values[index] = string;
            }
        }
    }

    enum FilterType {
        COLON, COLON_ARRAY, HYPHEN
    }
}
