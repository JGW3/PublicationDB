package publications;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static final String TODAY_DATE = LocalDate.now().toString();

    private static int getValidInteger(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static double getValidDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value < 0) {
                    System.out.println("Price cannot be negative. Please enter a valid price.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static String getValidAddress(Scanner scanner) {
        while (true) {
            System.out.print("Enter customer address: ");
            String address = scanner.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("Address cannot be empty.");
                continue;
            }
            if (address.length() > 40) {
                System.out.println("Error: Address cannot be longer than 40 characters.");
                continue;
            }
            return address;
        }
    }

    private static boolean confirmAction(Scanner scanner, String message) {
        System.out.println(message + " (Y/N)");
        String response = scanner.nextLine().trim();
        return response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("YES");
    }

    public static void main(String[] args) {
        DBLoader.preloadDatabase(); // Preload database

        DBConnect.testConnection();
        Scanner scanner = new Scanner(System.in);
        /// Main menu  ///
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Customers");
            System.out.println("2. Publications");
            System.out.println("3. Newspaper Subscriptions");
            System.out.println("4. Magazine Subscriptions");
            System.out.println("5. Database Operations");
            System.out.println("0. Exit");

            int choice = getValidInteger(scanner, "Choose an option: ");

            switch (choice) {
                case 1 -> customerOperations(scanner);
                case 2 -> publicationOperations(scanner);
                case 3 -> newspaperSubscriptionOperations(scanner);
                case 4 -> magazineSubscriptionOperations(scanner);
                case 5 -> databaseOperations(scanner);
                case 0 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void customerOperations(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Customer Operations ===");
            System.out.println("1. View All Customers");
            System.out.println("2. Add Customer");
            System.out.println("3. Edit Customer");
            System.out.println("4. Delete Customer");
            System.out.println("0. Return to Main Menu");

            int choice = getValidInteger(scanner, "Choose an option: ");

            switch (choice) {
                // View All Customers
                case 1 -> DBOps.displayCustomers();
                // Add Customer
                case 2 -> {
                    System.out.print("Enter customer name: ");
                    String name = scanner.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Customer name cannot be empty.");
                        break;
                    }
                    String address = getValidAddress(scanner);
                    DBOps.addCustomer(name, address);
                    DBOps.displayCustomer(DBOps.getLastCustomerId());
                }
                // Edit Customer
                case 3 -> {
                    DBOps.displayCustomers();
                    int customerId = getValidInteger(scanner, "Enter customer ID to edit: ");
                    if (customerId <= 0) {
                        System.out.println("Invalid customer ID.");
                        break;
                    }
                    System.out.print("Enter new name or press Enter to keep name: ");
                    String name = scanner.nextLine();
                    if (name.isEmpty()) {
                        name = "noChange";
                    }
                    System.out.print("Enter new address or press Enter to keep address: ");
                    String address = scanner.nextLine();
                    if (address.isEmpty()) {
                        address = "noChange";
                    }
                    DBOps.editCustomer(customerId, name, address);
                }
                // Delete Customer
                case 4 -> {
                    DBOps.displayCustomers();
                    int customerId = getValidInteger(scanner, "Enter customer ID to delete: ");
                    if (customerId <= 0) {
                        System.out.println("Invalid customer ID.");
                        break;
                    }
                    if (confirmAction(scanner, "Are you sure you want to delete this customer?")) {
                        DBOps.deleteCustomer(customerId);
                    }
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void publicationOperations(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Publications ===");
            System.out.println("1. View All Publications");
            System.out.println("2. Add Publication");
            System.out.println("3. Edit Publication");
            System.out.println("4. Delete Publication");
            System.out.println("0. Return to Main Menu");

            int choice = getValidInteger(scanner, "Choose an option: ");

            switch (choice) {
                case 1 -> DBOps.displayPublications();
                // Add Publication with options for Magazine or Newspaper
                case 2 -> {
                    System.out.print("Enter publication name: ");
                    String name = scanner.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Publication name cannot be empty.");
                        break;
                    }
                    int typeChoice = getValidInteger(scanner, "Publication type - 1 for Newspaper, 2 for Magazine: ");
                    if (typeChoice != 1 && typeChoice != 2) {
                        System.out.println("Invalid choice. Please select 1 or 2.");
                        break;
                    }
                    String type = (typeChoice == 1) ? "NEWSPAPER" : "MAGAZINE";

                    String frequency = null;
                    if ("NEWSPAPER".equalsIgnoreCase(type)) {
                        int freqChoice = getValidInteger(scanner, "Enter publication frequency (1 for daily, 2 for weekly): ");
                        switch (freqChoice) {
                            case 1 -> frequency = "daily";
                            case 2 -> frequency = "weekly";
                            default -> {
                                System.out.println("Invalid choice. Defaulting to daily.");
                                frequency = "daily";
                            }
                        }
                    } else {
                        int freqChoice = getValidInteger(scanner, "Enter publication frequency (1 for weekly, 2 for monthly, 3 for quarterly): ");
                        switch (freqChoice) {
                            case 1 -> frequency = "weekly";
                            case 2 -> frequency = "monthly";
                            case 3 -> frequency = "quarterly";
                            default -> {
                                System.out.println("Invalid choice. Defaulting to weekly.");
                                frequency = "weekly";
                            }
                        }
                    }

                    double price = getValidDouble(scanner, "Enter publication price: ");
                    DBOps.addPub(name, type, frequency, price);
                    DBOps.displayPub(DBOps.getLastPubId());
                }

                // Edit Publication
                case 3 -> {
                    DBOps.displayPublications();
                    int pubId = getValidInteger(scanner, "Enter publication ID to edit: ");
                    if (pubId <= 0) {
                        System.out.println("Invalid publication ID.");
                        break;
                    }
                    System.out.print("Enter new name or press Enter to keep name: ");
                    String name = scanner.nextLine();
                    if (name.isEmpty()) {
                        name = "noChange";
                    }
                    System.out.print("Enter new type or press Enter to keep type: ");
                    String type = scanner.nextLine();
                    if (type.isEmpty()) {
                        type = "noChange";
                    }
                    System.out.print("Enter new frequency or press Enter to keep frequency: ");
                    String frequency = scanner.nextLine();
                    if (frequency.isEmpty()) {
                        frequency = "noChange";
                    }
                    System.out.print("Enter new price or press Enter to keep price: ");
                    String priceInput = scanner.nextLine();
                    double price = -1;
                    if (!priceInput.isEmpty()) {
                        try {
                            price = Double.parseDouble(priceInput);
                            if (price < 0) {
                                System.out.println("Price cannot be negative. Keeping current price.");
                                price = -1;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid price format. Keeping current price.");
                            price = -1;
                        }
                    }
                    DBOps.editPub(pubId, name, type, frequency, price);
                }
                // Delete Publication
                case 4 -> {
                    DBOps.displayPublications();
                    int pubId = getValidInteger(scanner, "Enter publication ID to delete: ");
                    if (pubId <= 0) {
                        System.out.println("Invalid publication ID.");
                        break;
                    }
                    if (confirmAction(scanner, "Are you sure you want to delete this publication?")) {
                        DBOps.deletePub(pubId);
                    }
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void newspaperSubscriptionOperations(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Newspaper Subscriptions ===");
            System.out.println("1. View All Subscriptions");
            System.out.println("2. Add Subscription");
            System.out.println("3. Edit Subscription");
            System.out.println("4. Delete Subscription");
            System.out.println("5. View All Customers");
            System.out.println("6. View All Newspapers");
            System.out.println("0. Return to Main Menu");

            int choice = getValidInteger(scanner, "Choose an option: ");

            switch (choice) {
                case 1 -> DBOps.displayNewsSubscriptions();
                case 2 -> {
                    int customerId = getValidInteger(scanner, "Enter customer ID: ");
                    if (customerId <= 0) {
                        System.out.println("Customer ID must be a positive number.");
                        break;
                    }
                    int pubId = getValidInteger(scanner, "Enter publication ID: ");
                    if (pubId <= 0) {
                        System.out.println("Publication ID must be a positive number.");
                        break;
                    }
                    System.out.print("Enter subscription start date (YYYY-MM-DD) or press ENTER for today " + TODAY_DATE + ": ");
                    String startDate = scanner.nextLine();
                    if (startDate.isEmpty()) {
                        startDate = TODAY_DATE;
                    }
                    int noOfMonths = getValidInteger(scanner, "Enter number of months: ");
                    if (noOfMonths <= 0) {
                        System.out.println("Number of months must be a positive number.");
                        break;
                    }

                    String frequency = DBOps.getPubFreq(pubId);
                    double basePrice = DBOps.getPubBasePrice(pubId);
                    double totalPrice = 0.0;
                    String endDate = SubscriptionUtils.calcNewsEndDate(startDate, noOfMonths);
                    String subscriptionType = null;

                    if ("daily".equalsIgnoreCase(frequency)) {
                        int subTypeChoice = getValidInteger(scanner, "Subscription Type (1 for 7-day, 2 for Weekday only, 3 for Weekend only): ");
                        subscriptionType = switch (subTypeChoice) {
                            case 1 -> "7-day";
                            case 2 -> "Weekday";
                            case 3 -> "Weekend";
                            default -> {
                                System.out.println("Invalid choice. Defaulting to 7-day.");
                                yield "7-day";
                            }
                        };
                        totalPrice = SubscriptionUtils.calcDailyNewsPrice(basePrice, subscriptionType, noOfMonths);
                    } else if ("weekly".equalsIgnoreCase(frequency)) {
                        totalPrice = SubscriptionUtils.calcWeeklyNewsPrice(basePrice, noOfMonths);
                    }

                    DBOps.addNewsSub(customerId, pubId, startDate, noOfMonths, endDate, totalPrice, subscriptionType);
                    DBOps.displayNewsSub(customerId, pubId);
                }
                case 3 -> {
                    DBOps.displayNewsSubscriptions();
                    int customerId = getValidInteger(scanner, "Enter CustomerID of the entry to edit: ");
                    if (customerId <= 0) {
                        System.out.println("Customer ID must be a positive number.");
                        break;
                    }
                    int pubId = getValidInteger(scanner, "Enter PublicationID of the entry to edit: ");
                    if (pubId <= 0) {
                        System.out.println("Publication ID must be a positive number.");
                        break;
                    }
                    DBOps.displayNewsSub(customerId, pubId);

                    System.out.print("Enter new start date or press Enter to keep start date: ");
                    String startDate = scanner.nextLine();
                    if (startDate.isEmpty()) {
                        startDate = DBOps.getNewsSubStartDate(customerId, pubId);
                    }
                    System.out.print("Enter new number of months or press Enter to keep number of months: ");
                    String monthsInput = scanner.nextLine().trim();
                    int noOfMonths;
                    if (monthsInput.isEmpty()) {
                        noOfMonths = DBOps.getNewsNoOfMonths(customerId, pubId);
                    } else {
                        try {
                            noOfMonths = Integer.parseInt(monthsInput);
                            if (noOfMonths <= 0) {
                                System.out.println("Number of months must be a positive number. Keeping current value.");
                                noOfMonths = DBOps.getNewsNoOfMonths(customerId, pubId);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Keeping current number of months.");
                            noOfMonths = DBOps.getNewsNoOfMonths(customerId, pubId);
                        }
                    }
                    String frequency = DBOps.getPubFreq(pubId);
                    double basePrice = DBOps.getPubBasePrice(pubId);
                    double totalPrice = 0.0;
                    String endDate = SubscriptionUtils.calcNewsEndDate(startDate, noOfMonths);
                    String subscriptionType = null;

                    if ("daily".equalsIgnoreCase(frequency)) {
                        System.out.print("Subscription Type (1 for 7-day, 2 for Weekday only, 3 for Weekend only) or press Enter to keep subscription type: ");
                        String subTypeInput = scanner.nextLine().trim();
                        int subTypeChoice;
                        if (subTypeInput.isEmpty()) {
                            subTypeChoice = 0; // Keep current
                        } else {
                            try {
                                subTypeChoice = Integer.parseInt(subTypeInput);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Keeping current subscription type.");
                                subTypeChoice = 0; // Keep current
                            }
                        }
                        subscriptionType = switch (subTypeChoice) {
                            case 1 -> "7-day";
                            case 2 -> "Weekday";
                            case 3 -> "Weekend";
                            case 0 -> DBOps.getNewsSubType(customerId, pubId); // Keep current
                            default -> {
                                System.out.println("Invalid choice. Keeping current subscription type.");
                                yield DBOps.getNewsSubType(customerId, pubId);
                            }
                        };
                        totalPrice = SubscriptionUtils.calcDailyNewsPrice(basePrice, subscriptionType, noOfMonths);
                    } else if ("weekly".equalsIgnoreCase(frequency)) {
                        totalPrice = SubscriptionUtils.calcWeeklyNewsPrice(basePrice, noOfMonths);
                    }
                    DBOps.editNewsSub(customerId, pubId, startDate, noOfMonths, endDate, totalPrice, subscriptionType);
                }


                case 4 -> {
                    DBOps.displayNewsSubscriptions();
                    int custID = getValidInteger(scanner, "Enter CustomerID of subscription to delete: ");
                    if (custID <= 0) {
                        System.out.println("Customer ID must be a positive number.");
                        break;
                    }
                    int pubID = getValidInteger(scanner, "Enter PublicationID of subscription to delete: ");
                    if (pubID <= 0) {
                        System.out.println("Publication ID must be a positive number.");
                        break;
                    }
                    DBOps.displayNewsSub(custID, pubID);
                    if (confirmAction(scanner, "Are you sure you want to delete this subscription?")) {
                        DBOps.deleteNewsSub(custID, pubID);
                    }
                }
                case 5 -> DBOps.displayCustomers();
                case 6 -> DBOps.displayNewspapers();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void magazineSubscriptionOperations(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Magazine Subscriptions ===");
            System.out.println("1. View All Subscriptions");
            System.out.println("2. Add Subscription");
            System.out.println("3. Edit Subscription");
            System.out.println("4. Delete Subscription");
            System.out.println("5. View All Customers");
            System.out.println("6. View All Magazines");
            System.out.println("0. Return to Main Menu");

            int choice = getValidInteger(scanner, "Choose an option: ");

            switch (choice) {
                case 1 -> DBOps.displayMagSubscriptions();
                case 2 -> {
                    int customerId = getValidInteger(scanner, "Enter customer ID: ");
                    if (customerId <= 0) {
                        System.out.println("Customer ID must be a positive number.");
                        break;
                    }
                    int pubId = getValidInteger(scanner, "Enter publication ID: ");
                    if (pubId <= 0) {
                        System.out.println("Publication ID must be a positive number.");
                        break;
                    }
                    System.out.print("Enter subscription start date (YYYY-MM-DD) or press ENTER for today " + TODAY_DATE + ": ");
                    String startDate = scanner.nextLine();
                    if (startDate.isEmpty()) {
                        startDate = TODAY_DATE;
                    }
                    int numberOfIssues = getValidInteger(scanner, "Enter number of issues: ");
                    if (numberOfIssues <= 0) {
                        System.out.println("Number of issues must be a positive number.");
                        break;
                    }
                    DBOps.addMagSub(customerId, pubId, startDate, numberOfIssues);
                    DBOps.displayMagSub(customerId, pubId);
                }
                case 3 -> {
                    DBOps.displayMagSubscriptions();
                    int customerId = getValidInteger(scanner, "Enter CustomerID of the entry to edit: ");
                    if (customerId <= 0) {
                        System.out.println("Customer ID must be a positive number.");
                        break;
                    }
                    int pubId = getValidInteger(scanner, "Enter PublicationID of the entry to edit: ");
                    if (pubId <= 0) {
                        System.out.println("Publication ID must be a positive number.");
                        break;
                    }
                    DBOps.displayMagSub(customerId, pubId);
                    System.out.print("Enter new start date or press Enter to keep start date: ");
                    String startDate = scanner.nextLine();
                    if (startDate.isEmpty()) {
                        startDate = DBOps.getMagSubStartDate(customerId, pubId);
                    }
                    System.out.print("Enter new number of issues or press Enter to keep number of issues: ");
                    String issuesInput = scanner.nextLine().trim();
                    int numberOfIssues;
                    if (issuesInput.isEmpty()) {
                        numberOfIssues = DBOps.getMagNoOfIssues(customerId, pubId);
                    } else {
                        try {
                            numberOfIssues = Integer.parseInt(issuesInput);
                            if (numberOfIssues <= 0) {
                                System.out.println("Number of issues must be a positive number. Keeping current value.");
                                numberOfIssues = DBOps.getMagNoOfIssues(customerId, pubId);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Keeping current number of issues.");
                            numberOfIssues = DBOps.getMagNoOfIssues(customerId, pubId);
                        }
                    }
                    DBOps.editMagSub(customerId, pubId, startDate, numberOfIssues);
                }

                case 4 -> {
                    DBOps.displayMagSubscriptions();
                    int custID = getValidInteger(scanner, "Enter CustomerID of subscription to delete: ");
                    if (custID <= 0) {
                        System.out.println("Customer ID must be a positive number.");
                        break;
                    }
                    int pubID = getValidInteger(scanner, "Enter PublicationID of subscription to delete: ");
                    if (pubID <= 0) {
                        System.out.println("Publication ID must be a positive number.");
                        break;
                    }
                    DBOps.displayMagSub(custID, pubID);
                    if (confirmAction(scanner, "Are you sure you want to delete this subscription?")) {
                        DBOps.deleteMagSub(custID, pubID);
                    }
                }
                case 5 -> DBOps.displayCustomers();
                case 6 -> DBOps.displayMagazines();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void databaseOperations(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Database Operations ===");
            System.out.println("1. Clear All Tables");
            System.out.println("2. Reload Database");
            System.out.println("0. Return to Main Menu");

            int choice = getValidInteger(scanner, "Choose an option: ");

            switch (choice) {
                case 1 -> {
                    if (confirmAction(scanner, "Are you sure you want to clear all tables? This will delete all data.")) {
                        DBOps.clearAllTables();
                    }
                }
                case 2 -> DBLoader.preloadDatabase();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}