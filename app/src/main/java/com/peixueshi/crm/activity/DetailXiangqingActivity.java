package com.peixueshi.crm.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.peixueshi.crm.R;
import com.peixueshi.crm.bean.ZiXunUserInfo;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.utils.Util;

import butterknife.BindView;

public class DetailXiangqingActivity extends BaseActivity {

    @BindView(R.id.tv_xiangmu_text)
    TextView tv_xiangmu_text;
    @BindView(R.id.tv_phone_num)
    TextView tv_phone_num;
    @BindView(R.id.tv_count)
    TextView tv_count;
   /* @BindView(R.id.tv_money_detail)
    EditText tv_money_detail;*/

    @BindView(R.id.tv_yixiang_text)
    TextView tv_yixiang_text;
    @BindView(R.id.tv_biezhu_text)
    TextView tv_biezhu_text;
    @BindView(R.id.tv_creat_time)
    TextView tv_creat_time;
    @BindView(R.id.iv_back)
    ImageView iv_back;


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
        return R.layout.xiangqing_activity;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Intent intent = getIntent();
        ZiXunUserInfo ziXunUserInfo= (ZiXunUserInfo)intent.getSerializableExtra("info");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       if(ziXunUserInfo.isShouZi()){
           tv_xiangmu_text.setText(ziXunUserInfo.getWf_p_name());
           tv_phone_num.setText(ziXunUserInfo.getWf_phone());
           tv_count.setText(ziXunUserInfo.getWf_c_count()+"次");
           String text ="";
           int weight = Integer.valueOf(ziXunUserInfo.getWf_weight());

           String stateInfo = "";
           int state = Integer.valueOf(ziXunUserInfo.getWf_state());
           if(weight == 0){//	权重 0 C 1 B 2 A
               text = "C";
           }else if(weight == 1){
               text = "B";
           }else if(weight == 2){
               text = "A";
           }
           tv_yixiang_text.setText(text);
           Log.e("tag", "initData: "+ziXunUserInfo.getWf_u_note() );
           tv_biezhu_text.setText(ziXunUserInfo.getWf_u_note());
           tv_creat_time.setText(Util.stampToDate(ziXunUserInfo.getWf_get_at()));


       }else{
           tv_xiangmu_text.setText(ziXunUserInfo.getWp_pname());
           tv_phone_num.setText(ziXunUserInfo.getWp_phone());
           tv_count.setText(ziXunUserInfo.getWp_call_count()+"次");
           String text ="";
           int weight = Integer.valueOf(ziXunUserInfo.getWp_weight());

           String stateInfo = "";
           int state = Integer.valueOf(ziXunUserInfo.getWp_state());
           if(weight == 0){//	权重 0 C 1 B 2 A
               text = "C";
           }else if(weight == 1){
               text = "B";
           }else if(weight == 2){
               text = "A";
           }
           tv_yixiang_text.setText(text);
           Log.e("tag", "initDatas: "+ziXunUserInfo.getWp_u_note() );
           tv_biezhu_text.setText(ziXunUserInfo.getWp_u_note());
           tv_creat_time.setText(Util.stampToDate(ziXunUserInfo.getWp_at()));
           /*if(state == 0){

           }else if(state == 1){

           }else if(state == 2){

           }else if(state == 3){

           }*/

       }
    }

}
