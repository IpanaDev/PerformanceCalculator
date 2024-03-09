package calculators.priority;

import calculators.result.Result;
import performance.*;
import utils.OctaBoolConsumer;

public enum Priority {
    NODE_T_A_H((result, tGain, aGain, hGain, additionalValue, tStat, aStat, hStat, engine, turbo, trans, suspension, brakes, tires) ->
            result.tGain() < tGain ||
                    result.tGain() == tGain && result.aGain() < aGain ||
                    result.tGain() == tGain && result.aGain() == aGain && result.hGain() < hGain ||
                    result.tGain() == tGain && result.aGain() == aGain && result.hGain() == hGain &&
                            Result.cost(engine,turbo,trans,suspension,brakes,tires) < result.cost()
    ),
    NODE_T_H_A((result, tGain, aGain, hGain, additionalValue, tStat, aStat, hStat, engine, turbo, trans, suspension, brakes, tires) ->
            result.tGain() < tGain ||
                    result.tGain() == tGain && result.hGain() < hGain ||
                    result.tGain() == tGain && result.hGain() == hGain && result.aGain() < aGain ||
                    result.tGain() == tGain && result.aGain() == aGain && result.hGain() == hGain &&
                            Result.cost(engine,turbo,trans,suspension,brakes,tires) < result.cost()
    ),
    NODE_A_T_H((result, tGain, aGain, hGain, additionalValue, tStat, aStat, hStat, engine, turbo, trans, suspension, brakes, tires) ->
            result.aGain() < aGain ||
                    result.aGain() == aGain && result.tGain() < tGain ||
                    result.aGain() == aGain && result.tGain() == tGain && result.hGain() < hGain ||
                    result.tGain() == tGain && result.aGain() == aGain && result.hGain() == hGain &&
                            Result.cost(engine,turbo,trans,suspension,brakes,tires) < result.cost()
    ),
    NODE_A_H_T((result, tGain, aGain, hGain, additionalValue, tStat, aStat, hStat, engine, turbo, trans, suspension, brakes, tires) ->
            result.aGain() < aGain ||
                    result.aGain() == aGain && result.hGain() < hGain ||
                    result.aGain() == aGain && result.hGain() == hGain && result.tGain() < tGain ||
                    result.tGain() == tGain && result.aGain() == aGain && result.hGain() == hGain &&
                            Result.cost(engine,turbo,trans,suspension,brakes,tires) < result.cost()
    ),
    NODE_H_A_T((result, tGain, aGain, hGain, additionalValue, tStat, aStat, hStat, engine, turbo, trans, suspension, brakes, tires) ->
            result.hGain() < hGain ||
                    result.hGain() == hGain && result.aGain() < aGain ||
                    result.hGain() == hGain && result.aGain() == aGain && result.tGain() < tGain ||
                    result.tGain() == tGain && result.aGain() == aGain && result.hGain() == hGain &&
                            Result.cost(engine,turbo,trans,suspension,brakes,tires) < result.cost()
    ),
    NODE_H_T_A((result, tGain, aGain, hGain, additionalValue, tStat, aStat, hStat, engine, turbo, trans, suspension, brakes, tires) ->
            result.hGain() < hGain ||
                    result.hGain() == hGain && result.tGain() < tGain ||
                    result.hGain() == hGain && result.tGain() == tGain && result.aGain() < aGain ||
                    result.tGain() == tGain && result.aGain() == aGain && result.hGain() == hGain &&
                            Result.cost(engine,turbo,trans,suspension,brakes,tires) < result.cost()
    ),
    TOP_SPEED_KMH((result, tGain, aGain, hGain, additionalValue, tStat, aStat, hStat, engine, turbo, trans, suspension, brakes, tires) ->
            result.realTopSpeed() < additionalValue  ||
                    result.realTopSpeed() == additionalValue && Result.cost(engine,turbo,trans,suspension,brakes,tires) < result.cost()),
    ACCEL((result, tGain, aGain, hGain, additionalValue, tStat, aStat, hStat, engine, turbo, trans, suspension, brakes, tires) ->
            result.acceleration() < additionalValue  ||
                    result.acceleration() == additionalValue && Result.cost(engine,turbo,trans,suspension,brakes,tires) < result.cost());


    private OctaBoolConsumer function;

    Priority(OctaBoolConsumer function) {
        this.function = function;
    }

    public void handle(Result result, int tGain, int aGain, int hGain, float additionalValue, float finalTopSpeed, float finalAccel, float finalHandling, int finalClassInt, PerfPart engine, PerfPart turbo, PerfPart trans, PerfPart suspension, PerfPart brakes, PerfPart tires) {
        if (this.function.check(result, tGain, aGain, hGain, additionalValue, finalTopSpeed, finalAccel, finalHandling, engine, turbo, trans, suspension, brakes, tires)) {
            result.set(engine, turbo, trans, suspension, brakes, tires, tGain, aGain, hGain, additionalValue, finalTopSpeed, finalAccel, finalHandling, finalClassInt);
        }
    }
}
