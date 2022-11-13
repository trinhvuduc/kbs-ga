package com.kbs.ga.model;

import java.util.ArrayList;
import java.util.List;

public class MatchRound {
    private List<Match> matches;

    public MatchRound() {
        this.matches = new ArrayList<>();
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public void addMatch(Match match) {
        this.matches.add(match);
    }

    @Override
    public String toString() {
        String output = "[";
        for (int i = 0; i < matches.size(); i++) {
            output += matches.get(i).toString() + ",";
        }
        output += "]";
        return output;
    }
}
