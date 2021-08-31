package com.peixueshi.crm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.view.PullDownView;
import com.peixueshi.crm.R;
import com.peixueshi.crm.activity.MineCallHistoryActivity;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.StudentUserInfo;
import com.peixueshi.crm.bean.ZiXunUserInfo;
import com.peixueshi.crm.ui.adapter.UserDataHandMasterListAdapter;
import com.peixueshi.crm.ui.widget.LoadMoreListView;
import com.peixueshi.crm.utils.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class OneFragmentHandMaster extends Fragment implements PullDownView.UpdateHandle,View.OnClickListener {

    public static final String TYPE = "type";
    private View parentView;


    LoadMoreListView recycle_view;

    public TextView tv_count_number;
    PullDownView pullDownView;
    LinearLayout ll_refresh;
    TextView tv_refresh;

    private boolean isShengji = false;
    public OneFragmentHandMaster() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_one_handmaster, container, false);
        initView();
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {


        isShengji = getArguments().getBoolean("isShengji", false);

        recycle_view = (LoadMoreListView) parentView.findViewById(R.id.recycle_view);
        ll_refresh =  parentView.findViewById(R.id.ll_refresh);
        tv_refresh = parentView.findViewById(R.id.tv_refresh);
        tv_count_number = (TextView) parentView.findViewById(R.id.tv_count_number);
        pullDownView =(PullDownView) parentView.findViewById(R.id.user_pull_to_refresh);
       // StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);


      /*  LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycle_view.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recycle_view.setLayoutManager(layoutManager);*/
      recycle_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           /*   String phone = userInfos.get(position).getCstu_phone();
              Intent intent = new Intent(getActivity(), MineCallHistoryActivity.class);
              intent.putExtra("phone",phone);
              getActivity().startActivity(intent);*/
          }
      });
        initData(isShengji,"");
        setRefresh();
    }


    private boolean isLoadMore = false;
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
                initData(isShengji,"");
            }
        });
    }









    private int page = 1;
    /**
     * 获取首咨或者库存
     */
    private void initData(boolean isShegnji,String phone) {
        try {
            String url = null;
                url = Constants.host+"user/studen_list";
                HashMap<String,String> keyMap = new HashMap<String,String>();
                keyMap.put("count","10");
                if(isLoadMore){
                    page++;
                    keyMap.put("page",page+"");
                }else{
                    page = 1;
                    keyMap.put("page",1+"");
                }

                keyMap.put("coll",0+"");
                keyMap.put("rule",1+"");
                if(!isShegnji){
                    keyMap.put("senior","0");
                }else{
                    keyMap.put("senior","1");
                }


                keyMap.put("amount","0");
                keyMap.put("phone",phone);
                keyMap.put("anme","");
                keyMap.put("fage","");
                keyMap.put("s_at","");
                keyMap.put("e_at","");

                OkHttpUtils.post(getActivity(), url, keyMap,new OkhttpCallback()
                {
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
                        getUserInfo(jsonObject);
                        return null;
                    }
                });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


   public List<StudentUserInfo> userInfos = new ArrayList<>();
    ZiXunUserInfo info;
    public UserDataHandMasterListAdapter adapter;
    private ZiXunUserInfo getUserInfo(JSONObject job) throws Exception{

                    JSONObject object = job.getJSONObject("date");
                    String total = object.getString("total");
                    JSONArray jsonArray = object.getJSONArray("list");
                    if (jsonArray != null && jsonArray.length()>0) {
                        List<StudentUserInfo> users =  JSONUtil.parserArrayList(jsonArray, StudentUserInfo.class,0);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isLoadMore) {
                                    countNumber = Integer.valueOf(total);
                                    tv_count_number.setText("共" + total + "个");
                                    userInfos.addAll(0, users);
                                    if (adapter == null) {
                                        adapter = new UserDataHandMasterListAdapter(getActivity(), userInfos, isShengji, OneFragmentHandMaster.this);
                                        recycle_view.setAdapter(adapter);
                                    }
                                    adapter.setData(userInfos);
                                    adapter.notifyDataSetChanged();
                                    pullDownView.endUpdate(new Date());
                                    if (userInfos != null && userInfos.size() > 0) {
                                        if (userInfos.size() < Integer.valueOf(total)) {
                                            recycle_view.onLoadMoreComplete(true);
                                        } else {
                                            recycle_view.onLoadMoreComplete(false);
                                        }
                                    } else {
                                        recycle_view.onLoadMoreComplete(false);
                                    }
                                } else {
                                    countNumber = Integer.valueOf(total);
                                    tv_count_number.setText("共" + total + "个");
                                    userInfos.addAll(users);
                                    if (adapter == null) {
                                        adapter = new UserDataHandMasterListAdapter(getActivity(), userInfos, isShengji, OneFragmentHandMaster.this);
                                        recycle_view.setAdapter(adapter);
                                    }
                                    adapter.setData(userInfos);
                                    adapter.notifyDataSetChanged();
                                    pullDownView.endUpdate(new Date());

                                    if (userInfos != null && userInfos.size() > 0) {
                                        if (userInfos.size() < Integer.valueOf(total)) {
                                            recycle_view.onLoadMoreComplete(true);
                                        } else {
                                            recycle_view.onLoadMoreComplete(false);
                                        }
                                    } else {
                                        recycle_view.onLoadMoreComplete(false);
                                    }
                                }
                            }
                        });
                    }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(userInfos.size() == 0){
                            ll_refresh.setVisibility(View.VISIBLE);
                            ll_refresh.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    isLoadMore = false;
                                    userInfos.clear();
                                    page = 1;
                                    if(adapter != null){
                                        adapter.setData(userInfos);
                                        adapter.notifyDataSetChanged();
                                    }
                                    initData(isShengji,"");
                                }
                            });
                        }else{
                            ll_refresh.setVisibility(View.GONE);
                        }
                    }
                });

        return info;
    }


    public static OneFragmentHandMaster newInstance(String text, boolean isShengji) {
        OneFragmentHandMaster fragment = new OneFragmentHandMaster();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, text);
        bundle.putBoolean("isShengji",isShengji);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_get_zixun:
               // isLoadMore = false;
                // getTenCount();
                break;
        }
    }

    public int countNumber = 0;
    @Override
    public void onUpdate() {

        isLoadMore = false;
        userInfos.clear();
        page = 1;
        if(adapter != null){
            adapter.setData(userInfos);
            adapter.notifyDataSetChanged();
        }
        initData(isShengji,"");
       // pullDownView.endUpdate(new Date());
       /* AsyncTask task=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                pullDownView.endUpdate(new Date());
            }


        }.execute();*/
    }

    public void searchInfo(String phone) {

        isLoadMore = false;
        userInfos.clear();
        page = 1;
        if (adapter != null) {
            adapter.setData(userInfos);
            adapter.notifyDataSetChanged();
        }
        initData(isShengji,phone);
    }

}
