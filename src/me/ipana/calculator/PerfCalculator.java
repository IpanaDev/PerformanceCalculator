package me.ipana.calculator;


import me.ipana.calculator.checks.PreChecks;
import me.ipana.calculator.checks.SumCheck;
import me.ipana.calculator.priority.Priority;
import me.ipana.calculator.result.Result;
import me.ipana.calculator.search.AccelerationSearch;
import me.ipana.calculator.search.DefaultSearch;
import me.ipana.calculator.search.ISearch;
import me.ipana.calculator.search.TopSpeedSearch;
import me.ipana.car.Car;
import me.ipana.car.Overall;
import me.ipana.parts.PerfPart;
import me.ipana.parts.Type;
import me.ipana.parts.ValueFilter;
import me.ipana.ui.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PerfCalculator {
    private static final int CORE_COUNT = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService THREAD = Executors.newWorkStealingPool(CORE_COUNT);
    public float tValue0, tValue1, tValue2, tValue3;
    public float aValue0, aValue1, aValue2, aValue3;
    public float hValue0, hValue1, hValue2, hValue3;
    public Overall overall;

    public Result find(Car car, PerfPart[] parts) {
        System.currentTimeMillis();
        this.tValue0 = car.tStats()[0];
        this.tValue1 = car.tStats()[1];
        this.tValue2 = car.tStats()[2];
        this.tValue3 = car.tStats()[3];

        this.aValue0 = car.aStats()[0];
        this.aValue1 = car.aStats()[1];
        this.aValue2 = car.aStats()[2];
        this.aValue3 = car.aStats()[3];

        this.hValue0 = car.hStats()[0];
        this.hValue1 = car.hStats()[1];
        this.hValue2 = car.hStats()[2];
        this.hValue3 = car.hStats()[3];
        PerfPart engine = parts[0];
        PerfPart turbo = parts[1];
        PerfPart trans = parts[2];
        PerfPart suspension = parts[3];
        PerfPart brakes = parts[4];
        PerfPart tires = parts[5];
        int tGain = engine.tGain() + turbo.tGain() + trans.tGain() + suspension.tGain() + brakes.tGain() + tires.tGain();
        int aGain = engine.aGain() + turbo.aGain() + trans.aGain() + suspension.aGain() + brakes.aGain() + tires.aGain();
        int hGain = engine.hGain() + turbo.hGain() + trans.hGain() + suspension.hGain() + brakes.hGain() + tires.hGain();

        float commonDivisor = SumCache.INV_1[tGain + aGain + hGain];
        float tStat = (tGain * tValue0 + aGain * tValue1 + hGain * tValue2 + tValue3) * commonDivisor;
        float aStat = (tGain * aValue0 + aGain * aValue1 + hGain * aValue2 + aValue3) * commonDivisor;
        float hStat = (tGain * hValue0 + aGain * hValue1 + hGain * hValue2 + hValue3) * commonDivisor;
        float finalClass = ((int) tStat + (int) aStat + (int) hStat) / 3f;
        int finalClassInt = (int) finalClass;
        final Result result = new Result();
        result.set(engine, turbo, trans, suspension, brakes, tires, tGain, aGain, hGain, 0, tStat, aStat, hStat, finalClassInt);
        //System.out.println("Search took (" + (System.currentTimeMillis() - ms) + "ms)");
        //System.out.println(result);
        return result;
    }

    public Result findMultiThread(Car car, Overall overall, ValueFilter valueFilter, Priority priority) throws IllegalAccessException, NoSuchFieldException {
        System.currentTimeMillis();
        long ms = System.currentTimeMillis();
        this.tValue0 = car.tStats()[0];
        this.tValue1 = car.tStats()[1];
        this.tValue2 = car.tStats()[2];
        this.tValue3 = car.tStats()[3];

        this.aValue0 = car.aStats()[0];
        this.aValue1 = car.aStats()[1];
        this.aValue2 = car.aStats()[2];
        this.aValue3 = car.aStats()[3];

        this.hValue0 = car.hStats()[0];
        this.hValue1 = car.hStats()[1];
        this.hValue2 = car.hStats()[2];
        this.hValue3 = car.hStats()[3];
        this.overall = overall;
        PerfPart[] engineParts = valueFilter.from(Type.ENGINE);
        PerfPart[] turboParts = valueFilter.from(Type.FORCED_INDUCTION);
        PerfPart[] transParts = valueFilter.from(Type.TRANSMISSION);
        PerfPart[] suspensionParts = valueFilter.from(Type.SUSPENSION);
        PerfPart[] brakeParts = valueFilter.from(Type.BRAKES);
        PerfPart[] tireParts = valueFilter.from(Type.TIRES);
        PreChecks preChecks = new PreChecks(this);
        ISearch iSearch;
        switch (priority) {
            case TOP_SPEED_KMH:
                iSearch = new TopSpeedSearch();
                break;
            case ACCEL:
                iSearch = new AccelerationSearch();
                break;
            default:
                iSearch = new DefaultSearch();
                break;
        }
        final int lo = overall.min() * 3;
        final int hi = overall.max() * 3 + 2;
        boolean limitByBudget = UI.INSTANCE.performanceMenu().limitByBudget.isSelected();
        int budget = UI.INSTANCE.performanceMenu().budget;
        List<Callable<Result>> threadResults = new ArrayList<>(engineParts.length);
        for (PerfPart engine : engineParts) {
            if (preChecks.check(engine, preChecks.engineSum())) {
                continue;
            }
            threadResults.add(() -> {
                Result threadResult = new Result();
                PreChecks subChecks = new PreChecks(this);
                SumCheck brakeSum = subChecks.brakeSum();
                subChecks.engineSum().set(engine.tGain(), engine.aGain(), engine.hGain());
                for (PerfPart turbo : turboParts) {
                    if (subChecks.check(turbo, subChecks.turboSum())) {
                        continue;
                    }
                    for (PerfPart trans : transParts) {
                        if (subChecks.check(trans, subChecks.transSum())) {
                            continue;
                        }
                        for (PerfPart suspension : suspensionParts) {
                            if (subChecks.check(suspension, subChecks.suspensionSum())) {
                                continue;
                            }
                            for (PerfPart brakes : brakeParts) {
                                if (subChecks.check(brakes, subChecks.brakeSum())) {
                                    continue;
                                }
                                for (PerfPart tires : tireParts) {
                                    if (limitByBudget && Result.cost(engine, turbo, trans, suspension, brakes, tires) > budget) {
                                        continue;
                                    }
                                    int tGain = brakeSum.t() + tires.tGain();
                                    int aGain = brakeSum.a() + tires.aGain();
                                    int hGain = brakeSum.h() + tires.hGain();
                                    float divisor = SumCache.INV_1[tGain + aGain + hGain];
                                    float tStat = (tGain * tValue0 + aGain * tValue1 + hGain * tValue2 + tValue3) * divisor;
                                    float aStat = (tGain * aValue0 + aGain * aValue1 + hGain * aValue2 + aValue3) * divisor;
                                    float hStat = (tGain * hValue0 + aGain * hValue1 + hGain * hValue2 + hValue3) * divisor;
                                    int sum = (int) tStat + (int) aStat + (int) hStat;
                                    if ((sum - lo | hi - sum) >= 0) {
                                        priority.handle(threadResult, tGain, aGain, hGain, iSearch.additionalValue(car, tGain, aGain, hGain), tStat, aStat, hStat, sum / 3, engine, turbo, trans, suspension, brakes, tires);
                                    }
                                }
                            }
                        }
                    }
                }
                return threadResult;
            });
        }
        final Result finalResult = new Result();
        try {
            List<Future<Result>> futures = THREAD.invokeAll(threadResults);
            for (Future<Result> future : futures) {
                Result threadResult = future.get(30, TimeUnit.DAYS);
                priority.handle(
                        finalResult,
                        threadResult.tGain(), threadResult.aGain(), threadResult.hGain(),
                        threadResult.additionalValue(),
                        threadResult.tStat(), threadResult.aStat(), threadResult.hStat(), threadResult.rating(),
                        threadResult.engine(), threadResult.turbo(), threadResult.trans(), threadResult.suspension(), threadResult.brakes(), threadResult.tires());
            }
            threadResults.clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finalResult.setTime(System.currentTimeMillis() - ms);
        return finalResult;
    }
}