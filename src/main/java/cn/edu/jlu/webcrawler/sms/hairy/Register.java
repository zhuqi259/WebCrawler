package cn.edu.jlu.webcrawler.sms.hairy;

import cn.edu.jlu.webcrawler.sms.hairy.model.SMSResponse;
import cn.edu.jlu.webcrawler.sms.hairy.util.HRIApi;
import cn.edu.jlu.webcrawler.sms.hairy.util.MyRequestInterceptor;
import cn.edu.jlu.webcrawler.sms.hairy.util.MySSLSocketFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Register {

    @SuppressWarnings("deprecation")
    private HRIApi API(String paramString) {
        Gson gson = new GsonBuilder().create();
        RequestInterceptor interceptor = new MyRequestInterceptor(this);
        RestAdapter.Builder localBuilder = new RestAdapter.Builder();
        localBuilder.setEndpoint(paramString).setConverter(new GsonConverter(gson)).setRequestInterceptor(interceptor);
        if (paramString.contains("https")) {
            try {
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null);
                MySSLSocketFactory factory = new MySSLSocketFactory(keyStore);
                factory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, "UTF-8");
                SchemeRegistry schemeRegistry = new SchemeRegistry();
                schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                schemeRegistry.register(new Scheme("https", factory, 443));
                HttpClient client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, schemeRegistry), params);
                client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
                client.getParams().setParameter("http.useragent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
                client.getParams().setParameter("http.protocol.expect-continue", Boolean.FALSE);
                client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 60000);
                localBuilder.setClient(new ApacheClient(client));
                return localBuilder.build().create(HRIApi.class);
            } catch (Exception e) {
                e.printStackTrace();
                return localBuilder.build().create(HRIApi.class);
            }
        } else {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(30L, TimeUnit.SECONDS);
            client.setReadTimeout(30L, TimeUnit.SECONDS);
            localBuilder.setClient(new OkClient(client));
            return localBuilder.build().create(HRIApi.class);
        }
    }

    public void sendSMS(String phoneNo) {
        HashMap<String, Object> localHashMap = new HashMap<>();
        localHashMap.put("phone", phoneNo);
        localHashMap.put("codeType", "REGISTER_MOBILE");
        API("https://zhuanle.kjtpay.com/zhuanle/").sendSMS(localHashMap, new SMSResponse());
    }


    public static void main(String[] args) {
        Register register = new Register();
        register.sendSMS("15143089622");
    }
}
