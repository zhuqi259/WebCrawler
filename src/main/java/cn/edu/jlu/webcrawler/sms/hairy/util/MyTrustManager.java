package cn.edu.jlu.webcrawler.sms.hairy.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

@SuppressWarnings("deprecation")
public class MyTrustManager implements X509TrustManager {
    MyTrustManager(MySSLSocketFactory parame) {
    }

    public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
            throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
            throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
