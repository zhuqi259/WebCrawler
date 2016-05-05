package cn.edu.jlu.webcrawler.sms.feifan;

import cn.edu.jlu.webcrawler.sms.autoeasy.util.Client;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class FFVC {
    public static Model userSendSMSValcode(String phoneNo, String type) {
        Map<String, String> params = new HashMap<>();
        params.put("FFClientType", "2");
        params.put("FFClientVersion", "25200");
        params.put("appid", "feifan");
        params.put("type", type);
        params.put("mobile", phoneNo);
        String result = Client.sendHttpClientPost("http://api.ffan.com/ffan/v1/member/verifycodes", params, "utf-8");
        System.out.println(result);
        return JSON.parseObject(result, Model.class);
    }

    public static void send(String phoneNo) {
        Model result1 = userSendSMSValcode(phoneNo, "2");
        if (result1.status != 200) {
            userSendSMSValcode(phoneNo, "1");
        }
    }

    public static void main(String[] args) {
        send("15143089000");
    }
}
