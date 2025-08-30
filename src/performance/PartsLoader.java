package performance;

import main.Main;
import vaultlib.core.data.VltClass;
import vaultlib.core.data.VltCollection;
import vaultlib.core.db.Database;
import vaultlib.core.types.EAReflection.Int32;
import vaultlib.core.types.EAReflection.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PartsLoader {
    public static final PartsLoader[] PARTS = new PartsLoader[6];

    public static void init() throws IOException {
        for (int i = 0; i < PARTS.length; i++) {
            PARTS[i] = new PartsLoader();
        }
        Database database = Main.DB;
        VltClass performancepartClass = database.FindClass("performancepart");
        VltClass virtualitemClass = database.FindClass("virtualitem");
        readPPart(database, performancepartClass);
        readVirtualItem(database, virtualitemClass);
        for (Type type : Type.VALUES) {
            fromType(type).parts.add(new PerfPart("Empty", "", type, null, null, 0,0,0,0));
            PARTS[type.ordinal()].load();
        }
        System.out.println("Performance parts initialized");
    }
    private ArrayList<PerfPart> parts = new ArrayList<>();
    public PerfPart[] VALUES;
    public PerfPart[] GREEN_VALUES;
    public PerfPart[] BLUE_VALUES;
    public PerfPart[] RED_VALUES;
    public PerfPart[] WHITE_VALUES;

    private void load() {
        parts.sort(Comparator.comparing(PerfPart::name).reversed());
        VALUES = parts.toArray(new PerfPart[parts.size()]);
        GREEN_VALUES = fromColor(Color.Green);
        BLUE_VALUES = fromColor(Color.Blue);
        RED_VALUES = fromColor(Color.Red);
        WHITE_VALUES = fromColor(Color.White);
        parts.clear();
    }

    private static void readPPart(Database database, VltClass vltClass) {
        List<VltCollection> collections = database.rowManager.EnumerateCollections(vltClass.Name);
        for (VltCollection collection : collections) {
            String[] splitName = collection.Name.split("_");
            if (splitName.length < 5) {
                continue;
            }
            Type type = getType(splitName[2]);
            Color color = getColor(splitName[1]);
            Brand brand = getBrand(splitName[4]);
            int star = getStar(splitName[3]);
            if (splitName[0].equals("t0") && type != null && color != null && star != 0 && brand != null) {
                Int32 topspeed = collection.GetExactValue("topspeed");
                Int32 acceleration = collection.GetExactValue("acceleration");
                Int32 handling = collection.GetExactValue("handling");
                ArrayList<PerfPart> parts = fromType(type).parts;
                PerfPart perfPart = new PerfPart(star + "* " + color.name() + " " + brand.name(), collection.Name, type, color, brand, topspeed.GetValue(), acceleration.GetValue(), handling.GetValue(), 0);
                PerfPart duplicate = fromName(parts, perfPart.name());
                if (duplicate == null || duplicate.tGain() != perfPart.tGain() || duplicate.aGain() != perfPart.aGain() || duplicate.hGain() != perfPart.hGain()) {
                    parts.add(perfPart);
                }
            }
        }
    }
    private static void readVirtualItem(Database database, VltClass vltClass) {
        List<VltCollection> collections = database.rowManager.EnumerateCollections(vltClass.Name);
        for (VltCollection collection : collections) {
            Text type = collection.GetExactValue("type");
            if (type.GetValue().equals("performancepart")) {
                Text itemName = collection.GetExactValue("itemName");
                Int32 resellprice = collection.GetExactValue("resellprice");
                Text subType = collection.GetExactValue("subType");
                Type partType = getType(subType.GetValue().substring(6));
                if (partType != null) {
                    PerfPart perfPart = fromVLTName(fromType(partType).parts, itemName.GetValue());
                    if (perfPart != null) {
                        perfPart.setPrice(resellprice.GetValue() * 24);
                    }
                }
            }
        }
    }

    private static PerfPart fromVLTName(ArrayList<PerfPart> perfParts, String vltName) {
        for (PerfPart part : perfParts) {
            if (part.vltName().equals(vltName)) {
                return part;
            }
        }
        return null;
    }
    private static PerfPart fromName(ArrayList<PerfPart> perfParts, String vltName) {
        for (PerfPart part : perfParts) {
            if (part.name().equals(vltName)) {
                return part;
            }
        }
        return null;
    }
    public static PartsLoader fromType(Type type) {
        return PARTS[type.ordinal()];
    }

    private PerfPart[] fromColor(Color color) {
        ArrayList<PerfPart> parts = new ArrayList<>();
        for (PerfPart part : VALUES) {
            if ((part.color() == color || part.color() == null) && !sameGains(part, parts)) {
                parts.add(part);
            }
        }
        return parts.toArray(new PerfPart[parts.size()]);
    }

    private boolean sameGains(PerfPart part, ArrayList<PerfPart> parts) {
        for (PerfPart partToCompare : parts) {
            if (part.tGain() == partToCompare.tGain() && part.aGain() == partToCompare.aGain() && part.hGain() == partToCompare.hGain()) {
                if (partToCompare.price() > part.price()) {
                    parts.set(parts.indexOf(partToCompare), part);
                }
                return true;
            }
        }
        return false;
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
    private static Type getType(String name) {
        for (Type type : Type.VALUES) {
            if (type.fileName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
