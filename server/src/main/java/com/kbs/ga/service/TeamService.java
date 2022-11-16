package com.kbs.ga.service;

import com.kbs.ga.model.Individual;
import com.kbs.ga.model.MatchRound;
import com.kbs.ga.model.Team;

import java.io.IOException;
import java.util.List;

public interface TeamService {
    String getAllTeam() throws IOException;
    List<MatchRound> generateSchedule(List<Team> teams, Individual individual);
}
