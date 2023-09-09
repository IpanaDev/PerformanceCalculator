package cars;

public class Car {
    private int[] tStats, aStats, hStats;
    private double[] cTStats, cAStats, cHStats;
    private String vltName, fullName;
    public int[] RPM;
    public int[] aspectRatio, sectionWidth, rimSize;
    public double[] gearRatio, finalGear, torqueSplit;
    public double[] RIM_SIZE, SECTION_WIDTH, ASPECT_RATIO, FINAL_DRIVE, cRPM;
    public double[][] GEAR_RATIO;
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
            cRPM = new double[4];
            for (int i = 0; i < cRPM.length - 1; i++) {
                cRPM[i] = 1.5 * RPM[i + 1] - 0.5 * RPM[0];
            }
            cRPM[3] = RPM[0] * 150;
        } else {
            cRPM = new double[1];
            cRPM[0] = RPM[0];
        }
        int tireIndex = avgTorque() > 0 ? 0 : 1;
        if (aspectRatio.length > 2) {
            ASPECT_RATIO = new double[4];
            for (int i = 0; i < ASPECT_RATIO.length-1; i++) {
                int arrayIndex = i*2 + 2 + tireIndex;
                ASPECT_RATIO[i] = 1.5*aspectRatio[arrayIndex] - 0.5*aspectRatio[tireIndex];
            }
            ASPECT_RATIO[3] = 150*aspectRatio[tireIndex];
        } else {
            ASPECT_RATIO = new double[1];
            ASPECT_RATIO[0] = aspectRatio[tireIndex];
        }
        if (sectionWidth.length > 2) {
            SECTION_WIDTH = new double[4];
            for (int i = 0; i < SECTION_WIDTH.length-1; i++) {
                int arrayIndex = i*2 + 2 + tireIndex;
                SECTION_WIDTH[i] = 1.5*sectionWidth[arrayIndex] - 0.5*sectionWidth[tireIndex];
            }
            SECTION_WIDTH[3] = 150*sectionWidth[tireIndex];
        } else {
            SECTION_WIDTH = new double[1];
            SECTION_WIDTH[0] = sectionWidth[tireIndex];
        }
        if (rimSize.length > 2) {
            RIM_SIZE = new double[4];
            for (int i = 0; i < RIM_SIZE.length-1; i++) {
                int arrayIndex = i*2 + 2 + tireIndex;
                RIM_SIZE[i] = 1.5*rimSize[arrayIndex] - 0.5*rimSize[tireIndex];
            }
            RIM_SIZE[3] = 150*rimSize[tireIndex];
        } else {
            RIM_SIZE = new double[1];
            RIM_SIZE[0] = rimSize[tireIndex];
        }
        if (finalGear.length > 1) {
            FINAL_DRIVE = new double[4];
            for (int i = 0; i < FINAL_DRIVE.length-1; i++) {
                FINAL_DRIVE[i] = 1.5*finalGear[i+1] - 0.5*finalGear[0];
            }
            FINAL_DRIVE[3] = finalGear[0]*150;
        } else {
            FINAL_DRIVE = new double[1];
            FINAL_DRIVE[0] = finalGear[0];
        }
        if (gearRatio.length > 9) {
            GEAR_RATIO = new double[9][4];
            for (int i = 0; i < GEAR_RATIO.length; i++) {
                for (int j = 0; j < 3; j++) {
                    GEAR_RATIO[i][j] = gearRatio[(j+1)*9 + i]*1.5 - gearRatio[i]*0.5;
                }
            }
            for (int i = 0; i < GEAR_RATIO.length; i++) {
                GEAR_RATIO[i][3] = gearRatio[i]*150;
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
