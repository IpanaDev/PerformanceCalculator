package calculators.priority;

import calculators.Result;
import performance.*;
import utils.OctaBoolConsumer;

public enum Priority {
    STAT_T_A_H((result, tGain, aGain, hGain, speedKMH, tStat, aStat, hStat) ->
            result.topSpeed() < tStat ||
                    result.topSpeed() == tStat && result.acceleration() < aStat ||
                    result.topSpeed() == tStat && result.acceleration() == aStat && result.handling() < hStat),
    STAT_T_H_A((result, tGain, aGain, hGain, speedKMH, tStat, aStat, hStat) ->
            result.topSpeed() < tStat ||
                    result.topSpeed() == tStat && result.handling() < hStat ||
                    result.topSpeed() == tStat && result.handling() == hStat && result.acceleration() < aStat),
    STAT_A_T_H((result, tGain, aGain, hGain, speedKMH, tStat, aStat, hStat) ->
            result.acceleration() < aStat ||
            result.acceleration() == aStat && result.topSpeed() < tStat ||
            result.topSpeed() == tStat && result.acceleration() == aStat && result.handling() < hStat),
    STAT_A_H_T((result, tGain, aGain, hGain, speedKMH, tStat, aStat, hStat) ->
            result.acceleration() < aStat ||
            result.acceleration() == aStat && result.handling() < hStat ||
            result.handling() == hStat && result.acceleration() == aStat && result.topSpeed() < tStat),
    STAT_H_T_A((result, tGain, aGain, hGain, speedKMH, tStat, aStat, hStat) ->
            result.handling() < hStat ||
                    result.handling() == hStat && result.topSpeed() < tStat ||
                    result.handling() == hStat && result.topSpeed() == tStat && result.acceleration() < aStat),
    STAT_H_A_T((result, tGain, aGain, hGain, speedKMH, tStat, aStat, hStat) ->
            result.handling() < hStat ||
                    result.handling() == hStat && result.acceleration() < aStat ||
                    result.handling() == hStat && result.acceleration() == aStat && result.topSpeed() < tStat),
    NODE_T_A_H((result, tGain, aGain, hGain, speedKMH, tStat, aStat, hStat) ->
            result.tGain() < tGain ||
                    result.tGain() == tGain && result.aGain() < aGain ||
                    result.tGain() == tGain && result.aGain() == aGain && result.hGain() < hGain
    ),
    NODE_T_H_A((result, tGain, aGain, hGain, speedKMH, tStat, aStat, hStat) ->
            result.tGain() < tGain ||
                    result.tGain() == tGain && result.hGain() < hGain ||
                    result.tGain() == tGain && result.hGain() == hGain && result.aGain() < aGain
    ),
    NODE_A_T_H((result, tGain, aGain, hGain, speedKMH, tStat, aStat, hStat) ->
            result.aGain() < aGain ||
                    result.aGain() == aGain && result.tGain() < tGain ||
                    result.aGain() == aGain && result.tGain() == tGain && result.hGain() < hGain
    ),
    NODE_A_H_T((result, tGain, aGain, hGain, speedKMH, tStat, aStat, hStat) ->
            result.aGain() < aGain ||
                    result.aGain() == aGain && result.hGain() < hGain ||
                    result.aGain() == aGain && result.hGain() == hGain && result.tGain() < tGain
    ),
    NODE_H_A_T((result, tGain, aGain, hGain, speedKMH, tStat, aStat, hStat) ->
            result.hGain() < hGain ||
                    result.hGain() == hGain && result.aGain() < aGain ||
                    result.hGain() == hGain && result.aGain() == aGain && result.tGain() < tGain
    ),
    NODE_H_T_A((result, tGain, aGain, hGain, speedKMH, tStat, aStat, hStat) ->
            result.hGain() < hGain ||
                    result.hGain() == hGain && result.tGain() < tGain ||
                    result.hGain() == hGain && result.tGain() == tGain && result.aGain() < aGain
    ),
    TOP_SPEED_KMH((result, tGain, aGain, hGain, speedKMH, tStat, aStat, hStat) ->
            result.realTopSpeed() < speedKMH);


    private OctaBoolConsumer<Result, Integer, Integer, Integer, Double, Double, Double, Double> function;

    Priority(OctaBoolConsumer<Result, Integer, Integer, Integer, Double, Double, Double, Double> function) {
        this.function = function;
    }

    public void handle(Result result, int tGain, int aGain, int hGain, double realTopSpeed, double finalTopSpeed, double finalAccel, double finalHandling, int finalClassInt, PerfPart engine, PerfPart turbo, PerfPart trans, PerfPart suspension, PerfPart brakes, PerfPart tires) {
        if (this.function.accept(result, tGain, aGain, hGain, realTopSpeed, finalTopSpeed, finalAccel, finalHandling)) {
            result.set(engine, turbo, trans, suspension, brakes, tires, tGain, aGain, hGain, realTopSpeed, finalTopSpeed, finalAccel, finalHandling, finalClassInt);
        }
    }
}
