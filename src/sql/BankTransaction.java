package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class BankTransaction {
    public double getSolde(String accountNumber){
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@192.168.0.43:1521/orcl", "BD_ACS", "oracle")) {
            if (conn != null) {
                System.out.println("Vérification du solde sur ACS");
                Statement stmt = conn.createStatement();
                String sql = "SELECT account_solde from account where account_number = '" + accountNumber+"'";
                System.out.println(sql);
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    float montant = rs.getFloat("account_solde");
                    return montant;
                }
            } else {
                System.out.println("Echec de la connection à la base de données!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -99999;
    }
    
    public boolean pay(String accountNumber, double value){
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@192.168.0.43:1521/orcl", "BD_ACS", "oracle")) {
            if (conn != null) {
                System.out.println("Transaction en cours sur ACS");
                PreparedStatement ps = conn.prepareStatement("UPDATE account set account_solde = account_solde - " + value + " where account_number = '" + accountNumber+"'");;
                ps.executeUpdate();
                return true;
            } else {
                System.out.println("Echec de la connection à la base de données!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
