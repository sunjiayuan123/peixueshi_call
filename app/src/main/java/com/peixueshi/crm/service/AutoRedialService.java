package com.peixueshi.crm.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog.Calls;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mf.library.utils.DateUtils;
import com.mf.library.utils.SharedPreferencesUtil;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.CallLogBean;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AutoRedialService extends Service {

    public static Map<String, CallLogBean> callLogBeanMap = new HashMap<String, CallLogBean>();
    public static final String TAG = "AutoRedialService";
    private int mRetryCount = 0;
    private int mDialedCount = 0;
    private String mPhoneNumber = "10010";
    private boolean mJustCall = false;
    Handler myhandler;
    CallLogBean callLogBean;
    private Boolean simFlag = true;
    private List<Map<String, String>> dataList;
    private ContentResolver resolver;
    private Uri callUri = Calls.CONTENT_URI;
    private String[] columns = {Calls.CACHED_NAME// 通话记录的联系人
            , Calls.NUMBER// 通话记录的电话号码
            , Calls.DATE// 通话记录的日期
            , Calls.DURATION// 通话时长
            , Calls.TYPE};// 通话类型}


    myRun runnable = new myRun();

    class myRun implements Runnable {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void run() {

//            Boolean callCountFlag = checkCountFlag();
////            if (callCountFlag) {
////
////                callLogBean.phoneNumber = "10010";
////                callLogBean.date = DateUtils.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss");
////                ;
////                callLogBeanMap.put(callLogBean.phoneNumber, callLogBean);
////                SharedPreferencesUtil.getInstance(getApplication(), Constants.Prefrence).putHashMapData(Constants.CALLLOGMAPKEY, callLogBeanMap);
////                if (simFlag == true) {
////                    Log.e("AutoRedialService", "AutoRedialService===执行sim卡1");
////                    remoteCall(0);
////                } else {
////                    Log.e("AutoRedialService", "AutoRedialService===执行sim卡2");
////                    remoteCall(1);
////                }
////
////            }

            Boolean callTimesFlag = checkTimeFlag();
            if (callTimesFlag) {


                if (simFlag == true) {
                    Log.e("AutoRedialService", "AutoRedialService===执行sim卡1");
                    remoteCall(0);
                } else {
                    Log.e("AutoRedialService", "AutoRedialService===执行sim卡2");
                    remoteCall(1);
                }
            }

            myhandler.postDelayed(runnable, 60000);//每4000毫秒执行一次run方法
            mDialedCount++;
            simFlag = !simFlag;

        }
    }

    @Override
    public IBinder onBind(Intent msg) {
        // 使用 bind的办法，可以方便的在service和activity两个不同的进程直接交互，不过看代码很多啊
        return null;

    }

    private void PhoneCall() {
        Log.e(TAG, "PhoneCall===执行了！");
        mJustCall = false;
        callLogBean = new CallLogBean();
        myhandler = new Handler();
        myhandler.post(runnable);


    }

    private boolean ShouldStop() {
        return mDialedCount > mRetryCount;
    }

    private boolean LastCallSucceed() {

        if (mJustCall == false) {
            return false;
        }

        String[] projection = new String[]{Calls.NUMBER, Calls.DURATION};

        ContentResolver cr = getContentResolver();
        final Cursor cur = cr.query(Calls.CONTENT_URI,
                projection, null, null, Calls.DEFAULT_SORT_ORDER);
        if (cur.moveToFirst()) {
            int duration = cur.getInt(1);
            // 上次通话时间
            if (duration > 0) {

                return true;
            }
        }

        return false;
    }

    public void onCreate() {
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    public void onStart(Intent intent, int startID) {
        super.onStart(intent, startID);
        String tmp = "10010";
        if (tmp != null) {
            mPhoneNumber = tmp;
        }
        getPersimmionInfo();
        TelephonyManager telephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // 建立一个监听器来实时监听电话的通话状态
        telephonyMgr.listen(new TeleListener(this), PhoneStateListener.LISTEN_CALL_STATE);

        mDialedCount = 0;
    }

    public void onDestroy() {
        @SuppressLint("WrongConstant") TelephonyManager telephonyMgr = (TelephonyManager) getSystemService("phone");
        TeleListener teleListener = new TeleListener(this);
        telephonyMgr.listen(teleListener, 0);
        mDialedCount = 0;
        mRetryCount = 0;
        mPhoneNumber = "18613869712";
        if (myhandler != null) {
            myhandler.removeCallbacks(runnable);
        }
        myhandler = null;
        super.onDestroy();
    }

    class TeleListener extends PhoneStateListener {
        private String incomeNumber;
        private AutoRedialService manager;
        private MediaRecorder mediaRecorder;
        private File file;

        public TeleListener(AutoRedialService a) {
            this.manager = a;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                // 当处于待机状态中
                case TelephonyManager.CALL_STATE_IDLE: {
                    if (mediaRecorder != null) {

                        Log.e("Recorder", "RecorderPath" + Environment.getExternalStorageDirectory());
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                    }
                    if (manager.ShouldStop() || manager.LastCallSucceed()) {
                        manager.stopSelf();
                        break;
                    }
                    PhoneCall();
                    break;
                }
                // 当处于正在拨号出去，或者正在通话中
                case TelephonyManager.CALL_STATE_OFFHOOK: {
                   File mRecorderFile = new File(Environment.getExternalStorageDirectory(),
                            "121" + System.currentTimeMillis()
                                    + ".m4a");
                    System.out.println(">>>>>>接通>>>>>>>>" + state);
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                    //获得声音数据源（下麦克风）
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);//这个设置就是获取双向声音
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 按3gp格式输出
//                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    //所有android系统都支持的适中采样的频率
                    mediaRecorder.setAudioSamplingRate(44100);

                    //通用的AAC编码格式
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

                    //设置音质频率
                    mediaRecorder.setAudioEncodingBitRate(96000);
//                    createRecorderFile();//创建保存录音的文件夹
                   /* File mRecorderFile = new File(getFilesDir().getParent()
                            + "/recorderdemo/" + System.currentTimeMillis() + ".m4a");*/
                    if (!mRecorderFile.getParentFile().exists())
                        mRecorderFile.getParentFile().mkdirs();
                    try {
                        mRecorderFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaRecorder.setOutputFile(mRecorderFile.getAbsolutePath()); // 输出文件
                    try {
                        mediaRecorder.prepare(); // 准备
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaRecorder.start();
                    mJustCall = true;
                    break;
                }
                // 外面拨进来，好没有接拨号状态中..
                case TelephonyManager.CALL_STATE_RINGING: {
                    this.incomeNumber = incomingNumber;
                    Log.e("IDLE", ">>>>>>来电>>>>>>>>" + state);


                    break;
                }
                default:
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void remoteCall(int simFlag) {


        callPhone("18613869712", simFlag);
    }


    //指定SIM卡拨打
    public static final String[] dualSimTypes = { "subscription", "Subscription",
        "com.android.phone.extra.slot",
        "phone", "com.android.phone.DialingMode",
        "simId", "simnum", "phone_type",
        "simSlot" };

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void callPhone(String phoneNumber, int slotId) {
        Log.e(TAG, "call phone : phoneNumber=" + phoneNumber + ", slotId=" + slotId);
        PhoneAccountHandle phoneAccountHandle = getPhoneAccountHandle(slotId);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);//
        for (int i=0; i < dualSimTypes.length; i++) {//0 和 1代表切换卡1和卡2
            //0代表卡1,1代表卡2
            intent.putExtra(dualSimTypes[1], 0);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private PhoneAccountHandle getPhoneAccountHandle(int slotId) {
        TelecomManager tm = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        //PhoneAccountHandle api>5.1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (tm != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(this, "请允许拨号权限后再试", Toast.LENGTH_SHORT).show();
                }
                List<PhoneAccountHandle> handles = tm.getCallCapablePhoneAccounts();

//            List<PhoneAccountHandle> handles = (List<PhoneAccountHandle>) ReflectUtil.invokeMethod(tm, "getCallCapablePhoneAccounts");
                SubscriptionManager sm = SubscriptionManager.from(this);
                if (handles != null) {
                    for (PhoneAccountHandle handle : handles) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Toast.makeText(this, "请允许拨号权限后再试", Toast.LENGTH_SHORT).show();
                        }
                        SubscriptionInfo info = sm.getActiveSubscriptionInfoForSimSlotIndex(slotId);
                        if (info != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (TextUtils.equals(info.getIccId(), handle.getId())) {
                                    Log.e(TAG, "getPhoneAccountHandle for slot" + slotId + " " + handle);
                                    return handle;
                                }
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                if (TextUtils.equals(info.getSubscriptionId() + "", handle.getId())) {
                                    Log.e(TAG, "getPhoneAccountHandle for slot" + slotId + " " + handle);
                                    return handle;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    //创建保存录音的目录
    private void createRecorderFile() {
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();

        String filePath = absolutePath + "/recorder";
        File file = new File(filePath);
        Log.e("absolutePath", "createRecorderFile==" + file.getAbsolutePath());
        if (!file.exists()) {
            file.mkdir();
        }
    }

    //获取当前时间，以其为名来保存录音
    private String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String str = format.format(date);
        return str;

    }

    private Boolean checkTimeFlag() {
        Boolean timeFlag = true;
        String olddate = (String) SharedPreferencesUtil.getInstance(getApplication(), Constants.Prefrence).getData(Constants.CALLSTARTKEY, "");
        String newdate = DateUtils.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        if (!TextUtils.isEmpty(olddate)) {
            int minute = DateUtils.differentDaysByString(olddate, newdate);

            if (minute < 60) {
                if (mDialedCount > 40) {
                    timeFlag = false;
                }
            } else {
                String newdateupdate = DateUtils.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss");
                SharedPreferencesUtil.getInstance(getApplication(), Constants.Prefrence).putData(Constants.CALLSTARTKEY, newdateupdate);
                mDialedCount = 0;
            }
        } else {
            SharedPreferencesUtil.getInstance(getApplication(), Constants.Prefrence).putData(Constants.CALLSTARTKEY, newdate);
            timeFlag = true;
        }
        return timeFlag;
    }

    private Boolean checkCountFlag() {
        Boolean countFlag = true;
        callLogBeanMap = SharedPreferencesUtil.getInstance(getApplication(), Constants.Prefrence).getHashMapData(Constants.CALLLOGMAPKEY, CallLogBean.class);
        String newdate = DateUtils.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        if (callLogBeanMap != null && callLogBeanMap.size() > 0) {

            Iterator<Map.Entry<String, CallLogBean>> iterator = callLogBeanMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, CallLogBean> entry = iterator.next();
                CallLogBean callLogBean = entry.getValue();
                if (callLogBean != null) {
                    int minute = DateUtils.differentDaysByString(callLogBean.date, newdate);
                    if (minute > 60) {
                        iterator.remove();
                    }
                }
            }
            SharedPreferencesUtil.getInstance(getApplication(), Constants.Prefrence).putHashMapData(Constants.CALLLOGMAPKEY, callLogBeanMap);
            if (callLogBeanMap.size() > 40) {
                countFlag = false;
            }
        } else {

            countFlag = true;
        }
        return countFlag;
    }


    private void getPersimmionInfo() {
        initContacts();

    }

    private void initContacts() {
        dataList = getDataList();
    }

    /**
     * 读取数据
     *
     * @return 读取到的数据
     */
    private List<Map<String, String>> getDataList() {
        // 1.获得ContentResolver
        resolver = getContentResolver();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
        }
        // 2.利用ContentResolver的query方法查询通话记录数据库
        /**
         * @param uri 需要查询的URI，（这个URI是ContentProvider提供的）
         * @param projection 需要查询的字段
         * @param selection sql语句where之后的语句
         * @param selectionArgs ?占位符代表的数据
         * @param sortOrder 排序方式
         */
        Cursor cursor = resolver.query(callUri, // 查询通话记录的URI
                columns
                , null, null, Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        // 3.通过Cursor获得数据
        List<Map<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(Calls.CACHED_NAME));
            String number = cursor.getString(cursor.getColumnIndex(Calls.NUMBER));
            long dateLong = cursor.getLong(cursor.getColumnIndex(Calls.DATE));
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
            String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(Calls.DURATION));
            int type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE));
            String dayCurrent = new SimpleDateFormat("dd").format(new Date());
            String dayRecord = new SimpleDateFormat("dd").format(new Date(dateLong));
            String typeString = "";
            switch (type) {
                case Calls.INCOMING_TYPE:
                    //"打入"
                    typeString = "打入";
                    break;
                case Calls.OUTGOING_TYPE:
                    //"打出"
                    typeString = "打出";
                    String newdate = DateUtils.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss");
                    int minute = DateUtils.differentDaysByString(date, newdate);
                    if (minute < 60) {//只显示1小时以内通话记录，防止通     //话记录数据过多影响加载速度
                        Map<String, String> map = new HashMap<>();
                        //"未备注联系人"
                        map.put("name", (name == null) ? "未备注联系人" : name);//姓名
                        map.put("number", number);//手机号
                        map.put("date", date);//通话日期
                        // "分钟"
                        map.put("duration", (duration / 60) + "分钟");//时长
                        map.put("type", typeString);//类型
                        map.put("time", time);//通话时间
                        list.add(map);
                    } else {
                        return list;
                    }
                    Log.e("打出", "打出" + list.size() + "===" + list.toString());
                    break;
                case Calls.MISSED_TYPE:
                    //"未接"
                    typeString = "未接";
                    break;
                default:
                    break;
            }

        }
        return list;
    }
}