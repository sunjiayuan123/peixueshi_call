package com.peixueshi.crm.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.ui.adapter.PopCallHistoryAdapter;
import com.peixueshi.crm.utils.JSONUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class DetailBeizhuActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.rg_goutong)
    RadioGroup rg_goutong;
    @BindView(R.id.rg_yixiang)
    RadioGroup rg_yixiang;

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.rv_call_history)
    RecyclerView rv_call_history;




    @BindView(R.id.ll_call_history_include)
    LinearLayout ll_call_history_include;

    @BindView(R.id.ll_submit_beizhu)
    LinearLayout ll_submit_beizhu;
    @BindView(R.id.et_beizhu)
    EditText et_beizhu;

    @BindView(R.id.rb_01)
    RadioButton rb_01;
    @BindView(R.id.rb_02)
    RadioButton rb_02;
    @BindView(R.id.rb_03)
    RadioButton rb_03;
    @BindView(R.id.rb_04)
    RadioButton rb_04;

    @BindView(R.id.rb_yixiang_01)
    RadioButton rb_yixiang_01;
    @BindView(R.id.rb_yixiang_02)
    RadioButton rb_yixiang_02;
    @BindView(R.id.rb_yixiang_03)
    RadioButton rb_yixiang_03;

    List<Map<String, String>> selectKemu = new ArrayList<>();//网课



    private int yixiang_type = 0;
    private int goutong_type = 0;
    //接收子Fragment传递信息
    public void SendMessageValue(Map<String,String> mapInfo,int type) {
        //  ll_wangke_include.setVisibility(View.GONE);
        if(type == 0){
            selectKemu.add(mapInfo);
            ll_call_history_include.setVisibility(View.VISIBLE);
        }
    }


    //定义一个回调接口
    public interface CallBackValue{
        public void SendMessageValue(Map<String, String> mapInfo);
    }
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    PopCallHistoryAdapter popCallHistoryAdapter;
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.beizhu_activity;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String id = intent.getStringExtra("w_id");
        String w_phone = intent.getStringExtra("w_phone");
        boolean isShouZi = intent.getBooleanExtra("isShouZi",true);
        getCallHistory(w_phone);


        ll_submit_beizhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBeizhu(url,id,isShouZi,w_phone);
            }
        });


        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_call_history.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        rv_call_history.setLayoutManager(layoutManager1);


        rg_goutong.check(R.id.rb_01);
        rg_yixiang.check(R.id.rb_yixiang_01);
        rg_goutong.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == rb_01.getId()){
                    goutong_type = 0;//正常接通
                }else if(checkedId == rb_02.getId()){
                    goutong_type = 1;//1未接通
                }else if(checkedId == rb_03.getId()){
                    goutong_type = 2;//1关停机
                }else if(checkedId == rb_04.getId()){
                    goutong_type = 3;//空号
                }
            }
        });

        rg_yixiang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == rb_yixiang_01.getId()){
                    yixiang_type = 0;
                }else if(checkedId == rb_yixiang_02.getId()){
                    yixiang_type = 1;
                }else if(checkedId == rb_yixiang_03.getId()){
                    yixiang_type = 2;
                }

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void getCallHistory(String phone) {
        try {
            String  reqUrl = Constants.host+"work/n_list?phone="+phone+"&page=1&count=100&coll=0&rule=1";
            Log.e("tag", "getCallHistory: "+reqUrl );
            /*HashMap<String,String> map = new HashMap<>();
            map.put("phone",phone);
            map.put("coll","0");
            map.put("rule","1");
            map.put("page","1");
            map.put("count","100");*/
            OkHttpUtils.get(DetailBeizhuActivity.this, reqUrl,new OkhttpCallback()
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
                    getDetailHistory(object);
                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    private void getDetailHistory(JSONObject job) throws Exception{

        List<Map<String,String>> listInfos = JSONUtil.getListMap(job.getJSONObject("date").getJSONArray("list"));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText( getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
                if(listInfos != null && listInfos.size()>0){
                    //呼叫历史
                    if(popCallHistoryAdapter == null){
                        popCallHistoryAdapter = new PopCallHistoryAdapter(getApplicationContext(),listInfos);
                        rv_call_history.setAdapter(popCallHistoryAdapter);
                    }else{
                        popCallHistoryAdapter.setData(listInfos);
                        popCallHistoryAdapter.notifyDataSetChanged();
                    }

                }
            }
        });
    }

    private void setBeizhu(String url,String id,boolean isShouzi,String phone){
        if(isShouzi){
            String  reqUrl = Constants.host+url+"id="+id+"&note="+et_beizhu.getText().toString()+"&weight="+yixiang_type+"&state="+goutong_type;
            OkHttpUtils.get(DetailBeizhuActivity.this, reqUrl, new OkhttpCallback()
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
                public Object parseNetworkResponse(JSONObject jsonObject) throws
                        Exception {
                    getOrderInfo(jsonObject);
                    return null;
                }


            });
        }else{
            String  reqUrl = Constants.host+url;
            HashMap<String,String> map = new HashMap<>();
            if(!url.equals("work/addset")){
                map.put("id",id);
                map.put("note",et_beizhu.getText().toString());
                map.put("weight",yixiang_type+"");
                map.put("state",goutong_type+"");
            }else{//升班 未升班
                map.put("note",et_beizhu.getText().toString());
                map.put("weight",yixiang_type+"");
                map.put("state",goutong_type+"");
                map.put("phone",phone);
            }

            OkHttpUtils.post(DetailBeizhuActivity.this, reqUrl,map, new OkhttpCallback()
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
                public Object parseNetworkResponse(JSONObject job) throws
                        Exception {
                    getOrderInfo(job);
                    return null;
                }


            });
        }

    }


    private void getOrderInfo(JSONObject job) throws Exception{
        String err = job.getString("err");
        if (!TextUtils.isEmpty(err) && err.equals("0")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText( getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            default:
        }
    }
}
