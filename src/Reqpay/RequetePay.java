package Reqpay;

import CertFile.CertFile;
import Money.ReponseMoney;
import Money.RequeteMoney;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import serveurthreaddemande.ConsoleServeur;
import serveurthreaddemande.requetethreaddemande.Requete;
import sql.BankAccount;
import sql.ConnectionManager;
import utilitaires.SqlProperties;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class RequetePay implements Requete, Serializable{
    public static int REQUEST_PAY = 2;
    
    private int type;
    private String chargeUtile;
    private Socket socketClient;
    private double montant;
    
    public RequetePay(int t, String chu, double montant){
        type = t;
        setChargeUtile(chu);
        setMontant(montant);
    }
    public RequetePay(int t, String chu, Socket s,double montant){
        type = t;
        setChargeUtile(chu);
        socketClient = s;
        setMontant(montant);
    }
    
    private void setChargeUtile(String chargeUtile){
        this.chargeUtile = chargeUtile;
    }
    
    public String getChargeUtile(){
        return chargeUtile;
    }
    
    private void setMontant(double montant){
        this.montant = montant;
    }
    
    public double getMontant(){
        return montant;
    }
    
    @Override
    public Runnable createRunnable(ObjectInputStream ois, ObjectOutputStream oos, ConsoleServeur cs) {
        if(type == REQUEST_PAY){
            return new Runnable(){
                public void run(){
                    traiteRequetePay(ois,oos,cs);
                }
            };
        }else return null;
    }
    
    private void traiteRequetePay(ObjectInputStream ois, ObjectOutputStream oos, ConsoleServeur cs){
        File sqlPropertiesFile = new File("src\\pkg3dsecure\\ACQ\\sql.properties");
        SqlProperties sqlProperties = new SqlProperties(sqlPropertiesFile);
        ConnectionManager cm = new ConnectionManager(sqlProperties.getDriverName(), 
                sqlProperties.getServerIP(),
                Integer.toString(sqlProperties.getServerPort()), 
                sqlProperties.getService(), 
                sqlProperties.getUrlStringHeader(), 
                sqlProperties.getUsername(), 
                sqlProperties.getPassword());

        
        // Affichage des informations
        //String adresseDistante = sock.getRemoteSocketAddress().toString();
        String adresseDistante = " client";
        System.out.println("Début de traiteRequete : adresse distante = " + adresseDistante);
        
        
        // la charge utile est la source et la destination
        String chargeUtile = getChargeUtile();
        StringTokenizer tokenizer = new StringTokenizer(chargeUtile,"#");
        
        String source = tokenizer.nextToken();
        String destination = tokenizer.nextToken();
        
        cs.TraceEvenements(adresseDistante+"#Demande depayement depuis " + source + " vers " + destination);
        
        //Vérification de l'authentification
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCon(cm.getConnection());
        boolean exist = bankAccount.exist(destination);
        
        // Construction d'une réponse
        if(!exist){
            try {
                System.out.println("LE COMPTE DESTINATION N'EXISTE PAS");
                ReponsePay rep = new ReponsePay(ReponsePay.PAY_REFUSED, "Compte destination n'existe pas");
                oos.writeObject(rep);
                oos.flush();
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(RequetePay.class.getName()).log(Level.SEVERE, null, ex);
            }

        }else{
            try{
                System.out.println("LE COMPTE DESTINATION A ETE TROUVE");
                //Connexion au serveur sur le port non SSL
                System.out.println("Connexion au serveur sur le port non-SSL");
                String adresse = "192.168.0.11";
                
                int portM = 2400;
                
                Socket cliSock = new Socket(adresse,portM);
                ObjectInputStream oism = new ObjectInputStream(cliSock.getInputStream());
                ObjectOutputStream oosm = new ObjectOutputStream(cliSock.getOutputStream());
                
                //Réception du premier packet indiquant le port SSL éventuel
                System.out.println("Réception du premier packet indiquant le port SSL éventuel");
                String isSsl = oism.readUTF();
                System.out.println("SSL - " + isSsl);
                if(!isSsl.equals("no-SSL")){
                    int portSSL = Integer.parseInt(isSsl.substring(isSsl.lastIndexOf("#")+1));
                    SSLSocketFactory SslSFac = CertFile.getSSLClientSocketFactory("keystore/ACS.jks","password","password");
                    SSLSocket SslSocket = (SSLSocket) SslSFac.createSocket(adresse, portSSL);
                    oism = new ObjectInputStream(SslSocket.getInputStream());
                    oosm = new ObjectOutputStream(SslSocket.getOutputStream());
                    
                    RequeteMoney req = new RequeteMoney(RequeteMoney.REQUEST_MONEY,source,montant);
                    oosm.writeObject(req);
                    oosm.flush();
                    
                    ReponseMoney rep = (ReponseMoney)oism.readObject();
                    System.out.println("Reponse reçue : " + rep.getChargeUtile());

                    if(rep.getCode()==ReponseMoney.MONEY_OK){
                        bankAccount.gain(destination, montant);
                        ReponsePay repPay = new ReponsePay(ReponsePay.PAY_OK, "Payement effectué");
                        oos.writeObject(repPay);
                        oos.flush();
                        oos.close();
                    }else{
                        ReponsePay repPay = new ReponsePay(ReponsePay.PAY_NOT_OK, "Payement refusé");
                        oos.writeObject(repPay);
                        oos.flush();
                        oos.close();
                    }
                }
            }catch (IOException ex){
                System.err.println("[RequetePay : traiteRequetePay] IOException - " + ex);
            } catch (ClassNotFoundException ex) {
                System.err.println("[RequetePay : traiteRequetePay] ClassNotFoundException - " + ex);
            }
        }
    }

}
