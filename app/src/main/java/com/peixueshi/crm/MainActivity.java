package com.peixueshi.crm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.peixueshi.crm.activity.LoginActivity;
import com.peixueshi.crm.activity.ModifyPassActivity;
import com.peixueshi.crm.activity.SplashActivity;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.app.utils.TkyOkHttpUtils;
import com.peixueshi.crm.audio.interfaces.IAudioCallback;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.TkyLoginRes;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.fragment.MainCallFragment;
import com.peixueshi.crm.fragment.MainCenterFragment;
import com.peixueshi.crm.fragment.MainHomeFragment;
import com.peixueshi.crm.fragment.MainOrderFragment;
import com.peixueshi.crm.fragment.RankingFragment;
import com.peixueshi.crm.newfragment.MainChanceFragment;
import com.peixueshi.crm.utils.AESOperator;
import com.peixueshi.crm.utils.ActivityUtils;
import com.peixueshi.crm.utils.Constant;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.EnjoyPreferenceSaveCall;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.PromptManager;
import com.peixueshi.crm.utils.QiniuUploadManager;
import com.peixueshi.crm.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, IAudioCallback {

    /*1000 4w

    2000 3w*/
    @BindView(R.id.frameLayout_main)
    FrameLayout frameLayoutMain;
    @BindView(R.id.radio_home)
    RadioButton radioHome;
    @BindView(R.id.radio_ranking)
    RadioButton radio_ranking;
    @BindView(R.id.radio_order)
    RadioButton radioOrder;

    @BindView(R.id.radio_center)
    RadioButton radioCenter;
    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;
    MainChanceFragment mainChanceFragment = new MainChanceFragment();
    MainHomeFragment mainHomeFragment = new MainHomeFragment();

   RankingFragment mainHomeRanking = new RankingFragment();
    MainOrderFragment mainOrderFragment = new MainOrderFragment();
    //NewOrderFragment newOrderFragment=new NewOrderFragment();
    //  MainCommunityFragment mainCommunityFragment = new MainCommunityFragment();
//  MainMessageFragment mainMessageFragment = new MainMessageFragment();
    MainCenterFragment mainCenterFragment = new MainCenterFragment();
    MainCallFragment mainCallFragment = new MainCallFragment();
    @BindView(R.id.dial_center)
    RadioButton dialCenter;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("isExit") && intent.hasExtra("isAutoLogin")) {
            if (intent.getBooleanExtra("isExit", false) && intent.hasExtra("isAutoLogin")) {
                Intent intenExit = new Intent(MainActivity.this,
                        SplashActivity.class);//singleTask
                startActivity(intenExit);
                finish();
            }
        }

        if (intent.hasExtra("isExit")) {
            if (intent.getBooleanExtra("isExit", false)) {
                Intent intenExit = new Intent(MainActivity.this, LoginActivity.class);//singleTask
                startActivity(intenExit);
                finish();
            }
        }
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    public static Activity mainContext;
    public static MainActivity instance;

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mainContext = MainActivity.this;
        instance = this;


/*
        String password = "0123456789abcdef";   //此处使用AES-128-CBC加密模式，key需要为16位
        String encryptResult = "";

        try {
            encryptResult = AESOperator.encrypt("15699329103", password);
            Log.d("OutGoingReceiver", "加密后: " +encryptResult);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        YuXueApplication.setMainActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioGroup.check(R.id.radio_home);
        getUploadToken();
        checkVersion();

        handler.postDelayed(runnable, 10 * 1000);
        initLogin();
        if (Constants.isUpdatePass) {
            showDialogMessage(this, "您的密码是初始密码,请设置新密码", true);
        } else if (Constants.weekPass) {
            showDialogMessage(this, "每两周更换一次密码,请设置新密码", true);
        }
//       initCallChannelLogin();
    }

    public void showDialogMessage(Activity activity, String msg, boolean isOpen) {
        View view = View.inflate(activity, R.layout.update_pass_dialog, null);
        TextView textView = view.findViewById(R.id.tv_to_bind_warn);
        textView.setText(msg);
        view.findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  PromptManager.closeCustomDialog();
                if (isOpen == true) {
                    Intent intent = new Intent(activity, ModifyPassActivity.class);
                    activity.startActivity(intent);
                }
            }
        });
        view.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptManager.closeCustomDialog();
            }
        });
        view.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptManager.closeCustomDialog();
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        PromptManager.showCustomDialogFalse(activity, view, Gravity.CENTER, Gravity.CENTER);
    }


    private void initCallChannelLogin() {
        String url = Constants.hosttky + "thirdparty/api/v1/authenticate";
        try {
            HashMap<String, String> paramMap = new HashMap();
            paramMap.put("username", "1299@zybx.com");
            paramMap.put("password", "yuxue20200706");
            TkyOkHttpUtils.post(MainActivity.this, url, paramMap, new OkhttpCallback() {
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
                    TkyLoginRes res = JSONUtil.parser(object.getJSONObject("data"), TkyLoginRes.class, 0);
                    Constants.tkyUserInfo = res;
                    EnjoyPreference.saveString(getApplicationContext(), "tky_acc_token", res.getToken());
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void initLogin() {
        String url = Constants.host + "user/init_info?app_version=" + Util.getVersionName(getApplicationContext()) + "&mobil_brand=" + Build.BRAND + ":" + Build.MODEL + "&imei=" + Util.getIMEI(getApplicationContext());
        try {
            OkHttpUtils.get(MainActivity.this, url, new OkhttpCallback() {
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

                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    @Override
    protected void onDestroy() {
        PromptManager.closeUpdateDialog();
        super.onDestroy();
    }


    private void checkVersion() {
        try {
            OkHttpUtils.get(MainActivity.this, Constants.a, new OkhttpCallback() {
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
                    Map<String, String> map = JSONUtil.getMap(object.getJSONObject("info"));
                    String versionName = Util.getVersionName(getApplicationContext()).replace(".", "");
                    String serVersion = map.get("AndroidHighVersions").replace(".", "");//AndroidLowVersions -> 0 AndroidHighVersions -> 1.1
                    int serVer = 0;

                    if (serVersion.length() == 2) {
                        serVer = Integer.valueOf(serVersion) * 10;
                    }
                    if (Integer.valueOf(versionName) < serVer) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showUploadDialog(map.get("HighVersionsUrl"));
                            }
                        });

                    }


                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    private void showUploadDialog(String url) {
        View view = View.inflate(getApplicationContext(), R.layout.update_version, null);

        view.findViewById(R.id.tv_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.openBrowser(MainActivity.this, url);
            }
        });

        PromptManager.showUPdateDialog(this, view, Gravity.CENTER, Gravity.CENTER);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_home:
                ActivityUtils.addOrShowFragmentToActivity(getSupportFragmentManager(),
                        mainHomeFragment, R.id.frameLayout_main);
                break;
            case R.id.radio_ranking:
                ActivityUtils.addOrShowFragmentToActivity(getSupportFragmentManager(),
                        mainHomeRanking, R.id.frameLayout_main);
                break;
            case R.id.radio_order:
                ActivityUtils.addOrShowFragmentToActivity(getSupportFragmentManager(),
                        mainOrderFragment, R.id.frameLayout_main);
                break;
            case R.id.radio_center:
                ActivityUtils.addOrShowFragmentToActivity(getSupportFragmentManager(),
                        mainCenterFragment, R.id.frameLayout_main);
                mainCenterFragment.onResume();
                break;
            case R.id.dial_center://mainCallFragment
                ActivityUtils.addOrShowFragmentToActivity(getSupportFragmentManager(),
                        mainCallFragment, R.id.frameLayout_main);
                break;

            default:
        }
    }


    private void getUploadToken() {
        try {
            OkHttpUtils.post(MainActivity.this, Constants.host + "other/qiniutoken", new HashMap<>(), new OkhttpCallback() {
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
                public Object parseNetworkResponse(JSONObject jsonObject) throws
                        Exception {
                    uploadLocal(jsonObject);
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void uploadLocal(JSONObject job) throws Exception {
        callLocalHistoryUpload();
        String token = job.getString("qiniutoken");
        Constants.qiniuToken = token;
        if (!isTelephonyCalling()) {
            if (token != null) {
                initInfos(token);
            }
        }


    }

    public static boolean isTelephonyCalling() {
        boolean calling = false;
        TelephonyManager telephonyManager = (TelephonyManager) mainContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (TelephonyManager.CALL_STATE_OFFHOOK == telephonyManager.getCallState() || TelephonyManager.CALL_STATE_RINGING == telephonyManager.getCallState()) {
            calling = true;
        }
        return calling;
    }

    private void callLocalHistoryUpload() {
        String callList = EnjoyPreferenceSaveCall.readString(mainContext, "call_list");
        if (callList == null || callList.equals("null")) {
            return;
        }
        try {
            JSONArray array = new JSONArray(callList);
            for (int i = 0; i < array.length(); i++) {
                String url = "";
                try {
                    JSONObject object = array.getJSONObject(i);
                    url = object.getString("call_one");
                } catch (Exception e) {

                }

                OkHttpUtils.get(null, url, new OkhttpCallback() {
                    @Override
                    public void onBefore() {
                        super.onBefore();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(mainContext, message,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onGetResult(Object object) {
                    }

                    @Override
                    public Object parseNetworkResponse(JSONObject object) throws
                            Exception {
//                            fileNameKey ="";
                        if (object.getString("err") != null && object.getString("err").equals("0")) {
                            Log.d("OutGoingReceiver", "onCallStateChanged1: " + "通话记录成功");
                            EnjoyPreferenceSaveCall.saveString(mainContext, "call_list", null);
                        }
                        return null;
                    }


                });
            }
        } catch (Exception e) {

        }

    }

    private boolean isExit = false;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 1000);
        } else {
//            handler.removeCallbacks(runnable);
            finish();
            System.exit(0);
        }

    }

    @Override
    public void onBackPressed() {
        exit();
    }


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isTelephonyCalling()) {
                if (Constants.qiniuToken != null) {
                    initInfos(Constants.qiniuToken);
                }
            }

//            handler.postDelayed(this, 60*3*1000);
            Log.d("OutGoingReceiver", "onCallStateChanged1: 执行了定时任务");
        }
    };


    public static int getSoundReocrdCount() {

        String path;
        String phoneBrand = Build.BRAND;
        Log.d("OutGoingReceiver", "onCallStateChanged1: " + phoneBrand);
        if (phoneBrand.toUpperCase().equals(Constant.HUAWEI)) {
            path = "/Sounds/CallRecord/";
        } else if (phoneBrand.toUpperCase().equals(Constant.XIAOMI)) {//小米 sound_recorder/call/
            path = "/MIUI/sound_recorder/call_rec/";
        } else {
            path = "/MIUI/sound_recorder/call_rec/";
        }
        Constants.stroDir = path;
//        Queue<QiniuUploadManager.QiniuUploadFile> queue = getFiles(new File(Environment.getExternalStorageDirectory() +  path),Constants.qiniuToken);

        File file = new File(Environment.getExternalStorageDirectory() + path);
        Queue<QiniuUploadManager.QiniuUploadFile> queue = new LinkedList<QiniuUploadManager.QiniuUploadFile>();
        if (file.isFile()) {
            file.delete();
            return 0;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return 0;
            }

            for (File f : childFile) {
                String fileName = f.getName();
                if (fileName.endsWith(".mp3") || fileName.endsWith("amr") || fileName.endsWith("wav")) {
                    tempMap.put(fileName, fileName);//文件名代替key
                    QiniuUploadManager.QiniuUploadFile qiniuUploadFile = new QiniuUploadManager.QiniuUploadFile(file + "/" + fileName, fileName, "audio/mp4a-latm", Constants.qiniuToken);
                    queue.add(qiniuUploadFile);
                }
            }
        }

        if (queue == null || queue.size() == 0) {
            return 0;
        } else {
            return queue.size();
        }
    }


    public static void initInfos(String token) {
        //开启服务
//        Intent intent = new Intent(this,yuxueMediaService.class);
//        startService(intent);
        //获取最新Token

       /* if(Constants.isRunningCall){
            return;
        }*/

        String path;
        String phoneBrand = Build.BRAND;
        Log.d("OutGoingReceiver", "onCallStateChanged1: " + token);
        if (phoneBrand.toUpperCase().equals(Constant.HUAWEI)) {
            path = "/Sounds/CallRecord/";
        } else if (phoneBrand.toUpperCase().equals(Constant.XIAOMI)) {//小米 sound_recorder/call/
            path = "/MIUI/sound_recorder/call_rec/";
        } else {
            path = "/MIUI/sound_recorder/call_rec/";
        }
        Constants.stroDir = path;
        QiniuUploadManager qiniuUploadManager = QiniuUploadManager.getInstance(mainContext);
        Queue<QiniuUploadManager.QiniuUploadFile> queue = getFiles(new File(Environment.getExternalStorageDirectory() + path), token);
        if (queue == null || queue.size() == 0) {
            return;
        }

        qiniuUploadManager.queueUpload(queue, new QiniuUploadManager.OnUploadListener() {
            @Override
            public void onStartUpload() {

            }

            @Override
            public void onUploadProgress(String key, double percent) {

            }

            @Override
            public void onUploadFailed(String key, String err) {
                Log.d("OutGoingReceiver", "上传失败" + "key:" + key + "err:" + err);
            }

            @Override
            public void onUploadBlockComplete(String key) {
//                File file = new File(Environment.getExternalStorageDirectory() + "/yuxue/"+tempMap.get(key));
                File file = new File(Environment.getExternalStorageDirectory() + path + key);
                Log.d("OutGoingReceiver", "上传成功");
                if (file.exists()) {
                    Log.d("yuxueApplication", "要删除的文件" + tempMap.get(key));
                    file.delete();
                }
                if (Constants.qiniuToken != null && MainCenterFragment.tv_sound_local != null) {
                    MainCenterFragment.tv_sound_local.setText("本地录音(共" + MainActivity.getSoundReocrdCount() + "条,可点击上传)");
                }
            }

            @Override
            public void onUploadCompleted() {

            }

            @Override
            public void onUploadCancel() {

            }
        });

    }


    public static Map<String, String> tempMap = new HashMap<>();

    public static Queue<QiniuUploadManager.QiniuUploadFile> getFiles(File file, String token) {
        Queue<QiniuUploadManager.QiniuUploadFile> queue = new LinkedList<QiniuUploadManager.QiniuUploadFile>();
        if (file.isFile()) {
            file.delete();
            return null;
        }
        if (Constants.isUpdatingFile) {//正常上传中，不再上传
            return queue;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return null;
            }

            for (File f : childFile) {
                String fileName = f.getName();
//Constants.isUpdatingFile
                //非上传中，非拨打电话中，上传带“_”的
                //非上传中，拨打电话中，只上传正常没上传文件
                if (!isTelephonyCalling()) {//!Constants.isRunningCall
                    Log.d("OutGoingReceiver", "----文件名-----：" + fileName);
                    if (fileName.contains("_")) {
                        String fileNameKey = System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "");
                        String phoneBrand = Build.BRAND;
                        String number = "";
                        if (phoneBrand.toUpperCase().equals(Constant.HUAWEI)) {
                            fileNameKey = fileNameKey + ".amr";
//                        fileNameKey = fileNameKey+".mp3";
                        } else if (phoneBrand.toUpperCase().equals(Constant.XIAOMI)) {//小米 sound_recorder/call/  m4a 6a
                            String name = fileName.split("_")[1];
                            String end = name.substring(name.indexOf("."));
//                        fileNameKey = fileNameKey+".mp3";
                            fileNameKey = fileNameKey + end;
                        } else {
                            String name = fileName.split("_")[1];
                            String end = name.substring(name.indexOf("."));
                            fileNameKey = fileNameKey + end;
                        }


                        //截取目标手机号
                        String callAt = "";
                        if (phoneBrand.toUpperCase().equals(Constant.HUAWEI)) {
                            if (fileName.contains("@")) {
                                String fileNewName = fileName.split("_")[0];
                                Log.d("yuxueApplication", "子文件dd" + fileNewName);
                                number = fileNewName.substring(5, fileNewName.length());

                                String callTime = fileName.split("_")[1];
                                callTime = callTime.substring(0, callTime.length() - 4);
                                callAt = Util.dateToStamp(callTime);
                            } else {
                                String fileNewName = fileName.split("_")[0];
                                number = fileNewName.trim();
                                String callTime = fileName.split("_")[1];
                                callTime = callTime.substring(0, callTime.length() - 4);
                                callAt = Util.dateToStamp(callTime);
                            }
                        } else if (phoneBrand.toUpperCase().equals(Constant.XIAOMI)) {//小米
                            String fileNewName = fileName.split("_")[0];
                            //遍历所有文件,按时间desc排序,获取时间最大的第一个重命名
                            if (fileNewName.contains("电话会议")) {
                                f.delete();
                                continue;
                            }
                            if (fileName.contains("@")) {
                                number = fileNewName.substring(5, fileNewName.lastIndexOf("("));
                                String callTime = fileName.split("_")[1];
                                callTime = callTime.substring(0, callTime.length() - 4);
                                callAt = Util.dateToStamp(callTime);
                            } else {
                                number = fileNewName.substring(0, fileNewName.lastIndexOf("("));
                                String callTime = fileName.split("_")[1];
                                callTime = callTime.substring(0, callTime.length() - 4);
                                callAt = Util.dateToStamp(callTime);
                            }
                        }
                        Constants.callAt = callAt;
                        number = number.replaceAll(" ", "");
                        if (number.length() != 11 || !number.startsWith("1")) {
                            f.delete();
                            Log.d("OutGoingReceiver", "已删除: " + fileName);
                            continue;
                        }


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
                            break;
                        }
                        //获取到最大时间文件路径
                        File oldFile = new File(f.getAbsolutePath());
                        int duration = Util.getAudioFileVoiceTime(oldFile + "");


                        String password = "0123456789abcdef";   //此处使用AES-128-CBC加密模式，key需要为16位
                        String encryptResult = "";
                        Log.d("OutGoingReceiver", "加密前: " + number);
                        try {
                            encryptResult = AESOperator.encrypt(number, password);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String newFileNameKey = encryptResult + "*" + duration + "*" + 3 + "*" + callAt + "*" + empId + "*" + fileNameKey;
                        File newFile = new File(file.toString() + "/" + newFileNameKey);


                        Log.d("OutGoingReceiver", "旧文件名：" + oldFile.getName());
                        Log.d("OutGoingReceiver", "新文件名：" + newFile.getName());
                        boolean isRameSuccess = oldFile.renameTo(newFile);


                        Log.d("OutGoingReceiver", "callat: " + callAt);
                        String reqUrl = Constants.host + "work/call_add?s_phone=" + Constants.localCallNumber + "&p_number=" + Constants.iccID + "&t_phone=" + number + "&duration=" + duration + "&qn_key=" + newFileNameKey + "&call_type=" + 3 + "&c_at=" + callAt;


                        OkHttpUtils.get(null, reqUrl, new OkhttpCallback() {
                            @Override
                            public void onBefore() {
                                super.onBefore();
                            }

                            @Override
                            public void onFailure(String message) {
                                Toast.makeText(mainContext, message,
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onGetResult(Object object) {
                            }

                            @Override
                            public Object parseNetworkResponse(JSONObject object) throws
                                    Exception {
//                            fileNameKey ="";
                                if (object.getString("err") != null && object.getString("err").equals("0")) {
                                    Log.d("OutGoingReceiver", "onCallStateChanged1: " + "通话记录成功");
                                }
                                return null;
                            }


                        });
                    }
                }
                Log.d("OutGoingReceiver", "子文件aa" + fileName);
                //String key = UUID.randomUUID().toString().replace("-", "")+".m4a";
                if (fileName.contains("_")) {
                    continue;
                }
                if (fileName.endsWith(".mp3") || fileName.endsWith("amr") || fileName.endsWith("wav")) {
                    tempMap.put(fileName, fileName);//文件名代替key
                    QiniuUploadManager.QiniuUploadFile qiniuUploadFile = new QiniuUploadManager.QiniuUploadFile(file + "/" + fileName, fileName, "audio/mp4a-latm", token);
                    queue.add(qiniuUploadFile);
                }

            }

        }
        return queue;
    }

    @Override
    public void showPlay(String filePath) {

    }
}
