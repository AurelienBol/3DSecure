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