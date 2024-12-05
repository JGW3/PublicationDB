package publications;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static String todayDate = LocalDate.now().toString();
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
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

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
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                // View All Customers
                case 1 -> DBOps.displayCustomers();
                // Add Customer
                case 2 -> {
                    System.out.print("Enter customer name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter customer address: ");
                    String address = scanner.nextLine();
                    if (address.length() > 40) {
                        System.out.println("Error: Address cannot be longer than 40 characters.\n" +
                                "Enter customer address: ");
                        address = scanner.nextLine();
                    } else {
                        DBOps.addCustomer(name, address);
                    }
                }
                // Edit Customer
                case 3 -> {
                    DBOps.displayCustomers();
                    System.out.print("Enter customer ID to edit: ");
                    int customerId = Integer.parseInt(scanner.nextLine());
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
                    System.out.print("Enter customer ID to delete: ");
                    int customerId = Integer.parseInt(scanner.nextLine());
                    System.out.println("Are you sure you want to delete this customer? (Y/N)");
                    if (scanner.nextLine().equalsIgnoreCase("Y")) DBOps.deleteCustomer(customerId);
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
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> DBOps.displayPublications();
                // Add Publication with options for Magazine or Newspaper
                case 2 -> {
                    System.out.print("Enter publication name: ");
                    String name = scanner.nextLine();
                    System.out.print("Publication type - 1 for Newspaper, 2 for Magazine: ");
                    int typeChoice = Integer.parseInt(scanner.nextLine());
                    String type = (typeChoice == 1) ? "NEWSPAPER" : "MAGAZINE";

                    String frequency = null;
                    if ("NEWSPAPER".equalsIgnoreCase(type)) {
                        System.out.print("Enter publication frequency (1 for daily, 2 for weekly): ");
                        int freqChoice = Integer.parseInt(scanner.nextLine());
                        switch (freqChoice) {
                            case 1 -> frequency = "daily";
                            case 2 -> frequency = "weekly";
                            default -> {
                                System.out.println("Invalid choice. Defaulting to daily.");
                                frequency = "daily";
                            }
                        }
                    } else {
                        System.out.print("Enter publication frequency (1 for weekly, 2 for monthly, 3 for quarterly): ");
                        int freqChoice = Integer.parseInt(scanner.nextLine());
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

                    System.out.print("Enter publication price: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    DBOps.addPub(name, type, frequency, price);
                }

                // Edit Publication
                case 3 -> {
                    DBOps.displayPublications();
                    System.out.print("Enter publication ID to edit: ");
                    int pubId = Integer.parseInt(scanner.nextLine());
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
                    double price = priceInput.isEmpty() ? -1 : Double.parseDouble(priceInput);
                    DBOps.editPub(pubId, name, type, frequency, price);
                }
                // Delete Publication
                case 4 -> {
                    DBOps.displayPublications();
                    System.out.print("Enter publication ID to delete: ");
                    int pubId = Integer.parseInt(scanner.nextLine());
                    System.out.println("Are you sure you want to delete this publication? (Y/N)");
                    if (scanner.nextLine().equalsIgnoreCase("Y")) DBOps.deletePub(pubId);
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
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> DBOps.displayNewsSubs();
                case 2 -> {
                    System.out.print("Enter customer ID: ");
                    int customerId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter publication ID: ");
                    int pubId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter subscription start date (YYYY-MM-DD) or press ENTER for today " + todayDate + ": ");
                    String startDate = scanner.nextLine();
                    System.out.print("Enter number of months: ");
                    int noOfMonths = Integer.parseInt(scanner.nextLine());

                    String frequency = DBOps.getPubFreq(pubId);
                    double basePrice = DBOps.getPubBasePrice(pubId);
                    double totalPrice = 0.0;
                    String endDate = SubscriptionUtils.calcNewsEndDate(startDate, noOfMonths);
                    String subscriptionType = null;

                    if ("daily".equalsIgnoreCase(frequency)) {
                        System.out.print("Subscription Type (1 for 7-day, 2 for Weekday only, 3 for Weekend only): ");
                        int subTypeChoice = Integer.parseInt(scanner.nextLine());
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
                }
                case 3 -> {
                    DBOps.displayNewsSubs();
                    System.out.print("Enter subscription number to edit: ");
                    int subNumber = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter new customer ID or press Enter to keep customer ID: ");
                    if (scanner.hasNextLine()) {
                        int customerId = Integer.parseInt(scanner.nextLine());
                        if (customerId == 0) {
                            customerId = DBOps.getNewsCustId(subNumber);
                        }
                        System.out.print("Enter new start date or press Enter to keep start date: ");
                        String startDate = scanner.nextLine();
                        if (startDate.isEmpty()) {
                            startDate = DBOps.getNewsSubStartDate(subNumber);
                        }
                        System.out.print("Enter new number of months or press Enter to keep number of months: ");
                        int noOfMonths = Integer.parseInt(scanner.nextLine());
                        if (noOfMonths == 0) {
                            noOfMonths = DBOps.getNewsNoOfMonths(subNumber);
                        }
                        DBOps.editNewsSubs(subNumber, customerId, startDate, noOfMonths);
                    }
                }
                case 4 -> {
                    DBOps.displayNewsSubs();
                    System.out.print("Enter subscription number to delete: ");
                    int subNumber = Integer.parseInt(scanner.nextLine());
                    System.out.println("Are you sure you want to delete this subscription? (Y/N)");
                    if (scanner.nextLine().equalsIgnoreCase("Y")) DBOps.deleteNewsSub(subNumber);
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
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> DBOps.displayMagSubscriptions();
                case 2 -> {
                    System.out.print("Enter customer ID: ");
                    int customerId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter publication ID: ");
                    int pubId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter subscription start date (YYYY-MM-DD) or press ENTER for today " + todayDate + ": ");
                    String startDate = scanner.nextLine();
                    System.out.print("Enter number of issues: ");
                    int numberOfIssues = Integer.parseInt(scanner.nextLine());
                    DBOps.addMagSub(customerId, pubId, startDate, numberOfIssues);
                }
                case 3 -> {
                    DBOps.displayMagSubscriptions();
                    System.out.print("Enter subscription number to edit: ");
                    int subNumber = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter new customer ID or press Enter to keep customer ID: ");
                    if (scanner.hasNextLine()) {
                        int customerId = Integer.parseInt(scanner.nextLine());
                        if (customerId == 0) {
                            customerId = DBOps.getMagCustId(subNumber);
                        }
                        System.out.print("Enter new start date or press Enter to keep start date: ");
                        String startDate = scanner.nextLine();
                        if (startDate.isEmpty()) {
                            startDate = DBOps.getMagSubStartDate(subNumber);
                        }
                        System.out.print("Enter new number of issues or press Enter to keep number of issues: ");
                        int numberOfIssues = Integer.parseInt(scanner.nextLine());
                        if (numberOfIssues == 0) {
                            numberOfIssues = DBOps.getMagNoOfIssues(subNumber);
                        }
                        DBOps.editMagSub(subNumber, customerId, startDate, numberOfIssues);
                    }
                }
                case 4 -> {
                    DBOps.displayMagSubscriptions();
                    System.out.print("Enter subscription number to delete: ");
                    int subNumber = Integer.parseInt(scanner.nextLine());
                    System.out.println("Are you sure you want to delete this subscription? (Y/N)");
                    if (scanner.nextLine().equalsIgnoreCase("Y")) DBOps.deleteMagSub(subNumber);
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
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> DBOps.clearAllTables();
                case 2 -> DBLoader.preloadDatabase();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}