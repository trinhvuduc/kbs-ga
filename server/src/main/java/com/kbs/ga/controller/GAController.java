package com.kbs.ga.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbs.ga.model.*;
import com.kbs.ga.utils.GeneticAlgorithm;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/ga")
public class GAController {

    @PostMapping("/init")
    public Population initPopulation(@RequestBody List<Team> teams) {
        Database database = new Database(teams);
        GeneticAlgorithm ga = new GeneticAlgorithm();
        Population population = ga.initPopulation(database);
        Population newPopulation = new Population();
        newPopulation.setPopulationFitness(population.getPopulationFitness());
        List<Individual> individuals = new ArrayList<>();
        for (int i = 0; i < population.getIndividuals().size(); i++) {
            Individual individual = population.getIndividualByIndex(i);
            // ignore match rounds
            individual.setMatchRounds(List.of());
            individuals.add(individual);
        }
        newPopulation.setIndividuals(individuals);
        return newPopulation;
    }

    @PostMapping("/exec")
    public List<Population> executeLogic(@RequestBody Map<String, Object> body) {//    public String executeLogic(@RequestBody Map<String, Object> body) throws IOException {
        Object object1 = body.get("teams");
        Object object2 = body.get("population");
        ObjectMapper mapper = new ObjectMapper();
        List<Team> teams = mapper.convertValue(object1, new TypeReference<List<Team>>() {});
        Population population = mapper.convertValue(object2, new TypeReference<Population>() {});

        Database database = new Database(teams);

        for (int i = 0; i < population.getPopulationSize(); i++) {
            population.getIndividualByIndex(i).createMatchRounds(database);
        }

        List<Population> generations = new ArrayList<>();

        GeneticAlgorithm ga = new GeneticAlgorithm();
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
        System.out.println("Found solution in " + generation + " generations");
        System.out.println("Best solution: " + population.getIndividualByIndex(0).getFitness());
        Individual individual = population.getIndividuals().stream()
                .filter(ind -> ind.getFitness() == 1)
                .findAny().orElse(null);
        System.out.println(individual.getFitness());
        System.out.println("Best solution: " + population.getIndividualByIndex(0).toString());
        System.out.println();
        System.out.println("#######################");
        System.out.println("English Premier League Schedule");
        System.out.println("#######################");
        System.out.println();
        Individual fittest = population.getIndividualByIndex(0);
        database.createSeasonSchedule(fittest, database);
        List<MatchRound> seasonSchedule = database.getSeasonSchedule();
        for (MatchRound matchSchedule : seasonSchedule) {
            int matchDayNumber = seasonSchedule.indexOf(matchSchedule) + 1;
            System.out.println("MatchRound: " + matchDayNumber);
            for (Match match : matchSchedule.getMatches()) {
                Team[] match1 = match.getMatch();
                int matchNumber = matchSchedule.getMatches().indexOf(match) + 1;
                Team teamA = match1[0];
                Team teamB = match1[1];
                System.out.println("Match " + matchNumber + ": " + teamA.getName() + " (H)" + " Vs " + teamB.getName() + " (A)");

            }
        }

         /* Save file */
//        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        String json = ow.writeValueAsString(generations);
//        File file = new ClassPathResource("generations.json").getFile();
//        System.out.println(file.toPath());
//        Files.writeString(file.toPath(), json.subSequence(0, json.length() - 1));

        return generations;
//        return String.valueOf(generations.size());
    }

    @PostMapping("/exec1")
    public String test(@RequestBody Population population) {
        System.out.println(population);
        return "200";
    }
}
