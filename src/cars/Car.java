package cars;

public class Car {
    private int[] topSpeed, accel, handling;
    private double[] cTopSpeed, cAccel, cHandling;
    private String vltName, fullName;
    private int[] RPM;
    private int[][] wheelRatio, wheelWidth, wheelSize;
    private double[] gearRatios;
    private double[] finalDrive;
    public double[] RIM_SIZE, SECTION_WIDTH, ASPECT_RATIO, cFINAL_DRIVE, cRPM;
    public double MAX_GEAR_RATIO;
    private double torqueSplit;

    public Car(String vltName, String fullName, int[] topSpeed, int[] accel, int[] handling, int[] RPM, int[][] wheelRatio, int[][] wheelWidth, int[][] wheelSize, double[] gearRatios, double torqueSplit, double[] finalDrive) {
        this.vltName = vltName;
        this.fullName = fullName;
        this.topSpeed = topSpeed;
        this.accel = accel;
        this.handling = handling;
        this.RPM = RPM;
        this.wheelRatio = wheelRatio;
        this.wheelWidth = wheelWidth;
        this.wheelSize = wheelSize;
        this.gearRatios = gearRatios;
        this.torqueSplit = torqueSplit;
        this.finalDrive = finalDrive;
        cTopSpeed = new double[topSpeed.length];
        cAccel = new double[accel.length];
        cHandling = new double[handling.length];
        for (int i = 0; i < topSpeed.length; i++) {
            cTopSpeed[i] = topSpeed[topSpeed.length-1-i]*1.5-topSpeed[0]*0.5;
        }
        for (int i = 0; i < accel.length; i++) {
            cAccel[i] = accel[accel.length-1-i]*1.5-accel[0]*0.5;
        }
        for (int i = 0; i < handling.length; i++) {
            cHandling[i] = handling[handling.length-1-i]*1.5-handling[0]*0.5;
        }

        cTopSpeed[cTopSpeed.length-1] = topSpeed[0]*150;
        cAccel[cAccel.length-1] = accel[0]*150;
        cHandling[cHandling.length-1] = handling[0]*150;
        RIM_SIZE = new double[4];
        SECTION_WIDTH = new double[4];
        ASPECT_RATIO = new double[4];
        cFINAL_DRIVE = new double[4];
        cRPM = new double[4];
        int tireIndex = torqueSplit > 0 ? 0 : 1;
        for (int i = 0; i < RIM_SIZE.length; i++) {
            RIM_SIZE[i] = 1.5*wheelSize[i][tireIndex] - 0.5*wheelSize[0][tireIndex];
        }
        for (int i = 0; i < SECTION_WIDTH.length; i++) {
            SECTION_WIDTH[i] = 1.5*wheelWidth[i][tireIndex] - 0.5*wheelWidth[0][tireIndex];
        }
        for (int i = 0; i < ASPECT_RATIO.length; i++) {
            ASPECT_RATIO[i] = 1.5*wheelRatio[i][tireIndex] - 0.5*wheelRatio[0][tireIndex];
        }
        for (int i = 0; i < cFINAL_DRIVE.length-1; i++) {
            cFINAL_DRIVE[i] = 1.5*finalDrive[i+1] - 0.5*finalDrive[0];
        }
        for (int i = 0; i < cRPM.length-1; i++) {
            cRPM[i] = 1.5*RPM[i+1] - 0.5*RPM[0];
        }
        RIM_SIZE[3] = wheelSize[0][tireIndex]*150;
        SECTION_WIDTH[3] = wheelWidth[0][tireIndex]*150;
        ASPECT_RATIO[3] = wheelRatio[0][tireIndex]*150;
        cFINAL_DRIVE[3] = finalDrive[0]*150;
        cRPM[3] = RPM[0]*150;
        int i = gearRatios.length-1;
        while (gearRatios[i] == 0) {
            i--;
        }
        MAX_GEAR_RATIO = gearRatios[i];
    }

    public String vltName() {
        return vltName;
    }

    public String fullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return fullName;
    }

    public int[] topSpeed() {
        return topSpeed;
    }

    public int[] accel() {
        return accel;
    }

    public int[] handling() {
        return handling;
    }

    public double[] cTopSpeed() {
        return cTopSpeed;
    }

    public double[] cAccel() {
        return cAccel;
    }

    public double[] cHandling() {
        return cHandling;
    }

    public int[] RPM() {
        return RPM;
    }

    public int[][] wheelRatio() {
        return wheelRatio;
    }

    public double[] gearRatio() {
        return gearRatios;
    }

    public double[] finalDrives() {
        return finalDrive;
    }

    public int[][] wheelWidth() {
        return wheelWidth;
    }

    public double torqueSplit() {
        return torqueSplit;
    }

    public int[][] wheelSize() {
        return wheelSize;
    }
}
