package publications;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBLoader {

    public static void preloadDatabase(){
        if(isDatabaseLoaded()){
            System.out.println("Database is preloaded");
            return;
        }

        System.out.println("Preloading database...");
        loadFromFile("src/preload.txt");
    }

    public static void loadFromFile(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
           String line;

           // Start processing each line
           System.out.println("Processing file...");
           while((line = br.readLine()) != null){
                line = line.trim();

                if (line.isEmpty()){
                    System.out.println("Skipping line: " + line);
                    continue;
                }

                if (line.startsWith("# Customers")){
                    System.out.println("Processing customers...");
                    processCustomers(br);
                }
                else if (line.startsWith("# Publications")){
                    processPublications(br);
                }

            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void processCustomers(BufferedReader br) throws Exception{
        String line;
        while((line = br.readLine()) != null){
            line = line.trim();

            // Stop processing when we reach the next section
            if (line.isEmpty() || line.startsWith("#")){
                break;
            }
            String[] data = line.split("\\|");
            String name = data[0].trim();
            String address = data[1].trim();
            DBOps.addCustomer(name, address);
        }
    }

    private static void processPublications(BufferedReader br) throws Exception {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();

            // Stop processing when we reach the next section
            if (line.isEmpty() || line.startsWith("#")) {
                break;
            }
            String[] data = line.split("\\|");
            String name = data[0].trim();
            String type = data[1].trim();
            String frequency = data[2].trim();
            double price = Double.parseDouble(data[3].trim());

            DBOps.addPub(name, type, frequency, price);
        }
    }

    private static boolean isDatabaseLoaded(){
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement()) {
            // Check if there's data in the CUSTOMER table
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM CUSTOMER");
            if (rs.next() && rs.getInt("count") > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error checking database: " + e.getMessage());
        }
        return false;
    }
}
