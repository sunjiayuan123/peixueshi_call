package com.peixueshi.crm.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.view.PullDownView;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.OrderDetail;
import com.peixueshi.crm.bean.OrderUserInfo;
import com.peixueshi.crm.bean.ZiXunUserInfo;
import com.peixueshi.crm.ui.adapter.UserDataOrderAdapter;
import com.peixueshi.crm.ui.widget.ExpandableListView;
import com.peixueshi.crm.ui.widget.LoadMoreListViewExpand;
import com.peixueshi.crm.utils.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class OneFragmentOrder extends Fragment implements PullDownView.UpdateHandle,View.OnClickListener {

    public static final String TYPE = "type";
    private View parentView;

    TextView txt_content;
    ExpandableListView recycle_view;
    PullDownView pullDownView;

    private boolean isShouZi = true;
    public OneFragmentOrder() {
        // Required empty public constructor
    }

    LinearLayout ll_refresh;
    TextView tv_refresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_one_order, container, false);
        initView();
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {

        txt_content = (TextView) parentView.findViewById(R.id.txt_content);
        if(getArguments() != null && getArguments().containsKey(TYPE)){
            txt_content.setText(getArguments().getString(TYPE, "Default"));
        }
        if(getArguments() != null){
            isShouZi = getArguments().getBoolean("isShouZi", true);
        }


        recycle_view = (ExpandableListView) parentView.findViewById(R.id.recycle_view);
        pullDownView =(PullDownView) parentView.findViewById(R.id.user_pull_to_refresh);

        ll_refresh =  parentView.findViewById(R.id.ll_refresh);
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
                initData(isShouZi);
            }
        });
        tv_refresh = parentView.findViewById(R.id.tv_refresh);
        initData(isShouZi);
        setRefresh();

        recycle_view.setOnGroupClickListener(new android.widget.ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(android.widget.ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

    }


    private boolean isLoadMore = true;
    private void setRefresh() {

        pullDownView.initSkin();
      //  pullDownView.endUpdate(new Date());
        pullDownView.setUpdateHandle(this);
        pullDownView.setEnable(true);
        //pullDownView.update();



        recycle_view.setLoadMoreListener(new LoadMoreListViewExpand.LoadMoreListener() {
            @Override
            public void onLoadMore(int currentPage) {
                isLoadMore = true;
                initData(isShouZi);
            }
        });
    }


    private int page = 1;
    /**
     * ????????????
     */
    private void initData(boolean isShouZi) {
        try {
            String url = null;
                url = Constants.host+"order/o_list";
                HashMap<String,String> keyMap = new HashMap<String,String>();
                keyMap.put("count","10");
                if(isLoadMore){
                    keyMap.put("page",page+"");
                    page++;
                }else{//??????
                    keyMap.put("page",1+"");
                }

                keyMap.put("coll",0+"");
                keyMap.put("rule",1+"");
               if(isShouZi){
                    keyMap.put("state",0+"");
                }else{
                    keyMap.put("state",1+"");
                }


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


    List<OrderUserInfo> userInfos = new ArrayList<OrderUserInfo>();
    List<OrderDetail> orders = new ArrayList<>();
    ZiXunUserInfo info;
    UserDataOrderAdapter adapter;
    private ZiXunUserInfo getUserInfo(JSONObject job) throws Exception{
                    JSONObject object = job.getJSONObject("date");
                    String total = object.getString("total");

                    JSONArray jsonArray = object.getJSONArray("list");
                    if (jsonArray != null) {
                        List<OrderUserInfo> userGet =  JSONUtil.parserArrayList(jsonArray, OrderUserInfo.class,0);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!isLoadMore){
                                    userInfos.addAll(0,userGet);
                                    if(adapter == null){
                                        adapter = new UserDataOrderAdapter(getActivity(),userInfos,isShouZi,recycle_view);
                                        recycle_view.setAdapter(adapter);
                                        recycle_view.setGroupIndicator(null);
                                    }
                                    adapter.setData(userInfos);
                                    adapter.notifyDataSetChanged();
                                    pullDownView.endUpdate(new Date());
                                    if (userInfos != null && userInfos.size()>0) {
                                        if (userInfos.size()<Integer.valueOf(total)) {
                                            recycle_view.onLoadMoreComplete(true);
                                        } else {
                                            recycle_view.onLoadMoreComplete(false);
                                        }
                                    } else {
                                        recycle_view.onLoadMoreComplete(false);
                                    }
                                }else{
                                    userInfos.addAll(userGet);
                                    if(adapter == null){
                                        adapter = new UserDataOrderAdapter(getActivity(),userInfos,isShouZi,recycle_view);
                                        recycle_view.setAdapter(adapter);
                                        recycle_view.setGroupIndicator(null);
                                    }
                                    adapter.setData(userInfos);
                                    adapter.notifyDataSetChanged();
                                    pullDownView.endUpdate(new Date());
                                    if (userInfos != null && userInfos.size()>0) {
                                        if (userInfos.size()<Integer.valueOf(total)) {
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

                        }else{
                            ll_refresh.setVisibility(View.GONE);
                        }
                    }
                });

        return info;
    }


    public static OneFragmentOrder newInstance(String text, boolean isShouzi) {
        OneFragmentOrder fragment = new OneFragmentOrder();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, text);
        bundle.putBoolean("isShouZi",isShouzi);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_get_zixun:

                break;
        }
    }




    @Override
    public void onUpdate() {
        isLoadMore = false;
        userInfos.clear();
        page = 1;//????????????
        if(adapter != null){
            adapter.setData(userInfos);
            adapter.notifyDataSetChanged();
        }
        initData(isShouZi);
       // pullDownView.endUpdate(new Date());
    }
}
