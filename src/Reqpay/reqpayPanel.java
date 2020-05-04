/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reqpay;

import javax.net.ssl.SSLServerSocketFactory;
import serveurthreaddemande.ServeurPanel;

public class reqpayPanel extends ServeurPanel{
    RequetePay req = null;
    public reqpayPanel(String name, int p) {
        super(name, p,RequetePay.class);
    }
    public reqpayPanel(String name, int p, int pSSL, SSLServerSocketFactory SslSFac) {
        super(name, p, pSSL, SslSFac,RequetePay.class);
    }
}