package cars.value;

public class DynamicValue implements IValue {
    public DynamicValue(double[] values) {
        this.value0 = values[0];
        this.value1 = values[1];
        this.value2 = values[2];
        this.value3 = values[3];
    }

    private double value0, value1, value2, value3;

    @Override
    public double calculate(double t, double a, double h, double divisor) {
        return (value0*t + value1*a + value2*h + value3) / divisor;
    }
}
