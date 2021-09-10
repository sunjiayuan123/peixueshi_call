package com.peixueshi.crm.ui.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peixueshi.crm.R;
import com.peixueshi.crm.activity.DetailBeizhuActivity;
import com.peixueshi.crm.activity.DetailXiangqingActivity;
import com.peixueshi.crm.activity.MineZhongHistoryActivity;
import com.peixueshi.crm.activity.SetPhoneActivity;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.ZiXunUserInfo;
import com.peixueshi.crm.fragment.OneFragmentMasterShouzi;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.PhoneInfoUtils;
import com.peixueshi.crm.utils.PromptManager;
import com.peixueshi.crm.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserDataZiHandShouziListAdapter extends BaseAdapter {

    private boolean isShouZi = true;
    private List<ZiXunUserInfo> mUserList;

    private Activity activity;
    private OneFragmentMasterShouzi oneFragment;

    private int time = -1;
    private int TIME_COUNT_DEFAULT = 3;//间隔
    private int COUN_CALL = 20;//拨打量
    ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            if (time <= 0) {
                mHandler2.sendEmptyMessage(1);
            } else {
                mHandler2.sendEmptyMessage(2);
                mHandler2.postDelayed(this, 1000);
            }
        }
    };
    private Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    tv_next_get_time.setText("00:00");
                    break;
                case 2:
                    //   PromptManager.showMyToast("请"+time+"秒后再次拨打",activity);
                    break;
                default:
            }

        }
    };
    public static String localCallNumber;
    private int wp_id;


    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.zixun_item_info, parent, false);
            holder = new ViewHolder(convertView);
            // holder.textView = (TextView)convertView.findViewById(R.id.tv_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ZiXunUserInfo ziXunUserInfo = mUserList.get(position);
        if (isShouZi) {
            String phone = ziXunUserInfo.getWf_phone();
            if (phone.length()==11){
                Log.e("tag", "getView: " + phone);
                String number = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
                holder.tv_user_mumber.setText(number);
                holder.tv_xiangmu.setText(ziXunUserInfo.getWf_p_name());
                holder.iv_copy_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData mClipData = ClipData.newPlainText("Label", phone);
                        cm.setPrimaryClip(mClipData);
                        PromptManager.showMyToast(phone + "已复制", activity);
                    }
                });
            }

        } else {
            String phone = ziXunUserInfo.getWp_phone();
            if (phone.length()==11){
                Log.e("tag", "getViewsss: " + phone);
                String number = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
                holder.tv_user_mumber.setText(number);
                holder.tv_xiangmu.setText(ziXunUserInfo.getWp_pname());
                holder.iv_copy_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData mClipData = ClipData.newPlainText("Label", phone);
                        cm.setPrimaryClip(mClipData);
                        PromptManager.showMyToast(phone + "已复制", activity);
                    }
                });
            }

        }


        holder.re_tv_user_number.setOnClickListener(new View.OnClickListener() {
            private String wf_id;

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                ZiXunUserInfo info = mUserList.get(position);
                // Toast.makeText(view.getContext(), "you call " + info.getWf_phone(), Toast.LENGTH_SHORT).show();
                String number;
                if (!isFastClick()){
                    if (isShouZi) {
                        wf_id = info.getWf_id();

                        wp_id = info.getWp_pid();

                        Log.e("tag", "onClick: " + wp_id);
                        if (wp_id == 0) {
                            wp_id = info.getWf_p_id();

                        }
                        getRealPhone("goals/fild_first", wf_id, 1);
                    } else {
                        wf_id = info.getWp_id();
                        wp_id = info.getWp_pid();

                        if (wp_id == 0) {
                            wp_id = info.getWf_p_id();

                        }
                        getRealPhone("goals/fild_pool", wf_id, 1);
                    }
                }

            }
        });
        //记录
        holder.re_iv_user_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZiXunUserInfo userInfo = mUserList.get(position);
                String number;
                String wf_id;
                if (isShouZi) {
                    wf_id = userInfo.getWf_id();
                    getRealPhone("goals/fild_first", wf_id, 3);
                } else {
                    wf_id = userInfo.getWp_id();
                    getRealPhone("goals/fild_pool", wf_id, 3);
                }

            }
        });
        holder.iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZiXunUserInfo userInfo = mUserList.get(position);
                String number;
                String wf_id;
                if (isShouZi) {
                    wf_id = userInfo.getWf_id();
                    getRealPhone("goals/fild_first", wf_id, 3);
                } else {
                    wf_id = userInfo.getWp_id();
                    getRealPhone("goals/fild_pool", wf_id, 3);
                }
            }
        });
        holder.bt_beizhu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                ZiXunUserInfo info = mUserList.get(position);
                /*if (isShouZi) {
                    beizhu("other/firs_record?",info.getWf_id(),info.getWf_phone(),true);
                } else {
                    beizhu("work/pool_record?",info.getWp_id(),info.getWp_phone(),false);
                }*/
                String wf_id;
                if (isShouZi) {
                    wf_id = info.getWf_id();
                    getRealPhone("goals/fild_first", wf_id, 2);
                } else {
                    wf_id = info.getWp_id();
                    getRealPhone("goals/fild_pool", wf_id, 2);
                }
            }
        });

        holder.bt_huiku.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                ZiXunUserInfo info = mUserList.get(position);
                if (isShouZi) {
                    huiku("user/firs_return?", info.getWf_id(), true, position);
                } else {
                    huiku("work/pool_return?", info.getWp_id(), false, position);
                }
                if (oneFragment.recycle_view != null && oneFragment.recycle_view.getLastVisiblePosition() >= mUserList.size() - 1) {
                    oneFragment.adapter.notifyDataSetChanged();
                    oneFragment.pullDownView.endUpdate(new Date());
                    oneFragment.recycle_view.onLoadMoreComplete(false);
                }
            }
        });

        holder.bt_xiangqing.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                ZiXunUserInfo info = mUserList.get(position);
                info.setShouZi(isShouZi);
                Intent intent = new Intent(activity, DetailXiangqingActivity.class);
                intent.putExtra("info", info);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }

    /**
     * 备注
     *
     * @param url
     * @param w_id
     */
    private void beizhu(String url, String w_id, String w_phone, boolean isShouZi) {
        Intent intent = new Intent(activity, DetailBeizhuActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("w_id", w_id);
        intent.putExtra("w_phone", w_phone);
        intent.putExtra("isShouZi", isShouZi);
        activity.startActivity(intent);
    }


    public void setData(List<ZiXunUserInfo> userInfos) {
        this.mUserList = userInfos;
    }

    static class ViewHolder {
        ImageView iv_call;
        TextView tv_user_mumber;
        TextView tv_xiangmu;

        Button bt_beizhu;
        Button bt_xiangqing;
        Button bt_huiku;
        ImageView iv_copy_phone;
        RelativeLayout re_iv_user_call;
        RelativeLayout re_tv_user_number;

        public ViewHolder(View view) {
            iv_call = view.findViewById(R.id.iv_user_call);
            tv_user_mumber = view.findViewById(R.id.tv_user_number);
            tv_xiangmu = view.findViewById(R.id.tv_user_xinagmu);
            re_iv_user_call = view.findViewById(R.id.re_iv_user_call);
            re_tv_user_number = view.findViewById(R.id.re_tv_user_number);
            bt_beizhu = view.findViewById(R.id.bt_beizhu);
            bt_xiangqing = view.findViewById(R.id.bt_xiangqing);
            bt_huiku = view.findViewById(R.id.bt_huiku);
            iv_copy_phone = view.findViewById(R.id.iv_copy_phone);
        }

    }

    public UserDataZiHandShouziListAdapter(Activity activity, List<ZiXunUserInfo> userInfos, boolean isShouZi, OneFragmentMasterShouzi fragment) {
        this.isShouZi = isShouZi;
        this.activity = activity;
        mUserList = userInfos;
        this.oneFragment = fragment;
    }


    /**
     * 获取真实的手机号
     */
    private void getRealPhone(String urls, String work_id, int check) {
        try {
            String url = null;
            url = Constants.host + urls + "?work_id=" + work_id;
            Log.e("tag", "getRealPhone: " + url);
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
                    if (err == 0) {
                        String phone = object.getString("phone");
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (check == 1) {//拨打电话
                                    String call_type = EnjoyPreference.readString(activity, "calls_type");
                                    if (!TextUtils.isEmpty(call_type)) {
                                        if (call_type.equals("1")) {
                                            checkDualSim(phone);
                                        } else {
                                            getRealTime(phone, wp_id);
                                         /*   new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    NTPUDPClient timeClient = new NTPUDPClient();
                                                    InetAddress inetAddress = null;
                                                    long serverTime = 0;
                                                    try {
                                                        inetAddress = InetAddress.getByName(TIME_SERVER);
                                                        TimeInfo timeInfo = null;
                                                        try {
                                                            timeInfo = timeClient.getTime(inetAddress);
                                                            //long localDeviceTime = timeInfo.getReturnTime();
                                                            serverTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();

                                                            Date time = new Date(serverTime);
                                                            SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");//24小时制
                                                            String datatime = ss.format(time);
                                                            String currentDay = EnjoyPreference.readString(activity, "currentDay");
                                                            if (!TextUtils.isEmpty(currentDay)) {
                                                                if (!datatime.equals(currentDay)) {
                                                                    getRealToken(phone, wp_id);
                                                                    //return;
                                                                }else {
                                                                    APICallRequest(phone, wp_id, activity,datatime);//电销
                                                                }
                                                            }else {
                                                                getRealToken(phone, wp_id);
                                                            }
                                                            Log.e("tag", "Time from " + TIME_SERVER + ": " + ss.format(time));
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                            String datatime = Util.getData();
                                                            String currentDay = EnjoyPreference.readString(activity, "currentDay");
                                                            Log.e("tag", "getCurrentNetworkTime: " + e.getMessage());
                                                            if (!TextUtils.isEmpty(currentDay)) {
                                                                if (!datatime.equals(currentDay)) {
                                                                    getRealToken(phone, wp_id);
                                                                    //return;
                                                                }else {
                                                                    APICallRequest(phone, wp_id, activity,datatime);//电销
                                                                }
                                                            }else {
                                                                getRealToken(phone, wp_id);
                                                            }

                                                        }
                                                    } catch (UnknownHostException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).start();*/
                                         //   APICallRequest(phone, wp_id, activity);//电销
                                        }

                                    } else {
                                        EnjoyPreference.saveString(activity, "calls_type", "1");
                                        checkDualSim(phone);
                                    }

                                    //  checkDualSim(phone);
                                } else if (check == 2) {//备注
                                    Log.e("tag", "run: " + phone);
                                    if (isShouZi) {
                                        beizhu("other/firs_record?", work_id, phone, true);
                                    } else {
                                        beizhu("work/pool_record?", work_id, phone, false);
                                    }
                                } else {
                                    Intent intent = new Intent(activity, MineZhongHistoryActivity.class);
                                    intent.putExtra("phone", phone);
                                    activity.startActivity(intent);
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
    // 两次点击间隔不能少于1000ms
    private static final int FAST_CLICK_DELAY_TIME = 3000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= FAST_CLICK_DELAY_TIME ) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }
    /**
     * 获取token
     */
    private void getRealToken(String phone, int wp_id,String datatime) {
        try {

            String emp_name = EnjoyPreference.readString(activity, "emp_name");//名
            String emp_team_id = EnjoyPreference.readString(activity, "emp_team_id");//组id
            String emp_id = EnjoyPreference.readString(activity, "emp_id");//坐席id
            String url = null;
            url = Constants.host + "team/work_d?uid=" + emp_id + "&uname=" + emp_name + "&gid="+ emp_team_id ;//+ emp_team_id

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
                                    APICallRequest(phone, wp_id, activity,datatime);//电销
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

    public String TAG = "UserDataZiAdapter";


    /**
     * 获取时间
     */
    private void getRealTime(String phone,int wp_id) {

        okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
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
                            String  datatime =s[0];
                            Log.e("tag", "parseNetworkResponse: " + object.toString());
                            String currentDay = EnjoyPreference.readString(activity, "currentDay");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!TextUtils.isEmpty(currentDay)) {
                                        if (!datatime.equals(currentDay)) {
                                            getRealToken(phone, wp_id,datatime);
                                            //return;
                                        }else {
                                            APICallRequest(phone, wp_id, activity,datatime);//电销
                                        }
                                    }else {
                                        getRealToken(phone, wp_id,datatime);
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
                                            getRealToken(phone, wp_id,datatime);
                                            //return;
                                        }else {
                                            APICallRequest(phone, wp_id, activity,datatime);//电销
                                        }
                                    }else {
                                        getRealToken(phone, wp_id,datatime);
                                    }
                                }
                            });

                        }

                        Log.e("tag", "APICallRequestnnnnnnn2: " + result);
                    }
                });
            }
        }).start();
    }

    public static final String TIME_SERVER = "time-a.nist.gov";

    public void APICallRequest(String callee, int p_id, Activity activity,String datatime) {//电销
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
        Constants.jwtToken=jwtoken;

        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(calls_zhu_phone)) {
            Toast.makeText(activity, "手机号为空请先保存", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, SetPhoneActivity.class);
            activity.startActivity(intent);
            return;
        }
        if (simNumber == 1 && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(calls_zhu_phone)) {
            EnjoyPreference.saveString(activity, "calls_phone", "");
            EnjoyPreference.saveString(activity, "calls_zhu_phone", "");
            Toast.makeText(activity, "手机号为空请先保存", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, SetPhoneActivity.class);
            activity.startActivity(intent);
            return;
        } else if (simNumber == 2 && (TextUtils.isEmpty(phone) || TextUtils.isEmpty(calls_zhu_phone))) {
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
        Log.e("tag", "APICallRequest: " + calls_type+"===="+endUrl);
        if (calls_type.equals("2")){
            getXNum(emp_id, emp_name, p_id, emp_team_id, callee, endUrl, datatime);
        }else {
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
        Constants.jwtToken=jwtoken;
        // getXphone(emp_id,"");
        okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient();
        // {"code":1,"msg":"绑定关系失败","data":"错误码: 1005, 请查寻中普ACB对接文档"}
        okhttp3.Request request = new okhttp3.Request.Builder()
                .get()
                .addHeader("Authorization",Constants.jwtToken)//Constants.jwtToken
                .url(Constants.chuXin+"/axb/x_pool?" + endUrl)
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
                            }else if (reult==-1){
                                getRealToken(callee, p_id,datatime);
                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (msg.contains("请查寻")){
                                            String msgs=msg.replace("请查寻中普ACB对接文档","");
                                            Toast.makeText(activity, msgs, Toast.LENGTH_LONG).show();
                                        }else {
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

                        Log.e("tag", "APICallRequestx_pool: " + result);
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
        // getXphone(emp_id,"");
        okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient();
        // {"code":1,"msg":"绑定关系失败","data":"错误码: 1005, 请查寻中普ACB对接文档"}
        okhttp3.Request request = new okhttp3.Request.Builder()
                .get()
                .addHeader("Authorization",Constants.jwtToken)
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
                            }else if (reult==-1){
                                getRealToken(callee, p_id,datatime);
                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (msg.contains("请查寻")){
                                            String msgs=msg.replace("请查寻中普ACB对接文档","");
                                            Toast.makeText(activity, msgs, Toast.LENGTH_LONG).show();
                                        }else {
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
        String endUrl = "c_id=" + c_id + "&p_id=" + p_id +  "&a_num=" + a_num + "&b_num=" + callee + "&x_num=" + xnum;

     //   String endUrl = "u_id=" + u_id + "&u_name=" + emp_name + "&c_id=" + c_id + "&p_id=" + p_id + "&g_id=" + emp_team_id + "&a_num=" + a_num + "&b_num=" + callee + "&x_num=" + xnum;
        Log.e("tag", "APICallRequest: " + endUrl);
        //http://cx.chuxinjiaoyu.com/axb/zp
        okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
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
                            }else if (reult==-1){
                                getRealToken(callee, p_id,datatime);
                            } else {
                                Log.e("tag", "onResponse: ");
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (msg.contains("请查寻")){
                                            String msgs=msg.replace("请查寻中普ACB对接文档","");
                                            Toast.makeText(activity, msgs, Toast.LENGTH_LONG).show();
                                        }else {
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

                        Log.e("tag", "APICallRequestvff2: " + result);
                    }
                });
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private int checkDualSim(String phoneNumber) {
       /* if(Constants.isUpdatingFile){
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
        checkIsCall(phoneNumber, isDualSim);
      /*  if (!isDualSim) {
            //单卡
            String number = new PhoneInfoUtils(activity).getNativePhoneNumber();
            Constants.localCallNumber = number;
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            activity.startActivity(intent);
            return;
        }
        TelecomManager telecomManager = (TelecomManager)activity.getSystemService(Context.TELECOM_SERVICE);
        if(telecomManager != null) {
           *//* List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();
            Log.d(TAG, "callPhone: " + phoneAccountHandleList);
            Log.d(TAG, "callPhone: " + phoneAccountHandleList.get(1).toString());
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            int number = Math.random()>0.5?1:0;
            intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandleList.get(Math.random()>0.5?1:0));
            activity.startActivity(intent);*//*

            //@param slotId      0:卡1  1:卡2
            PhoneAccountHandle phoneAccountHandle = getPhoneAccountHandle(Math.random()>0.5?1:0);//slotId
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }*/
    }

    PhoneAccountHandle phoneAccountHandle;

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

//        if(Constants.localCallNumber != null){
         /*   if(TextUtils.isEmpty(EnjoyPreference.readString(activity,Constants.localCallNumber))){
                //获取最新一个小时的通话记录超限提醒
                getLeastCallHistory(callNumber,isDualSim);
            }else{
                String count = EnjoyPreference.readString(activity,Constants.localCallNumber);
                if(Integer.valueOf(count)<20){//+1
                    int countNum = Integer.valueOf(count)+1;
                    EnjoyPreference.saveString(activity,Constants.localCallNumber,countNum+"");*/
        if (!isDualSim) {
            Log.e("tag", "checkIsCall: " + isDualSim);
            String number = new PhoneInfoUtils(activity).getNativePhoneNumber();//
                       /* if(number == null || TextUtils.isEmpty(number)){
                            number = new PhoneInfoUtils(activity).getIccid();
                        }*/
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
            if (TextUtils.isEmpty(EnjoyPreference.readString(activity, Constants.iccID)) || EnjoyPreference.readString(activity, Constants.iccID).equals("0")) {
                //获取最新一个小时的通话记录超限提醒
                getLeastCallHistory(callNumber, isDualSim);
            } else {
                String count = EnjoyPreference.readString(activity, Constants.iccID);
                if (Integer.valueOf(count) < COUN_CALL) {//+1
                    int countNum = Integer.valueOf(count) + 1;
                    EnjoyPreference.saveString(activity, Constants.iccID, countNum + "");
                    Constants.localCallNumber = number;

                    Log.i("UserDataAdapter", "position" + 2);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + callNumber));
                    Constants.isShouZi = isShouZi;
                    activity.startActivity(intent);
                } else {
                    //本地超限则每次呼叫请求，并保存
                    getLeastCallHistory(callNumber, isDualSim);
                }
            }
        } else {
            TelecomManager telecomManager = (TelecomManager) activity.getSystemService(Context.TELECOM_SERVICE);
            Log.e("tag", "checkIsCall: ===============" + telecomManager);
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
                    Log.e("tag", "checkIsCall:nullll ");
                    getLeastCallHistory(callNumber, isDualSim);
                } else {
                    String count = EnjoyPreference.readString(activity, Constants.iccID);
                    if (Integer.valueOf(count) < COUN_CALL) {//+1
                        int countNum = Integer.valueOf(count) + 1;
                        EnjoyPreference.saveString(activity, Constants.iccID, countNum + "");

                        Log.i("UserDataAdapter", "position" + 3);
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callNumber));
                        intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Constants.isShouZi = isShouZi;
                        activity.startActivity(intent);
                    } else {
                        //本地超限则每次呼叫请求，并保存
                        getLeastCallHistory(callNumber, isDualSim);
                    }
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLeastCallHistory(final String phoneNumber, boolean isDualSim) {
        try {
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
           /* if(phoneNumber != null){
                if(phoneNumber.contains("+")){
                    phoneNumber = phoneNumber.replace("+","");
                }

                if(phoneNumber.contains("86")){
                    if(phoneNumber.substring(0,2).equals("86")){
                        phoneNumber =  phoneNumber.substring(2,phoneNumber.length());
                    }
                }
            }*/
            String reqUrl = Constants.host + "work/call_list";
            HashMap<String, String> reqMap = new HashMap<>();
            reqMap.put("page", "1");
            reqMap.put("count", "100");
            reqMap.put("at", "1");
            reqMap.put("call_type", "0");
//            reqMap.put("s_phone",Constants.localCallNumber);
            reqMap.put("p_number", Constants.iccID);
            Log.e("tag", "getLeastCallHistoryss: " + Constants.iccID);
            Log.e("tag", "getLeastCallHistory: " + reqUrl);
            OkHttpUtils.post(null, reqUrl, reqMap, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Log.e("tag", "onFailure: " + message);
                    Toast.makeText(activity, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject object) throws Exception {
                    Log.e("tag", "parseNetworkResponse: " + object);
                    JSONObject obj = object.getJSONObject("date");
                    List<Map<String, String>> listMap = new ArrayList<>();
                    Object obj2 = obj.get("list");
                    if (!obj2.toString().equals("null")) {
                        listMap = JSONUtil.getListMap(obj.getJSONArray("list"));
                    }
                            /*if(obj.get("list") != null && !obj.get("list").equals("null")){

                            }*/
                    Log.e("tag", "parseNetworkResponse: " + listMap.size());
                           /* if(listMap != null && listMap.size()>COUN_CALL){
                                Message msg = Message.obtain();
                                msg.arg1 = 1001;
                                mHandler.sendMessage(msg);
                                EnjoyPreference.saveString(activity,Constants.iccID,listMap.size()+"");
                            }else{*/
                    EnjoyPreference.saveString(activity, Constants.iccID, (listMap.size() + 1) + "");
                    Log.e("tag", "EnjoyPreferenceparseNetworkResponse: " + isDualSim);
                    if (!isDualSim) {
                        String number = new PhoneInfoUtils(activity).getNativePhoneNumber();
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
                        Constants.localCallNumber = number;
                        Log.i("UserDataAdapter", "position" + 4);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        Constants.isShouZi = isShouZi;
                        activity.startActivity(intent);
                    } else {
                        Log.e("tag", "EnjoyPreferenceparseNetworkResponse:ddddddddd " + isDualSim);
                        TelecomManager telecomManager = (TelecomManager) activity.getSystemService(Context.TELECOM_SERVICE);
                        if (telecomManager != null) {

                            //@param slotId      0:卡1  1:卡2
//                                        double idCard = Math.random();idCard>0.7?1:0
                                       /* int numb = new Random().nextInt(2);
                                        PhoneAccountHandle phoneAccountHandle = getPhoneAccountHandle(numb);//slotId*/

                            Log.i("UserDataAdapter", "position" + 1);
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                            intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Constants.isShouZi = isShouZi;
                            activity.startActivity(intent);
                        }
                    }
                    //  }
                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

  /*  Handler handlerThree=new Handler(Looper.getMainLooper());
                                    handlerThree.post(new Runnable(){
        public void run(){
            PromptManager.showMyToast(Constants.localCallNumber+"此号码已超过小时上限24次,请换号呼叫",CallService.this);
        }
    });*/

   /* Handler mHandler = new Handler(){

        //handleMessage为处理消息的方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1 == 1001){

                View view = View.inflate(activity, R.layout.call_warn_dialog, null);
                TextView tv_to_bind_warn= view.findViewById(R.id.tv_to_bind_warn);
                if(Constants.localCallNumber != null){
                    tv_to_bind_warn.setText(Constants.localCallNumber+"你最近一个小时单卡拨打电话量已经"+COUN_CALL+"次上限，请换卡或者稍后再次拨打!");
                }else{
                    tv_to_bind_warn.setText(Constants.iccID+"你最近一个小时单卡拨打电话量已经"+COUN_CALL+"次上限，请换卡或者稍后再次拨打!");
            }

                view.findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PromptManager.closeCustomDialog();

                    }
                });
                view.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PromptManager.closeCustomDialog();
                    }
                });

                PromptManager.showCustomDialog(activity, view, Gravity.CENTER, Gravity.CENTER);
                //PromptManager.showMyToast(Constants.localCallNumber+"此号码已超过小时上限24次,请换号呼叫",CallService.this);
            }
        }
    };*/

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


    /**
     * @param url
     * @param w_id
     */

    private void huiku(String url, String w_id, boolean isShou, int position) {
        if (isShou) {
            String reqUrl = Constants.host + url + "id=" + w_id;
            OkHttpUtils.get(activity, reqUrl, new OkhttpCallback() {
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
                public Object parseNetworkResponse(JSONObject object) throws
                        Exception {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "回库成功",
                                    Toast.LENGTH_SHORT).show();
                            oneFragment.countNumber--;
                            oneFragment.tv_count_number.setText("共" + oneFragment.countNumber + "条");
                            //刷新数据及列表
                            oneFragment.userInfos.remove(position);
                            oneFragment.adapter.setData(oneFragment.userInfos);
                            oneFragment.adapter.notifyDataSetChanged();

                            if (mUserList.size() == 0 && oneFragment.recycle_view != null) {
                                oneFragment.recycle_view.setVisibility(View.GONE);
                                oneFragment.ll_refresh_none.setVisibility(View.VISIBLE);
                            } else {
                                if (oneFragment.ll_refresh_none != null) {
                                    oneFragment.ll_refresh_none.setVisibility(View.GONE);
                                    oneFragment.recycle_view.setVisibility(View.VISIBLE);
                                }
                            }
                            oneFragment.adapter.notifyDataSetChanged();


                        }
                    });

                    return null;
                }
            });
        } else {
            String reqUrl = Constants.host + url;
            HashMap<String, String> map = new HashMap<>();
            map.put("id", w_id);
            OkHttpUtils.post(activity, reqUrl, map, new OkhttpCallback() {
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
                public Object parseNetworkResponse(JSONObject object) throws
                        Exception {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "回库成功",
                                    Toast.LENGTH_SHORT).show();
                            oneFragment.countNumber--;
                            oneFragment.tv_count_number.setText("共" + oneFragment.countNumber + "条");
                            //刷新数据及列表
                            oneFragment.userInfos.remove(position);
                            oneFragment.adapter.setData(oneFragment.userInfos);
                            oneFragment.adapter.notifyDataSetChanged();

                            if (mUserList.size() == 0 && oneFragment.recycle_view != null) {
                                oneFragment.recycle_view.setVisibility(View.GONE);
                                oneFragment.ll_refresh_none.setVisibility(View.VISIBLE);
                            } else {
                                if (oneFragment.ll_refresh_none != null) {
                                    oneFragment.ll_refresh_none.setVisibility(View.GONE);
                                    oneFragment.recycle_view.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });

                    return null;
                }
            });
        }

    }
}