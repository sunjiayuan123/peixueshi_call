package com.peixueshi.crm.view;

import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.peixueshi.crm.MainActivity;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.NetUtils;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.audio.interfaces.IAudioCallback;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.utils.Constant;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.PhoneInfoUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CallService extends Service implements IAudioCallback {
private static String TAG = "CallService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


  /*  @Override
    public void onCreate() {
        super.onCreate();
        PhoneReceiver screenReceiver = new PhoneReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(screenReceiver, filter);
    }*/


    private int isInComing = 0;//0代表呼出 1呼入
    private TelephonyManager mTelephonyManager;
    private long currentTime;
    private CallListener mCallListener = new CallListener(new CallListener.OnIncomingListener() {
        @Override
        public void incomingOn(String number) {
            //来电接通,开启录音
            isInComing = 1;
            phoneNumber = number;
            currentTime = System.currentTimeMillis();
            Log.d("OutGoingReceiver", "onCallStateChanged1: " +"来电接通,开启录音"+ number);
//            startRecord(number);
            String localPhone = new PhoneInfoUtils(CallService.this).getNativePhoneNumber();
            Constants.localCallNumber = localPhone;
        }
    }, new CallListener.OnCallOffListener(){
        @Override
        public void off() {
            //电话挂断，不分来去电，停止录音
//           stopRecord();//TODO 挂断走打电话接口，传通话时长以及是否接通
//            audioRecorder.pauseRecord();
          /*  Message m = handler.obtainMessage();//获取一个Message
            m.what = STOPAUDIO;
            handler.sendMessage(m);*/
//            MainActivity.instance.finishAndReset();
           //查询通话记录
            Log.d("OutGoingReceiver", "onCallStateChanged1: " +"电话挂断，不分来去电，停止录音");
            String time = getTimeString(currentTime,System.currentTimeMillis());
            currentTime = 0;
            Log.d("OutGoingReceiver", "onCallStateChanged1: "+ time);
          //  Constants.localCallNumber = "0";
//            requestCallIdle(time);
            getCallUserHistory(getApplicationContext());
        }
    });



    private int duratTotal = 50;
    private int UPDATE = 1;
    private int STOPAUDIO = 2;
   /* ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int position, mMax, sMax;
            while (!Thread.currentThread().isInterrupted()) {
                    Message m = handler.obtainMessage();//获取一个Message
                    m.what = UPDATE;
                    handler.sendMessage(m);
                    //  handler.sendEmptyMessage(UPDATE);
                    try {
                        Thread.sleep(1000);// 每间隔1秒发送一次更新消息
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
    };// 自动改变进度条的线程*/

   /* Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //更新UI

            if(msg.what == STOPAUDIO){
                if(duratTotal == -1){
                    //挂断电话停止录音重置 停止线程
                    if(mExecutorService.isShutdown()){
                        mExecutorService.shutdown();
                    }
                    duratTotal = 50;
                }
            }
           else if (msg.what == UPDATE) {
                try {

                    if(duratTotal == 0){
                        MainActivity.instance.videoStart(fileNameKey);//50s暂定一次
                        MainActivity.instance.videoStart(fileNameKey);//50s暂定一次
                       duratTotal = 50;
                    }else if(duratTotal>0){
                        duratTotal--;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    };*/

    private String getTimeString(long currentTime, long endTime){
        //传入字串类型 end:2016/06/28 08:30 expend: 03:25
       /* long longEnd = getTimeMillis(endTime);
        String[] expendTimes = expendTime.split(":");   //截取出小时数和分钟数
        long longExpend = Long.parseLong(expendTimes[0]) * 60 * 60 * 1000 + Long.parseLong(expendTimes[1]) * 60 * 1000;*/
        SimpleDateFormat sdfTime = new SimpleDateFormat("mm:ss");//yyyy/MM/dd
        String mills = sdfTime.format(new Date(endTime - currentTime));
        String[] times = mills.split(":");
        long time = Integer.valueOf(times[0])*60+Integer.valueOf(times[1]);//Integer.valueOf(times[0])*3600+
        return time+"";
    }

    private long getTimeMillis(String strTime) {
        long returnMillis = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date d = null;
        try {
            d = sdf.parse(strTime);
            returnMillis = d.getTime();
        } catch (ParseException e) {
            Toast.makeText(CallService.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return returnMillis;
    }


    private Uri callUri = CallLog.Calls.CONTENT_URI;
    private String[] columns = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE};// 通话类型}

    private long lastCallFirstTime;
    public void getCallUserHistory(Context context){
        new AsyncTask<String,Integer,String>(){


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
                Cursor cursor = getContentResolver().query(callUri, // 查询通话记录的URI
                        columns
                        , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
                );
                Log.i(TAG,"cursor count:" + cursor.getCount());

                if (cursor.moveToFirst()) {//cursor.moveToNext()
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //姓名
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
                    long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)); //获取通话日期

                 /*   double douTime = Math.round(dateLong*1.0/1000L);
                    double resuTime = (int)Math.rint(douTime);

                    if(Integer.parseInt(resuTime+"") - lastCallFirstTime<= 3 && Integer.parseInt(resuTime+"") - lastCallFirstTime>= -3){
                        return;
                    }
                    lastCallFirstTime = Integer.parseInt(resuTime+"");*/

                    if(String.valueOf(dateLong).equals(String.valueOf(lastCallFirstTime))){
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
                    if(phoneBrand.toUpperCase().equals(Constant.HUAWEI)){
                        path = "/Sounds/CallRecord/";
                        stroFile = new File(Environment.getExternalStorageDirectory()+path);
                    }else if(phoneBrand.toUpperCase().equals(Constant.XIAOMI)){//小米 sound_recorder/call/
                        path = "/MIUI/sound_recorder/call_rec/";
                        stroFile = new File(Environment.getExternalStorageDirectory()+path);
                    }else{
                        path = "/MIUI/sound_recorder/call_rec/";
                        stroFile = new File(Environment.getExternalStorageDirectory()+path);
                    }


                    if(duration!= 0 && stroFile.exists() && stroFile.isDirectory()){
                        File[] childFile = stroFile.listFiles();
                        if(childFile == null || childFile.length == 0){

                        }else{
                            long maxFilename = 0;
                            String maxFile = "";
                            for(File f : childFile){
                                String fileName =  f.getName();
                                Log.d("yuxueApplication","子文件nn"+fileName);
                                if(!fileName.contains("_")){
                                    continue;
                                }
                                String fileNewName = fileName.split("_")[1];
                                //遍历所有文件,按时间desc排序,获取时间最大的第一个重命名
                               String timeName =  fileNewName.substring(0,fileNewName.lastIndexOf("."));


                               long currFileTime = Long.valueOf(timeName).longValue();
                               if(currFileTime > maxFilename){
                                   maxFilename = currFileTime;
                                   maxFile = fileName;
                               }
                            }
                            if(childFile.length>0){
                                //获取到最大时间文件路径
                                File oldFile = new File(stroFile.getAbsolutePath()+"/"+maxFile);
                                File newFile = new File(stroFile.getAbsolutePath()+"/"+fileNameKey);
                                boolean isRameSuccess = oldFile.renameTo(newFile);
                            }


                        }

                    }
                   /* if(duration == 0){
                        File file = new File(Environment.getExternalStorageDirectory() +  Constants.stroDir+fileNameKey);
                        if(file.exists()){
                            Log.d("YuXUeApplication","要删除的文件"+fileNameKey);
                            file.delete();//删除时长为0的录音文件
                            fileNameKey="";
                        }
                    }
*/
                    requestCallIdle(duration+"",number,Constants.iccID,dateLong/1000);
//                    duration>0 &&
                    if(Constants.qiniuToken != null){
                        MainActivity.initInfos(Constants.qiniuToken);
                    }
                }
            }
        }.execute();

    }



    private void requestCallIdle(String mills,String number,String ssid,Long callat) {
            try {
                if(Constants.localCallNumber != null){
                    if(Constants.localCallNumber.contains("+")){
                        Constants.localCallNumber = Constants.localCallNumber.replace("+","");
                    }
                    if(Constants.localCallNumber.contains("86")){
                        if(Constants.localCallNumber.substring(0,2).equals("86")){
                            Constants.localCallNumber =  Constants.localCallNumber.substring(2,Constants.localCallNumber.length());
                        }
                    }
                }

                if(phoneNumber != null){
                    if(phoneNumber.contains("+")){
                        phoneNumber = phoneNumber.replace("+","");//t_phone
                    }

                    if(phoneNumber.contains("86")){
                        if(phoneNumber.substring(0,2).equals("86")){
                            phoneNumber =  phoneNumber.substring(2,phoneNumber.length());
                        }
                    }
                }
                String  reqUrl = Constants.host+"work/call_add?s_phone="+ Constants.localCallNumber+"&p_number="+ssid+"&t_phone="+number+"&duration="+mills+"&qn_key="+fileNameKey+"&call_type="+isInComing+"&c_at="+callat;

                if (!NetUtils.isConnected(getApplicationContext())) {
                    if(reqUrl.contains("work/call_add")){//呼叫记录本地保存
                        String callList =  EnjoyPreference.readString(getApplicationContext(),"call_list");
                        try{
                            if(callList == null || callList.equals("null")){
                                JSONObject object = new JSONObject();
                                object.put("call_one",reqUrl);
                                JSONArray array = new JSONArray();
                                array.put(object);
                                EnjoyPreference.saveString(getApplicationContext(),"call_list",array.toString());
                            }else{
                                String localCallList =  EnjoyPreference.readString(getApplicationContext(),"call_list");
                                JSONArray array = new JSONArray(localCallList);
                                JSONObject object = new JSONObject();
                                object.put("call_one",reqUrl);
                                array.put(object);
                                EnjoyPreference.saveString(getApplicationContext(),"call_list",array.toString());
                            }
                        }catch (Exception e){

                        }

                    }
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.common_neterror), Toast.LENGTH_SHORT).show();
                    return;
                }

                OkHttpUtils.get(null, reqUrl,new OkhttpCallback()
                {
                    @Override
                    public void onBefore() {
                        super.onBefore();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(), message,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onGetResult(Object object) {
                    }

                    @Override
                    public Object parseNetworkResponse(JSONObject object) throws
                            Exception {
                            if(object.getString("err") != null && object.getString("err").equals("0")){
                                Log.d("OutGoingReceiver", "onCallStateChanged1: " +"通话记录成功");
                            }
                        return null;
                    }


                });

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
    }


    private MediaRecorder mediaRecorder;
    private String fileNameKey;
    public void startRecord(String number){


         fileNameKey = System.currentTimeMillis()+ UUID.randomUUID().toString().replace("-", "")
                + ".m4a";
        File mRecorderFile = new File(Environment.getExternalStorageDirectory(),
                Constants.stroDir + fileNameKey);
        mediaRecorder = new MediaRecorder();
//       mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);//DEFAULT  VOICE_RECOGNITION
       mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);//DEFAULT  VOICE_RECOGNITION

        //获得声音数据源（下麦克风）
//      mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);//这个设置就是获取双向声音
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 按3gp格式输出
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 按3gp格式输出
//                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //所有android系统都支持的适中采样的频率
//        mediaRecorder.setAudioChannels(1);
       /* mediaRecorder.setMaxDuration(1000*60*60*2);
        mediaRecorder.setMaxFileSize(1024*1024*500);*/

        //通用的AAC编码格式
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
        //所有android系统都支持的适中采样的频率
        mediaRecorder.setAudioSamplingRate(44100);

        //设置音质频率
        mediaRecorder.setAudioEncodingBitRate(192000);
//        mediaRecorder.setAudioEncodingBitRate(96000);
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
    }
  /*  public void stopRecord(){
        if (mediaRecorder != null) {

            Log.e("Recorder", "RecorderPath" + Environment.getExternalStorageDirectory());
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if(manager != null){
            manager.stopSelf();
        }

    }
*/
    private static Context context;
    String phoneBrand;
    @Override
    public void onCreate() {
        super.onCreate();
        /*mTelephonyManager = (TelephonyManager) this.getSystemService(
                Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mCallListener, PhoneStateListener.LISTEN_CALL_STATE);*/
        context = CallService.this;
         mTelephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mCallListener, PhoneStateListener.LISTEN_CALL_STATE);
        Log.d("OutGoingReceiver", "onCallStateChanged1: " +"开启服务监听");
        //getCallUserHistory(CallService.this);
         phoneBrand = android.os.Build.BRAND;
    }

//    private OutGoingLogThread mOutGoingLogThread;
    private String phoneNumber;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ("outgoingAction".equals(intent.getAction())) {
            String number = intent.getStringExtra("outgoingNumber");
            Log.d("OutGoingReceiver", "onCallStateChanged1: " +"进入stratCommand开始录音");
            phoneNumber = number;
            isInComing = 0;//呼出
            currentTime = System.currentTimeMillis();
            String uuid = UUID.randomUUID().toString();
           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    startRecord(number);
                }
            }).start();*/
            fileNameKey = System.currentTimeMillis()+ UUID.randomUUID().toString().replace("-", "");
            Log.d("OutGoingReceiver", "onCallStateChanged1: " +phoneBrand);
            if(phoneBrand.toUpperCase().equals(Constant.HUAWEI)){
                fileNameKey = fileNameKey+".amr";
//                fileNameKey = fileNameKey+".mp3";
            }else if(phoneBrand.toUpperCase().equals(Constant.XIAOMI)){//小米 sound_recorder/call/
                fileNameKey = fileNameKey+".mp3";

            }else{
                fileNameKey = fileNameKey+".mp3";
            }
            Log.d("OutGoingReceiver", "onCallStateChanged1: " +fileNameKey);
/*
            if(stroFile.exists() && stroFile.isDirectory()) {
                File[] childFiles = stroFile.listFiles();
                for(File childFile:childFiles){
                    childFile.delete();//开始录音前先清空
                }
            }*/
          /* File mRecorderFile = new File(Environment.getExternalStorageDirectory(),
                    Constants.stroDir + fileNameKey);*/

            /*audioRecorder.createDefaultAudio(fileNameKey);
            audioRecorder.startRecord();*/

//            MainActivity.instance.videoStart(fileNameKey);
//            mExecutorService.submit(runnable);
         /*   String fileName = new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA).format(new Date());
            audioRecorder.createDefaultAudio(fileName);
            audioRecorder.startRecord();*/
//开启读取日志，获取去电状态
           /* if (mOutGoingLogThread == null) {
                mOutGoingLogThread = new OutGoingLogThread(CallService.this, new OutGoingLogThread.OutGoingListener() {
                    @Override
                    public void outGoingOn() {
                        //去电接通了 开启录音
                        currentTime = 0;
                        Log.d("OutGoingReceiver", "onCallStateChanged1: " +"去电接通了 开启录音");
                        startRecord(number);
                    }
                });
                mOutGoingLogThread.start();
           }*/

        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void showPlay(String filePath) {

    }


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
                    if (mLastState == TelephonyManager.CALL_STATE_OFFHOOK&&mOnCallOffListener!=null) {
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
            public interface OnIncomingListener { void incomingOn(String number); }
            //电话挂断监听，不分来去电
            public interface OnCallOffListener { void off();}

}



   /* 去电日志获取线程：*/

    //系统通话信息的TAG，不同机型TAG不同，自己获取
    public final static String LOG_TAG="your phone system phone call tag";

   //日志里接通的信息，不同机型信息不同，自己研究获取
    public final static String OUTGOING_ON="call_state = 5";


    public static boolean mIsRunning;
  /*  public static class OutGoingLogThread extends Thread{

        private OutGoingListener mOutGoingListener;

        public OutGoingLogThread(Context callService, OutGoingListener outGoingListener) {
            this.mOutGoingListener = outGoingListener;
        }

        @Override
        public void run() {
            mIsRunning = true;
            Process process = null;
            BufferedReader bufferReader = null;
            try {
//此处尤为注意！！！读取日志前如果不清理日志，会获取缓存区域的日志，导致不是最新实时日志，影响本次判断
//网上很多清理日志时没调用waitFor(),经测试根本无法及时清理日志，所以必须调用

                Runtime.getRuntime().exec("su");//申请root权限
                Runtime.getRuntime().exec("logcat -c").waitFor();
//获取系统指定TAG日志
                String cmd = "logcat -b system -s " + LOG_TAG;
                process = Runtime.getRuntime().exec(cmd);
                bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                while (mIsRunning && (line = bufferReader.readLine()) != null) {
                    Log.d(TAG, "读到日志"+line);
                    if (line.contains(OUTGOING_ON)) {
                        if (mOutGoingListener != null) {
                            mOutGoingListener.outGoingOn();
                        }
                        mIsRunning = false;
                        Log.d(TAG, "去电接通");
                    }
                }
            } catch (IOException e) {
            } catch (InterruptedException e) {
            } finally {
                try {
                    if (bufferReader != null) {
                        bufferReader.close();
                    }
                    if (process != null) {
                        process.destroy();
                    }
                } catch (IOException e) {
                }

            }
        }


        public interface OutGoingListener {
            void outGoingOn();
        }
    }*/





    public void getCallHistory(Context context,String number){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    boolean callLogState = getCallLogState(context,number);
                    if (callLogState) {
                        //----------------去电接通 执行发送去电短信操作！--------------------

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    /**
     * 读取系统日志，获取通话记录时长
     * @param context
     * @param number
     * @return
     */
    private static boolean getCallLogState(Context context, String number) {
        boolean isLink = false;
        ContentResolver cr = context.getContentResolver();
        PermissionChecker.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG);
        final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI,
                new String[]{CallLog.Calls.NUMBER,CallLog.Calls.TYPE,CallLog.Calls.DURATION},
                CallLog.Calls.NUMBER +"=?",
                new String[]{number},
                CallLog.Calls.DATE + " desc");
        int i = 0;
        if(cursor != null){
            while(cursor.moveToNext()){

                if (i == 0) {//第一个记录 也就是当前这个电话的记录
                    int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);
                    long durationTime = cursor.getLong(durationIndex);
//                Log.d("test", "getCallLogState: -----------------duration= " + durationTime);
                    if(durationTime > 0){
                        Log.i("TAG","到这里了 这是if里 durationTime = "+durationTime);
                        isLink = true;

                    } else {
                        Log.i("TAG","到这里了 这是else里");
                        isLink = false;
                    }
                }
                i++;
//            int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);
//            long durationTime = cursor.getLong(durationIndex);


            }
        }

        return isLink;
    }

}
