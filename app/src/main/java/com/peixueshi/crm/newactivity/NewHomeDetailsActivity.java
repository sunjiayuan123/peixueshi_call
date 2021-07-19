package com.peixueshi.crm.newactivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.NewOrderUserInfo;
import com.peixueshi.crm.bean.ZiXunUserInfo;
import com.peixueshi.crm.ui.newadapter.NewHomeItemsAdapter;
import com.peixueshi.crm.utils.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewHomeDetailsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.hand_text)
    TextView handText;
    @BindView(R.id.rl_title_view)
    RelativeLayout rlTitleView;
    @BindView(R.id.details)
    TextView details;
    @BindView(R.id.client_id)
    TextView clientId;
    @BindView(R.id.postion_detail)
    TextView postionDetail;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.xiangmu)
    TextView xiangmu;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.heat)
    TextView heat;
    @BindView(R.id.heat_text)
    TextView heatText;
    @BindView(R.id.source)
    TextView source;
    @BindView(R.id.source_text)
    TextView sourceText;
    @BindView(R.id.sky)
    TextView sky;
    @BindView(R.id.sky_test)
    TextView skyTest;
    @BindView(R.id.consult_time)
    TextView consultTime;
    @BindView(R.id.consult_time_test)
    TextView consultTimeTest;
    @BindView(R.id.finally_consult_time)
    TextView finallyConsultTime;
    @BindView(R.id.finally_consult_time_test)
    TextView finallyConsultTimeTest;
    @BindView(R.id.bz_time)
    TextView bzTime;
    @BindView(R.id.bz_time_test)
    TextView bzTimeTest;
    @BindView(R.id.order_bz)
    TextView orderBz;
    @BindView(R.id.bz_text)
    TextView bzText;
    @BindView(R.id.details_details)
    TextView detailsDetails;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.name_text)
    TextView nameText;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.sex_text)
    TextView sexText;
    @BindView(R.id.wx)
    TextView wx;
    @BindView(R.id.wx_text)
    TextView wxText;
    @BindView(R.id.xueli)
    TextView xueli;
    @BindView(R.id.xueli_text)
    TextView xueliText;
    @BindView(R.id.major)
    TextView major;
    @BindView(R.id.major_text)
    TextView majorText;
    @BindView(R.id.area)
    TextView area;
    @BindView(R.id.area_test)
    TextView areaTest;
    @BindView(R.id.record)
    TextView record;
    @BindView(R.id.client_ida)
    TextView clientIda;
    @BindView(R.id.client_ida_text)
    TextView clientIdaText;
    @BindView(R.id.xiamua)
    TextView xiamua;
    @BindView(R.id.xiamua_text)
    TextView xiamuaText;
    @BindView(R.id.record_money)
    TextView recordMoney;
    @BindView(R.id.record_money_text)
    TextView recordMoneyText;
    @BindView(R.id.record_time)
    TextView recordTime;
    @BindView(R.id.record_time_text)
    TextView recordTimeText;
    @BindView(R.id.rlv)
    RecyclerView recycle_view;
    @BindView(R.id.lin_img)
    LinearLayout lin_img;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.has_data)
    RelativeLayout hasData;
    private boolean isLoadMore;
    private int page = 1;
    private String endUrl;
    private NewHomeItemsAdapter adapter;
    private boolean isShow = false;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new_home_details;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //tab颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Intent intent = getIntent();
        String csid = intent.getStringExtra("csid");
        String phone = intent.getStringExtra("phone");
        Log.e("tag", "initData: " + csid);
        initData(csid);
        initDatas(phone);
        /*NewHomeDetailsAdapter newHomeDetailsAdapter = new NewHomeDetailsAdapter();
        rlv.setLayoutManager(new LinearLayoutManager(this));
        rlv.setAdapter(newHomeDetailsAdapter);*/

    }

    /**
     * 获取首咨或者库存
     */
    private void initData(String csid) {
        try {
            String url = null;
            url = Constants.allhost + "cmc/csid_info?csid=" + csid;

            OkHttpUtils.newGet(this, url, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Log.e("tag", "onFailure: " + message);
                    Toast.makeText(NewHomeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject object) throws
                        Exception {
                    JSONObject data = object.getJSONObject("data");
                    int total = data.getInt("total");
                    if (total != 0) {
                        JSONArray jsonArray = data.getJSONArray("list");
                        List<Map<String, String>> listMap = JSONUtil.getListMap(jsonArray);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < listMap.size(); i++) {
                                    Map<String, String> stringStringMap = listMap.get(i);
                                    String baseProject = stringStringMap.get("baseProject");
                                    com.alibaba.fastjson.JSONObject baseProjects = com.alibaba.fastjson.JSONObject.parseObject(baseProject);

                                    String projName = baseProjects.getString("projName");

                                    int levels = baseProjects.getIntValue("levels");//意向度
                                    String csid = baseProjects.getString("id");

                                    String baseBase = stringStringMap.get("baseBase");
                                    com.alibaba.fastjson.JSONObject baseBases = com.alibaba.fastjson.JSONObject.parseObject(baseBase);
                                    String name = baseBases.getString("name");
                                    String phone = baseBases.getString("phone");
                                    int sex = baseBases.getIntValue("sex");
                                    int srank = baseBases.getIntValue("srank");
                                    String weChar = baseBases.getString("weChar");
                                    String major = baseBases.getString("major");
                                    String address = baseBases.getString("address");
                                    clientIdaText.setText(csid);
                                    postionDetail.setText(csid);
                                    tvUserName.setText(phone);
                                    tvPosition.setText(projName);
                                    sourceText.setText("");//机会来源
                                    nameText.setText(name);
                                    wxText.setText(weChar);

                                    majorText.setText(major);
                                    areaTest.setText(address);
                                    if (srank == 1) {
                                        xueliText.setText("初中以下");
                                    } else if (srank == 2) {
                                        xueliText.setText("中专");
                                    } else if (srank == 3) {
                                        xueliText.setText("高中");
                                    } else if (srank == 4) {
                                        xueliText.setText("大专");
                                    } else if (srank == 5) {
                                        xueliText.setText("本科");
                                    } else if (srank == 6) {
                                        xueliText.setText("硕士");
                                    } else if (srank == 7) {
                                        xueliText.setText("博士");
                                    }

                                    if (sex == 1) {
                                        sexText.setText("男");
                                    } else {
                                        sexText.setText("女");
                                    }

                                    if (levels == 1) {
                                        heatText.setText("A");
                                    } else if (levels == 2) {
                                        heatText.setText("B");
                                    } else if (levels == 3) {
                                        heatText.setText("C");
                                    }

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

    /**
     * 获取订单
     */
    private void initDatas(String phone) {
        try {
            String url = null;
            url = Constants.allhost + "comm/orderBList";
            if (isLoadMore) {
                page++;
            } else {//刷新
                page = 1;
            }
            /*csId:
            orderId:
            phone:
            state: 1
            page: 1
            pnum: 10*/
            endUrl = "?csId=&orderId=&phone=" + phone + "&who=" + 1 + "&page=" + page + "&pnum=100";

            Log.e("tag", "initData: " + endUrl);
            // http://tserver.api.yuxuejiaoyu.net/crm/comm/orderBList?csId=&orderId=&phone=&state=1&page=2&pnum=10
            // http://tserver.api.yuxuejiaoyu.net/crm/comm/orderBList?csId=&orderId=&phone=&state=1&page=1&pnum=10
            OkHttpUtils.newGet(this, url + endUrl, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(NewHomeDetailsActivity.this, message,
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

    ZiXunUserInfo info;
    List<NewOrderUserInfo> userInfos = new ArrayList<NewOrderUserInfo>();

    private ZiXunUserInfo getUserInfo(JSONObject job) throws Exception {
        JSONObject object = job.getJSONObject("data");

        String total = object.getString("total");
        JSONArray jsonArray;
        if (!total.equals("0")) {
            jsonArray = object.getJSONArray("list");
            hasData.setVisibility(View.VISIBLE);
        } else {

            hasData.setVisibility(View.GONE);
            jsonArray = null;
        }

        if (jsonArray != null) {
            List<NewOrderUserInfo> userGet = JSONUtil.parserArrayList(jsonArray, NewOrderUserInfo.class, 1);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //recycle_view.setVisibility(View.VISIBLE);
                    if (!isLoadMore) {
                        userInfos.addAll(0, userGet);
                        if (adapter == null) {
                            adapter = new NewHomeItemsAdapter(NewHomeDetailsActivity.this, userInfos);
                            recycle_view.setLayoutManager(new LinearLayoutManager(NewHomeDetailsActivity.this));
                            recycle_view.setAdapter(adapter);
                        }
                        adapter.setData(userInfos);
                        adapter.notifyDataSetChanged();
                        //  pullDownView.endUpdate(new Date());
                        if (userInfos != null && userInfos.size() > 0) {
                            if (userInfos.size() < Integer.valueOf(total)) {
                                // recycle_view.onLoadMoreComplete(true);
                            } else {
                                //   recycle_view.onLoadMoreComplete(false);
                            }
                        } else {
                            // recycle_view.onLoadMoreComplete(false);
                        }
                    } else {
                        userInfos.addAll(userGet);
                        if (adapter == null) {
                            adapter = new NewHomeItemsAdapter(NewHomeDetailsActivity.this, userInfos);
                            recycle_view.setAdapter(adapter);
                        }
                        adapter.setData(userInfos);
                        adapter.notifyDataSetChanged();
                        // pullDownView.endUpdate(new Date());
                        if (userInfos != null && userInfos.size() > 0) {
                            if (userInfos.size() < Integer.valueOf(total)) {
                                // recycle_view.onLoadMoreComplete(true);
                            } else {
                                // recycle_view.onLoadMoreComplete(false);
                            }
                        } else {
                            //recycle_view.onLoadMoreComplete(false);
                        }
                    }

                }
            });
        }
/*
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (userInfos.size() == 0) {
                    ll_refresh.setVisibility(View.VISIBLE);

                } else {
                    ll_refresh.setVisibility(View.GONE);
                }
            }
        });*/

        return info;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @OnClick({R.id.iv_back, R.id.hand_text, R.id.rl_title_view, R.id.details, R.id.client_id, R.id.postion_detail, R.id.phone, R.id.tv_user_name, R.id.xiangmu, R.id.tv_position, R.id.heat, R.id.heat_text, R.id.source, R.id.source_text, R.id.sky, R.id.sky_test, R.id.consult_time, R.id.consult_time_test, R.id.finally_consult_time, R.id.finally_consult_time_test, R.id.bz_time, R.id.bz_time_test, R.id.order_bz, R.id.bz_text, R.id.details_details, R.id.name, R.id.name_text, R.id.sex, R.id.sex_text, R.id.wx, R.id.wx_text, R.id.xueli, R.id.xueli_text, R.id.major, R.id.major_text, R.id.area, R.id.area_test, R.id.record, R.id.client_ida, R.id.client_ida_text, R.id.xiamua, R.id.xiamua_text, R.id.record_money, R.id.record_money_text, R.id.record_time, R.id.record_time_text, R.id.rlv, R.id.lin_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.hand_text:
                break;
            case R.id.rl_title_view:
                break;
            case R.id.details:
                break;
            case R.id.client_id:
                break;
            case R.id.postion_detail:
                break;
            case R.id.phone:
                break;
            case R.id.tv_user_name:
                break;
            case R.id.xiangmu:
                break;
            case R.id.tv_position:
                break;
            case R.id.heat:
                break;
            case R.id.heat_text:
                break;
            case R.id.source:
                break;
            case R.id.source_text:
                break;
            case R.id.sky:
                break;
            case R.id.sky_test:
                break;
            case R.id.consult_time:
                break;
            case R.id.consult_time_test:
                break;
            case R.id.finally_consult_time:
                break;
            case R.id.finally_consult_time_test:
                break;
            case R.id.bz_time:
                break;
            case R.id.bz_time_test:
                break;
            case R.id.order_bz:
                break;
            case R.id.bz_text:
                break;
            case R.id.details_details:
                break;
            case R.id.name:
                break;
            case R.id.name_text:
                break;
            case R.id.sex:
                break;
            case R.id.sex_text:
                break;
            case R.id.wx:
                break;
            case R.id.wx_text:
                break;
            case R.id.xueli:
                break;
            case R.id.xueli_text:
                break;
            case R.id.major:
                break;
            case R.id.major_text:
                break;
            case R.id.area:
                break;
            case R.id.area_test:
                break;
            case R.id.record:
                break;
            case R.id.client_ida:
                break;
            case R.id.client_ida_text:
                break;
            case R.id.xiamua:
                break;
            case R.id.xiamua_text:
                break;
            case R.id.record_money:
                break;
            case R.id.record_money_text:
                break;
            case R.id.record_time:
                break;
            case R.id.record_time_text:
                break;
            case R.id.rlv:
                break;
            case R.id.lin_img:


                if (isShow) {
                    recycle_view.setVisibility(View.GONE);
                    img.setImageResource(R.mipmap.bottom_img);
                    isShow = false;
                } else {
                    img.setImageResource(R.mipmap.top_img);
                    recycle_view.setVisibility(View.VISIBLE);
                    isShow = true;
                }

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
