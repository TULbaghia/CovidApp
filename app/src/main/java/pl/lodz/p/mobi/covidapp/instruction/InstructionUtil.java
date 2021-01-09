package pl.lodz.p.mobi.covidapp.instruction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class InstructionUtil {

    public static String formatDate(String rawDate) {
        LocalDateTime dateTime = LocalDateTime.parse(rawDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StringBuilder sb = new StringBuilder();
        sb.append(dateTime.getDayOfMonth())
                .append(" ")
                .append(dateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("pl-PL")))
                .append(", ")
                .append(formatNumber(dateTime.getHour()))
                .append(":")
                .append(formatNumber(dateTime.getMinute()));
        return sb.toString();
    }

    private static String formatNumber(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return String.valueOf(value);
    }
}
