/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Money;

import Reqpay.*;
import javax.net.ssl.SSLServerSocketFactory;
import serveurthreaddemande.ServeurPanel;

public class moneyServeurPanel extends ServeurPanel{
    RequeteMoney req = null;
    public moneyServeurPanel(String name, int p) {
        super(name, p,RequeteMoney.class);
    }
    public moneyServeurPanel(String name, int p, int pSSL, SSLServerSocketFactory SslSFac) {
        super(name, p, pSSL, SslSFac,RequeteMoney.class);
    }
}