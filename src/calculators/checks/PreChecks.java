package calculators.checks;

import calculators.PerfCalculator;
import performance.PerfPart;

public class PreChecks {
    public SumCheck engineSum, turboSum, transSum, suspensionSum, brakeSum, tireSum;
    public PerfCalculator calculator;

    public PreChecks(PerfCalculator calculator) {
        this.calculator = calculator;

        this.engineSum = new SumCheck(null);
        this.turboSum = new SumCheck(this.engineSum);
        this.transSum = new SumCheck(this.turboSum);
        this.suspensionSum = new SumCheck(this.transSum);
        this.brakeSum = new SumCheck(this.suspensionSum);
        this.tireSum = new SumCheck(this.brakeSum);
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
        float finalT = (t*calculator.tValue0 + a*calculator.tValue1 + h*calculator.tValue2 + calculator.tValue3) / commonDivisor;
        float finalA = (t*calculator.aValue0 + a*calculator.aValue1 + h*calculator.aValue2 + calculator.aValue3) / commonDivisor;
        float finalH = (t*calculator.hValue0 + a*calculator.hValue1 + h*calculator.hValue2 + calculator.hValue3) / commonDivisor;

        return (((int)finalT + (int)finalA + (int)finalH) / 3) > calculator.overall.max();
    }
}

