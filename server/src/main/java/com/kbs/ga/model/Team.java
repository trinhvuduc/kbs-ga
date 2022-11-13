package com.kbs.ga.model;

public class Team {
    private int id;
    private String name;
    private String shortName;
    private String logo;
    private String stadium;

    public Team() {
    }

    public Team(String name, String shortName, String logo, String stadium) {
        this.name = name;
        this.shortName = shortName;
        this.logo = logo;
        this.stadium = stadium;
    }

    public Team(int id, String name, String shortName, String logo, String stadium) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.logo = logo;
        this.stadium = stadium;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", logo='" + logo + '\'' +
                ", stadium='" + stadium + '\'' +
                '}';
    }
}
