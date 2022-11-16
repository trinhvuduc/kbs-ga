package com.kbs.ga.service;

import com.kbs.ga.model.Database;
import com.kbs.ga.model.Individual;
import com.kbs.ga.model.MatchRound;
import com.kbs.ga.model.Team;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    @Override
    public String getAllTeam() throws IOException {
        File file = new ClassPathResource("teams.json").getFile();
        String result = new String(Files.readAllBytes(file.toPath()));
        return result;
    }

    @Override
    public List<MatchRound> generateSchedule(List<Team> teams, Individual individual) {
        Database database = new Database(teams);
        individual.createMatchRounds(database);
        return individual.getMatchRounds();
    }
}
