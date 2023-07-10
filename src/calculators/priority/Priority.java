package calculators.priority;

import calculators.Result;
import performance.*;
import utils.PentaBoolConsumer;

public enum Priority {
    T_A_H((result, realTopSpeed, topSpeed, accel, handling) ->
            result.topSpeed() < topSpeed ||
                    result.topSpeed() == topSpeed && result.acceleration() < accel ||
                    result.topSpeed() == topSpeed && result.acceleration() == accel && result.handling() < handling),
    T_H_A((result, realTopSpeed, topSpeed, accel, handling) ->
            result.topSpeed() < topSpeed ||
                    result.topSpeed() == topSpeed && result.handling() < handling ||
                    result.topSpeed() == topSpeed && result.handling() == handling && result.acceleration() < accel),
    A_T_H((result, realTopSpeed, topSpeed, accel, handling) ->
            result.acceleration() < accel ||
            result.acceleration() == accel && result.topSpeed() < topSpeed ||
            result.topSpeed() == topSpeed && result.acceleration() == accel && result.handling() < handling),
    A_H_T((result, realTopSpeed, topSpeed, accel, handling) ->
            result.acceleration() < accel ||
            result.acceleration() == accel && result.handling() < handling ||
            result.handling() == handling && result.acceleration() == accel && result.topSpeed() < topSpeed),
    H_T_A((result, realTopSpeed, topSpeed, accel, handling) ->
            result.handling() < handling ||
                    result.handling() == handling && result.topSpeed() < topSpeed ||
                    result.handling() == handling && result.topSpeed() == topSpeed && result.acceleration() < accel),
    H_A_T((result, realTopSpeed, topSpeed, accel, handling) ->
            result.handling() < handling ||
                    result.handling() == handling && result.acceleration() < accel ||
                    result.handling() == handling && result.acceleration() == accel && result.topSpeed() < topSpeed),
    R((result, realTopSpeed, topSpeed, accel, handling) ->
            result.realTopSpeed() < realTopSpeed);


    private String name;
    private PentaBoolConsumer<Result, Double, Double, Double, Double> consumer;

    Priority(PentaBoolConsumer<Result, Double, Double, Double, Double> result) {
        this.name = name().
                replace("T", "TopSpeed").
                replace("A", "Acceleration").
                replace("H", "Handling").
                replace("P", "Price").
                replace("R","RealTSpeed").
                replace("_", "-");
        this.consumer = result;
    }

    public void handle(Result result, double realTopSpeed, double finalTopSpeed, double finalAccel, double finalHandling, int finalClassInt, PerfPart engine, PerfPart turbo, PerfPart trans, PerfPart suspension, PerfPart brakes, PerfPart tires) {
        if (this.consumer.accept(result, realTopSpeed, finalTopSpeed, finalAccel, finalHandling)) {
            result.set(engine, turbo, trans, suspension, brakes, tires, realTopSpeed, finalTopSpeed, finalAccel, finalHandling, finalClassInt);
        }
    }

    public String fullName() {
        return name;
    }
}
