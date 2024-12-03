package publications;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SubscriptionUtils {

    public static String calculateMagazineEndDate(String startDate, int numberOfIssues) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = start.plusMonths(numberOfIssues / 12);
        return end.format(DateTimeFormatter.ISO_DATE);
    }

    public static String calculateNewspaperEndDate(String startDate, int noOfMonths) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = start.plusMonths(noOfMonths);
        return end.format(DateTimeFormatter.ISO_DATE);
    }
}