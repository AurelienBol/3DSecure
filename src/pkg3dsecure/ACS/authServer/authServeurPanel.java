/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3dsecure.ACS.authServer;

import javax.net.ssl.SSLServerSocketFactory;
import serveurthreaddemande.ServeurPanel;

public class authServeurPanel extends ServeurPanel{
    RequeteAuth req = null;
    public authServeurPanel(String name, int p) {
        super(name, p,RequeteAuth.class);
    }
    public authServeurPanel(String name, int p, int pSSL, SSLServerSocketFactory SslSFac) {
        super(name, p, pSSL, SslSFac,RequeteAuth.class);
    }
}