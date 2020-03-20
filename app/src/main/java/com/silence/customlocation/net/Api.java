package com.silence.customlocation.net;



import com.silence.customlocation.net.retrofit.RetrofitClient;


public class Api extends RetrofitClient {


    private Api() {
        super();
    }

    /**
     * 单例
     */
    public static RetrofitService getInstance() {
        return new Api().getRetrofit().create(RetrofitService.class);
    }


}
