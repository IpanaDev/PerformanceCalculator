package calculators.checks;

public class SumCheck {
    private int topSpeed, acceleration, handling;
    private SumCheck prevSum;

    public SumCheck(SumCheck prevSum) {
        this.prevSum = prevSum;
    }

    public SumCheck prevSum() {
        return prevSum;
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

    public void set(int t, int a, int h) {
        this.topSpeed = t;
        this.acceleration = a;
        this.handling = h;
    }
}
