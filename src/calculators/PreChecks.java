package calculators;

import cars.Overall;
import performance.*;

public class PreChecks {
    private double tValue0, tValue1, tValue2, tValue3;
    private double aValue0, aValue1, aValue2, aValue3;
    private double hValue0, hValue1, hValue2, hValue3;
    private Overall overall;

    public PreChecks(Overall overall, double tValue0, double tValue1, double tValue2, double tValue3, double aValue0, double aValue1, double aValue2, double aValue3, double hValue0, double hValue1, double hValue2, double hValue3) {
        this.overall = overall;
        this.tValue0 = tValue0;
        this.tValue1 = tValue1;
        this.tValue2 = tValue2;
        this.tValue3 = tValue3;
        this.aValue0 = aValue0;
        this.aValue1 = aValue1;
        this.aValue2 = aValue2;
        this.aValue3 = aValue3;
        this.hValue0 = hValue0;
        this.hValue1 = hValue1;
        this.hValue2 = hValue2;
        this.hValue3 = hValue3;
    }

    public boolean check(PerfPart... parts) {
        int topSpeed = 0;
        int accel = 0;
        int handling = 0;
        for (PerfPart part : parts) {
            topSpeed += part.tGain();
            accel += part.aGain();
            handling += part.hGain();
        }
        return check(topSpeed, accel, handling);
    }

    private boolean check(int topSpeed, int accel, int handling) {
        int i = calc(topSpeed, accel, handling);
        return i > overall.max();
    }

    private int calc(int topSpeed, int accel, int handling) {
        double commonDivisor = 150.0 + topSpeed + accel + handling;
        double finalTopSpeed = (topSpeed*tValue0 + accel*tValue1 + handling*tValue2 + tValue3) / commonDivisor;
        double finalAccel = (topSpeed*aValue0 + accel*aValue1 + handling*aValue2 + aValue3) / commonDivisor;
        double finalHandling = (topSpeed*hValue0 + accel*hValue1 + handling*hValue2 + hValue3) / commonDivisor;
        double finalClass = ((int) finalTopSpeed + (int) finalAccel + (int) finalHandling) / 3d;
        return (int) finalClass;
    }
}
