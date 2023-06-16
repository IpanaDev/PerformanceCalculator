package performance;

import java.lang.reflect.Field;
import java.util.Locale;

public enum ValueFilter {
    GREEN_VALUES("Green Parts"),
    BLUE_VALUES("Blue Parts"),
    RED_VALUES("Red Parts"),
    WHITE_VALUES("White Parts"),
    VALUES("Custom Parts");

    ValueFilter(String name) {
        this.name = name;
    }
    private String name;

    public String fullName() {
        return name;
    }

    @Override
    public String toString() {
        return fullName();
    }

    public static String formattedName(Enum<?> e) {
        String[] split = e.name().split("_");
        if (split.length != 4) {
            return e.name().charAt(0)+e.name().substring(1).toLowerCase(Locale.ENGLISH);
        }
        String number = split[0].
                replace("ONE", "1").
                replace("TWO","2").
                replace("THREE", "3").
                replace("FOUR", "4").
                replace("FIVE", "5");
        int star = Integer.parseInt(number);
        String color = split[2].charAt(0)+split[2].substring(1).toLowerCase(Locale.ENGLISH);
        String brand = split[3].charAt(0)+split[3].substring(1).toLowerCase(Locale.ENGLISH);
        String extra = "";
        if (star > 1 && brand.equals("Improved")) {
            extra = " / "+(star-1)+ "* "+color.charAt(0)+" Elite";
        }
        return number + "* " + color + " " + brand + extra;
    }

    public <T extends Enum<?>> T[] from(Class<T> enumPart) throws NoSuchFieldException, IllegalAccessException {
        Field field = enumPart.getDeclaredField(this.name());
        return (T[]) field.get(null);
    }



}
