package ru.gitadded.diary_bot.utility;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConverter {
    public static String formZonedTimeToSimpleString(ZonedDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd");
        return time.format(formatter);
    }
}
