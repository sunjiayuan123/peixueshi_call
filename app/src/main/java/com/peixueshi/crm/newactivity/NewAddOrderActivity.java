package com.peixueshi.crm.newactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.mf.library.utils.ToastUtils;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.CertificateBean;
import com.peixueshi.crm.bean.HelpSignBean;
import com.peixueshi.crm.bean.NewOrderUserInfo;
import com.peixueshi.crm.ui.newadapter.NewIndentAdapter;
import com.peixueshi.crm.ui.newadapter.NewSelectOtherAdapter;
import com.peixueshi.crm.ui.newadapter.NewSelectWangAdapter;
import com.peixueshi.crm.ui.newadapter.NewSelectXieAdapter;
import com.peixueshi.crm.ui.newadapter.NewSelectXueAdapter;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.PromptManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewAddOrderActivity extends BaseActivity {

    @BindView(R.id.client_id)
    TextView clientId;
    @BindView(R.id.client_ed)
    TextView clientEd;
    @BindView(R.id.client_rl)
    RelativeLayout clientRl;
    @BindView(R.id.phone_text)
    TextView phoneText;
    @BindView(R.id.phone_ed)
    TextView phoneEd;
    @BindView(R.id.phone_rl)
    RelativeLayout phoneRl;
    @BindView(R.id.type_text)
    TextView typeText;
    @BindView(R.id.rb_quankuan)
    RadioButton rbQuankuan;
    @BindView(R.id.rb_dingjin)
    RadioButton rbDingjin;
    @BindView(R.id.rb_weikuan)
    RadioButton rbWeikuan;
    @BindView(R.id.rg_pay_leixing_detail)
    RadioGroup rgPayLeixingDetail;
    @BindView(R.id.type_rl)
    RelativeLayout typeRl;
    @BindView(R.id.client_earnest)
    TextView clientEarnest;
    @BindView(R.id.client_recycle)
    RecyclerView clientRecycle;
    @BindView(R.id.client_earnestrl)
    RelativeLayout clientEarnestrl;
    @BindView(R.id.tv_wangke_leixing)
    TextView tvWangkeLeixing;
    @BindView(R.id.ll_wangke_include)
    RelativeLayout llWangkeInclude;
    @BindView(R.id.include_select_order_xueli)
    RelativeLayout includeSelectOrderXueli;
    @BindView(R.id.ll_xiezhu_include)
    RelativeLayout llXiezhuInclude;
    @BindView(R.id.ll_zhiqu_include)
    RelativeLayout llZhiquInclude;
    @BindView(R.id.selected_text)
    TextView selectedText;
    @BindView(R.id.client_recyclerview)
    RecyclerView clientRecyclerview;
    @BindView(R.id.selected_rl)
    RelativeLayout selectedRl;
    @BindView(R.id.client_money_text)
    TextView clientMoneyText;
    @BindView(R.id.client_money)
    TextView clientMoney;
    @BindView(R.id.client_money_rl)
    RelativeLayout clientMoneyRl;
    @BindView(R.id.order_form_text)
    TextView orderFormText;
    @BindView(R.id.order_form_ed)
    EditText orderFormEd;
    @BindView(R.id.order_form_rl)
    RelativeLayout orderFormRl;
    @BindView(R.id.client_ll)
    LinearLayout clientLl;
    @BindView(R.id.game_play)
    TextView gamePlay;
    @BindView(R.id.ll_submit_order)
    LinearLayout llSubmitOrder;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.wangke)
    TextView wangke;
    @BindView(R.id.re_wangke)
    RelativeLayout reWangke;
    @BindView(R.id.re_xueli)
    RelativeLayout reXueli;
    @BindView(R.id.xue_recyclerview)
    RecyclerView xueRecyclerview;
    @BindView(R.id.re_xie)
    RelativeLayout reXie;
    @BindView(R.id.xie_recyclerview)
    RecyclerView xieRecyclerview;
    @BindView(R.id.re_other)
    RelativeLayout reOther;
    @BindView(R.id.other_recyclerview)
    RecyclerView otherRecyclerview;
    @BindView(R.id.wang_re)
    RelativeLayout wangRe;
    private Intent intent;

    int type;
    public static List<Map<String, String>> wangList = new ArrayList<>();
    public static List<Map<String, String>> xueList = new ArrayList<>();
    public static List<HelpSignBean> xieList = new ArrayList<>();
    public static List<CertificateBean> otherList = new ArrayList<>();
    private NewSelectWangAdapter newSelectWangAdapter;
    boolean wangZhan = false;
    boolean xueZhan = false;
    private NewSelectXueAdapter newSelectXueAdapter;
    private NewSelectXieAdapter newSelectXieAdapter;
    private boolean xieZhan = false;
    private NewSelectOtherAdapter newSelectOtherAdapter;
    private boolean otherZhan = false;
    public static int price;
    public static int wangPrice;
    public static int xiePrice;
    public static int otherPrice;
    public static int xuePrice;
    public static TextView clientMoneys;
    private Map<String, String> infos;
    private int billType = 1;
    private int num = 0;
    private JSONObject ziXunUserInfo;
    private String customIds;
    private String phone;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new_add_order;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //tab颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        clientMoneys = clientMoney;
        Intent intent = getIntent();
        infos = (Map<String, String>) intent.getSerializableExtra("info");
        String workPool = infos.get("workPool");
        ziXunUserInfo = JSONObject.parseObject(workPool);
        customIds = ziXunUserInfo.getString("id");
        phone = ziXunUserInfo.getString("phone");
        clientMoneys.setText("");
        clientEd.setText(customIds);
        phoneEd.setText(phone);
        rgPayLeixingDetail.check(R.id.rb_quankuan);
        rgPayLeixingDetail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                clearList();
                if (checkedId == rbQuankuan.getId()) {
                    billType = 1;//全款
                    wangRe.setVisibility(View.VISIBLE);
                    //et_mother_order_id.setVisibility(View.GONE);
                } else if (checkedId == rbDingjin.getId()) {
                    billType = 3;//定金
                    getSelect(customIds, 100, billType, 1);
                    //et_mother_order_id.setVisibility(View.VISIBLE);
                } else {
                    billType = 2;//尾款
                    getSelect(customIds, 2, 3, 1);
                    //et_mother_order_id.setVisibility(View.VISIBLE);
                }
            }
        });

        newSelectWangAdapter = new NewSelectWangAdapter(this, wangList);
        clientRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        clientRecyclerview.setAdapter(newSelectWangAdapter);
        newSelectWangAdapter.setOnItemClickListener(new NewSelectWangAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int pos) {
                if (wangList.size() > 0) {
                    wangList.remove(pos);
                    newSelectWangAdapter.setData(wangList, 1);
                    setWangPrice(wangList);
                }
            }
        });
        newSelectXueAdapter = new NewSelectXueAdapter(this, xueList);
        xueRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        xueRecyclerview.setAdapter(newSelectXueAdapter);
        newSelectXueAdapter.setOnItemClickListener(new NewSelectXueAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int pos) {
                if (xueList.size() > 0) {
                    xueList.remove(pos);
                    newSelectXueAdapter.setData(xueList);
                    setXuePrice(xueList);
                }
            }
        });

        newSelectXieAdapter = new NewSelectXieAdapter(this, xieList);
        xieRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        xieRecyclerview.setAdapter(newSelectXieAdapter);
        newSelectXieAdapter.setOnItemClickListener(new NewSelectXieAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int pos) {
                if (xieList.size() > 0) {
                    xieList.remove(pos);
                    newSelectXieAdapter.setData(xieList);
                    setXiePrice(xieList);
                }
            }
        });
        newSelectOtherAdapter = new NewSelectOtherAdapter(this, otherList);
        otherRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        otherRecyclerview.setAdapter(newSelectOtherAdapter);

        newSelectOtherAdapter.setOnItemClickListener(new NewSelectOtherAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int pos) {
                if (otherList.size() > 0) {
                    otherList.remove(pos);
                    newSelectOtherAdapter.setData(otherList);
                    setOtherPrice(otherList);
                }
            }
        });
        price = wangPrice + xuePrice + xiePrice + otherPrice;
        clientMoney.setText(price + "");
        clearList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (newSelectWangAdapter != null && wangList.size() > 0) {
            newSelectWangAdapter.setData(wangList, 1);
            reWangke.setVisibility(View.VISIBLE);
        } else {
            reWangke.setVisibility(View.GONE);
        }
        if (newSelectXueAdapter != null && xueList.size() > 0) {
            newSelectXueAdapter.setData(xueList);
            reXueli.setVisibility(View.VISIBLE);
        } else {
            reXueli.setVisibility(View.GONE);
        }
        if (newSelectXieAdapter != null && xieList.size() > 0) {
            newSelectXieAdapter.setData(xieList);
            reXie.setVisibility(View.VISIBLE);
        } else {
            reXie.setVisibility(View.GONE);
        }
        if (newSelectOtherAdapter != null && otherList.size() > 0) {
            newSelectOtherAdapter.setData(otherList);
            reOther.setVisibility(View.VISIBLE);
        } else {
            reOther.setVisibility(View.GONE);
        }
        price = wangPrice + xuePrice + xiePrice + otherPrice;
        clientMoney.setText(price + "");

    }

    public static void setWangPrice(List<Map<String, String>> listmap) {
        NewAddOrderActivity.wangList = listmap;
        wangPrice = 0;
        for (Map<String, String> stringStringMap : listmap) {
            if (!TextUtils.isEmpty(stringStringMap.get("newPrice"))) {
                wangPrice = wangPrice + Integer.valueOf(stringStringMap.get("newPrice"));
            } else {
                wangPrice = 0;
            }
        }
        price = wangPrice + xuePrice + xiePrice + otherPrice;
        clientMoneys.setText(price + "");
    }

    public static void setXuePrice(List<Map<String, String>> listmap) {
        NewAddOrderActivity.xueList = listmap;
        xuePrice = 0;
        for (Map<String, String> stringStringMap : listmap) {
            if (!TextUtils.isEmpty(stringStringMap.get("newPrice"))) {
                xuePrice = xuePrice + Integer.valueOf(stringStringMap.get("newPrice"));
            } else {
                xuePrice = 0;
            }
        }
        price = wangPrice + xuePrice + xiePrice + otherPrice;
        clientMoneys.setText(price + "");
    }

    public static void setXiePrice(List<HelpSignBean> listmap) {
        NewAddOrderActivity.xieList = listmap;
        xiePrice = 0;
        for (int i = 0; i < listmap.size(); i++) {
            String price = listmap.get(i).getNewPrice();
            if (!TextUtils.isEmpty(price)) {
                xiePrice = xiePrice + Integer.valueOf(price);
            } else {
                xiePrice = 0;
            }
        }
        price = wangPrice + xuePrice + xiePrice + otherPrice;
        clientMoneys.setText(price + "");
    }

    public static void setOtherPrice(List<CertificateBean> listmap) {
        NewAddOrderActivity.otherList = listmap;
        otherPrice = 0;
        for (int i = 0; i < listmap.size(); i++) {
            String price = listmap.get(i).getNewPrice();
            if (!TextUtils.isEmpty(price)) {
                otherPrice = otherPrice + Integer.valueOf(price);
            } else {
                otherPrice = 0;
            }
        }
        price = wangPrice + xuePrice + xiePrice + otherPrice;
        clientMoneys.setText(price + "");
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @OnClick({R.id.re_xueli, R.id.re_xie, R.id.re_other, R.id.re_wangke, R.id.iv_back, R.id.client_id, R.id.client_ed, R.id.client_rl, R.id.phone_text, R.id.phone_ed, R.id.phone_rl, R.id.type_text, R.id.rb_quankuan, R.id.rb_dingjin, R.id.rb_weikuan, R.id.rg_pay_leixing_detail, R.id.type_rl, R.id.client_earnest, R.id.client_recycle, R.id.client_earnestrl, R.id.tv_wangke_leixing, R.id.ll_wangke_include, R.id.include_select_order_xueli, R.id.ll_xiezhu_include, R.id.ll_zhiqu_include, R.id.selected_text, R.id.selected_rl, R.id.client_money_text, R.id.client_money, R.id.client_money_rl, R.id.order_form_text, R.id.order_form_ed, R.id.order_form_rl, R.id.client_ll, R.id.game_play, R.id.ll_submit_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.client_id:
                break;
            case R.id.re_wangke://网课
                if (wangZhan) {
                    wangZhan = false;
                    clientRecyclerview.setVisibility(View.GONE);
                } else {
                    clientRecyclerview.setVisibility(View.VISIBLE);
                    wangZhan = true;
                }

                break;
            case R.id.re_xueli://学历
                if (xueZhan) {
                    xueZhan = false;
                    xueRecyclerview.setVisibility(View.GONE);
                } else {
                    xueRecyclerview.setVisibility(View.VISIBLE);
                    xueZhan = true;
                }

                break;
            case R.id.re_xie://协助报名
                if (xieZhan) {
                    xieZhan = false;
                    xieRecyclerview.setVisibility(View.GONE);
                } else {
                    xieRecyclerview.setVisibility(View.VISIBLE);
                    xieZhan = true;
                }
                break;
            case R.id.re_other://其他
                if (otherZhan) {
                    otherZhan = false;
                    otherRecyclerview.setVisibility(View.GONE);
                } else {
                    otherRecyclerview.setVisibility(View.VISIBLE);
                    otherZhan = true;
                }
                break;
            case R.id.client_ed://客户id
                break;
            case R.id.client_rl:
                break;
            case R.id.phone_text:
                break;
            case R.id.phone_ed://手机号
                break;
            case R.id.phone_rl:
                break;
            case R.id.type_text:
                break;
            case R.id.rb_quankuan://全款

                break;
            case R.id.rb_dingjin://订金
                break;
            case R.id.rb_weikuan://尾款
                break;
            case R.id.rg_pay_leixing_detail:
                break;
            case R.id.type_rl:
                break;
            case R.id.client_earnest:
                break;
            case R.id.client_recycle:
                break;
            case R.id.client_earnestrl:
                break;
            case R.id.tv_wangke_leixing:
                break;
            case R.id.ll_wangke_include://网课
                intent = new Intent(this, NewNetWorkClassActivity.class);
                intent.putExtra("type", 4);
                startActivity(intent);
                break;
            case R.id.include_select_order_xueli://学历
                intent = new Intent(this, NewSelectTypeActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.ll_xiezhu_include://协助报名
                intent = new Intent(this, NewSelectTypeActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.ll_zhiqu_include://其他
                intent = new Intent(this, NewSelectTypeActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
                break;
            case R.id.selected_text:
                break;

            case R.id.selected_rl:
                break;
            case R.id.client_money_text:
                break;
            case R.id.client_money:
                break;
            case R.id.client_money_rl:
                break;
            case R.id.order_form_text:
                break;
            case R.id.order_form_ed:
                break;
            case R.id.order_form_rl:
                break;
            case R.id.client_ll:
                break;
            case R.id.game_play://提交订单
                String clientEdstr = clientEd.getText().toString();//客户ID
                String phoneEdstr = phoneEd.getText().toString();//手机号
                String clientMoneystr = clientMoney.getText().toString();//订单总金额
                String notes = orderFormEd.getText().toString();//备注
                int orderType = 0;
                String s = clientMoneys.getText().toString();
                if (s.equals("0")) {
                    ToastUtils.showShort("订单金额不能为0");
                    break;
                }
                //billType 1 全款 2 尾款 3 定金
                JSONArray endArray = new JSONArray();

                Long customId = Long.valueOf(customIds);
                int placeid = 0;
                int totalPrice = Integer.valueOf(clientMoneys.getText().toString()) * 100;
                if (wangList.size() > 0) {

                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < wangList.size(); i++) {
                        orderType = 1;
                        Map<String, String> stringStringMap = wangList.get(i);
                        try {
                            String classId = stringStringMap.get("classId");//classId
                            String className = stringStringMap.get("className");
                            String price = stringStringMap.get("newPrice");
                            String projectId = stringStringMap.get("projectId");
                            String projectName = stringStringMap.get("projectName");
                            JSONObject user1 = new JSONObject();
                            user1.put("classId", Integer.valueOf(classId));
                            user1.put("className", className);
                            if (!TextUtils.isEmpty(price)) {
                                user1.put("price",Integer.valueOf(price)*100);
                            } else {
                                user1.put("price", 0);
                            }
                            user1.put("projectId", Integer.valueOf(projectId));
                            user1.put("projectName", projectName);
                            jsonArray.add(i, user1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    JSONObject user1 = new JSONObject();
                    user1.put("billType", billType);
                    user1.put("customerId", customId);
                    user1.put("notes", notes);
                    user1.put("phone", phone);
                    user1.put("placeId", placeid);
                    user1.put("totalPrice", totalPrice);
                    user1.put("infos", jsonArray);
                    user1.put("orderType", orderType);
                    endArray.add(endArray.size(), user1);
                }


                if (xueList.size() > 0) {
                    JSONArray jsonArrayXue = new JSONArray();
                    for (int i = 0; i < xueList.size(); i++) {
                        Map<String, String> jsonObject = xueList.get(i);
                        orderType = 2;

                        try {
                            String schoolId = jsonObject.get("schoolId");

                            String schoolName = jsonObject.get("schoolName");
                            String idCard = jsonObject.get("idCard");
                            String majorName = jsonObject.get("majorName");
                            String majorId = jsonObject.get("majorId");
                            String sStatus = jsonObject.get("sStatus");
                            String studentName = jsonObject.get("studentName");
                            String mStatus = jsonObject.get("mStatus");
                            String price = jsonObject.get("newPrice");
                            String address = jsonObject.get("address");
                            JSONObject user1 = new JSONObject();
                            user1.put("majorId", Integer.valueOf(majorId));
                            user1.put("majorName", majorName);
                            user1.put("mStatus", Integer.valueOf(mStatus));
                            user1.put("address", address);
                            user1.put("idCard", idCard);
                            if (!TextUtils.isEmpty(price)) {
                                user1.put("price", Integer.valueOf(price)*100);
                            } else {
                                user1.put("price", 0);
                            }
                            user1.put("sStatus", Integer.valueOf(sStatus));
                            user1.put("schoolId", Integer.valueOf(schoolId));
                            user1.put("schoolName", schoolName);
                            user1.put("studentName", studentName);
                            jsonArrayXue.add(i, user1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    JSONObject user1 = new JSONObject();
                    user1.put("billType", billType);
                    user1.put("customerId", customId);
                    user1.put("notes", notes);
                    user1.put("phone", phone);
                    user1.put("placeId", placeid);
                    user1.put("totalPrice", totalPrice);

                    user1.put("infos", jsonArrayXue);
                    user1.put("orderType", orderType);
                    endArray.add(endArray.size(), user1);
                    num = num + 1;
                    Log.e("tag", "onViewClicked: "+endArray );
                }


                if (xieList.size() > 0) {
                    JSONArray jsonArrayXie = new JSONArray();
                    for (int i = 0; i < xieList.size(); i++) {
                        orderType = 3;
                        String address = xieList.get(i).getAddress();
                        String oAddress = xieList.get(i).getRegistrationArea();
                        String price = xieList.get(i).getNewPrice();
                        int projectId = xieList.get(i).getProjectId();
                        String projectName = xieList.get(i).getpName();
                        String studentName = xieList.get(i).getStu_name();
                        String idCard = xieList.get(i).getIdCard();
                        JSONObject user1 = new JSONObject();
                        user1.put("oAddress", oAddress);
                        user1.put("projectName", projectName);
                        user1.put("projectId", projectId);
                        user1.put("address", address);
                        user1.put("idCard", idCard);
                        user1.put("price", Integer.valueOf(price)*100);
                        user1.put("studentName", studentName);
                        jsonArrayXie.add(i, user1);
                    }
                    JSONObject user1 = new JSONObject();
                    user1.put("billType", billType);
                    user1.put("customerId", customId);
                    user1.put("notes", notes);
                    user1.put("phone", phone);
                    user1.put("placeId", placeid);
                    user1.put("totalPrice", totalPrice);

                    user1.put("infos", jsonArrayXie);
                    user1.put("orderType", orderType);
                    endArray.add(endArray.size(), user1);
                    num = num + 1;
                }


                if (otherList.size() > 0) {
                    JSONArray jsonArrayOther = new JSONArray();
                    for (int i = 0; i < otherList.size(); i++) {
                        orderType = 4;
                        String address = otherList.get(i).getAddress();
                        String idCard = otherList.get(i).getIdCard();
                        String certName = otherList.get(i).getName();
                        String stu_name = otherList.get(i).getStu_name();
                        String newPrice = otherList.get(i).getNewPrice();
                        JSONObject user1 = new JSONObject();
                        user1.put("address", address);
                        user1.put("idCard", idCard);
                        user1.put("price", Integer.valueOf(price)*100);
                        user1.put("studentName", stu_name);
                        user1.put("certName", certName);
                        jsonArrayOther.add(i, user1);
                    }
                    JSONObject user1 = new JSONObject();
                    user1.put("billType", billType);
                    user1.put("customerId", customId);
                    user1.put("notes", notes);
                    user1.put("phone", phone);
                    user1.put("placeId", placeid);
                    user1.put("totalPrice", totalPrice);
                    user1.put("infos", jsonArrayOther);
                    user1.put("orderType", orderType);
                    endArray.add(endArray.size(), user1);
                    num = num + 1;
                }
                JSONObject endUser1 = new JSONObject();
                endUser1.put("items", endArray);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (billType == 1) {
                            String post = post(Constants.allhost + "order/ply_order", endUser1.toJSONString());
                        } else {
                            Log.e("tag", "run: " + endUser1.toJSONString());
                            String post = post(Constants.allhost + "order/deposit_balance", endUser1.toJSONString());//定金尾款下单


                            JSONObject rowData = JSONObject.parseObject(post);
                            int code = (int) rowData.get("code");
                            if (code == 0) {
                                ToastUtils.showShort("提交成功");
                                if (billType == 3) {
                                    getSelect(customIds, 100, billType, 1);
                                } else if (billType == 2) {
                                    getSelect(customIds, 2, 3, 1);
                                }
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                clearList();
                            }
                        });

                    }
                }).start();
                break;
            case R.id.ll_submit_order:
                break;
        }
    }

    public void clearList() {
        if (wangList != null) {
            wangList.clear();
            newSelectWangAdapter.setData(wangList, 1);
            reWangke.setVisibility(View.GONE);
        }
        if (xueList != null) {
            xueList.clear();
            newSelectXueAdapter.setData(xueList);
            reXueli.setVisibility(View.GONE);
        }
        if (xieList != null) {
            xieList.clear();
            newSelectXieAdapter.setData(xieList);
            reXie.setVisibility(View.GONE);
        }
        if (otherList != null) {
            otherList.clear();
            newSelectOtherAdapter.setData(otherList);
            reOther.setVisibility(View.GONE);
        }
    }

    private void getSelect(String csId, int state, int payType, int repair) {
        //type=1临时开课   type=2取消订单
        try {
            String url;

            url = Constants.allhost + "order/deposit";

            /*csId: 10710000004393
            payTyp: 3
            repair: 1
            state: 2*/
            HashMap<String, String> keyMap = new HashMap<String, String>();
            keyMap.put("csId", csId + "");//客户Id
            keyMap.put("state", state + "");//1 未支付 2 已支付 3 已分配 4已驳回 5手动取消 6自动取消 ，如果传100的话是所有状态
            keyMap.put("payType", payType + "");//1 全款 2,尾款,3定金
            keyMap.put("repair", repair + "");//是否有补过 定金的尾款状态 1 没补过 2已补过
            OkHttpUtils.newPost(this, url, keyMap, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(NewAddOrderActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(org.json.JSONObject object) throws
                        Exception {
                    if (object.has("code")) {
                        int code = object.getInt("code");
                        Log.e("tag", "run:ssssssssssss " + object);
                        if (code == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        org.json.JSONObject data = object.getJSONObject("data");
                                        int total = data.getInt("total");

                                        if (total != 0) {
                                            org.json.JSONArray list = data.getJSONArray("list");
                                            List<Map<String, String>> listMap = JSONUtil.getListMap(list);
                                            Log.e("tag", "run: " + listMap);
                                            //已存在的定金订单Adapter
                                            NewIndentAdapter newIndentAdapter = new NewIndentAdapter(NewAddOrderActivity.this, listMap);
                                            //RecyclerView布局显示方式
                                            clientRecycle.setLayoutManager(new LinearLayoutManager(NewAddOrderActivity.this));
                                            clientRecycle.setAdapter(newIndentAdapter);
                                            clientEarnestrl.setVisibility(View.VISIBLE);

                                            newIndentAdapter.setOnItemClickListener(new NewIndentAdapter.OnItemClickListener() {
                                                @Override
                                                public void OnItemClickListener(int pos, int type) {//关联
                                                    Map<String, String> stringStringMap = listMap.get(pos);
                                                    String orderType = stringStringMap.get("orderType");
                                                    String details = stringStringMap.get("details");
                                                    String orderId = stringStringMap.get("orderId");
                                                    JSONArray jsonArray = JSONObject.parseArray(details);
                                                    if (type == 1) {
                                                        wangRe.setVisibility(View.GONE);
                                                        if (orderType.equals("1")) {//  1,网课
                                                            for (int i = 0; i < jsonArray.size(); i++) {
                                                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                                int classId = jsonObject.getIntValue("classId");

                                                                String className = jsonObject.getString("className");
                                                                String projectName = jsonObject.getString("projectName");
                                                                int projectId = jsonObject.getIntValue("projectId");
                                                                Map<String, String> wangMap = new HashMap<>();
                                                                wangMap.put("classId", classId + "");
                                                                wangMap.put("className", className);
                                                                wangMap.put("projectName", projectName);
                                                                wangMap.put("projectId", projectId + "");
                                                                wangMap.put("orderId",orderId);
                                                                wangList.add(wangMap);
                                                            }

                                                            if (wangList.size() > 0) {
                                                                newSelectWangAdapter.setData(wangList, 2);
                                                                reWangke.setVisibility(View.VISIBLE);
                                                            }
                                                        } else if (orderType.equals("2")) {// 2,学历
                                                            for (int i = 0; i < jsonArray.size(); i++) {
                                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                                int schoolId = jsonObject.getIntValue("schoolId");

                                                                String schoolName = jsonObject.getString("schoolName");
                                                                String idCard = jsonObject.getString("idCard");
                                                                String majorName = jsonObject.getString("majorName");
                                                                String majorId = jsonObject.getString("majorId");
                                                                String sStatus = jsonObject.getString("sStatus");
                                                                String studentName = jsonObject.getString("studentName");
                                                                Log.e("tag", "OnItemClickListener: "+studentName );
                                                                String address = jsonObject.getString("address");
                                                                int mStatus = jsonObject.getIntValue("mStatus");
                                                                Map<String, String> xueMap = new HashMap<>();
                                                                xueMap.put("schoolId", schoolId + "");
                                                                xueMap.put("schoolName", schoolName);
                                                                xueMap.put("idCard", idCard);
                                                                xueMap.put("majorName", majorName + "");
                                                                xueMap.put("majorId", majorId + "");
                                                                xueMap.put("sStatus", sStatus + "");
                                                                xueMap.put("studentName", studentName + "");
                                                                xueMap.put("mStatus", mStatus + "");
                                                                xueMap.put("address", address + "");
                                                                xueMap.put("orderId", orderId + "");
                                                                xueList.add(xueMap);
                                                            }
                                                            if (xueList.size() > 0) {
                                                                newSelectXueAdapter.setData(xueList);
                                                                reXueli.setVisibility(View.VISIBLE);
                                                            }

                                                        } else if (orderType.equals("3")) {// 3协助报名

                                                            for (int i = 0; i < jsonArray.size(); i++) {
                                                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                                int projectId = jsonObject.getIntValue("projectId");
                                                                String projectName = jsonObject.getString("projectName");
                                                                String idCard = jsonObject.getString("idCard");
                                                                String studentName = jsonObject.getString("studentName");
                                                                String oAddress = jsonObject.getString("oAddress");
                                                                String address = jsonObject.getString("address");
                                                                HelpSignBean helpSignBean = new HelpSignBean();
                                                                helpSignBean.setProjectId(projectId);
                                                                helpSignBean.setAddress(address);
                                                                helpSignBean.setpName(projectName);
                                                                helpSignBean.setStu_name(studentName);
                                                                helpSignBean.setIdCard(idCard);
                                                                helpSignBean.setRegistrationArea(oAddress);
                                                                helpSignBean.setOrderId(orderId);
                                                                xieList.add(helpSignBean);
                                                            }
                                                            if (xieList.size() > 0) {
                                                                newSelectXieAdapter.setData(xieList);
                                                                reXie.setVisibility(View.VISIBLE);
                                                            }
                                                        } else if (orderType.equals("4")) {//4其他

                                                            for (int i = 0; i < jsonArray.size(); i++) {
                                                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                                String certName = jsonObject.getString("certName");
                                                                String idCard = jsonObject.getString("idCard");
                                                                String studentName = jsonObject.getString("studentName");
                                                                String oAddress = jsonObject.getString("oAddress");
                                                                String address = jsonObject.getString("address");
                                                                CertificateBean certificateBean = new CertificateBean();
                                                                certificateBean.setName(certName);
                                                                certificateBean.setAddress(address);
                                                                certificateBean.setStu_name(studentName);
                                                                certificateBean.setIdCard(idCard);
                                                                certificateBean.setRegistrationArea(oAddress);
                                                                certificateBean.setOrderId(orderId);
                                                                otherList.add(certificateBean);
                                                            }
                                                            if (otherList.size() > 0) {
                                                                newSelectOtherAdapter.setData(otherList);
                                                                reOther.setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                    } else {
                                                        wangRe.setVisibility(View.VISIBLE);
                                                        if (orderType.equals("1")) {//  1,网课

                                                            for (int i = 0; i < wangList.size(); i++) {
                                                                String orderId1 = wangList.get(i).get("orderId");
                                                                if (!TextUtils.isEmpty(orderId)&&!TextUtils.isEmpty(orderId1)){
                                                                    if (orderId.equals(orderId1)){
                                                                        wangList.remove(i);
                                                                    }
                                                                }
                                                            }
                                                            newSelectWangAdapter.setData(wangList, 2);
                                                            if (wangList.size()<0){
                                                                reWangke.setVisibility(View.GONE);
                                                            }
                                                        } else if (orderType.equals("2")) {// 2,学历

                                                            for (int i = 0; i < xueList.size(); i++) {
                                                                String orderId1 = xueList.get(i).get("orderId");
                                                                if (!TextUtils.isEmpty(orderId)&&!TextUtils.isEmpty(orderId1)){
                                                                    if (orderId.equals(orderId1)){
                                                                        xueList.remove(i);
                                                                    }
                                                                }
                                                            }
                                                            newSelectXueAdapter.setData(xueList);
                                                            if (xueList.size()<0){
                                                                reXueli.setVisibility(View.GONE);
                                                            }
                                                        } else if (orderType.equals("3")) {// 3协助报名
                                                            for (int i = 0; i < xieList.size(); i++) {
                                                                String orderId1 = xieList.get(i).getOrderId();
                                                                if (!TextUtils.isEmpty(orderId)&&!TextUtils.isEmpty(orderId1)){
                                                                    if (orderId.equals(orderId1)){
                                                                        xieList.remove(i);
                                                                    }
                                                                }
                                                            }
                                                            newSelectXieAdapter.setData(xieList);
                                                            if (xieList.size()<0){
                                                                reXie.setVisibility(View.GONE);
                                                            }
                                                        } else if (orderType.equals("4")) {//4其他
                                                            for (int i = 0; i < otherList.size(); i++) {
                                                                String orderId1 = otherList.get(i).getOrderId();
                                                                if (!TextUtils.isEmpty(orderId)&&!TextUtils.isEmpty(orderId1)){
                                                                    if (orderId.equals(orderId1)){
                                                                        otherList.remove(i);
                                                                    }
                                                                }
                                                            }
                                                            newSelectOtherAdapter.setData(otherList);
                                                            if (otherList.size()<0){
                                                                reOther.setVisibility(View.GONE);
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        } else {
                                            if (billType == 2) {
                                                showDialogMessage(NewAddOrderActivity.this, "当前客户ID下无未补尾款的定金订单,请选择全款或定金下单", true);
                                            }
                                        }

                                    } catch (org.json.JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    private void getDepsitbla(String billNo) {

        try {
            String url;

            url = Constants.allhost + "order/depsitbla";


            HashMap<String, String> keyMap = new HashMap<String, String>();
            keyMap.put("billNo", billNo + "");//（订单号）
            keyMap.put("status", 2 + "");//1 未支付 2 已支付 3 已分配 4已驳回 5手动取消 6自动取消 ，如果传100的话是所有状态
            keyMap.put("orderId", billNo + "");//订单ID
            keyMap.put("parentOrder", billNo + "");//母订单单号
            keyMap.put("billNo", billNo + "");//（订单号）
            OkHttpUtils.newPost(this, url, keyMap, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(NewAddOrderActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(org.json.JSONObject object) throws
                        Exception {
                    if (object.has("code")) {
                        int code = object.getInt("code");
                        if (code == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showDialogMessage(NewAddOrderActivity.this, "当前客户ID下无未补尾款的定金订单,请选择全款或定金下单", true);
                                }
                            });
                        }
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
     * 获取订单
     */
    private void initDataOride() {
        try {
            String url = null;
            url = Constants.allhost + "comm/orderBList";
            String endUrl = "?who=1&phone=" + phone + "&page=1&pnum=100";
            Log.e("tag", "initData: " + endUrl);
            // http://tserver.api.yuxuejiaoyu.net/crm/comm/orderBList?csId=&orderId=&phone=&state=1&page=2&pnum=10
            // http://tserver.api.yuxuejiaoyu.net/crm/comm/orderBList?csId=&orderId=&phone=&state=1&page=1&pnum=10
            OkHttpUtils.newGet(this, url + endUrl, new OkhttpCallback() {

                private int state;

                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(NewAddOrderActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(org.json.JSONObject jsonObject) throws
                        Exception {
                    List<NewOrderUserInfo> userInfo = getUserInfo(jsonObject);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (userInfo.size() > 0) {
                                int num = 0;
                                for (int i = 0; i < userInfo.size(); i++) {
                                    int orderType = userInfo.get(i).getBillType();
                                    if (orderType == 3) {
                                        num = orderType;
                                    }
                                }
                                if (num == 0) {
                                    state = 1;
                                } else {
                                    state = 2;
                                }

                            } else {
                                state = 1;
                            }

                        }
                    });
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    //getSelect(ziXunUserInfo.getString("id"),state, 3);
    private List<NewOrderUserInfo> getUserInfo(org.json.JSONObject job) throws Exception {
        org.json.JSONObject object = job.getJSONObject("data");

        String total = object.getString("total");
        org.json.JSONArray jsonArray;
        if (!total.equals("0")) {
            jsonArray = object.getJSONArray("list");
        } else {
            jsonArray = null;
        }

        if (jsonArray != null) {
            List<NewOrderUserInfo> userGet = JSONUtil.parserArrayList(jsonArray, NewOrderUserInfo.class, 1);
            return userGet;
        }
        return null;
    }


    public void showDialogMessage(Activity activity, String msg, boolean isOpen) {
        View view = View.inflate(activity, R.layout.create_chance_dialog, null);
        TextView textView = view.findViewById(R.id.tv_to_bind_warn);
        textView.setText(msg);
        TextView tv_create = view.findViewById(R.id.tv_create);//确定
        tv_create.setText("定金下单");
        tv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptManager.closeCustomDialog();
                billType = 3;
                rbDingjin.setChecked(true);

            }
        });

        view.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptManager.closeCustomDialog();
            }
        });
        TextView tv_cancle = view.findViewById(R.id.tv_cancle);//取消
        tv_cancle.setText("全款下单");
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptManager.closeCustomDialog();
                billType = 1;
                rbQuankuan.setChecked(true);
            }
        });
        PromptManager.showCustomDialog(activity, view, Gravity.CENTER, Gravity.CENTER);
    }

    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 100;
    public final static int WRITE_TIMEOUT = 60;
    public static final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
            .build();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public String post(String url, String json) {
        String token;
        if (Constants.newloginUserInfo != null) {
            token = Constants.newloginUserInfo.getAcc_token();
        } else {
            token = EnjoyPreference.readString(this, "acc_token");
        }

        if (token == null) {
            token = "";
        }
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url).addHeader("Authorization", token)
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.isSuccessful()) {
            try {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new IOException("Unexpected code " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
