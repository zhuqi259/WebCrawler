package cn.edu.jlu.webcrawler.sms.hairy.util;


import cn.edu.jlu.webcrawler.sms.hairy.model.BaseResponseModel;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

import java.util.Map;

public interface HRIApi {
    @POST("/sms/sendSMS")
    void sendSMS(@Body Map<String, Object> paramMap, Callback<BaseResponseModel> paramCallback);
}
