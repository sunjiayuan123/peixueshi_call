package com.peixueshi.crm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * 开启广播
 * 去电监听,开启服务
 */
public class OutGoingReceiver extends BroadcastReceiver {

    private static Context context;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        //只获取去电广播

        this.context = context;
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            Log.d("OutGoingReceiver","收到广播了1");
            String number = intent.getExtras().getString(Intent.EXTRA_PHONE_NUMBER);

            Log.d("OutGoingReceiver", "onCallStateChanged1: " +"电话呼出"+ number);
            CallServiceLister callServiceLister = CallServiceLister.getInstance();
            callServiceLister.outCall(number);

        }
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            Log.d("OutGoingReceiver","收到广播了2");
            //电话状态改变
        }

    }

}
