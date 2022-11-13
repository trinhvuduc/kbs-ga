package com.kbs.ga.model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Database {
    private HashMap<Integer, Team> teams;
    private List<MatchRound> seasonSchedule;

    public Database(List<Team> list) {
        this.teams = new HashMap<>();
        this.seasonSchedule = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Team team = list.get(i);
            team.setId(i + 1);
            this.teams.put(team.getId(), team);
        }
    }

    public Database(Database cloneable) {
        this.teams = cloneable.teams;
        this.seasonSchedule = new ArrayList<>();
    }

    public HashMap<Integer, Team> getTeams() {
        return teams;
    }

    public void setTeams(HashMap<Integer, Team> teams) {
        this.teams = teams;
    }

    public List<MatchRound> getSeasonSchedule() {
        return seasonSchedule;
    }

    public void setSeasonSchedule(List<MatchRound> seasonSchedule) {
        this.seasonSchedule = seasonSchedule;
    }

    public Team getRandomTeam() {
        Object[] teamArrays = this.teams.values().toArray();
        int randomId = (int) (teamArrays.length * Math.random());
        Team team = (Team) teamArrays[randomId];
        return team;
    }

    public int getNumberOfTeams() {
        return this.teams.size();
    }

    public Team getTeamById(int teamId) {
        return this.teams.get(teamId);
    }

    public void createSeasonSchedule(Individual individual, Database database) {
        int numberOfTeams = teams.size();
        int matchDays = (numberOfTeams - 1) * 2;
        int[] chromosome = individual.getChromosome();
        int chromsoPos = 0;
        int seasonSchedulePos = 0;
        for (int i = 0; i < matchDays; i++) {
            MatchRound matchRound = new MatchRound();
            for (int j = 0; j < numberOfTeams / 2; j++) {
                int id1 = chromosome[chromsoPos++];
                int id2 = chromosome[chromsoPos++];
                Team team1 = database.getTeamById(id1);
                Team team2 = database.getTeamById(id2);
                Match match = new Match(team1, team2);
                matchRound.addMatch(match);
            }
            this.seasonSchedule.add(matchRound);
        }
    }

    public int calculateConflicts() {
        int numberOfTimeSameMatchBeingPlayed = 0;
        int teamsPlayingMultipleMatchesSameDay = 0;
        int teamsPlayingAgainstEachOther = 0;

        List<MatchRound> seasonSchedule = this.seasonSchedule;

        // Calculate number of times same match being played
        List<Match> allMatches = seasonSchedule.stream()
                .flatMap(x -> x.getMatches().stream())
                .collect(Collectors.toList());
        Map<Match, Long> matchesMap = allMatches.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        numberOfTimeSameMatchBeingPlayed = matchesMap.values().stream()
                .filter(x -> x.intValue() > 1)
                .mapToInt(x -> x.intValue())
                .sum();
        if (numberOfTimeSameMatchBeingPlayed != 0) {
            numberOfTimeSameMatchBeingPlayed = numberOfTimeSameMatchBeingPlayed / 2;
        }

        // Calculate the number of times one team is playing matches on the same day.
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
            if (sum != 0) {
                sum = sum / 2;
            }
            teamsPlayingMultipleMatchesSameDay = teamsPlayingMultipleMatchesSameDay + sum;
        }

        // Calculate the number of times team is playing against each other.
//        long teamsPlayingAgainstEachOtherLong = allMatches.stream()
//                .filter(x -> x.getMatch()[0] == x.getMatch()[1])
//                .count();
//        teamsPlayingAgainstEachOther = (int) teamsPlayingAgainstEachOtherLong;

        var conflicts = numberOfTimeSameMatchBeingPlayed + teamsPlayingMultipleMatchesSameDay + teamsPlayingAgainstEachOther;
        return conflicts;

//        int clashes;
//        int numberOfTimeSameMatchBeingPlayed;
//        int teamsPlayingMultipleMatchesSameDay = 0;
//        int teamsPlayingAgainstEachOther = 0;
//        List<MatchRound> seasonSchedule = this.getMatchRounds();
//
//        //Get all matches for a particular schedule
//        List<Match> allMatches = seasonSchedule.stream()
//                .flatMap(x -> x.getMatches().stream())
//                .collect(Collectors.toList());
//        // Calculate number of times same match being played
//        Map<Match, Long> matchesMap = allMatches.stream()
//                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//        numberOfTimeSameMatchBeingPlayed = matchesMap.values().stream()
//                .filter(x -> x.intValue() > 1)
//                .mapToInt(x -> x.intValue())
//                .sum();
//
//        // ? / 2
//        if (numberOfTimeSameMatchBeingPlayed != 0) {
//            numberOfTimeSameMatchBeingPlayed = numberOfTimeSameMatchBeingPlayed / 2;
//        }
//
//        //Calculate the number of time one team is playing matches on the same day.
//        for (MatchRound matchSchedule : seasonSchedule) {
//            Integer[] matchDayChromosome = matchSchedule.getMatches().stream()
//                    .flatMap(x -> Stream.of(x.getMatch()))
//                    .flatMap(x -> Stream.of(x.getTeamId()))
//                    .toArray(Integer[]::new);
//            Map<Integer, Long> teamPlayingMultipleTimesSameDayMap = Arrays.asList(matchDayChromosome).stream()
//                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//            int sum = teamPlayingMultipleTimesSameDayMap.values().stream()
//                    .filter(x -> x.intValue() > 1)
//                    .mapToInt(x -> x.intValue())
//                    .sum();
//            if (sum != 0) {
//                sum = sum / 2;
//            }
//            teamsPlayingMultipleMatchesSameDay = teamsPlayingMultipleMatchesSameDay + sum;
//        }
//        clashes = numberOfTimeSameMatchBeingPlayed + teamsPlayingMultipleMatchesSameDay + teamsPlayingAgainstEachOther;
//        return clashes;
    }
}
