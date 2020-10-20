package genetic_algorithm;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Classe para representar um indivíduo
public class Individual {
    private final List<Integer> cities;
    private double fitness;

    public Individual(List<Integer> cities) {
        this.cities = cities;
        this.fitness = 0.0;
    }

    public List<Integer> getCities() {
        return cities;
    }

    public void mutate() {
        for (int i = 0; i < cities.size(); i++) {
            if (Math.random() < Settings.MUTATION_RATE_CITY) {
                Collections.swap(cities, i, (i + 1) % cities.size());
            }
        }
    }

    public void computeFitness(DistanceMatrix distanceMatrix) {
        double distance = 0;

        for (int i = 0; i < cities.size() - 1; i++) {
            distance += distanceMatrix.getDistanceBetweenCities(cities.get(i), cities.get(i + 1));
        }

        fitness = distance;
    }

    public static Individual getRandomIndividual(int numberOfCities) {
        List<Integer> cities = IntStream
                .range(0, numberOfCities)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(cities);

        return new Individual(cities);
    }

    public double getFitness() {
        return fitness;
    }

    // Realiza o "cruzamento", sem que a cidade se repita para o mesmo indivíduo
    private static Individual orderedCrossover(Individual parent1, Individual parent2) {
        List<Integer> cities = new ArrayList<>();

        // Foi utilizado um HashMap para otimizar o tempo de verificação se uma cidade já está presente no indivíduo.
        // Sem o HashMap, a maior parte do tempo de execução do algoritmo estava sendo gasta verificando se a cidade já
        // estava presente no indivíduo (log(N)).
        HashMap<Integer, Boolean> alreadyUsedCities = new HashMap<>();

        Random random = new Random();

        int cut1 = random.nextInt(parent1.cities.size());
        int cut2 = random.nextInt(parent2.cities.size());

        int startIndex = Math.min(cut1, cut2);
        int endIndex = Math.max(cut1, cut2);

        for (int i = startIndex; i < endIndex; i++) {
            cities.add(parent1.getCities().get(i));
            alreadyUsedCities.put(parent1.getCities().get(i), true);
        }

        for (Integer city : parent2.getCities()) {
            if (!alreadyUsedCities.containsKey(city))
                cities.add(city);
        }

        return new Individual(cities);
    }

    public List<Individual> getChildren(Individual otherParent) {
        return Arrays.asList(orderedCrossover(this, otherParent), orderedCrossover(otherParent, this));
    }

    public Individual copy() {
        Individual individual = new Individual(new ArrayList<>(cities));
        individual.fitness = fitness;

        return individual;
    }
}
