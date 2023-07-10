package performance;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;

public class Parts {
    public static final Parts[] PARTS = new Parts[6];

    static {
        for (int i = 0; i < PARTS.length; i++) {
            PARTS[i] = new Parts();
        }
    }

    public static void init() throws IOException {
        System.out.println("Performance parts initialize");
        for (Type type : Type.VALUES) {
            PARTS[type.ordinal()].load(type, type.fileName());
        }
    }

    public PerfPart[] VALUES;
    public PerfPart[] GREEN_VALUES;
    public PerfPart[] BLUE_VALUES;
    public PerfPart[] RED_VALUES;
    public PerfPart[] WHITE_VALUES;

    public Parts load(Type type, String fileName) throws IOException {
        File file = new File("parts", fileName+".txt");

        InputStreamReader fileReader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        ArrayList<PerfPart> parts = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(",");
            if (split[0].equals("Empty")) {
                parts.add(new PerfPart(split[0], type, null, null, Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4])));
            } else {
                String[] nameSplit = split[0].split(" ");
                Color color = Color.valueOf(nameSplit[1]);
                Brand brand = Brand.valueOf(nameSplit[2]);
                parts.add(new PerfPart(split[0], type, color, brand, Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4])));
            }
        }
        reader.close();
        fileReader.close();
        parts.sort(Comparator.comparing(PerfPart::name).reversed());
        VALUES = parts.toArray(new PerfPart[parts.size()]);
        GREEN_VALUES = fromColor(Color.Green);
        BLUE_VALUES = fromColor(Color.Blue);
        RED_VALUES = fromColor(Color.Red);
        WHITE_VALUES = fromColor(Color.White);
        parts.clear();
        return this;
    }

    public static Parts fromType(Type type) {
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
                if (part.price() > partToCompare.price()) {
                    parts.set(parts.indexOf(part), partToCompare);
                }
                return true;
            }
        }
        return false;
    }
}
