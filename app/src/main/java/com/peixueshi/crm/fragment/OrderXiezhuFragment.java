package com.peixueshi.crm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

public class OrderXiezhuFragment extends BaseFragment {
    @BindView(R.id.et_xiangmu)
    EditText et_xiangmu;
    @BindView(R.id.et_user_name)
    EditText et_user_name;

    @BindView(R.id.et_user_number)
    EditText et_user_number;
    @BindView(R.id.et_add)
    EditText et_add;

    @BindView(R.id.et_current_city)
    EditText et_current_city;
    @BindView(R.id.et_price)
    EditText et_price;
    @BindView(R.id.ll_save)
    LinearLayout ll_save;
    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    //接口
    OrderActivity callBackValue;
    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context );
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(OrderActivity) getActivity();
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ArmsUtils.inflate(getActivity(), R.layout.order_xiezhu_bm);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }


    @OnClick({R.id.ll_save,R.id.et_xiangmu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
         /*   case R.id.et_select_school:
                initDataSchool();
                break;*/
            case R.id.ll_save:
                saveLocalAndCheck();
                break;
            case R.id.et_xiangmu://项目
                PromptManager.hideKeyboard(getActivity());
                showDropView(et_xiangmu);
                break;
        }
    }


    PopXiangmuDropAdapter popXiangmuDropAdapter;
    List<Map<String,String>> listTypeSelect = new ArrayList<>();
    private void showDropView(EditText dropVeiw) {
        //取协助报名项目
        if(listTypeSelect.size() == 0) {
            try {
                String url = null;
                //url = Constants.host+"team/p_list";
                url = Constants.f;
                HashMap<String, String> keyMap = new HashMap<>();
                keyMap.put("page", "1");
                keyMap.put("count", "100");
                keyMap.put("h_start", "0");
                OkHttpUtils.post(getActivity(), url, keyMap, new OkhttpCallback() {
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

                            JSONObject object = job.getJSONObject("date");
                            JSONArray jsonArray = object.getJSONArray("list");
                            if (jsonArray != null) {
                                listTypeSelect = JSONUtil.getListMap(jsonArray);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showView(dropVeiw);
                                    }
                                });
                            }

                        return null;
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }else{
            showView(dropVeiw);
        }


    }

    public void showView(EditText dropVeiw){
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
                rightInfo = listTypeSelect.get(pos);
                String name ="";
                if(rightInfo.containsKey("pname")){
                     name =  rightInfo.get("pname");
                }
                else if(rightInfo.containsKey("pr_name")){
                     name =  rightInfo.get("pr_name");
                }else if(rightInfo.containsKey("p_name")){
                     name =  rightInfo.get("p_name");
                }
                dropVeiw.setText(name);
                PromptManager.closePopWindow();
            }
        });
        PromptManager.showPopView(view,dropVeiw,getActivity());
    }

    Map<String, String> rightInfo = new HashMap<>();
    private void saveLocalAndCheck() {
        String type = et_xiangmu.getText().toString().trim();
        String userName = et_user_name.getText().toString().trim();
        String userNumber = et_user_number.getText().toString().trim();
        String add = et_add.getText().toString().trim();
        String addCurr = et_current_city.getText().toString().trim();
        String c_price = et_price.getText().toString().trim();
        if(TextUtils.isEmpty(type)){
            PromptManager.showMyToast("请选择报考项目",getActivity());
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
        }else if(TextUtils.isEmpty(addCurr)){
            PromptManager.showMyToast("请输入学员报考城市",getActivity());
            return;
        }else if(TextUtils.isEmpty(c_price)){
            PromptManager.showMyToast("请输入报名金额",getActivity());
            return;
        }
        if(rightInfo != null){
            rightInfo.put("u_name",userName);
            rightInfo.put("u_number",userNumber);
            rightInfo.put("u_add",add);
            rightInfo.put("u_city",addCurr);
            rightInfo.put("c_price",c_price);
            callBackValue.SendMessageValue(rightInfo,2);
        }
    }
}
