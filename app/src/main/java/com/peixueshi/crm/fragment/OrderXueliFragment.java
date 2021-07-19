package com.peixueshi.crm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.peixueshi.crm.ui.adapter.PopXiangmuDropAdapter;
import com.peixueshi.crm.ui.widget.OutPopWindow;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.PromptManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderXueliFragment extends BaseFragment {
    @BindView(R.id.et_select_school)
    EditText et_select_school;
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

    @BindView(R.id.et_kaoshi_leixing)
    EditText et_kaoshi_leixing;
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_user_number)
    EditText et_user_number;
    @BindView(R.id.et_user_address)
    EditText et_user_address;


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
        return ArmsUtils.inflate(getActivity(), R.layout.order_xueli);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @OnClick({R.id.et_select_school,R.id.ll_save,R.id.et_kaoshi_leixing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_select_school:
                initDataSchool();
                break;
            case R.id.ll_save:
                saveLocalAndCheck();
                break;
            case R.id.et_kaoshi_leixing://考试类型
                PromptManager.hideKeyboard(getActivity());
                showDropView(et_kaoshi_leixing);
                break;
        }
    }

    private void saveLocalAndCheck() {
        String school =  et_select_school.getText().toString().trim();
        String type = et_kaoshi_leixing.getText().toString().trim();
        String userName = et_username.getText().toString().trim();
        String userNumber = et_user_number.getText().toString().trim();
        String add = et_user_address.getText().toString().trim();
        if(TextUtils.isEmpty(school)){
            PromptManager.showMyToast("请选择学校及专业",getActivity());
            return;
        }else if(TextUtils.isEmpty(type)){
            PromptManager.showMyToast("请选择报考类型",getActivity());
            return;
        }else if(TextUtils.isEmpty(userName)){
            PromptManager.showMyToast("请输入学员姓名",getActivity());
            return;
        }else if(TextUtils.isEmpty(userNumber)){
            PromptManager.showMyToast("请输入学员身份证号",getActivity());
            return;
        }else if(TextUtils.isEmpty(add)){
            PromptManager.showMyToast("请输入学员户籍地址",getActivity());
            return;
        }
        if(rightInfo != null){
            rightInfo.put("u_name",userName);
            if(type.equals("网教自己考")){
                rightInfo.put("u_type","1");
            }else if(type.equals("网教直取")){
                rightInfo.put("u_type","2");
            }else{
                rightInfo.put("u_type","3");
            }
            rightInfo.put("u_number",userNumber);
            rightInfo.put("u_add",add);
            callBackValue.SendMessageValue(rightInfo,1);
        }
    }


    PopXiangmuDropAdapter popXiangmuDropAdapter;
    List<Map<String,String>> listTypeSelect = new ArrayList<>();
    private void showDropView(EditText dropVeiw) {
        if(listTypeSelect.size() == 0){
            for(int i=0;i<3;i++){
                if(i == 0){
                    Map<String,String> map = new HashMap<>();
                    map.put("pr_name","网教自己考");//1
                    listTypeSelect.add(map);
                }else if(i == 1){
                    Map<String,String> map = new HashMap<>();//2
                    map.put("pr_name","网教直取");
                    listTypeSelect.add(map);
                }else{
                    Map<String,String> map = new HashMap<>();//3
                    map.put("pr_name","成考");
                    listTypeSelect.add(map);
                }

            }
        }
        View view = View.inflate(getActivity(),R.layout.ll_xiangmu_drop,null);
        RecyclerView recyclerView = view.findViewById(R.id.rv_drop);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager1);
        if(popXiangmuDropAdapter == null){
            popXiangmuDropAdapter = new PopXiangmuDropAdapter(getActivity(), listTypeSelect);
            recyclerView.setAdapter(popXiangmuDropAdapter);
        }else{
            popXiangmuDropAdapter.setData(listTypeSelect);
            recyclerView.setAdapter(popXiangmuDropAdapter);
            popXiangmuDropAdapter.notifyDataSetChanged();
        }
        popXiangmuDropAdapter.setOnItemClickListener(new PopXiangmuDropAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int pos) {
                String name =  listTypeSelect.get(pos).get("pr_name");
                dropVeiw.setText(name);
                PromptManager.closePopWindow();
            }
        });
        PromptManager.showPopView(view,dropVeiw,getActivity());
    }

    private void initDataSchool() {
        try {
            String url = null;
            //url = Constants.host+"team/p_list";
            url = Constants.d;
            HashMap<String,String> keyMap = new HashMap<>();
            keyMap.put("page","1");
            keyMap.put("count","100");
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



    public void showPopFormBottom(View view, List<Map<String,String>> left, List<Map<String,String>> right) {
        OutPopWindow popWindow = new OutPopWindow(getActivity(), onClickListener,1,left,right,this);
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

    private void getUserInfo(JSONObject job) throws Exception{

                JSONObject object = job.getJSONObject("date");
                JSONArray jsonArray = object.getJSONArray("list");
                if (jsonArray != null) {
                    List<Map<String,String>> listXiangmu = JSONUtil.getListMap(jsonArray);
                    String p_id = null;
                    if(listXiangmu != null && listXiangmu.size() >0){
                        p_id = listXiangmu.get(0).get("s_id");
                    }
                    getPIdInfo(listXiangmu,p_id);
                }


    }

    public void getPIdInfo(List<Map<String,String>> listXiangmu,String sid){
        try {
            String url = null;
            url = Constants.e+sid;
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
                    getPidInfo(listXiangmu,jsonObject);
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void getPidInfo(List<Map<String,String>> listSchools,JSONObject job) throws Exception{

            JSONObject object = job.getJSONObject("date");
            if (!TextUtils.isEmpty(object.toString())) {
                JSONArray jsonArray = object.getJSONArray("list");
                if (jsonArray != null) {
                    List<Map<String,String>> listSchoolZhuannyes = JSONUtil.getListMap(jsonArray);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showPopFormBottom(et_select_school,listSchools,listSchoolZhuannyes);
                        }
                    });
                }
            }else{
                String msg = job.getString("msg");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }

    }

    Map<String, String> leftMap;
    Map<String, String> rightInfo = new HashMap<>();
    public void setDisInfo(Map<String, String> leftMap, Map<String, String> rightInfo) {
        this.leftMap = leftMap;
        if(rightInfo != null){
            rightInfo.put("s_name",leftMap.get("s_name"));
            rightInfo.put("s_id",leftMap.get("s_id"));
        }
        this.rightInfo = rightInfo;
        //callBackValue.SendMessageValue(rightInfo,1);
        initInfo();
    }

    private void initInfo() {
        et_select_school.setText(rightInfo.get("s_name")+"/"+rightInfo.get("m_name"));
    }
}
