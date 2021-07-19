package com.peixueshi.crm.app.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.peixueshi.crm.MainActivity;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.PromptManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


/**
 * Created by zhaobaolei on 2018/1/23.
 */

public class TkyOkHttpUtils {
    private static String TAG = "OkHttpUtils";

    public static class ClientHolder {
        public static OkHttpClient client = new OkHttpClient();

    }

    public static OkHttpClient getOkHttpClient() {
        return ClientHolder.client;
    }

    public static void post(final Activity activity, final String url, final HashMap<String, String> params, final OkhttpCallback callback) {
        callback.onBefore();
        if(activity != null){
            PromptManager.showTransParentDialog(activity);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                FormEncodingBuilder builder = new FormEncodingBuilder();
                for (String key : params.keySet()) {
                    if(params.get(key)!=null){
                        builder.add(key, params.get(key));
                    }
                }

                if(Constants.tkyUserInfo != null ){
                    token = Constants.tkyUserInfo.getToken();
                }else{
                    token = EnjoyPreference.readString(activity,"tky_acc_token");
                }

                if(token == null){
                    token = "";
                }
                Request request = new Request.Builder()
                        .url(url).addHeader("token", token)
                        .post(builder.build())
                        .build();
                Log.e(TAG, "request" + request.header("cookie"));

                getOkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, final IOException e) {
                        if(activity != null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure("网络访问失败:" + e.getMessage());
                                }
                            });
                        }

                        Log.e("post访问失败", e.getMessage());
                    }

                    @Override
                    public void onResponse(Response response) {
                        final int code = response.code();
                        Log.e("post访问code", code + "");
                        if (code != 200 && activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure("网络访问失败:ResponseCode-->" + code);
                                }
                            });
                            return;
                        }else{
                            if(activity != null){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        PromptManager.closeTransParentDialog();
                                    }
                                });

                            }
                        }
                        try {

                            String str_body = response.body().string();
                            JSONObject object = new JSONObject(str_body);
                            Log.d("OutGoingReceiver", str_body);
                            if(object.getInt("code") == 0){
                                Object o = callback.parseNetworkResponse(object);
                                callback.onGetResult(o);
                            }else{
                                if(activity == null){
                                    return;
                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String msg = object.getString("msg");
                                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                                            if(msg.contains("value")){
                                                Intent intent = new Intent(activity, MainActivity.class);//singleTask
                                                intent.putExtra("isExit",true);
                                                activity.startActivity(intent);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            callback.onFailure(""+e.getMessage());
                                        }
                                    }
                                });
                            }




                        } catch (final Exception e) {
                            //json解析需捕获异常
                            e.printStackTrace();
                            if(activity != null){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFailure(""+e.getMessage());
                                    }
                                });
                            }
                        }

                    }
                });
            }
        }).start();

    }

    static String token = "";
    public static void postJson(final Activity activity, final String url, final HashMap<String, String> params, final OkhttpCallback callback) {
        callback.onBefore();
        if(activity != null){
            PromptManager.showTransParentDialog(activity);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    for (String key : params.keySet()) {
                        if(params.get(key)!=null){
                            json.put(key, params.get(key));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                if(Constants.tkyUserInfo != null ){
                    token = Constants.tkyUserInfo.getToken();
                }else{
                    token = EnjoyPreference.readString(activity,"tky_acc_token");
                }

                if(token == null){
                    token = "";
                }
                Request request = new Request.Builder()
                        .url(url).addHeader("token", token)
                        .post(requestBody)
                        .build();
                Log.e(TAG, "request" + request.header("cookie"));

                getOkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, final IOException e) {
                        if(activity != null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure("网络访问失败:" + e.getMessage());
                                }
                            });
                        }

                        Log.e("post访问失败", e.getMessage());
                    }

                    @Override
                    public void onResponse(Response response) {
                        final int code = response.code();
                        Log.e("post访问code", code + "");
                        if (code != 200 && activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure("网络访问失败:ResponseCode-->" + code);
                                }
                            });
                            return;
                        }else{
                            if(activity != null){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        PromptManager.closeTransParentDialog();
                                    }
                                });

                            }
                        }
                        try {

                            String str_body = response.body().string();
                            JSONObject object = new JSONObject(str_body);
                            Log.d("OutGoingReceiver", str_body);
                            if(object.getInt("code") == 0){
                                Object o = callback.parseNetworkResponse(object);
                                callback.onGetResult(o);
                            }else{
                                if(activity == null){
                                    return;
                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String msg = object.getString("msg");
                                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                                            if(msg.contains("value")){
                                                Intent intent = new Intent(activity, MainActivity.class);//singleTask
                                                intent.putExtra("isExit",true);
                                                activity.startActivity(intent);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            callback.onFailure(""+e.getMessage());
                                        }
                                    }
                                });
                            }




                        } catch (final Exception e) {
                            //json解析需捕获异常
                            e.printStackTrace();
                            if(activity != null){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFailure(""+e.getMessage());
                                    }
                                });
                            }
                        }

                    }
                });
            }
        }).start();

    }


}
