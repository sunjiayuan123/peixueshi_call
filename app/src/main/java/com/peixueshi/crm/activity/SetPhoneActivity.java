package com.peixueshi.crm.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.peixueshi.crm.R;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.PromptManager;

import java.util.List;

import butterknife.BindView;

public class SetPhoneActivity extends BaseActivity {
    @BindView(R.id.et_pass_text)
    EditText et_pass_text;
    @BindView(R.id.ll_submit_order)
    LinearLayout ll_submit_order;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.et_fu_phone_text)
    EditText et_fu_phone_text;
    @BindView(R.id.tishi)
    TextView tishi;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_set_phone;
    }
    public static final String regExp = "^(1)\\d{10}$";
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        int simNumber = 0;
        SubscriptionManager sm = SubscriptionManager.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "no call phone permission", Toast.LENGTH_SHORT).show();
            return;
        }
        List<SubscriptionInfo> subs = sm.getActiveSubscriptionInfoList();
        if (subs == null) {
            Log.d(TAG, "checkDualSim: " + "no sim");
            PromptManager.showMyToast("no sim", this);
            return;
        }
        if (subs.size() > 1) {
            simNumber = 2;
            Log.d(TAG, "checkDualSim: " + "two sims");
        } else {
            simNumber = 1;
        }
        if (simNumber==2){
            tishi.setText("检测到双卡,请填写主号和副号");
        }else {
            tishi.setText("");
        }
        ll_submit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_pass_text.getText().toString();
                String s1 = et_fu_phone_text.getText().toString();

              /*  if (TextUtils.isEmpty(s)){
                    Toast.makeText(SetPhoneActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }else*/


                if (!TextUtils.isEmpty(s1)){
                    if (!s1.matches(regExp)){
                        Toast.makeText(SetPhoneActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        Log.e("tag", "onClick: "+s1 );
                        EnjoyPreference.saveString(getApplicationContext(), "calls_phone", s1+ "");
                        Toast.makeText(SetPhoneActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    EnjoyPreference.saveString(getApplicationContext(), "calls_phone", s1+ "");
                }

                if (!TextUtils.isEmpty(s)){
                    if (!s.matches(regExp)){
                        Toast.makeText(SetPhoneActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        EnjoyPreference.saveString(getApplicationContext(), "calls_zhu_phone", s+ "");
                        Toast.makeText(SetPhoneActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    EnjoyPreference.saveString(getApplicationContext(), "calls_zhu_phone", s+ "");
                }
                finish();

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String phone = EnjoyPreference.readString(this, "calls_phone");
        if (!TextUtils.isEmpty(phone)){
            et_fu_phone_text.setText(phone);
        }
        String zhuphone = EnjoyPreference.readString(this, "calls_zhu_phone");
        if (!TextUtils.isEmpty(zhuphone)){
            et_pass_text.setText(zhuphone);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String phone = EnjoyPreference.readString(this, "calls_phone");
        if (!TextUtils.isEmpty(phone)){
            et_fu_phone_text.setText(phone);
        }
        String zhuphone = EnjoyPreference.readString(this, "calls_zhu_phone");
        if (!TextUtils.isEmpty(zhuphone)){
            et_pass_text.setText(zhuphone);
        }
    }
}