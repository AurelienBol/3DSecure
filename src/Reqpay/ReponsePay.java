/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reqpay;

import java.io.Serializable;
import serveurthreaddemande.requetethreaddemande.Reponse;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class ReponsePay implements Reponse, Serializable{
    public static int PAY_OK = 201;
    public static int PAY_NOT_OK = 501;
    public static int PAY_REFUSED = 401;
    
    private int codeRetour;
    private String chargeUtile;
    
    public ReponsePay(int c, String chu){
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
