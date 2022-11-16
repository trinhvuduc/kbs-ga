package com.kbs.ga.utils;

import com.kbs.ga.model.Database;
import com.kbs.ga.model.Individual;
import com.kbs.ga.model.Population;

import java.util.List;

public class GeneticAlgorithm {
    int populationSize = 100;
    double crossoverRate = 0.95;
    double mutationRate = 0.01;
    int elitismCount = 0;

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
        // Create new population
        Population newPopulation = new Population();
        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.getPopulationSize(); populationIndex++) {
            Individual parent1 = population.getIndividualByIndex(0);
            // Apply crossover to this individual?
            if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
                // Initialize offspring
                Individual offspring = new Individual(parent1.getChromosomeLength());
                // Find second parent
                Individual parent2 = selectParent(population);
                // Loop over genome
                for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
                    // Use half of parent1's genes and half of parent2's genes
                    if (0.5 > Math.random()) {
                        offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                    } else {
                        offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                    }
                }
                // Add offspring to new population
                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                // Add individual to new population without applying crossover
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }
        return newPopulation;
    }

    public Population mutatePopulation(Population population, Database database) {
        population.sortBasedOnFitness();
        // Initialize new population
        Population newPopulation = new Population();
        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.getPopulationSize(); populationIndex++) {
            Individual individual = population.getIndividualByIndex(populationIndex);
            // Loop over individual’s genes
            // Create random individual to swap genes with
            Individual randomIndividual = new Individual(database);
            for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                // Skip mutation if this is an elite individual
                if (populationIndex > 0) {
                    // Does this gene need mutation?
                    if (this.mutationRate > Math.random()) {
                        // Swap for new gene
                        individual.setGene(geneIndex, randomIndividual.getGene(geneIndex));
                    }
                }
            }
            // Add individual to population
            newPopulation.setIndividual(populationIndex, individual);
        }
        // Return mutated population
        return newPopulation;
    }

    public void evalPopulation(Population population, Database database) {
        double populationFitness = population.getIndividuals().stream()
                .mapToDouble(x -> calculateFitness(x, database))
                .sum();
        population.setPopulationFitness(populationFitness);
        //sort the population based on fitness
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

