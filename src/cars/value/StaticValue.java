package cars.value;

public class StaticValue implements IValue{
    private float value0;

    public StaticValue(float value0) {
        this.value0 = value0;
    }

    @Override
    public float calculate(float h, float a, float t, float divisor) {
        return value0;
    }
}
