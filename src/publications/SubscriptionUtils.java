package publications;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SubscriptionUtils {

    public static String calcMagEndDate(String startDate, int numberOfIssues) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = start.plusMonths(numberOfIssues / 12);
        return end.format(DateTimeFormatter.ISO_DATE);
    }

    public static String calcNewsEndDate(String startDate, int noOfMonths) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = start.plusMonths(noOfMonths);
        return end.format(DateTimeFormatter.ISO_DATE);
    }

    public static double calcMagPrice(double basePrice, int noOfIssues) {
        if (noOfIssues == 12) {
            return basePrice * 12;
        } else if (noOfIssues == 24) {
            return basePrice * 24 * 0.9; // 10% discount for 24 issues
        } else {
            return basePrice * noOfIssues;
        }
    }


    public static double calcDailyNewsPrice(double basePrice, String subscriptionType, int noOfMonths) {
        return switch (subscriptionType.toLowerCase()) {
            case "7-day" -> basePrice * noOfMonths * 4; // Assuming 4 weeks per month
            case "weekday" -> basePrice * noOfMonths * 4 * 5 / 7; // 5 days out of 7
            case "weekend" -> basePrice * noOfMonths * 4 * 2 / 7; // 2 days out of 7
            default -> 0.0;
        };
    }

    public static double calcWeeklyNewsPrice(double basePrice, int noOfMonths) {
        return basePrice * noOfMonths;
    }
}