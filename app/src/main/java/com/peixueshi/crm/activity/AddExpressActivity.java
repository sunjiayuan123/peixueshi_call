package com.peixueshi.crm.activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.ui.adapter.PopPhoneHistoryAdapter;
import com.peixueshi.crm.utils.PromptManager;

import org.json.JSONObject;

import butterknife.BindView;

public class AddExpressActivity extends BaseActivity{
    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_order_number)
    TextView tv_order_number;

    @BindView(R.id.et_user_name)
    EditText et_user_name;

    @BindView(R.id.et_user_phone)
    EditText et_user_phone;

    @BindView(R.id.et_user_address)
    EditText et_user_address;
    @BindView(R.id.et_user_express)
    EditText et_user_express;
    @BindView(R.id.tv_sure)
    TextView tv_sure;

    @BindView(R.id.rg_goutong)
    RadioGroup rg_goutong;

    @BindView(R.id.rb_01)
    RadioButton rb_01;
    @BindView(R.id.rb_02)
    RadioButton rb_02;
    @BindView(R.id.rb_03)
    RadioButton rb_03;
    @BindView(R.id.rb_04)
    RadioButton rb_04;
    @BindView(R.id.rb_05)
    RadioButton rb_05;

    private int goutong_type = 0;
    private String billNo;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    PopPhoneHistoryAdapter popPhoneHistoryAdapter;
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.add_express;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        billNo = getIntent().getStringExtra("bill_no");
        String newNo = billNo.substring(0,6)+"****"+billNo.substring(10,billNo.length());
        tv_order_number.setText(newNo);

        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_user_name.getText().toString().trim()) ||  TextUtils.isEmpty(et_user_phone.getText().toString().trim()) || TextUtils.isEmpty(et_user_address.getText().toString().trim())){
                    PromptManager.showMyToast("必填项不能为空", AddExpressActivity.this);
                    return;
                }

                if(goutong_type == 0){
                    PromptManager.showMyToast("请选择邮寄材料类型", AddExpressActivity.this);
                    return;
                }
                PromptManager.hideKeyboard(AddExpressActivity.this);
                addExpress(et_user_name.getText().toString(),et_user_phone.getText().toString(),et_user_address.getText().toString(),et_user_express.getText().toString());
            }
        });


        rg_goutong.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == rb_01.getId()){
                    goutong_type = 1;//正常接通
                }else if(checkedId == rb_02.getId()){
                    goutong_type = 1;//1未接通
                }else if(checkedId == rb_03.getId()){
                    goutong_type = 2;//1关停机
                }else if(checkedId == rb_04.getId()){
                    goutong_type = 4;//空号
                }else if(checkedId == rb_05.getId()){
                    goutong_type = 6;//空号
                }
            }
        });

    }


    /**
     * 添加快递
     * @param username
     * @param phone
     * @param add
     * @param beizhu
     */
    private void addExpress(String username, String phone,String add,String beizhu) {
            try {
                    String  reqUrl = Constants.host+"order/exp_set?bill_no="+ billNo+"&mail_type="+goutong_type+"&uname="+username+"&uphone="+phone+"&address="+add+"&note="+beizhu;
                    OkHttpUtils.get(AddExpressActivity.this, reqUrl, new OkhttpCallback()
                    {
                        @Override
                        public void onBefore() {
                            super.onBefore();
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(getApplicationContext(), message,
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onGetResult(Object object) {
                        }

                        @Override
                        public Object parseNetworkResponse(JSONObject object) throws
                                Exception {
                            getOrderInfo(object);
                            return null;
                        }


                    });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
    }


    private void getOrderInfo(JSONObject job) throws Exception{
            String err = job.getString("err");
            String msg = job.getString("msg");
            if (!TextUtils.isEmpty(err) && err.equals("0")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PromptManager.showMyToast(msg, AddExpressActivity.this);
                        finish();
                    }
                });
            }

    }

}
