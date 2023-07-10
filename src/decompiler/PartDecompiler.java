package decompiler;

import performance.Brand;
import performance.Color;
import performance.Parts;
import performance.Type;
import ui.UI;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class PartDecompiler {
    public static void start(File partsFile) throws IOException, InterruptedException {
        File attributeFiles = new File("attributes");
        AttributeDecompiler.unpackAttributes(attributeFiles);
        File performanceParts = new File("attributes\\main\\commerce\\commerce\\performancepart.yml");
        File virtualItem = new File("attributes\\main\\commerce\\commerce\\virtualitem.yml");
        InputStreamReader fileReader = new InputStreamReader(Files.newInputStream(performanceParts.toPath()), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        partsFile.mkdir();
        PartWriter[] partWriters = setupWriters(partsFile);

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("    name:")) {
                String fullName = line.substring(10);
                String[] splitName = fullName.split("_");
                Parts parts = get(splitName[2]);
                Color color = getColor(splitName[1]);
                Brand brand = getBrand(splitName[4]);
                int star = getStar(splitName[3]);
                if (parts != null && color != null && star != 0 && brand != null) {
                    UI.INSTANCE.decompilerMenu().setStatus("Parsing ATTRIBUTE files (parts/"+fullName+")");
                    StringBuilder builder = getBuilder(splitName[2], partWriters);
                    builder.append(star).append("* ").append(color.name()).append(" ").append(brand.name()).append(",");//Star Color Brand
                    skip(reader, 2); //skipping to top speed
                    String topSpeed = reader.readLine().split(":")[1].substring(1);
                    skip(reader, 3); //skipping to handling
                    String handling = reader.readLine().split(":")[1].substring(1);
                    String acceleration = reader.readLine().split(":")[1].substring(1);
                    builder.append(topSpeed).append(",").append(acceleration).append(",").append(handling).append(",");
                    int price = getPrice(virtualItem, fullName);
                    builder.append(price).append("\n");
                }
            }
        }
        for (PartWriter partWriter : partWriters) {
            partWriter.builder.append("Empty,0,0,0,0");
            Writer writer = partWriter.writer;
            writer.write(partWriter.builder.toString());
            writer.close();
        }

        reader.close();
        fileReader.close();
        deleteFile(attributeFiles);
    }

    private static void deleteFile(File file) throws IOException {
        if (file.listFiles() != null && file.listFiles().length > 0) {
            for (File child : file.listFiles()) {
                deleteFile(child);
            }
        }
        Files.delete(file.toPath());
    }

    private static StringBuilder getBuilder(String s, PartWriter[] partWriters) {
        for (PartWriter partWriter : partWriters) {
            if (s.equals(partWriter.name)) {
                return partWriter.builder;
            }
        }
        return null;
    }

    private static int getPrice(File virtualItem, String fullName) throws IOException {
        InputStreamReader fileReader = new InputStreamReader(Files.newInputStream(virtualItem.toPath()), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("- ParentName:")) {
                String parentType = line.split(":")[1].substring(1);
                //0x4EA19B0E = forcedinduction vlt hash
                if (parentType.equals("engine") ||
                        parentType.equals("0x4EA19B0E") ||
                        parentType.equals("transmission") ||
                        parentType.equals("suspension") ||
                        parentType.equals("brakes") ||
                        parentType.equals("tires")) {
                    skip(reader, 7); //skip to itemName
                    String itemName = reader.readLine().split(":")[1].substring(1);
                    if (itemName.equals(fullName)) {
                        skip(reader, 3); //skip to resellPrice
                        String resellPrice = reader.readLine().split(":")[1].substring(1);
                        reader.close();
                        fileReader.close();
                        return Integer.parseInt(resellPrice)*24; //WUGG's buying price = 24x selling price
                    }
                }
            }
        }
        reader.close();
        fileReader.close();
        return -1;
    }

    private static void skip(BufferedReader reader, int times) throws IOException {
        for (int i = 0; i < times; i++) {
            reader.readLine();
        }
    }

    private static int getStar(String s) {
        switch (s) {
            case "elite":
                return 5;
            case "uber":
                return 4;
            case "pro":
                return 3;
            case "race":
                return 2;
            case "street":
                return 1;
        }
        return 0;
    }

    private static Brand getBrand(String s) {
        switch (s) {
            case "eliterarity":
                return Brand.Elite;
            case "rarerarity":
                return Brand.Custom;
            case "uncommonrarity":
                return Brand.Tuned;
            case "commonrarity":
                return Brand.Sport;
            case "greyrarity":
                return Brand.Improved;
        }
        return null;
    }

    private static Color getColor(String s) {
        switch (s) {
            case "pistonhead":
                return Color.Green;
            case "smokeshow":
                return Color.Blue;
            case "rails":
                return Color.Red;
            case "gearhead":
                return Color.White;
        }
        return null;
    }

    private static Parts get(String name) {
        for (Type type : Type.VALUES) {
            if (type.fileName().equals(name)) {
                return Parts.fromType(type);
            }
        }
        return null;
    }

    private static PartWriter[] setupWriters(File parent) throws IOException {
        String[] names = {"engine","forcedinduction","transmission","suspension","brakes","tires"};
        PartWriter[] partWriters = new PartWriter[names.length];
        for (int i = 0; i < names.length; i++) {
            partWriters[i] = new PartWriter(names[i], parent);
        }
        return partWriters;
    }

    static class PartWriter {
        String name;
        File file;
        Writer writer;
        StringBuilder builder;

        public PartWriter(String name, File parent) throws IOException {
            this.name = name;
            this.file = new File(parent, name+".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            this.writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8));
            this.builder = new StringBuilder((int) Math.pow(2, 16));
        }
    }
}
