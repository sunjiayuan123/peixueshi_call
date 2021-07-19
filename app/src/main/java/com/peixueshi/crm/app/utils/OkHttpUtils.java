package com.peixueshi.crm.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.peixueshi.crm.YuXueApplication;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.peixueshi.crm.MainActivity;
import com.peixueshi.crm.R;
import com.peixueshi.crm.activity.LoginActivity;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.EnjoyPreferenceSaveCall;
import com.peixueshi.crm.utils.PromptManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;




/**
 * Created by zhaobaolei on 2018/1/23.
 */

public class OkHttpUtils {
    private static String TAG = "OkHttpUtils";

    public static class ClientHolder {
        public static OkHttpClient client = new OkHttpClient();

    }

    public static OkHttpClient getOkHttpClient() {
        return ClientHolder.client;
    }

    public static void newPost(final Activity activity, final String url, final HashMap<String, String> params, final OkhttpCallback callback) {
        if(activity != null){
            if (!NetUtils.isConnected(activity)) {
                Toast.makeText(activity, "网络未连接，请检查网络设置！", Toast.LENGTH_SHORT).show();
                callback.onFailure("网络未连接，请检查网络设置！");
                return;
            }
        }
        if(Constants.isWifiLimit){
            if(url.contains("user/getfirs") || url.contains("user/firs_list") || url.contains("work/pool_list") || url.contains("/work/come") || url.contains("/order/o_list")) {
                if (NetUtils.getNetworkState(activity) != NetUtils.NETWORN_WIFI) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "非wifi连接不能获取首咨、库存和订单数据", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                } else {
//                下面的代码可以获取当当前设备连接到的网络的wifi信息

                    WifiManager mWifi = (WifiManager) ((Context) activity).getSystemService(Context.WIFI_SERVICE);
                    if (mWifi.isWifiEnabled()) {
                        WifiInfo wifiInfo = mWifi.getConnectionInfo();
                        String netName = wifiInfo.getSSID(); //获取被连接网络的名称
                        String netMac = wifiInfo.getBSSID();//02:00:00:00:00:00
                        int mIpAddress = wifiInfo.getIpAddress();//192.168.10.161
                        int id = wifiInfo.getNetworkId();
                        String freq = "";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            freq = wifiInfo.getFrequency() + "";//频段 2462
                        }
                        String localMac = wifiInfo.getMacAddress();// 获得本机的MAC地址  
                        Log.d("MainActivity", "---netName:" + netName);  //---netName:HUAWEI MediaPad
                        Log.d("MainActivity", "---netMac:" + netMac);
                        Log.d("MainActivity", "---localMac:" + localMac);
                        String key = netMac + freq;//2442
                        EnjoyPreference.saveString(activity, netName + key + mIpAddress, key);
                        if (!key.equals("02:00:00:00:00:00" + "2462") && key.equals("02:00:00:00:00:00" + "2412") && key.equals("02:00:00:00:00:00" + "2442")) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "非YuXue wifi不能获取首咨、库存和订单数据", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                    }
                }
            }
        }

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
                if(Constants.newloginUserInfo != null ){
                    token = Constants.newloginUserInfo.getAcc_token();
                }else{
                    token = EnjoyPreference.readString(activity,"acc_token");
                }

                if(token == null){
                    token = "";
                }
                Log.e("tag", "run: "+token );
                Request request= new Request.Builder()
                        .url(url).addHeader("Authorization", token).addHeader("Content-Type","application/json")
                        .post(builder.build())
                        .build();
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
                            if(object.has("error_code") && object.getInt("error_code") == -1){
                                if(activity == null){
                                    return;
                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            PromptManager.showMyToast(object.getString("msg"),activity);
                                            if(object.getString("msg").contains("授权已过期") || object.getString("msg").contains("无权限访问") || object.getString("msg").contains("无效的token") || object.getString("msg").contains("value") ){
                                                Intent intent = new Intent(activity, MainActivity.class);//singleTask
                                                intent.putExtra("isExit",true);
                                                intent.putExtra("isAutoLogin",true);
                                                activity.startActivity(intent);
                                                return;
                                            }
//                                            {"error_code":-1,"msg":"请求未携带token，无权限访问"}
                                            if(object.getString("msg").contains("无效的token") || object.getString("msg").contains("value")  || object.getString("msg").contains("无权限访问")){
                                                Intent intent = new Intent(activity, MainActivity.class);//singleTask
                                                intent.putExtra("isExit",true);
                                                activity.startActivity(intent);
                                            }
                                            return;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                return;
                            }else if (object.has("error_code") && object.getInt("error_code") == -5){
                                Log.e("tag", "onResponse: -5" );
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showDialogMessage(activity,"您的账号已在其他设备登陆, 请重新登陆",true);
                                    }
                                });
                                return;
                            }
                            if(object.getInt("err") == 0){
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
                                            Log.e("tag", "run: "+msg.contains("value") );
                                            if(msg.contains("value")){
                                                Intent intent = new Intent(activity, MainActivity.class);//singleTask
                                                intent.putExtra("isExit",true);
                                                activity.startActivity(intent);
                                            }else {
                                              /*  Intent intent = new Intent(activity, LoginActivity.class);//singleTask
                                                activity.startActivity(intent);
                                                activity.finish();*/
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.e("tag", "run: " );
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

    public static void newGet(final Activity activity, final String url, final OkhttpCallback callback) {

        Log.d("OutGoingReceiver", "url: " +url);
        if(activity != null){
            if (!NetUtils.isConnected(activity)) {
                Toast.makeText(activity, activity.getResources().getString(R.string.common_neterror), Toast.LENGTH_SHORT).show();
                callback.onFailure("网络未连接，请检查网络设置！");
                return;
            }
        }

        if(Constants.isWifiLimit){

            if(url.contains("user/getfirs") || url.contains("user/firs_list") || url.contains("work/pool_list") || url.contains("/work/come") || url.contains("/order/o_list")){
                if(NetUtils.getNetworkState(activity) != NetUtils.NETWORN_WIFI){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "非wifi连接不能获取首咨、库存和订单数据", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }else{
//             下面的代码可以获取当当前设备连接到的网络的wifi信息

                    WifiManager mWifi = (WifiManager) ((Context)activity).getSystemService(Context.WIFI_SERVICE);
                    if (mWifi.isWifiEnabled()) {
                        WifiInfo wifiInfo = mWifi.getConnectionInfo();
                        String netName = wifiInfo.getSSID(); //获取被连接网络的名称
                        String netMac = wifiInfo.getBSSID();//02:00:00:00:00:00
                        int mIpAddress = wifiInfo.getIpAddress();//192.168.10.161
                        int id = wifiInfo.getNetworkId();
                        String freq="";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            freq = wifiInfo.getFrequency()+"";//频段 2462
                        }
                        String localMac = wifiInfo.getMacAddress();// 获得本机的MAC地址  
                        Log.d("MainActivity","---netName:"+netName);  //---netName:HUAWEI MediaPad
                        Log.d("MainActivity","---netMac:"+netMac);
                        Log.d("MainActivity","---localMac:"+localMac);
                        String key = netMac+freq;//2442
                        EnjoyPreference.saveString(activity,netName+key+mIpAddress,key);
                        if(!key.equals("02:00:00:00:00:00"+"2462") && key.equals("02:00:00:00:00:00"+"2412") && key.equals("02:00:00:00:00:00"+"2442")){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "非YuXue wifi不能获取首咨、库存和订单数据", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                    }
                }

            }
        }

        callback.onBefore();

        if(activity != null){
            PromptManager.showTransParentDialog(activity);
        }

        if(Constants.newloginUserInfo != null ){
            token = Constants.newloginUserInfo.getAcc_token();
        }else{
            token = EnjoyPreference.readString(activity,"acc_token");
        }
        if(token == null){
            token = "";
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("tag", "run: "+token );
                final Request request = new Request.Builder()
                        .url(url).addHeader("Authorization", token)
                        .build();//getCookie()cookie

                getOkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, final IOException e) {
                        Log.e("OutGoingReceiver", e.toString());
                        if(MainActivity.mainContext != null){
                            if(url.contains("work/call_add")){//呼叫记录本地保存
                                String callList =  EnjoyPreferenceSaveCall.readString(MainActivity.mainContext,"call_list");
                                try{
                                    if(callList == null || callList.equals("null")){
                                        JSONObject object = new JSONObject();
                                        object.put("call_one",url);
                                        JSONArray array = new JSONArray();
                                        array.put(object);
                                        EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext,"call_list",array.toString());
                                    }else{
                                        JSONArray array = new JSONArray(callList);
                                        JSONObject object = new JSONObject();
                                        object.put("call_one",url);
                                        array.put(object);
                                        EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext,"call_list",array.toString());
                                    }
                                }catch (Exception ex){
                                }
                            }
                        }



                        if(activity != null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure("网络访问失败:" + e.getMessage());

                                }
                            });
                        }
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {

                        final int code = response.code();
                        Log.e("OutGoingReceiver", "code:"+code);
                        if (code != 200) {
                            if(MainActivity.mainContext != null) {
                                if (url.contains("work/call_add")) {//呼叫记录本地保存
                                    String callList = EnjoyPreferenceSaveCall.readString(MainActivity.mainContext, "call_list");
                                    try {
                                        if (callList == null || callList.equals("null")) {
                                            JSONObject object = new JSONObject();
                                            object.put("call_one", url);
                                            JSONArray array = new JSONArray();
                                            array.put(object);
                                            EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext, "call_list", array.toString());
                                        } else {
                                            JSONArray array = new JSONArray(callList);
                                            JSONObject object = new JSONObject();
                                            object.put("call_one", url);
                                            array.put(object);
                                            EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext, "call_list", array.toString());
                                        }
                                    } catch (Exception e) {

                                    }

                                }
                            }
                        }
                        if (code != 200) {
                            if(activity != null){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFailure("网络访问失败:ResponseCode-->" + code);
                                    }
                                });
                            }
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
                            Log.d("OutGoingReceiver", str_body);
                            JSONObject object = new JSONObject(str_body);
                            Log.d("OutGoingReceiver", str_body);
                            if(object.has("error_code") && object.getInt("error_code") == -1){
                                if(activity == null){
                                    return;
                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            PromptManager.showMyToast(object.getString("msg"),activity);
                                            if(object.getString("msg").contains("授权已过期") || object.getString("msg").contains("无权限访问") || object.getString("msg").contains("无效的token") || object.getString("msg").contains("value") ){
                                                Intent intent = new Intent(activity, MainActivity.class);//singleTask
                                                intent.putExtra("isExit",true);
                                                intent.putExtra("isAutoLogin",true);
                                                activity.startActivity(intent);

                                              /*  if (url.contains("work/call_add")) {//呼叫记录本地保存
                                                    String callList = EnjoyPreferenceSaveCall.readString(MainActivity.mainContext, "call_list");
                                                    try {
                                                        if (callList == null || callList.equals("null")) {
                                                            JSONObject object = new JSONObject();
                                                            object.put("call_one", url);
                                                            JSONArray array = new JSONArray();
                                                            array.put(object);
                                                            EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext, "call_list", array.toString());
                                                        } else {
                                                            JSONArray array = new JSONArray(callList);
                                                            JSONObject object = new JSONObject();
                                                            object.put("call_one", url);
                                                            array.put(object);
                                                            EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext, "call_list", array.toString());
                                                        }
                                                    } catch (Exception e) {

                                                    }

                                                }*/
                                                return;
                                            }
//                                            {"error_code":-1,"msg":"请求未携带token，无权限访问"}
                                            if(object.getString("msg").contains("无效的token") || object.getString("msg").contains("value") || object.getString("msg").contains("无权限访问")){

                                                /*Intent intent = new Intent(activity, LoginActivity.class);
                                                activity.startActivity(intent);
                                                activity.finish();*/
                                                Intent intent = new Intent(activity, MainActivity.class);//singleTask
                                                intent.putExtra("isExit",true);
                                                activity.startActivity(intent);
                                                return;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                return;
                            }else if (object.has("error_code") && object.getInt("error_code") == -5){
                                Log.e("tag", "onResponse: -15" );
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showDialogMessage(activity,"您的账号已在其他设备登陆, 请重新登陆",true);
                                    }
                                });
                                return;
                            }
                            Log.e("tag", "onResponse: "+object );
                            //自动登录密码错误情况处理
                            if(object.getInt("err") != 0 && url.contains("login/login")){
                                String msg = object.getString("msg");
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String msg = object.getString("msg");
                                            if(object.getString("msg").contains("密码不正确")){
                                                Object o = callback.parseNetworkResponse(object);
                                                callback.onGetResult(o);
                                            }
                                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                                            return;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            callback.onFailure(""+e.getMessage());
                                        }
                                    }
                                });
                            }

                            if(object.getInt("err") == 0){
                                Object o = callback.parseNetworkResponse(object);
                                callback.onGetResult(o);
                            }else{
                                if(activity == null){
                                    return;
                                }
                                if (url.contains("user/getfirs") && object.has("err") && object.getInt("err") == 5){
                                    Object o = callback.parseNetworkResponse(object);
                                    callback.onGetResult(o);
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

                        } catch (JSONException e) {
                            e.printStackTrace();
                            if(activity == null){
                                return;
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("tag", "run:接口异常 " );
                                    Toast.makeText(activity, "接口异常", Toast.LENGTH_SHORT).show();
                                    if(MainActivity.mainContext != null) {
                                        if (url.contains("work/call_add")) {//呼叫记录本地保存
                                            String callList = EnjoyPreferenceSaveCall.readString(MainActivity.mainContext, "call_list");
                                            try {
                                                if (callList == null || callList.equals("null")) {
                                                    JSONObject object = new JSONObject();
                                                    object.put("call_one", url);
                                                    JSONArray array = new JSONArray();
                                                    array.put(object);
                                                    EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext, "call_list", array.toString());
                                                } else {
                                                    JSONArray array = new JSONArray(callList);
                                                    JSONObject object = new JSONObject();
                                                    object.put("call_one", url);
                                                    array.put(object);
                                                    EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext, "call_list", array.toString());
                                                }
                                            } catch (Exception e) {

                                            }

                                        }
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }).start();

    }

    public static void post(final Activity activity, final String url, final HashMap<String, String> params, final OkhttpCallback callback) {
        if(activity != null){
            if (!NetUtils.isConnected(activity)) {
                Toast.makeText(activity, "网络未连接，请检查网络设置！", Toast.LENGTH_SHORT).show();
                callback.onFailure("网络未连接，请检查网络设置！");
                return;
            }
        }
        if(Constants.isWifiLimit){
        if(url.contains("user/getfirs") || url.contains("user/firs_list") || url.contains("work/pool_list") || url.contains("/work/come") || url.contains("/order/o_list")) {
            if (NetUtils.getNetworkState(activity) != NetUtils.NETWORN_WIFI) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "非wifi连接不能获取首咨、库存和订单数据", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            } else {
//                下面的代码可以获取当当前设备连接到的网络的wifi信息

                WifiManager mWifi = (WifiManager) ((Context) activity).getSystemService(Context.WIFI_SERVICE);
                if (mWifi.isWifiEnabled()) {
                    WifiInfo wifiInfo = mWifi.getConnectionInfo();
                    String netName = wifiInfo.getSSID(); //获取被连接网络的名称
                    String netMac = wifiInfo.getBSSID();//02:00:00:00:00:00
                    int mIpAddress = wifiInfo.getIpAddress();//192.168.10.161
                    int id = wifiInfo.getNetworkId();
                    String freq = "";
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        freq = wifiInfo.getFrequency() + "";//频段 2462
                    }
                    String localMac = wifiInfo.getMacAddress();// 获得本机的MAC地址  
                    Log.d("MainActivity", "---netName:" + netName);  //---netName:HUAWEI MediaPad
                    Log.d("MainActivity", "---netMac:" + netMac);
                    Log.d("MainActivity", "---localMac:" + localMac);
                    String key = netMac + freq;//2442
                    EnjoyPreference.saveString(activity, netName + key + mIpAddress, key);
                    if (!key.equals("02:00:00:00:00:00" + "2462") && key.equals("02:00:00:00:00:00" + "2412") && key.equals("02:00:00:00:00:00" + "2442")) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "非YuXue wifi不能获取首咨、库存和订单数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                }
            }
        }
        }

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
                if(Constants.newloginUserInfo != null ){
                    token = Constants.newloginUserInfo.getAcc_token();
                }else{
                    token = EnjoyPreference.readString(YuXueApplication.context,"acc_token");
                }

                if(token == null){
                    token = "";
                }
                Request request = new Request.Builder()
                        .url(url).addHeader("Authorization", token)
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
                            if(object.has("error_code") && object.getInt("error_code") == -1){
                                if(activity == null){
                                    return;
                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            PromptManager.showMyToast(object.getString("msg"),activity);
                                            if(object.getString("msg").contains("授权已过期") || object.getString("msg").contains("无权限访问") || object.getString("msg").contains("无效的token") || object.getString("msg").contains("value") ){
                                                Intent intent = new Intent(activity, MainActivity.class);//singleTask
                                                intent.putExtra("isExit",true);
                                                intent.putExtra("isAutoLogin",true);
                                                activity.startActivity(intent);
                                                return;
                                            }
//                                            {"error_code":-1,"msg":"请求未携带token，无权限访问"}
                                            if(object.getString("msg").contains("无效的token") || object.getString("msg").contains("value")  || object.getString("msg").contains("无权限访问")){
                                                Intent intent = new Intent(activity, MainActivity.class);//singleTask
                                                intent.putExtra("isExit",true);
                                                activity.startActivity(intent);
                                            }
                                            return;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                return;
                            }else if (object.has("error_code") && object.getInt("error_code") == -5){
                                Log.e("tag", "onResponse: -5" );
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showDialogMessage(activity,"您的账号已在其他设备登陆, 请重新登陆",true);
                                    }
                                });
                                return;
                            }

                            if(object.getInt("err") == 0){
                                Log.e("tag", "onResponse: "+object );
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
    public static void get(final Activity activity, final String url, final OkhttpCallback callback) {

        Log.d("OutGoingReceiver", "url: " +url);
        if(activity != null){
            if (!NetUtils.isConnected(activity)) {
                Toast.makeText(activity, activity.getResources().getString(R.string.common_neterror), Toast.LENGTH_SHORT).show();
                callback.onFailure("网络未连接，请检查网络设置！");
                return;
            }
        }

        if(Constants.isWifiLimit){

        if(url.contains("user/getfirs") || url.contains("user/firs_list") || url.contains("work/pool_list") || url.contains("/work/come") || url.contains("/order/o_list")){
            if(NetUtils.getNetworkState(activity) != NetUtils.NETWORN_WIFI){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "非wifi连接不能获取首咨、库存和订单数据", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }else{
//             下面的代码可以获取当当前设备连接到的网络的wifi信息

                WifiManager mWifi = (WifiManager) ((Context)activity).getSystemService(Context.WIFI_SERVICE);
                if (mWifi.isWifiEnabled()) {
                    WifiInfo wifiInfo = mWifi.getConnectionInfo();
                    String netName = wifiInfo.getSSID(); //获取被连接网络的名称
                    String netMac = wifiInfo.getBSSID();//02:00:00:00:00:00
                    int mIpAddress = wifiInfo.getIpAddress();//192.168.10.161
                    int id = wifiInfo.getNetworkId();
                    String freq="";
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        freq = wifiInfo.getFrequency()+"";//频段 2462
                    }
                    String localMac = wifiInfo.getMacAddress();// 获得本机的MAC地址  
                    Log.d("MainActivity","---netName:"+netName);  //---netName:HUAWEI MediaPad
                    Log.d("MainActivity","---netMac:"+netMac);
                    Log.d("MainActivity","---localMac:"+localMac);
                    String key = netMac+freq;//2442
                    EnjoyPreference.saveString(activity,netName+key+mIpAddress,key);
                    if(!key.equals("02:00:00:00:00:00"+"2462") && key.equals("02:00:00:00:00:00"+"2412") && key.equals("02:00:00:00:00:00"+"2442")){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "非YuXue wifi不能获取首咨、库存和订单数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                }
            }

        }
        }

        callback.onBefore();

        if(activity != null){
            PromptManager.showTransParentDialog(activity);
        }

        if(Constants.newloginUserInfo != null ){
            token = Constants.newloginUserInfo.getAcc_token();
        }else{
            token = EnjoyPreference.readString(activity,"acc_token");
        }
        if(token == null){
            token = "";
        }
        Log.e("tag", "get: "+url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization", token)
                        .build();//getCookie()cookie

                getOkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, final IOException e) {
                        Log.e("OutGoingReceiver", e.toString());
                        if(MainActivity.mainContext != null){
                            if(url.contains("work/call_add")){//呼叫记录本地保存
                                String callList =  EnjoyPreferenceSaveCall.readString(MainActivity.mainContext,"call_list");
                                try{
                                    if(callList == null || callList.equals("null")){
                                        JSONObject object = new JSONObject();
                                        object.put("call_one",url);
                                        JSONArray array = new JSONArray();
                                        array.put(object);
                                        EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext,"call_list",array.toString());
                                    }else{
                                        JSONArray array = new JSONArray(callList);
                                        JSONObject object = new JSONObject();
                                        object.put("call_one",url);
                                        array.put(object);
                                        EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext,"call_list",array.toString());
                                    }
                                }catch (Exception ex){
                                }
                            }
                        }



                        if(activity != null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure("网络访问失败:" + e.getMessage());

                                }
                            });
                        }
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {

                        final int code = response.code();
                        Log.e("OutGoingReceiver", "code:"+code);
                        if (code != 200) {
                            if(MainActivity.mainContext != null) {
                                if (url.contains("work/call_add")) {//呼叫记录本地保存
                                    String callList = EnjoyPreferenceSaveCall.readString(MainActivity.mainContext, "call_list");
                                    try {
                                        if (callList == null || callList.equals("null")) {
                                            JSONObject object = new JSONObject();
                                            object.put("call_one", url);
                                            JSONArray array = new JSONArray();
                                            array.put(object);
                                            EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext, "call_list", array.toString());
                                        } else {
                                            JSONArray array = new JSONArray(callList);
                                            JSONObject object = new JSONObject();
                                            object.put("call_one", url);
                                            array.put(object);
                                            EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext, "call_list", array.toString());
                                        }
                                    } catch (Exception e) {

                                    }

                                }
                            }
                        }
                        if (code != 200) {
                            if(activity != null){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onFailure("网络访问失败:ResponseCode-->" + code);
                                    }
                                });
                            }
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
                            Log.d("OutGoingReceiver", str_body);
                            JSONObject object = new JSONObject(str_body);
                            Log.d("OutGoingReceiver", str_body);
                            if(object.has("error_code") && object.getInt("error_code") == -1){
                                if(activity == null){
                                    return;
                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            PromptManager.showMyToast(object.getString("msg"),activity);
                                            if(object.getString("msg").contains("授权已过期") || object.getString("msg").contains("无权限访问") || object.getString("msg").contains("无效的token") || object.getString("msg").contains("value") ){
                                                Intent intent = new Intent(activity, MainActivity.class);//singleTask
                                                intent.putExtra("isExit",true);
                                                intent.putExtra("isAutoLogin",true);
                                                activity.startActivity(intent);

                                              /*  if (url.contains("work/call_add")) {//呼叫记录本地保存
                                                    String callList = EnjoyPreferenceSaveCall.readString(MainActivity.mainContext, "call_list");
                                                    try {
                                                        if (callList == null || callList.equals("null")) {
                                                            JSONObject object = new JSONObject();
                                                            object.put("call_one", url);
                                                            JSONArray array = new JSONArray();
                                                            array.put(object);
                                                            EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext, "call_list", array.toString());
                                                        } else {
                                                            JSONArray array = new JSONArray(callList);
                                                            JSONObject object = new JSONObject();
                                                            object.put("call_one", url);
                                                            array.put(object);
                                                            EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext, "call_list", array.toString());
                                                        }
                                                    } catch (Exception e) {

                                                    }

                                                }*/
                                                return;
                                            }
//                                            {"error_code":-1,"msg":"请求未携带token，无权限访问"}
                                            if(object.getString("msg").contains("无效的token") || object.getString("msg").contains("value") || object.getString("msg").contains("无权限访问")){

                                                /*Intent intent = new Intent(activity, LoginActivity.class);
                                                activity.startActivity(intent);
                                                activity.finish();*/
                                                Intent intent = new Intent(activity, MainActivity.class);//singleTask
                                                intent.putExtra("isExit",true);
                                                activity.startActivity(intent);
                                                return;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                return;
                            }else if (object.has("error_code") && object.getInt("error_code") == -5){
                                Log.e("tag", "onResponse: -15" );
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showDialogMessage(activity,"您的账号已在其他设备登陆, 请重新登陆",true);
                                    }
                                });
                                return;
                            }

                            //自动登录密码错误情况处理
                            if(object.getInt("err") != 0 && url.contains("login/login")){
                                String msg = object.getString("msg");
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String msg = object.getString("msg");
                                            if(object.getString("msg").contains("密码不正确")){
                                                Object o = callback.parseNetworkResponse(object);
                                                callback.onGetResult(o);
                                            }
                                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                                            return;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            callback.onFailure(""+e.getMessage());
                                        }
                                    }
                                });
                            }

                            if(object.getInt("err") == 0){
                                Object o = callback.parseNetworkResponse(object);
                                callback.onGetResult(o);
                            }else{
                                if(activity == null){
                                    return;
                                }
                                if (url.contains("user/getfirs") && object.has("err") && object.getInt("err") == 5){
                                    Object o = callback.parseNetworkResponse(object);
                                    callback.onGetResult(o);
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

                        } catch (JSONException e) {
                            e.printStackTrace();
                            if(activity == null){
                                return;
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("tag", "runsssss:接口异常 "+e.getMessage() );
                                    Toast.makeText(activity, "接口异常", Toast.LENGTH_SHORT).show();
                                    if(MainActivity.mainContext != null) {
                                        if (url.contains("work/call_add")) {//呼叫记录本地保存
                                            String callList = EnjoyPreferenceSaveCall.readString(MainActivity.mainContext, "call_list");
                                            try {
                                                if (callList == null || callList.equals("null")) {
                                                    JSONObject object = new JSONObject();
                                                    object.put("call_one", url);
                                                    JSONArray array = new JSONArray();
                                                    array.put(object);
                                                    EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext, "call_list", array.toString());
                                                } else {
                                                    JSONArray array = new JSONArray(callList);
                                                    JSONObject object = new JSONObject();
                                                    object.put("call_one", url);
                                                    array.put(object);
                                                    EnjoyPreferenceSaveCall.saveString(MainActivity.mainContext, "call_list", array.toString());
                                                }
                                            } catch (Exception e) {

                                            }

                                        }
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }).start();

    }


    public static void showDialogMessage(Activity activity, String msg, boolean isOpen) {
        View view = View.inflate(activity, R.layout.login_again, null);
        TextView textView = view.findViewById(R.id.tv_to_bind_warn);
        textView.setText(msg);
        view.findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  PromptManager.closeCustomDialog();
                if (isOpen == true) {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        });

        PromptManager.showCustomDialogFalse(activity, view, Gravity.CENTER, Gravity.CENTER);
    }


    public static String getCookie() {
        StringBuilder stringBuilder_cookie = new StringBuilder();
        HashMap<String, String> cookieMap = new HashMap<>();
        if(Constants.loginUserInfo != null){
            cookieMap.put("Authorization",Constants.loginUserInfo.getAcc_token());
        }

//        cookieMap.put("h5api_aToken", CommonUtils.getaToken(LoginActivity.getAccount().getToken()));
        //cookieMap.put("h5api_channel", Configv2PropertiesUtils.getdhl_channel());
        for (String key : cookieMap.keySet()) {
            if (stringBuilder_cookie.toString().length() == 0) {
                stringBuilder_cookie.append(key + "=" + cookieMap.get(key));
            } else {
                stringBuilder_cookie.append(";" + key + "=" + cookieMap.get(key));
            }
        }
        Log.e("访问的cookie", stringBuilder_cookie.toString());
        return stringBuilder_cookie.toString();
    }
}
