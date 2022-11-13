package com.kbs.ga.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbs.ga.model.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllTeam() throws IOException {
        File file = new ClassPathResource("teams.json").getFile();
        String result = new String(Files.readAllBytes(file.toPath()));
        return result;
    }

    @PostMapping("/schedule")
    public List<MatchRound> generateSchedule(@RequestBody Map<String, Object> body) {
        Object object1 = body.get("teams");
        Object object2 = body.get("individual");
        ObjectMapper mapper = new ObjectMapper();
        List<Team> teams = mapper.convertValue(object1, new TypeReference<List<Team>>() {});
        Individual individual = mapper.convertValue(object2, new TypeReference<Individual>() {});
        Database database = new Database(teams);
        individual.createMatchRounds(database);
        return individual.getMatchRounds();
    }
}
