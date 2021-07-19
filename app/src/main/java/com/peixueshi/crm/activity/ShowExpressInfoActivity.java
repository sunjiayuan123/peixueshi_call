package com.peixueshi.crm.activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.peixueshi.crm.utils.JSONUtil;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;

public class ShowExpressInfoActivity extends BaseActivity{
    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_express)
    TextView tv_express;
    @BindView(R.id.et_user_name)
    TextView et_user_name;
    @BindView(R.id.et_user_phone)
    TextView et_user_phone;
    @BindView(R.id.et_user_address)
    TextView et_user_address;
    @BindView(R.id.et_user_express)
    TextView et_user_express;
    @BindView(R.id.tv_order_number)
    TextView tv_order_number;





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
        return R.layout.show_express;
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

        String billNo = getIntent().getStringExtra("bill_no");
        String newNo = billNo.substring(0,6)+"****"+billNo.substring(10,billNo.length());
        tv_order_number.setText(newNo);
        getExpressInfo(getIntent().getStringExtra("bill_no"));
    }



    private void getExpressInfo(String biilNo) {
            try {
                    String  reqUrl = Constants.host+"order/exp_list?bill_no="+ biilNo;
                    OkHttpUtils.get(ShowExpressInfoActivity.this, reqUrl, new OkhttpCallback()
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


    private void getOrderInfo(JSONObject job) throws Exception{

                Map<String,String> info = JSONUtil.getMap(job.getJSONObject("info"));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(info.get("ex_mail_type").equals("1")){
                            tv_express.setText("协议");
                        }else if(info.get("ex_mail_type").equals("2")){
                            tv_express.setText("收据");
                        }else if(info.get("ex_mail_type").equals("3")){
                            tv_express.setText("发票");
                        }else if(info.get("ex_mail_type").equals("4")){
                            tv_express.setText("教材");
                        }else if(info.get("ex_mail_type").equals("5")){
                            tv_express.setText("教材");
                        }else if(info.get("ex_mail_type").equals("6")){
                            tv_express.setText("习题册");
                        }
                        et_user_name.setText(info.get("ex_uname"));
                        et_user_phone.setText(info.get("ex_uphone"));
                        et_user_address.setText(info.get("ex_address"));
                        if(!info.get("ex_note").equals("")){
                            et_user_express.setText(info.get("ex_note"));
                        }


                    }
                });
    }

}
