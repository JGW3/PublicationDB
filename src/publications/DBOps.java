package publications;

import java.sql.*;

public class DBOps {
    /// Customer Operations
    public static void addCustomer(String name, String address) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Customer name cannot be empty.");
            return;
        }
        if (address == null || address.trim().isEmpty()) {
            System.out.println("Error: Customer address cannot be empty.");
            return;
        }
        
        String sql = "INSERT INTO CUSTOMER (Name, Address) VALUES (?, ?)";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name.trim());
            pstmt.setString(2, address.trim());
            pstmt.executeUpdate();
            System.out.println("Customer added successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Edit Customer
    public static void editCustomer(int customerId, String name, String address) {
        StringBuilder sql = new StringBuilder("UPDATE CUSTOMER SET ");
        boolean first = true;

        if (!"noChange".equals(name)) {
            sql.append("Name = ?");
            first = false;
        }
        if (!"noChange".equals(address)) {
            if (!first) {
                sql.append(", ");
            }
            sql.append("Address = ?");
        }
        sql.append(" WHERE CustomerID = ?");

        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (!"noChange".equals(name)) {
                pstmt.setString(paramIndex++, name);
            }
            if (!"noChange".equals(address)) {
                pstmt.setString(paramIndex++, address);
            }
            pstmt.setInt(paramIndex, customerId);
            pstmt.executeUpdate();
            System.out.println("Customer updated successfully.");
            displayCustomer(customerId);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Delete Customer
    public static void deleteCustomer(int id) {
        if (id <= 0) {
            System.out.println("Error: Invalid customer ID.");
            return;
        }
        
        String sql = "DELETE FROM CUSTOMER WHERE CustomerID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer deleted successfully.");
            } else {
                System.out.println("No customer found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display Customer
    public static void displayCustomer(int id) {
        String sql = "SELECT * FROM CUSTOMER WHERE CustomerID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-10s %-20s %-30s%n", "ID", "Name", "Address");
                System.out.printf("%-10d %-20s %-30s%n", rs.getInt("CustomerID"), rs.getString("Name"), rs.getString("Address"));
            } else {
                System.out.println("Customer not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display All Customers
    public static void displayCustomers() {
        String sql = "SELECT * FROM CUSTOMER";
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf("%-10s %-20s %-30s%n", "ID", "Name", "Address");
            while (rs.next()) {
                System.out.printf("%-10d %-20s %-30s%n", rs.getInt("CustomerID"), rs.getString("Name"), rs.getString("Address"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static int getLastCustomerId() {
        String sql = "SELECT CustomerID FROM CUSTOMER ORDER BY CustomerID DESC LIMIT 1";
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt("CustomerID");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }

    /// End of Customer Operations
    /// Publication Operations
    public static void addPub(String name, String type, String frequency, double price) {
        String pubSql = "INSERT INTO PUBLICATION (Name, Type) VALUES (?, ?)";
        String magSql = "INSERT INTO MAGAZINEPUB (PubID, Frequency, Price) VALUES (?, ?, ?)";
        String newsSql = "INSERT INTO NEWSPUB (PubID, Frequency, Price) VALUES (?, ?, ?)";

        try (Connection conn = DBConnect.connect();
             PreparedStatement pubStmt = conn.prepareStatement(pubSql, Statement.RETURN_GENERATED_KEYS)) {
            // Insert into PUBLICATION table
            pubStmt.setString(1, name);
            pubStmt.setString(2, type);
            pubStmt.executeUpdate();
            ResultSet rs = pubStmt.getGeneratedKeys();
            if (rs.next()) {
                int pubId = rs.getInt(1);

                // Insert into specific publication table
                if ("MAGAZINE".equalsIgnoreCase(type)) {
                    try (PreparedStatement magStmt = conn.prepareStatement(magSql)) {
                        magStmt.setInt(1, pubId);
                        magStmt.setString(2, frequency);
                        magStmt.setDouble(3, price);
                        magStmt.executeUpdate();
                    }
                } else if ("NEWSPAPER".equalsIgnoreCase(type)) {
                    try (PreparedStatement newsStmt = conn.prepareStatement(newsSql)) {
                        newsStmt.setInt(1, pubId);
                        newsStmt.setString(2, frequency);
                        newsStmt.setDouble(3, price);
                        newsStmt.executeUpdate();
                    }
                }
            }
            System.out.println("Publication added successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Edit Publication
    public static void editPub(int pubId, String name, String type, String frequency, double price) {
        String pubSql = "UPDATE PUBLICATION SET Name = ? WHERE PubID = ?";
        String magSql = "UPDATE MAGAZINEPUB SET Frequency = ?, Price = ? WHERE PubID = ?";
        String newsSql = "UPDATE NEWSPUB SET Frequency = ?, Price = ? WHERE PubID = ?";

        try (Connection conn = DBConnect.connect();
             PreparedStatement pubStmt = conn.prepareStatement(pubSql)) {
            // Update PUBLICATION table
            if (!"noChange".equals(name)) {
                pubStmt.setString(1, name);
                pubStmt.setInt(2, pubId);
                pubStmt.executeUpdate();
            }

            // Update specific publication table
            if ("MAGAZINE".equalsIgnoreCase(type)) {
                try (PreparedStatement magStmt = conn.prepareStatement(magSql)) {
                    if (!"noChange".equals(frequency)) {
                        magStmt.setString(1, frequency);
                    }
                    magStmt.setDouble(2, price);
                    magStmt.setInt(3, pubId);
                    magStmt.executeUpdate();
                }
            } else if ("NEWSPAPER".equalsIgnoreCase(type)) {
                try (PreparedStatement newsStmt = conn.prepareStatement(newsSql)) {
                    if (!"noChange".equals(frequency)) {
                        newsStmt.setString(1, frequency);
                    }
                    newsStmt.setDouble(2, price);
                    newsStmt.setInt(3, pubId);
                    newsStmt.executeUpdate();
                }
            }
            System.out.println("Publication updated successfully.");
            displayPub(pubId);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display Publication
    public static void displayPub(int id) {
        String pubSql = "SELECT p.PubID, p.Name, p.Type, m.Frequency AS MagFrequency, m.Price AS MagPrice, n.Frequency AS NewsFrequency, n.Price AS NewsPrice " +
                "FROM PUBLICATION p " +
                "LEFT JOIN MAGAZINEPUB m ON p.PubID = m.PubID " +
                "LEFT JOIN NEWSPUB n ON p.PubID = n.PubID " +
                "WHERE p.PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(pubSql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String frequency = rs.getString("MagFrequency") != null ? rs.getString("MagFrequency") : rs.getString("NewsFrequency");
                double price = rs.getDouble("MagPrice") != 0 ? rs.getDouble("MagPrice") : rs.getDouble("NewsPrice");
                System.out.printf("%-10s %-20s %-20s %-20s %-10s%n", "ID", "Name", "Type", "Frequency", "Price");
                System.out.printf("%-10d %-20s %-20s %-20s %-10.2f%n", rs.getInt("PubID"), rs.getString("Name"), rs.getString("Type"), frequency, price);
            } else {
                System.out.println("Publication not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display All Publications
    public static void displayPublications() {
        String pubSql = "SELECT p.PubID, p.Name, p.Type, m.Frequency AS MagFrequency, m.Price AS MagPrice, n.Frequency AS NewsFrequency, n.Price AS NewsPrice " +
                "FROM PUBLICATION p " +
                "LEFT JOIN MAGAZINEPUB m ON p.PubID = m.PubID " +
                "LEFT JOIN NEWSPUB n ON p.PubID = n.PubID";
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(pubSql)) {
            System.out.printf("%-10s %-20s %-20s %-20s %-10s%n", "ID", "Name", "Type", "Frequency", "Price");
            while (rs.next()) {
                String frequency = rs.getString("MagFrequency") != null ? rs.getString("MagFrequency") : rs.getString("NewsFrequency");
                double price = rs.getDouble("MagPrice") != 0 ? rs.getDouble("MagPrice") : rs.getDouble("NewsPrice");
                System.out.printf("%-10d %-20s %-20s %-20s %-10.2f%n", rs.getInt("PubID"), rs.getString("Name"), rs.getString("Type"), frequency, price);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //get publication type from publication id
    public static String getPubType(int id) {
        String sql = "SELECT Type FROM PUBLICATION WHERE PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("Type");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Delete Publication
    public static void deletePub(int id) {
        String sql = "DELETE FROM PUBLICATION WHERE PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Publication deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Get Publication Frequency
    public static String getPubFreq(int id) {
        String type = getPubType(id);
        String sql = null;

        if ("NEWSPAPER".equalsIgnoreCase(type)) {
            sql = "SELECT Frequency FROM NEWSPUB WHERE PubID = ?";
        } else if ("MAGAZINE".equalsIgnoreCase(type)) {
            sql = "SELECT Frequency FROM MAGAZINEPUB WHERE PubID = ?";
        }

        if (sql != null) {
            try (Connection conn = DBConnect.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("Frequency");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return null;
    }

    public static double getPubBasePrice(int id) {
        String type = getPubType(id);
        String sql = null;

        if ("NEWSPAPER".equalsIgnoreCase(type)) {
            sql = "SELECT Price FROM NEWSPUB WHERE PubID = ?";
        } else if ("MAGAZINE".equalsIgnoreCase(type)) {
            sql = "SELECT Price FROM MAGAZINEPUB WHERE PubID = ?";
        }

        if (sql != null) {
            try (Connection conn = DBConnect.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("Price");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return 0.0;
    }

    public static int getLastPubId() {
        String sql = "SELECT PubID FROM PUBLICATION ORDER BY PubID DESC LIMIT 1";
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt("PubID");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }

    /// End of Publication Operations
    /// Newspaper Subscription Operations
    public static void addNewsSub(int customerId, int pubId, String startDate, int noOfMonths, String endDate, double totalPrice, String subscriptionType) {
        String type = getPubType(pubId);
        if (!"NEWSPAPER".equalsIgnoreCase(type)) {
            System.out.println("Error: PubID " + pubId + " is not a newspaper.");
            return;
        }

        String sql = "INSERT INTO NEWSPAPER_SUBSCRIPTION (CustomerID, PubID, StartDate, NoOfMonths, EndDate, TotalPrice, SubscriptionType) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, pubId);
            pstmt.setString(3, startDate);
            pstmt.setInt(4, noOfMonths);
            pstmt.setString(5, endDate);
            pstmt.setDouble(6, totalPrice);
            pstmt.setString(7, subscriptionType);
            pstmt.executeUpdate();
            System.out.println("Newspaper subscription added successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display Newspaper Subscription
    public static void displayNewsSub(int customerID, int pubID) {
        String sql = "SELECT ns.CustomerID, ns.PubID, p.Name AS PublicationName, c.Name AS CustomerName, p.Type, np.Frequency, ns.NoOfMonths, ns.TotalPrice, ns.StartDate, ns.EndDate " +
                "FROM NEWSPAPER_SUBSCRIPTION ns " +
                "JOIN CUSTOMER c ON ns.CustomerID = c.CustomerID " +
                "JOIN PUBLICATION p ON ns.PubID = p.PubID " +
                "JOIN NEWSPUB np ON ns.PubID = np.PubID " +
                "WHERE ns.CustomerID = ? AND ns.PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerID);
            pstmt.setInt(2, pubID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-10s %-10s %-20s %-20s %-10s %-10s %-15s %-10s %-12s %-12s%n", "CustID", "PubID", "Newspaper", "Customer", "Type", "Frequency", "NoOfMonths", "TotalPrice", "StartDate", "EndDate");
                System.out.printf("%-10d %-10d %-20s %-20s %-10s %-10s %-15d %-10.2f %-12s %-12s%n",
                        rs.getInt("CustomerID"),
                        rs.getInt("PubID"),
                        rs.getString("PublicationName"),
                        rs.getString("CustomerName"),
                        rs.getString("Type"),
                        rs.getString("Frequency"),
                        rs.getInt("NoOfMonths"),
                        rs.getDouble("TotalPrice"),
                        rs.getString("StartDate"),
                        rs.getString("EndDate"));
            } else {
                System.out.println("Subscription not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display All Newspaper Subscriptions
    public static void displayNewsSubscriptions() {
        String sql = "SELECT ns.CustomerID, ns.PubID, p.Name AS PublicationName, c.Name AS CustomerName, p.Type, np.Frequency, ns.NoOfMonths, ns.TotalPrice, ns.StartDate, ns.EndDate " +
                "FROM NEWSPAPER_SUBSCRIPTION ns " +
                "JOIN CUSTOMER c ON ns.CustomerID = c.CustomerID " +
                "JOIN PUBLICATION p ON ns.PubID = p.PubID " +
                "JOIN NEWSPUB np ON ns.PubID = np.PubID";
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf("%-10s %-10s %-20s %-20s %-10s %-10s %-15s %-10s %-12s %-12s%n", "CustID", "PubID", "Newspaper", "Customer", "Type", "Frequency", "NoOfMonths", "TotalPrice", "StartDate", "EndDate");
            while (rs.next()) {
                System.out.printf("%-10d %-10d %-20s %-20s %-10s %-10s %-15d %-10.2f %-12s %-12s%n",
                        rs.getInt("CustomerID"),
                        rs.getInt("PubID"),
                        rs.getString("PublicationName"),
                        rs.getString("CustomerName"),
                        rs.getString("Type"),
                        rs.getString("Frequency"),
                        rs.getInt("NoOfMonths"),
                        rs.getDouble("TotalPrice"),
                        rs.getString("StartDate"),
                        rs.getString("EndDate"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Delete Newspaper Subscription
    public static void deleteNewsSub(int custID, int pubID) {
        String sql = "DELETE FROM NEWSPAPER_SUBSCRIPTION WHERE CustomerID = ? AND PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, custID);
            pstmt.setInt(2, pubID);
            pstmt.executeUpdate();
            System.out.println("Newspaper subscription deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Edit Newspaper Subscription
    public static void editNewsSub(int custID, int pubID, String startDate, int noOfMonths, String endDate, double totalPrice, String subscriptionType) {
        String sql = "UPDATE NEWSPAPER_SUBSCRIPTION SET StartDate = ?, NoOfMonths = ?, EndDate = ?, TotalPrice = ?, SubscriptionType = ? WHERE CustomerID = ? AND PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, startDate);
            pstmt.setInt(2, noOfMonths);
            pstmt.setString(3, endDate);
            pstmt.setDouble(4, totalPrice);
            pstmt.setString(5, subscriptionType);
            pstmt.setInt(6, custID);
            pstmt.setInt(7, pubID);
            pstmt.executeUpdate();
            System.out.println("Newspaper subscription updated successfully.");
            displayNewsSub(custID, pubID);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Get Newspaper Subscription Start Date
    public static String getNewsSubStartDate(int custID, int pubID) {
        String sql = "SELECT StartDate FROM NEWSPAPER_SUBSCRIPTION WHERE CustomerID = ? AND PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, custID);
            pstmt.setInt(2, pubID);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("StartDate");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Get Newspaper Subscription No of Months
    public static int getNewsNoOfMonths(int custID, int pubID) {
        String sql = "SELECT NoOfMonths FROM NEWSPAPER_SUBSCRIPTION WHERE CustomerID = ? AND PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, custID);
            pstmt.setInt(2, pubID);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("NoOfMonths");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }

    public static String getNewsSubType(int custID, int pubID) {
        String sql = "SELECT SubscriptionType FROM NEWSPAPER_SUBSCRIPTION WHERE CustomerID = ? AND PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, custID);
            pstmt.setInt(2, pubID);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("SubscriptionType");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    //Display all Newspapers
    public static void displayNewspapers() {
        String pubSql = "SELECT p.PubID, p.Name, p.Type, n.Frequency AS NewsFrequency, n.Price AS NewsPrice " +
                "FROM PUBLICATION p " +
                "JOIN NEWSPUB n ON p.PubID = n.PubID";
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(pubSql)) {
            System.out.printf("%-10s %-20s %-20s %-20s %-10s%n", "ID", "Name", "Type", "Frequency", "Price");
            while (rs.next()) {
                System.out.printf("%-10d %-20s %-20s %-20s %-10.2f%n", rs.getInt("PubID"), rs.getString("Name"), rs.getString("Type"), rs.getString("NewsFrequency"), rs.getDouble("NewsPrice"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /// End of Newspaper Subscription Operations
    /// Magazine Subscription Operations
    public static void addMagSub(int customerId, int pubId, String startDate, int numberOfIssues) {
        String type = getPubType(pubId);
        if (!"MAGAZINE".equalsIgnoreCase(type)) {
            System.out.println("Error: PubID " + pubId + " is not a magazine.");
            return;
        }
        String frequency = getPubFreq(pubId);
        String endDate = SubscriptionUtils.calcMagEndDate(startDate, numberOfIssues, frequency);
        double basePrice = getPubBasePrice(pubId);
        double price = SubscriptionUtils.calcMagPrice(basePrice, numberOfIssues);
        String sql = "INSERT INTO MAGAZINE_SUBSCRIPTION (CustomerID, PubID, StartDate, NoOfIssues, EndDate, TotalPrice) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, pubId);
            pstmt.setString(3, startDate);
            pstmt.setInt(4, numberOfIssues);
            pstmt.setString(5, endDate);
            pstmt.setDouble(6, price);
            pstmt.executeUpdate();
            System.out.println("Magazine subscription added successfully.");
            displayMagSub(customerId, pubId);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display Magazine Subscription
    public static void displayMagSub(int customerID, int pubID) {
        String sql = "SELECT ms.CustomerID, ms.PubID, p.Name AS PublicationName, c.Name AS CustomerName, ms.StartDate, ms.EndDate, ms.NoOfIssues, ms.TotalPrice " +
                "FROM MAGAZINE_SUBSCRIPTION ms " +
                "JOIN CUSTOMER c ON ms.CustomerID = c.CustomerID " +
                "JOIN PUBLICATION p ON ms.PubID = p.PubID " +
                "WHERE ms.CustomerID = ? AND ms.PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerID);
            pstmt.setInt(2, pubID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-10s %-10s %-20s %-20s %-12s %-12s %-12s %-12s%n", "CustID", "PubID", "PublicationName", "CustomerName", "StartDate", "EndDate", "NoOfIssues", "TotalPrice");
                System.out.printf("%-10d %-10d %-20s %-20s %-12s %-12s %-12d %-12.2f%n",
                        rs.getInt("CustomerID"),
                        rs.getInt("PubID"),
                        rs.getString("PublicationName"),
                        rs.getString("CustomerName"),
                        rs.getString("StartDate"),
                        rs.getString("EndDate"),
                        rs.getInt("NoOfIssues"),
                        rs.getDouble("TotalPrice"));
            } else {
                System.out.println("Subscription not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    // Display All Magazine Subscriptions
    public static void displayMagSubscriptions() {
        String sql = "SELECT ms.CustomerID, ms.PubID, p.Name AS PublicationName, c.Name AS CustomerName, ms.StartDate, ms.EndDate, ms.NoOfIssues, ms.TotalPrice " +
                "FROM MAGAZINE_SUBSCRIPTION ms " +
                "JOIN CUSTOMER c ON ms.CustomerID = c.CustomerID " +
                "JOIN PUBLICATION p ON ms.PubID = p.PubID";
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf("%-10s %-10s %-20s %-20s %-12s %-12s %-12s %-12s%n", "CustID", "PubID", "PublicationName", "CustomerName", "StartDate", "EndDate", "NoOfIssues", "TotalPrice");
            while (rs.next()) {
                System.out.printf("%-10d %-10d %-20s %-20s %-12s %-12s %-12d %-12.2f%n",
                        rs.getInt("CustomerID"),
                        rs.getInt("PubID"),
                        rs.getString("PublicationName"),
                        rs.getString("CustomerName"),
                        rs.getString("StartDate"),
                        rs.getString("EndDate"),
                        rs.getInt("NoOfIssues"),
                        rs.getDouble("TotalPrice"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Delete Magazine Subscription
    public static void deleteMagSub(int custID, int pubID) {
        String sql = "DELETE FROM MAGAZINE_SUBSCRIPTION WHERE CustomerID = ? AND PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, custID);
            pstmt.setInt(2, pubID);
            pstmt.executeUpdate();
            System.out.println("Magazine subscription deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Edit Magazine Subscription
    public static void editMagSub(int custID, int pubID, String startDate, int numberOfIssues) {
        String sql = "UPDATE MAGAZINE_SUBSCRIPTION SET StartDate = ?, NoOfIssues = ?, EndDate = ?, TotalPrice = ? WHERE CustomerID = ? AND PubID = ?";
        try (Connection conn = DBConnect.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String endDate = SubscriptionUtils.calcMagEndDate(startDate, numberOfIssues, getPubFreq(pubID));
            double basePrice = getPubBasePrice(pubID);
            double price = SubscriptionUtils.calcMagPrice(basePrice, numberOfIssues);
            pstmt.setString(1, startDate);
            pstmt.setInt(2, numberOfIssues);
            pstmt.setString(3, endDate);
            pstmt.setDouble(4, price);
            pstmt.setInt(5, custID);
            pstmt.setInt(6, pubID);
            pstmt.executeUpdate();
            System.out.println("Magazine subscription updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Get Magazine Subscription Start Date
    public static String getMagSubStartDate(int custID, int pubID) {
        String sql = "SELECT StartDate FROM MAGAZINE_SUBSCRIPTION WHERE CustomerID = ? AND PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, custID);
            pstmt.setInt(2, pubID);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("StartDate");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Get Magazine Subscription Number of Issues
    public static int getMagNoOfIssues(int custID, int pubID) {
        String sql = "SELECT NoOfIssues FROM MAGAZINE_SUBSCRIPTION WHERE CustomerID = ? AND PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, custID);
            pstmt.setInt(2, pubID);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("NoOfIssues");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }

    //Display all Magazines
    public static void displayMagazines() {
        String pubSql = "SELECT p.PubID, p.Name, p.Type, m.Frequency AS MagFrequency, m.Price AS MagPrice " +
                "FROM PUBLICATION p " +
                "JOIN MAGAZINEPUB m ON p.PubID = m.PubID";
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(pubSql)) {
            System.out.printf("%-10s %-20s %-20s %-20s %-10s%n", "ID", "Name", "Type", "Frequency", "Price");
            while (rs.next()) {
                System.out.printf("%-10d %-20s %-20s %-20s %-10.2f%n", rs.getInt("PubID"), rs.getString("Name"), rs.getString("Type"), rs.getString("MagFrequency"), rs.getDouble("MagPrice"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /// End of Magazine Subscription Operations

    public static void clearAllTables() {
        String[] tables = {
                "CUSTOMER",
                "MAGAZINE_SUBSCRIPTION",
                "NEWSPAPER_SUBSCRIPTION",
                "PUBLICATION",
                "MAGAZINEPUB",
                "NEWSPUB",
                "sqlite_sequence"};
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement()) {
            for (String table : tables) {
                stmt.executeUpdate("DELETE FROM " + table);
            }
            System.out.println("All tables cleared successfully.");
        } catch (SQLException e) {
            System.out.println("Error clearing tables: " + e.getMessage());
        }
    }
}
