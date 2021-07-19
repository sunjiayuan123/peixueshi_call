package com.peixueshi.crm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.peixueshi.crm.R;
import com.peixueshi.crm.activity.OrderActivity;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.ui.widget.OutPopWindow;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.PromptManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderWangkeFragment extends BaseFragment {
    @BindView(R.id.et_show_info)
    EditText et_show_info;

    //接口
    OrderActivity callBackValue;
    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Context context) {
        // TODO Auto-generated method stub
        super.onAttach(context );
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(OrderActivity) getActivity();
    }


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ArmsUtils.inflate(getActivity(), R.layout.order_wangke);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }


    @OnClick({R.id.et_show_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_show_info:
                initDataWangke();
                break;
        }
    }

    private void initDataWangke() {
            try {
                String url = null;
                //url = Constants.host+"team/p_list";
                url = Constants.b;
                HashMap<String,String> keyMap = new HashMap<>();
            /*    if(Constants.isWifiLimit){
                    keyMap.put("page","1");
                    keyMap.put("count","100");
                    keyMap.put("id","1");
                }else{*/
                    keyMap.put("page","1");
                    keyMap.put("count","100");
                    keyMap.put("id","0");
//                }

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

    @Override
    public void setData(@Nullable Object data) {

    }

    public void showPopFormBottom(View view,List<Map<String,String>> left,List<Map<String,String>> right) {
        OutPopWindow popWindow = new OutPopWindow(getActivity(), onClickListener,0,left,right,this);
        //showAtLocation(View parent, int gravity, int x, int y)
        popWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();

    }

    private void getUserInfo(JSONObject job) throws Exception {

        JSONArray jsonArray = null;
        if(job.has("list")){
             jsonArray = job.getJSONArray("list");
        }else{
            jsonArray = job.getJSONObject("date").getJSONArray("list");
        }

        if (jsonArray != null) {


            if (Constants.isWifiLimit) {
                List<Map<String, String>> listXiangmu = JSONUtil.getListMap(jsonArray);
            if(listXiangmu.size() == 0){
                PromptManager.showMyToast("暂无项目", getActivity());
                return;
            }
                String p_id = null;
                if (listXiangmu != null && listXiangmu.size() > 0) {
                    if(listXiangmu.get(0).containsKey("p_id")){
                        p_id = listXiangmu.get(0).get("p_id");
                    }else{
                        p_id = listXiangmu.get(0).get("pr_id");
                    }

                }
                getPIdInfo(listXiangmu, p_id);
      } else {

                List<Map<String, String>> listXiangmu = JSONUtil.getListMap(jsonArray);
                if (listXiangmu != null && listXiangmu.size() > 0) {
                    if (listXiangmu.get(0).get("pr_id").equals("1000000") || listXiangmu.get(0).get("pr_id").equals("1001000")) {
                        listXiangmu.remove(0);
                    }
                }

                if (listXiangmu != null && listXiangmu.size() > 0) {
                    if (listXiangmu.get(0).get("pr_id").equals("1000000") || listXiangmu.get(0).get("pr_id").equals("1001000")) {
                        listXiangmu.remove(0);
                    }
                }

                String p_id = null;
                if (listXiangmu != null && listXiangmu.size() > 0) {
                    p_id = listXiangmu.get(0).get("pr_id");
                }
                getPIdInfo(listXiangmu, p_id);
            }
    }

    }

    public void getPIdInfo(List<Map<String,String>> listXiangmu,String pid){
        try {
            String url = null;
            url = Constants.c;
            HashMap<String,String> keyMap = new HashMap<>();
            keyMap.put("page","1");
            keyMap.put("count","100");
            keyMap.put("p_id",pid);
            OkHttpUtils.post(getActivity(), url, keyMap,new OkhttpCallback()
            {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
               /*     Toast.makeText(getActivity(), message,
                            Toast.LENGTH_SHORT).show();*/
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject jsonObject) throws
                        Exception {
                    getPidInfo(listXiangmu,jsonObject);
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void getPidInfo(List<Map<String,String>> listXiangmu,JSONObject job) throws Exception{
            JSONObject object = job.getJSONObject("date");
                JSONArray jsonArray = object.getJSONArray("list");
                if (jsonArray != null) {
                    List<Map<String,String>> listPidInfo = JSONUtil.getListMap(jsonArray);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showPopFormBottom(et_show_info,listXiangmu,listPidInfo);
                        }
                    });
                }


    }

    Map<String, String> leftMap;
    Map<String, String> rightInfo;
    public void setDisInfo(Map<String, String> leftMap, Map<String, String> rightInfo) {
        this.leftMap = leftMap;
        this.rightInfo = rightInfo;
        callBackValue.SendMessageValue(rightInfo,0);
        initInfo();
    }

    private void initInfo() {
      // et_show_info.setText(leftMap.get("pr_name"));
    }
}
