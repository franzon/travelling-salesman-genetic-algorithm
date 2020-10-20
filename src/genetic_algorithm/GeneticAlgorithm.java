package genetic_algorithm;

public class GeneticAlgorithm {

    public static void main(String[] args) {
        DistanceMatrix distanceMatrix = DistanceMatrix.loadFromFile("./src/data/teste-d.txt");

        Population population = Population.getRandomPopulation(Settings.POPULATION_SIZE, distanceMatrix.getNumberOfCities());

        for (int i = 0; i < Settings.GENERATIONS; i++) {
            System.out.println("Generation: " + i);

            population.computeFitness(distanceMatrix);
            population.performSelection();
            population.performCrossover();
            population.performMutation();

            System.out.println("Best fitness: " + population.getBestIndividual().getFitness());
        }

        System.out.println(population.getBestIndividual().getCities());
    }
}
