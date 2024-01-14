package cars;

import cars.value.DynamicValue;
import cars.value.IValue;
import cars.value.StaticValue;

public class Car {
    private int[] tStats, aStats, hStats;
    private double[] cTStats, cAStats, cHStats;
    private String vltName, fullName;
    private int nosLevel = -1;
    public int[] RPM;
    public int[] aspectRatio, sectionWidth, rimSize;
    public double[] gearRatio, finalGear, torqueSplit;
    public IValue RIM_SIZE, SECTION_WIDTH, ASPECT_RATIO, FINAL_DRIVE, cRPM;
    public IValue[] GEAR_RATIO;
    public int MAX_GEAR_INDEX;

    public Car() {

    }

    public void setupPreValues() {
        cTStats = new double[tStats.length];
        cAStats = new double[aStats.length];
        cHStats = new double[hStats.length];
        for (int i = 0; i < tStats.length; i++) {
            cTStats[i] = tStats[tStats.length-1-i]*1.5-tStats[0]*0.5;
        }
        for (int i = 0; i < aStats.length; i++) {
            cAStats[i] = aStats[aStats.length-1-i]*1.5-aStats[0]*0.5;
        }
        for (int i = 0; i < hStats.length; i++) {
            cHStats[i] = hStats[hStats.length-1-i]*1.5-hStats[0]*0.5;
        }
        cTStats[cTStats.length-1] = tStats[0]*150;
        cAStats[cAStats.length-1] = aStats[0]*150;
        cHStats[cHStats.length-1] = hStats[0]*150;
        if (RPM.length > 1) {
            double[] RPM_ARRAY = new double[4];
            for (int i = 0; i < RPM_ARRAY.length - 1; i++) {
                RPM_ARRAY[i] = 1.5 * RPM[i + 1] - 0.5 * RPM[0];
            }
            RPM_ARRAY[3] = RPM[0] * 150;
            cRPM = new DynamicValue(RPM_ARRAY);
        } else {
            cRPM = new StaticValue(RPM[0]);
        }
        int tireIndex = avgTorque() > 0 ? 0 : 1;
        if (aspectRatio.length > 2) {
            double[] ASPECT_RATIO_ARRAY = new double[4];
            for (int i = 0; i < ASPECT_RATIO_ARRAY.length-1; i++) {
                int arrayIndex = i*2 + 2 + tireIndex;
                ASPECT_RATIO_ARRAY[i] = 1.5*aspectRatio[arrayIndex] - 0.5*aspectRatio[tireIndex];
            }
            ASPECT_RATIO_ARRAY[3] = 150*aspectRatio[tireIndex];
            ASPECT_RATIO = new DynamicValue(ASPECT_RATIO_ARRAY);
        } else {
            ASPECT_RATIO = new StaticValue(aspectRatio[tireIndex]);
        }
        if (sectionWidth.length > 2) {
            double[] SECTION_WIDTH_ARRAY = new double[4];
            for (int i = 0; i < SECTION_WIDTH_ARRAY.length-1; i++) {
                int arrayIndex = i*2 + 2 + tireIndex;
                SECTION_WIDTH_ARRAY[i] = 1.5*sectionWidth[arrayIndex] - 0.5*sectionWidth[tireIndex];
            }
            SECTION_WIDTH_ARRAY[3] = 150*sectionWidth[tireIndex];
            SECTION_WIDTH = new DynamicValue(SECTION_WIDTH_ARRAY);
        } else {
            SECTION_WIDTH = new StaticValue(sectionWidth[tireIndex]);
        }
        if (rimSize.length > 2) {
            double[] RIM_SIZE_ARRAY = new double[4];
            for (int i = 0; i < RIM_SIZE_ARRAY.length-1; i++) {
                int arrayIndex = i*2 + 2 + tireIndex;
                RIM_SIZE_ARRAY[i] = 1.5*rimSize[arrayIndex] - 0.5*rimSize[tireIndex];
            }
            RIM_SIZE_ARRAY[3] = 150*rimSize[tireIndex];
            RIM_SIZE = new DynamicValue(RIM_SIZE_ARRAY);
        } else {
            RIM_SIZE = new StaticValue(rimSize[tireIndex]);
        }
        if (finalGear.length > 1) {
            double[] FINAL_DRIVE_ARRAY = new double[4];
            for (int i = 0; i < FINAL_DRIVE_ARRAY.length-1; i++) {
                FINAL_DRIVE_ARRAY[i] = 1.5*finalGear[i+1] - 0.5*finalGear[0];
            }
            FINAL_DRIVE_ARRAY[3] = finalGear[0]*150;
            FINAL_DRIVE = new DynamicValue(FINAL_DRIVE_ARRAY);
        } else {
            FINAL_DRIVE = new StaticValue(finalGear[0]);
        }
        if (gearRatio.length > 9) {
            double[][] GEAR_RATIO_ARRAY = new double[9][4];
            for (int i = 0; i < GEAR_RATIO_ARRAY.length; i++) {
                for (int j = 0; j < 3; j++) {
                    GEAR_RATIO_ARRAY[i][j] = gearRatio[(j+1)*9 + i]*1.5 - gearRatio[i]*0.5;
                }
            }
            for (int i = 0; i < GEAR_RATIO_ARRAY.length; i++) {
                GEAR_RATIO_ARRAY[i][3] = gearRatio[i]*150;
            }
            GEAR_RATIO = new DynamicValue[9];
            for (int i = 0; i < GEAR_RATIO.length; i++) {
                GEAR_RATIO[i] = new DynamicValue(GEAR_RATIO_ARRAY[i]);
            }
        } else {
            GEAR_RATIO = new StaticValue[9];
            for (int i = 0; i < GEAR_RATIO.length; i++) {
                GEAR_RATIO[i] = new StaticValue(gearRatio[i]);
            }
        }
        int i = 8;
        while (gearRatio[i] == 0) {
            i--;
        }
        MAX_GEAR_INDEX = i;
    }

    private double avgTorque() {
        double sum = 0;
        for (double t : torqueSplit) {
            sum += t;
        }
        return sum / torqueSplit.length;
    }

    public String fullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return fullName;
    }

    public String nosLevel() {
        String s;
        switch (nosLevel) {
            case 4: s = " (Cosmic)"; break;
            case 3: s = " (Strong)"; break;
            case 2: s = " (Normal)"; break;
            case 1: s = " (Weak)"; break;
            case 0: s = " (Weakest)"; break;
            default: s = " (Unknown)"; break;
        }
        return nosLevel + s;
    }

    public double[] tStats() {
        return cTStats;
    }

    public double[] aStats() {
        return cAStats;
    }

    public double[] hStats() {
        return cHStats;
    }

    public double[] gearRatio() {
        return gearRatio;
    }
}
