package decompiler.vlt;

public class VltNode {
    private CarVLT carVLT;
    private String name;
    private int index;

    public VltNode(CarVLT carVLT, String name, int index) {
        this.carVLT = carVLT;
        this.name = name;
        this.index = index;
    }

    public CarVLT carVLT() {
        return carVLT;
    }

    public String name() {
        return name;
    }

    public int index() {
        return index;
    }
}
