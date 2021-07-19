package com.peixueshi.crm.newfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.peixueshi.crm.bean.NewOrderUserInfo;
import com.peixueshi.crm.bean.OrderDetail;
import com.peixueshi.crm.bean.ZiXunUserInfo;
import com.peixueshi.crm.ui.newadapter.NewUserDataOrderAdapter;
import com.peixueshi.crm.ui.widget.ExpandableListView;
import com.peixueshi.crm.ui.widget.LoadMoreListViewExpand;
import com.peixueshi.crm.utils.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NewOneFragmentOrder extends Fragment implements PullDownView.UpdateHandle, View.OnClickListener {

    public static final String TYPE = "type";
    private View parentView;

    TextView txt_content;
    ExpandableListView recycle_view;
    PullDownView pullDownView;

    private boolean isShouZi = true;
    private int state;
    private String endUrl;
    private int payTyp;
    private String phone="";

    public NewOneFragmentOrder() {
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
        if (getArguments() != null && getArguments().containsKey(TYPE)) {
            txt_content.setText(getArguments().getString(TYPE, "Default"));
        }
        if (getArguments() != null) {
            isShouZi = getArguments().getBoolean("isShouZi", true);
            state = getArguments().getInt("status", 1);
        }


        recycle_view = (ExpandableListView) parentView.findViewById(R.id.recycle_view);
        pullDownView = (PullDownView) parentView.findViewById(R.id.user_pull_to_refresh);

        ll_refresh = parentView.findViewById(R.id.ll_refresh);
        ll_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoadMore = false;
                userInfos.clear();
                page = 1;
                if (adapter != null) {
                    adapter.setData(userInfos);
                    adapter.notifyDataSetChanged();
                }
                initData(phone,payTyp);
            }
        });
        tv_refresh = parentView.findViewById(R.id.tv_refresh);
        initData(phone,payTyp);
        setRefresh();

        recycle_view.setOnGroupClickListener(new android.widget.ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(android.widget.ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

    }


    private boolean isLoadMore = false;

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
                initData(phone,payTyp);
            }
        });
    }


    private int page = 1;

    /**
     * 获取订单
     */
    private void initData(String phone,int payTyp) {
        try {
            String url = null;
            url = Constants.allhost + "comm/orderBList";
            if (isLoadMore) {
                page++;
            } else {//刷新
                page=1;
            }
            /*csId:
            orderId:
            phone:
            state: 1
            page: 1
            pnum: 10*/
            if (payTyp!=0){
                endUrl = "?csId=&orderId=&phone="+phone+"&state=" + state +"&states="+state+ "&page=" + page + "&pnum=10"+"&payTyp="+payTyp;
            }else {
                endUrl = "?csId=&orderId=&phone="+phone+"&state=" + state +"&states="+state+ "&page=" + page + "&pnum=10";
            }

            Log.e("tag", "initData: " + endUrl);
            // http://tserver.api.yuxuejiaoyu.net/crm/comm/orderBList?csId=&orderId=&phone=&state=1&page=2&pnum=10
            // http://tserver.api.yuxuejiaoyu.net/crm/comm/orderBList?csId=&orderId=&phone=&state=1&page=1&pnum=10
            OkHttpUtils.newGet(getActivity(), url + endUrl, new OkhttpCallback() {
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


    List<NewOrderUserInfo> userInfos = new ArrayList<NewOrderUserInfo>();
    List<OrderDetail> orders = new ArrayList<>();
    ZiXunUserInfo info;
    NewUserDataOrderAdapter adapter;

    private ZiXunUserInfo getUserInfo(JSONObject job) throws Exception {
        JSONObject object = job.getJSONObject("data");

        String total = object.getString("total");
        JSONArray jsonArray;
        if (!total.equals("0")) {
             jsonArray = object.getJSONArray("list");
        }else {
            jsonArray=null;
        }

        if (jsonArray != null) {
            List<NewOrderUserInfo> userGet = JSONUtil.parserArrayList(jsonArray, NewOrderUserInfo.class, 1);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isLoadMore) {
                        userInfos.addAll(0, userGet);
                        if (adapter == null) {
                            adapter = new NewUserDataOrderAdapter(getActivity(), userInfos, isShouZi, recycle_view);
                            recycle_view.setAdapter(adapter);
                            recycle_view.setGroupIndicator(null);
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
                        userInfos.addAll(userGet);
                        if (adapter == null) {
                            adapter = new NewUserDataOrderAdapter(getActivity(), userInfos, isShouZi, recycle_view);
                            recycle_view.setAdapter(adapter);
                            recycle_view.setGroupIndicator(null);
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
                if (userInfos.size() == 0) {
                    ll_refresh.setVisibility(View.VISIBLE);

                } else {
                    ll_refresh.setVisibility(View.GONE);
                }
            }
        });

        return info;
    }


    public static NewOneFragmentOrder newInstance(String text, boolean isShouzi, int status) {
        NewOneFragmentOrder fragment = new NewOneFragmentOrder();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, text);
        bundle.putBoolean("isShouZi", isShouzi);
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_get_zixun:

                break;
        }
    }


    public void searchInfo(String phone,int states) {

        isLoadMore = false;
        userInfos.clear();
        page = 1;
        if (adapter != null) {
            adapter.setData(userInfos);
            adapter.notifyDataSetChanged();
        }

        initData(phone,payTyp);
    }
    public void searchInfos(String phone,int payTypes) {

        isLoadMore = false;
        userInfos.clear();
        page = 1;
        if (adapter != null) {
            adapter.setData(userInfos);
            adapter.notifyDataSetChanged();
        }
        payTyp=payTypes;
        initData(phone,payTyp);
    }
    @Override
    public void onUpdate() {
        isLoadMore = false;
        userInfos.clear();
        page = 1;//数据清空
        if (adapter != null) {
            adapter.setData(userInfos);
            adapter.notifyDataSetChanged();
        }
        initData(phone,payTyp);
        // pullDownView.endUpdate(new Date());
    }
}
