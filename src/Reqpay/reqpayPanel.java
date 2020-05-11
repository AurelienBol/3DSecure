package Reqpay;

import javax.net.ssl.SSLServerSocketFactory;
import serveurthreaddemande.ServeurPanel;

public class reqpayPanel extends ServeurPanel{
    RequetePay req = null;
    public reqpayPanel(String name, int p, String ip) {
        super(name, p,  ip,RequetePay.class);
    }
    public reqpayPanel(String name, int p, int pSSL, String ip, SSLServerSocketFactory SslSFac) {
        super(name, p, pSSL,  ip, SslSFac,RequetePay.class);
    }
}