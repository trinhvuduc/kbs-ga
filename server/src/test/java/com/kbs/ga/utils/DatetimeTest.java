package com.kbs.ga.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class DatetimeTest {

    public static void main(String[] args) {
        TemporalAdjuster nextSaturday = TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY);
        TemporalAdjuster nextSunday = TemporalAdjusters.next(DayOfWeek.SUNDAY);
        LocalDate date = LocalDate.now();
        LocalDateTime dateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0, 0);
        LocalDateTime firstTime = dateTime.withHour(20).with(nextSaturday);
        LocalDateTime secondTime = dateTime.withHour(22).with(nextSaturday);
        LocalDateTime thirdTime = dateTime.withHour(20).with(nextSunday);
        LocalDateTime fourthTime = dateTime.withHour(22).with(nextSunday);

        int numberOfMatches = 2;
        for (int i = 0; i < numberOfMatches / 2; i++) {
            if (i < (numberOfMatches - 4) / 2) System.out.println(firstTime);
            else System.out.println(secondTime);
        }
        for (int i = numberOfMatches / 2; i < numberOfMatches; i++) {
            if (i < (numberOfMatches + 4) / 2) System.out.println(thirdTime);
            else System.out.println(fourthTime);
        }
    }
}
