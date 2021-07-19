package com.peixueshi.crm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.mf.library.utils.Constant;

public class EnjoyPreference {
    private static String ENJOY_WIFI_PREF_TAG = "gameinfo";
    private static String ENJOY_WIFI_SDCARD_DIR = "/com.enmonet.key";
    private static String ENJOY_WIFI_SDCARD_DIR_MT = "/com.qzgame.wjmtsj.wyx";
    private static String ENJOY_WIFI_SDCARD_FILE = "/accinfo.txt";
    private static String ENJOY_WIFI_SDCARD_FILE_EVA = "/acc";
    private static String packageName;

    public static int readInt(Context context, String key, int defValue) {
        String str = readString(context, key);
        if (str == null || str.length() <= 0) {
            return defValue;
        }
        return Util.getIntFromString(str, defValue);
    }

    public static String readString(Context context, String key) {
        if (context==null){
            context= Constant.context;
        }
        SharedPreferences share = context.getSharedPreferences(ENJOY_WIFI_PREF_TAG, Context.MODE_PRIVATE);
        String valueInfo = share.getString(key, null);
        if(valueInfo != null && valueInfo.length()>0){
            return valueInfo;
        }else {
            boolean sdCardExist = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);

            // read from sdcard
            if (sdCardExist) {
                Map<String, String> keyValues = readPrefsFromSdcard(context);
                if (keyValues != null && keyValues.containsKey(key)) {
                     share = context.getSharedPreferences(ENJOY_WIFI_PREF_TAG,
                            Context.MODE_PRIVATE);
                    boolean isSuccess = share.edit().putString(key, keyValues.get(key)).commit();
                    return keyValues.get(key);
                }
            }
        }

        /*SharedPreferences share = context.getSharedPreferences(ENJOY_WIFI_PREF_TAG,
                Context.MODE_PRIVATE);
        return share.getString(key, null);*/
        return null;
    }

    public static boolean saveInt(Context context, String key, int value) {
        return saveString(context, key, String.valueOf(value));
    }

    public static boolean saveString(Context context, String key, String value) {
         if (context==null){
             context=Constant.context;
         }
        // 写入包内
        // save to share preference
        SharedPreferences share = context.getSharedPreferences(ENJOY_WIFI_PREF_TAG,
                Context.MODE_PRIVATE);
        boolean isSuccess = share.edit().putString(key, value).commit();

        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        // save to sdcard
        if (sdCardExist) {
            Map<String, String> keyValues = readPrefsFromSdcard(context);
            if (keyValues == null) {
                keyValues = new HashMap<>();
            }
            keyValues.put(key, value);
            boolean sdcardOk = writePrefsToSdcard(keyValues,context);
            if (sdcardOk || isSuccess) {
                return true;
            }
        }

        // save to share preference
       /* SharedPreferences share = context.getSharedPreferences(ENJOY_WIFI_PREF_TAG,
                Context.MODE_PRIVATE);
        return share.edit().putString(key, value).commit();*/
       return false;
    }

    public static boolean clearPrefs(Context context) {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            boolean sdcardOk = clearPrefsOfSdcard(context);
            if (sdcardOk) {
                return true;
            }
        }

        // save to share preference
        SharedPreferences share = context.getSharedPreferences(ENJOY_WIFI_PREF_TAG,
                Context.MODE_PRIVATE);
        return share.edit().clear().commit();
    }

    private static Map<String, String> readPrefsFromSdcard(Context context) {
        packageName = context.getPackageName();
        String filePath = null;
        if(packageName != null && packageName.equals("com.wyx.qzgame.google.evaoverseas")){
             filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + ENJOY_WIFI_SDCARD_DIR + ENJOY_WIFI_SDCARD_FILE_EVA;
//            filePath = context.getExternalFilesDir(null).toString()+ ENJOY_WIFI_SDCARD_FILE;
        }else if(packageName != null && packageName.equals("com.qzgame.wjmtsj.wyx")){
            filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/"+packageName + ENJOY_WIFI_SDCARD_FILE;
            android.util.Log.i("dddd===",filePath);
        }
        else{
//            filePath = context.getExternalFilesDir(null).toString()+ENJOY_WIFI_SDCARD_FILE;
           filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/"+packageName + ENJOY_WIFI_SDCARD_FILE;
            android.util.Log.i("dddd===",filePath);
        }

        File file = new File(filePath);
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                int len = inputStream.available();
                if (len > 0) {
                    byte[] buf = new byte[len + 1];
                    int readLen = inputStream.read(buf);
                    if (readLen > 0) {
                        String prefs = new String(buf);
                        return Util
                                .getKeyValueMap(prefs);
                    }
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static boolean clearPrefsOfSdcard(Context context) {
        return writePrefsToSdcard(new HashMap<String, String>(),context);
    }

    private static boolean writePrefsToSdcard(Map<String, String> keyValues,Context context) {
        String filePath = null;
        if(packageName != null && packageName.equals("com.wyx.qzgame.google.evaoverseas")){
            filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + ENJOY_WIFI_SDCARD_DIR ;//+ENJOY_WIFI_SDCARD_FILE
        }else if(packageName != null && packageName.equals("com.qzgame.wjmtsj.wyx")){
            /*filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + ENJOY_WIFI_SDCARD_DIR_MT + ENJOY_WIFI_SDCARD_FILE;*/
            filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/"+packageName;
            android.util.Log.i("dddd===",filePath);
        }
        else{
            filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/"+packageName;
//            filePath = context.getExternalFilesDir(null).toString();
            android.util.Log.i("dddd===",filePath);
        }

        File file;
        if(packageName != null && packageName.equals("com.wyx.qzgame.google.evaoverseas")){
             file = new File(filePath+ENJOY_WIFI_SDCARD_FILE_EVA);
        }else{
             file = new File(filePath+ENJOY_WIFI_SDCARD_FILE);
        }
        if (!file.exists()) {
            boolean isSuccess = file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!file.exists()) {
            return false;
        }
//        file = new File(file.getAbsolutePath());
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
                Set<String> keySet = keyValues.keySet();
                Iterator it = keySet.iterator();
                StringBuilder sb = new StringBuilder();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    String value = keyValues.get(key);
                    sb.append(key).append("=").append(value).append("|");
                }
                outputStream.write(sb.toString().getBytes());
                outputStream.flush();
                outputStream.close();
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
