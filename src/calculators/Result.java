package calculators;

import performance.*;

public class Result {
    private EngineParts engine = EngineParts.EMPTY;
    private TurboParts turbo = TurboParts.EMPTY;
    private TransmissionParts trans = TransmissionParts.EMPTY;
    private SuspensionParts suspension = SuspensionParts.EMPTY;
    private BrakeParts brakes = BrakeParts.EMPTY;
    private TireParts tires = TireParts.EMPTY;
    private double topSpeed, acceleration, handling;
    private int rating;
    private long time;

    public void set(EngineParts engine, TurboParts turbo, TransmissionParts trans, SuspensionParts suspension, BrakeParts brakes, TireParts tires, double topSpeed, double acceleration, double handling, int rating) {
        this.engine = engine;
        this.turbo = turbo;
        this.trans = trans;
        this.suspension = suspension;
        this.brakes = brakes;
        this.tires = tires;
        this.topSpeed = topSpeed;
        this.acceleration = acceleration;
        this.handling = handling;
        this.rating = rating;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("calculators.Result{");
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

    public EngineParts engine() {
        return engine;
    }

    public TurboParts turbo() {
        return turbo;
    }

    public TransmissionParts trans() {
        return trans;
    }

    public SuspensionParts suspension() {
        return suspension;
    }

    public BrakeParts brakes() {
        return brakes;
    }

    public TireParts tires() {
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

    public int rating() {
        return rating;
    }
}
