package com.peixueshi.crm.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.peixueshi.crm.utils.EnjoyPreference;

/**
 * Created by zhaobaolei on 2018/2/26.
 */

public class NetUtils {

    //没有网络连接
    public static final int NETWORN_NONE = 0;
    //wifi连接
    public static final int NETWORN_WIFI = 1;
    //手机网络数据连接类型
    public static final int NETWORN_2G = 2;
    public static final int NETWORN_3G = 3;
    public static final int NETWORN_4G = 4;
    public static final int NETWORN_MOBILE = 5;

    /**
     *      * 获取当前网络连接类型
     *      * @param context
     *      * @return
     *      
     */
    public static int getNetworkState(Context context) {
        //获取系统的网络服务
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //如果当前没有网络
        if (null == connManager)
            return NETWORN_NONE;

        //获取当前网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORN_NONE;
        }

        // 判断是不是连接的是不是wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORN_WIFI;
                }
        }
        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        //如果是2g类型
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NETWORN_2G;
                        //如果是3g类型
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NETWORN_3G;
                        //如果是4g类型
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NETWORN_4G;
                        default:
                            //中国移动 联通 电信 三种3G制式
                            if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                return NETWORN_3G;
                            } else {
                                return NETWORN_MOBILE;
                            }
                    }
                }
        }
        return NETWORN_NONE;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static Drawable loadImageFromNetwork(final Activity activity, final String imageUrl)
    {
        Drawable drawable = null;
        Bitmap myBitmap = null;
//        try {
//            // 可以在这里通过文件名来判断，是否本地有此图片
//            drawable = Drawable.createFromStream(
//                    new URL(imageUrl).openStream(), "image.png");
//        } catch (IOException e) {
//            Log.e("通过url得到drawable-报错", e.getMessage());
//        }
//        if (drawable == null) {
//            Log.e("通过url得到drawable", "null drawable");
//        } else {
//            Log.e("通过url得到drawable", "not null drawable");
//        }
        try {
           myBitmap = null; // = Glide.with(activity)
//                    .load(imageUrl)
//                    .asBitmap() //必须
//                    .centerCrop()
//                    .into(400, 208)
//                    .get();
            Log.e("动态tab图片", "mywidth " + myBitmap.getWidth()); //200px
            Log.e("动态tab图片", "myheight " + myBitmap.getHeight()); //200px
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(myBitmap!=null){
            drawable = new BitmapDrawable(myBitmap);
        }
        return drawable ;
    }

    /**
     * 如果是非wifi网络，则给出提示
     */
    public static boolean isWifiNet(Context context) {
        if (NetUtils.getNetworkState(context) != NetUtils.NETWORN_WIFI) {
            return false;
        } else {
            return true;
        }
    }


    public static boolean isEnable(Activity activity){
        if(NetUtils.getNetworkState(activity) != NetUtils.NETWORN_WIFI){
           /* activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(activity, "非wifi连接不能获取首咨、库存和订单数据", Toast.LENGTH_SHORT).show();
                }
            });*/
            return false;
        }else{
//                下面的代码可以获取当当前设备连接到的网络的wifi信息

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
                   /* activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "非YuXue wifi不能获取首咨、库存和订单数据", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    return false;
                }
            }
            return true;
        }

    }
}
