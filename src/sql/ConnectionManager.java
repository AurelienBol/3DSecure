package sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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
    
    public ConnectionManager(String sqlPropertiesFilePath){
        File clientFramePropertiesFile = new File(sqlPropertiesFilePath);
        InputStream input = null;
        try {
            
            Properties prop = new Properties();
            input = new FileInputStream(sqlPropertiesFilePath);
            prop.load(input);
            setDriverName(prop.getProperty("driverName"));
            setUsername(prop.getProperty("username"));
            setPassword(prop.getProperty("password"));
            
            String serverIP = prop.getProperty("serverIP");
            String serverPort = prop.getProperty("serverPort");
            String service = prop.getProperty("service");
            String urlStringHeader = prop.getProperty("urlStringHeader");
            
            String urlString = urlStringHeader + "@" + serverIP + ":"  +serverPort + "/" + service; 
            // urlString -> jdbc:oracle:thin:@192.168.0.47:1521/orcl
            setUrlstring(urlString);
            
        } catch (FileNotFoundException ex) {
            System.err.println("[ConnectionManager : ConnectionManager(sqlPropertiesFilePath)] FileNotFoundException - " + ex);
        } catch (IOException ex) {
            System.err.println("[ConnectionManager : ConnectionManager(sqlPropertiesFilePath)]  IOException - " + ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                System.err.println("[ConnectionManager : ConnectionManager(sqlPropertiesFilePath)]  IOException - " + ex);
            }
        }
    }
    
    public ConnectionManager(String driverName, String urlstring, String username, String password){
        setDriverName(driverName);
        setUrlstring(urlstring);
        setUsername(username);
        setPassword(password);
    }
    
    public ConnectionManager(String driverName, String serverIP, String serverPort, String service, String urlStringHeader, String username, String password){
        setDriverName(driverName);
        String urlString = urlStringHeader + "@" + serverIP + ":"  +serverPort + "/" + service;
        setUrlstring(urlString);
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
