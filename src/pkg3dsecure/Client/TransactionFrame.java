/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3dsecure.Client;

import CertFile.CertFile;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import pkg3dsecure.ACS.authServer.ReponseAuth;
import pkg3dsecure.ACS.authServer.RequeteAuth;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class TransactionFrame extends javax.swing.JFrame {

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket cliSock;
    
    public TransactionFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        SourceTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        DestinationTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        MontantSpinner = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        HistoriquePanel = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Demande de transaction");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Vendeur :");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel1, gridBagConstraints);

        SourceTextField.setMinimumSize(new java.awt.Dimension(200, 22));
        SourceTextField.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        getContentPane().add(SourceTextField, gridBagConstraints);

        jLabel2.setText("Acheteur : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel2, gridBagConstraints);

        DestinationTextField.setMinimumSize(new java.awt.Dimension(200, 22));
        DestinationTextField.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        getContentPane().add(DestinationTextField, gridBagConstraints);

        jLabel3.setText("Montant : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel3, gridBagConstraints);

        MontantSpinner.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, null, 1.0d));
        MontantSpinner.setMinimumSize(new java.awt.Dimension(200, 22));
        MontantSpinner.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        getContentPane().add(MontantSpinner, gridBagConstraints);

        jButton1.setText("Ok");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        getContentPane().add(jButton1, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Demande de transaction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        getContentPane().add(jLabel4, gridBagConstraints);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Vendeur", "Acheteur", "Montant", "Résultat"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        HistoriquePanel.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        getContentPane().add(HistoriquePanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try{
            //Connexion au serveur sur le port non SSL
            System.out.println("Connexion au serveur sur le port non-SSL");
            String adresse = "localhost";
            int port = 2500;
            
            cliSock = new Socket(adresse,port);
            ois = new ObjectInputStream(cliSock.getInputStream());
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            
            //Réception du premier packet indiquant le port SSL éventuel
            System.out.println("Réception du premier packet indiquant le port SSL éventuel");
            String isSsl = ois.readUTF();
            System.out.println("SSL - " + isSsl);
            
            if(!isSsl.equals("no-SSL")){
                int portSSL = Integer.parseInt(isSsl.substring(isSsl.lastIndexOf("#")+1));
                SSLSocketFactory SslSFac = CertFile.getSSLClientSocketFactory("C:/Users/Aurélien Bolkaerts/Desktop/key/acs_keystore.jks","password","password");
                SSLSocket SslSocket = (SSLSocket) SslSFac.createSocket("localhost", portSSL);
                ois = new ObjectInputStream(SslSocket.getInputStream());
                oos = new ObjectOutputStream(SslSocket.getOutputStream());
            }
            
            //Envoie de la requête
            System.out.println("Envoie de la requête");
            //String chargeUtile = NomTF.getText() + "#" + new String(PINTF.getPassword());
            //RequeteAuth req = new RequeteAuth(RequeteAuth.REQUEST_AUTH,chargeUtile);
            
            //oos.writeObject(req);
            oos.flush();
            
            // Lecture de la réponse      
            System.out.println("Lecture de la réponse");
            ReponseAuth rep = (ReponseAuth)ois.readObject();
            System.out.println("Reponse reçue : " + rep.getChargeUtile());
            
            
            //LReponse.setText(rep.getChargeUtile());
        } catch (IOException ex) {
            System.err.println("IOException - " + ex);
        } catch (ClassNotFoundException ex) {
            System.err.println("ClassNotFoundException - " + ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TransactionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransactionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransactionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransactionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TransactionFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DestinationTextField;
    private javax.swing.JScrollPane HistoriquePanel;
    private javax.swing.JSpinner MontantSpinner;
    private javax.swing.JTextField SourceTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}