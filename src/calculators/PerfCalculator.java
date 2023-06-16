package calculators;

import calculators.priority.Priority;
import cars.Car;
import cars.Overall;
import performance.*;

public class PerfCalculator {

    public static Result find(Car car, Object[] parts) {
        System.currentTimeMillis();
        long ms = System.currentTimeMillis();
        double tValue0 = car.cTopSpeed()[0];
        double tValue1 = car.cTopSpeed()[1];
        double tValue2 = car.cTopSpeed()[2];
        double tValue3 = car.cTopSpeed()[3];

        double aValue0 = car.cAccel()[0];
        double aValue1 = car.cAccel()[1];
        double aValue2 = car.cAccel()[2];
        double aValue3 = car.cAccel()[3];

        double hValue0 = car.cHandling()[0];
        double hValue1 = car.cHandling()[1];
        double hValue2 = car.cHandling()[2];
        double hValue3 = car.cHandling()[3];
        EngineParts engine = (EngineParts) parts[0];
        TurboParts turbo = (TurboParts) parts[1];
        TransmissionParts trans = (TransmissionParts) parts[2];
        SuspensionParts suspension = (SuspensionParts) parts[3];
        BrakeParts brakes = (BrakeParts) parts[4];
        TireParts tires = (TireParts) parts[5];
        int tGain = engine.tGain() + turbo.tGain() + trans.tGain() + suspension.tGain() + brakes.tGain() + tires.tGain();
        int aGain = engine.aGain() + turbo.aGain() + trans.aGain() + suspension.aGain() + brakes.aGain() + tires.aGain();
        int hGain = engine.hGain() + turbo.hGain() + trans.hGain() + suspension.hGain() + brakes.hGain() + tires.hGain();
        double commonDivisor = 150.0 + tGain + aGain + hGain;
        double tStat = (tGain * tValue0 + aGain * tValue1 + hGain * tValue2 + tValue3) / commonDivisor;
        double aStat = (tGain * aValue0 + aGain * aValue1 + hGain * aValue2 + aValue3) / commonDivisor;
        double hStat = (tGain * hValue0 + aGain * hValue1 + hGain * hValue2 + hValue3) / commonDivisor;
        double finalClass = ((int) tStat + (int) aStat + (int) hStat) / 3d;
        int finalClassInt = (int) finalClass;
        final Result result = new Result();
        result.set(engine, turbo, trans, suspension, brakes, tires, tStat, aStat, hStat, finalClassInt);
        System.out.println("Search took (" + (System.currentTimeMillis() - ms) + "ms)");
        System.out.println(result);
        return result;
    }
    public static Result findFast(Car car, Overall overall, ValueFilter valueFilter, Priority priority) throws IllegalAccessException, NoSuchFieldException {
        System.currentTimeMillis();
        long ms = System.currentTimeMillis();
        double tValue0 = car.cTopSpeed()[0];
        double tValue1 = car.cTopSpeed()[1];
        double tValue2 = car.cTopSpeed()[2];
        double tValue3 = car.cTopSpeed()[3];

        double aValue0 = car.cAccel()[0];
        double aValue1 = car.cAccel()[1];
        double aValue2 = car.cAccel()[2];
        double aValue3 = car.cAccel()[3];

        double hValue0 = car.cHandling()[0];
        double hValue1 = car.cHandling()[1];
        double hValue2 = car.cHandling()[2];
        double hValue3 = car.cHandling()[3];
        PreChecks preChecks = new PreChecks(overall, tValue0, tValue1, tValue2, tValue3, aValue0, aValue1, aValue2, aValue3, hValue0, hValue1, hValue2, hValue3);
        final Result result = new Result();
        EngineParts[] engineParts = valueFilter.from(EngineParts.class);
        TurboParts[] turboParts = valueFilter.from(TurboParts.class);
        TransmissionParts[] transParts = valueFilter.from(TransmissionParts.class);
        SuspensionParts[] suspensionParts = valueFilter.from(SuspensionParts.class);
        BrakeParts[] brakeParts = valueFilter.from(BrakeParts.class);
        TireParts[] tireParts = valueFilter.from(TireParts.class);
        for (EngineParts engine : engineParts) {
            if (preChecks.check(engine)) {
                continue;
            }
            for (TurboParts turbo : turboParts) {
                if (preChecks.check(turbo)) {
                    continue;
                }
                for (TransmissionParts trans : transParts) {
                    if (preChecks.check(trans)) {
                        continue;
                    }
                    for (SuspensionParts suspension : suspensionParts) {
                        if (preChecks.check(suspension)) {
                            continue;
                        }
                        for (BrakeParts brakes : brakeParts) {
                            if (preChecks.check(brakes)) {
                                continue;
                            }
                            for (TireParts tires : tireParts) {
                                int tGain = preChecks.sumBrake.topSpeed + tires.tGain();
                                int aGain = preChecks.sumBrake.accel + tires.aGain();
                                int hGain = preChecks.sumBrake.handling + tires.hGain();
                                double commonDivisor = 150.0 + tGain + aGain + hGain;
                                double finalTopSpeed = (tGain*tValue0 + aGain*tValue1 + hGain*tValue2 + tValue3) / commonDivisor;
                                double finalAccel = (tGain*aValue0 + aGain*aValue1 + hGain*aValue2 + aValue3) / commonDivisor;
                                double finalHandling = (tGain*hValue0 + aGain*hValue1 + hGain*hValue2 + hValue3) / commonDivisor;
                                /*
                                Getting the value as (finalTopSpeed + finalAccel + finalHandling) will not work.
                                Why?
                                Actual best values for B Class T-A-H Green MR2
                                654.9262589928057, 660.1978417266187, 485.69604316546764, 1800.820143884892, 600.273381294964
                                With this calculation
                                655.3093525179856, 660.4892086330935, 485.021582733813, 1800.820143884892, 600.273381294964
                                First: (654 + 660 + 485) = 1799 / 3 = 599,6666666666667
                                Second: 1800.820143884892 / 3 = 600.273381294964
                                ***Both sum value are 1800.820143884892***
                                 */
                                double finalClass = ((int)finalTopSpeed + (int)finalAccel + (int)finalHandling) / 3d;
                                int finalClassInt = (int) finalClass;
                                if (finalClassInt >= overall.min() && finalClassInt <= overall.max()) {
                                    priority.handle(result, finalTopSpeed, finalAccel, finalHandling, finalClassInt, engine, turbo, trans, suspension, brakes, tires);
                                }
                            }
                        }
                    }
                }
            }
        }
        result.setTime(System.currentTimeMillis() - ms);
        return result;
    }
}