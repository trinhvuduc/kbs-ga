package com.kbs.ga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Individual {
    private int[] chromosome;
    private double fitness = -1;
    private List<MatchRound> matchRounds;

    public Individual() {
    }

    public Individual(int[] chromosome, double fitness) {
        this.chromosome = chromosome;
        this.fitness = fitness;
        this.matchRounds = new ArrayList<>();
    }

    public Individual(int[] chromosome, double fitness, List<MatchRound> matchRounds) {
        this.chromosome = chromosome;
        this.fitness = fitness;
        this.matchRounds = matchRounds;
    }

    public Individual(int length) {
        this.chromosome = new int[length];
    }

    public Individual(Database database) {
        int numberOfTeams = database.getNumberOfTeams();
        int matchesPlayedByEachTeam = (numberOfTeams - 1) * 2;

        int chromosomeLength = matchesPlayedByEachTeam * numberOfTeams;
        this.chromosome = new int[chromosomeLength];
        for (int i = 0; i < chromosomeLength; i++) {
            Team team = database.getRandomTeam();
            this.chromosome[i] = team.getId();
        }

        int chromosomePos = 0;
        List<MatchRound> rounds = new ArrayList<>();
        for (int i = 0; i < matchesPlayedByEachTeam; i++) {
            MatchRound matchRound = new MatchRound();
            for (int j = 0; j < numberOfTeams / 2; j++) {
                int id1 = chromosome[chromosomePos++];
                int id2 = chromosome[chromosomePos++];
                Team team1 = database.getTeamById(id1);
                Team team2 = database.getTeamById(id2);
                Match match = new Match(team1, team2);
                matchRound.addMatch(match);
            }
            rounds.add(matchRound);
        }
        this.matchRounds = rounds;
        this.calculateFitness();
    }

    public int[] getChromosome() {
        return chromosome;
    }

    public void setChromosome(int[] chromosome) {
        this.chromosome = chromosome;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @JsonIgnore
    public List<MatchRound> getMatchRounds() {
        return matchRounds;
    }

    public void setMatchRounds(List<MatchRound> mathRounds) {
        this.matchRounds = mathRounds;
    }

    public int getGene(int geneIndex) {
        return chromosome[geneIndex];
    }

    public void setGene(int geneIndex, int gene) {
        this.chromosome[geneIndex] = gene;
    }

    @JsonIgnore
    public int getChromosomeLength() {
        return this.chromosome.length;
    }

    public void createMatchRounds(Database database) {
        int numberOfTeams = database.getNumberOfTeams();
        int totalRounds = 0;
        if (numberOfTeams % 2 == 0) {
            totalRounds = (numberOfTeams - 1) * 2;
        } else {
            totalRounds = numberOfTeams * 2;
        }

        // Create match round
        int chromosomePos = 0;
        List<MatchRound> rounds = new ArrayList<>();
        for (int i = 0; i < totalRounds; i++) {
            MatchRound matchRound = new MatchRound();
            for (int j = 0; j < numberOfTeams / 2; j++) {
                int id1 = chromosome[chromosomePos++];
                int id2 = chromosome[chromosomePos++];
                Team team1 = database.getTeamById(id1);
                Team team2 = database.getTeamById(id2);
                Match match = new Match(team1, team2);
                matchRound.addMatch(match);
                matchRound.generateTime(i);
            }
            rounds.add(matchRound);
        }
        this.matchRounds = rounds;
        this.calculateFitness();
    }

    public void calculateFitness() {
        int clashes = calculateConflicts();
        double fitness = 1 / (double) (clashes + 1);
        this.setFitness(fitness);
    }

    public int calculateConflicts() {
        int teamsPlayingMatchesInRound = 0;
        int numberOfTimeSameMatchBeingPlayed = 0;
        int teamsPlayingAgainstItSelf = 0;

        List<MatchRound> seasonSchedule = this.getMatchRounds();

        List<Match> allMatches = seasonSchedule.stream()
                .flatMap(x -> x.getMatches().stream())
                .collect(Collectors.toList());
        Map<Match, Long> matchesMap = allMatches.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        numberOfTimeSameMatchBeingPlayed = matchesMap.values().stream()
                .filter(x -> x.intValue() > 1)
                .mapToInt(x -> x.intValue())
                .sum();

        for (MatchRound matchSchedule : seasonSchedule) {
            Integer[] matchDayChromosome = matchSchedule.getMatches().stream()
                    .flatMap(x -> Stream.of(x.getMatch()))
                    .flatMap(x -> Stream.of(x.getId()))
                    .toArray(Integer[]::new);
            Map<Integer, Long> teamPlayingMultipleTimesSameDayMap = Arrays.asList(matchDayChromosome).stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            int sum = teamPlayingMultipleTimesSameDayMap.values().stream()
                    .filter(x -> x.intValue() > 1)
                    .mapToInt(x -> x.intValue())
                    .sum();
            teamsPlayingMatchesInRound = teamsPlayingMatchesInRound + sum;
        }

        long teamsPlayingAgainstEachOtherLong = allMatches.stream()
                .filter(x -> x.getMatch()[0] == x.getMatch()[1])
                .count();
        teamsPlayingAgainstItSelf = (int) teamsPlayingAgainstEachOtherLong;

        var conflicts = numberOfTimeSameMatchBeingPlayed + teamsPlayingMatchesInRound + teamsPlayingAgainstItSelf;
        return conflicts;
    }

    public void displaySchedule() {
        List<MatchRound> seasonSchedule = getMatchRounds();
        for (MatchRound matchSchedule : seasonSchedule) {
            int matchDayNumber = seasonSchedule.indexOf(matchSchedule) + 1;
            System.out.println("Round: " + matchDayNumber);
            for (Match match : matchSchedule.getMatches()) {
                Team[] match1 = match.getMatch();
                int matchNumber = matchSchedule.getMatches().indexOf(match) + 1;
                Team teamA = match1[0];
                Team teamB = match1[1];
                System.out.println("Match " + matchNumber + ": " + teamA.getName() + " (H)" + " Vs " + teamB.getName() + " (A)");
            }
            System.out.println();
        }
    }

    public String toString() {
        String output = "";
        for (int gene = 0; gene < this.chromosome.length; gene++) {
            output += this.chromosome[gene] + ",";
        }
        return output;
    }
}
