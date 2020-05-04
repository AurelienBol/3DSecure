/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3dsecure;

import utilitaires.VerificationServer;

/**
 *
 * @author Aurélien Bolkaerts
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VerificationServer vs = new VerificationServer();
        vs.ping("8.8.8.8");
    }
    
}
