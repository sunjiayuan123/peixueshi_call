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
import com.mf.library.utils.ToastUtils;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.ProjectBean;
import com.peixueshi.crm.ui.adapter.PopLeftRecycleView;
import com.peixueshi.crm.ui.newadapter.NewXueliRightRecycleView;
import com.peixueshi.crm.ui.newadapter.PopXueliLeftRecycleView;
import com.peixueshi.crm.ui.newadapter.ProLeftRecycleView;
import com.peixueshi.crm.ui.newadapter.ProRightRecycleView;
import com.peixueshi.crm.utils.JSONUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class NewNetWorkClassActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.hand_text)
    TextView handText;
    @BindView(R.id.rl_title_view)
    RelativeLayout rlTitleView;
    @BindView(R.id.tv_xiangmu)
    TextView tvXiangmu;
    @BindView(R.id.tv_banxing)
    TextView tvBanxing;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.rv_left)
    RecyclerView rvLeft;
    @BindView(R.id.rv_right)
    RecyclerView rvRight;
    @BindView(R.id.pop_layout)
    LinearLayout popLayout;
    @BindView(R.id.wang_re)
    RelativeLayout wangRe;
    @BindView(R.id.xue_re)
    RelativeLayout xueRe;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new_net_work_class;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //tab颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", 0);
        if (type == 4) {//网课
            wangRe.setVisibility(View.VISIBLE);
            xueRe.setVisibility(View.GONE);
            initDataWangke();
        } else {//学历
            wangRe.setVisibility(View.GONE);
            xueRe.setVisibility(View.VISIBLE);
            initDataXueLi();
        }

        // ProLeftRecycleView proLeftRecycleView=new ProLeftRecycleView(this,)

    }

    private void initDataWangke() {
        try {
            String url = null;
            //url = Constants.host+"team/p_list";
            url = Constants.payhost;
            HashMap<String, String> keyMap = new HashMap<>();
            keyMap.put("page", "1");
            keyMap.put("pnum", "100");
            Log.e("tag", "initDataWangke: " + url + "kb_proj/p_list");
            OkHttpUtils.newGet(this, url + "dict/projectList", new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(NewNetWorkClassActivity.this, message, Toast.LENGTH_SHORT).show();
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

    private void initDataXueLi() {
        try {
            String url = null;
            //url = Constants.host+"team/p_list";
            url = Constants.payhost;
            HashMap<String, String> keyMap = new HashMap<>();
            keyMap.put("page", "1");
            keyMap.put("pnum", "100");
            Log.e("tag", "initDataWangke: " + url + "kb_proj/p_list");
            OkHttpUtils.newGet(this, url + "dict/school", new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(NewNetWorkClassActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject jsonObject) throws
                        Exception {
                    getXueLi(jsonObject);
                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void initDataXueLi(String sch_name) {
        try {
            String url = null;
            //url = Constants.host+"team/p_list";
            url = Constants.newhost;
           /* page: 1
            pid: 1001002
            pnum: 200
            state: 2
            typ: 1*/
            HashMap<String, String> keyMap = new HashMap<>();
            keyMap.put("page", "1");
            keyMap.put("pnum", "200");
            keyMap.put("sch_name", sch_name);
            //http://tserver.api.yuxuejiaoyu.net/periphery/kb_proj/pro_list
            //http://tserver.api.yuxuejiaoyu.net/periphery/kb_proj/pro_list
            Log.e("tag", "initDataWangke: " + url + "periphery/kb_proj/sch_list");
            OkHttpUtils.newPost(this, url + "periphery/kb_proj/sch_list", keyMap, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(NewNetWorkClassActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject jsonObject) throws
                        Exception {
                    getXueRightData(jsonObject);
                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void getXueLi(JSONObject jsonObject) {
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray list = data.getJSONArray("list");
            if (list != null) {
                List<Map<String, String>> userGet = JSONUtil.getListMap(list);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (userGet.size() > 0) {
                            String sch_name = userGet.get(0).get("name");
                            initDataXueLi(sch_name);
                        }
                        PopXueliLeftRecycleView proLeftRecycleView = new PopXueliLeftRecycleView(NewNetWorkClassActivity.this, userGet);
                        rvLeft.setLayoutManager(new LinearLayoutManager(NewNetWorkClassActivity.this));
                        rvLeft.setAdapter(proLeftRecycleView);
                        proLeftRecycleView.setOnItemClickListener(new PopXueliLeftRecycleView.OnItemClickListener() {
                            @Override
                            public void OnItemClickListener(int pos) {
                                proLeftRecycleView.refreshItem(pos);
                                String p_id = userGet.get(pos).get("name");
                                initDataXueLi(p_id);
                            }
                        });
                    }
                });

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getXueRightData(JSONObject jsonObject) {
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray list = data.getJSONArray("list");
            if (list != null) {
                List<Map<String, String>> userGet = JSONUtil.getListMap(list);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("tag", "run: " + userGet.toString());
                        NewXueliRightRecycleView proLeftRecycleView = new NewXueliRightRecycleView(NewNetWorkClassActivity.this, userGet);
                        rvRight.setLayoutManager(new LinearLayoutManager(NewNetWorkClassActivity.this));
                        rvRight.setAdapter(proLeftRecycleView);
                        proLeftRecycleView.setOnItemClickListener(new NewXueliRightRecycleView.OnItemClickListener() {
                            @Override
                            public void OnItemClickListener(int pos) {
                                Map<String, String> stringStringMap = userGet.get(pos);


                                if (NewSelectTypeActivity.xueList.size() > 0) {
                                    NewSelectTypeActivity.xueList.clear();
                                }
                                NewSelectTypeActivity.xueList.add(stringStringMap);
                                finish();

                            }
                        });
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("tag", "getUserInfo: " + jsonObject.toString());
    }

    private void initDataBanxing(int pid) {
        try {
            String url = null;
            //url = Constants.host+"team/p_list";
            url = Constants.newhost;
           /* page: 1
            pid: 1001002
            pnum: 200
            state: 2
            typ: 1*/
            HashMap<String, String> keyMap = new HashMap<>();
            keyMap.put("page", "1");
            keyMap.put("pnum", "200");
            keyMap.put("pid", pid + "");
            keyMap.put("state", "2");
            keyMap.put("typ", "1");
            //http://tserver.api.yuxuejiaoyu.net/periphery/kb_proj/pro_list
            //http://tserver.api.yuxuejiaoyu.net/periphery/kb_proj/pro_list
            Log.e("tag", "initDataWangke: " + url + "periphery/kb_proj/pro_list");
            OkHttpUtils.newPost(this, url + "periphery/kb_proj/pro_list", keyMap, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(NewNetWorkClassActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject jsonObject) throws
                        Exception {
                    getRightData(jsonObject);
                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void getUserInfo(JSONObject jsonObject) {
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray list = data.getJSONArray("list");
            if (list != null) {
                List<ProjectBean> userGet = JSONUtil.parserArrayList(list, ProjectBean.class, 0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (userGet.size() > 0) {
                            int p_id = userGet.get(0).getP_id();
                            initDataBanxing(p_id);
                        }
                        ProLeftRecycleView proLeftRecycleView = new ProLeftRecycleView(NewNetWorkClassActivity.this, userGet, 0);
                        rvLeft.setLayoutManager(new LinearLayoutManager(NewNetWorkClassActivity.this));
                        rvLeft.setAdapter(proLeftRecycleView);
                        proLeftRecycleView.setOnItemClickListener(new ProLeftRecycleView.OnItemClickListener() {
                            @Override
                            public void OnItemClickListener(int pos) {
                                proLeftRecycleView.refreshItem(pos);
                                int p_id = userGet.get(pos).getP_id();
                                initDataBanxing(p_id);
                            }
                        });
                    }
                });

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getRightData(JSONObject jsonObject) {
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray list = data.getJSONArray("list");
            if (list != null) {
                List<Map<String, String>> userGet = JSONUtil.getListMap(list);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("tag", "run: " + userGet.toString());
                        ProRightRecycleView proLeftRecycleView = new ProRightRecycleView(NewNetWorkClassActivity.this, userGet);
                        rvRight.setLayoutManager(new LinearLayoutManager(NewNetWorkClassActivity.this));
                        rvRight.setAdapter(proLeftRecycleView);
                        proLeftRecycleView.setOnItemClickListener(new PopLeftRecycleView.OnItemClickListener() {
                            @Override
                            public void OnItemClickListener(int pos) {
                                Map<String, String> stringStringMap1 = userGet.get(pos);

                                int selectWang = 0;
                                int size = NewAddOrderActivity.wangList.size();
                                if (size > 0) {
                                    List<Map<String, String>> list = NewAddOrderActivity.wangList;
                                    for (int i = 0; i < list.size(); i++) {
                                        Map<String, String> info = list.get(i);
                                        if (info.containsKey("class")) {
                                            String aClass = info.get("class");
                                            String userGetClass = stringStringMap1.get("class");
                                            com.alibaba.fastjson.JSONObject jsonObject1 = com.alibaba.fastjson.JSONObject.parseObject(aClass);
                                            com.alibaba.fastjson.JSONObject userGetClassJson = com.alibaba.fastjson.JSONObject.parseObject(userGetClass);
                                            try {
                                                int id = jsonObject1.getIntValue("id");
                                                int id1 = userGetClassJson.getIntValue("id");
                                                if (id1 == id) {
                                                    selectWang = id;
                                                }
                                            } catch (com.alibaba.fastjson.JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                if (selectWang == 0) {
                                    Map<String, String> stringStringMap = userGet.get(pos);
                                    NewAddOrderActivity.wangList.add(stringStringMap);
                                    finish();
                                } else {
                                    ToastUtils.showShort("该班型已保存");
                                }
                            }
                        });
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("tag", "getUserInfo: " + jsonObject.toString());
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }


    @OnClick({R.id.iv_back, R.id.hand_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.hand_text:
                break;
        }
    }

}
