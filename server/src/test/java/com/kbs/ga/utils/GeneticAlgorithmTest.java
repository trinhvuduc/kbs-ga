package com.kbs.ga.utils;

import com.kbs.ga.model.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

class GeneticAlgorithmTest {

    @Test
    void testGA() {
        Database database = initDatabase();
        GeneticAlgorithm ga = new GeneticAlgorithm();
        Population population = ga.initPopulation(database);
        ga.evalPopulation(population, database);
        int generation = 1;
        while (ga.isTerminationConditionMet(population) == false) {
            // Print fittest genotype from population
            System.out.println("Generation " + generation + " Best solution: " + population.getEliteIndividual().getFitness());
            // Apply crossover
            population = ga.crossoverPopulation(population);
            // Apply mutation
            population = ga.mutatePopulation(population, database);
            // Evaluate population
            ga.evalPopulation(population, database);
            // Increment the current generation
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
    }

    private Database initDatabase() {
        Database database = new Database();
        HashMap<Integer, Team> teams = new HashMap<>();
        teams.put(1, new Team(1, "Manchester United", "MUN", "https://resources.premierleague.com/premierleague/badges/50/t1.png", "Old Trafford"));
        teams.put(2, new Team(2, "Manchester City", "MCI", "https://resources.premierleague.com/premierleague/badges/50/t43.png", "Etihad Stadium"));
        teams.put(3, new Team(3, "Liverpool", "LIV", "https://resources.premierleague.com/premierleague/badges/50/t14.png", "Anfield"));
        teams.put(4, new Team(4, "Chelsea", "CHE", "https://resources.premierleague.com/premierleague/badges/50/t8.png", "Stamford Bridge"));
        database.setTeams(teams);
        return database;
    }
}