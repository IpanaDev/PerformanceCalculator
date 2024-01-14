package cars.value;

public class StaticValue implements IValue{
    private double value0;

    public StaticValue(double value0) {
        this.value0 = value0;
    }

    @Override
    public double calculate(double t, double a, double h, double divisor) {
        return value0;
    }
}
