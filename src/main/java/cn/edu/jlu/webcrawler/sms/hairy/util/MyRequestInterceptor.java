package cn.edu.jlu.webcrawler.sms.hairy.util;

import cn.edu.jlu.webcrawler.sms.hairy.Register;
import retrofit.RequestInterceptor;

@SuppressWarnings("deprecation")
public class MyRequestInterceptor implements RequestInterceptor {
    public MyRequestInterceptor(Register r) {
    }

    public void intercept(RequestInterceptor.RequestFacade paramRequestFacade) {
        paramRequestFacade.addHeader("X-KJT-Auth", "");
        paramRequestFacade.addHeader("X-KJT-Agent", "847588026805459;460011430647079;Android4.4.4;lte26007;CMCC;HM 2A;WANDOUJIA;1.8.0;WIFI;-");
        paramRequestFacade.addHeader("X-API-VER", "2.0");
    }
}
