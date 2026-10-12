package me.ipana.calculator.checks;

import me.ipana.calculator.PerfCalculator;
import me.ipana.calculator.SumCache;
import me.ipana.parts.PerfPart;

public class PreChecks {
    private final SumCheck engineSum, turboSum, transSum, suspensionSum, brakeSum;
    private final PerfCalculator calculator;

    public PreChecks(PerfCalculator calculator) {
        this.calculator = calculator;

        this.engineSum = new SumCheck(null);
        this.turboSum = new SumCheck(this.engineSum);
        this.transSum = new SumCheck(this.turboSum);
        this.suspensionSum = new SumCheck(this.transSum);
        this.brakeSum = new SumCheck(this.suspensionSum);
    }

    public boolean check(PerfPart part, SumCheck sumCheck) {
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

    public SumCheck engineSum() {
        return engineSum;
    }
    public SumCheck turboSum() {
        return turboSum;
    }
    public SumCheck transSum() {
        return transSum;
    }
    public SumCheck suspensionSum() {
        return suspensionSum;
    }
    public SumCheck brakeSum() {
        return brakeSum;
    }
    private boolean maxCheck(SumCheck sumCheck) {
        int t = sumCheck.t();
        int a = sumCheck.a();
        int h = sumCheck.h();
        float commonDivisor = SumCache.INV_1[t + a + h];
        int finalT = (int) ((t*calculator.tValue0 + a*calculator.tValue1 + h*calculator.tValue2 + calculator.tValue3) * commonDivisor);
        int finalA = (int) ((t*calculator.aValue0 + a*calculator.aValue1 + h*calculator.aValue2 + calculator.aValue3) * commonDivisor);
        int finalH = (int) ((t*calculator.hValue0 + a*calculator.hValue1 + h*calculator.hValue2 + calculator.hValue3) * commonDivisor);

        return ((finalT + finalA + finalH) / 3) > calculator.overall.max();
    }
}

