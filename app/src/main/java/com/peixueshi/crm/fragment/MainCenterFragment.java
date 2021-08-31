package com.peixueshi.crm.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.peixueshi.crm.MainActivity;
import com.peixueshi.crm.R;
import com.peixueshi.crm.activity.LoginActivity;
import com.peixueshi.crm.activity.MineCallHistoryActivity;
import com.peixueshi.crm.activity.MineCreateChanceActivity;
import com.peixueshi.crm.activity.MineZhongHistoryActivity;
import com.peixueshi.crm.activity.MinehandMasterActivity;
import com.peixueshi.crm.activity.ModifyPassActivity;
import com.peixueshi.crm.activity.OrderActivity;
import com.peixueshi.crm.activity.RealPhoneActivity;
import com.peixueshi.crm.activity.ScanSureActivity;
import com.peixueshi.crm.activity.SetPhoneActivity;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.UserDetailInfo;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.ui.adapter.MyViewPagerAdapter;
import com.peixueshi.crm.ui.widget.CircleImageView;
import com.peixueshi.crm.utils.Constant;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.PromptManager;
import com.peixueshi.crm.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainCenterFragment extends BaseFragment {

    @BindView(R.id.iv_user_icon)
    CircleImageView iv_user_icon;
    @BindView(R.id.tv_user_phone)
    TextView tv_user_phone;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.tv_xiaozu_1)
    TextView tv_xiaozu_1;
    @BindView(R.id.tv_xiangmu)
    TextView tv_xiangmu;

    @BindView(R.id.tv_rate)
    TextView tv_rate;
    @BindView(R.id.tv_connect_count)
    TextView tv_connect_count;

    @BindView(R.id.rl_jihui)
    RelativeLayout rl_jihui;
    @BindView(R.id.rl_exit)
    RelativeLayout rl_exit;
    @BindView(R.id.rl_put_order)
    RelativeLayout rl_put_order;

    @BindView(R.id.rl_modify_pass)
    RelativeLayout rl_modify_pass;
    @BindView(R.id.rl_call_history)
    RelativeLayout rl_call_history;
    //号码设置
    @BindView(R.id.rl_call_set)
    RelativeLayout rl_call_set;
    @BindView(R.id.rl_jilu)
    RelativeLayout rl_jilu;

    @BindView(R.id.rl_sound_local)
    RelativeLayout rl_sound_local;

    @BindView(R.id.tv_attache)
    TextView tv_attache;
    @BindView(R.id.tv_attache_count)
    TextView tv_attache_count;
    @BindView(R.id.tv_call_count)
    TextView tv_call_count;
    @BindView(R.id.tv_duration)
    TextView tv_duration;
    @BindView(R.id.tv_scan)
    TextView tv_scan;
    //    @BindView(R.id.tv_sound_local)
    public static TextView tv_sound_local;
    @BindView(R.id.rl_set)
    RelativeLayout rl_set;
    @BindView(R.id.view_line4)
    View view_line4;

    MyViewPagerAdapter adapter;
    /*  @BindView(R.id.viewpager)
      ViewPager vp;*/
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

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
        return ArmsUtils.inflate(getActivity(), R.layout.fragment_center);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        if (tabLayout != null) {
            Calendar calendar = Calendar.getInstance();
            Date today = new Date();
            calendar.setTime(today);// 此处可换为具体某一时间
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
            Log.i("MainCenterFragment", "点击了" + currentSelectTab);
            if (currentSelectTab == 0) {
                getcountTime(1);
            } else if (currentSelectTab == 1) {
                getcountTime(weekDay);
            } else if (currentSelectTab == 2) {
                getcountTime(monthDay);
            }
        }
        tv_sound_local = getActivity().findViewById(R.id.tv_sound_local);
        if (Constants.qiniuToken != null && tv_sound_local != null) {
            tv_sound_local.setText("本地录音(共" + MainActivity.getSoundReocrdCount() + "条,可点击上传)");
        }
    }


    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                Toast.makeText(getActivity(), "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
            }
            // 申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String uuid = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            Toast.makeText(getActivity(), "扫码结果" + uuid, Toast.LENGTH_SHORT);
            //将扫描出的信息显示出来
//            tvResult.setText(scanResult);
            requestCodeIsLogin(uuid);
        }
    }


    private void requestCodeIsLogin(String uuid) {
        try {
            String reqUrl = Constants.host + "login/qr_login_scan?uuid=" + uuid;
            OkHttpUtils.get(getActivity(), reqUrl, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(getActivity(), message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject object) throws
                        Exception {
                    Intent intent = new Intent(getActivity(), ScanSureActivity.class);
                    intent.putExtra("uuid", uuid);
                    startActivity(intent);
                    return null;
                }


            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(getActivity(), "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private int currentSelectTab = 0;
    public void getcountTime(int day){
        String calls_type = EnjoyPreference.readString(getActivity(), "calls_type");
        if (!TextUtils.isEmpty(calls_type)){
            if (calls_type.equals("1")){
                getMineAchievement(day);
            }else if (calls_type.equals("3")){
                getMineAchievementAXB(day);
            }else if (calls_type.equals("2")){
                getMineAchievementAXB(day);
            }
        }else {
            getMineAchievementAXB(day);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (Constants.headMaster) {
            if (rl_modify_pass != null) {
                rl_modify_pass.setVisibility(View.GONE);
            }
        }

        tabLayout.addTab(tabLayout.newTab().setText("今日"));
        tabLayout.addTab(tabLayout.newTab().setText("本周"));
        tabLayout.addTab(tabLayout.newTab().setText("本月"));

        tv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCode();
            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            Log.e(TAG, "tab点击事件");
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (null == tab) return;
            // 这里使用到反射，拿到Tab对象后获取Class
            Class c = tab.getClass();
            try {
                // Filed “字段、属性”的意思,c.getDeclaredField 获取私有属性。
                // "view"是Tab的私有属性名称(可查看TabLayout源码),类型是 TabView,TabLayout私有内部类。
                Field field = c.getDeclaredField("view");
                field.setAccessible(true);
                final View view = (View) field.get(tab);
                if (null == view) return;
                view.setTag(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        Date today = new Date();
                        calendar.setTime(today);// 此处可换为具体某一时间
                        int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
                        int position = (int) view.getTag();
                        if (position == 0) {
                            currentSelectTab = 0;
                            getcountTime(1);
                        } else if (position == 1) {
                            currentSelectTab = 1;
                            getcountTime(weekDay);
                        } else {
                            currentSelectTab = 2;
                            getcountTime(monthDay);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (Constants.headMaster) {
            rl_jilu.setVisibility(View.VISIBLE);

            rl_jilu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MinehandMasterActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            rl_jilu.setVisibility(View.GONE);

        }

        getUserInfo();
        String calls_type = EnjoyPreference.readString(getActivity(), "calls_type");
        if (!TextUtils.isEmpty(calls_type)){
            if (calls_type.equals("1")){
                getMineAchievement(1);
            }else if (calls_type.equals("3")){
                getMineAchievementAXB(1);
            }else if (calls_type.equals("2")){
                getMineAchievementAXB(1);
            }
        }else {
            getMineAchievementAXB(1);
        }





/*
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选择时触发
                TextView title = (TextView)(((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                //  title.setTextSize(18);
                title.setAlpha(0.9f);
                title.setTextAppearance(getActivity(), R.style.TabLayoutTextStyle);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //未选择是触发
                TextView title = (TextView)(((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                //  title.setTextSize(14);
                title.setAlpha(0.5f);
                title.setTextAppearance(getActivity(), Typeface.NORMAL);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //选中之后再次点击即复选时触发
             *//*   if (tab.getText().equals("产品分类")) {
                    mPopupWindow.showAsDropDown(mTabLayout);
                }*//*
//                adapter.notifyDataSetChanged();
            }
        });
        tabLayout.getTabAt(0).select();*/
    }

    /**
     * 获取个人信息
     */
    private void getUserInfo() {
        try {
            OkHttpUtils.get(getActivity(), Constants.host + "user/info", new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(getActivity(), message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject jsonObject) throws
                        Exception {
                    getUserDetail(jsonObject);
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 获取个人业绩
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getMineAchievement(int day) {
        try {
            long current = System.currentTimeMillis();
            // long zero = current/(1000*3600*24*day)*(1000*3600*24*day) - TimeZone.getDefault().getRawOffset();


            HashMap<String, String> keyMap = new HashMap<>();
            keyMap.put("s_time", Util.getPastDateMills(day));
            keyMap.put("e_time", current / 1000 + "");

            OkHttpUtils.post(getActivity(), Constants.host + "work/own_list", keyMap, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(getActivity(), message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject job) throws
                        Exception {

                    JSONArray array = job.getJSONArray("info");
                    JSONObject object = array.getJSONObject(0);
                    if (object != null) {
                        Map<String, String> call = JSONUtil.getMap(object.getJSONObject("state"));
                        Map<String, String> performance = JSONUtil.getMap(object.getJSONObject("performance"));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (TextUtils.isEmpty(performance.get("total_price"))) {
                                    performance.put("total_price", "0");
                                }
                                if (TextUtils.isEmpty(performance.get("single"))) {
                                    performance.put("single", "0");
                                }
                                if (TextUtils.isEmpty(call.get("duratioin"))) {
                                    call.put("duratioin", "0");
                                }
                                if (TextUtils.isEmpty(call.get("dial"))) {
                                    call.put("dial", "0");
                                }
                                int attach = Integer.valueOf(performance.get("total_price"));
                                tv_attache.setText(new Double(attach) / new Double(100) + "");
                                tv_attache_count.setText(performance.get("single"));//成交单数

                                String duration = Util.getTime(Integer.valueOf(call.get("duratioin")));//时长
                                tv_call_count.setText(call.get("dial"));//拨打量
                                tv_duration.setText(duration);
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
    /**
     * 获取个人业绩
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getMineAchievementAXB(int day) {
        try {
            long current = System.currentTimeMillis();
            String jwtoken = EnjoyPreference.readString(getActivity(), "jwtoken");
            Constants.jwtToken = jwtoken;
            // long zero = current/(1000*3600*24*day)*(1000*3600*24*day) - TimeZone.getDefault().getRawOffset();


           /* HashMap<String, String> keyMap = new HashMap<>();
            keyMap.put("c_s_time", Util.getPastDateMills(day));
            keyMap.put("c_e_time", current / 1000 + "");*/
           String e_time=(current / 1000)+"";

            okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient();
            // {"code":1,"msg":"绑定关系失败","data":"错误码: 1005, 请查寻中普ACB对接文档"}
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .get()
                    .addHeader("Authorization",Constants.jwtToken)//Constants.jwtToken
                    .url(Constants.chuXin + "/token/user_time?c_s_time="+Util.getPastDateMills(day)+"&c_e_time="+e_time)
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
                                int code = object.getInt("code");
                                if (code==0){
                                    JSONObject array = object.getJSONObject("data");
                                    int talk_time = array.getInt("talk_time");
                                    int talk_count = array.getInt("talk_count");
                                    int connect_count = array.getInt("connect_count");
                                    String duration = Util.getTime(Integer.valueOf(talk_time));//时长
                                    String connectNum = Util.getConnectNum(connect_count, talk_count);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tv_call_count.setText(String.valueOf(talk_count));//拨打量
                                            tv_duration.setText(duration);
                                            tv_connect_count.setText(String.valueOf(connect_count));
                                            tv_rate.setText(connectNum);
                                        }
                                    });
                                }else if (code<0){
                                    getRealToken();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "解析异常", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            Log.e("tag", "APICallRequestx_pool: " + result);
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 获取token
     */
    private void getRealToken() {
        try {

            String emp_name = EnjoyPreference.readString(getActivity(), "emp_name");//名
            String emp_team_id = EnjoyPreference.readString(getActivity(), "emp_team_id");//组id
            String emp_id = EnjoyPreference.readString(getActivity(), "emp_id");//坐席id
            String url = null;
            url = Constants.host + "team/work_d?uid=" + emp_id + "&uname=" + emp_name + "&gid=" + emp_team_id;//+ emp_team_id
            Log.e("tag", "getRealToken: " + url);


            OkHttpUtils.get(getActivity(), url, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Log.e("tag", "onFailure:4 " + message);
                    Toast.makeText(getActivity(), message,
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EnjoyPreference.saveString(getActivity(), "jwtoken", data);
                                Constants.jwtToken = data;
                                if (tabLayout != null) {
                                    Calendar calendar = Calendar.getInstance();
                                    Date today = new Date();
                                    calendar.setTime(today);// 此处可换为具体某一时间
                                    int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                                    int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
                                    Log.i("MainCenterFragment", "点击了" + currentSelectTab);
                                    if (currentSelectTab == 0) {
                                        getcountTime(1);
                                    } else if (currentSelectTab == 1) {
                                        getcountTime(weekDay);
                                    } else if (currentSelectTab == 2) {
                                        getcountTime(monthDay);
                                    }
                                }
                            }
                        });
                    } else {
                        getRealToken();
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    UserDetailInfo info;

    private UserDetailInfo getUserDetail(JSONObject job) throws Exception {
        JSONObject object = job.getJSONObject("info");
        if (object != null) {
            info = JSONUtil.parser(object, UserDetailInfo.class, 0);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_user_phone.setText(info.getEmp_phone());
                    tv_user_name.setText(info.getEmp_name());
                    tv_xiaozu_1.setText(info.getTe_name());
                    tv_xiangmu.setText(info.getPr_name());
                }
            });
        }
        return info;
    }

    @Override
    public void setData(@Nullable Object data) {

    }


    @OnClick({R.id.rl_zhongpu_history,R.id.rl_call_set, R.id.rl_set, R.id.rl_exit, R.id.rl_put_order, R.id.rl_jihui, R.id.rl_modify_pass, R.id.rl_call_history, R.id.rl_sound_local})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //号码设置
            case R.id.rl_call_set:
                Intent intent1 = new Intent(getActivity(), RealPhoneActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_set:
                Intent intent = new Intent(getActivity(), SetPhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_exit:
                /*String[] aa = new String[]{"18313869712","18313869712","18313869712","18313869712","18313869712"};
                String aa2 = Arrays.toString(aa);*/

                showSureDialog();
                break;
            case R.id.rl_put_order:
                Intent intentOrder = new Intent(getActivity(), OrderActivity.class);
                startActivity(intentOrder);
                break;
            case R.id.rl_jihui:
                Intent intentChance = new Intent(getActivity(), MineCreateChanceActivity.class);
                startActivity(intentChance);
                break;
            case R.id.rl_modify_pass:
                Intent intentPass = new Intent(getActivity(), ModifyPassActivity.class);
                startActivity(intentPass);
                break;
            case R.id.rl_call_history:
                Intent callIntent = new Intent(getActivity(), MineCallHistoryActivity.class);
                startActivity(callIntent);
                break;
            case R.id.rl_zhongpu_history:
                Intent callIntent1 = new Intent(getActivity(), MineZhongHistoryActivity.class);
                startActivity(callIntent1);
            break;
            case R.id.rl_sound_local://录音上传
                if (MainActivity.getSoundReocrdCount() > 0) {
                    PromptManager.showMyToast("录音上传中", getContext());
                    if (Constants.isUpdatingFile) {
                        return;
                    }
                    if (Constants.qiniuToken != null) {
                        MainActivity.initInfos(Constants.qiniuToken);
                    }
                } else {
                    PromptManager.showMyToast("本地暂无录音", getContext());
                }
                break;
        }
    }

    private void showSureDialog() {
        View view = View.inflate(getActivity(), R.layout.exit_dialog, null);
        view.findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptManager.closeCustomDialog();
                EnjoyPreference.saveString(getActivity(), "user_phone", "");
                EnjoyPreference.saveString(getActivity(), "pass", "");
                EnjoyPreference.saveString(getActivity(), "acc_token", "");
                EnjoyPreference.saveString(getActivity(), "emp_id", "");
                EnjoyPreference.saveString(getActivity(), "calls_zhu_phone", "");
                EnjoyPreference.saveString(getActivity(), "calls_phone", "");
                EnjoyPreference.saveString(getActivity(), "jwtoken","");
               /* EnjoyPreference.saveString(getActivity(), "currentDay", "");
                EnjoyPreference.saveString(getActivity(), "xnum","");*/
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

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
            }
        });
        PromptManager.showCustomDialog(getActivity(), view, Gravity.CENTER, Gravity.CENTER);
    }
}
