package cn.edu.jlu.webcrawler.sms.hairy.model;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SMSResponse<T> implements Callback<T> {

    public final void failure(RetrofitError paramRetrofitError) {
        Response response = paramRetrofitError.getResponse();
        if (response != null) {
            System.out.println(response.getStatus() + ", " + response.getReason());
        }
    }

    public final void success(T paramT, Response paramResponse) {
        BaseResponseModel responseModel = (BaseResponseModel) paramT;
        if ((responseModel != null) && (responseModel.status == 0)) {
            System.out.println(responseModel.status + ", " + responseModel.message);
        }
    }
}
