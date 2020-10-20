package genetic_algorithm;

import java.util.*;

public class Population {
    private List<Individual> individuals;
    private final int size;
    private Individual bestIndividual;

    public Population(List<Individual> individuals) {
        this.individuals = individuals;
        this.size = individuals.size();
    }

    public static Population getRandomPopulation(int size, int numberOfCities) {
        List<Individual> individuals = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            individuals.add(Individual.getRandomIndividual(numberOfCities));
        }

        return new Population(individuals);
    }

    public void computeFitness(DistanceMatrix distanceMatrix) {
        for (Individual individual : individuals)
            individual.computeFitness(distanceMatrix);
    }

    public void performSelection() {
        Individual[] individualsSortedByFitness = individuals.toArray(new Individual[0]);

        Arrays.parallelSort(individualsSortedByFitness, Comparator.comparing(Individual::getFitness));

        if (bestIndividual == null || individualsSortedByFitness[0].getFitness() < bestIndividual.getFitness())
            bestIndividual = individualsSortedByFitness[0].copy();

        individuals = new ArrayList<>(Arrays.asList(individualsSortedByFitness))
                .subList(0, individuals.size() / 2);
    }

    public void performCrossover() {
        List<Individual> children = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < size; i++) {
            Individual parent1 = individuals.get(random.nextInt(individuals.size()));
            Individual parent2 = individuals.get(random.nextInt(individuals.size()));

            if (Math.random() < Settings.CROSSOVER_RATE)
                children.addAll(parent1.getChildren(parent2));
            else {
                children.add(parent1);
                children.add(parent2);
            }
        }

        individuals = children;
    }

    public void performMutation() {
        for (Individual individual : individuals)
            individual.mutate();
    }

    public Individual getBestIndividual() {
        return bestIndividual;
    }
}
