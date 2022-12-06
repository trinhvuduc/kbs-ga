package com.kbs.ga.utils;

import com.kbs.ga.model.Database;
import com.kbs.ga.model.Individual;
import com.kbs.ga.model.Population;

import java.util.List;

public class GeneticAlgorithm {
    int populationSize = 100;
    double crossoverRate = 0.9;
    double mutationRate = 0.01;

    public Population initPopulation(Database database) {
        Population population = new Population(this.populationSize, database);
        return population;
    }

    public boolean isTerminationConditionMet(Population population) {
        boolean isTerminationConditionMet = population.getIndividuals().stream()
                .anyMatch(ind -> ind.getFitness() == 1);
        return isTerminationConditionMet;
    }

    public Individual selectParent(Population population) {
        List<Individual> individuals = population.getIndividuals();
        double populationFitness = population.getPopulationFitness();
        double rouletteWheelPosition = Math.random() * populationFitness;

        double spinWheel = 0;
        for (Individual individual : individuals) {
            spinWheel += individual.getFitness();
            if (spinWheel >= rouletteWheelPosition) {
                return individual;
            }
        }

        return individuals.get(population.getPopulationSize() - 1);
    }

    public Population crossoverPopulation(Population population) {
        Population newPopulation = new Population();
        for (int populationIndex = 0; populationIndex < population.getPopulationSize(); populationIndex++) {
            Individual parent1 = population.getIndividualByIndex(0);

            if (this.crossoverRate > Math.random()) {
                Individual offspring = new Individual(parent1.getChromosomeLength());
                Individual parent2 = selectParent(population);

                for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
                    if (0.5 > Math.random()) {
                        offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                    } else {
                        offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                    }
                }

                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }
        return newPopulation;
    }

    public Population mutatePopulation(Population population, Database database) {
        population.sortBasedOnFitness();
        Population newPopulation = new Population();
        for (int populationIndex = 0; populationIndex < population.getPopulationSize(); populationIndex++) {
            Individual individual = population.getIndividualByIndex(populationIndex);
            Individual randomIndividual = new Individual(database);
            for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                if (populationIndex > 0) {
                    if (this.mutationRate > Math.random()) {
                        individual.setGene(geneIndex, randomIndividual.getGene(geneIndex));
                    }
                }
            }
            newPopulation.setIndividual(populationIndex, individual);
        }
        return newPopulation;
    }

    public void evalPopulation(Population population, Database database) {
        double populationFitness = population.getIndividuals().stream()
                .mapToDouble(x -> calculateFitness(x, database))
                .sum();
        population.setPopulationFitness(populationFitness);
        population.sortBasedOnFitness();
    }

    public double calculateFitness(Individual individual, Database database) {
        individual.createMatchRounds(database);
        int clashes = individual.calculateConflicts();
        double fitness = 1 / (double) (clashes + 1);
        individual.setFitness(fitness);
        return fitness;
    }
}

