package calculators;

import cars.Overall;
import performance.*;

public class PreChecks {
    private double tValue0, tValue1, tValue2, tValue3;
    private double aValue0, aValue1, aValue2, aValue3;
    private double hValue0, hValue1, hValue2, hValue3;
    private SumCheck[] sumChecks = new SumCheck[5];
    private Overall overall;

    public PreChecks(ValueFilter valueFilter, Overall overall, double tValue0, double tValue1, double tValue2, double tValue3, double aValue0, double aValue1, double aValue2, double aValue3, double hValue0, double hValue1, double hValue2, double hValue3) throws NoSuchFieldException, IllegalAccessException {
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

        for (int i = 0; i < sumChecks.length; i++) {
            int maxT = 0;
            int maxA = 0;
            int maxH = 0;
            for (int j = i+1; j < Type.VALUES.length; j++) {
                PerfPart FiveElite = valueFilter.from(Type.VALUES[j])[4];
                maxT += FiveElite.tGain();
                maxA += FiveElite.aGain();
                maxH += FiveElite.hGain();
            }
            this.sumChecks[i] = new SumCheck(maxT, maxA, maxH, i == 0 ? null : this.sumChecks[i-1]);
        }
    }

    public boolean check(PerfPart part) {
        SumCheck sumCheck = this.sumChecks[part.type().ordinal()];
        if (part.type() == Type.ENGINE) {
            sumCheck.set(part.tGain(), part.aGain(), part.hGain());
        } else {
            sumCheck.set(sumCheck.prevT() + part.tGain(), sumCheck.prevA() + part.aGain(), sumCheck.prevH() + part.hGain());
        }
        return maxCheck(sumCheck) || minCheck(sumCheck);
    }

    public SumCheck brakeSum() {
        return this.sumChecks[Type.BRAKES.ordinal()];
    }

    private boolean maxCheck(SumCheck sumCheck) {
        int commonDivisor = 150 + sumCheck.t() + sumCheck.a() + sumCheck.h();
        double finalT = (sumCheck.t()*tValue0 + sumCheck.a()*tValue1 + sumCheck.h()*tValue2 + tValue3) / commonDivisor;
        double finalA = (sumCheck.t()*aValue0 + sumCheck.a()*aValue1 + sumCheck.h()*aValue2 + aValue3) / commonDivisor;
        double finalH = (sumCheck.t()*hValue0 + sumCheck.a()*hValue1 + sumCheck.h()*hValue2 + hValue3) / commonDivisor;

        return (((int)finalT + (int)finalA + (int)finalH) / 3) > this.overall.max();
    }

    private boolean minCheck(SumCheck sumCheck) {
        int t = sumCheck.t() + sumCheck.maxTGain();
        int a = sumCheck.a() + sumCheck.maxAGain();
        int h = sumCheck.h() + sumCheck.maxHGain();
        int commonDivisor = 150 + t + a + h;
        double finalT = (t*tValue0 + a*tValue1 + h*tValue2 + tValue3) / commonDivisor;
        double finalA = (t*aValue0 + a*aValue1 + h*aValue2 + aValue3) / commonDivisor;
        double finalH = (t*hValue0 + a*hValue1 + h*hValue2 + hValue3) / commonDivisor;

        return (((int)finalT + (int)finalA + (int)finalH) / 3) < this.overall.min();
    }
}
