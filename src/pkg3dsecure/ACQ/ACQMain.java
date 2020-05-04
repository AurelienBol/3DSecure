/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3dsecure.ACQ;

import java.awt.GridLayout;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import serveurthreaddemande.ServeurPanel;
import utilitaires.ServerProperties;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class ACQMain {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loadProperties();
                initComponents(); 
            }
        });
    }
    
    private static void loadProperties(){
        File file = new File("src\\pkg3dsecure\\ACQ\\money.properties");
        ServerProperties sp = new ServerProperties(file);
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
 
        tabbedPane.addTab("Money", makeServeurPanel("Money",2400));
        tabbedPane.addTab("Requête de demande de paiement", makeServeurPanel("Requête de demande de paiement",2500));
 
        frame.getContentPane().add(tabbedPane);
    }
    
    private static JPanel makeServeurPanel(String titre, int port){
        //ServeurPanel sp = new ServeurPanel(titre,port);
        JPanel p = new JPanel();
        //p.add(sp);
        p.setLayout(new GridLayout(1,1));
        return p;
    }
}
