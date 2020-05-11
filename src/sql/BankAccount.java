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
public class BankAccount {
    public void connect(){
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@192.168.0.43:1521/orcl", "BD_ACQ", "oracle")) {
            if (conn != null) {
                System.out.println("Connection à la base de données réussie");
                
            } else {
                System.err.println("Echec de la connection à la base de données!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean exist(String accountNumber){
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@192.168.0.43:1521/orcl", "BD_ACQ", "oracle")) {
            if (conn != null) {
                System.out.println("Vérification de l'existance du compte sur ACQ");
                Statement stmt = conn.createStatement();
                String sql = "SELECT account_solde from account where account_number = '" + accountNumber+"'";
                System.out.println(sql);
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    float montant = rs.getFloat("account_solde");
                    return true;
                }
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
    
    public boolean gain(String accountNumber, double value){
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@192.168.0.43:1521/orcl", "BD_ACQ", "oracle")) {
            if (conn != null) {
                System.out.println("Transaction en cours sur ACQ");
                PreparedStatement ps = conn.prepareStatement("UPDATE account set account_solde = account_solde + " + value + " where account_number = '" + accountNumber+"'");;
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
