package com.peixueshi.crm.activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.sina.weibo.view.PullDownView;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.ui.adapter.PopPhoneListenAdapter;
import com.peixueshi.crm.ui.widget.LoadMoreListView;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.PromptManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MinehandMasterActivity extends BaseActivity implements PullDownView.UpdateHandle{

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.rv_chance_history)
    LoadMoreListView recycle_view;



    @BindView(R.id.ll_chance_history_include)
    LinearLayout ll_chance_history_include;

    @BindView(R.id.et_search_phone)
    EditText et_search_phone;

    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.user_pull_to_refresh)
    PullDownView pullDownView;




    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    PopPhoneListenAdapter popPhoneHistoryAdapter;
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.mine_handmaster;
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
                    PromptManager.showMyToast("请输入手机号", MinehandMasterActivity.this);
                    return;
                }
                if(et_search_phone.getText().toString().length() != 11){
                    PromptManager.showMyToast("手机号不符合格式", MinehandMasterActivity.this);
                    return;
                }
                PromptManager.hideKeyboard(MinehandMasterActivity.this);
                listerList.clear();
                if(popPhoneHistoryAdapter != null){
                    popPhoneHistoryAdapter.setData(listerList);
                    popPhoneHistoryAdapter.notifyDataSetChanged();
                }

                initListerHistory(et_search_phone.getText().toString());
            }
        });

       /* LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_chance_history.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        rv_chance_history.setLayoutManager(layoutManager1);*/

        setRefresh();
        initListerHistory("");
    }


    private boolean isLoadMore = true;
    private void setRefresh() {

        pullDownView.initSkin();
        //  pullDownView.endUpdate(new Date());
        pullDownView.setUpdateHandle(this);
        pullDownView.setEnable(true);
        //pullDownView.update();



        recycle_view.setLoadMoreListener(new LoadMoreListView.LoadMoreListener() {
            @Override
            public void onLoadMore(int currentPage) {
                isLoadMore = true;
                initListerHistory("");
            }
        });
    }



    private int page = 1;
    private void initListerHistory(String phone) {
        try {
            String  reqUrl = Constants.host+"work/record_log";
            HashMap<String,String> keyMap = new HashMap<>();
            if(isLoadMore){
                keyMap.put("page",page+"");
                page++;
            }else{
                page = 1;
                keyMap.put("page",1+"");
            }

            keyMap.put("count","10");
            keyMap.put("rule","1");
            keyMap.put("coll","0");
            keyMap.put("phone",phone);
            OkHttpUtils.post(MinehandMasterActivity.this, reqUrl,keyMap,new OkhttpCallback()
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

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    List<Map<String,String>> listerList = new ArrayList<>();
    private void getPhoneHistory(JSONObject job) throws Exception{


                if(job.getJSONObject("date").getJSONArray("list").length()>0){
                    int total = job.getJSONObject("date").getInt("total");
                    List<Map<String,String>> listMap = JSONUtil.getListMap(job.getJSONObject("date").getJSONArray("list"));
                    listerList.addAll(listMap);//下拉刷新先清空
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(popPhoneHistoryAdapter == null){
                                popPhoneHistoryAdapter = new PopPhoneListenAdapter(MinehandMasterActivity.this, listerList);
                                recycle_view.setAdapter(popPhoneHistoryAdapter);
                            }else{
                                popPhoneHistoryAdapter.setData(listerList);
                                popPhoneHistoryAdapter.notifyDataSetChanged();
                            }
                            pullDownView.endUpdate(new Date());
                            if (listerList != null && listerList.size() > 0) {
                                if (listerList.size() < Integer.valueOf(total)) {
                                    recycle_view.onLoadMoreComplete(true);
                                } else {
                                    recycle_view.onLoadMoreComplete(false);
                                }
                            } else {
                                recycle_view.onLoadMoreComplete(false);
                            }
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PromptManager.showMyToast("暂无听课记录",MinehandMasterActivity.this);
                        }
                    });
                }


    }

    @Override
    public void onUpdate() {

        et_search_phone.setText("");
        isLoadMore = false;
        listerList.clear();
        page = 1;
        if(popPhoneHistoryAdapter != null){
            popPhoneHistoryAdapter.setData(listerList);
            popPhoneHistoryAdapter.notifyDataSetChanged();
        }
        initListerHistory("");
    }


}
