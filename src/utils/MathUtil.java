package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {
    public static String roundUpString(double value, int places) {
        BigDecimal bd = rounding(value, places);
        return bd.doubleValue() == bd.intValue() ? String.valueOf(bd.intValue()) : String.valueOf(bd.doubleValue());
    }
    public static String roundUpString(float value, int places) {
        BigDecimal bd = rounding(value, places);
        return bd.floatValue() == bd.intValue() ? String.valueOf(bd.intValue()) : String.valueOf(bd.floatValue());
    }
    public static double roundUp(double value, int places) {
        return rounding(value, places).doubleValue();
    }
    public static float roundUp(float value, int places) {
        return rounding(value, places).floatValue();
    }
    private static BigDecimal rounding(Number number, int places) {
        if (places<0) {
            throw new IllegalArgumentException();
        } else {
            BigDecimal bd = new BigDecimal(String.valueOf(number));
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd;
        }
    }
}
