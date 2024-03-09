package cars;

import cars.value.AxlePairValue;
import config.ConfigFile;
import lang.LangUnpacker;
import lang.LanguagePackJson;
import main.Main;
import utils.Benchmark;
import utils.Pair;
import vaultlib.core.data.VltClass;
import vaultlib.core.data.VltCollection;
import vaultlib.core.db.Database;
import vaultlib.core.types.EAReflection.Float;
import vaultlib.core.types.VLTArrayType;
import vaultlib.core.types.VLTBaseType;
import vaultlib.core.types.attrib.RefSpec;
import vaultlib.frameworks.AxlePair;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class CarLoader {
    public static final ArrayList<Car> CARS = new ArrayList<>();
    private static final List<CarSpecKey> CHASSIS_KEYS = new ArrayList<>();
    private static final List<CarSpecKey> ENGINE_KEYS = new ArrayList<>();
    private static final List<CarSpecKey> TIRES_KEYS = new ArrayList<>();
    private static final List<CarSpecKey> TRANSMISSION_KEYS = new ArrayList<>();
    public static void init() throws Exception {
        File gameDir = new File(String.valueOf(ConfigFile.GAME_LOCATION.value()));
        File data = new File(gameDir, ".data\\b2d5f170c62d6e37ac67c04be2235249");
        readCarNames(data);
        Database database = Main.DB;
        VltClass pvehicleClass = database.FindClass("pvehicle");
        VltClass chassisClass = database.FindClass("chassis");
        VltClass engineClass = database.FindClass("engine");
        VltClass tiresClass = database.FindClass("tires");
        VltClass transmissionClass = database.FindClass("transmission");
        readPVehicle(database, pvehicleClass);
        readChassis(database, chassisClass);
        readEngine(database, engineClass);
        readTires(database, tiresClass);
        readTransmission(database, transmissionClass);
        for (Car car : CARS) {
            car.setupPreValues();
        }
        CARS.sort(Comparator.comparing(Car::fullName));
    }
    private static void readTransmission(Database database, VltClass vltClass) {
        List<VltCollection> collections = database.rowManager.EnumerateCollections(vltClass.Name);
        for (VltCollection collection : collections) {
            for (int i = 0; i < TRANSMISSION_KEYS.size(); i++) {
                CarSpecKey carSpecKey = TRANSMISSION_KEYS.get(i);
                if (carSpecKey.refSpec.CollectionKey().equals(collection.Name)) {
                    Float FINAL_GEAR = collection.GetExactValue("FINAL_GEAR");
                    Float TORQUE_SPLIT = collection.GetExactValue("TORQUE_SPLIT");
                    VLTArrayType<Float> GEAR_RATIO = collection.GetExactValue("GEAR_RATIO");
                    VLTArrayType<Float> GEAR_EFFICIENCY = collection.GetExactValue("GEAR_EFFICIENCY");
                    carSpecKey.car.finalGear.put(carSpecKey.index, FINAL_GEAR.GetValue());
                    carSpecKey.car.torqueSplit.put(carSpecKey.index, TORQUE_SPLIT.GetValue());
                    carSpecKey.car.gearRatio.put(carSpecKey.index, GEAR_RATIO.Items);
                    carSpecKey.car.gearEfficiency.put(carSpecKey.index, GEAR_EFFICIENCY.Items);
                    TRANSMISSION_KEYS.remove(i--);
                }
            }
        }
    }
    private static void readTires(Database database, VltClass vltClass) {
        List<VltCollection> collections = database.rowManager.EnumerateCollections(vltClass.Name);
        for (VltCollection collection : collections) {
            for (int i = 0; i < TIRES_KEYS.size(); i++) {
                CarSpecKey carSpecKey = TIRES_KEYS.get(i);
                if (carSpecKey.refSpec.CollectionKey().equals(collection.Name)) {
                    AxlePair ASPECT_RATIO = collection.GetExactValue("ASPECT_RATIO");
                    AxlePair SECTION_WIDTH = collection.GetExactValue("SECTION_WIDTH");
                    AxlePair RIM_SIZE = collection.GetExactValue("RIM_SIZE");
                    carSpecKey.car.aspectRatio[carSpecKey.index] = new AxlePairValue(ASPECT_RATIO);
                    carSpecKey.car.sectionWidth[carSpecKey.index] = new AxlePairValue(SECTION_WIDTH);
                    carSpecKey.car.rimSize[carSpecKey.index] = new AxlePairValue(RIM_SIZE);
                    TIRES_KEYS.remove(i--);
                }
            }
        }
    }
    private static void readEngine(Database database, VltClass vltClass) {
        List<VltCollection> collections = database.rowManager.EnumerateCollections(vltClass.Name);
        for (VltCollection collection : collections) {
            for (int i = 0; i < ENGINE_KEYS.size(); i++) {
                CarSpecKey carSpecKey = ENGINE_KEYS.get(i);
                if (carSpecKey.refSpec.CollectionKey().equals(collection.Name)) {
                    Float RED_LINE = collection.GetExactValue("RED_LINE");
                    VLTArrayType<Float> TORQUE = collection.GetExactValue("TORQUE");
                    carSpecKey.car.rpm.put(carSpecKey.index, RED_LINE.GetValue());
                    carSpecKey.car.torque.put(carSpecKey.index, TORQUE.Items);
                    ENGINE_KEYS.remove(i--);
                }
            }
        }
    }

    private static void readChassis(Database database, VltClass vltClass) {
        List<VltCollection> collections = database.rowManager.EnumerateCollections(vltClass.Name);
        for (VltCollection collection : collections) {
            for (int i = 0; i < CHASSIS_KEYS.size(); i++) {
                CarSpecKey carSpecKey = CHASSIS_KEYS.get(i);
                if (carSpecKey.refSpec.CollectionKey().equals(collection.Name)) {
                    Float MASS = collection.GetExactValue("MASS");
                    Float DRAG_COEFFICIENT = collection.GetExactValue("DRAG_COEFFICIENT");
                    carSpecKey.car.mass.put(carSpecKey.index, MASS.GetValue());
                    carSpecKey.car.coefficient.put(carSpecKey.index, DRAG_COEFFICIENT.GetValue());
                    CHASSIS_KEYS.remove(i--);
                }
            }
        }
    }

    private static void readPVehicle(Database database, VltClass vltClass) {
        List<VltCollection> collections = database.rowManager.EnumerateCollections(vltClass.Name);
        for (VltCollection collection : collections) {
            Car car = fromVltName(collection.Name);
            if (car != null) {//TODO: rp cars
                //its always sorted as BASE_NODE, H_NODE, A_NODE, T_NODE
                if (!collection.HasAllEntries("TopSpeed", "Acceleration", "Handling")) {
                    CARS.remove(car);
                    continue;
                }
                VLTArrayType<Float> tStat = collection.GetExactValue("TopSpeed");
                VLTArrayType<Float> aStat = collection.GetExactValue("Acceleration");
                VLTArrayType<Float> hStat = collection.GetExactValue("Handling");
                VLTArrayType<RefSpec> chassis = collection.GetExactValue("chassis");
                VLTArrayType<RefSpec> engine = collection.GetExactValue("engine");
                VLTArrayType<RefSpec> tires = collection.GetExactValue("tires");
                VLTArrayType<RefSpec> transmission = collection.GetExactValue("transmission");
                VLTArrayType<RefSpec> nos = collection.GetExactValue("nos");
                car.nosLevel(toLevel(nos.GetValue(0).CollectionKey()));
                car.setTStats(tStat.Items);
                car.setAStats(aStat.Items);
                car.setHStats(hStat.Items);
                for (int i = 0; i < chassis.Items.size(); i++) {
                    CHASSIS_KEYS.add(new CarSpecKey(chassis.Items.get(i), car, i));
                }
                for (int i = 0; i < engine.Items.size(); i++) {
                    ENGINE_KEYS.add(new CarSpecKey(engine.Items.get(i), car, i));
                }
                for (int i = 0; i < tires.Items.size(); i++) {
                    TIRES_KEYS.add(new CarSpecKey(tires.Items.get(i), car, i));
                }
                for (int i = 0; i < transmission.Items.size(); i++) {
                    TRANSMISSION_KEYS.add(new CarSpecKey(transmission.Items.get(i), car, i));
                }
            }
        }
    }

    private static void readCarNames(File dataFolder) throws Exception {
        Path langPath = new File(dataFolder, "LANGUAGES").toPath();
        LanguagePackJson json = LangUnpacker.read(langPath, "English");
        String key = "GM_CAR";
        for (Map.Entry<String, String> fileEntry : json.entries().entrySet()) {
            if (fileEntry.getKey() != null && fileEntry.getKey().startsWith(key)) {
                String vltName = fileEntry.getKey().substring(key.length() + 1).toLowerCase(Locale.ENGLISH);
                String fullName = fileEntry.getValue();
                int i = fullName.length() - 1;
                while (fullName.charAt(i) == ' ') {
                    fullName = fullName.substring(0, i);
                    i--;
                }
                CARS.add(new Car(vltName, fullName));
            }
        }
    }
    private static int toLevel(String nodeName) {
        switch (nodeName) {
            case "nos_top": return 4;
            case "nos_base": return 3;
            case "0x9BCC9CC7": return 2;
            case "0x79A84534": return 1;
            case "0xEBEA290E": return 0;
        }
        return -1;
    }
    private static Car fromVltName(String vltName) {
        for (Car car : CARS) {
            if (car.vltName().equals(vltName)) {
                return car;
            }
        }
        return null;
    }
    private static class CarSpecKey {
        private RefSpec refSpec;
        private Car car;
        private int index;


        private CarSpecKey(RefSpec refSpec, Car car, int index) {
            this.refSpec = refSpec;
            this.car = car;
            this.index = index;
        }
    }
}
