package com.peixueshi.crm.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.view.PullDownView;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.ZiXunUserInfo;
import com.peixueshi.crm.ui.adapter.UserDataRankingAdapter;
import com.peixueshi.crm.ui.widget.LoadMoreListView;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OneFragmentRanking extends Fragment implements PullDownView.UpdateHandle,View.OnClickListener {

    public static final String TYPE = "type";
    private View parentView;


    LoadMoreListView recycle_view;

    public TextView tv_count_number;
    PullDownView pullDownView;

    private boolean isZhaosheng = true;
    private int day = 1;
    public OneFragmentRanking() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_one_ranking, container, false);
        initView();
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {


        isZhaosheng = getArguments().getBoolean("isZhaosheng", true);
        day = getArguments().getInt("day");

        recycle_view = (LoadMoreListView) parentView.findViewById(R.id.recycle_view);

        tv_count_number = (TextView) parentView.findViewById(R.id.tv_count_number);
        pullDownView =(PullDownView) parentView.findViewById(R.id.user_pull_to_refresh);
       // StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);


      /*  LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycle_view.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recycle_view.setLayoutManager(layoutManager);*/
        initData(isZhaosheng,day);
        setRefresh();
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
                initData(isZhaosheng,day);
            }
        });
    }









    private int page = 1;
    /**
     * 招生 、小组排行
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initData(boolean isZhaosheng, int day) {
        try {
            String url = null;
            if(isZhaosheng){
                url = Constants.host+"team/anren";
            }else{
                url = Constants.host+"team/anteam";
            }

                HashMap<String,String> keyMap = new HashMap<String,String>();
                if(isLoadMore){
                    keyMap.put("page",page+"");
                    page++;
                }else{
                    page = 1;
                    keyMap.put("page",1+"");
                }
                keyMap.put("count","10");
               /* keyMap.put("coll",0+"");
                keyMap.put("rule",1+"");*/


            long current = System.currentTimeMillis();
            /*long zero = current/(1000*3600*24*day)*(1000*3600*24*day) - TimeZone.getDefault().getRawOffset();*/

            keyMap.put("s_time", Util.getPastDateMills(day));
            keyMap.put("e_time",current/1000+"");

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


   public List<Map<String,String>> userInfos = new ArrayList<>();
    String total = "10";
    ZiXunUserInfo info;
    public UserDataRankingAdapter adapter;
    private ZiXunUserInfo getUserInfo(JSONObject job) throws Exception{
                    JSONArray jsonArray = job.getJSONArray("info");
                    if (jsonArray != null && jsonArray.length()>0) {
                        List<Map<String,String>> users =  JSONUtil.getListMap(jsonArray);
                        if(users != null && users.size()<10){
                            total = users.size()+"";
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isLoadMore) {
                                    countNumber = Integer.valueOf(total);
                                    tv_count_number.setText("共" + total + "个");
                                    userInfos.addAll(0, users);
                                    if (adapter == null) {
                                        adapter = new UserDataRankingAdapter(getActivity(), userInfos, isZhaosheng, OneFragmentRanking.this);
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
                                        adapter = new UserDataRankingAdapter(getActivity(), userInfos, isZhaosheng, OneFragmentRanking.this);
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
        return info;
    }


    public static OneFragmentRanking newInstance(String text, boolean isZhaosheng,int day) {
        OneFragmentRanking fragment = new OneFragmentRanking();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, text);
        bundle.putBoolean("isZhaosheng",isZhaosheng);
        bundle.putInt("day",day);
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
        initData(isZhaosheng,day);
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
}
