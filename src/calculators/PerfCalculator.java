package calculators;

import calculators.checks.PreChecks;
import calculators.checks.SumCheck;
import calculators.priority.Priority;
import calculators.result.Result;
import cars.Car;
import cars.Overall;
import performance.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PerfCalculator {
    private static final int CORE_COUNT = Runtime.getRuntime().availableProcessors();

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
        result.set(engine, turbo, trans, suspension, brakes, tires, tGain, aGain, hGain, 0, tStat, aStat, hStat, finalClassInt);
        //System.out.println("Search took (" + (System.currentTimeMillis() - ms) + "ms)");
        //System.out.println(result);
        return result;
    }
    public static Result findSingleThread(Car car, Overall overall, ValueFilter valueFilter, Priority priority) throws IllegalAccessException, NoSuchFieldException {
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
        PreChecks preChecks = new PreChecks(overall, tValue0, tValue1, tValue2, tValue3, aValue0, aValue1, aValue2, aValue3, hValue0, hValue1, hValue2, hValue3);
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
                                int divisor = 150 + tGain + aGain + hGain;
                                double tStat = (tGain * tValue0 + aGain * tValue1 + hGain * tValue2 + tValue3) / divisor;
                                double aStat = (tGain * aValue0 + aGain * aValue1 + hGain * aValue2 + aValue3) / divisor;
                                double hStat = (tGain * hValue0 + aGain * hValue1 + hGain * hValue2 + hValue3) / divisor;
                                int overallStat = ((int) tStat + (int) aStat + (int) hStat) / 3;
                                if (overallStat >= overall.min() && overallStat <= overall.max()) {
                                    double real = priority == Priority.TOP_SPEED_KMH ? calcRealTopSpeed(car, tGain, aGain, hGain) : 0;
                                    priority.handle(result, tGain, aGain, hGain, real, tStat, aStat, hStat, overallStat, engine, turbo, trans, suspension, brakes, tires);
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
    public static Result findMultiThread(Car car, Overall overall, ValueFilter valueFilter, Priority priority) throws IllegalAccessException, NoSuchFieldException {
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
        final Result result = new Result();
        PerfPart[] engineParts = valueFilter.from(Type.ENGINE);
        PerfPart[] turboParts = valueFilter.from(Type.FORCED_INDUCTION);
        PerfPart[] transParts = valueFilter.from(Type.TRANSMISSION);
        PerfPart[] suspensionParts = valueFilter.from(Type.SUSPENSION);
        PerfPart[] brakeParts = valueFilter.from(Type.BRAKES);
        PerfPart[] tireParts = valueFilter.from(Type.TIRES);
        ExecutorService THREAD = Executors.newFixedThreadPool(CORE_COUNT);
        PreChecks preChecks = new PreChecks(overall, tValue0, tValue1, tValue2, tValue3, aValue0, aValue1, aValue2, aValue3, hValue0, hValue1, hValue2, hValue3);
        ArrayList<Future<?>> tasks = new ArrayList<>(22);
        for (PerfPart engine : engineParts) {
            if (preChecks.check(engine)) {
                continue;
            }
            PreChecks subChecks = new PreChecks(overall, tValue0, tValue1, tValue2, tValue3, aValue0, aValue1, aValue2, aValue3, hValue0, hValue1, hValue2, hValue3);
            SumCheck brakeSum = subChecks.brakeSum();
            tasks.add(THREAD.submit(() -> {
                subChecks.engineSum().set(engine.tGain(), engine.aGain(), engine.hGain());
                for (PerfPart turbo : turboParts) {
                    if (subChecks.check(turbo)) {
                        continue;
                    }
                    for (PerfPart trans : transParts) {
                        if (subChecks.check(trans)) {
                            continue;
                        }
                        for (PerfPart suspension : suspensionParts) {
                            if (subChecks.check(suspension)) {
                                continue;
                            }
                            for (PerfPart brakes : brakeParts) {
                                if (subChecks.check(brakes)) {
                                    continue;
                                }
                                for (PerfPart tires : tireParts) {
                                    int tGain = brakeSum.t() + tires.tGain();
                                    int aGain = brakeSum.a() + tires.aGain();
                                    int hGain = brakeSum.h() + tires.hGain();
                                    int divisor = 150 + tGain + aGain + hGain;
                                    double tStat = (tGain * tValue0 + aGain * tValue1 + hGain * tValue2 + tValue3) / divisor;
                                    double aStat = (tGain * aValue0 + aGain * aValue1 + hGain * aValue2 + aValue3) / divisor;
                                    double hStat = (tGain * hValue0 + aGain * hValue1 + hGain * hValue2 + hValue3) / divisor;
                                    int overallStat = ((int) tStat + (int) aStat + (int) hStat) / 3;
                                    if (overallStat >= overall.min() && overallStat <= overall.max()) {
                                        double real = priority == Priority.TOP_SPEED_KMH ? calcRealTopSpeed(car, tGain, aGain, hGain) : 0;
                                        priority.handle(result, tGain, aGain, hGain, real, tStat, aStat, hStat, overallStat, engine, turbo, trans, suspension, brakes, tires);
                                    }
                                }
                            }
                        }
                    }
                }
            }));
        }
        for (Future<?> future : tasks) {
            while (!future.isDone()) {
                //additional thread sleep can be added
            }
        }
        tasks.clear();
        THREAD.shutdown();
        result.setTime(System.currentTimeMillis() - ms);
        return result;
    }
    private static double calcRealTopSpeed(Car car, int tGain, int aGain, int hGain) {
        double t = tGain;
        double a = aGain;
        double h = hGain;
        if (tGain < 0) {
            double m = 150.0 / (150 + tGain);
            a *= m;
            h *= m;
            t = 0;
        }
        if (aGain < 0) {
            double m = 150.0 / (150 + aGain);
            t *= m;
            h *= m;
            a = 0;
        }
        if (hGain < 0) {
            double m = 150.0 / (150 + hGain);
            t *= m;
            a *= m;
            h = 0;
        }
        double divisor = 150 + t + a + h;
        double FINAL_DRIVE = car.FINAL_DRIVE.calculate(t, a, h, divisor);
        double RPM = car.cRPM.calculate(t, a, h, divisor);
        double RIM_SIZE = car.RIM_SIZE.calculate(t, a, h, divisor);
        double SECTION_WIDTH = car.SECTION_WIDTH.calculate(t, a, h, divisor);
        double ASPECT_RATIO = car.ASPECT_RATIO.calculate(t, a, h, divisor);
        double GEAR_RATIO = car.GEAR_RATIO[car.MAX_GEAR_INDEX].calculate(t, a, h, divisor);
        double TyreCircumference = RIM_SIZE + SECTION_WIDTH * ASPECT_RATIO / 1270;
        return (RPM / GEAR_RATIO / FINAL_DRIVE) * TyreCircumference;
    }
}