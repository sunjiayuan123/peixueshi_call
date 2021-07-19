package com.peixueshi.crm.activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.peixueshi.crm.MainActivity;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.ui.adapter.PopPhoneHistoryAdapter;
import com.peixueshi.crm.utils.MD5Util;
import com.peixueshi.crm.utils.PromptManager;

import org.json.JSONObject;

import butterknife.BindView;

public class ModifyPassActivity extends BaseActivity{
    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.et_pass_text)
    EditText et_pass_text;

    @BindView(R.id.et_pass_new_text)
    EditText et_pass_new_text;

    @BindView(R.id.et_pass_new_sure_text)
    EditText et_pass_new_sure_text;
    @BindView(R.id.tv_sure)
    TextView tv_sure;





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
        return R.layout.modify_pass;
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

        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_pass_text.getText().toString()) ||  TextUtils.isEmpty(et_pass_new_text.getText().toString()) || TextUtils.isEmpty(et_pass_new_sure_text.getText().toString())){
                    PromptManager.showMyToast("密码不能为空",ModifyPassActivity.this);
                    return;
                }
                if(!et_pass_new_text.getText().toString().equals(et_pass_new_sure_text.getText().toString())){
                    PromptManager.showMyToast("两次密码不一致",ModifyPassActivity.this);
                    return;
                }
                PromptManager.hideKeyboard(ModifyPassActivity.this);
                modifyPass(et_pass_text.getText().toString(),et_pass_new_sure_text.getText().toString());
            }
        });



    }


    /**
     * 修改密码
     * @param oldpass
     * @param newpass
     */
    private void modifyPass(String oldpass, String newpass) {
            try {
                    String  reqUrl = Constants.host+"user/set_pass?oldpass="+ MD5Util.md5Decode32(oldpass)+"&pass="+MD5Util.md5Decode32(newpass);
                    OkHttpUtils.get(ModifyPassActivity.this, reqUrl, new OkhttpCallback()
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
                        public Object parseNetworkResponse(JSONObject jsonObject) throws
                                Exception {
                            getOrderInfo(jsonObject);
                            return null;
                        }


                    });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
    }


    private void getOrderInfo(JSONObject job) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        et_pass_text.setText("");
                        et_pass_new_text.setText("");
                        et_pass_new_sure_text.setText("");
                        PromptManager.showMyToast("密码修改成功,请重新登录",ModifyPassActivity.this);
                        Intent intent = new Intent(ModifyPassActivity.this, MainActivity.class);//singleTask
                        intent.putExtra("isExit",true);
                        startActivity(intent);
                      //  finish();
                    }
                });

    }

}
