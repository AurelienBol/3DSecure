package Money;

import javax.net.ssl.SSLServerSocketFactory;
import serveurthreaddemande.ServeurPanel;

public class moneyServeurPanel extends ServeurPanel{
    RequeteMoney req = null;
    public moneyServeurPanel(String name, int p, String ip) {
        super(name, p,ip,RequeteMoney.class);
    }
    public moneyServeurPanel(String name, int p, int pSSL, String ip, SSLServerSocketFactory SslSFac) {
        super(name, p, pSSL,ip, SslSFac,RequeteMoney.class);
    }
}