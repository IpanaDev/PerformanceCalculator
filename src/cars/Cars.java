package cars;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Cars {
    public static final ArrayList<Car> CARS = new ArrayList<>();

    public static void init() throws IOException {
        File file = new File("cars.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(",");
            String vltName = split[0];
            String fullName = split[1];
            int[] top = {Integer.parseInt(split[2]),Integer.parseInt(split[3]),Integer.parseInt(split[4]),Integer.parseInt(split[5])};
            int[] accel = {Integer.parseInt(split[6]),Integer.parseInt(split[7]),Integer.parseInt(split[8]),Integer.parseInt(split[9])};
            int[] handling = {Integer.parseInt(split[10]),Integer.parseInt(split[11]),Integer.parseInt(split[12]),Integer.parseInt(split[13])};
            int[] RPM = {Integer.parseInt(split[14]),Integer.parseInt(split[15]),Integer.parseInt(split[16]),Integer.parseInt(split[17])};
            int[][] wheelRatio = new int[4][2];
            int[][] wheelWidth = new int[4][2];
            int[][] wheelSize = new int[4][2];
            for (int i = 18; i <= 36; i+=6) {
                wheelRatio[(i-18)/6][0] = Integer.parseInt(split[i]);
                wheelRatio[(i-18)/6][1] = Integer.parseInt(split[i+1]);
            }
            for (int i = 20; i <= 38; i+=6) {
                wheelWidth[(i-20)/6][0] = Integer.parseInt(split[i]);
                wheelWidth[(i-20)/6][1] = Integer.parseInt(split[i+1]);
            }
            for (int i = 22; i <= 40; i+=6) {
                wheelSize[(i-22)/6][0] = Integer.parseInt(split[i]);
                wheelSize[(i-22)/6][1] = Integer.parseInt(split[i+1]);
            }
            double[] gearRatio = new double[]{Double.parseDouble(split[42]),Double.parseDouble(split[43]),Double.parseDouble(split[44]),Double.parseDouble(split[45]),Double.parseDouble(split[46]),Double.parseDouble(split[47]),Double.parseDouble(split[48]),Double.parseDouble(split[49]),Double.parseDouble(split[50])};
            double torqueSplit = Double.parseDouble(split[51]);
            double[] finalDrive = {Double.parseDouble(split[52]), Double.parseDouble(split[53]), Double.parseDouble(split[54]), Double.parseDouble(split[55])};
            Car car = new Car(vltName, fullName, top, accel, handling, RPM, wheelRatio, wheelWidth, wheelSize, gearRatio, torqueSplit, finalDrive);
            CARS.add(car);
        }
        bufferedReader.close();
        fileReader.close();
        CARS.sort(Comparator.comparing(Car::fullName));
    }
}
