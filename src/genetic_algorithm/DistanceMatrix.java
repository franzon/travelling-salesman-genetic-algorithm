package genetic_algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DistanceMatrix {
    private final List<List<Double>> distances;

    public DistanceMatrix(List<List<Double>> distances) {
        this.distances = distances;
    }

    public double getDistanceBetweenCities(int city1, int city2) {
        return distances.get(city1).get(city2);
    }

    public int getNumberOfCities() {
        return distances.size();
    }

    public static DistanceMatrix loadFromFile(String filePath) {
        try {
            List<List<Double>> distances = new ArrayList<>();

            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                List<String> distancesString = Arrays.asList(line.split(" +"));

                List<Double> distancesDouble = distancesString
                        .stream()
                        .filter(s -> !s.isBlank())
                        .map(Double::valueOf)
                        .collect(Collectors.toList());

                distances.add(distancesDouble);
            }

            bufferedReader.close();

            return new DistanceMatrix(distances);
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}
