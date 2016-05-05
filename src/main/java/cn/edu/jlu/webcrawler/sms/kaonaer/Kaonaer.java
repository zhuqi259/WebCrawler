package cn.edu.jlu.webcrawler.sms.kaonaer;

import cn.edu.jlu.webcrawler.sms.autoeasy.util.Client;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Kaonaer {
    /**
     * GET http://api.51kkww.com/getAuthCode?phoneNum=15555555555&act=3&t=be4cb9d2-3bfa-46b3-9217-1378d9b3c4f6 HTTP/1.1
     * username:
     * appType: 5
     * deviceType: 1
     * versionCode:
     * os: android
     * osVersion: 4.4.4
     * protocolVersion: 1
     * isForgetCache: 0
     * Content-Length: 0
     * Host: api.51kkww.com
     * Connection: Keep-Alive
     * User-Agent: Mozilla/5.0 (Linux; U; Android 4.4.4; en-us; HM 2A Build/KTU84Q) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1
     * Accept-Encoding: gzip
     *
     * @param phoneNo 手机号
     */
    public static void sendSMS(String phoneNo) {
        HttpRequestInterceptor interceptor = new HttpRequestInterceptor() {
            @Override
            public void process(org.apache.http.HttpRequest request, HttpContext context) throws org.apache.http.HttpException, IOException {
                request.addHeader("username", "");
                request.addHeader("appType", "5");
                request.addHeader("deviceType", "1");
                request.addHeader("versionCode", "");
                request.addHeader("os", "android");
                request.addHeader("osVersion", "5.0");
                request.addHeader("protocolVersion", "1");
                request.addHeader("isForgetCache", "0");
                request.addHeader("User-Agent", "Mobile Safari/533.1");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("phoneNum", phoneNo);
        params.put("act", "3");
        params.put("t", UUID.randomUUID().toString());
        Client.simpleGet("api.51kkww.com", "/getAuthCode", params, interceptor);
    }

    public static void main(String[] args) {
        sendSMS("15143089558");
    }
}
