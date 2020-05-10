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
