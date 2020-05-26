package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class BankTransaction {
    Connection con; 
    public void setCon(Connection con){
        this.con = con;
    }
    public double getSolde(String accountNumber){
            if (con != null) {
                try {
                    System.out.println("Vérification du solde sur ACS");
                    Statement stmt = con.createStatement();
                    String sql = "SELECT account_solde from account where account_number = '" + accountNumber+"'";
                    System.out.println(sql);
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        float montant = rs.getFloat("account_solde");
                        return montant;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(BankTransaction.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Echec de la connection à la base de données!");
            }
        return -99999;
    }
    
    public boolean pay(String accountNumber, double value){
            if (con != null) {
                try {
                    System.out.println("Transaction en cours sur ACS");
                    PreparedStatement ps = con.prepareStatement("UPDATE account set account_solde = account_solde - " + value + " where account_number = '" + accountNumber+"'");;
                    ps.executeUpdate();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(BankTransaction.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Echec de la connection à la base de données!");
            }
        return false;
    }
}
