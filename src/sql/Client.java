/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public String getPassword(String user){
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521/orcl", "BD_ACS", "oracle")) {
            if (conn != null) {
                System.out.println("Connection à la base de données réussie");
                Statement stmt = conn.createStatement();
                String sql = "SELECT login_password from login where login_user = '" + user+"'";
                System.out.println(sql);
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    String password = rs.getString("login_password");
                    return password;
                }
            } else {
                System.out.println("Echec de la connection à la base de données!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }   
        return null;
    }/*
    public String getNom(String user){
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521/orcl", "BD_ACS", "oracle")) {
            if (conn != null) {
                System.out.println("Connection à la base de données réussie");
                Statement stmt = conn.createStatement();
                String sql = "SELECT client_id from login where login_user = '" + user+"'";
                System.out.println(sql);
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    String client_id = rs.getString("login_password");
                    return password;
                }
            } else {
                System.out.println("Echec de la connection à la base de données!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }   
        return null;
    }*/
    
}
