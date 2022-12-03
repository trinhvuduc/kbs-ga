package com.kbs.ga.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
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

    public void generateTime(int plusWeek) {
        TemporalAdjuster nextSaturday = TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY);
        TemporalAdjuster nextSunday = TemporalAdjusters.next(DayOfWeek.SUNDAY);
        LocalDate date = LocalDate.now();
        date = date.plusDays(7 * plusWeek);
        LocalDateTime dateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0, 0);
        LocalDateTime firstTime = dateTime.withHour(20).with(nextSaturday);
        LocalDateTime secondTime = dateTime.withHour(22).with(nextSaturday);
        LocalDateTime thirdTime = dateTime.withHour(20).with(nextSunday);
        LocalDateTime fourthTime = dateTime.withHour(22).with(nextSunday);

        int numberOfMatches = this.matches.size();
        for (int i = 0; i < numberOfMatches / 2; i++) {
            if (i < (numberOfMatches - 4) / 2) this.matches.get(i).setTime(firstTime);
            else this.matches.get(i).setTime(secondTime);
        }
        for (int i = numberOfMatches / 2; i < numberOfMatches; i++) {
            if (i < (numberOfMatches + 4) / 2) this.matches.get(i).setTime(thirdTime);
            else this.matches.get(i).setTime(fourthTime);
        }
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
