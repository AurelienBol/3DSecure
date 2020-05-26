package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class Client {
    Connection con; 
    public void setCon(Connection con){
        this.con = con;
    }
    public String getPassword(String user){
            if (con != null) {
                try {
                    System.out.println("Connection à la base de données réussie");
                    Statement stmt = con.createStatement();
                    String sql = "SELECT login_password from login where login_user = '" + user+"'";
                    System.out.println(sql);
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        String password = rs.getString("login_password");
                        return password;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Echec de la connection à la base de données!");
            } 
        return null;
    }
}
