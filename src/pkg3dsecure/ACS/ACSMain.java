package pkg3dsecure.ACS;
import CertFile.CertFile;
import pkg3dsecure.ACS.authServer.authServeurPanel;
import java.awt.GridLayout;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import sql.BankAccount;
import utilitaires.ServerProperties;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class ACSMain {

    static ServerProperties spAuth, spMoney;
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loadProperties();
                initComponents(); 
            }
        });
    }
    
    
    
    private static void loadProperties(){
        System.out.println("Chargement des fichiers de propriétés des serveurs.");
        
        File authPropertiesFile = new File("src\\pkg3dsecure\\ACS\\auth.properties");
        spAuth = new ServerProperties(authPropertiesFile);
     
        File moneyPropertiesFile = new File("src\\pkg3dsecure\\ACS\\money.properties");
        spMoney = new ServerProperties(moneyPropertiesFile);
    }
    
    private static void initComponents(){
        //Création de la fenêtre
        final JFrame frame = new JFrame("Serveur ACS");
        
        //Configuration des paramêtres de la fenêtre
        frame.setSize(500,500);
       
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Ajout d'un layout
        frame.getContentPane().setLayout(new GridLayout(1, 1));
 
        //Ajout du tabbedPane
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        System.out.println(spAuth);
        
        tabbedPane.addTab(spAuth.getTitle(), makeAuthServeurPanel(spAuth.getTitle(),
                                                                spAuth.getPort(),
                                                                spAuth.getPortSSL(),
                                                                spAuth.getFichierKeystore(),
                                                                spAuth.getPasswordKeystore(),
                                                                spAuth.getPasswordKey()));
        frame.getContentPane().add(tabbedPane);
        testCon();
    }
    
    private static JPanel makeAuthServeurPanel(String titre, int port, int portSSL, String FICHIER_KEYSTORE,String PASSWD_KEYSTORE, String PASSWD_KEY){
        authServeurPanel sp = new authServeurPanel(titre,port,portSSL,CertFile.getSSLServerSocketFactory(FICHIER_KEYSTORE, PASSWD_KEYSTORE, PASSWD_KEY));
        JPanel p = new JPanel();
        p.add(sp);
        p.setLayout(new GridLayout(1,1));
        return p;
    }
    
    private static void testCon(){
        BankAccount ba = new BankAccount();
        ba.connect();
    }
    
    private void connectToSql(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");  
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/orcl","CBSGBD","oracle");
            //setsqlConnection(con);
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(Serveur_Banque.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            //Logger.getLogger(Serveur_Banque.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    private static JPanel makeServeurPanel(String titre, int port, int portSSL, String FICHIER_KEYSTORE,String PASSWD_KEYSTORE, String PASSWD_KEY){
        ServeurPanel sp = new ServeurPanel(titre,port,portSSL,CertFile.getSSLServerSocketFactory(FICHIER_KEYSTORE, PASSWD_KEYSTORE, PASSWD_KEY));
        JPanel p = new JPanel();
        p.add(sp);
        p.setLayout(new GridLayout(1,1));
        return p;
    }
    */
}