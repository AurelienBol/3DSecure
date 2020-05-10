package pkg3dsecure.ACS.authServer;

import java.io.Serializable;
import serveurthreaddemande.requetethreaddemande.Reponse;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class ReponseAuth implements Reponse, Serializable{
    public static int AUTH_OK = 201;
    public static int AUTH_NOT_FOUND = 501;
    public static int WRONG_PASSWORD = 401;
    
    private int codeRetour;
    private String chargeUtile;
    
    public ReponseAuth(int c, String chu){
        codeRetour = c;
        setChargeUtile(chu);
    }
    
    @Override
    public int getCode() {
        return codeRetour;
    }
    public String getChargeUtile(){
        return chargeUtile;
    }
    public void setChargeUtile(String chargeUtile){
        this.chargeUtile = chargeUtile;
    }
}
