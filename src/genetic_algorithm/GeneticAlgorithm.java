/*
    Descrição: solução concorrente para o problema do caixeiro viajante, utilizando algoritmo genético.
    Autor: Jorge Rossi
*/

package genetic_algorithm;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Classe principal da aplicação
public class GeneticAlgorithm {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Settings.NUM_THREADS);
        DistanceMatrix distanceMatrix = DistanceMatrix.loadFromFile("./src/data/teste-d.txt");

        Population population = Population.getRandomPopulation(Settings.POPULATION_SIZE, distanceMatrix.getNumberOfCities());

        for (int i = 0; i < Settings.GENERATIONS; i++) {
            System.out.println("Generation: " + i);

            Instant start = Instant.now();

            population.computeFitness(distanceMatrix, executorService);
            population.performSelection();
            population.performCrossover();
            population.performMutation();

            Instant end = Instant.now();

            System.out.println("Best fitness: " + population.getBestIndividual().getFitness());
            System.out.println("Time (s): " + Duration.between(start, end).toSeconds());
            System.out.println("");
        }

        System.out.println(population.getBestIndividual().getCities());

        executorService.shutdown();
    }
}
