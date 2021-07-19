package com.peixueshi.crm.app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Properties;

public class SystemDevice {
    private static final String TAG = "system";
    /** 手机唯一码 */
    public String DEVICE_HASH;

    /** 牌子 */
    public String BRAND;

    /** 制造商 */
    public String MANUFACTURER;

    /** 硬件 */
    public String HARDWARE;

    /** CPU1 */
    public String CPU_ABI;

    /** CPU2 */
    public String CPU_ABI2;

    /** 整个产品 */
    public String PRODUCT;

    public String ID;

    /** 指纹 */
    public String FINGERPRINT;

    /** RADIO */
    public String RADIO;

    public String BOOTLOADER;

    /** SDK版本 */
    public String SDK_VERSION;

    private Context context;

    /** Android手机sdk = a,Android Pad sdk=ap */
    public String SDK_PLATFORM;

    /** 手机信息 */
    public String UA;

    /** 新增渠道信息 */
    public static final String ASSETSNAME_NEW_V2 = "sina/configv2.properties";
    public static final String ASSETSNAME_NEW = "sina/config.properties";
    public static final String ASSETSNAME_OLD = "sinasng.properties";
    /** 默认渠道号 */
    public static final String DEFAULT_CHANNEL = "108610011001";
    public static final String CHANNEL_KEY = "channel";
    private static final String emptyMac = "02:00:00:00:00:00";

    public static String loadChannel(Context context) {
        String channel = loadAssertChannel(context, ASSETSNAME_NEW_V2);
        if (TextUtils.isEmpty(channel)) {
            channel = loadAssertChannel(context, ASSETSNAME_NEW);
            if (TextUtils.isEmpty(channel)) {
                channel = loadAssertChannel(context, ASSETSNAME_OLD);
            }
            if (TextUtils.isEmpty(channel)) {
                channel = DEFAULT_CHANNEL;
            }
        }
        if(channel !=null){
            return channel.trim();
        }
        return "";
    }

    private static String loadAssertChannel(Context context, String assertName) {
        String channel = null;
        InputStream in = null;
        try {
            // assert目录下
            Properties props = new Properties();
            in = context.getAssets().open(assertName);
            props.load(in);
            channel = props.getProperty(CHANNEL_KEY, "");
            return channel;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static SystemDevice instance = null;

    private SystemDevice(Context context) {
        this.context = context.getApplicationContext();
    }

    public Context getContext() {
        return context;
    }

    public static SystemDevice getInstance() {
        return instance;
    }

    public synchronized static void initDevice(Context context) {
        if (instance == null) {
            instance = new SystemDevice(context.getApplicationContext());
            instance.init(context);
        }
    }

    public static boolean hasPermission(Context mContext, String permissionName) {
        PackageManager pm = mContext.getPackageManager();
        if (pm.checkPermission(permissionName, mContext.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
    private static String deviceid_key = "deviceid";
    public static String getDeviceId(Context mContext) {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
        }
        String id = null;
        try {
            if (hasPermission(mContext, "android.permission.READ_PHONE_STATE")) {
//                id = telephonyManager.getDeviceId();
            }
        } catch (Exception e) {
        }
        // GET mac 确保deviceID有,MAC重启的时候可能是没有值，因此不作 deviceId + mac，确保一个手机一定不会有不同值
        if (TextUtils.isEmpty(id)) {
            id = getMAC(mContext);
            // Setting android_id
            if (TextUtils.isEmpty(id)) {
                String androidID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                id = androidID + Build.SERIAL;
                if(TextUtils.isEmpty(id)){
                    id = SharePreferenceUtils.getSpValueAsString(mContext,deviceid_key,"");
                    if(TextUtils.isEmpty(id)){
//                        id = CommonUtils.getuuid();
//                        SharePreferenceUtils.updateSpValueAsString(mContext,deviceid_key,id);
                    }
                }
            }
        }
        return id;
    }

    public static String getMAC(Context context) {
        try {
            WifiManager localWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            if (hasPermission(context, "android.permission.ACCESS_WIFI_STATE")) {
                WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
                if (localWifiInfo == null || localWifiInfo.getMacAddress() == null) {
                    return "";
                }
                if (emptyMac.equals(localWifiInfo.getMacAddress().trim())) {
                    return "";
                } else {
                    return localWifiInfo.getMacAddress().trim();
                }
            }
        } catch (Exception e) {
        }
        return "";


    }

    private void init(Context context) {
        BRAND = Build.BRAND;
        MANUFACTURER = Build.MANUFACTURER;

        // android.os.Build
        Class<Build> build = Build.class;
        try {
            Field hardWAREF = build.getDeclaredField("HARDWARE");
            HARDWARE = hardWAREF.get(null).toString();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            Field CPU_ABIF = build.getDeclaredField("CPU_ABI2");
            CPU_ABI2 = CPU_ABIF.get(null).toString();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            Field RADIOF = build.getDeclaredField("RADIO");
            RADIO = RADIOF.get(null).toString();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            Field BOOTLOADERF = build.getDeclaredField("BOOTLOADER");
            BOOTLOADER = BOOTLOADERF.get(null).toString();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        CPU_ABI = Build.CPU_ABI;
        PRODUCT = Build.PRODUCT;
        ID = Build.ID;
        FINGERPRINT = Build.FINGERPRINT;
        SDK_VERSION = Build.VERSION.SDK;
        DEVICE_HASH = readDeviceHashCode();

        if (isTablet(context)) {
            SDK_PLATFORM = "ap";
        } else {
            SDK_PLATFORM = "a";
        }
        try {
            // 避免出现 ZTE ZTE-N881F
            if (Build.MODEL.toUpperCase().contains(Build.MANUFACTURER)) {
                UA = URLEncoder.encode(Build.MODEL.toUpperCase(), "UTF-8");
            } else {
                UA = URLEncoder.encode(Build.MANUFACTURER + " " + Build.MODEL.toUpperCase(), "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否是平板
     * 
     * @param context
     * @return
     */
    private static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private String readDeviceHashCode() {
        return getDeviceId(context);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"DEVICE_ID\":\"").append(DEVICE_HASH).append("\",\"").append("BRAND\":\"").append(BRAND)
                .append("\",\"").append("MANUFACTURER\":\"").append(MANUFACTURER).append("\",\"")
                .append("HARDWARE\":\"").append(HARDWARE).append("\",\"").append("CPU_ABI\":\"").append(CPU_ABI)
                .append("\",\"").append("CPU_ABI2\":\"").append(CPU_ABI2).append("\",\"").append("PRODUCT\":\"")
                .append(PRODUCT).append("\",\"").append("ID\":\"").append(ID).append("\",\"")
                .append("FINGERPRINT\":\"").append(FINGERPRINT).append("\",\"").append("RADIO\":\"").append(RADIO)
                .append("\",\"").append("BOOTLOADER\":\"").append(BOOTLOADER).append("\",\"")
                .append("SDK_VERSION\":\"").append(SDK_VERSION).append("\",\"").append("VERSION_NAME\":\"")
                .append("\",\"").append("BOOTLOADER\":\"").append(BOOTLOADER).append("\",\"").append("UA\":\"")
                .append(UA).append("\"}");
        return sb.toString();
    }
    public static String getWebViewUA(Context context){
        String webViewUA = "";
        try {
            // 避免出现 ZTE ZTE-N881F
            if (Build.MODEL.toUpperCase().contains(Build.MANUFACTURER)) {
//                webViewUA = URLEncoder.encode(Build.MODEL.toUpperCase(), "UTF-8");
                webViewUA = Build.MODEL;
            } else {
//                webViewUA = URLEncoder.encode(Build.MANUFACTURER + " " + Build.MODEL.toUpperCase(), "UTF-8");
                webViewUA = Build.MANUFACTURER + "-" + Build.MODEL;
            }

            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
//            webViewUA = webViewUA+"___dahuilangyouxi___"+version+"___android___android"+Build.VERSION.RELEASE;
//            webViewUA = webViewUA+"___dhl___"+version+"___android___android"+Build.VERSION.RELEASE;
            webViewUA = webViewUA+"___dhl___"+version+"___android"+Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webViewUA;
    }

    public static int getVersionCode(Context context){
        int versionCode = 0;
        try{
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        }catch (Exception e){
            e.printStackTrace();
        }

        return versionCode;
    }

    public static String getVersionName(Context context){
        String versionName = "";
        try{
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }

        return versionName;
    }
}
