package com.peixueshi.crm.newactivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.peixueshi.crm.R;
import com.peixueshi.crm.bean.CertificateBean;
import com.peixueshi.crm.bean.HelpSignBean;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.ui.newadapter.NewEamestOtherAdapter;
import com.peixueshi.crm.ui.newadapter.NewEamestWangAdapter;
import com.peixueshi.crm.ui.newadapter.NewEamestXueAdapter;
import com.peixueshi.crm.ui.newadapter.NewEamesttXieAdapter;
import com.peixueshi.crm.ui.newadapter.NeworderMsgAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class NewIndentDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.hand_text)
    TextView handText;
    @BindView(R.id.rl_title_view)
    RelativeLayout rlTitleView;
    @BindView(R.id.order_number)
    TextView orderNumber;
    @BindView(R.id.order_recycle)
    RecyclerView orderRecycle;
    @BindView(R.id.wangke)
    TextView wangke;
    @BindView(R.id.re_wangke)
    RelativeLayout reWangke;
    @BindView(R.id.client_recyclerview)
    RecyclerView clientRecyclerview;
    @BindView(R.id.xueli)
    TextView xueli;
    @BindView(R.id.re_xueli)
    RelativeLayout reXueli;
    @BindView(R.id.xue_recyclerview)
    RecyclerView xueRecyclerview;
    @BindView(R.id.xie_zhu)
    TextView xieZhu;
    @BindView(R.id.re_xie)
    RelativeLayout reXie;
    @BindView(R.id.xie_recyclerview)
    RecyclerView xieRecyclerview;
    @BindView(R.id.other_zhu)
    TextView otherZhu;
    @BindView(R.id.re_other)
    RelativeLayout reOther;
    @BindView(R.id.other_recyclerview)
    RecyclerView otherRecyclerview;
    @BindView(R.id.selected_rl)
    RelativeLayout selectedRl;
    private boolean wangZhan=false;
    private boolean otherZhan=false;
    private boolean xueZhan=false;
    private boolean xieZhan=false;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new_indent_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //tab颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Intent intent = getIntent();
        Map<String, String> stringStringMap = (Map<String, String>) intent.getSerializableExtra("stringStringMap");
        List<Map<String, String>> list = new ArrayList<>();
        list.add(stringStringMap);

        //订单信息
        NeworderMsgAdapter neworderMsgAdapter = new NeworderMsgAdapter(this, list);
        orderRecycle.setLayoutManager(new LinearLayoutManager(this));
        orderRecycle.setAdapter(neworderMsgAdapter);

        String orderType = stringStringMap.get("orderType");
        String details = stringStringMap.get("details");
        String orderId = stringStringMap.get("orderId");
        JSONArray jsonArray = JSONObject.parseArray(details);
        List<Map<String, String>> wangList = new ArrayList<>();
        List<Map<String, String>> xueList = new ArrayList<>();
        List<HelpSignBean> xieList = new ArrayList<>();
        List<CertificateBean> otherList = new ArrayList<>();
        reWangke.setVisibility(View.GONE);
        //网课
        NewEamestWangAdapter newEamestWangAdapter = new NewEamestWangAdapter(this,wangList);
        clientRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        clientRecyclerview.setAdapter(newEamestWangAdapter);

        //学历
        NewEamestXueAdapter newEamestXueAdapter = new NewEamestXueAdapter(this,xueList);
        xueRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        xueRecyclerview.setAdapter(newEamestXueAdapter);

        //协助报名
        NewEamesttXieAdapter newEamesttXieAdapter = new NewEamesttXieAdapter(this,xieList);
        xieRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        xieRecyclerview.setAdapter(newEamesttXieAdapter);

        //其他
        NewEamestOtherAdapter newEamestOtherAdapter = new NewEamestOtherAdapter(this,otherList);
        otherRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        otherRecyclerview.setAdapter(newEamestOtherAdapter);

        if (orderType.equals("1")) {//  1,网课
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int classId = jsonObject.getIntValue("classId");

                String className = jsonObject.getString("className");
                String projectName = jsonObject.getString("projectName");
                int projectId = jsonObject.getIntValue("projectId");
                int price = jsonObject.getIntValue("price");
                Map<String, String> wangMap = new HashMap<>();
                wangMap.put("classId", classId + "");
                wangMap.put("className", className);
                wangMap.put("projectName", projectName);
                wangMap.put("projectId", projectId + "");
                wangMap.put("orderId", orderId);
                wangMap.put("newPrice",price+"");
                wangList.add(wangMap);
            }

            if (wangList.size() > 0) {
                newEamestWangAdapter.setData(wangList, 2);
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
                String address = jsonObject.getString("address");
                int mStatus = jsonObject.getIntValue("mStatus");
                int price = jsonObject.getIntValue("price");
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
                xueMap.put("newPrice",price+"");
                xueList.add(xueMap);
            }
            if (xueList.size() > 0) {
                newEamestXueAdapter.setData(xueList);
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
                int price = jsonObject.getIntValue("price");
                HelpSignBean helpSignBean = new HelpSignBean();
                helpSignBean.setProjectId(projectId);
                helpSignBean.setAddress(address);
                helpSignBean.setpName(projectName);
                helpSignBean.setStu_name(studentName);
                helpSignBean.setIdCard(idCard);
                helpSignBean.setRegistrationArea(oAddress);
                helpSignBean.setOrderId(orderId);
                helpSignBean.setNewPrice(price+"");
                xieList.add(helpSignBean);
            }
            if (xieList.size() > 0) {
                newEamesttXieAdapter.setData(xieList);
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
                int price = jsonObject.getIntValue("price");
                CertificateBean certificateBean = new CertificateBean();
                certificateBean.setName(certName);
                certificateBean.setAddress(address);
                certificateBean.setStu_name(studentName);
                certificateBean.setIdCard(idCard);
                certificateBean.setRegistrationArea(oAddress);
                certificateBean.setNewPrice(price+"");
                otherList.add(certificateBean);
            }
            if (otherList.size() > 0) {
                newEamestOtherAdapter.setData(otherList);
                reOther.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    @OnClick({R.id.iv_back, R.id.re_wangke, R.id.re_xueli, R.id.re_xie, R.id.re_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.re_wangke:
                if (wangZhan) {
                    wangZhan = false;
                    clientRecyclerview.setVisibility(View.GONE);
                } else {
                    clientRecyclerview.setVisibility(View.VISIBLE);
                    wangZhan = true;
                }
                break;
            case R.id.re_xueli:
                if (xueZhan) {
                    xueZhan = false;
                    xueRecyclerview.setVisibility(View.GONE);
                } else {
                    xueRecyclerview.setVisibility(View.VISIBLE);
                    xueZhan = true;
                }

                break;
            case R.id.re_xie:
                if (xieZhan) {
                    xieZhan = false;
                    xieRecyclerview.setVisibility(View.GONE);
                } else {
                    xieRecyclerview.setVisibility(View.VISIBLE);
                    xieZhan = true;
                }
                break;
            case R.id.re_other:
                if (otherZhan) {
                    otherZhan = false;
                    otherRecyclerview.setVisibility(View.GONE);
                } else {
                    otherRecyclerview.setVisibility(View.VISIBLE);
                    otherZhan = true;
                }
                break;
        }
    }
}
