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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import pkg3dsecure.Client.ClientFrame;
import serveurthreaddemande.ConsoleServeur;
import serveurthreaddemande.requetethreaddemande.Requete;
import sql.Client;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class RequeteAuth implements Requete, Serializable{
    public static int REQUEST_AUTH = 1;
    
    private int type;
    private String chargeUtile;
    private Socket socketClient;
    private byte[] hash;
    
    public RequeteAuth(int t, String chu, byte[] hash){
        type = t;
        setChargeUtile(chu);
        setHash(hash);
    }
    public RequeteAuth(int t, String chu, Socket s,byte[] hash){
        type = t;
        setChargeUtile(chu);
        socketClient = s;
        setHash(hash);
    }
    
    private void setChargeUtile(String chargeUtile){
        this.chargeUtile = chargeUtile;
    }
    
    public String getChargeUtile(){
        return chargeUtile;
    }
    
    private void setHash(byte[]hash){
        this.hash = hash;
    }
    
    public byte[] getHash(){
        return hash;
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
        
        
        // la charge utile est le nom du client + la date
        String chargeUtile = getChargeUtile();
        StringTokenizer tokenizer = new StringTokenizer(chargeUtile,"#");
        
        String nom = tokenizer.nextToken();
        String date = tokenizer.nextToken();
        
        cs.TraceEvenements(adresseDistante+"#Demande d'authentification de "+ nom);
        
        //Vérification de l'authentification
        Client client = new Client();
        String password = client.getPassword(nom);
        
        boolean isOk = false;
        if(password != null){
            System.out.println("date - " + date );
            System.out.println("nom - " + nom);
            System.out.println("password - " + password);

            if(MessageDigest.isEqual(hash, makeDigest(date,nom,password))){
                System.out.println("Digest OK");
                isOk = true;
            }else{
                System.out.println("Digest NOK");
            }
        
        // Construction d'une réponse
        if(!isOk){
            ReponseAuth rep = new ReponseAuth(ReponseAuth.WRONG_PASSWORD, "Erreur de password ou identifiant non trouvé");
            try{
                oos.writeObject(rep);
                oos.flush();
                oos.close();
            }catch (IOException e){
                System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
            }
        }else{
            String reponse;
            String nomBanque = "Delta";
            long serialNumber = (long) (Math.random() * (100000));
            reponse = nomBanque + "#" + nom + "#"+ serialNumber;
            ReponseAuth rep = new ReponseAuth(ReponseAuth.AUTH_OK, reponse);
            try{
                oos.writeObject(rep);
                oos.flush();
                oos.close();
            }catch (IOException e){
                System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
            }
        }
        }
    }
    private byte[] makeDigest(String date, String nom, String pin){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(nom.getBytes());
            md.update(date.getBytes());
            md.update(pin.getBytes());
            byte[] digest = md.digest();
            return digest;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
