package com.kbs.ga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;

public class Match {
    private Team[] match;

    public Match(Team homeTeam, Team awayTeam) {
        this.match = new Team[2];
        this.match[0] = homeTeam;
        this.match[1] = awayTeam;
    }

    @JsonIgnore
    public Team[] getMatch() {
        return match;
    }

    public void setMatch(Team[] match) {
        this.match = match;
    }

    public Team getHomeTeam() {
        return this.match[0];
    }

    public Team getAwayTeam() {
        return this.match[1];
    }

    @Override
    public String toString() {
        return getHomeTeam().getId() + "-" + getAwayTeam().getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match1 = (Match) o;
        return Arrays.equals(getMatch(), match1.getMatch());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getMatch());
    }
}