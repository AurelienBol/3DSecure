package utilitaires;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class SqlProperties {
    private File propFile;
    
    private String driverName;
    private String username;
    private String password;
    private String serverIP;
    private int serverPort;
    private String service;
    private String urlStringHeader;
    
    public String getDriverName(){ return driverName;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public String getServerIP(){return serverIP;}
    public int getServerPort(){return serverPort;}
    public String getService(){return service;}
    public String getUrlStringHeader(){return urlStringHeader;}
    
    public SqlProperties(File file){
        InputStream input = null;
        try {
            propFile = file;
            input = new FileInputStream(file);
            Properties prop = new Properties();
            prop.load(input);
            driverName = prop.getProperty("driverName");
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            serverIP = prop.getProperty("serverIP");
            serverPort = Integer.parseInt(prop.getProperty("serverPort"));
            service = prop.getProperty("service");
            urlStringHeader = prop.getProperty("urlStringHeader");
            
        } catch (FileNotFoundException ex) {
            System.err.println("[SqlProperties : SqlProperties] FileNotFoundException  - " + ex);
        } catch (IOException ex) {
            System.err.println("[SqlProperties : SqlProperties] IOException  - " + ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                System.err.println("[SqlProperties : SqlProperties] IOException  - " + ex);
            }
        }

    }
}
