package cn.edu.jlu.webcrawler.sms.autoeasy.service;


import cn.edu.jlu.webcrawler.sms.autoeasy.model.SendSMSValcodeModel;
import cn.edu.jlu.webcrawler.sms.autoeasy.util.Client;
import cn.edu.jlu.webcrawler.sms.autoeasy.util.Encrypt;
import cn.edu.jlu.webcrawler.sms.autoeasy.util.Urls;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class UserAccountService {

    public static void userSendSMSValcode(String phoneNo) {
        Map<String, String> params = new HashMap<>();
        SendSMSValcodeModel model = new SendSMSValcodeModel(phoneNo);
        String token = JSON.toJSONString(model);
        try {
            params.put("token", Encrypt.encrypt(token));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = Client.sendHttpClientPost(Urls.USER_SEND_SMS_VALCODE, params, "utf-8");
        System.out.println(result);
    }

    public static void main(String[] args) {
        userSendSMSValcode("15143089610");
    }
}
