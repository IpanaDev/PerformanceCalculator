package performance;

public class PerfPart {
    public static final PerfPart EMPTY_ENGINE = new PerfPart("Empty", Type.ENGINE, null, null, 0,0,0,0);
    public static final PerfPart EMPTY_TURBO = new PerfPart("Empty", Type.FORCED_INDUCTION, null, null, 0,0,0,0);
    public static final PerfPart EMPTY_TRANSMISSION = new PerfPart("Empty", Type.TRANSMISSION, null, null, 0,0,0,0);
    public static final PerfPart EMPTY_SUSPENSION = new PerfPart("Empty", Type.SUSPENSION, null, null, 0,0,0,0);
    public static final PerfPart EMPTY_BRAKES = new PerfPart("Empty", Type.BRAKES, null, null, 0,0,0,0);
    public static final PerfPart EMPTY_TIRES = new PerfPart("Empty", Type.TIRES, null, null, 0,0,0,0);
    private String name;
    private Type type;
    private Color color;
    private Brand brand;
    private int tGain, aGain, hGain, price;

    public PerfPart(String name, Type type, Color color, Brand brand, int tGain, int aGain, int hGain, int price) {
        this.name = name;
        this.type = type;
        this.color = color;
        this.brand = brand;
        this.tGain = tGain;
        this.aGain = aGain;
        this.hGain = hGain;
        this.price = price;
    }

    public String name() {
        return name;
    }

    public Color color() {
        return color;
    }

    public Brand brand() {
        return brand;
    }

    public int tGain() {
        return tGain;
    }

    public int aGain() {
        return aGain;
    }

    public int hGain() {
        return hGain;
    }

    public int price() {
        return price;
    }

    public Type type() {
        return type;
    }
}
