package com.peixueshi.crm.service;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.peixueshi.crm.app.Api.APP_DOMAIN;


/**
 * 接口
 */
public interface CommonService {

    /**
     * 注册
     */
    @POST(APP_DOMAIN + "user/register")
    @FormUrlEncoded
    Observable<String> register(@Field("user_string") String user_string,
                                @Field("password") String password,
                                @Field("re_password") String re_password,
                                @Field("code") String code,
                                @Field("extension_code") String extension_code);

    /**
     * 登录
     */
    @POST(APP_DOMAIN + "user/login")
    @FormUrlEncoded
    Observable<String> login(@Field("user_string") String user_string,
                             @Field("password") String password);


    /**
     * 发送验证码
     */
    @POST(APP_DOMAIN + "sms_send")
    @FormUrlEncoded
    Observable<String> sendCode(@Field("user_string") String user_string);

}
