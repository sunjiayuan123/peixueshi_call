package com.peixueshi.crm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.view.PullDownView;
import com.peixueshi.crm.R;
import com.peixueshi.crm.activity.MineCallHistoryActivity;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.ZiXunUserInfo;
import com.peixueshi.crm.ui.adapter.PopXiangmuDropAdapter;
import com.peixueshi.crm.ui.adapter.UserDataZiHandShouziListAdapter;
import com.peixueshi.crm.ui.widget.LoadMoreListView;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.PromptManager;
import com.peixueshi.crm.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OneFragmentMasterShouzi extends Fragment implements PullDownView.UpdateHandle,View.OnClickListener {

    public static final String TYPE = "type";
    private View parentView;

   // @BindView(R.id.txt_content)
//    TextView txt_content;

    //@BindView(R.id.recycle_view)
    public LoadMoreListView recycle_view;

    TextView tv_huoqu_text;
   // @BindView(R.id.tv_current_get)
    TextView tv_current_get;
   // @BindView(R.id.tv_next_get_time)
    TextView tv_next_get_time;
   // @BindView(R.id.tv_count_number)
    public TextView tv_count_number;
    ImageView iv_get_zixun;
    public PullDownView pullDownView;

    public LinearLayout ll_refresh_none;

    private boolean isShouZi = true;
    public OneFragmentMasterShouzi() {
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
        parentView = inflater.inflate(R.layout.fragment_one_shouzi_hand, container, false);
        initView();
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {

     /*   txt_content = (TextView) parentView.findViewById(R.id.txt_content);
        txt_content.setText(getArguments().getString(TYPE, "Default"));*/
        isShouZi = getArguments().getBoolean("isShouZi", true);

        recycle_view = (LoadMoreListView) parentView.findViewById(R.id.recycle_view_hand_shouzi);
        tv_huoqu_text = (TextView) parentView.findViewById(R.id.tv_huoqu_text);
        iv_get_zixun = (ImageView) parentView.findViewById(R.id.iv_get_zixun);
        ll_refresh_none =  parentView.findViewById(R.id.ll_refresh_none);
        iv_get_zixun.setOnClickListener(this);
        tv_huoqu_text.setOnClickListener(this);
        tv_current_get = (TextView) parentView.findViewById(R.id.tv_current_get);
        tv_next_get_time = (TextView) parentView.findViewById(R.id.tv_next_get_time);
        tv_count_number = (TextView) parentView.findViewById(R.id.tv_count_number);
        pullDownView =(PullDownView) parentView.findViewById(R.id.user_pull_to_refresh);
       // StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);

        if(isShouZi){
            tv_huoqu_text.setText("首咨");
            iv_get_zixun.setVisibility(View.GONE);
            tv_huoqu_text.setBackground(getResources().getDrawable(R.drawable.btn_shouzi_bg));
            tv_next_get_time.setVisibility(View.VISIBLE);
        }else{
            tv_huoqu_text.setText("来十个");
            tv_next_get_time.setVisibility(View.INVISIBLE);
            iv_get_zixun.setVisibility(View.VISIBLE);
            tv_huoqu_text.setBackground(null);
        }

        ll_refresh_none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoadMore = false;
                userInfos.clear();
                page = 1;
                if(adapter != null){
                    adapter.setData(userInfos);
                    adapter.notifyDataSetChanged();
                }
                initData(isShouZi,null);
            }
        });

      /*  LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycle_view.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recycle_view.setLayoutManager(layoutManager);*/
        initData(isShouZi,null);
        initXiangmu(null);
//        tv_next_get_time.setText("00:00");
//        getShouziTime();
        setRefresh();
        if(recycle_view != null){
            recycle_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position>=userInfos.size()){//刷新条 执行加载更多
                        return;
                    }
                   ZiXunUserInfo userInfo = userInfos.get(position);
                   String number;
                    if (isShouZi) {
                        number = userInfo.getWf_phone();
                    } else {
                        number = userInfo.getWp_phone();
                    }
                    Intent intent = new Intent(getActivity(), MineCallHistoryActivity.class);
                    intent.putExtra("phone",number);
                    getActivity().startActivity(intent);
                }
            });
        }
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
                initData(isShouZi,null);
            }
        });
    }




    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            if (time <= 0) {
                mHandler.sendEmptyMessage(1);
            } else {
                mHandler.sendEmptyMessage(2);
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    int time;
    int startTime;
    Thread thread;
    private void getShouInfo(JSONObject object) {
        try {
            time = Integer.parseInt(object.getString("set_time"));
            startTime = time;
           getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_next_get_time.setText("00:00");
                }
            });

           time = 1;

            thread = new Thread(runnable);
            thread.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tv_next_get_time.setText("00:00");
                    break;
                case 2:
                    tv_next_get_time.setText(Util.getTime(time));
                    break;
                default:
            }

        }
    };

    private int page = 1;
    /**
     * 获取首咨或者库存
     */
    private void initData(boolean isShouZi,String phone) {
        try {
            String url = null;
            if(isShouZi){
                url = Constants.host+"user/firs_list";
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
                if(phone != null){
                    keyMap.put("phone",phone);
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
                    public Object parseNetworkResponse(JSONObject object) throws
                            Exception {
                        getUserInfo(object);
                        return null;
                    }
                });
            }else{
                url = Constants.host+"work/pool_list";
                HashMap<String,String> keyMap = new HashMap<String,String>();
                keyMap.put("count","10");
                if(isLoadMore){
                    keyMap.put("page",page+"");
                    page++;
                }else{
                    keyMap.put("page",1+"");
                }
                keyMap.put("coll",0+"");
                keyMap.put("rule",1+"");
                if(phone != null){
                    keyMap.put("phone",phone);
                }

                OkHttpUtils.post(getActivity(), url, keyMap,new OkhttpCallback()
                {
                    @Override
                    public void onBefore() {
                        super.onBefore();
                        Log.e("获取四位验证码开始测试", "测试开始请求");
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getActivity(), message,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onGetResult(Object object) {
//                        Log.e("获取到得h5api_vcodeindex-->", object.toString());
                    }

                    @Override
                    public Object parseNetworkResponse(JSONObject object) throws
                            Exception {
                        getUserInfo(object);
                        return null;
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


   public List<ZiXunUserInfo> userInfos = new ArrayList<ZiXunUserInfo>();
    ZiXunUserInfo info;
    public UserDataZiHandShouziListAdapter adapter;
    private ZiXunUserInfo getUserInfo(JSONObject job) throws Exception{
                //{"error_code":-1,"msg":"请求未携带token，无权限访问"}
                if(isShouZi){
                    JSONObject object = job.getJSONObject("date");
                    String total = object.getString("total");
                    JSONArray jsonArray = object.getJSONArray("list");
                    if (jsonArray != null && jsonArray.length()>0) {
                        List<ZiXunUserInfo> users =  JSONUtil.parserArrayList(jsonArray, ZiXunUserInfo.class,0);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isLoadMore) {
                                    countNumber = Integer.valueOf(total);
                                    tv_count_number.setText("共" + total + "个");
                                    userInfos.addAll(0, users);
                                    if (adapter == null) {
                                        adapter = new UserDataZiHandShouziListAdapter(getActivity(), userInfos, isShouZi, OneFragmentMasterShouzi.this);
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
                                        adapter = new UserDataZiHandShouziListAdapter(getActivity(), userInfos, isShouZi, OneFragmentMasterShouzi.this);
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
                }else{
                    JSONObject object = job.getJSONObject("date");
                    String total = object.getString("total");

                    JSONArray jsonArray = object.getJSONArray("list");
                    if (jsonArray != null) {
                        List<ZiXunUserInfo> userGet =  JSONUtil.parserArrayList(jsonArray, ZiXunUserInfo.class,0);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                             /*   if(isNewAdd){

                                }*/
                             if(!isLoadMore){
                                 countNumber = Integer.valueOf(total);
                                 tv_count_number.setText("共"+total+"个");
                                 userInfos.addAll(0,userGet);
                                 if(adapter == null){
                                     adapter = new UserDataZiHandShouziListAdapter(getActivity(),userInfos,isShouZi, OneFragmentMasterShouzi.this);
                                     recycle_view.setAdapter(adapter);
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
                                 countNumber = Integer.valueOf(total);
                                 tv_count_number.setText("共"+total+"个");
                                 userInfos.addAll(userGet);
                                 if(adapter == null){
                                     adapter = new UserDataZiHandShouziListAdapter(getActivity(),userInfos,isShouZi, OneFragmentMasterShouzi.this);
                                     recycle_view.setAdapter(adapter);
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
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(userInfos.size() == 0){
                            recycle_view.setVisibility(View.GONE);
                            ll_refresh_none.setVisibility(View.VISIBLE);
                        }else{
                            ll_refresh_none.setVisibility(View.GONE);
                            recycle_view.setVisibility(View.VISIBLE);
                        }
                    }
                });


        return info;
    }


    public static OneFragmentMasterShouzi newInstance(String text, boolean isShouzi) {
        OneFragmentMasterShouzi fragment = new OneFragmentMasterShouzi();
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
                isLoadMore = false;
                if(isShouZi){
                   /* if(time>0){
                        Toast.makeText(getActivity(),"倒计时中，请稍后获取",Toast.LENGTH_SHORT).show();
                    }else{
                        time = startTime;
                        thread = new Thread(runnable);
                        thread.start();//获取到再开始倒计时
                        initXiangmu(iv_get_zixun);
//                        getShouZi("1001001");
                    }*/

                   // initData(isShouZi);
                }else{
                    //先刷新count
                    getTenCount();
                }
                break;
            case R.id.tv_huoqu_text:
                isLoadMore = false;
                if(isShouZi){
                    initXiangmu(iv_get_zixun);
                }
                break;
        }
    }


    private boolean isFriLoadInfo = true;
    List<Map<String,String>> listXiangmu;
    private void initXiangmu(ImageView iv_get_zixun) {
        try {
            String url = null;
            url = Constants.host+"user/firs_info";


            OkHttpUtils.get(getActivity(), url,new OkhttpCallback()
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
                    listXiangmu = JSONUtil.getListMap(jsonObject.getJSONArray("msg"));
                    if(listXiangmu.size() == 0){
                        PromptManager.showMyToast("暂无项目",getActivity());
                    }else{
                        if(isFriLoadInfo){
                            isFriLoadInfo = false;

                           getActivity().runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   String name = "";
                                   if(listXiangmu.get(0).containsKey("pname")){
                                       name =  listXiangmu.get(0).get("pname");
                                   }
                                   Map<String,String> map = listXiangmu.get(0);
                                   startTime = Integer.valueOf(map.get("often"));
                                   if(isShouZi){
                                       tv_huoqu_text.setText(name+"首咨");
                                       tv_current_get.setVisibility(View.VISIBLE);
                                       tv_current_get.setText(Integer.valueOf(map.get("count"))%1000+"/"+Integer.valueOf(map.get("count"))/1000);
                                   }else{
                                       tv_huoqu_text.setText("来十条");
                                       tv_current_get.setVisibility(View.GONE);
                                   }
                               }
                           });

                        }else{
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(iv_get_zixun != null){
                                        showDropView(iv_get_zixun);
                                    }

                                }
                            });
                        }

                    }
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    PopXiangmuDropAdapter popXiangmuDropAdapter;
    private void showDropView(ImageView iv_get_zixun) {
        View view = View.inflate(getActivity(),R.layout.ll_xiangmu_drop,null);
        RecyclerView recyclerView = view.findViewById(R.id.rv_drop);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager1);
        if(popXiangmuDropAdapter == null){
            popXiangmuDropAdapter = new PopXiangmuDropAdapter(getActivity(), listXiangmu);
            recyclerView.setAdapter(popXiangmuDropAdapter);
        }else{
            popXiangmuDropAdapter.setData(listXiangmu);
            recyclerView.setAdapter(popXiangmuDropAdapter);
            popXiangmuDropAdapter.notifyDataSetChanged();
        }
        popXiangmuDropAdapter.setOnItemClickListener(new PopXiangmuDropAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int pos) {
                String name = "";
                if(listXiangmu.get(pos).containsKey("pname")){
                    name =  listXiangmu.get(pos).get("pname");
                }
               Map<String,String> map = listXiangmu.get(pos);
                startTime = Integer.valueOf(map.get("often"));
                tv_huoqu_text.setText(name+"首咨");
                tv_current_get.setText(Integer.valueOf(map.get("count"))%1000+"/"+Integer.valueOf(map.get("count"))/1000);
               /* clickPos = pos;
                et_xiangmu.setText(name);*/
                PromptManager.closePopWindow();
              //  getShouZi(listXiangmu.get(pos).get("pid"));
            }
        });
        PromptManager.showPopViewSize(view,iv_get_zixun,300,getActivity());
    }


    private void getShouZi(String pid) {
       String url = Constants.host+"user/getfirs?pid="+pid;
        OkHttpUtils.get(getActivity(), url, new OkhttpCallback()
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
            public Object parseNetworkResponse(JSONObject object) throws
                    Exception {
                getShouZiUser(object);
                return null;
            }
        });
    }

    public int countNumber = 0;
    //添加一条Head
    private void getShouZiUser(JSONObject job) throws Exception {
                if (isShouZi) {
                    if (job.has("err") && job.getInt("err") == 5){
                        time = 60;//获取失败，60秒倒计时
                        thread = new Thread(runnable);
                        thread.start();//开始倒计时
                        return;
                    }
                    JSONObject object = job.getJSONObject("info");
                    if (object != null) {
                        info = JSONUtil.parser(object, ZiXunUserInfo.class, 0);
                        if(info != null){
                            time = startTime;
                            thread = new Thread(runnable);
                            thread.start();//开始倒计时
                        }else{
                            time = 60;//获取失败，60秒倒计时
                            thread = new Thread(runnable);
                            thread.start();//开始倒计时
                            return;
                        }
                       /* getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ll_refresh.setVisibility(View.GONE);
                                countNumber++;
                                tv_count_number.setText("共"+countNumber+"条");
                                userInfos.add(0,info);
                                if (adapter == null) {
                                    adapter = new UserDataZiListAdapter(getActivity(), userInfos, isShouZi,OneFragment.this);
                                    recycle_view.setAdapter(adapter);
                                }
                                adapter.setData(userInfos);
                                adapter.notifyDataSetChanged();
                                pullDownView.endUpdate(new Date());
                                recycle_view.onLoadMoreComplete(false);
                            }
                        });*/
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isLoadMore = false;
                                userInfos.clear();
                                page = 1;
                                if(adapter != null){
                                    adapter.setData(userInfos);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                        initData(isShouZi,null);
                    }
                  }

    }


    /**
     * 获取首咨或者库存
     */
    private void getTenCount() {
        try {
            String url = null;
                url = Constants.host+"work/come";
                OkHttpUtils.get(getActivity(), url, new OkhttpCallback()
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
                    public Object parseNetworkResponse(JSONObject object) throws
                            Exception {
                        int count = object.getInt("count");
                        isLoadMore = false;
                        if(count == 0){
                            PromptManager.showMyToast("无更多数据",getActivity());
                        }else{
                            initData(isShouZi,null);
                        }
                        return null;
                    }
                });


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void onUpdate() {

        isLoadMore = false;
        userInfos.clear();
        page = 1;
        if(adapter != null){
            adapter.setData(userInfos);
            adapter.notifyDataSetChanged();
        }
        initData(isShouZi,null);
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
        initData(isShouZi,phone);
    }
}
