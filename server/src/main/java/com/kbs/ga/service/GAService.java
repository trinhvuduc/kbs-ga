package com.kbs.ga.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kbs.ga.model.Population;
import com.kbs.ga.model.Team;

import java.io.IOException;
import java.util.List;

public interface GAService {
    Population initPopulation(List<Team> teams);
    List<Population> executeLogic(List<Team> teams, Population population) throws IOException;
}
