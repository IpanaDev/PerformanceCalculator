package cars;

public class Car {
    private int[] topSpeed, accel, handling;
    private double[] cTopSpeed, cAccel, cHandling;
    private String vltName, fullName;
    private int RPM;
    private int[] wheelRatio, wheelWidth, wheelSize;
    private double[] gearRatios, differential;
    private double[] finalDrive;
    private DrivetrainType drivetrainType;
    private double[] stockTopSpeeds;

    public Car(String vltName, String fullName, int[] topSpeed, int[] accel, int[] handling, int RPM, int[] wheelRatio, int[] wheelWidth, int[] wheelSize, double[] gearRatios, double[] differential, double[] finalDrive) {
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
        this.differential = differential;
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
        if ((differential[0] > 0 && differential[1] > 0 && differential[2] > 0) || (differential[0] == 0 && differential[1] == 0 && differential[2] == 0)) {
            setDrivetrain(DrivetrainType.ALL_WHEEL);
        }
        if (differential[2] == 0) {
            if (differential[0] == 0 && differential[1] > 0) {
                setDrivetrain(DrivetrainType.REAR_WHEEL);
            } else if (differential[0] > 0 && differential[1] == 0) {
                setDrivetrain(DrivetrainType.FRONT_WHEEL);
            }
            if (differential[0] > 0 && differential[1] > 0) {
                setDrivetrain(DrivetrainType.FOUR_WHEEL);
            }
        }
        this.stockTopSpeeds = new double[7];
    }

    public double[] stockTopSpeeds() {
        return stockTopSpeeds;
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

    public int RPM() {
        return RPM;
    }

    public int[] wheelRatio() {
        return wheelRatio;
    }

    public double[] gearRatio() {
        return gearRatios;
    }

    public double[] finalDrives() {
        return finalDrive;
    }

    public int[] wheelWidth() {
        return wheelWidth;
    }

    public double[] differential() {
        return differential;
    }

    public int[] wheelSize() {
        return wheelSize;
    }

    public DrivetrainType drivetrain() {
        return drivetrainType;
    }

    public void setDrivetrain(DrivetrainType _drivetrainType) {
        drivetrainType = _drivetrainType;
    }
}
