package com.kbs.ga.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kbs.ga.model.*;
import com.kbs.ga.utils.GeneticAlgorithm;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
            // Print fittest genotype from population
            System.out.println("Generation " + generation + " Best solution: " + population.getIndividualByIndex(0).getFitness());
            // Apply crossover
            population = ga.crossoverPopulation(population);
            // Apply mutation
            population = ga.mutatePopulation(population, database);
            // Evaluate population
            ga.evalPopulation(population, database);
            // Increment the current generation
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

//        /* Save file */
//        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        String json = ow.writeValueAsString(generations);
//        File file = new ClassPathResource("generations.json").getFile();
//        System.out.println(file.toPath());
//        Files.writeString(file.toPath(), json.subSequence(0, json.length() - 1));

        return generations;
    }
}
