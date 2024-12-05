package publications;

import java.sql.*;

public class DBOps {
    /// Customer Operations
    public static void addCustomer(String name, String address) {
        String sql = "INSERT INTO CUSTOMER (Name, Address) VALUES (?, ?)";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.executeUpdate();
            System.out.println("Customer added successfully.");
            displayCustomer(getLastCustomerId());
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
        String sql = "DELETE FROM CUSTOMER WHERE CustomerID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Customer deleted successfully.");
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
            displayPub(getLastPubId());
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
    /// Magazine Subscription Operations
    public static void addMagSub(int customerId, int pubId, String startDate, int numberOfIssues) {
        String type = getPubType(pubId);
        if (!"MAGAZINE".equalsIgnoreCase(type)) {
            System.out.println("Error: PubID " + pubId + " is not a magazine.");
            return;
        }
        String endDate = SubscriptionUtils.calcMagEndDate(startDate, numberOfIssues);
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
    public static void displayMagSub(int customerID, int pubID){
        String sql = "SELECT ms.CustomerID, c.Name AS CustomerName, p.Name AS PublicationName, ms.StartDate, ms.NoOfIssues, ms.EndDate, ms.TotalPrice " +
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
                System.out.printf("%-15s %-20s %-20s %-12s %-12s %-12s %-12.2s%n", "CustomerID", "CustomerName", "PublicationName", "StartDate", "NoOfIssues", "EndDate", "TotalPrice");
                System.out.printf("%-15d %-20s %-20s %-12s %-12d %-12s %-12.2f%n",
                        rs.getInt("CustomerID"),
                        rs.getString("CustomerName"),
                        rs.getString("PublicationName"),
                        rs.getString("StartDate"),
                        rs.getInt("NoOfIssues"),
                        rs.getString("EndDate"),
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
        String sql = "SELECT p.Name AS PublicationName, c.Name AS CustomerName, ms.StartDate, ms.NoOfIssues, ms.EndDate, ms.TotalPrice " +
                "FROM MAGAZINE_SUBSCRIPTION ms " +
                "JOIN CUSTOMER c ON ms.CustomerID = c.CustomerID " +
                "JOIN PUBLICATION p ON ms.PubID = p.PubID";
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf("%-5s %-20s %-20s %-12s %-12s %-12s %-12s%n", "No", "PublicationName", "CustomerName", "StartDate", "NoOfIssues", "EndDate", "Price");
            int count = 1;
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-20s %-12s %-12d %-12s %-12.2f%n",
                        count++,
                        rs.getString("PublicationName"),
                        rs.getString("CustomerName"),
                        rs.getString("StartDate"),
                        rs.getInt("NoOfIssues"),
                        rs.getString("EndDate"),
                        rs.getDouble("TotalPrice"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Delete Magazine Subscription
    public static void deleteMagSub(int subNumber) {
        String sql = "SELECT row_num, CustomerID, PubID FROM (SELECT ROW_NUMBER() OVER (ORDER BY CustomerID, PubID) AS row_num, CustomerID, PubID FROM MAGAZINE_SUBSCRIPTION) WHERE row_num = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, subNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int customerId = rs.getInt("CustomerID");
                int pubId = rs.getInt("PubID");
                String deleteSql = "DELETE FROM MAGAZINE_SUBSCRIPTION WHERE CustomerID = ? AND PubID = ?";
                try (PreparedStatement deletePstmt = conn.prepareStatement(deleteSql)) {
                    deletePstmt.setInt(1, customerId);
                    deletePstmt.setInt(2, pubId);
                    deletePstmt.executeUpdate();
                    System.out.println("Magazine subscription deleted successfully.");
                }
            } else {
                System.out.println("Invalid subscription number.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Edit Magazine Subscription
    public static void editMagSub(int id, int customerId, String startDate, int numberOfIssues) {
        String endDate = SubscriptionUtils.calcMagEndDate(startDate, numberOfIssues);
        String sql = "UPDATE MAGAZINE_SUBSCRIPTION SET CustomerID = ?, StartDate = ?, NoOfIssues = ?, EndDate = ? WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.setString(2, startDate);
            pstmt.setInt(3, numberOfIssues);
            pstmt.setString(4, endDate);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            System.out.println("Magazine subscription updated successfully.");
            displayMagSub(customerId, id);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Get Magazine Subscription Start Date
    public static String getMagSubStartDate(int id) {
        String sql = "SELECT StartDate FROM MAGAZINE_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("StartDate");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Get Magazine Subscription Number of Issues
    public static int getMagNoOfIssues(int id) {
        String sql = "SELECT NoOfIssues FROM MAGAZINE_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("NoOfIssues");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }

    // Get Magazine Subscription Customer ID
    public static int getMagCustId(int id) {
        String sql = "SELECT CustomerID FROM MAGAZINE_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("CustomerID");
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
            displayNewsSub(customerId, pubId);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display Newspaper Subscription
    public static void displayNewsSub(int customerID, int pubID) {
        String sql = "SELECT ns.CustomerID, c.Name AS CustomerName, p.Name AS PublicationName, ns.StartDate, ns.NoOfMonths, ns.EndDate, ns.TotalPrice, ns.SubscriptionType " +
                "FROM NEWSPAPER_SUBSCRIPTION ns " +
                "JOIN CUSTOMER c ON ns.CustomerID = c.CustomerID " +
                "JOIN PUBLICATION p ON ns.PubID = p.PubID " +
                "WHERE ns.SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerID);
            pstmt.setInt(2, pubID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.printf("%-15s %-20s %-20s %-12s %-12s %-12s %-12s%n", "CustomerID", "CustomerName", "PublicationName", "StartDate", "NoOfMonths", "EndDate", "TotalPrice", "SubscriptionType");
                System.out.printf("%-15d %-20s %-20s %-12s %-12d %-12s %-12.2f %-12s%n",
                        rs.getInt("CustomerID"),
                        rs.getString("CustomerName"),
                        rs.getString("PublicationName"),
                        rs.getString("StartDate"),
                        rs.getInt("NoOfMonths"),
                        rs.getString("EndDate"),
                        rs.getDouble("TotalPrice"),
                        rs.getString("SubscriptionType"));
            } else {
                System.out.println("Subscription not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display All Newspaper Subscriptions
    public static void displayNewsSubs() {
        String sql = "SELECT p.Name AS PublicationName, c.Name AS CustomerName, ns.NoOfMonths, ns.StartDate, ns.EndDate, np.Price AS PricePerIssue, np.Frequency, ns.TotalPrice " +
                "FROM NEWSPAPER_SUBSCRIPTION ns " +
                "JOIN CUSTOMER c ON ns.CustomerID = c.CustomerID " +
                "JOIN PUBLICATION p ON ns.PubID = p.PubID " +
                "JOIN NEWSPUB np ON ns.PubID = np.PubID";
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf("%-5s %-20s %-20s %-12s %-12s %-12s %-15s %-10s %-12s%n", "No", "PublicationName", "CustomerName", "NoOfMonths", "StartDate", "EndDate", "PricePerIssue", "Frequency", "TotalPrice");
            int count = 1;
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-20s %-12d %-12s %-12s %-15.2f %-10s %-12.2f%n",
                        count++,
                        rs.getString("PublicationName"),
                        rs.getString("CustomerName"),
                        rs.getInt("NoOfMonths"),
                        rs.getString("StartDate"),
                        rs.getString("EndDate"),
                        rs.getDouble("PricePerIssue"),
                        rs.getString("Frequency"),
                        rs.getDouble("TotalPrice"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Delete Newspaper Subscription
    public static void deleteNewsSub(int subNumber) {
        String sql = "SELECT SubscriptionID FROM (SELECT ROW_NUMBER() OVER (ORDER BY ns.SubscriptionID) AS row_num, ns.SubscriptionID " +
                "FROM NEWSPAPER_SUBSCRIPTION ns) WHERE row_num = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, subNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int subscriptionId = rs.getInt("SubscriptionID");
                String deleteSql = "DELETE FROM NEWSPAPER_SUBSCRIPTION WHERE SubscriptionID = ?";
                try (PreparedStatement deletePstmt = conn.prepareStatement(deleteSql)) {
                    deletePstmt.setInt(1, subscriptionId);
                    deletePstmt.executeUpdate();
                    System.out.println("Newspaper subscription deleted successfully.");
                }
            } else {
                System.out.println("Invalid subscription number.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

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

    // Edit Newspaper Subscription
    public static void editNewsSubs(int id, int customerId, String startDate, int noOfMonths) {
        String endDate = SubscriptionUtils.calcNewsEndDate(startDate, noOfMonths);
        String sql = "UPDATE NEWSPAPER_SUBSCRIPTION SET CustomerID = ?, StartDate = ?, NoOfMonths = ?, EndDate = ? WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.setString(2, startDate);
            pstmt.setInt(3, noOfMonths);
            pstmt.setString(4, endDate);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            System.out.println("Newspaper subscription updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Get Newspaper Subscription Start Date
    public static String getNewsSubStartDate(int id) {
        String sql = "SELECT StartDate FROM NEWSPAPER_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("StartDate");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Get Newspaper Subscription No of Months
    public static int getNewsNoOfMonths(int id) {
        String sql = "SELECT NoOfMonths FROM NEWSPAPER_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("NoOfMonths");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }

    // Get Newspaper Subscription Customer ID
    public static int getNewsCustId(int id) {
        String sql = "SELECT CustomerID FROM NEWSPAPER_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("CustomerID");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }

    /// End of Newspaper Subscription Operations

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
