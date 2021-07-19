package com.peixueshi.crm.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.widget.Toast;

import com.mf.library.utils.Constant;


public class Util {
    public static final String SHAREDPREFERENCES_NAME = "gameinfo";

    public static String getPrefString(Context context, String key, String def) {
        SharedPreferences share = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return share.getString(key, def);
    }


    public static int getPrefInt(Context context, String key, int def) {
        int result = def;
        String resultStr = getPrefString(context, key, null);
        if (resultStr != null) {
            try {
                result = Integer.valueOf(resultStr);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return result;
    }


    /**
     * 将时间戳转换为时间
     *
     * s就是时间戳
     */

    public static String stampToDate(String s) {
        String res;
        if (!TextUtils.isEmpty(s)){
            if (s.length() == 10) {
                s = s + "000";
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //如果它本来就是long类型的,则不用写这一步
            long lt = new Long(s);
//        Date date = new Date(lt * 1000);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
        }else {
            return "0";
        }

        return res;
    }


    /**
     * 获取过去几天时间戳
     * @param past 天
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getPastDateMills(int past) {
        /*Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String result = format.format(today);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            return String.valueOf( sdf.parse(result).getTime()/ 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        ZoneId z = ZoneId.systemDefault();//获取时区
        ZonedDateTime dt = ZonedDateTime.now(z);//获取当前时间
        long start = dt.toLocalDate().atStartOfDay(z).toEpochSecond() * 1000;//获取当天的起始时间戳
        //long end=start+24L*3600*1000;//获取当天的结束时间戳
       /* if(past == 1){
            return start/1000+"";
        }*/
        long time = start - 24L * 3600 * 1000 * (past - 1);
        return time / 1000 + "";
    }


    public static String getDataTime(String s) {
        if (s.length() == 10) {
            s = s + "000";
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
        //如果它本来就是long类型的,则不用写这一步
        long lt = new Long(s);
//        Date date = new Date(lt * 1000);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static void savePrefString(Context context, String key, String value) {
        SharedPreferences share = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        share.edit().putString(key, value).apply();
    }

    public static void savePrefInt(Context context, String key, int value) {
        SharedPreferences share = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        share.edit().putInt(key, value).apply();
    }


    public static int getIntPref(Context context, String key, int def) {
        int value = def;
        try {
            String valueStr = getStringPref(context, key);
            value = Integer.valueOf(valueStr);
        } catch (Exception e) {
            e.getMessage();
        }
        return value;
    }

    public static void saveIntPref(Context context, String key, int value) {
        saveStringPref(context, key, String.valueOf(value));
    }

    public static String getStringPref(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Activity.MODE_PRIVATE);
        return pref.getString(key, null);
    }

    public static void saveStringPref(Context context, String key, String value) {
        if (context==null){
            context= Constant.context;
        }
        SharedPreferences pref = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Activity.MODE_PRIVATE);
        if (value != null && value.length() > 0) {
            pref.edit().putString(key, value).apply();
        } else {
            pref.edit().remove(key).apply();
        }
    }

    public static Map<String, String> getKeyValueMap(String buyInfoNative) {
        Map<String, String> keyValueMap = new HashMap<>();
        String[] buyInfo = buyInfoNative.split("\\|");
        for (String aBuyInfo : buyInfo) {
            int pos = aBuyInfo.indexOf("=");
            if (pos > 0) {
                String[] itemInfo = aBuyInfo.split("=");
                if (itemInfo.length < 2) {
                    continue;
                }
                keyValueMap.put(itemInfo[0], itemInfo[1]);
            }
        }
        return keyValueMap;
    }

    public static int getIntFromString(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            e.getMessage();
        }
        return defValue;
    }

    public static float getFloatFromString(String str, float defValue) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            e.getMessage();
        }
        return defValue;
    }

    public static int getIntFromExtra(Intent intent, String key, int defValue) {
        String str = intent.getStringExtra(key);
        if (str != null && str.length() > 0) {
            try {
                return Integer.parseInt(str);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return defValue;
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static String urlEncodeUtf8(String a) {
        if (a == null) {
            return "";
        }
        try {
            return URLEncoder.encode(a, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String gettime() {
        String tt;
        Time t = new Time();
        t.setToNow();
        tt = t.toString();
        tt = tt.substring(0, 15);
        return tt;
    }


  /*  public static String timestampToDate(long time) {
        String dateTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_SEC_FULL);
        long timeLong = Long.valueOf(time);
        dateTime = simpleDateFormat.format(new Date(timeLong * 1000L));
        return dateTime;
    }*/

    //根据秒数转化为时分秒   00:00:00
    public static String getTime(int second) {
        if (second < 10) {
            return "00:0" + second;
        }
        if (second < 60) {
            return "00:" + second;
        }
        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "0" + minute + ":0" + second;
                }
                return "0" + minute + ":" + second;
            }
            if (second < 10) {
                return minute + ":0" + second;
            }
            return minute + ":" + second;
        }
        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + ":0" + minute + ":0" + second;
                }
                return "0" + hour + ":0" + minute + ":" + second;
            }
            if (second < 10) {
                return "0" + hour + ":" + minute + ":0" + second;
            }
            return "0" + hour + ":" + minute + ":" + second;
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + ":0" + minute + ":0" + second;
            }
            return hour + ":0" + minute + ":" + second;
        }
        if (second < 10) {
            return hour + ":" + minute + ":0" + second;
        }
        return hour + ":" + minute + ":" + second;
    }


    /**
     * 调用第三方浏览器打开
     * @param context
     * @param url 要浏览的资源地址
     */
    public static void openBrowser(Activity context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.EMPTY.parse(url));
// 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
// 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager()); // 打印Log   ComponentName到底是什么 L.d("componentName = " + componentName.getClassName());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(context.getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * 获取版本名称
     *
     * @param context 上下文
     *
     * @return 版本名称
     */
    public static String getVersionName(Context context) {

        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }


    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            return tm.getDeviceId();
        }
        return null;
    }



    /**
     * 获取音频文件的总时长大小
     *
     * @param filePath 音频文件路径
     * @return 返回时长大小
     */
    public static int getAudioFileVoiceTime(String filePath) {
        int mediaPlayerDuration = 0;
        if (filePath == null || filePath.isEmpty()) {
            return 0;
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayerDuration = mediaPlayer.getDuration();
            mediaPlayerDuration = mediaPlayerDuration/1000;
        } catch (IOException ioException) {
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
//        int dura = 193580;
        if(mediaPlayerDuration>100000){
            mediaPlayerDuration = mediaPlayerDuration/1000;
        }
        return mediaPlayerDuration;
    }


    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s){
        String res = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try{
            Date date = simpleDateFormat.parse(s);
            long ts = date.getTime()/1000;
            res = String.valueOf(ts);
        }catch (Exception e){
            
        }

      
        return res;
    }

}
