package genetic_algorithm;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

// A classe Task representa uma tarefa que calcula o fitness de parte da população
class Task implements Runnable {
    private final DistanceMatrix distanceMatrix;
    private final CountDownLatch countDownLatch;
    private final List<Individual> individuals;
    private final int startIndex;
    private final int endIndex;

    public Task(DistanceMatrix distanceMatrix, CountDownLatch countDownLatch, List<Individual> individuals, int startIndex, int endIndex) {
        this.distanceMatrix = distanceMatrix;
        this.countDownLatch = countDownLatch;
        this.individuals = individuals;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void run() {
        for (int i = startIndex; i < endIndex; i++)
            individuals.get(i).computeFitness(distanceMatrix);

        countDownLatch.countDown();
    }
}

// Classe para representar uma população de indivíduos
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

        for (int i = 0; i < size; i++)
            individuals.add(Individual.getRandomIndividual(numberOfCities));

        return new Population(individuals);
    }

    public void computeFitness(DistanceMatrix distanceMatrix, ExecutorService executorService) {
        CountDownLatch countDownLatch = new CountDownLatch(Settings.NUM_THREADS);

        int individualsPerThread = individuals.size() / Settings.NUM_THREADS;

        for (int i = 0; i < Settings.NUM_THREADS; i++) {
            int startIndex = i * individualsPerThread;
            int endIndex = startIndex + individualsPerThread;

            executorService.submit(new Task(distanceMatrix, countDownLatch, individuals, startIndex, endIndex));
        }

        // Aguarda todas as threads terminarem de executar
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Foi utilizada uma seleção elitista para simplificar o desenvolvimento
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
            if (Math.random() < Settings.MUTATION_RATE_INDIVIDUAL)
                individual.mutate();
    }

    public Individual getBestIndividual() {
        return bestIndividual;
    }
}
