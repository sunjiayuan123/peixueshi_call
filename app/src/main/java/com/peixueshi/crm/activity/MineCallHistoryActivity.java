package com.peixueshi.crm.activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.peixueshi.crm.ui.adapter.PopPhoneCallAdapter;
import com.peixueshi.crm.ui.widget.LoadMoreListView;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.PromptManager;
import com.peixueshi.crm.utils.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MineCallHistoryActivity extends BaseActivity implements PullDownView.UpdateHandle{

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.rv_chance_history)
    LoadMoreListView recycle_view;



    @BindView(R.id.ll_chance_history_include)
    LinearLayout ll_chance_history_include;



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

    PopPhoneCallAdapter popPhoneHistoryAdapter;
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.mine_call_history;
    }

    private String callPhone = "";
    @RequiresApi(api = Build.VERSION_CODES.O)
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



       /* LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_chance_history.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        rv_chance_history.setLayoutManager(layoutManager1);*/

        setRefresh();
        callPhone = getIntent().getStringExtra("phone");
        initCallHistory(30,callPhone);
      /*  recycle_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+listerList.get(position).get("yx_t_phone")));
                startActivity(intent);//内部类
            }
        });*/
    }


    private boolean isLoadMore = true;
    private void setRefresh() {

        pullDownView.initSkin();
        //  pullDownView.endUpdate(new Date());
        pullDownView.setUpdateHandle(this);
        pullDownView.setEnable(true);
        //pullDownView.update();



        recycle_view.setLoadMoreListener(new LoadMoreListView.LoadMoreListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onLoadMore(int currentPage) {
                isLoadMore = true;
                initCallHistory(30,callPhone);
            }
        });
    }



    private int page = 1;
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initCallHistory(int day, String phone) {
        try {
            HashMap<String,String> keyMap = new HashMap<>();
            String  reqUrl;
            if(!TextUtils.isEmpty(phone) && phone.length() >0){
                reqUrl = Constants.host+"work/findcall";
                keyMap.put("t_phone",phone);
            }else{
                reqUrl = Constants.host+"work/own_call";
            }
            Log.e("tag", "initCallHistory: "+reqUrl );
            if(isLoadMore){
                keyMap.put("page",page+"");
                page++;
            }else{
                page = 1;
                keyMap.put("page",1+"");
            }

            keyMap.put("count","15");
            keyMap.put("rule","0");
            keyMap.put("coll","0");



            long current = System.currentTimeMillis();
            keyMap.put("s_time", Util.getPastDateMills(day));
            keyMap.put("e_time",current/1000+"");
            OkHttpUtils.post(MineCallHistoryActivity.this, reqUrl,keyMap,new OkhttpCallback()
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
                                popPhoneHistoryAdapter = new PopPhoneCallAdapter(MineCallHistoryActivity.this, listerList);
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
                            PromptManager.showMyToast("暂无呼叫记录", MineCallHistoryActivity.this);
                        }
                    });
                }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onUpdate() {

        isLoadMore = false;
        listerList.clear();
        page = 1;
        if(popPhoneHistoryAdapter != null){
            popPhoneHistoryAdapter.setData(listerList);
            popPhoneHistoryAdapter.notifyDataSetChanged();
        }
        initCallHistory(30,callPhone);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
