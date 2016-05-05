package cn.edu.jlu.webcrawler.sms.hairy.util;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.Socket;
import java.security.*;

public class MySSLSocketFactory extends org.apache.http.conn.ssl.SSLSocketFactory {
    SSLContext context = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore paramKeyStore)
            throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(paramKeyStore);
        MyTrustManager manager = new MyTrustManager(this);
        context.init(null, new TrustManager[]{manager}, null);
    }

    public Socket createSocket()
            throws IOException {
        return context.getSocketFactory().createSocket();
    }

    public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean)
            throws IOException {
        return this.context.getSocketFactory().createSocket(paramSocket, paramString, paramInt, paramBoolean);
    }
}
