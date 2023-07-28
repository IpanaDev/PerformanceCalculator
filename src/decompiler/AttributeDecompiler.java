package decompiler;

import config.ConfigFile;
import decompiler.vlt.CarVLT;
import decompiler.vlt.VltNode;
import ui.UI;
import utils.Equality;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class AttributeDecompiler {

    public static void start(File partsFile, File parsedFile) throws IOException, InterruptedException {
        File attributeFiles = new File("attributes");
        unpackAttributes(attributeFiles);
        File languagesFile = new File("languages");
        File jsonFile = new File(languagesFile, "English_Global.json");

        InputStreamReader fileReader = new InputStreamReader(Files.newInputStream(jsonFile.toPath()), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        long ms = System.currentTimeMillis();
        ArrayList<CarVLT> carVLTS = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(":");
            if (line.startsWith("  \"GM_CAR_")) {
                String vltName = split[0].substring(10, split[0].length() - 1).toLowerCase(Locale.ENGLISH);
                String fullName = split[1].substring(2, split[1].length() - 2);
                //Thanks to lang files we have extra space in some car names
                int i = fullName.length()-1;
                while (fullName.charAt(i) == ' ') {
                    fullName = fullName.substring(0, i);
                    i--;
                }
                UI.INSTANCE.decompilerMenu().setStatus("Reading car files ("+fullName+")");
                carVLTS.add(new CarVLT(vltName, fullName));
            }
        }
        readPVehicle(attributeFiles, carVLTS);
        removeInvalid(carVLTS);
        readEngine(attributeFiles, carVLTS);
        readTires(attributeFiles, carVLTS);
        readTransmission(attributeFiles, carVLTS);
        removeDuplicates(carVLTS);
        System.out.println("Took "+(System.currentTimeMillis()-ms)+"ms to read cars");
        ms = System.currentTimeMillis();
        parsedFile.createNewFile();
        StringBuilder builder = new StringBuilder((int) Math.pow(2, 16));
        for (CarVLT carVLT : carVLTS) {
            builder.append(carVLT.toString());
        }
        Writer writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(parsedFile.toPath()), StandardCharsets.UTF_8));
        writer.write(builder.toString());
        System.out.println("Took "+(System.currentTimeMillis()-ms)+"ms to parse cars");

        writer.close();
        reader.close();
        fileReader.close();
        if (partsFile.exists()) {
            deleteFile(attributeFiles);
        }
        deleteFile(languagesFile);
    }

    private static void removeInvalid(ArrayList<CarVLT> carVLTS) {
        for (int i = 0; i < carVLTS.size(); i++) {
            CarVLT carVLT = carVLTS.get(i);
            if (carVLT.engineVLT == null || carVLT.transmissionVLT == null || carVLT.tiresVLT == null) {
                System.out.println("remove invalid: "+carVLT.vltName+", "+Arrays.toString(carVLT.engineVLT)+", "+Arrays.toString(carVLT.transmissionVLT)+", "+Arrays.toString(carVLT.tiresVLT));
                carVLTS.remove(i);
                i--;
            }
        }
    }

    private static void removeDuplicates(ArrayList<CarVLT> carVLTS) {
        for (int i = 0; i < carVLTS.size(); i++) {
            CarVLT carVLT = carVLTS.get(i);
            for (int j = 0; j < carVLTS.size(); j++) {
                CarVLT carVLT2 = carVLTS.get(j);
                if (i != j && carVLT.fullName.equals(carVLT2.fullName)) {
                    boolean canRemove;
                    canRemove = Equality.isIntEqualAtIndex(carVLT.STATS, carVLT2.STATS);
                    canRemove &= Equality.isIntEqualAtIndex(carVLT.RPM, carVLT2.RPM);
                    canRemove &= Equality.isDoubleEqualAtIndex(carVLT.finalGear, carVLT2.finalGear);
                    canRemove &= Equality.isDoubleEqualAtIndex(carVLT.torqueSplit, carVLT2.torqueSplit);
                    canRemove &= Equality.isDoubleEqualAtIndex(carVLT.gearRatio, carVLT2.gearRatio);
                    canRemove &= Equality.isIntEqualAtIndex(carVLT.sectionWidth, carVLT2.sectionWidth);
                    canRemove &= Equality.isIntEqualAtIndex(carVLT.aspectRatio, carVLT2.aspectRatio);
                    canRemove &= Equality.isIntEqualAtIndex(carVLT.rimSize, carVLT2.rimSize);
                    if (canRemove) {
                        System.out.println("remove duplicate: "+carVLT.fullName+", "+carVLT.vltName);
                        carVLTS.remove(j);
                        j--;
                        i--;
                    }
                }
            }
        }
    }

    private static void readPVehicle(File attributeFile, ArrayList<CarVLT> carVLTS) throws IOException {
        File pvehicleFile = new File(attributeFile, "main\\attributes\\db\\pvehicle.yml");
        InputStreamReader fileReader = new InputStreamReader(Files.newInputStream(pvehicleFile.toPath()), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("- ParentName:")) {
                String parentName = line.substring(14);
                String vehicleName = reader.readLine().substring(8);
                boolean mainCar = parentName.equals("addon_muscle") || parentName.equals("addon_tuner") || parentName.equals("addon_exotic") || parentName.equals("muscle") || parentName.equals("tuner") || parentName.equals("exotic");
                boolean rpCar = vehicleName.endsWith("_rp");
                if (mainCar || rpCar) {
                    CarVLT carVLT = fromVLTName(vehicleName, carVLTS);
                    if (carVLT == null && !rpCar) {
                        System.out.println("PVEHICLE.YML Vehicle name doesn't exist in language file: "+vehicleName);
                    } else {
                        if (rpCar) {
                            carVLT = new CarVLT(vehicleName, fromVLTName(parentName, carVLTS).fullName + " Royal Purple");
                            carVLTS.add(carVLT);
                            System.out.println("Royal Purple car: "+vehicleName);
                        }
                        skip(reader, 12);
                        carVLT.STATS = new int[3][4];
                        //Acceleration Stats
                        for (int i = 0; i < 4; i++) {
                            String valueLine = reader.readLine().substring(8);
                            while (!Character.isDigit(valueLine.charAt(0))) {
                                valueLine = reader.readLine().substring(8);
                            }
                            int value = Integer.parseInt(valueLine);
                            carVLT.STATS[1][i] = value;
                        }
                        skip(reader, 36);
                        //Engine node
                        String classKeyEngine = reader.readLine();
                        while (!classKeyEngine.equals("      - ClassKey: engine")) {
                            classKeyEngine = reader.readLine();
                        }
                        carVLT.engineVLT = new VltNode[4];
                        for (int i = 0; i < carVLT.engineVLT.length; i++) {
                            int index = i == 0 ? 0 : 4 - i;
                            carVLT.engineVLT[index] = new VltNode(carVLT, reader.readLine().substring(23), index);
                            skip(reader,1);
                        }
                        skip(reader, 2);
                        //Handling Stats
                        for (int i = 0; i < 4; i++) {
                            //Thanks wuggie BEHAVIOR_MECHANIC_RESET: ''
                            String valueLine = reader.readLine().substring(8);
                            while (!Character.isDigit(valueLine.charAt(0))) {
                                valueLine = reader.readLine().substring(8);
                            }
                            int value = Integer.parseInt(valueLine);
                            carVLT.STATS[2][i] = value;
                        }
                        skip(reader, 19);
                        //Tires node
                        String classKeyTires = reader.readLine();
                        while (!classKeyTires.equals("      - ClassKey: tires")) {
                            classKeyTires = reader.readLine();
                        }
                        carVLT.tiresVLT = new VltNode[4];
                        for (int i = 0; i < carVLT.tiresVLT.length; i++) {
                            int index = i == 0 ? 0 : 4 - i;
                            carVLT.tiresVLT[index] = new VltNode(carVLT, reader.readLine().substring(23), index);
                            skip(reader,1);
                        }
                        skip(reader, 2);
                        //Top Speed Stats
                        for (int i = 0; i < 4; i++) {
                            int value = Integer.parseInt(reader.readLine().substring(8));
                            carVLT.STATS[0][i] = value;
                        }
                        skip(reader, 3);
                        carVLT.transmissionVLT = new VltNode[4];
                        String classKeyTransmission = reader.readLine();
                        while (!classKeyTransmission.equals("      - ClassKey: transmission")) {
                            classKeyTransmission = reader.readLine();
                        }
                        for (int i = 0; i < carVLT.transmissionVLT.length; i++) {
                            int index = i == 0 ? 0 : carVLT.transmissionVLT.length - i;
                            carVLT.transmissionVLT[index] = new VltNode(carVLT, reader.readLine().substring(23), index);
                            if (index != 1) {
                                skip(reader, 1);
                            }
                        }
                    }
                }
            }
        }
        reader.close();
        fileReader.close();
    }

    private static void readEngine(File attributeFile, ArrayList<CarVLT> carVLTS) throws IOException {
        File pvehicleFile = new File(attributeFile, "main\\attributes\\db\\engine.yml");
        InputStreamReader fileReader = new InputStreamReader(Files.newInputStream(pvehicleFile.toPath()), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("  Name:")) {
                String vehicleName = line.substring(8);
                ArrayList<VltNode> vltNodes = fromVLTEngine(vehicleName, carVLTS);
                if (vltNodes.isEmpty()) {
                    System.out.println("ENGINE.YML " + vehicleName + " doesn't exist in any EngineVLTs");
                } else {
                    skip(reader, 20);
                    if (reader.readLine().equals("      Data: []")) {
                        skip(reader, 6);
                    } else {
                        skip(reader, 8);
                    }
                    int rpm = Integer.parseInt(reader.readLine().substring(14));
                    for (VltNode vltNode : vltNodes) {
                        CarVLT carVLT = vltNode.carVLT();
                        int index = vltNode.index();
                        if (carVLT.RPM == null) {
                            carVLT.RPM = new int[4];
                        }
                        carVLT.RPM[index] = rpm;
                    }
                    skip(reader, 3);
                }
            }
        }
        reader.close();
        fileReader.close();
    }
    private static void readTires(File attributeFile, ArrayList<CarVLT> carVLTS) throws IOException {
        File pvehicleFile = new File(attributeFile, "main\\attributes\\db\\tires.yml");
        InputStreamReader fileReader = new InputStreamReader(Files.newInputStream(pvehicleFile.toPath()), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("  Name:")) {
                String vehicleName = line.substring(8);
                ArrayList<VltNode> vltNodes = fromVLTTires(vehicleName, carVLTS);
                if (vltNodes.isEmpty()) {
                    System.out.println("TIRES.YML " + vehicleName + " doesn't exist in any TiresVLTs");
                } else {
                    skip(reader, 25);
                    int FSW = Integer.parseInt(reader.readLine().substring(13));
                    int RSW = Integer.parseInt(reader.readLine().substring(12));
                    skip(reader, 1);
                    int FRS = Integer.parseInt(reader.readLine().substring(13));
                    int RRS = Integer.parseInt(reader.readLine().substring(12));
                    skip(reader, 7);
                    int FAR = Integer.parseInt(reader.readLine().substring(13));
                    int RAR = Integer.parseInt(reader.readLine().substring(12));
                    for (VltNode vltNode : vltNodes) {
                        CarVLT carVLT = vltNode.carVLT();
                        int index = vltNode.index();
                        if (carVLT.sectionWidth == null) {
                            carVLT.sectionWidth = new int[4][2];
                        }
                        if (carVLT.rimSize == null) {
                            carVLT.rimSize = new int[4][2];
                        }
                        if (carVLT.aspectRatio == null) {
                            carVLT.aspectRatio = new int[4][2];
                        }
                        carVLT.sectionWidth[index][0] = FSW;
                        carVLT.sectionWidth[index][1] = RSW;
                        carVLT.rimSize[index][0] = FRS;
                        carVLT.rimSize[index][1] = RRS;
                        carVLT.aspectRatio[index][0] = FAR;
                        carVLT.aspectRatio[index][1] = RAR;
                    }
                }
            }
        }
        reader.close();
        fileReader.close();
    }
    private static void readTransmission(File attributeFile, ArrayList<CarVLT> carVLTS) throws IOException {
        File pvehicleFile = new File(attributeFile, "main\\attributes\\db\\transmission.yml");
        InputStreamReader fileReader = new InputStreamReader(Files.newInputStream(pvehicleFile.toPath()), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("  Name:")) {
                String vehicleName = line.substring(8);
                ArrayList<VltNode> carVLTs = fromVLTTransmission(vehicleName, carVLTS);
                if (carVLTs.isEmpty()) {
                    System.out.println("TRANSMISSION.YML " + vehicleName + " doesn't exist in any TransmissionVLTs");
                } else {
                    skip(reader, 4);
                    double[] gearRatio = new double[9];
                    for (int i = 0; i < gearRatio.length+1; i++) {
                        String newLine = reader.readLine();
                        if (newLine.charAt(6) == '-') {
                            gearRatio[i] = roundUp(Double.parseDouble(newLine.substring(8)), 5);
                        } else {
                            break;
                        }
                    }
                    skip(reader, 29);
                    double torqueSplit = roundUp(Double.parseDouble(reader.readLine().substring(17)), 5);
                    skip(reader, 4);
                    double finalGear = roundUp(Double.parseDouble(reader.readLine().substring(16)), 5);
                    skip(reader, 1);
                    for (VltNode vltNode : carVLTs) {
                        CarVLT carVLT = vltNode.carVLT();
                        int index = vltNode.index();
                        if (carVLT.gearRatio == null) {
                            carVLT.gearRatio = new double[4][9];
                        }
                        if (carVLT.torqueSplit == null) {
                            carVLT.torqueSplit = new double[4];
                        }
                        if (carVLT.finalGear == null) {
                            carVLT.finalGear = new double[4];
                        }
                        System.arraycopy(gearRatio, 0, carVLT.gearRatio[index], 0, gearRatio.length);
                        carVLT.torqueSplit[index] = torqueSplit;
                        carVLT.finalGear[index] = finalGear;
                    }
                }
            }
        }
        reader.close();
        fileReader.close();
    }
    private static CarVLT fromVLTName(String vltName, ArrayList<CarVLT> carVLTS) {
        for (CarVLT carVLT : carVLTS) {
            if (carVLT.vltName.equals(vltName)) {
                return carVLT;
            }
        }
        return null;
    }
    private static ArrayList<VltNode> fromVLTEngine(String vltName, ArrayList<CarVLT> carVLTS) {
        ArrayList<VltNode> pairs = new ArrayList<>();
        for (CarVLT carVLT : carVLTS) {
            for (VltNode vltNode : carVLT.engineVLT) {
                if (vltNode.name().equals(vltName)) {
                    pairs.add(vltNode);
                }
            }
        }
        return pairs;
    }
    private static ArrayList<VltNode> fromVLTTires(String vltName, ArrayList<CarVLT> carVLTS) {
        ArrayList<VltNode> pairs = new ArrayList<>();
        for (CarVLT carVLT : carVLTS) {
            for (VltNode vltNode : carVLT.tiresVLT) {
                if (vltNode.name().equals(vltName)) {
                    pairs.add(vltNode);
                }
            }
        }
        return pairs;
    }
    private static ArrayList<VltNode> fromVLTTransmission(String vltName, ArrayList<CarVLT> carVLTS) {
        ArrayList<VltNode> pairs = new ArrayList<>();
        for (CarVLT carVLT : carVLTS) {
            for (VltNode vltNode : carVLT.transmissionVLT) {
                if (vltNode.name().equals(vltName)) {
                    pairs.add(vltNode);
                }
            }
        }
        return pairs;
    }
    private static void skip(BufferedReader reader, int times) throws IOException {
        for (int i = 0; i < times; i++) {
            reader.readLine();
        }
    }
    public static double roundUp(double value, int places) {
        if (places<0) {
            throw new IllegalArgumentException();
        } else {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
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

    private static void deleteFile(File file) throws IOException {
        if (file.listFiles() != null && file.listFiles().length > 0) {
            for (File child : file.listFiles()) {
                deleteFile(child);
            }
        }
        Files.delete(file.toPath());
    }
}
