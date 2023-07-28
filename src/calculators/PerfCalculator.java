package calculators;

import calculators.priority.Priority;
import cars.Car;
import cars.Overall;
import performance.*;

public class PerfCalculator {

    public static Result find(Car car, PerfPart[] parts) {
        System.currentTimeMillis();
        double tValue0 = car.tStats()[0];
        double tValue1 = car.tStats()[1];
        double tValue2 = car.tStats()[2];
        double tValue3 = car.tStats()[3];

        double aValue0 = car.aStats()[0];
        double aValue1 = car.aStats()[1];
        double aValue2 = car.aStats()[2];
        double aValue3 = car.aStats()[3];

        double hValue0 = car.hStats()[0];
        double hValue1 = car.hStats()[1];
        double hValue2 = car.hStats()[2];
        double hValue3 = car.hStats()[3];
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
        double tValue0 = car.tStats()[0];
        double tValue1 = car.tStats()[1];
        double tValue2 = car.tStats()[2];
        double tValue3 = car.tStats()[3];

        double aValue0 = car.aStats()[0];
        double aValue1 = car.aStats()[1];
        double aValue2 = car.aStats()[2];
        double aValue3 = car.aStats()[3];

        double hValue0 = car.hStats()[0];
        double hValue1 = car.hStats()[1];
        double hValue2 = car.hStats()[2];
        double hValue3 = car.hStats()[3];
        PreChecks preChecks = new PreChecks(valueFilter, overall, tValue0, tValue1, tValue2, tValue3, aValue0, aValue1, aValue2, aValue3, hValue0, hValue1, hValue2, hValue3);
        SumCheck brakeSum = preChecks.brakeSum();
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
                if (preChecks.check(turbo)) {
                    continue;
                }
                for (PerfPart trans : transParts) {
                    if (preChecks.check(trans)) {
                        continue;
                    }
                    for (PerfPart suspension : suspensionParts) {
                        if (preChecks.check(suspension)) {
                            continue;
                        }
                        for (PerfPart brakes : brakeParts) {
                            if (preChecks.check(brakes)) {
                                continue;
                            }
                            for (PerfPart tires : tireParts) {
                                int tGain = brakeSum.t() + tires.tGain();
                                int aGain = brakeSum.a() + tires.aGain();
                                int hGain = brakeSum.h() + tires.hGain();
                                double commonDivisor = 150.0 + tGain + aGain + hGain;
                                double finalTopSpeed = (tGain*tValue0 + aGain*tValue1 + hGain*tValue2 + tValue3) / commonDivisor;
                                double finalAccel = (tGain*aValue0 + aGain*aValue1 + hGain*aValue2 + aValue3) / commonDivisor;
                                double finalHandling = (tGain*hValue0 + aGain*hValue1 + hGain*hValue2 + hValue3) / commonDivisor;
                                double finalClass = ((int)finalTopSpeed + (int)finalAccel + (int)finalHandling) / 3d;
                                int finalClassInt = (int) finalClass;
                                if (finalClassInt >= overall.min() && finalClassInt <= overall.max()) {
                                    double real = priority == Priority.R ? calcRealTopSpeed(car, tGain, aGain, hGain) : 0;
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

    private static double calcRealTopSpeed(Car car, int t, int a, int h) {
        int tGain = t;
        int aGain = a;
        int hGain = h;
        if (tGain < 0) {
            tGain *= -(aGain+hGain)/300;
        }

        if (aGain < 0) {
            aGain *= -(tGain+hGain)/25;
        }

        if (hGain < 0) {
            hGain *= -(tGain+aGain)/100;
        }
        int commonDivisor = 150 + tGain + aGain + hGain;
        double FINAL_DRIVE = partCalc(tGain, aGain, hGain, commonDivisor, car.FINAL_DRIVE);
        double RPM = partCalc(tGain, aGain, hGain, commonDivisor, car.cRPM);
        double RIM_SIZE = partCalc(tGain, aGain, hGain, commonDivisor, car.RIM_SIZE);
        double SECTION_WIDTH = partCalc(tGain, aGain, hGain, commonDivisor, car.SECTION_WIDTH);
        double ASPECT_RATIO = partCalc(tGain, aGain, hGain, commonDivisor, car.ASPECT_RATIO);
        double GEAR_RATIO = car.gearRatio().length == 9 ? car.gearRatio()[car.MAX_GEAR_INDEX] : partCalc(tGain, aGain, hGain, commonDivisor, car.GEAR_RATIO[car.MAX_GEAR_INDEX]);
        double TyreCircumference = Math.PI * (RIM_SIZE * 25.4 + ((SECTION_WIDTH * ASPECT_RATIO) / 50));
        return ((RPM / GEAR_RATIO / FINAL_DRIVE) * TyreCircumference) * 0.00006;
    }
    private static double partCalc(int tGain, int aGain, int hGain, int commonDivisor, double[] array) {
        return array.length == 1 ? array[0] : (tGain*array[0] + aGain*array[1] + hGain*array[2] + array[3]) / commonDivisor;
    }
}