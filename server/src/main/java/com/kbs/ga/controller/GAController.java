package com.kbs.ga.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbs.ga.model.*;
import com.kbs.ga.service.GAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/ga")
public class GAController {

    @Autowired
    private GAService gaService;

    @PostMapping("/init")
    public Population initPopulation(@RequestBody List<Team> teams) {
        return gaService.initPopulation(teams);
    }

    @PostMapping("/exec")
    public List<Population> executeLogic(@RequestBody Map<String, Object> body) throws IOException {
        Object object1 = body.get("teams");
        Object object2 = body.get("population");
        ObjectMapper mapper = new ObjectMapper();
        List<Team> teams = mapper.convertValue(object1, new TypeReference<List<Team>>() {});
        Population population = mapper.convertValue(object2, new TypeReference<Population>() {});
        return gaService.executeLogic(teams, population);
    }

}
