package performance;

import java.lang.reflect.Field;

public enum ValueFilter {
    GREEN_VALUES("Green Parts"),
    BLUE_VALUES("Blue Parts"),
    RED_VALUES("Red Parts"),
    WHITE_VALUES("White Parts"),
    VALUES("All Parts"),
    CUSTOM("Custom Parts");

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

    public PerfPart[] from(Type type) throws NoSuchFieldException, IllegalAccessException {
        PartsLoader parts = PartsLoader.fromType(type);
        Field field = parts.getClass().getDeclaredField(this.name());
        return (PerfPart[]) field.get(parts);
    }
}
