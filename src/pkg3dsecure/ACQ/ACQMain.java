package pkg3dsecure.ACQ;

import CertFile.CertFile;
import Reqpay.reqpayPanel;
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

public class ACQMain {
    static ServerProperties spReqpay, spMoney;
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
        
        File authPropertiesFile = new File("src\\pkg3dsecure\\ACQ\\reqpay.properties");
        spReqpay = new ServerProperties(authPropertiesFile);
     
        File moneyPropertiesFile = new File("src\\pkg3dsecure\\ACQ\\money.properties");
        spMoney = new ServerProperties(moneyPropertiesFile);
    }
   
    private static void initComponents(){
        //Création de la fenêtre
        final JFrame frame = new JFrame("Serveur ACQ");
        
        //Configuration des paramêtres de la fenêtre
        frame.setSize(500,500);
       
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Ajout d'un layout
        frame.getContentPane().setLayout(new GridLayout(1, 1));
 
        //Ajout du tabbedPane
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
 
        System.out.println(spReqpay);
        tabbedPane.addTab(spReqpay.getTitle(), makeReqPayServeurPanel(spReqpay.getOtherServer(),
                                                                spReqpay.getOtherServerName(),
                                                                spReqpay.getTitle(),
                                                                spReqpay.getPort(),
                                                                spReqpay.getPortSSL(),
                                                                spReqpay.getIP(),
                                                                spReqpay.getFichierKeystore(),
                                                                spReqpay.getPasswordKeystore(),
                                                                spReqpay.getPasswordKey()));
 
        frame.getContentPane().add(tabbedPane);
    }
    
    private static JPanel makeReqPayServeurPanel(String ACS,String ACSName, String titre, int port, int portSSL,String ip, String FICHIER_KEYSTORE,String PASSWD_KEYSTORE, String PASSWD_KEY){
        VerificationServer vs = new VerificationServer();
        if(!vs.ping(ACS)){
            System.err.println("[ACQMain : makeRePayServeurPanel] Impossible de ping le serveur ACS");
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(f,"Le serveur ACS doit être lancé avant celui-ci","Démarrage du serveur ACS nécessaire!",JOptionPane.ERROR_MESSAGE);
            exit(-1);
        }
        String result = vs.getNameSNMP(ACS);

        if(result!=null && result !="" &&result !=" "){
            String name = result.split(" = ")[1];
            System.out.println("Name = " +name);
            if(name.equalsIgnoreCase(ACSName)){
                System.out.println("Good to go!");
            }else{
                System.err.println("Requête du nom ratée :(");
                JFrame f = new JFrame();
                JOptionPane.showMessageDialog(f,"Requête du nom ratée :(","Démarrage du serveur ACS nécessaire!",JOptionPane.ERROR_MESSAGE);
                exit(-1);
            }
        }else{
            System.out.println("Pas de réponse SNMP");
        }
        
        reqpayPanel sp = new reqpayPanel(titre,port,portSSL,ip,CertFile.getSSLServerSocketFactory(FICHIER_KEYSTORE, PASSWD_KEYSTORE, PASSWD_KEY));
        JPanel p = new JPanel();
        p.add(sp);
        p.setLayout(new GridLayout(1,1));
        return p;
    }
}
