package cars;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;

public class Cars {
    public static final ArrayList<Car> CARS = new ArrayList<>();

    public static void init() throws IOException, NoSuchFieldException, IllegalAccessException {
        File file = new File("cars.txt");
        InputStreamReader fileReader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Car car = new Car();
            String[] objects = line.split(",");
            for (String object : objects) {
                String[] split = object.split("=");
                String name = split[0];
                String value = split[1];
                Field field = Car.class.getDeclaredField(name);
                field.setAccessible(true);
                if (field.getType() == int.class) {
                    field.set(car, Integer.parseInt(value));
                } else if (field.getType() == double.class) {
                    field.set(car, Double.parseDouble(value));
                } else if (field.getType() == String.class) {
                    field.set(car, value);
                } else if (field.getType().getName().equals("[I")){
                    String[] values = value.split("\\|");
                    int[] ints = new int[values.length];
                    for (int i = 0; i < values.length; i++) {
                        ints[i] = Integer.parseInt(values[i]);
                    }
                    field.set(car, ints);
                } else if (field.getType().getName().equals("[D")){
                    String[] values = value.split("\\|");
                    double[] doubles = new double[values.length];
                    for (int i = 0; i < values.length; i++) {
                        doubles[i] = Double.parseDouble(values[i]);
                    }
                    field.set(car, doubles);
                }
            }
            car.setupPreValues();
            CARS.add(car);
        }
        bufferedReader.close();
        fileReader.close();
        CARS.sort(Comparator.comparing(Car::fullName));
    }
}
