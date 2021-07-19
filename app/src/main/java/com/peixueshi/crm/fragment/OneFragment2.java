package com.peixueshi.crm.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.ZiXunUserInfo;
import com.peixueshi.crm.ui.adapter.UserDataZiAdapter;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OneFragment2 extends Fragment implements View.OnClickListener {

    public static final String TYPE = "type";
    private View parentView;

   // @BindView(R.id.txt_content)
    TextView txt_content;

    //@BindView(R.id.recycle_view)
    RecyclerView recycle_view;

    TextView tv_huoqu_text;
   // @BindView(R.id.tv_current_get)
    TextView tv_current_get;
   // @BindView(R.id.tv_next_get_time)
    TextView tv_next_get_time;
   // @BindView(R.id.tv_count_number)
    TextView tv_count_number;
    ImageView iv_get_zixun;

    private boolean isShouZi = true;
    public OneFragment2() {
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
        parentView = inflater.inflate(R.layout.fragment_one, container, false);
        initView();
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {

        txt_content = (TextView) parentView.findViewById(R.id.txt_content);
        txt_content.setText(getArguments().getString(TYPE, "Default"));
        isShouZi = getArguments().getBoolean("isShouZi", true);

        recycle_view = (RecyclerView) parentView.findViewById(R.id.recycle_view);
        tv_huoqu_text = (TextView) parentView.findViewById(R.id.tv_huoqu_text);
        iv_get_zixun = (ImageView) parentView.findViewById(R.id.iv_get_zixun);
        iv_get_zixun.setOnClickListener(this);
        tv_current_get = (TextView) parentView.findViewById(R.id.tv_current_get);
        tv_next_get_time = (TextView) parentView.findViewById(R.id.tv_next_get_time);
        tv_count_number = (TextView) parentView.findViewById(R.id.tv_count_number);
       // StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);

        if(isShouZi){
            tv_huoqu_text.setText("获取首咨");
        }else{
            tv_huoqu_text.setText("来十个");
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycle_view.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recycle_view.setLayoutManager(layoutManager);
        initData(isShouZi);
        getShouziTime();
    }

    private void getShouziTime() {
        try {
            String url = null;
            if(isShouZi){
                url = Constants.host+"user/set_at";
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
                    public Object parseNetworkResponse(JSONObject jsonObject) throws
                            Exception {
                        getShouInfo(jsonObject);
                        return null;
                    }


                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    int time;
    int startTime;
    private void getShouInfo(JSONObject object) throws Exception{

            time = Integer.parseInt(object.getString("set_time"));
            startTime = time;
            CountDownTimer timer = new CountDownTimer(time*1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    time--;
                    tv_next_get_time.setText(Util.getTime(time));
                }

                public void onFinish() {
                    tv_next_get_time.setText("00:00");
                }
            };
            //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
            timer.start();
            tv_next_get_time.setText(object.getString("set_time")+" s");

    }

    private int page = 1;
    /**
     * 获取首咨或者库存
     */
    private void initData(boolean isShouZi) {
        try {
            String url = null;
            if(isShouZi){
                url = Constants.host+"user/getfirs";
                OkHttpUtils.get(getActivity(), url, new OkhttpCallback()
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
                        Log.e("获取到得h5api_vcodeindex-->", object.toString());
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
                keyMap.put("page",page+"");
                keyMap.put("coll",0+"");
                keyMap.put("rule",1+"");
                page++;

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
                    public Object parseNetworkResponse(JSONObject jsonObject) throws
                            Exception {
                        getUserInfo(jsonObject);
                        return null;
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    List<ZiXunUserInfo> userInfos = new ArrayList<ZiXunUserInfo>();
    ZiXunUserInfo info;
    private ZiXunUserInfo getUserInfo(JSONObject job) throws Exception{

                if(isShouZi){
                    JSONObject object = job.getJSONObject("info");
                    if (object != null) {
                        info =  JSONUtil.parser(object, ZiXunUserInfo.class,0);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userInfos.add(info);
                                recycle_view.setAdapter(new UserDataZiAdapter(getActivity(),userInfos,isShouZi));
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
                                tv_count_number.setText("共"+total+"个");
                                userInfos.addAll(0,userGet);
                                recycle_view.setAdapter(new UserDataZiAdapter(getActivity(),userInfos,isShouZi));
                            }
                        });
                    }
                }
        return info;
    }


    public static OneFragment2 newInstance(String text, boolean isShouzi) {
        OneFragment2 fragment = new OneFragment2();
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
                Toast.makeText(getActivity(),"点击了",Toast.LENGTH_SHORT).show();
                if(isShouZi){
                    time = startTime;
                    initData(isShouZi);
                }else{
                    //先刷新count
                    getTenCount();
                }
                break;
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
                    public Object parseNetworkResponse(JSONObject job) throws
                            Exception {
                        String count = job.getString("count");
                        page = 1;
                        initData(false);
                        return null;
                    }
                });


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}
