package com.peixueshi.crm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneReceiver";
    private boolean mInComingFlag = false;
    private static String ACTION_NEW_INCOMMING_CALL = "android.intent.action.PHONE_STATE";


    //
//    //发送自定义广播，PhoneReceiver接收
//    Intent intent = new Intent(ACTION);
//                intent.putExtra("Msg", "helloReceiver");
//    sendBroadcast(intent);
    private PhoneStateListener listen = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
           // Log.d(TAG, "onCallStateChanged: " + state);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    //电话打进响铃中，电话打出没有此状态
                    mInComingFlag = true;
                 //   Log.d(TAG, "onCallStateChanged: " + incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //挂断电话
                    if (mInComingFlag) {
                       // Log.d(TAG, "onCallStateChanged: " + "call hang up");
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // 当处于正在拨号出去 或者 电话接听
                    if (mInComingFlag) {
                       // Log.d(TAG, "onCallStateChanged: " + "接听电话：" + incomingNumber);
                    }
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            //电话打出
            mInComingFlag = false;
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d(TAG, "onCallStateChanged: " + "call phoneNumber：" + phoneNumber);
        }
        if (intent.getAction().equals(ACTION_NEW_INCOMMING_CALL)) {
            //电话状态改变
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            tm.listen(listen, PhoneStateListener.LISTEN_CALL_STATE);

        }
    }
}