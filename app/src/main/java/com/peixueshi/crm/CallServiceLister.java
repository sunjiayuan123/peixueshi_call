package com.peixueshi.crm;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.peixueshi.crm.activity.LoginActivity;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.utils.AESOperator;
import com.peixueshi.crm.utils.Constant;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.PhoneInfoUtils;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CallServiceLister {
    private static String TAG = "CallService";

    private static int isInComing = 0;//0代表呼出 1呼入
    private TelephonyManager mTelephonyManager;
    private static long currentTime;
    private static String phoneNumber;

    private static CallServiceLister callServiceLister;
    private int type;

    private CallServiceLister() {
    }

    public static synchronized CallServiceLister getInstance() {
        if (callServiceLister == null) {
            callServiceLister = new CallServiceLister();
            Log.d("OutGoingReceiver", "onCallStateChanged1: " + "开启服务监听");
            phoneBrand = android.os.Build.BRAND;
        }
        return callServiceLister;
    }

    private CallListener mCallListener = new CallListener(new CallListener.OnIncomingListener() {
        @Override
        public void incomingOn(String number) {
            Constants.isRunningCall = true;

            isInComing = 1;
            phoneNumber = number;
            currentTime = System.currentTimeMillis();
            Log.d("OutGoingReceiver", "onCallStateChanged1: " + "来电接通,开启录音" + number);
            String localPhone = new PhoneInfoUtils(MainActivity.mainContext).getNativePhoneNumber();
            Constants.localCallNumber = localPhone;
        }
    }, new CallListener.OnCallOffListener() {
        @Override
        public void off() {
            Constants.isRunningCall = false;
            Log.d("OutGoingReceiver", "onCallStateChanged1: " + "电话挂断，不分来去电，停止录音");
            String time = getTimeString(currentTime, System.currentTimeMillis());
            currentTime = 0;
            Log.d("OutGoingReceiver", "onCallStateChanged1: " + time);
            getCallUserHistory(MainActivity.mainContext);
        }
    });


    public void outCall(String number) {
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager) MainActivity.mainContext.getSystemService(Context.TELEPHONY_SERVICE);
            mTelephonyManager.listen(mCallListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        Constants.isRunningCall = true;
        Log.d("OutGoingReceiver", "onCallStateChanged1: " + "进入stratCommand开始录音");
        phoneNumber = number;
        isInComing = 0;//呼出
        currentTime = System.currentTimeMillis();

        fileNameKey = System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "");
        Log.d("OutGoingReceiver", "onCallStateChanged1: " + phoneBrand);
        if (phoneBrand.toUpperCase().equals(Constant.HUAWEI)) {
            fileNameKey = fileNameKey + ".amr";
        } else if (phoneBrand.toUpperCase().equals(Constant.XIAOMI)) {//小米 sound_recorder/call/
            fileNameKey = fileNameKey + ".mp3";
        } else {
            fileNameKey = fileNameKey + ".mp3";
        }
        Log.d("OutGoingReceiver", "onCallStateChanged1: " + fileNameKey);

    }


    private static String getTimeString(long currentTime, long endTime) {

        SimpleDateFormat sdfTime = new SimpleDateFormat("mm:ss");//yyyy/MM/dd
        String mills = sdfTime.format(new Date(endTime - currentTime));
        String[] times = mills.split(":");
        long time = Integer.valueOf(times[0]) * 60 + Integer.valueOf(times[1]);//Integer.valueOf(times[0])*3600+
        return time + "";
    }


    private Uri callUri = CallLog.Calls.CONTENT_URI;
    private String[] columns = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE};// 通话类型}

    private long lastCallFirstTime;

    public void getCallUserHistory(Context context) {
        new AsyncTask<String, Integer, String>() {


            @Override
            protected String doInBackground(String... strings) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                //获取通话记录
                Cursor cursor = MainActivity.mainContext.getContentResolver().query(callUri, // 查询通话记录的URI
                        columns
                        , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
                );
                Log.i(TAG, "cursor count:" + cursor.getCount());

                if (cursor.moveToFirst()) {//cursor.moveToNext()
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //姓名
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
                    long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)); //获取通话日期

                    dateLong = dateLong / 1000;
                    if (String.valueOf(dateLong).equals(String.valueOf(lastCallFirstTime))) {
//                         || String.valueOf(dateLong).equals(Constants.callAt) || dateLong-Integer.valueOf(Constants.callAt)==1 || dateLong-Integer.valueOf(Constants.callAt)==-1
                        Log.d("OutGoingReceiver", "onCallStateChanged1: " + "数据重复");
                        return;
                    }
                    lastCallFirstTime = dateLong;

                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
                    String timeMills = new SimpleDateFormat("yyyymmddhhmmss").format(new Date(dateLong));
                    String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));
                    int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));//获取通话时长，值为多少秒
                    int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)); //获取通话类型：1.呼入2.呼出3.未接
                    String dayCurrent = new SimpleDateFormat("dd").format(new Date());
                    String dayRecord = new SimpleDateFormat("dd").format(new Date(dateLong));


                    String path;
                    File stroFile;
                    //获取自动录音文件路径
                    if (phoneBrand.toUpperCase().equals(Constant.HUAWEI)) {
                        path = "/Sounds/CallRecord/";
                        stroFile = new File(Environment.getExternalStorageDirectory() + path);
                    } else if (phoneBrand.toUpperCase().equals(Constant.XIAOMI)) {//小米 sound_recorder/call/
                        path = "/MIUI/sound_recorder/call_rec/";
                        stroFile = new File(Environment.getExternalStorageDirectory() + path);
                    } else {
                        path = "/MIUI/sound_recorder/call_rec/";
                        stroFile = new File(Environment.getExternalStorageDirectory() + path);
                    }


                    if (duration != 0 && stroFile.exists() && stroFile.isDirectory()&&type!=3) {
                        File[] childFile = stroFile.listFiles();
                        if (childFile == null || childFile.length == 0) {

                        } else {
                            long maxFilename = 0;
                            String maxFile = "";
                            File maxFiletoDelete = null;
                            for (File f : childFile) {
                                String fileName = f.getName();
                                Log.d("yuxueApplication", "子文件s" + fileName);
                                if (!fileName.contains("_")) {
                                    continue;
                                }
                                String fileNewName = fileName.split("_")[1];
                                //遍历所有文件,按时间desc排序,获取时间最大的第一个重命名
                                String timeName = fileNewName.substring(0, fileNewName.lastIndexOf("."));


                                long currFileTime = Long.valueOf(timeName).longValue();
                                if (currFileTime > maxFilename) {
                                    maxFilename = currFileTime;
                                    maxFile = fileName;
                                    maxFiletoDelete = f;
                                }
                            }
                            if (number.length() != 11 || !number.startsWith("1")) {
                                if (maxFiletoDelete != null) {
                                    maxFiletoDelete.delete();
                                }
                                Log.d("OutGoingReceiver", "已删除: " + maxFile);
                            }
                            if (childFile.length > 0) {
                                //获取到最大时间文件路径
                                int empId = 0;
                                if (Constants.loginUserInfo != null) {
                                    empId = Constants.loginUserInfo.getEmp_id();
                                }
                                if (empId == 0) {
                                    if (MainActivity.mainContext != null) {
                                        String emp = EnjoyPreference.readString(MainActivity.mainContext, "emp_id");
                                        if (emp != null && !emp.equals("")) {
                                            empId = Integer.valueOf(emp);
                                        }
                                    }
                                }

                                if (empId == 0) {//重新登录
                                    if (MainActivity.mainContext != null) {
                                        EnjoyPreference.saveString(MainActivity.mainContext, "user_phone", "");
                                        EnjoyPreference.saveString(MainActivity.mainContext, "pass", "");
                                        EnjoyPreference.saveString(MainActivity.mainContext, "acc_token", "");
                                        EnjoyPreference.saveString(MainActivity.mainContext, "emp_id", "");
                                        Intent intent = new Intent(MainActivity.mainContext, LoginActivity.class);
                                        MainActivity.mainContext.startActivity(intent);
                                    }
                                    return;
                                }
                                String password = "0123456789abcdef";   //此处使用AES-128-CBC加密模式，key需要为16位
                                String encryptResult = "";
                                Log.d("OutGoingReceiver", "加密前: " + number);
                                try {
                                    encryptResult = AESOperator.encrypt(number, password);
                                    Log.e("tag", "onPostExecutes: "+dateLong+"==="+lastCallFirstTime+"===="+number +"===="+duration+"===="+type);

                                  /* String aa =  AESOperator.decrypt(encryptResult, password);
                                    Log.d("OutGoingReceiver", "解密后: " +aa);*/
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                newFileNameKey = encryptResult + "*" + duration + "*" + isInComing + "*" + dateLong + "*" + empId + "*" + fileNameKey;
                                File oldFile = new File(stroFile.getAbsolutePath() + "/" + maxFile);
                                File newFile = new File(stroFile.getAbsolutePath() + "/" + newFileNameKey);
                                boolean isRameSuccess = oldFile.renameTo(newFile);
                            }


                        }

                    }

                    if (duration == 0) {
                        int empId = 0;
                        if (Constants.loginUserInfo != null) {
                            empId = Constants.loginUserInfo.getEmp_id();
                        }
                        if (empId == 0) {
                            if (MainActivity.mainContext != null) {
                                String emp = EnjoyPreference.readString(MainActivity.mainContext, "emp_id");
                                if (emp != null && !emp.equals("")) {
                                    empId = Integer.valueOf(emp);
                                }
                            }
                        }
                        if (empId == 0) {//重新登录
                            if (MainActivity.mainContext != null) {
                                EnjoyPreference.saveString(MainActivity.mainContext, "user_phone", "");
                                EnjoyPreference.saveString(MainActivity.mainContext, "pass", "");
                                EnjoyPreference.saveString(MainActivity.mainContext, "acc_token", "");
                                EnjoyPreference.saveString(MainActivity.mainContext, "emp_id", "");
                                Intent intent = new Intent(MainActivity.mainContext, LoginActivity.class);
                                MainActivity.mainContext.startActivity(intent);
                                MainActivity.mainContext.finish();
                            }
                            return;
                        }
                        String password = "0123456789abcdef";   //此处使用AES-128-CBC加密模式，key需要为16位
                        String encryptResult = "";
                        Log.d("OutGoingReceiver", "加密前: " + number);
                        try {
                            encryptResult = AESOperator.encrypt(number, password);
                            Log.e("tag", "onPostExecute: "+dateLong+"==="+lastCallFirstTime+"===="+number +"===="+duration+"===="+type);
                                  /* String aa =  AESOperator.decrypt(encryptResult, password);
                                    Log.d("OutGoingReceiver", "解密后: " +aa);*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        newFileNameKey = encryptResult + "*" + duration + "*" + isInComing + "*" + dateLong + "*" + empId + "*" + fileNameKey;
                    }

                    number = number.replaceAll(" ", "");
                    if (number.length() == 11 && number.startsWith("1")) {
                        requestCallIdle(duration + "", number, Constants.iccID, dateLong);
                        //调接口
                        boolean isshouZi = Constants.isShouZi;
                        if (isshouZi) {//首咨
                            type = 2;
                        } else {     //库存
                            type = 1;
                        }
                        requestCallpofs(type, number);//挂断电话
                    }
//                    duration>0 &&
                    if (Constants.qiniuToken != null) {
                        MainActivity.initInfos(Constants.qiniuToken);
                    }
                }
            }
        }.execute();

    }


    private void requestCallpofs(int typ, String number) {
        try {

            String reqUrl = Constants.host + "user/callpofs?typ=" + typ + "&phone=" + number;

            OkHttpUtils.get(null, reqUrl, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(MainActivity.mainContext, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject object) throws
                        Exception {
                    if (object.getString("err") != null && object.getString("err").equals("0")) {
                                /*if(Constants.qiniuToken != null){
                                    MainActivity.initInfos(Constants.qiniuToken);
                                }*/
                        Log.d("OutGoingReceiver", "onCallStateChanged1: " + "通话记录");
                    }
                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    private void requestCallIdle(String mills, String number, String ssid, Long callat) {
        try {

            String reqUrl = Constants.host + "work/call_add?s_phone=" + Constants.localCallNumber + "&p_number=" + ssid + "&t_phone=" + number + "&duration=" + mills + "&qn_key=" + newFileNameKey + "&call_type=" + isInComing + "&c_at=" + callat;

            OkHttpUtils.get(null, reqUrl, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(MainActivity.mainContext, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject object) throws
                        Exception {
                    if (object.getString("err") != null && object.getString("err").equals("0")) {
                                /*if(Constants.qiniuToken != null){
                                    MainActivity.initInfos(Constants.qiniuToken);
                                }*/
                        Log.d("OutGoingReceiver", "onCallStateChanged1: " + "通话记录成功");
                    }
                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    private String fileNameKey;
    static String phoneBrand;

    private String newFileNameKey;


    /*二、来电监听：*/
    public static class CallListener extends PhoneStateListener {
        //上个电话状态，当上个状态为响铃，当前状态为OFFHOOK时即为来电接通
        private int mLastState = TelephonyManager.CALL_STATE_IDLE;
        private OnIncomingListener mOnIncomingListener;
        private OnCallOffListener mOnCallOffListener;
        //来电号码
        private String mIncomingNumber;

        public CallListener(OnIncomingListener listener, OnCallOffListener callOffListener) {
            this.mOnIncomingListener = listener;
            this.mOnCallOffListener = callOffListener;
        }


        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: // 空闲状态，即无来电也无去电
                    // Log.d("OutGoingReceiver", "onCallStateChanged1: " +"电话挂断1，不分来去电，停止录音");
                    if (mLastState == TelephonyManager.CALL_STATE_OFFHOOK && mOnCallOffListener != null) {
                        mOnCallOffListener.off();
                        // getCallHistory();
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING: // 来电响铃
                    mIncomingNumber = String.valueOf(incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: // 摘机，即接通
                    //此处添加一系列功能代码
                    if (mLastState == TelephonyManager.CALL_STATE_RINGING && mOnIncomingListener != null) {
                        mOnIncomingListener.incomingOn(mIncomingNumber);
                    }
                    break;
            }
            mLastState = state;
            super.onCallStateChanged(state, incomingNumber);
        }


        //来电接通
        public interface OnIncomingListener {
            void incomingOn(String number);
        }

        //电话挂断监听，不分来去电
        public interface OnCallOffListener {
            void off();
        }

    }



    /* 去电日志获取线程：*/

    //系统通话信息的TAG，不同机型TAG不同，自己获取
    public final static String LOG_TAG = "your phone system phone call tag";

    //日志里接通的信息，不同机型信息不同，自己研究获取
    public final static String OUTGOING_ON = "call_state = 5";


}
