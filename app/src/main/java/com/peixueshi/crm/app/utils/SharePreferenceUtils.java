package com.peixueshi.crm.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.mf.library.utils.Constant;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by zhaobaolei on 2018/3/21.
 */

public class SharePreferenceUtils {
    public static final String wifiAutoDownload = "wifiAutoDownload";
    public static final String useJpush = "useJpus";
    public static final String key_startInfo = "startInfo";
    public static final String key_first_launch = "first_launch";
    private static final String dhl_sp = "dhl_sp";
    public static final String key_check_notification_time = "cn_time";

    /**
     * 判断wifi环境下是否自动下载更新包,默认true
     *
     * @param activity
     * @return
     */
    public static boolean getSpValueAsBoolean(Activity activity, String keyName, boolean defaultValue) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dhl_sp, MODE_PRIVATE);
        return sharedPreferences.getBoolean(keyName, defaultValue);
    }

    public static void updateSpValueAsBoolean(Activity activity, boolean newValue, String keyName) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dhl_sp, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(keyName, newValue);
        editor.commit();
    }

    public static String getSpValueAsString(Activity activity, String keyName, String defaultValue) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dhl_sp, MODE_PRIVATE);
        return sharedPreferences.getString(keyName, defaultValue);
    }


    public static void updateSpValueAsString(Activity activity, String keyName, String newValue) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dhl_sp, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyName, newValue);
        editor.commit();
    }
    public static void updateSpValueAsString(Context context, String keyName, String newValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(dhl_sp, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyName, newValue);
        editor.commit();
    }

    public static String getSpValueAsString(Context context, String keyName, String defaultValue) {
        if (context==null){
            context= Constant.context;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(dhl_sp, MODE_PRIVATE);
        return sharedPreferences.getString(keyName, defaultValue);
    }

}
