package pkg3dsecure.ACS;
import CertFile.CertFile;
import Money.moneyServeurPanel;
import pkg3dsecure.ACS.authServer.authServeurPanel;
import java.awt.GridLayout;
import java.io.File;
import static java.lang.System.exit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import utilitaires.ServerProperties;
import utilitaires.VerificationServer;

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
                                                                spAuth.getIP(),
                                                                spAuth.getFichierKeystore(),
                                                                spAuth.getPasswordKeystore(),
                                                                spAuth.getPasswordKey()));
        
        System.out.println(spMoney);
        tabbedPane.addTab(spMoney.getTitle(), makeMoneyServeurPanel(spMoney.getOtherServer(),
                                                                spMoney.getOtherServerName(),
                                                                spMoney.getTitle(),
                                                                spMoney.getPort(),
                                                                spMoney.getPortSSL(),
                                                                spMoney.getIP(),
                                                                spMoney.getFichierKeystore(),
                                                                spMoney.getPasswordKeystore(),
                                                                spMoney.getPasswordKey()));
        
        frame.getContentPane().add(tabbedPane);
    }
    
    private static JPanel makeAuthServeurPanel(String titre, int port, int portSSL,String ip, String FICHIER_KEYSTORE,String PASSWD_KEYSTORE, String PASSWD_KEY){
        authServeurPanel sp = new authServeurPanel(titre,port,portSSL,ip,CertFile.getSSLServerSocketFactory(FICHIER_KEYSTORE, PASSWD_KEYSTORE, PASSWD_KEY));
        JPanel p = new JPanel();
        p.add(sp);
        p.setLayout(new GridLayout(1,1));
        return p;
    }
    
    
    private static JPanel makeMoneyServeurPanel(String ACQ,String ACQName, String titre, int port, int portSSL,String ip, String FICHIER_KEYSTORE,String PASSWD_KEYSTORE, String PASSWD_KEY){
        VerificationServer vs = new VerificationServer();
        if(!vs.ping(ACQ)){
            System.err.println("[ACSMain : makeMoneyServeurPanel] Impossible de ping le serveur ACQ");
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(f,"Ping raté","Démarrage du serveur ACQ nécessaire!",JOptionPane.ERROR_MESSAGE);
            exit(-1);
        }
        String result = vs.getNameSNMP(ACQ);

        if(result!=null && result !="" &&result !=" "){
            String name = result.split(" = ")[1];
            System.out.println("Name = " +name);
            if(name.equalsIgnoreCase(ACQName)){
                System.out.println("Good to go!");
            }else{
                System.err.println("Requête du nom ratée :(");
                JFrame f = new JFrame();
                JOptionPane.showMessageDialog(f,"Requête du nom ratée :(","Démarrage du serveur ACQ nécessaire!",JOptionPane.ERROR_MESSAGE);
                exit(-1);
            }
        }else{
            System.out.println("Pas de réponse SNMP");
        }
        
        moneyServeurPanel msp = new moneyServeurPanel(titre,port,portSSL,ip,CertFile.getSSLServerSocketFactory(FICHIER_KEYSTORE, PASSWD_KEYSTORE, PASSWD_KEY));
        JPanel p = new JPanel();
        p.add(msp);
        p.setLayout(new GridLayout(1,1));
        return p;
    }
    
}