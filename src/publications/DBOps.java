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
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Edit Customer
    public static void editCustomer(int id, String name, String address) {
        String sql = "UPDATE CUSTOMER SET Name = ?, Address = ? WHERE IdNo = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            System.out.println("Customer updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Delete Customer
    public static void deleteCustomer(int id) {
        String sql = "DELETE FROM CUSTOMER WHERE IdNo = ?";
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
        String sql = "SELECT * FROM CUSTOMER WHERE IdNo = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("ID\tName\tAddress");
            while (rs.next()) {
                System.out.println(rs.getInt("IdNo") + "\t" + rs.getString("Name") + "\t" + rs.getString("Address"));
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
            System.out.println("ID\tName\tAddress");
            while (rs.next()) {
                System.out.println(rs.getInt("IdNo") + "\t" + rs.getString("Name") + "\t" + rs.getString("Address"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Get Customer Name
    public static String getCustomerName(int id) {
        String sql = "SELECT Name FROM CUSTOMER WHERE IdNo = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("Name");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Get Customer Address
    public static String getCustomerAddress(int id) {
        String sql = "SELECT Address FROM CUSTOMER WHERE IdNo = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("Address");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    /// End of Customer Operations
    /// Publication Operations
    public static void addPub(String name, String type, String frequency) {
        String sql = "INSERT INTO PUBLICATION (Name, Type, Frequency) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setString(3, frequency);
            pstmt.executeUpdate();
            System.out.println("Publication added successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Edit Publication
    public static void editPub(int id, String name, String type, String frequency) {
        String sql = "UPDATE PUBLICATION SET Name = ?, Type = ?, Frequency = ? WHERE PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setString(3, frequency);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Publication updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display Publication
    public static void displayPub(int id) {
        String sql = "SELECT * FROM PUBLICATION WHERE PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("ID\tName\tType\tFrequency");
            while (rs.next()) {
                System.out.println(rs.getInt("PubID") + "\t" + rs.getString("Name") + "\t" + rs.getString("Type") + "\t" + rs.getString("Frequency"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display All Publications
    public static void displayPublications() {
        String sql = "SELECT * FROM PUBLICATION";
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("ID\tName\tType\tFrequency");
            while (rs.next()) {
                System.out.println(rs.getInt("PubID") + "\t" + rs.getString("Name") + "\t" + rs.getString("Type") + "\t" + rs.getString("Frequency"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
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

    // Get Publication Name
    public static String getPubName(int id) {
        String sql = "SELECT Name FROM PUBLICATION WHERE PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("Name");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Get Publication Type
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

    // Get Publication Frequency
    public static String getPubFreq(int id) {
        String sql = "SELECT Frequency FROM PUBLICATION WHERE PubID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("Frequency");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    /// End of Publication Operations
    /// Magazine Subscription Operations
    public static void addMagSub(int customerId, int pubId, String startDate, int numberOfIssues) {
        String endDate = SubscriptionUtils.calculateMagazineEndDate(startDate, numberOfIssues);
        String sql = "INSERT INTO MAGAZINE_SUBSCRIPTION (CustomerID, PubID, StartDate, NumberOfIssues, EndDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, pubId);
            pstmt.setString(3, startDate);
            pstmt.setInt(4, numberOfIssues);
            pstmt.setString(5, endDate);
            pstmt.executeUpdate();
            System.out.println("Magazine subscription added successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display Magazine Subscription
    public static void displayMagSub(int id) {
        String sql = "SELECT * FROM MAGAZINE_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("ID\tCustomerID\tPubID\tStartDate\tNumberOfIssues\tEndDate");
            while (rs.next()) {
                System.out.println(rs.getInt("SubscriptionID") + "\t" + rs.getInt("CustomerID") + "\t" + rs.getInt("PubID") + "\t" + rs.getString("StartDate") + "\t" + rs.getInt("NumberOfIssues") + "\t" + rs.getString("EndDate"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display All Magazine Subscriptions
    public static void displayMagSubscriptions() {
        String sql = "SELECT * FROM MAGAZINE_SUBSCRIPTION";
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("ID\tCustomerID\tPubID\tStartDate\tNumberOfIssues\tEndDate");
            while (rs.next()) {
                System.out.println(rs.getInt("SubscriptionID") + "\t" + rs.getInt("CustomerID") + "\t" + rs.getInt("PubID") + "\t" + rs.getString("StartDate") + "\t" + rs.getInt("NumberOfIssues") + "\t" + rs.getString("EndDate"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Delete Magazine Subscription
    public static void deleteMagSub(int id) {
        String sql = "DELETE FROM MAGAZINE_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Magazine subscription deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Edit Magazine Subscription
    public static void editMagSub(int id, int customerId, String startDate, int numberOfIssues) {
        String endDate = SubscriptionUtils.calculateMagazineEndDate(startDate, numberOfIssues);
        String sql = "UPDATE MAGAZINE_SUBSCRIPTION SET CustomerID = ?, StartDate = ?, NumberOfIssues = ?, EndDate = ? WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.setString(2, startDate);
            pstmt.setInt(3, numberOfIssues);
            pstmt.setString(4, endDate);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            System.out.println("Magazine subscription updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Get Magazine Subscription End Date
    public static String getMagSubEndDate(int id) {
        String sql = "SELECT EndDate FROM MAGAZINE_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("EndDate");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
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
        String sql = "SELECT NumberOfIssues FROM MAGAZINE_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("NumberOfIssues");
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

/// End of Magazine Subscription Operations
/// Newspaper Subscription Operations
    public static void addNewsSub(int customerId, int pubId, String startDate, int noOfMonths) {
        String endDate = SubscriptionUtils.calculateNewspaperEndDate(startDate, noOfMonths);
        String sql = "INSERT INTO NEWSPAPER_SUBSCRIPTION (CustomerID, PubID, StartDate, NoOfMonths, EndDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, pubId);
            pstmt.setString(3, startDate);
            pstmt.setInt(4, noOfMonths);
            pstmt.setString(5, endDate);
            pstmt.executeUpdate();
            System.out.println("Newspaper subscription added successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display Newspaper Subscription
    public static void displayNewsSub(int id) {
        String sql = "SELECT * FROM NEWSPAPER_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("ID\tCustomerID\tPubID\tStartDate\tNoOfMonths\tEndDate");
            while (rs.next()) {
                System.out.println(rs.getInt("SubscriptionID") + "\t" + rs.getInt("CustomerID") + "\t" + rs.getInt("PubID") + "\t" + rs.getString("StartDate") + "\t" + rs.getInt("NoOfMonths") + "\t" + rs.getString("EndDate"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Display All Newspaper Subscriptions
    public static void displayNewsSubscriptions() {
        String sql = "SELECT * FROM NEWSPAPER_SUBSCRIPTION";
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("ID\tCustomerID\tPubID\tStartDate\tNoOfMonths\tEndDate");
            while (rs.next()) {
                System.out.println(rs.getInt("SubscriptionID") + "\t" + rs.getInt("CustomerID") + "\t" + rs.getInt("PubID") + "\t" + rs.getString("StartDate") + "\t" + rs.getInt("NoOfMonths") + "\t" + rs.getString("EndDate"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Delete Newspaper Subscription
    public static void deleteNewsSub(int id) {
        String sql = "DELETE FROM NEWSPAPER_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Newspaper subscription deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Edit Newspaper Subscription
    public static void editNewsSubs(int id, int customerId, String startDate, int noOfMonths) {
        String endDate = SubscriptionUtils.calculateNewspaperEndDate(startDate, noOfMonths);
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

    // Get Newspaper Subscription End Date
    public static String getNewsSubEndDate(int id) {
        String sql = "SELECT EndDate FROM NEWSPAPER_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("EndDate");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
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

    // Get Newspaper Subscription Publication ID
    public static int getNewspaperPubId(int id) {
        String sql = "SELECT PubID FROM NEWSPAPER_SUBSCRIPTION WHERE SubscriptionID = ?";
        try (Connection conn = DBConnect.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("PubID");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }

    /// End of Newspaper Subscription Operations


    public static void clearAllTables() {
        String[] tables = {"CUSTOMER", "MAGAZINE_SUBSCRIPTION", "NEWSPAPER_SUBSCRIPTION", "PUBLICATION", "sqlite_sequence"};
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
