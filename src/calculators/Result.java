package calculators;

import performance.*;

import javax.swing.text.NumberFormatter;
import java.text.ParseException;

public class Result {
    private PerfPart engine = PerfPart.EMPTY_ENGINE;
    private PerfPart turbo = PerfPart.EMPTY_TURBO;
    private PerfPart trans = PerfPart.EMPTY_TRANSMISSION;
    private PerfPart suspension = PerfPart.EMPTY_SUSPENSION;
    private PerfPart brakes = PerfPart.EMPTY_BRAKES;
    private PerfPart tires = PerfPart.EMPTY_TIRES;
    private double topSpeed, acceleration, handling, realTopSpeed;
    private int rating;
    private long time;

    public void set(PerfPart engine, PerfPart turbo, PerfPart trans, PerfPart suspension, PerfPart brakes, PerfPart tires, double realTopSpeed, double topSpeed, double acceleration, double handling, int rating) {
        this.engine = engine;
        this.turbo = turbo;
        this.trans = trans;
        this.suspension = suspension;
        this.brakes = brakes;
        this.tires = tires;
        this.realTopSpeed = realTopSpeed;
        this.topSpeed = topSpeed;
        this.acceleration = acceleration;
        this.handling = handling;
        this.rating = rating;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("calculators.Result{");
        sb.append("engine=").append(engine);
        sb.append(", turbo=").append(turbo);
        sb.append(", trans=").append(trans);
        sb.append(", suspension=").append(suspension);
        sb.append(", brakes=").append(brakes);
        sb.append(", tires=").append(tires);
        sb.append(", topSpeed=").append(topSpeed);
        sb.append(", acceleration=").append(acceleration);
        sb.append(", handling=").append(handling);
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

    public double topSpeed() {
        return topSpeed;
    }

    public double acceleration() {
        return acceleration;
    }

    public double handling() {
        return handling;
    }

    public double realTopSpeed() {
        return realTopSpeed;
    }

    public int rating() {
        return rating;
    }

    public int cost() {
        return engine.price() + turbo.price() + trans.price() + suspension.price() + brakes.price() + tires.price();
    }
    public String costString() throws ParseException {
        return new NumberFormatter().valueToString(cost()) + " IGC";
    }
}
