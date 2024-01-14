package utils;

import calculators.result.Result;
import performance.PerfPart;

public interface OctaBoolConsumer {
    boolean check(Result result, int tGain, int aGain, int hGain, double speedKMH, double tStat, double aStat, double hStat, PerfPart engine, PerfPart turbo, PerfPart trans, PerfPart suspension, PerfPart brakes, PerfPart tires);
}
