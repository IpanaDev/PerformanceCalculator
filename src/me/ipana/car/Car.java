package me.ipana.car;

import me.ipana.car.value.AxlePairValue;
import me.ipana.car.value.FloatArrayValue;
import me.ipana.car.value.FloatValue;
import me.ipana.config.ConfigFile;
import vaultlib.core.types.EAReflection.Float;

import java.util.List;

public class Car {
    private List<Float> tStats, aStats, hStats;
    public FloatValue mass, coefficient, rpm, finalGear, torqueSplit;
    public FloatArrayValue torque, gearRatio, gearEfficiency;
    public AxlePairValue[] aspectRatio, rimSize, sectionWidth;
    private float[] cTStats, cAStats, cHStats;
    private String vltName, fullName;
    private int nosLevel = -1;
    public int MAX_GEAR_INDEX;
    public boolean hasPVehicle;

    public Car(String vltName, String fullName) {
        this.vltName = vltName;
        this.fullName = fullName;
        this.mass = new FloatValue();
        this.coefficient = new FloatValue();
        this.rpm = new FloatValue();
        this.finalGear = new FloatValue();
        this.torqueSplit = new FloatValue();
        this.torque = new FloatArrayValue(9);
        this.gearRatio = new FloatArrayValue(9);
        this.gearEfficiency = new FloatArrayValue(9);
        this.aspectRatio = new AxlePairValue[4];
        this.rimSize = new AxlePairValue[4];
        this.sectionWidth = new AxlePairValue[4];
    }

    public void setupPreValues() {
        cTStats = new float[tStats.size()];
        cAStats = new float[aStats.size()];
        cHStats = new float[hStats.size()];
        for (int i = 0; i < tStats.size(); i++) {
            cTStats[i] = tStats.get(tStats.size()-1-i).GetValue()*1.5f-tStats.get(0).GetValue()*0.5f;
        }
        for (int i = 0; i < aStats.size(); i++) {
            cAStats[i] = aStats.get(aStats.size()-1-i).GetValue()*1.5f-aStats.get(0).GetValue()*0.5f;
        }
        for (int i = 0; i < hStats.size(); i++) {
            cHStats[i] = hStats.get(hStats.size()-1-i).GetValue()*1.5f-hStats.get(0).GetValue()*0.5f;
        }
        cTStats[cTStats.length-1] = tStats.get(0).GetValue()*150;
        cAStats[cAStats.length-1] = aStats.get(0).GetValue()*150;
        cHStats[cHStats.length-1] = hStats.get(0).GetValue()*150;
        for (AxlePairValue axlePair : rimSize) {
            if (axlePair != null) {
                axlePair.Front *= 25.4f;
                axlePair.Rear *= 25.4f;
            }
        }
        for (int i = 0; i < torque.array.length; i++) {
            torque.array[i] *= 1.3558f; //Converting to Nm
        }
        int i = 8;
        while (gearRatio.array[i] == 0) {
            i--;
        }
        MAX_GEAR_INDEX = i;
    }

    public String fullName() {
        return fullName;
    }

    @Override
    public String toString() {
        if (Boolean.parseBoolean(String.valueOf(ConfigFile.DEV_MODE.value()))) {
            return fullName + " ["+vltName+"]";
        } else {
            return fullName;
        }
    }
    public void nosLevel(int level) {
        nosLevel = level;
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

    public String vltName() {
        return vltName;
    }

    public void setTStats(List<Float> tStats) {
        this.tStats = tStats;
    }
    public void setAStats(List<Float> aStats) {
        this.aStats = aStats;
    }
    public void setHStats(List<Float> hStats) {
        this.hStats = hStats;
    }
    public float[] tStats() {
        return cTStats;
    }

    public float[] aStats() {
        return cAStats;
    }

    public float[] hStats() {
        return cHStats;
    }
}
