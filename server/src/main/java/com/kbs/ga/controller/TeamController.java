package com.kbs.ga.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbs.ga.model.*;
import com.kbs.ga.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllTeam() throws IOException {
        return teamService.getAllTeam();
    }

    @PostMapping("/schedule")
    public List<MatchRound> generateSchedule(@RequestBody Map<String, Object> body) {
        Object object1 = body.get("teams");
        Object object2 = body.get("individual");
        ObjectMapper mapper = new ObjectMapper();
        List<Team> teams = mapper.convertValue(object1, new TypeReference<List<Team>>() {});
        Individual individual = mapper.convertValue(object2, new TypeReference<Individual>() {});
        return teamService.generateSchedule(teams, individual);
    }
}
