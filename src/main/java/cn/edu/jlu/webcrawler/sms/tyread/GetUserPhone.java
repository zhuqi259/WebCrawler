package cn.edu.jlu.webcrawler.sms.tyread;

import cn.edu.jlu.webcrawler.sms.autoeasy.util.Client;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetUserPhone {

    /**
     * GET http://client.tyread.com:8080/portalapi/portalapi?msisdn=15555555555 HTTP/1.1
     * Client-Agent: TYYD_Android_ZX068002_JAVA_4_7_0/720*1280/Xiaomi_2013022
     * user-id:
     * guest-id: 1451281837629223
     * APIVersion: 1.0.0
     * User-Agent: Openwave
     * Content-Type:
     * x-referred: 10.118.156.56
     * Accept-Charset: utf-8
     * qdid: ZX068002
     * Action: getUserPhoneCode
     * Host: client.tyread.com:8080
     * Connection: Keep-Alive
     *
     * @param phoneNo 手机号
     */
    public static void sendSMS(String phoneNo) {
        HttpRequestInterceptor interceptor = new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
                request.addHeader("User-Agent", "Openwave");
                request.addHeader("Action", "getUserPhoneCode");
                request.addHeader("Client-Agent", "TYYD_Android_ZX068011_JAVA_4_7_0/720*1280/Xiaomi_2013022");
                request.addHeader("qdid", "ZX068011");
                request.addHeader("APIVersion", "1.0.0");
                request.addHeader("guest-id", "1451281837629224");
                request.addHeader("user-id", "");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("msisdn", phoneNo);
        Client.simpleGet("client.tyread.com:8080", "/portalapi/portalapi", params, interceptor);
    }

    public static void uniqueIdentifier(String phoneNo) {
        HttpRequestInterceptor interceptor = new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
                request.addHeader("client-imei", "111111111111111");
                request.addHeader("client-imsi", "111111111111111");
                request.addHeader("User-Agent", "Android/TYYD");
                request.addHeader("Action", "isExistUser");
                request.addHeader("Client-Agent", "TYYD_Android_ZX068011_JAVA_4_7_0/720*1280/Xiaomi_2013022");
                request.addHeader("qdid", "ZX068011");
                request.addHeader("APIVersion", "1.0.0");
                request.addHeader("guest-id", "1451281837629224");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("uniqueIdentifier", phoneNo);
        Client.simpleGet("client.tyread.com:8080", "/portalapi/portalapi", params, interceptor);
    }

    public static void main(String[] args) {
        sendSMS("15143089558");
    }
}
