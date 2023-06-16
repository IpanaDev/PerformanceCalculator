package calculators.priority;

import calculators.Result;
import performance.*;
import utils.QuadBoolConsumer;

public enum Priority {
    T_A_H((result, topSpeed, accel, handling) ->
            result.topSpeed() < topSpeed ||
                    result.topSpeed() == topSpeed && result.acceleration() < accel ||
                    result.topSpeed() == topSpeed && result.acceleration() == accel && result.handling() < handling),
    T_H_A((result, topSpeed, accel, handling) ->
            result.topSpeed() < topSpeed ||
                    result.topSpeed() == topSpeed && result.handling() < handling ||
                    result.topSpeed() == topSpeed && result.handling() == handling && result.acceleration() < accel),
    A_T_H((result, topSpeed, accel, handling) ->
            result.acceleration() < accel ||
            result.acceleration() == accel && result.topSpeed() < topSpeed ||
            result.topSpeed() == topSpeed && result.acceleration() == accel && result.handling() < handling),
    A_H_T((result, topSpeed, accel, handling) ->
            result.acceleration() < accel ||
            result.acceleration() == accel && result.handling() < handling ||
            result.handling() == handling && result.acceleration() == accel && result.topSpeed() < topSpeed),
    H_T_A((result, topSpeed, accel, handling) ->
            result.handling() < handling ||
                    result.handling() == handling && result.topSpeed() < topSpeed ||
                    result.handling() == handling && result.topSpeed() == topSpeed && result.acceleration() < accel),
    H_A_T((result, topSpeed, accel, handling) ->
            result.handling() < handling ||
                    result.handling() == handling && result.acceleration() < accel ||
                    result.handling() == handling && result.acceleration() == accel && result.topSpeed() < topSpeed);


    private String name;
    private QuadBoolConsumer<Result, Double, Double, Double> consumer;

    Priority(QuadBoolConsumer<Result, Double, Double, Double> result) {
        this.name = name().
                replace("T", "TopSpeed").
                replace("A", "Acceleration").
                replace("H", "Handling").
                replace("P", "Price").
                replace("_", "-");
        this.consumer = result;
    }

    public void handle(Result result, double finalTopSpeed, double finalAccel, double finalHandling, int finalClassInt, EngineParts engine, TurboParts turbo, TransmissionParts trans, SuspensionParts suspension, BrakeParts brakes, TireParts tires) {
        if (this.consumer.accept(result, finalTopSpeed, finalAccel, finalHandling)) {
            result.set(engine, turbo, trans, suspension, brakes, tires, finalTopSpeed, finalAccel, finalHandling, finalClassInt);
        }
    }
    public String fullName() {
        return name;
    }
}
