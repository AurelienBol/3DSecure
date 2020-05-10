package Money;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import serveurthreaddemande.ConsoleServeur;
import serveurthreaddemande.requetethreaddemande.Requete;
import sql.BankTransaction;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class RequeteMoney implements Requete, Serializable{
    public static int REQUEST_MONEY = 3;
    private int type;
    private String chargeUtile;
    private Socket socketClient;
    private double montant;
    
    public RequeteMoney(int t, String chu, double montant){
        type = t;
        setChargeUtile(chu);
        setMontant(montant);
    }
    public RequeteMoney(int t, String chu, Socket s,double montant){
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
        if(type == REQUEST_MONEY){
            return new Runnable(){
                public void run(){
                    traiteRequeteMoney(ois,oos,cs);
                }
            };
        }else return null;
    }
    
    private void traiteRequeteMoney(ObjectInputStream ois, ObjectOutputStream oos, ConsoleServeur cs){
        // Affichage des informations
        //String adresseDistante = sock.getRemoteSocketAddress().toString();
        String adresseDistante = " client";
        System.out.println("Début de traiteRequete : adresse distante = " + adresseDistante);
        
        
        // la charge utile est la source
        String chargeUtile = getChargeUtile();
        String source = getChargeUtile();
        
        cs.TraceEvenements(adresseDistante+"#Demande de payement de " + montant + " depuis " + source);
        
        //Vérification du solde actuel
        BankTransaction bankTransaction = new BankTransaction();
        Double soldeActuel = bankTransaction.getSolde(source);
        System.out.println("Solde actuel = " + soldeActuel);
        System.out.flush();
        // Construction d'une réponse
        if(soldeActuel<montant){
            ReponseMoney rep = new ReponseMoney(ReponseMoney.MONEY_REFUSED, "Pas assez d'argent sur le compte source");
            try{
                oos.writeObject(rep);
                oos.flush();
                oos.close();
            }catch (IOException ex){
                System.err.println("[RequeteMoney : traiteRequeteMoney] IOException - " + ex);
            }
        }else{
            ReponseMoney rep = new ReponseMoney(ReponseMoney.MONEY_OK, "Prêt pour le virement");
            try{
                oos.writeObject(rep);
                oos.flush();
                bankTransaction.pay(source,montant);
                oos.close();
            }catch (IOException ex){
                System.err.println("[RequeteMoney : traiteRequeteMoney] IOException - " + ex);
            }
        }
    }

}
