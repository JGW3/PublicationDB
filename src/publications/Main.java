package publications;

import java.util.Scanner;

public class Main {

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
            System.out.println("6. Exit");
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
                case 6 -> {
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
            System.out.println("5. Return to Main Menu");
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
                    if (scanner.hasNextLine()) {
                        String name = scanner.nextLine();
                        if (name.isEmpty()) {
                            name = DBOps.getCustomerName(customerId);
                        }
                        System.out.print("Enter new address or press Enter to keep address: ");
                        String address = scanner.nextLine();
                        if (address.isEmpty()) {
                            address = DBOps.getCustomerAddress(customerId);
                        }
                        DBOps.editCustomer(customerId, name, address);
                    }
                }
                // Delete Customer
                case 4 -> {
                    DBOps.displayCustomers();
                    System.out.print("Enter customer ID to delete: ");
                    int customerId = Integer.parseInt(scanner.nextLine());
                    System.out.println("Are you sure you want to delete this customer? (Y/N)");
                    if (scanner.nextLine().equalsIgnoreCase("Y")) DBOps.deleteCustomer(customerId);
                }
                case 5 -> {
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
            System.out.println("5. Return to Main Menu");
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
                case 2 -> {
                    System.out.print("Enter publication name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter publication type (MAGAZINE or NEWSPAPER): ");
                    String type = scanner.nextLine();
                    System.out.print("Enter publication frequency (daily, weekly, monthly, quarterly): ");
                    String frequency = scanner.nextLine();
                    DBOps.addPub(name, type, frequency);
                }
                // Edit Publication
                case 3 -> {
                    DBOps.displayPublications();
                    System.out.print("Enter publication ID to edit: ");
                    int pubId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter new name or press Enter to keep name: ");
                    if (scanner.hasNextLine()) {
                        String name = scanner.nextLine();
                        if (name.isEmpty()) {
                            name = DBOps.getPubName(pubId);
                        }
                        System.out.print("Enter new type or press Enter to keep type: ");
                        String type = scanner.nextLine();
                        if (type.isEmpty()) {
                            type = DBOps.getPubType(pubId);
                        }
                        System.out.print("Enter new frequency or press Enter to keep frequency: ");
                        String frequency = scanner.nextLine();
                        if (frequency.isEmpty()) {
                            frequency = DBOps.getPubFreq(pubId);
                        }
                        DBOps.editPub(pubId, name, type, frequency);
                    }
                }
                // Delete Publication
                case 4 -> {
                    DBOps.displayPublications();
                    System.out.print("Enter publication ID to delete: ");
                    int pubId = Integer.parseInt(scanner.nextLine());
                    System.out.println("Are you sure you want to delete this publication? (Y/N)");
                    if (scanner.nextLine().equalsIgnoreCase("Y")) DBOps.deletePub(pubId);
                }
                case 5 -> {
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
            System.out.println("5. Return to Main Menu");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                // View All Subscriptions
                case 1 -> {
                    DBOps.displayNewsSubscriptions();
                }
                case 2 -> {
                    System.out.print("Enter customer ID: ");
                    int customerId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter publication ID: ");
                    int pubId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter subscription start date (YYYY-MM-DD): ");
                    String startDate = scanner.nextLine();
                    System.out.print("Enter number of months: ");
                    int noOfMonths = Integer.parseInt(scanner.nextLine());
                    DBOps.addNewsSub(customerId, pubId, startDate, noOfMonths);
                }
                // Edit Subscription
                case 3 -> {
                   DBOps.displayNewsSubscriptions();
                    System.out.print("Enter subscription ID to edit: ");
                    int subId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter new customer ID or press Enter to keep customer ID: ");
                    if (scanner.hasNextLine()) {
                        int customerId = Integer.parseInt(scanner.nextLine());
                        if (customerId == 0) {
                            customerId = DBOps.getNewsCustId(subId);
                        }
                        System.out.print("Enter new start date or press Enter to keep start date: ");
                        String startDate = scanner.nextLine();
                        if (startDate.isEmpty()) {
                            startDate = DBOps.getNewsSubStartDate(subId);
                        }
                        System.out.print("Enter new number of months or press Enter to keep number of months: ");
                        int noOfMonths = Integer.parseInt(scanner.nextLine());
                        if (noOfMonths == 0) {
                            noOfMonths = DBOps.getNewsNoOfMonths(subId);
                        }
                        DBOps.editNewsSubs(subId, customerId, startDate, noOfMonths);
                    }
                }
                // Delete Subscription
                case 4 -> {
                    DBOps.displayNewsSubscriptions();
                    System.out.print("Enter subscription ID to delete: ");
                    int subId = Integer.parseInt(scanner.nextLine());
                    System.out.println("Are you sure you want to delete this subscription? (Y/N)");
                    if (scanner.nextLine().equalsIgnoreCase("Y")) DBOps.deleteNewsSub(subId);
                }
                case 5 -> {
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
            System.out.println("5. Return to Main Menu");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                // View All Subscriptions
                case 1 -> {
                    DBOps.displayMagSubscriptions();
                }
                case 2 -> {
                    System.out.print("Enter customer ID: ");
                    int customerId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter publication ID: ");
                    int pubId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter subscription start date (YYYY-MM-DD): ");
                    String startDate = scanner.nextLine();
                    System.out.print("Enter number of issues: ");
                    int numberOfIssues = Integer.parseInt(scanner.nextLine());
                    DBOps.addMagSub(customerId, pubId, startDate, numberOfIssues);
                }
                // Edit Subscription
                case 3 -> {
                    DBOps.displayMagSubscriptions();
                    System.out.print("Enter subscription ID to edit: ");
                    int subId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter new customer ID or press Enter to keep customer ID: ");
                    if (scanner.hasNextLine()) {
                        int customerId = Integer.parseInt(scanner.nextLine());
                        if (customerId == 0) {
                            customerId = DBOps.getMagCustId(subId);
                        }
                        System.out.print("Enter new start date or press Enter to keep start date: ");
                        String startDate = scanner.nextLine();
                        if (startDate.isEmpty()) {
                            startDate = DBOps.getMagSubStartDate(subId);
                        }
                        System.out.print("Enter new number of issues or press Enter to keep number of issues: ");
                        int numberOfIssues = Integer.parseInt(scanner.nextLine());
                        if (numberOfIssues == 0) {
                            numberOfIssues = DBOps.getMagNoOfIssues(subId);
                        }
                        DBOps.editMagSub(subId, customerId, startDate, numberOfIssues);
                    }
                }
                // Delete Subscription
                case 4 -> {
                    DBOps.displayMagSubscriptions();
                    System.out.print("Enter subscription ID to delete: ");
                    int subId = Integer.parseInt(scanner.nextLine());
                    System.out.println("Are you sure you want to delete this subscription? (Y/N)");
                    if (scanner.nextLine().equalsIgnoreCase("Y")) DBOps.deleteMagSub(subId);
                }
                case 5 -> {
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
            System.out.println("3. Return to Main Menu");
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
                case 3 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}