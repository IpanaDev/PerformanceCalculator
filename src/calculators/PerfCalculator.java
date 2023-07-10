package calculators;

import calculators.priority.Priority;
import cars.Car;
import cars.Overall;
import performance.*;

public class PerfCalculator {

    public static Result find(Car car, PerfPart[] parts) {
        System.currentTimeMillis();
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
        PerfPart engine = parts[0];
        PerfPart turbo = parts[1];
        PerfPart trans = parts[2];
        PerfPart suspension = parts[3];
        PerfPart brakes = parts[4];
        PerfPart tires = parts[5];
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
        result.set(engine, turbo, trans, suspension, brakes, tires,0, tStat, aStat, hStat, finalClassInt);
        //System.out.println("Search took (" + (System.currentTimeMillis() - ms) + "ms)");
        //System.out.println(result);
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
        PerfPart[] engineParts = valueFilter.from(Type.ENGINE);
        PerfPart[] turboParts = valueFilter.from(Type.FORCED_INDUCTION);
        PerfPart[] transParts = valueFilter.from(Type.TRANSMISSION);
        PerfPart[] suspensionParts = valueFilter.from(Type.SUSPENSION);
        PerfPart[] brakeParts = valueFilter.from(Type.BRAKES);
        PerfPart[] tireParts = valueFilter.from(Type.TIRES);
        for (PerfPart engine : engineParts) {
            if (preChecks.check(engine)) {
                continue;
            }
            for (PerfPart turbo : turboParts) {
                if (preChecks.check(engine, turbo)) {
                    continue;
                }
                for (PerfPart trans : transParts) {
                    if (preChecks.check(engine, turbo, trans)) {
                        continue;
                    }
                    for (PerfPart suspension : suspensionParts) {
                        if (preChecks.check(engine, turbo, trans, suspension)) {
                            continue;
                        }
                        for (PerfPart brakes : brakeParts) {
                            if (preChecks.check(engine, turbo, trans, suspension, brakes)) {
                                continue;
                            }
                            for (PerfPart tires : tireParts) {
                                int tGain = engine.tGain() + turbo.tGain() + trans.tGain() + suspension.tGain() + brakes.tGain() + tires.tGain();
                                int aGain = engine.aGain() + turbo.aGain() + trans.aGain() + suspension.aGain() + brakes.aGain() + tires.aGain();
                                int hGain = engine.hGain() + turbo.hGain() + trans.hGain() + suspension.hGain() + brakes.hGain() + tires.hGain();
                                double commonDivisor = 150.0 + tGain + aGain + hGain;
                                double finalTopSpeed = (tGain*tValue0 + aGain*tValue1 + hGain*tValue2 + tValue3) / commonDivisor;
                                double finalAccel = (tGain*aValue0 + aGain*aValue1 + hGain*aValue2 + aValue3) / commonDivisor;
                                double finalHandling = (tGain*hValue0 + aGain*hValue1 + hGain*hValue2 + hValue3) / commonDivisor;
                                double finalClass = ((int)finalTopSpeed + (int)finalAccel + (int)finalHandling) / 3d;
                                int finalClassInt = (int) finalClass;
                                if (finalClassInt >= overall.min() && finalClassInt <= overall.max()) {
                                    double real = priority == Priority.R ? calcRealTopSpeed(car, engine, turbo, trans, suspension, brakes, tires) : 0;
                                    priority.handle(result, real, finalTopSpeed, finalAccel, finalHandling, finalClassInt, engine, turbo, trans, suspension, brakes, tires);
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

    private static double calcRealTopSpeed(Car car, PerfPart engine, PerfPart turbo, PerfPart trans, PerfPart suspension, PerfPart brakes, PerfPart tires) {
        int tGain = engine.tGain() + turbo.tGain() + trans.tGain() + suspension.tGain() + brakes.tGain() + tires.tGain();
        int aGain = engine.aGain() + turbo.aGain() + trans.aGain() + suspension.aGain() + brakes.aGain() + tires.aGain();
        int hGain = engine.hGain() + turbo.hGain() + trans.hGain() + suspension.hGain() + brakes.hGain() + tires.hGain();
        if (tGain < 0) {
            tGain *= -(aGain+hGain)/300;
        }

        if (aGain < 0) {
            aGain *= -(tGain+hGain)/25;
        }

        if (hGain < 0) {
            hGain *= -(tGain+aGain)/100;
        }
        double commonDivisor = 150 + tGain + aGain + hGain;
        double FINAL_DRIVE = (tGain*car.cFINAL_DRIVE[0] + aGain*car.cFINAL_DRIVE[1] + hGain*car.cFINAL_DRIVE[2] + car.cFINAL_DRIVE[3]) / commonDivisor;
        double RPM = (tGain*car.cRPM[0] + aGain*car.cRPM[1] + hGain*car.cRPM[2] + car.cRPM[3]) / commonDivisor;
        double RIM_SIZE = (tGain*car.RIM_SIZE[0] + aGain*car.RIM_SIZE[1] + hGain*car.RIM_SIZE[2] + car.RIM_SIZE[3]) / commonDivisor;
        double SECTION_WIDTH = (tGain*car.SECTION_WIDTH[0] + aGain*car.SECTION_WIDTH[1] + hGain*car.SECTION_WIDTH[2] + car.SECTION_WIDTH[3]) / commonDivisor;
        double ASPECT_RATIO = (tGain*car.ASPECT_RATIO[0] + aGain*car.ASPECT_RATIO[1] + hGain*car.ASPECT_RATIO[2] + car.ASPECT_RATIO[3]) / commonDivisor;
        double TyreCircumference = Math.PI * (RIM_SIZE * 25.4 + ((SECTION_WIDTH * ASPECT_RATIO) / 100.0) * 2);
        return (((RPM / car.MAX_GEAR_RATIO / FINAL_DRIVE) * TyreCircumference) / 1000000) * 60;
    }
}