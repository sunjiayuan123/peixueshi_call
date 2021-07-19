package com.peixueshi.crm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;

import org.json.JSONObject;


public class ScanSureActivity extends AppCompatActivity {

    public  String uuid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_sure);
        uuid = getIntent().getStringExtra("uuid");
        initView();
    }

    private void initView() {
       TextView tv_sure_login = findViewById(R.id.tv_sure_login);
       TextView tv_cancle_login = findViewById(R.id.tv_cancle_login);
        TextView tv_cancle = findViewById(R.id.tv_cancle_login);
        ImageView iv_cancle = findViewById(R.id.iv_cancle);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_cancle_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_sure_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCodeIsLogin(uuid);
            }
        });
    }


    private void requestCodeIsLogin(String uuid) {
        try {
            String  reqUrl = Constants.host+"login/qr_login_cfmd?uuid="+uuid;
           /* HashMap<String,String> map = new HashMap<>();
            map.put("uuid",uuid);*/
            OkHttpUtils.get(ScanSureActivity.this, reqUrl,new OkhttpCallback()
            {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(ScanSureActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject object) throws
                        Exception {
                    finish();
                    return null;
                }


            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}
