package cars;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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
            int RPM = Integer.parseInt(split[14]);
            int NOS_RPM = Integer.parseInt(split[15]);
            int[] wheelRatio = new int[]{Integer.parseInt(split[16]),Integer.parseInt(split[17])};
            int[] wheelWidth = new int[]{Integer.parseInt(split[18]),Integer.parseInt(split[19])};
            int[] wheelSize = new int[]{Integer.parseInt(split[20]),Integer.parseInt(split[21])};
            double[] gearRatio = new double[]{Double.parseDouble(split[22]),Double.parseDouble(split[23]),Double.parseDouble(split[24]),Double.parseDouble(split[25]),Double.parseDouble(split[26]),Double.parseDouble(split[27]),Double.parseDouble(split[28]),Double.parseDouble(split[29]),Double.parseDouble(split[30])};
            double[] differential = new double[]{Double.parseDouble(split[31]), Double.parseDouble(split[32]), Double.parseDouble(split[33])};
            double[] finalDrive = new double[]{Double.parseDouble(split[34]), Double.parseDouble(split[35]), Double.parseDouble(split[36]), Double.parseDouble(split[37])};
            Car car = new Car(vltName, fullName, top, accel, handling, RPM, wheelRatio, wheelWidth, wheelSize, gearRatio, differential, finalDrive);
            System.out.println(fullName+" , "+car.drivetrain().name());
            CARS.add(car);
        }
        bufferedReader.close();
        fileReader.close();
        CARS.sort(Comparator.comparing(Car::fullName));
    }
}
