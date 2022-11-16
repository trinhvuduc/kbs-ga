package com.kbs.ga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Population {
    private List<Individual> individuals;
    private double populationFitness = -1;

    public Population() {
        this.individuals = new ArrayList<>();
    }

    public Population(int populationSize, Database database) {
        this.individuals = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual(database);
            this.individuals.add(individual);
        }
    }

    public Population(List<Individual> individuals, double populationFitness) {
        this.individuals = individuals;
        this.populationFitness = populationFitness;
    }

    public Individual getIndividualByIndex(int index) {
        return individuals.get(index);
    }

    @JsonIgnore
    public Individual getEliteIndividual() {
        return getIndividualByIndex(0);
    }

    public void setIndividual(int populationIndex, Individual individual) {
        this.individuals.add(populationIndex, individual);
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Individual> population) {
        this.individuals = population;
    }

    public double getPopulationFitness() {
        return populationFitness;
    }

    public void calculateFitness() {
        double populationFitness = this.individuals.stream()
                .mapToDouble(x -> x.getFitness())
                .sum();
        this.setPopulationFitness(populationFitness);
    }

    public void setPopulationFitness(double populationFitness) {
        this.populationFitness = populationFitness;
    }

    @JsonIgnore
    public int getPopulationSize() {
        return this.individuals.size();
    }

    public void sortBasedOnFitness() {
        this.individuals = this.individuals.stream().sorted((x, y) -> {
            double xFitness = x.getFitness();
            double yFitness = y.getFitness();
            if (xFitness > yFitness) {
                return -1;
            } else if (xFitness < yFitness) {
                return 1;
            } else {
                return 0;
            }
        }).collect(Collectors.toList());
    }
}