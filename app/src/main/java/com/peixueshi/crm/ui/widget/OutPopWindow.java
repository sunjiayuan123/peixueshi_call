package com.peixueshi.crm.ui.widget;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jess.arms.base.BaseFragment;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.fragment.OrderWangkeFragment;
import com.peixueshi.crm.fragment.OrderXiezhuFragment;
import com.peixueshi.crm.fragment.OrderXueliFragment;
import com.peixueshi.crm.fragment.OrderZhiquFragment;
import com.peixueshi.crm.ui.adapter.PopLeftRecycleView;
import com.peixueshi.crm.ui.adapter.PopRightRecycleView;
import com.peixueshi.crm.ui.adapter.PopXueliRightRecycleView;
import com.peixueshi.crm.utils.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutPopWindow extends PopupWindow {

    private Activity mContext;

    private View view;

//    private Button btn_cancel;
    private RecyclerView rv_left;
    private RecyclerView rv_right;

    private PopLeftRecycleView popLeftRecycleView;
    private PopRightRecycleView popRightRecycleView;
    private PopXueliRightRecycleView popXueliRightRecycleView;

    private List<Map<String,String>> leftMap;
    private List<Map<String,String>> rightMap;

    Map<String,String> leftInfo;
    OrderWangkeFragment orderWangkeFragment;
    OrderXueliFragment orderXueliFragment;
    OrderXiezhuFragment orderXiezhuFragment;
    OrderZhiquFragment orderZhiquFragment;
    private int type = 0;
    public OutPopWindow(Activity mContext, View.OnClickListener itemsOnClick, int type, List<Map<String, String>> leftMapOld, List<Map<String, String>> rightMapOld, BaseFragment baseFragment) {

        this.type = type;
        if(type == 0){
            this.view = LayoutInflater.from(mContext).inflate(R.layout.bottom_out_layout, null);
        }else if(type == 1){
            this.view = LayoutInflater.from(mContext).inflate(R.layout.bottom_out_xueli_layout, null);
        }


        this.mContext = mContext;
        this.leftMap = leftMapOld;
        this.rightMap = rightMapOld;
        if(leftMap != null && leftMap.size()>0){
            leftInfo = leftMap.get(0);
        }
        if(type == 0){
            this.orderWangkeFragment = (OrderWangkeFragment) baseFragment;
        }else if(type == 1){
            this.orderXueliFragment = (OrderXueliFragment) baseFragment;
        }else if(type == 2){
            this.orderXiezhuFragment = (OrderXiezhuFragment) baseFragment;
        }else if(type == 3){
            this.orderZhiquFragment = (OrderZhiquFragment) baseFragment;
        }

//        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        rv_left = (RecyclerView) view.findViewById(R.id.rv_left);
        rv_right = (RecyclerView) view.findViewById(R.id.rv_right);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_left.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
        rv_left.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_right.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
        rv_right.setLayoutManager(layoutManager2);

        popLeftRecycleView = new PopLeftRecycleView(mContext,leftMap,type);
        rv_left.setAdapter(popLeftRecycleView);
        if(type == 0){
            popRightRecycleView =  new PopRightRecycleView(mContext,rightMap);
            rv_right.setAdapter(popRightRecycleView);
        }else if(type == 1){
            popXueliRightRecycleView =  new PopXueliRightRecycleView(mContext,rightMap);
            rv_right.setAdapter(popXueliRightRecycleView);
        }else{
            popRightRecycleView =  new PopRightRecycleView(mContext,rightMap);
            rv_right.setAdapter(popRightRecycleView);
        }




   /*     // 取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });*/

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.pop_anim_style);


        popLeftRecycleView.setOnItemClickListener(new PopLeftRecycleView.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int pos) {
                leftInfo = leftMap.get(pos);
                if(type == 0){
                    if(leftInfo.containsKey("p_id")){
                        getPIdInfo(leftInfo.get("p_id"),pos);
                    }else{
                        getPIdInfo(leftInfo.get("pr_id"),pos);
                    }

                }else if(type == 1){
                    getSchoolDetailInfo(leftInfo.get("s_id"),pos);
                }

            }
        });
        if(type == 1){
            popXueliRightRecycleView.setOnItemClickListener(new PopXueliRightRecycleView.OnItemClickListener() {
                @Override
                public void OnItemClickListener(int pos) {
                    Map<String,String> rightInfo =  rightMap.get(pos);
                    orderXueliFragment.setDisInfo(leftInfo,rightInfo);
                    dismiss();
                }
            });
        }else{
            popRightRecycleView.setOnItemClickListener(new PopLeftRecycleView.OnItemClickListener() {
                @Override
                public void OnItemClickListener(int pos) {
                    Map<String,String> rightInfo =  rightMap.get(pos);
                    orderWangkeFragment.setDisInfo(leftInfo,rightInfo);
                    dismiss();
                }
            });
        }

    }

    public void getPIdInfo(String pid,int pos){
        try {
            String url = null;
            url = Constants.c;
            HashMap<String,String> keyMap = new HashMap<>();
            keyMap.put("page","1");
            keyMap.put("count","100");
            keyMap.put("p_id",pid);
            OkHttpUtils.post(mContext, url, keyMap,new OkhttpCallback()
            {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(mContext, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject object) throws
                        Exception {
                    getPidInfo(object,pos);
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void getPidInfo(JSONObject job,int pos) throws Exception {
            JSONObject object = job.getJSONObject("date");
                JSONArray jsonArray = object.getJSONArray("list");
                if (jsonArray != null) {
                    List<Map<String,String>> listPidInfo = JSONUtil.getListMap(jsonArray);
                    this.rightMap = listPidInfo;
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(type == 0){
                                popRightRecycleView.setData(listPidInfo);
                                popRightRecycleView.notifyDataSetChanged();
                                popLeftRecycleView.refreshItem(pos);
                            }else{
                                popXueliRightRecycleView.setData(listPidInfo);
                                popXueliRightRecycleView.notifyDataSetChanged();
                                popXueliRightRecycleView.refreshItem(pos);
                            }

                        }
                    });
                }




    }



    public void getSchoolDetailInfo(String sid,int pos){
        try {
            String url = null;
            url = Constants.e+sid;
            OkHttpUtils.get(mContext, url,new OkhttpCallback()
            {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(mContext, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject jsonObject) throws
                        Exception {
                    getSchoolInfo(jsonObject,pos);
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void getSchoolInfo(JSONObject job,int pos) throws Exception{
            JSONObject object = job.getJSONObject("date");
            if (!TextUtils.isEmpty(object.toString())) {
                JSONArray jsonArray = object.getJSONArray("list");
                if (jsonArray != null) {
                    List<Map<String,String>> listSchoolZhuannyes = JSONUtil.getListMap(jsonArray);
                    this.rightMap = listSchoolZhuannyes;
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(type == 0){
                                popRightRecycleView.setData(listSchoolZhuannyes);
                                popRightRecycleView.notifyDataSetChanged();
                                popLeftRecycleView.refreshItem(pos);
                            }else{
                                popXueliRightRecycleView.setData(listSchoolZhuannyes);
                                popXueliRightRecycleView.notifyDataSetChanged();
                                popLeftRecycleView.refreshItem(pos);
                            }

                        }
                    });
                }
        }
    }
}
