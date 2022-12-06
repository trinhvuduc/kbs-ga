package com.kbs.ga.service;

import com.kbs.ga.model.*;
import com.kbs.ga.utils.GeneticAlgorithm;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GAServiceImpl implements GAService {

    @Override
    public Population initPopulation(List<Team> teams) {
        Database database = new Database(teams);
        GeneticAlgorithm ga = new GeneticAlgorithm();
        return ga.initPopulation(database);
    }

    @Override
    public List<Population> executeLogic(List<Team> teams, Population population) throws IOException {
        Database database = new Database(teams);
        for (int i = 0; i < population.getPopulationSize(); i++) {
            population.getIndividualByIndex(i).createMatchRounds(database);
        }

        GeneticAlgorithm ga = new GeneticAlgorithm();
        List<Population> generations = new ArrayList<>();
        ga.evalPopulation(population, database);
        generations.add(population);
        int generation = 1;

        while (ga.isTerminationConditionMet(population) == false) {
            System.out.println("Generation " + generation + " Best solution: " + population.getEliteIndividual().getFitness());
            // Apply crossover
            population = ga.crossoverPopulation(population);
            // Apply mutation
            population = ga.mutatePopulation(population, database);
            // Evaluate population
            ga.evalPopulation(population, database);
            generations.add(population);
            generation++;
        }

        Individual fittest = population.getEliteIndividual();
        System.out.println();
        System.out.println("Found solution in " + generation + " generations");
        System.out.println("Best solution: " + fittest.getFitness());
        System.out.println("Best solution: " + fittest);
        System.out.println();
        System.out.println("#######################");
        System.out.println("English Premier League Schedule");
        System.out.println("#######################");
        System.out.println();

        fittest.createMatchRounds(database);
        fittest.displaySchedule();

        List<Population> result = new ArrayList<>();
        for (Population pop: generations) {
            Individual elite = pop.getEliteIndividual();
            Population newPop = new Population();
            newPop.setIndividuals(List.of(elite));
            result.add(newPop);
        }

        return result;
    }
}
