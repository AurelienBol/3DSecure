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
public class BankAccount {
    Connection con; 
    public void setCon(Connection con){
        this.con = con;
    }
    
    public boolean exist(String accountNumber){
            if (con != null) {
                try {
                    System.out.println("Vérification de l'existance du compte sur ACQ");
                    Statement stmt = con.createStatement();
                    String sql = "SELECT account_solde from account where account_number = '" + accountNumber+"'";
                    System.out.println(sql);
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        float montant = rs.getFloat("account_solde");
                        return true;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Echec de la connection à la base de données!");
            }

        return false;
    }
    
    public boolean gain(String accountNumber, double value){
            if (con != null) {
                try {
                    System.out.println("Transaction en cours sur ACQ");
                    PreparedStatement ps = con.prepareStatement("UPDATE account set account_solde = account_solde + " + value + " where account_number = '" + accountNumber+"'");;
                    ps.executeUpdate();
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Echec de la connection à la base de données!");
            }

        return false;
    }
}
