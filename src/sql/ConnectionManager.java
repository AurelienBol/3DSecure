package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class ConnectionManager {
    private String driverName;
    private String username;
    private String password;
    private Connection con;
    private String urlstring;
    
    public void setDriverName(String driverName){
        this.driverName = driverName;
    }
    public String getDriverName(){
        return driverName;
    }
    
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
    
    public void setPassword(String password){
        this.password=password;
    }
    public String getPassword(){
        return password;
    }
    
    public void setUrlstring(String urlstring){
        this.urlstring =urlstring;
    }
    public String getUrlstring(){
        return urlstring;
    }
    
    public ConnectionManager(String driverName, String urlstring, String username, String password){
        setDriverName(driverName);
        setUrlstring(urlstring);
        setUsername(username);
        setPassword(password);
    }
    
    public Connection getConnection(){
        if(con!=null) return con;
        try{
            Class.forName(driverName);
            try{
                con = DriverManager.getConnection(urlstring,username,password);
            } catch (SQLException ex) {
                System.err.println("[ConnectionManager : getConnection] SQLException - " +ex);
            }
        } catch (ClassNotFoundException ex) {
            System.err.println("[ConnectionManager : getConnection] ClassNotFoundException - " +ex);
        }
        return con;
    }
}
