package calculators.checks;

import cars.Overall;
import performance.PerfPart;

public class PreChecks {
    private double tValue0, tValue1, tValue2, tValue3;
    private double aValue0, aValue1, aValue2, aValue3;
    private double hValue0, hValue1, hValue2, hValue3;
    private volatile SumCheck engineSum, turboSum, transSum, suspensionSum, brakeSum, tireSum;
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

        this.engineSum = new SumCheck(null);
        this.turboSum = new SumCheck(this.engineSum);
        this.transSum = new SumCheck(this.turboSum);
        this.suspensionSum = new SumCheck(this.transSum);
        this.brakeSum = new SumCheck(this.suspensionSum);
        this.tireSum = new SumCheck(this.brakeSum);
    }

    public boolean isValid(PerfPart part) {
        return !this.check(part);
    }

    public boolean check(PerfPart part) {
        SumCheck sumCheck = null;
        switch (part.type()) {
            case ENGINE: sumCheck = engineSum; break;
            case FORCED_INDUCTION: sumCheck = turboSum; break;
            case TRANSMISSION: sumCheck = transSum; break;
            case SUSPENSION: sumCheck = suspensionSum; break;
            case BRAKES: sumCheck = brakeSum; break;
            case TIRES: sumCheck = tireSum; break;
        }
        if (sumCheck == null) {
            return true;
        }
        if (sumCheck.prevSum() == null) {
            sumCheck.set(part.tGain(), part.aGain(), part.hGain());
        } else {
            SumCheck prevCheck = sumCheck.prevSum();
            sumCheck.set(prevCheck.t() + part.tGain(), prevCheck.a() + part.aGain(), prevCheck.h() + part.hGain());
        }
        return maxCheck(sumCheck);
    }

    public SumCheck brakeSum() {
        return this.brakeSum;
    }

    public SumCheck engineSum() {
        return engineSum;
    }
    private boolean maxCheck(SumCheck sumCheck) {
        int t = sumCheck.t();
        int a = sumCheck.a();
        int h = sumCheck.h();
        int commonDivisor = 150 + t + a + h;
        double finalT = (t*tValue0 + a*tValue1 + h*tValue2 + tValue3) / commonDivisor;
        double finalA = (t*aValue0 + a*aValue1 + h*aValue2 + aValue3) / commonDivisor;
        double finalH = (t*hValue0 + a*hValue1 + h*hValue2 + hValue3) / commonDivisor;

        return (((int)finalT + (int)finalA + (int)finalH) / 3) > this.overall.max();
    }
}

