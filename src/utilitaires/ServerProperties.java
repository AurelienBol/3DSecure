/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitaires;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class ServerProperties {
    private File propFile;
    private String title;
    private int port;
    private Boolean SSL;
    private int portSSL;
    private String fichierKeystore;
    private String passwordKeystore;
    private String passwordKey;
    
    
    public ServerProperties(File file){
        InputStream input = null;
        try {
            propFile = file;
            input = new FileInputStream(file);
            Properties prop = new Properties();
            prop.load(input);
            title = prop.getProperty("title");
            port = Integer.parseInt(prop.getProperty("port"));
            SSL = Boolean.parseBoolean(prop.getProperty("SSL"));
            portSSL = Integer.parseInt(prop.getProperty("portSSL"));
            fichierKeystore = prop.getProperty("fichierKeystore");
            passwordKeystore = prop.getProperty("passwordKeystore");
            passwordKey = prop.getProperty("passwordKey");

        } catch (FileNotFoundException ex) {
            System.err.println("FileNotFoundException - " + ex);
        } catch (IOException ex) {
            System.err.println("IOException - " + ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                System.err.println("IOException - " + ex);
            }
        }

    }
    
    public void setTitle(String title){
        WriteProp("title",title);
    }
    public String getTitle(){
        return title;
    }
    
    public void setPort(int port){
        WriteProp("port",Integer.toString(port));
    }
    public int getPort(){
        return port;
    }
    
    public void setSSL(Boolean enable){
        WriteProp("SSL",enable.toString());
    }
    public boolean getSSL(){
        return SSL;
    }
    
    public void setPortSSL(int portSSL){
        WriteProp("portSSL",Integer.toString(portSSL));
    }
    public int getPortSSL(){
        return portSSL;
    }
    
    public void setFichierKeystore(String fichierKeystore){
        WriteProp("fichierKeystore",fichierKeystore);
    }
    public String getFichierKeystore(){
        return fichierKeystore;
    }
    
    public void setPasswordKeystore(String passwordKeystore){
        WriteProp("passwordKeystore",passwordKeystore);
    }
    public String getPasswordKeystore(){
        return passwordKeystore;
    }
    
    public void setPasswordKey(String passwordKey){
        WriteProp("passwordKey",passwordKey);
    }
    public String getPasswordKey(){
        return passwordKey;
    }
    
    private void WriteProp(String key, String value){
        try (OutputStream output = new FileOutputStream(propFile)) {
            Properties prop = new Properties();
            prop.setProperty(key, value);
            prop.store(output, null);
        } catch (IOException ex) {
            System.err.println("IOException - " + ex);
        }
    }
    
    @Override
    public String toString(){
        String s = propFile.getAbsolutePath() + System.lineSeparator();
        s += "\t" + "Title = " + title + System.lineSeparator();
        s += "\t" + "Port = " + port + System.lineSeparator();
        s += "\t" + "SSL = " + SSL + System.lineSeparator();
        s += "\t" + "Port SSL = " + portSSL + System.lineSeparator();
        s += "\t" + "Fichier keystore = " + fichierKeystore + System.lineSeparator();
        s += "\t" + "Password keystore = " + passwordKeystore + System.lineSeparator();
        s += "\t" + "Password key = " + passwordKey + System.lineSeparator();
        return s;
    }
}
