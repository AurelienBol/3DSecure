package utilitaires;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;


/**
 *
 * @author Aurélien Bolkaerts
 */
public class VerificationServer {

    public  boolean ping(String adresse){
        System.out.println("Adresse testée = " + adresse);
        Process p = null;
        String commande = "ping -n 4 -w 1000 " + adresse;
        System.out.println("Commande testée = " + commande);
        BufferedReader bfIn = null;
        try{
            p=Runtime.getRuntime().exec(commande);
            if (p == null){
                System.out.println("** Erreur d'exécution de la commande **");
                return false;
            }
            bfIn = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String strLine;
        boolean pasDeReponse = false;
        while ((strLine = bfIn.readLine()) != null){
            System.out.println(strLine); // pour trace
            if (Trouve100(strLine)){
                System.err.println("La machine " + adresse + " ne répond pas");
                return false;
            }
        }
        bfIn.close();
        System.out.println("La machine " + adresse + " a répondu");
        return true;
        }catch(IOException ex){
            System.err.println("[VerificationServer : ping] Exception IO - " + ex);
        }
        catch(Exception ex){
            System.out.println("[VerificationServer : ping] Exception - " + ex);
        }
        return false;
    }
    private  boolean Trouve100 (String s){
        boolean trouve = false;
        StringTokenizer scan = new StringTokenizer (s, " ");
        int cpt = 0;
        while (scan.hasMoreTokens()){
            String essai = scan.nextToken();
            int pp = essai.indexOf("%");
            if (pp != -1){
                int p100 = essai.indexOf("100");
                trouve = (p100 != -1);
            }
            if (trouve) return true;
        }
        return false;
    }
    
    public String getNameSNMP(String adresse){
        try{
            TransportMapping transport=null;
            transport = new DefaultUdpTransportMapping();
            transport.listen();
            
            CommunityTarget target = new CommunityTarget();
            target.setVersion(SnmpConstants.version1);
            
            target.setCommunity(new OctetString("public"));
            Address targetAddress;
            targetAddress = GenericAddress.parse("udp:"+adresse+"/161");
            target.setAddress(targetAddress);
            target.setRetries(2);
            target.setTimeout(1500);
            
            PDU pdu = new PDU();
            pdu.setType(PDU.GET);
            pdu.add(new VariableBinding(new OID(new int[]{1,3,6,1,2,1,1,5,0})));
            
            Snmp snmp = new Snmp(transport);
            ResponseEvent paquetReponse = null;
            paquetReponse = snmp.get(pdu, target);
            System.out.println("Requete SNMP envoyée à l'agent");

            if (paquetReponse !=null){
                PDU pduReponse = paquetReponse.getResponse();
                if(pduReponse!=null){
                    System.out.println("Status de la réponse = " + pduReponse.getErrorStatus());
                    System.out.println("Status de la réponse = " + pduReponse.getErrorStatusText());
                    Vector vecReponse = pduReponse.getVariableBindings();
                    for (int i=0; i<vecReponse.size(); i++){
                       System.out.println("Elément n°"+i+ " : "+vecReponse.elementAt(i));
                       return vecReponse.elementAt(i).toString();
                    }
                }
            }
        }
        catch (IOException ex){ 
            Logger.getLogger(VerificationServer.class.getName()).log(Level.SEVERE, null, ex); }
            return "";    
        }
        
    }
