package com.hotel.ReservationComponent.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SeasonCalendar {
    private static SeasonCalendar instance;
    private final List<LocalDate> holidays;

    private SeasonCalendar(){
        holidays = new ArrayList<>();
        holidays.add(LocalDate.of(2026,1,1));
        holidays.add(LocalDate.of(2026,3,22));
        holidays.add(LocalDate.of(2026,5,9));
        holidays.add(LocalDate.of(2026,7,6));
        holidays.add(LocalDate.of(2026,8,30));
        holidays.add(LocalDate.of(2026,12,16));
    }

    public static synchronized SeasonCalendar getInstance(){
        if (instance == null){
            instance = new SeasonCalendar();
        }
        return instance;
    }

    public boolean isHighSeason(LocalDate date){
        int month=date.getMonthValue();
        return holidays.contains(date)||(month>=6&&month<=8);
    }
}
