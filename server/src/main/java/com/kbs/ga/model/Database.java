package com.kbs.ga.model;

import java.util.*;

public class Database {
    private HashMap<Integer, Team> teams;

    public Database() {
    }

    public Database(List<Team> list) {
        this.teams = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            Team team = list.get(i);
            team.setId(i + 1);
            this.teams.put(team.getId(), team);
        }
    }

    public HashMap<Integer, Team> getTeams() {
        return teams;
    }

    public void setTeams(HashMap<Integer, Team> teams) {
        this.teams = teams;
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

}
