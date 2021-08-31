package com.peixueshi.crm.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.ui.adapter.PopPhoneZhongPuAdapter;
import com.peixueshi.crm.ui.widget.LoadMoreListView;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.PromptManager;
import com.sina.weibo.view.PullDownView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MineZhongHistoryActivity extends BaseActivity implements PullDownView.UpdateHandle {

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.rv_chance_history)
    LoadMoreListView recycle_view;


    @BindView(R.id.ll_chance_history_include)
    LinearLayout ll_chance_history_include;


    @BindView(R.id.user_pull_to_refresh)
    PullDownView pullDownView;
    private Activity activity;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    PopPhoneZhongPuAdapter popPhoneHistoryAdapter;

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
        activity = this;
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
        if (callPhone==null){
            callPhone="";
        }
        getCallHistory();
      /*  recycle_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+listerList.get(position).get("yx_t_phone")));
                startActivity(intent);//内部类
            }
        });*/
    }


    private boolean isLoadMore = false;

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
                getCallHistory();
            }
        });
    }


    /**
     * 获取token
     */
    private void getRealToken() {
        try {

            String emp_name = EnjoyPreference.readString(MineZhongHistoryActivity.this, "emp_name");//名
            String emp_team_id = EnjoyPreference.readString(MineZhongHistoryActivity.this, "emp_team_id");//组id
            String emp_id = EnjoyPreference.readString(MineZhongHistoryActivity.this, "emp_id");//坐席id
            String url = null;
            url = Constants.host + "team/work_d?uid=" + emp_id + "&uname=" + emp_name + "&gid=" + emp_team_id;//+ emp_team_id
            Log.e("tag", "getRealToken: " + url);
            OkHttpUtils.get(MineZhongHistoryActivity.this, url, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Log.e("tag", "onFailure:4 " + message);
                    Toast.makeText(MineZhongHistoryActivity.this, message,
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
                                EnjoyPreference.saveString(activity, "jwtoken", data);
                                Constants.jwtToken = data;
                                getCallHistory();
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

    private int page = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initCallHistory() {
        try {

            String reqUrl;
            if (isLoadMore) {
                page++;
                reqUrl = Constants.chuXin + "/token/voide_list" + "?page=" + page+"&targe="+callPhone;
            } else {
                page = 1;
                reqUrl = Constants.chuXin + "/token/voide_list" + "?page=1"+"&targe="+callPhone;
            }
            String emp_name = EnjoyPreference.readString(MineZhongHistoryActivity.this, "emp_name");//名
            reqUrl = reqUrl + "&name=" + emp_name + "&count=" + 10;

         /*   long current = System.currentTimeMillis();
            keyMap.put("s_time", Util.getPastDateMills(day));
            keyMap.put("e_time",current/1000+"");*/
            OkHttpUtils.get(MineZhongHistoryActivity.this, reqUrl, new OkhttpCallback() {
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
    public void getCallHistory() {
        String reqUrl;
        if (isLoadMore) {
            page++;
            reqUrl = Constants.chuXin + "/token/voide_list" + "?page=" + page+"&targe="+callPhone;
        } else {
            page = 1;
            reqUrl = Constants.chuXin + "/token/voide_list" + "?page=1"+"&targe="+callPhone;
        }
        String emp_name = EnjoyPreference.readString(MineZhongHistoryActivity.this, "emp_name");//名
        reqUrl = reqUrl + "&name=" + emp_name + "&count=" + 10;
        Log.e("tag", "getCallHistory: "+reqUrl );
        Log.e("tag", "getCallHistory: "+Constants.jwtToken );
        String jwtoken = EnjoyPreference.readString(activity, "jwtoken");
        Constants.jwtToken = jwtoken;
        // getXphone(emp_id,"");
        okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient();
        // {"code":1,"msg":"绑定关系失败","data":"错误码: 1005, 请查寻中普ACB对接文档"}
        okhttp3.Request request = new okhttp3.Request.Builder()
                .get()
                .addHeader("Authorization",Constants.jwtToken)//Constants.jwtToken
                .url(reqUrl)
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
                                try {
                                    getPhoneHistory(object);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else if (reult==-1){
                                getRealToken();
                            }else if (reult==-2){
                                getRealToken();
                            }else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
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


    List<Map<String, String>> listerList = new ArrayList<>();

    private void getPhoneHistory(JSONObject job) throws Exception {
        if (job.getInt("code") == 0) {
            if (job.getJSONObject("data").getJSONArray("list").length() > 0) {
                int total = job.getJSONObject("data").getInt("count");
                List<Map<String, String>> listMap = JSONUtil.getListMap(job.getJSONObject("data").getJSONArray("list"));
                listerList.addAll(listMap);//下拉刷新先清空
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (popPhoneHistoryAdapter == null) {
                            popPhoneHistoryAdapter = new PopPhoneZhongPuAdapter(MineZhongHistoryActivity.this, listerList);
                            recycle_view.setAdapter(popPhoneHistoryAdapter);
                        } else {
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
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PromptManager.showMyToast("暂无呼叫记录", MineZhongHistoryActivity.this);
                    }
                });
            }
        }else {
            getRealToken();
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onUpdate() {

        isLoadMore = false;
        listerList.clear();
        page = 1;
        if (popPhoneHistoryAdapter != null) {
            popPhoneHistoryAdapter.setData(listerList);
            popPhoneHistoryAdapter.notifyDataSetChanged();
        }
        getCallHistory();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
