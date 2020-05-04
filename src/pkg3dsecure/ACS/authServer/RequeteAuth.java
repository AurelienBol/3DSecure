/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3dsecure.ACS.authServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import serveurthreaddemande.ConsoleServeur;
import serveurthreaddemande.requetethreaddemande.Requete;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class RequeteAuth implements Requete, Serializable{
    public static int REQUEST_AUTH = 1;
    
    private int type;
    private String chargeUtile;
    private Socket socketClient;
    
    public RequeteAuth(int t, String chu){
        type = t;
        setChargeUtile(chu);
    }
    public RequeteAuth(int t, String chu, Socket s){
        type = t;
        setChargeUtile(chu);
        socketClient = s;
    }
    
    private void setChargeUtile(String chargeUtile){
        this.chargeUtile = chargeUtile;
    }
    
    public String getChargeUtile(){
        return chargeUtile;
    }
    
    @Override
    public Runnable createRunnable(ObjectInputStream ois, ObjectOutputStream oos, ConsoleServeur cs) {
        if(type == REQUEST_AUTH){
            return new Runnable(){
                public void run(){
                    traiteRequeteAuth(ois,oos,cs);
                }
            };
        }else return null;
    }
    
    private void traiteRequeteAuth(ObjectInputStream ois, ObjectOutputStream oos, ConsoleServeur cs){
        // Affichage des informations
        //String adresseDistante = sock.getRemoteSocketAddress().toString();
        String adresseDistante = " client";
        System.out.println("Début de traiteRequete : adresse distante = " + adresseDistante);
        
        
        // la charge utile est le nom du client
        String nom = getChargeUtile();
        
        //String eMail = (String)tableMails.get(getChargeUtile());
        cs.TraceEvenements(adresseDistante+"#Demande d'authentification de "+
        getChargeUtile());
        if (nom != null)
        System.out.println("Nom trouvé pour " + getChargeUtile());
        else
        {
            System.out.println("Nom non trouvé pour " + getChargeUtile() + " : " + nom);
            //eMail="?@?";
        }
        
        // Construction d'une réponse
        ReponseAuth rep = new ReponseAuth(ReponseAuth.AUTH_OK, getChargeUtile());
        try{
            oos.writeObject(rep);
            oos.flush();
            oos.close();
        }catch (IOException e){
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
    }
}
