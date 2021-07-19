package com.peixueshi.crm.activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.ui.adapter.PopPhoneHistoryAdapter;
import com.peixueshi.crm.ui.adapter.PopXiangmuDropAdapter;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.PromptManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MineCreateChanceActivity extends BaseActivity{

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.rv_chance_history)
    RecyclerView rv_chance_history;

    @BindView(R.id.rl_xiangmu)
    RelativeLayout rl_xiangmu;
    @BindView(R.id.rl_beizhu)
    RelativeLayout rl_beizhu;

    @BindView(R.id.ll_chance_history_include)
    LinearLayout ll_chance_history_include;

    @BindView(R.id.et_search_phone)
    EditText et_search_phone;

    @BindView(R.id.et_xiangmu)
    EditText et_xiangmu;
    @BindView(R.id.et_beizhu)
    EditText et_beizhu;

    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.tv_create_chance)
    TextView tv_create_chance;

    @BindView(R.id.tv_user_info)
    TextView tv_user_info;
    @BindView(R.id.rl_renling)
    RelativeLayout rl_renling;




    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    PopPhoneHistoryAdapter popPhoneHistoryAdapter;
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.mine_create_chance;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_search_phone.getText().toString().length() == 0 ){
                    PromptManager.showMyToast("请输入手机号",MineCreateChanceActivity.this);
                    return;
                }
                if(et_search_phone.getText().toString().length() != 11){
                    PromptManager.showMyToast("手机号不符合不符合格式",MineCreateChanceActivity.this);
                    return;
                }
                PromptManager.hideKeyboard(MineCreateChanceActivity.this);
                getPhoneInfo(et_search_phone.getText().toString());
            }
        });


        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_chance_history.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        rv_chance_history.setLayoutManager(layoutManager1);


        et_xiangmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listXiangmu != null && listXiangmu.size()>0){
                    showDropView();
                }else{
                    initXiangmu();
                }
            }
        });


        tv_create_chance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_search_phone.getText().toString().length() == 0 ){
                    Toast.makeText(MineCreateChanceActivity.this,"请输入手机号",Toast.LENGTH_SHORT);
                    return;
                }
                if(et_search_phone.getText().toString().length() != 11){
                    Toast.makeText(MineCreateChanceActivity.this,"手机号不符合不符合格式",Toast.LENGTH_SHORT);
                    return;
                }
                if(et_xiangmu.getText().toString().length()<=0){
                    Toast.makeText(MineCreateChanceActivity.this,"请选择项目",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(listXiangmu != null && listXiangmu.get(clickPos) != null ){
                    String pid = null;
                    if(listXiangmu.get(clickPos).containsKey("p_id")){
                        pid = listXiangmu.get(clickPos).get("p_id");
                    }else{
                        pid =listXiangmu.get(clickPos).get("pr_id");
                    }
                    Log.e("tag", "onClick: "+pid );
                    createChance(et_search_phone.getText().toString(),et_xiangmu.getText().toString(),et_beizhu.getText().toString(),pid);
                }
            }
        });

    }




    private void initXiangmu() {
            try {
                String url = null;
                url = Constants.b;
                HashMap<String,String> keyMap = new HashMap<>();
               /* if(Constants.isWifiLimit){
                    keyMap.put("page","1");
                    keyMap.put("count","100");
                    keyMap.put("id","1");
                }else{*/
                    keyMap.put("page","1");
                    keyMap.put("count","100");
                    keyMap.put("id","0");
//                }

                OkHttpUtils.post(MineCreateChanceActivity.this, url, keyMap,new OkhttpCallback()
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
                        getUserInfo(jsonObject);
                        return null;
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
    }

    PopXiangmuDropAdapter popXiangmuDropAdapter;
    List<Map<String,String>> listXiangmu;
    private void getUserInfo(JSONObject job) throws Exception{
        JSONArray jsonArray = null;
        if(job.has("list")){
            jsonArray = job.getJSONArray("list");
        }else{
            jsonArray = job.getJSONObject("date").getJSONArray("list");
        }
                if (jsonArray != null) {
                    listXiangmu = JSONUtil.getListMap(jsonArray);
                    if(listXiangmu.size() == 0){
                        PromptManager.showMyToast("暂无项目",MineCreateChanceActivity.this);
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
                    runOnUiThread(new Runnable() {
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

    private int clickPos = 0;
    private void showDropView() {
        View view = View.inflate(getApplicationContext(),R.layout.ll_xiangmu_drop,null);
        RecyclerView recyclerView = view.findViewById(R.id.rv_drop);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager1);
        if(popXiangmuDropAdapter == null){
            popXiangmuDropAdapter = new PopXiangmuDropAdapter(MineCreateChanceActivity.this, listXiangmu);
            recyclerView.setAdapter(popXiangmuDropAdapter);
        }else{
            popXiangmuDropAdapter.setData(listXiangmu);
            recyclerView.setAdapter(popXiangmuDropAdapter);
            popXiangmuDropAdapter.notifyDataSetChanged();
        }
        popXiangmuDropAdapter.setOnItemClickListener(new PopXiangmuDropAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int pos) {
                String name;
                if(listXiangmu.get(pos).containsKey("p_name")){
                    name =  listXiangmu.get(pos).get("p_name");
                }else{
                    name =  listXiangmu.get(pos).get("pr_name");
                }

              clickPos = pos;
              et_xiangmu.setText(name);
              PromptManager.closePopWindow();
            }
        });
        PromptManager.showPopView(view,et_xiangmu,MineCreateChanceActivity.this);
    }


    private void getPhoneInfo(String phone){
            String  reqUrl = Constants.host+"work/p_get?phone="+phone;
            OkHttpUtils.get(MineCreateChanceActivity.this, reqUrl,new OkhttpCallback()
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
                    getPhoneHistory(jsonObject);
                    return null;
                }


            });

    }


    private void getPhoneHistory(JSONObject job) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int fage = 0;
                        try {
                            fage = job.getInt("fage");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //0 无， 1 库存， 2 首资， 3 班主任
                        if(fage == 0){//无库存,弹框

                            View view = View.inflate(getApplicationContext(),R.layout.create_chance_dialog,null);
                            view.findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rl_xiangmu.setVisibility(View.VISIBLE);
                                    rl_beizhu.setVisibility(View.VISIBLE);
                                    ll_chance_history_include.setVisibility(View.GONE);
                                    rl_renling.setVisibility(View.GONE);
                                    PromptManager.closeCustomDialog();
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
                            PromptManager.showCustomDialog(MineCreateChanceActivity.this,view,Gravity.CENTER,Gravity.CENTER);
                        }else{
                            Map<String,String> userInfo = null;
                            try {
                                userInfo = JSONUtil.getMap(job.getJSONObject("user"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            rl_xiangmu.setVisibility(View.GONE);
                            rl_beizhu.setVisibility(View.GONE);
                            ll_chance_history_include.setVisibility(View.VISIBLE);//解析展示

                            if(userInfo.get("emp_name").equals("")){//无人认领状态
                                rl_renling.setVisibility(View.VISIBLE);
                                rl_renling.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            getChance(job.getInt("fage")+"",et_search_phone.getText().toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }

                            List<Map<String,String>> lists = new ArrayList<>();
                            lists.add(userInfo);
                            if(popPhoneHistoryAdapter == null){
                                popPhoneHistoryAdapter = new PopPhoneHistoryAdapter(MineCreateChanceActivity.this, lists);
                                rv_chance_history.setAdapter(popPhoneHistoryAdapter);
                            }else{
                                popPhoneHistoryAdapter.setData(lists);
                                popPhoneHistoryAdapter.notifyDataSetChanged();
                            }
                        }

                        String text = "";
                        if(fage == 1){
                            text = "库存";
                        }else if(fage == 2){
                            text = "首咨";
                        }else if (fage == 3){
                            text = "班主任";
                        }
                        if(fage != 0){
                            tv_user_info.setText(et_search_phone.getText().toString()+"已存在在"+text+"列表中");
                        }
                    }
                });

    }


    private void getChance(String fage,String phone) {
        try {
            String  reqUrl = Constants.host+"work/p_ask?fage="+fage+"&phone="+phone;

            OkHttpUtils.get(MineCreateChanceActivity.this, reqUrl, new OkhttpCallback()
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
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               PromptManager.showMyToast("认领成功",MineCreateChanceActivity.this);//
                               rl_renling.setVisibility(View.GONE);
                               getPhoneInfo(et_search_phone.getText().toString());//刷新列表
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

    /**
     * 创建机会
     * @param phone
     * @param xiangmu
     * @param beizhu
     * @param pr_id
     */
    private void createChance(String phone, String xiangmu, String beizhu, String pr_id) {
            try {
                    String  reqUrl = Constants.host+"work/p_create?phone="+phone+"&pid="+pr_id+"&pname="+xiangmu+"&beizhu="+beizhu;
                    Log.e("tag", "createChance: "+reqUrl );
                    OkHttpUtils.get(MineCreateChanceActivity.this, reqUrl, new OkhttpCallback()
                    {
                        @Override
                        public void onBefore() {
                            super.onBefore();
                        }

                        @Override
                        public void onFailure(String message) {
                            Log.e("tag", "onFailure: "+message );
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
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
    }


    private void getOrderInfo(JSONObject job) {


               //刷新列表
                getPhoneInfo(et_search_phone.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        et_beizhu.setText("");
                        et_beizhu.setHint("备注信息");
                        et_xiangmu.setText("");
                        et_xiangmu.setText("请选择项目");
                        clickPos = 0;
                    }
                });
    }

}
