package publications;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SubscriptionUtils {

    public static String calcMagEndDate(String startDate, int numberOfIssues, String frequency) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate endDate;

        switch (frequency.toLowerCase()) {
            case "monthly":
                endDate = start.plusMonths(numberOfIssues);
                break;
            case "weekly":
                endDate = start.plusWeeks(numberOfIssues);
                break;
            case "quarterly":
                endDate = start.plusMonths(numberOfIssues * 3);
                break;
            default:
                throw new IllegalArgumentException("Unknown frequency: " + frequency);
        }

        return endDate.toString();
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
        int daysPerMonth = 30; // Approximate number of days per month
        int totalDays = daysPerMonth * noOfMonths;

        return switch (subscriptionType.toLowerCase()) {
            case "7-day" -> basePrice * totalDays;
            case "weekday" -> basePrice * (totalDays * 5.0 / 7.0); // 5 weekdays out of 7 days
            case "weekend" -> basePrice * (totalDays * 2.0 / 7.0); // 2 weekend days out of 7 days
            default -> 0.0;
        };
    }

    public static double calcWeeklyNewsPrice(double basePrice, int noOfMonths) {
        return basePrice * noOfMonths;
    }
}