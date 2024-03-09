package calculators.result;

import performance.*;

import javax.swing.text.NumberFormatter;
import java.text.ParseException;

public class Result {
    private volatile PerfPart engine = PerfPart.EMPTY_ENGINE;
    private volatile PerfPart turbo = PerfPart.EMPTY_TURBO;
    private volatile PerfPart trans = PerfPart.EMPTY_TRANSMISSION;
    private volatile PerfPart suspension = PerfPart.EMPTY_SUSPENSION;
    private volatile PerfPart brakes = PerfPart.EMPTY_BRAKES;
    private volatile PerfPart tires = PerfPart.EMPTY_TIRES;
    private volatile float tStat, aStat, hStat, speedKMH, acceleration;
    private volatile int tGain, aGain, hGain;
    private volatile int rating;
    private volatile long time;

    public void set(PerfPart engine, PerfPart turbo, PerfPart trans, PerfPart suspension, PerfPart brakes, PerfPart tires, int tGain, int aGain, int hGain, float additionalValue, float topSpeed, float acceleration, float handling, int rating) {
        this.engine = engine;
        this.turbo = turbo;
        this.trans = trans;
        this.suspension = suspension;
        this.brakes = brakes;
        this.tires = tires;
        this.tGain = tGain;
        this.aGain = aGain;
        this.hGain = hGain;
        this.acceleration = additionalValue;
        this.speedKMH = additionalValue;
        this.tStat = topSpeed;
        this.aStat = acceleration;
        this.hStat = handling;
        this.rating = rating;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("calculators.result.DefaultResult{");
        sb.append("engine=").append(engine);
        sb.append(", turbo=").append(turbo);
        sb.append(", trans=").append(trans);
        sb.append(", suspension=").append(suspension);
        sb.append(", brakes=").append(brakes);
        sb.append(", tires=").append(tires);
        sb.append(", topSpeed=").append(tStat);
        sb.append(", acceleration=").append(aStat);
        sb.append(", handling=").append(hStat);
        sb.append(", rating=").append(rating);
        sb.append('}');
        return sb.toString();
    }

    public void setTime(long _time) {
        time = _time;
    }

    public long time() {
        return time;
    }

    public PerfPart engine() {
        return engine;
    }

    public PerfPart turbo() {
        return turbo;
    }

    public PerfPart trans() {
        return trans;
    }

    public PerfPart suspension() {
        return suspension;
    }

    public PerfPart brakes() {
        return brakes;
    }

    public PerfPart tires() {
        return tires;
    }

    public float tStat() {
        return tStat;
    }

    public float aStat() {
        return aStat;
    }

    public float hStat() {
        return hStat;
    }

    public float acceleration() {
        return acceleration;
    }

    public float realTopSpeed() {
        return speedKMH;
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

    public int rating() {
        return rating;
    }

    public int cost() {
        return engine.price() + turbo.price() + trans.price() + suspension.price() + brakes.price() + tires.price();
    }
    public static int cost(PerfPart engine, PerfPart turbo, PerfPart trans, PerfPart suspension, PerfPart brakes, PerfPart tires) {
        return engine.price() + turbo.price() + trans.price() + suspension.price() + brakes.price() + tires.price();
    }
    public String costString() throws ParseException {
        return new NumberFormatter().valueToString(cost()) + " IGC";
    }
}
