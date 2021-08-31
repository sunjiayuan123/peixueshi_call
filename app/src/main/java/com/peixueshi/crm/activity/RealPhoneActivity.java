package com.peixueshi.crm.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.peixueshi.crm.R;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.Util;

import butterknife.BindView;
import butterknife.OnClick;

public class RealPhoneActivity extends BaseActivity {

    @BindView(R.id.lin_one)
    LinearLayout lin_one;
    @BindView(R.id.lin_two)
    LinearLayout lin_two;
    @BindView(R.id.test_one)
    TextView test_one;
    @BindView(R.id.test_two)
    TextView test_two;
    @BindView(R.id.test_three)
    TextView test_three;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.image_one)
    ImageView image_one;
    @BindView(R.id.image_two)
    ImageView image_two;
    @BindView(R.id.image_three)
    ImageView image_three;
    @BindView(R.id.call_types)
    TextView call_types;
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
        return R.layout.activity_real_phone;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        String call_type = EnjoyPreference.readString(RealPhoneActivity.this, "calls_type");
        if (!TextUtils.isEmpty(call_type)){

        }else {
            EnjoyPreference.saveString(RealPhoneActivity.this,"calls_type","3");
        }
        String calendarTime = Util.getCalendarTime();
        Log.e("tag", "initData: "+calendarTime );
    }

    @Override
    protected void onResume() {
        super.onResume();
        String call_type = EnjoyPreference.readString(RealPhoneActivity.this, "calls_type");
        if (!TextUtils.isEmpty(call_type)){
            if (call_type.equals("1")){
                Log.e("tag", "onResume: eeee" );
                test_two.setTextColor(getResources().getColor(R.color.font_666666));
                test_one.setTextColor(getResources().getColor(R.color.font_6882fd));
                test_three.setTextColor(getResources().getColor(R.color.font_666666));
                setImageBackground(image_one,image_two,image_three);
            }else if (call_type.equals("2")){
                Log.e("tag", "onResume: dddd" );
                test_two.setTextColor(getResources().getColor(R.color.font_6882fd));
                test_one.setTextColor(getResources().getColor(R.color.font_666666));
                test_three.setTextColor(getResources().getColor(R.color.font_666666));
                setImageBackground(image_two,image_one,image_three);
            }else if (call_type.equals("3")){
                test_three.setTextColor(getResources().getColor(R.color.font_6882fd));
                test_one.setTextColor(getResources().getColor(R.color.font_666666));
                test_two.setTextColor(getResources().getColor(R.color.font_666666));
                setImageBackground(image_three,image_one,image_two);
            }
        }else {
            Log.e("tag", "onResume: sss" );
            EnjoyPreference.saveString(RealPhoneActivity.this,"calls_type","3");
            test_three.setTextColor(getResources().getColor(R.color.font_6882fd));
            test_one.setTextColor(getResources().getColor(R.color.font_666666));
            test_two.setTextColor(getResources().getColor(R.color.font_666666));
            setImageBackground(image_three,image_one,image_two);
        }
      setType();
    }
   public void  setType(){
       String calls_type = EnjoyPreference.readString(RealPhoneActivity.this, "calls_type");
       if(calls_type.equals("1")){
           call_types.setText("当前拨号方式:A");
       }else if (calls_type.equals("2")){
           call_types.setText("当前拨号方式:A");
       }else if (calls_type.equals("3")){
           call_types.setText("当前拨号方式:B");
       }
   }
    @OnClick({R.id.lin_one,R.id.lin_two,R.id.iv_back,R.id.lin_three})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.lin_one:
                EnjoyPreference.saveString(RealPhoneActivity.this,"calls_type","1");

                test_two.setTextColor(getResources().getColor(R.color.font_666666));
                test_one.setTextColor(getResources().getColor(R.color.font_6882fd));
                test_three.setTextColor(getResources().getColor(R.color.font_666666));
                setImageBackground(image_one,image_two,image_three);
                setType();
                break;
            case R.id.lin_two:
                EnjoyPreference.saveString(RealPhoneActivity.this,"calls_type","2");
                test_two.setTextColor(getResources().getColor(R.color.font_6882fd));
                test_one.setTextColor(getResources().getColor(R.color.font_666666));
                test_three.setTextColor(getResources().getColor(R.color.font_666666));
              //  EnjoyPreference.saveString(RealPhoneActivity.this, "currentDay", "");
              //  EnjoyPreference.saveString(RealPhoneActivity.this, "xnum","");
                setImageBackground(image_two,image_one,image_three);
                setType();
                break;
            case R.id.lin_three:
                EnjoyPreference.saveString(RealPhoneActivity.this,"calls_type","3");
                test_three.setTextColor(getResources().getColor(R.color.font_6882fd));
                test_one.setTextColor(getResources().getColor(R.color.font_666666));
                test_two.setTextColor(getResources().getColor(R.color.font_666666));
             //   EnjoyPreference.saveString(RealPhoneActivity.this, "currentDay", "");
             //   EnjoyPreference.saveString(RealPhoneActivity.this, "xnum","");
                setImageBackground(image_three,image_one,image_two);
                setType();

                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
    public void setImageBackground(ImageView image_one,ImageView image_two, ImageView image_three){
        image_one.setBackground(getResources().getDrawable(R.drawable.check_yes));
        image_two.setBackground(getResources().getDrawable(R.drawable.check_no));
        image_three.setBackground(getResources().getDrawable(R.drawable.check_no));
    }
}