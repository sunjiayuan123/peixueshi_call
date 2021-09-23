package com.peixueshi.crm.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.peixueshi.crm.R;
import com.peixueshi.crm.activity.SetPhoneActivity;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.ui.adapter.PopLeiXingDropAdapter;
import com.peixueshi.crm.ui.adapter.PopXiangmuDropAdapter;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.PhoneInfoUtils;
import com.peixueshi.crm.utils.PromptManager;
import com.peixueshi.crm.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者:孙家圆
 * 拨号界面
 */
public class MainCallFragment extends BaseFragment {


    @BindView(R.id.tv_text2)
    EditText tvText2;
    @BindView(R.id.bt_delete)
    ImageView btDelete;
    @BindView(R.id.bt_one)
    ImageView btOne;
    @BindView(R.id.bt_two)
    ImageView btTwo;
    @BindView(R.id.bt_three)
    ImageView btThree;
    @BindView(R.id.bt_four)
    ImageView btFour;
    @BindView(R.id.bt_five)
    ImageView btFive;
    @BindView(R.id.bt_six)
    ImageView btSix;
    @BindView(R.id.bt_seven)
    ImageView btSeven;
    @BindView(R.id.bt_eight)
    ImageView btEight;
    @BindView(R.id.bt_nine)
    ImageView btNine;
    @BindView(R.id.bt_star)
    ImageView btStar;
    @BindView(R.id.bt_zero)
    ImageView btZero;
    @BindView(R.id.bt_bottom)
    ImageView btBottom;
    @BindView(R.id.bt_function)
    ImageView btFunction;
    @BindView(R.id.bt_call)
    ImageView btCall;
    @BindView(R.id.bt_all)
    ImageView btAll;
    @BindView(R.id.et_xiangmu)
    EditText et_xiangmu;
    Unbinder unbinder;
    @BindView(R.id.lin_call)
    LinearLayout linCall;
    @BindView(R.id.leixing)
    EditText leixing;
    Unbinder unbinder1;
    private String all = "";
    private int COUN_CALL = 20;//拨打量
    private static final int DTMF_DURATION_MS = 120; // 声音的播放时间
    private Object mToneGeneratorLock = new Object(); // 监视器对象锁
    private ToneGenerator mToneGenerator;             // 声音产生器
    private static boolean mDTMFToneEnabled;         // 系统参数“按键操作音”标志位
    private Activity activity;
    private PhoneAccountHandle phoneAccountHandle;
    private String localCallNumber;
    String pid = "0";
    private List<String> leixingList;

    public MainCallFragment() {
        // Required empty public constructor
    }


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ArmsUtils.inflate(getActivity(), R.layout.fragment_main_call);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        activity = getActivity();
        // getTime();
        //获取手机号码
//按键声音播放设置及初始化
        try {
            // 获取系统参数“按键操作音”是否开启
            mDTMFToneEnabled = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1;
            synchronized (mToneGeneratorLock) {
                if (mDTMFToneEnabled && mToneGenerator == null) {
                    mToneGenerator = new ToneGenerator(
                            AudioManager.STREAM_DTMF, 80); // 设置声音的大小
                    getActivity().setVolumeControlStream(AudioManager.STREAM_DTMF);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            mDTMFToneEnabled = false;
            mToneGenerator = null;
        }
        btDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                all = "";

                tvText2.setText("");
                return true;
            }
        });

    }
    int leixings=0;
    @Override
    public void setData(@Nullable Object data) {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Log.e("tag", "onHiddenChanged: ");
            linCall.setVisibility(View.VISIBLE);
            all = "";
            tvText2.setText(all);
            clickPos = -1;
            et_xiangmu.setText("");
            et_xiangmu.setHint("请选择项目");
            leixing.setText("");
            leixing.setHint("请选择号码位置");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  all = "";
        if (tvText2 != null) {
            tvText2.setText(all);
        }*/
    }

    /**
     * 播放按键声音
     */
    private void playTone(int tone) {
        if (!mDTMFToneEnabled) {
            return;
        }
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        if (ringerMode == AudioManager.RINGER_MODE_SILENT
                || ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
            // 静音或者震动时不发出声音
            return;
        }
        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                Log.w(TAG, "playTone: mToneGenerator == null, tone: " + tone);
                return;
            }
            mToneGenerator.startTone(tone, DTMF_DURATION_MS);   //发出声音
        }
    }

    @OnClick({R.id.leixing,R.id.et_xiangmu, R.id.tv_text2, R.id.bt_delete, R.id.bt_one, R.id.bt_two, R.id.bt_three, R.id.bt_four, R.id.bt_five, R.id.bt_six, R.id.bt_seven, R.id.bt_eight, R.id.bt_nine, R.id.bt_star, R.id.bt_zero, R.id.bt_bottom, R.id.bt_function, R.id.bt_call, R.id.bt_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_text2:
                linCall.setVisibility(View.VISIBLE);
                break;

            case R.id.bt_delete:
                deleteNum();
                break;
            case R.id.bt_one:
                all += "1";
                Log.e("tag", "onViewClicked: " + all + "====" + 1);
                //tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_1);
                break;
            case R.id.bt_two:
                all += "2";
                //tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_2);
                break;
            case R.id.bt_three:

                all += "3";
                // //tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_3);
                break;
            case R.id.bt_four:
                all += "4";
                //  //tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_4);
                break;
            case R.id.bt_five:
                all += "5";
                //  //tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_5);
                break;
            case R.id.bt_six:
                all += "6";
                // //tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_6);
                break;
            case R.id.bt_seven:
                all += "7";
                //tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_7);
                break;
            case R.id.bt_eight:
                all += "8";
                //tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_8);
                break;
            case R.id.bt_nine:
                all += "9";
                //tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_9);
                break;
            case R.id.bt_star:
                all += "*";
                //tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_S);
                break;
            case R.id.bt_zero:
                all += "0";
                //tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_0);
                break;
            case R.id.bt_bottom:
                all += "#";
                //tvText1.setText(all);
                tvText2.setText(all);
                playTone(ToneGenerator.TONE_DTMF_P);
                break;
            case R.id.bt_function:
                //     linCall.setVisibility(View.GONE);
                break;
            case R.id.et_xiangmu:
                if (listXiangmu != null && listXiangmu.size() > 0) {
                    showDropView();
                } else {
                    initXiangmu();
                }
                break;
            case R.id.leixing:
                showLeiXing();
                break;
            case R.id.bt_call:
                String phone = tvText2.getText().toString();
                Log.e("tag", "onViewClicked: " + phone);
                if (phone.length() == 0) {
                    Toast.makeText(activity, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.length() != 11) {
                    Log.e("tag", "onViewClicked: " + phone.length());
                    Toast.makeText(activity, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!phone.matches("^(1)\\d{10}$")) {
                    Toast.makeText(activity, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (clickPos == -1) {
                    Toast.makeText(activity, "请选择项目", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (listXiangmu != null && listXiangmu.get(clickPos) != null) {

                    if (listXiangmu.get(clickPos).containsKey("p_id")) {
                        pid = listXiangmu.get(clickPos).get("p_id");
                    } else {
                        pid = listXiangmu.get(clickPos).get("pr_id");
                    }
                    if (!TextUtils.isEmpty(pid) && pid != null) {
                        if (!pid.equals("0")) {
                            int pids = Integer.valueOf(pid);
                            Log.e("tag", "onViewClicked: " + pids + "===" + all);
                            getRealTime(phone, pids);
                        }
                    }
                }
                break;
            case R.id.bt_all:
                deleteNum();
                break;
        }
    }

    private void initXiangmu() {
        try {
            String url = null;
            url = Constants.b;
            HashMap<String, String> keyMap = new HashMap<>();
               /* if(Constants.isWifiLimit){
                    keyMap.put("page","1");
                    keyMap.put("count","100");
                    keyMap.put("id","1");
                }else{*/
            keyMap.put("page", "1");
            keyMap.put("count", "100");
            keyMap.put("id", "0");
//                }

            OkHttpUtils.post(activity, url, keyMap, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(activity, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject jsonObject) throws
                        Exception {
                    getUserInfo(jsonObject);
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
    PopLeiXingDropAdapter popLeiXingDropAdapter;
    PopXiangmuDropAdapter popXiangmuDropAdapter;
    List<Map<String, String>> listXiangmu;

    private void getUserInfo(JSONObject job) throws Exception {
        JSONArray jsonArray = null;
        if (job.has("list")) {
            jsonArray = job.getJSONArray("list");
        } else {
            jsonArray = job.getJSONObject("date").getJSONArray("list");
        }
        if (jsonArray != null) {
            listXiangmu = JSONUtil.getListMap(jsonArray);
            if (listXiangmu.size() == 0) {
                PromptManager.showMyToast("暂无项目", activity);
                return;
            }
                   /* if(!Constants.isWifiLimit){
                        if (listXiangmu != null && listXiangmu.size() > 0) {
                            if (listXiangmu.get(0).get("pr_id").equals("1000000") || listXiangmu.get(0).get("pr_id").equals("1001000")) {
                                listXiangmu.remove(0);
                            }
                        }

                        if (listXiangmu != null && listXiangmu.size() > 0) {
                            if (listXiangmu.get(0).get("pr_id").equals("1000000") || listXiangmu.get(0).get("pr_id").equals("1001000")) {
                                listXiangmu.remove(0);
                            }
                        }
                    }*/
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showDropView();
                }
            });
                    /*String p_id = null;
                    if(listXiangmu != null && listXiangmu.size() >0){
                        p_id = listXiangmu.get(0).get("pr_id");
                    }*/
            //  getPIdInfo(listXiangmu,p_id);
        }
    }

    private void showLeiXing() {
        leixingList = new ArrayList<>();
        leixingList.add("未知");
        leixingList.add("首咨");
        leixingList.add("库存");
        leixingList.add("未升班");
        leixingList.add("已升班");
        View view = View.inflate(activity, R.layout.ll_xiangmu_drop, null);
        RecyclerView recyclerView = view.findViewById(R.id.rv_drop);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager1);
        if (popLeiXingDropAdapter == null) {//leixingList
            popLeiXingDropAdapter = new PopLeiXingDropAdapter(activity, leixingList);
            popLeiXingDropAdapter.setData(leixingList);
            recyclerView.setAdapter(popLeiXingDropAdapter);
            popLeiXingDropAdapter.notifyDataSetChanged();
        } else {
            popLeiXingDropAdapter.setData(leixingList);
            recyclerView.setAdapter(popLeiXingDropAdapter);
            popLeiXingDropAdapter.notifyDataSetChanged();
        }
        popLeiXingDropAdapter.setOnItemClickListener(new PopLeiXingDropAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int pos) {
                String s = leixingList.get(pos);
                leixings = pos;
                leixing.setText(s);
                PromptManager.closePopWindow();
            }
        });
        PromptManager.showPopViewbb(view, leixing, activity);
    }

    private int clickPos = -1;

    private void showDropView() {
        View view = View.inflate(activity, R.layout.ll_xiangmu_drop, null);
        RecyclerView recyclerView = view.findViewById(R.id.rv_drop);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager1);
        if (popXiangmuDropAdapter == null) {
            popXiangmuDropAdapter = new PopXiangmuDropAdapter(activity, listXiangmu);
            recyclerView.setAdapter(popXiangmuDropAdapter);
        } else {
            popXiangmuDropAdapter.setData(listXiangmu);
            recyclerView.setAdapter(popXiangmuDropAdapter);
            popXiangmuDropAdapter.notifyDataSetChanged();
        }
        popXiangmuDropAdapter.setOnItemClickListener(new PopXiangmuDropAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int pos) {
                String name;
                if (listXiangmu.get(pos).containsKey("p_name")) {
                    name = listXiangmu.get(pos).get("p_name");
                } else {
                    name = listXiangmu.get(pos).get("pr_name");
                }

                clickPos = pos;
                et_xiangmu.setText(name);
                PromptManager.closePopWindow();
            }
        });
        PromptManager.showPopViewbb(view, et_xiangmu, activity);
    }


    /**
     * 获取时间
     */
    private void getRealTime(String phone, int wp_id) {

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                //  .addHeader("Authorization", Constants.jwtToken)
                .url("http://quan.suning.com/getSysTime.do")
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("tag", "onFailure: " + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        try {
                            JSONObject object = new JSONObject(result);
                            //  int reult = object.getInt("code");
                            String datatimes = object.getString("sysTime2");
                            String[] s = datatimes.split(" ");
                            String datatime = s[0];
                            Log.e("tag", "parseNetworkResponse: " + object.toString());
                            String currentDay = EnjoyPreference.readString(activity, "currentDay");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!TextUtils.isEmpty(currentDay)) {
                                        if (!datatime.equals(currentDay)) {
                                            getRealToken(phone, wp_id, datatime);
                                            //return;
                                        } else {
                                            APICallRequest(phone, wp_id, activity, datatime);//电销
                                        }
                                    } else {
                                        getRealToken(phone, wp_id, datatime);
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            String datatime = Util.getData();

                            String currentDay = EnjoyPreference.readString(activity, "currentDay");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("tag", "getCurrentNetworkTime: " + e.getMessage());
                                    if (!TextUtils.isEmpty(currentDay)) {
                                        if (!datatime.equals(currentDay)) {
                                            getRealToken(phone, wp_id, datatime);
                                            //return;
                                        } else {
                                            APICallRequest(phone, wp_id, activity, datatime);//电销
                                        }
                                    } else {
                                        getRealToken(phone, wp_id, datatime);
                                    }
                                }
                            });

                        }

                        Log.e("tag", "APICallRequestgetRealTime: " + result);
                    }
                });
            }
        }).start();
    }

    /**
     * 获取token
     */
    private void getRealToken(String phone, int wp_id, String datatime) {
        try {

            String emp_name = EnjoyPreference.readString(activity, "emp_name");//名
            String emp_team_id = EnjoyPreference.readString(activity, "emp_team_id");//组id
            String emp_id = EnjoyPreference.readString(activity, "emp_id");//坐席id
            String url = null;
            url = Constants.host + "team/work_d?uid=" + emp_id + "&uname=" + emp_name + "&gid=" + emp_team_id;//+ emp_team_id
            Log.e("tag", "getRealToken: " + url);
            OkHttpUtils.get(activity, url, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Log.e("tag", "onFailure:4 " + message);
                    Toast.makeText(activity, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject object) throws
                        Exception {
                    int err = object.getInt("err");
                    String data = object.getString("data");
                    Log.e("tag", "parseNetworkResponse: " + object.toString());
                    if (err == 0) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EnjoyPreference.saveString(activity, "currentDay", datatime);
                                EnjoyPreference.saveString(activity, "jwtoken", data);
                                Constants.jwtToken = data;
                                if (!TextUtils.isEmpty(Constants.jwtToken)) {
                                    APICallRequest(phone, wp_id, activity, datatime);//电销
                                }

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

    public void APICallRequest(String callee, int p_id, Activity activity, String datatime) {//电销
        int simNumber = 0;
        SubscriptionManager sm = SubscriptionManager.from(activity);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "no call phone permission", Toast.LENGTH_SHORT).show();
            return;
        }
        List<SubscriptionInfo> subs = sm.getActiveSubscriptionInfoList();
        if (subs == null) {
            Log.d(TAG, "checkDualSim: " + "no sim");
            PromptManager.showMyToast("no sim", activity);
            return;
        }
        if (subs.size() > 1) {
            simNumber = 2;
            Log.d(TAG, "checkDualSim: " + "two sims");
        } else {
            simNumber = 1;
        }
        String emp_id = EnjoyPreference.readString(activity, "emp_id");//坐席id
        String emp_name = EnjoyPreference.readString(activity, "emp_name");//名
        String emp_team_id = EnjoyPreference.readString(activity, "emp_team_id");//组id
        String nativePhoneNumber = new PhoneInfoUtils(activity).getNativePhoneNumber();
        String phone = EnjoyPreference.readString(activity, "calls_phone");
        String calls_zhu_phone = EnjoyPreference.readString(activity, "calls_zhu_phone");
        String jwtoken = EnjoyPreference.readString(activity, "jwtoken");
        Constants.jwtToken = jwtoken;

        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(calls_zhu_phone)) {
            Log.e("tag", "APICallRequestfffv: " + phone + "==" + calls_zhu_phone);
            Toast.makeText(activity, "手机号为空请先保存", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, SetPhoneActivity.class);
            activity.startActivity(intent);
            return;
        }
        if (simNumber == 1 && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(calls_zhu_phone)) {
            Log.e("tag", "APICallRequestvvv: " + phone + "==" + calls_zhu_phone);
            EnjoyPreference.saveString(activity, "calls_phone", "");
            EnjoyPreference.saveString(activity, "calls_zhu_phone", "");
            Toast.makeText(activity, "手机号为空请先保存", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, SetPhoneActivity.class);
            activity.startActivity(intent);
            return;
        } else if (simNumber == 2 && (TextUtils.isEmpty(phone) || TextUtils.isEmpty(calls_zhu_phone))) {
            Log.e("tag", "APICallRequestbbbv: " + phone + "==" + calls_zhu_phone);
            EnjoyPreference.saveString(activity, "calls_phone", "");
            EnjoyPreference.saveString(activity, "calls_zhu_phone", "");
            Toast.makeText(activity, "手机号为空请先保存", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, SetPhoneActivity.class);
            activity.startActivity(intent);
            return;
        }

        if ((!TextUtils.isEmpty(phone) || !TextUtils.isEmpty(calls_zhu_phone)) && simNumber == 1) {
            if (!TextUtils.isEmpty(phone)) {
                Constants.Phone = phone;
            }
            if (!TextUtils.isEmpty(calls_zhu_phone)) {
                Constants.Phone = calls_zhu_phone;
            }
        }

        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(calls_zhu_phone) && simNumber == 2) {
            if (Constants.callCard == 0) {
                Constants.Phone = calls_zhu_phone;
            } else {
                Constants.Phone = phone;
            }
        }
        if (Constants.Phone != null) {
            if (Constants.Phone.contains("+")) {
                Constants.Phone = Constants.Phone.replace("+", "");
            }
            if (Constants.Phone.contains("86")) {
                if (Constants.Phone.substring(0, 2).equals("86")) {
                    Constants.Phone = Constants.Phone.substring(2, Constants.Phone.length());
                }
            }
        }
        Log.e("tag", "APICallRequest: " + Constants.callCard + "===" + Constants.Phone);
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        //用户id，用户名
        //u_id=1&u_name=啥呢&c_id=1001&p_id=1003001&g_id=100&a_num=17731926724&b_num=17320833955
        int c_id = Constants.GID;
        // int p_id = 1003004;                                                                                                               //17320833955
        //String endUrl = "u_id=" + emp_id + "&u_name=" + emp_name + "&c_id=" + c_id + "&p_id=" + p_id + "&g_id=" + emp_team_id + "&a_num=" + Constants.Phone + "&b_num=" + callee;
        String endUrl = "u_id=" + emp_id + "&c_id=" + Constants.GID;
        Log.e("tag", "APICallRequest: " + endUrl);
        String calls_type = EnjoyPreference.readString(activity, "calls_type");
        Log.e("tag", "APICallRequest: " + calls_type + "====" + endUrl);
        if (calls_type.equals("2")) {
            getXNum(emp_id, emp_name, p_id, emp_team_id, callee, endUrl, datatime);
        } else {
            // {"code":3,"msg":"错误码: 1300, 请查寻中普ACB对接文档","data":""}
            getXNums(emp_id, emp_name, p_id, emp_team_id, callee, endUrl, datatime);
        }

    }

    public void getXNums(String emp_id, String emp_name, int p_id, String emp_team_id, String callee, String endUrl, String datatime) {
        String xnum = EnjoyPreference.readString(activity, "xnum");
        String currentDay = EnjoyPreference.readString(activity, "currentDay");
        if (!TextUtils.isEmpty(xnum) && !TextUtils.isEmpty(currentDay)) {
            if (datatime.equals(currentDay)) {
                getXphone(emp_id, datatime, Constants.GID, p_id, emp_team_id, Constants.Phone, callee, xnum);
                return;
            }
        }
        String jwtoken = EnjoyPreference.readString(activity, "jwtoken");
        Constants.jwtToken = jwtoken;
        // getXphone(emp_id,"");
        OkHttpClient okHttpClient = new OkHttpClient();
        // {"code":1,"msg":"绑定关系失败","data":"错误码: 1005, 请查寻中普ACB对接文档"}
        Request request = new Request.Builder()
                .get()
                .addHeader("Authorization", Constants.jwtToken)//Constants.jwtToken
                .url(Constants.chuXin + "/axb/x_pool?" + endUrl)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("tag", "onFailure: " + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        try {
                            JSONObject object = new JSONObject(result);
                            int reult = object.getInt("code");
                            String msg = object.getString("msg");
                            Log.e("tag", "onResponse: " + result);
                            if (reult == 0) {
                                String data = object.getString("data");

                                //  checkDualSim(data);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        EnjoyPreference.saveString(activity, "currentDay", datatime);
                                        EnjoyPreference.saveString(activity, "xnum", data);
                                        getXphone(emp_id, datatime, Constants.GID, p_id, emp_team_id, Constants.Phone, callee, data);
                                    }
                                });
                            } else if (reult == -1) {
                                getRealToken(callee, p_id, datatime);
                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (msg.contains("请查寻")) {
                                            String msgs = msg.replace("请查寻中普ACB对接文档", "");
                                            Toast.makeText(activity, msgs, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "解析异常", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        Log.e("tag", "APICallRequestaxb/x_pool: " + result);
                    }
                });
            }
        }).start();
    }

    public void getXNum(String emp_id, String emp_name, int p_id, String emp_team_id, String callee, String endUrl, String datatime) {
        String xnum = EnjoyPreference.readString(activity, "xnuma");
        String currentDay = EnjoyPreference.readString(activity, "currentDay");
        if (!TextUtils.isEmpty(xnum) && !TextUtils.isEmpty(currentDay)) {
            if (datatime.equals(currentDay)) {
                getXphone(emp_id, datatime, Constants.GID, p_id, emp_team_id, Constants.Phone, callee, xnum);
                return;
            }
        }
        String jwtoken = EnjoyPreference.readString(activity, "jwtoken");
        Constants.jwtToken = jwtoken;
        // getXphone(emp_id,"");
        OkHttpClient okHttpClient = new OkHttpClient();
        // {"code":1,"msg":"绑定关系失败","data":"错误码: 1005, 请查寻中普ACB对接文档"}
        Request request = new Request.Builder()
                .get()
                .addHeader("Authorization", Constants.jwtToken)
                .url(Constants.chuXin + "/axb/xnum?" + endUrl)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("tag", "onFailure: " + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        try {
                            JSONObject object = new JSONObject(result);
                            int reult = object.getInt("code");
                            String msg = object.getString("msg");
                            Log.e("tag", "onResponse: " + result);
                            if (reult == 0) {
                                String data = object.getString("data");

                                //  checkDualSim(data);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        EnjoyPreference.saveString(activity, "currentDay", datatime);
                                        EnjoyPreference.saveString(activity, "xnuma", data);
                                        getXphone(emp_id, datatime, Constants.GID, p_id, emp_team_id, Constants.Phone, callee, data);
                                    }
                                });
                            } else if (reult == -1) {
                                getRealToken(callee, p_id, datatime);
                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (msg.contains("请查寻")) {
                                            String msgs = msg.replace("请查寻中普ACB对接文档", "");
                                            Toast.makeText(activity, msgs, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "解析异常", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        Log.e("tag", "APICallRequest1: " + result);
                    }
                });
            }
        }).start();
    }

    public void getXphone(String u_id, String datatime, int c_id, int p_id, String emp_team_id, String a_num, String callee, String xnum) {
        //+"&xnum="+phone
        //  String endUrl = "u_id=" + u_id + "&u_name=" + emp_name + "&c_id=" + c_id + "&p_id=" + p_id + "&g_id=" + emp_team_id + "&a_num=" + a_num + "&b_num=" + callee + "&x_num=" + xnum;
        String endUrl = "c_id=" + c_id + "&p_id=" + p_id + "&a_num=" + a_num + "&b_num=" + callee + "&x_num=" + xnum+"&one="+leixings;

        Log.e("tag", "APICallRequest: " + endUrl);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .addHeader("Authorization", Constants.jwtToken)
                .url(Constants.chuXin + "/axb/zp?" + endUrl)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("tag", "onFailure: " + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        try {
                            JSONObject object = new JSONObject(result);
                            int reult = object.getInt("code");
                            String msg = object.getString("msg");
                            Log.e("tag", "onResponse: " + result);
                            if (reult == 0) {
                                //  String data = object.getString("data");
                                checkDualSim(xnum);
                            } else if (reult == -1) {
                                getRealToken(callee, p_id, datatime);
                            } else {
                                Log.e("tag", "onResponse: ");
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (msg.contains("请查寻")) {
                                            String msgs = msg.replace("请查寻中普ACB对接文档", "");
                                            Toast.makeText(activity, msgs, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "解析异常", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        Log.e("tag", "APICallRequestdddbb2: " + result);
                    }
                });
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private int checkDualSim(String phoneNumber) {
      /*  if(Constants.isUpdatingFile){
//                文件上传中，请稍后
            if(MainActivity.mainContext != null){
                Toast.makeText(MainActivity.mainContext, "文件上传中，请稍后接听", Toast.LENGTH_SHORT).show();
            }
            return 0;
        }*/

        //phoneNumber = "18613869712";
        int simNumber = 0;
        SubscriptionManager sm = SubscriptionManager.from(activity);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "no call phone permission", Toast.LENGTH_SHORT).show();
            return 0;
        }
        List<SubscriptionInfo> subs = sm.getActiveSubscriptionInfoList();
        if (subs == null) {
            Log.d(TAG, "checkDualSim: " + "no sim");
            PromptManager.showMyToast("no sim", activity);
            return simNumber;
        }
        if (subs.size() > 1) {
            simNumber = 2;
            callPhone(true, phoneNumber);
            Log.d(TAG, "checkDualSim: " + "two sims");
        } else {
            Log.d(TAG, "checkDualSim: " + "one sim");
            callPhone(false, phoneNumber);
            simNumber = 1;
        }
        for (SubscriptionInfo s : subs) {
            Log.d(TAG, "checkDualSim: " + "simInfo:" + subs.toString());
        }
        return simNumber;
    }

    //根据上述情况，初始化UI和拨打电话，尤其注意sim2的打电话情况
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void callPhone(boolean isDualSim, String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "no call phone permission", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "callPhone: " + "no call phone permission");
            return;
        }

        checkIsCall(phoneNumber, isDualSim);//卡呼叫
        //Tky网络呼叫
//        callTky();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkIsCall(String callNumber, boolean isDualSim) {
//        String callNumber = "18613869712";
        //判断是否超限
        if (Constants.localCallNumber != null) {
            if (Constants.localCallNumber.contains("+")) {
                Constants.localCallNumber = Constants.localCallNumber.replace("+", "");
            }
            if (Constants.localCallNumber.contains("86")) {
                if (Constants.localCallNumber.substring(0, 2).equals("86")) {
                    Constants.localCallNumber = Constants.localCallNumber.substring(2, Constants.localCallNumber.length());
                }
            }
        }

        if (!isDualSim) {
            String number = new PhoneInfoUtils(activity).getNativePhoneNumber();//
            Log.i("UserDataZiListAdapter", number + "msg" + "iccid" + new PhoneInfoUtils(activity).getIccid());
            Constants.iccID = new PhoneInfoUtils(activity).getIccid();
            if (!TextUtils.isEmpty(number)) {
                if (number.contains("+")) {
                    Constants.localCallNumber = number.replace("+", "");
                    number = Constants.localCallNumber;
                }
                if (number.contains("86")) {
                    if (number.substring(0, 2).equals("86")) {
                        Constants.localCallNumber = number.substring(2, Constants.localCallNumber.length());
                        number = Constants.localCallNumber;
                    }
                }
            }

//                        getLeastCallHistory(callNumber,isDualSim);

            String count = EnjoyPreference.readString(activity, Constants.iccID);

            int countNum = Integer.valueOf(count) + 1;
            EnjoyPreference.saveString(activity, Constants.iccID, countNum + "");
            Constants.localCallNumber = number;

            Log.i("UserDataAdapter", "position" + 2);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + callNumber));
            // Constants.isShouZi = isShouZi;
            activity.startActivity(intent);


        } else {
            TelecomManager telecomManager = (TelecomManager) activity.getSystemService(Context.TELECOM_SERVICE);
            if (telecomManager != null) {

                //@param slotId      0:卡1  1:卡2
                int numb = new Random().nextInt(2);
                phoneAccountHandle = getPhoneAccountHandle(Constants.callCard);//slotId
                if (Constants.callCard == 0) {
                    Constants.callCard = 1;
                } else {
                    Constants.callCard = 0;
                }
                Log.i("UserDataZiListAdapter", Constants.localCallNumber + "msg");
                if (!TextUtils.isEmpty(Constants.localCallNumber)) {
                    if (Constants.localCallNumber.contains("+")) {
                        Constants.localCallNumber = Constants.localCallNumber.replace("+", "");
                    }
                    if (Constants.localCallNumber.contains("86")) {
                        if (Constants.localCallNumber.substring(0, 2).equals("86")) {
                            Constants.localCallNumber = Constants.localCallNumber.substring(2, Constants.localCallNumber.length());
                        }
                    }
                }
                if (TextUtils.isEmpty(EnjoyPreference.readString(activity, Constants.iccID)) || EnjoyPreference.readString(activity, Constants.iccID).equals("0")) {
//                                TextUtils.isEmpty(EnjoyPreference.readString(activity,Constants.localCallNumber)) || (Constants.localCallNumber != null && Constants.localCallNumber.equals("0"))
                    //获取最新一个小时的通话记录超限提醒
                    //    getLeastCallHistory(callNumber, isDualSim);
                } else {
                    String count = EnjoyPreference.readString(activity, Constants.iccID);
                    int countNum = Integer.valueOf(count) + 1;
                    EnjoyPreference.saveString(activity, Constants.iccID, countNum + "");

                    Log.i("UserDataAdapter", "position" + 3);
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callNumber));
                    intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // Constants.isShouZi = isShouZi;
                    activity.startActivity(intent);

                }

            }
        }
               /* }else{
                    //本地超限则每次呼叫请求，并保存
                    getLeastCallHistory(callNumber,isDualSim);
                }*/
//            }
//        }
    }

    /**
     * 这一块首先获取手机中所有sim卡 PhoneAccountHandle 每一个 PhoneAccountHandle 表示一个sim卡, 然后根据 slotId 判断所指定的sim卡并返回此 PhoneAccountHandle (这里5.1 和 6.0需要区分对待)
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private PhoneAccountHandle getPhoneAccountHandle(int slotId) {
        TelecomManager tm = (TelecomManager) activity.getSystemService(Context.TELECOM_SERVICE);
        //PhoneAccountHandle api>5.1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (tm != null) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(activity, "请允许拨号权限后再试", Toast.LENGTH_SHORT).show();
                }
                List<PhoneAccountHandle> handles = tm.getCallCapablePhoneAccounts();

//            List<PhoneAccountHandle> handles = (List<PhoneAccountHandle>) ReflectUtil.invokeMethod(tm, "getCallCapablePhoneAccounts");
                SubscriptionManager sm = SubscriptionManager.from(activity);
                if (handles != null) {
                    for (PhoneAccountHandle handle : handles) {
                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Toast.makeText(activity, "请允许拨号权限后再试", Toast.LENGTH_SHORT).show();
                        }
                        SubscriptionInfo info = sm.getActiveSubscriptionInfoForSimSlotIndex(slotId);
                        if (info != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (TextUtils.equals(info.getIccId(), handle.getId())) {
                                    localCallNumber = info.getNumber();
                                   /* if(localCallNumber == null){
                                        localCallNumber = info.getIccId();
                                    }*/
                                    Constants.localCallNumber = localCallNumber;
                                    Constants.iccID = info.getIccId();
                                    Log.e(TAG, "getPhoneAccountHandle for slot" + slotId + " " + handle + Constants.localCallNumber);
                                    return handle;
                                }
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                if (TextUtils.equals(info.getSubscriptionId() + "", handle.getId())) {
                                    localCallNumber = info.getNumber();
//                                    getSimSerialNumber
                                  /*  if(localCallNumber == null){
                                        localCallNumber = info.getIccId();
                                    }*/
                                    Constants.localCallNumber = localCallNumber;
                                    Constants.iccID = info.getIccId();
//                                    Constants.localCallNumber = tm.getLine1Number(handle);//获取当前设置的电话号码
                                    Log.e(TAG, "getPhoneAccountHandle for slot" + slotId + " " + handle + Constants.localCallNumber);
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

    public void deleteNum() {
        // String s1 = tvText2.getText().toString();
        all = "";
        tvText2.setText(all);
    }

}
