package calculators;

public class SumCheck {
    private int topSpeed, acceleration, handling;
    private int maxTGain, maxAGain, maxHGain;
    private SumCheck prevSum;

    public SumCheck(int maxTGain, int maxAGain, int maxHGain, SumCheck prevSum) {
        this.maxTGain = maxTGain;
        this.maxAGain = maxAGain;
        this.maxHGain = maxHGain;
        this.prevSum = prevSum;
    }

    public int t() {
        return topSpeed;
    }

    public int a() {
        return acceleration;
    }

    public int h() {
        return handling;
    }

    public int prevT() {
        return prevSum.t();
    }

    public int prevA() {
        return prevSum.a();
    }

    public int prevH() {
        return prevSum.h();
    }

    public void set(int t, int a, int h) {
        this.topSpeed = t;
        this.acceleration = a;
        this.handling = h;
    }

    public int maxTGain() {
        return maxTGain;
    }

    public int maxAGain() {
        return maxAGain;
    }

    public int maxHGain() {
        return maxHGain;
    }
}
