package decompiler.vlt;

import utils.Equality;

public class CarVLT {
    public String vltName, fullName;
    public VltNode[] engineVLT, tiresVLT, transmissionVLT;
    public int[][] STATS;
    public int[] RPM;
    public int[][] sectionWidth, rimSize, aspectRatio;
    public double[][] gearRatio;
    public double[] torqueSplit;
    public double[] finalGear;

    public CarVLT(String vltName, String fullName) {
        this.vltName = vltName;
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("vltName=").append(vltName);
        sb.append(",fullName=").append(fullName);
        sb.append(",tStats=");
        for (int t : STATS[0]) {
            sb.append(t).append("|");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(",aStats=");
        for (int t : STATS[1]) {
            sb.append(t).append("|");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(",hStats=");
        for (int t : STATS[2]) {
            sb.append(t).append("|");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(",RPM=");
        if (Equality.isIntEqual(RPM)) {
            sb.append(RPM[0]);
        } else {
            for (int r : RPM) {
                sb.append(r).append("|");
            }
            sb.deleteCharAt(sb.length()-1);
        }

        sb.append(",sectionWidth=");
        if (Equality.isIntEqualAtIndex(sectionWidth)) {
            for (int n : sectionWidth[0]) {
                sb.append(n).append("|");
            }
            sb.deleteCharAt(sb.length()-1);
        } else {
            for (int[] ints : sectionWidth) {
                for (int n : ints) {
                    sb.append(n).append("|");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(",rimSize=");
        if (Equality.isIntEqualAtIndex(rimSize)) {
            for (int n : rimSize[0]) {
                sb.append(n).append("|");
            }
            sb.deleteCharAt(sb.length()-1);
        } else {
            for (int[] ints : rimSize) {
                for (int n : ints) {
                    sb.append(n).append("|");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(",aspectRatio=");
        if (Equality.isIntEqualAtIndex(aspectRatio)) {
            for (int n : aspectRatio[0]) {
                sb.append(n).append("|");
            }
            sb.deleteCharAt(sb.length()-1);
        } else {
            for (int[] ints : aspectRatio) {
                for (int n : ints) {
                    sb.append(n).append("|");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(",gearRatio=");
        if (Equality.isDoubleEqualAtIndex(gearRatio)) {
            for (double n : gearRatio[0]) {
                sb.append(n).append("|");
            }
            sb.deleteCharAt(sb.length()-1);
        } else {
            for (double[] doubles : gearRatio) {
                for (double n : doubles) {
                    sb.append(n).append("|");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(",torqueSplit=");
        if (Equality.isDoubleEqual(torqueSplit)) {
            sb.append(torqueSplit[0]);
        } else {
            for (double n : torqueSplit) {
                sb.append(n).append("|");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append(",finalGear=");
        if (Equality.isDoubleEqual(finalGear)) {
            sb.append(finalGear[0]);
        } else {
            for (double n : finalGear) {
                sb.append(n).append("|");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append("\n");
        return sb.toString();
    }
}
