package com.peixueshi.crm.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.peixueshi.crm.MainActivity;
import com.peixueshi.crm.R;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.NewUserInfo;
import com.peixueshi.crm.bean.UserInfo;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.MD5Util;
import com.peixueshi.crm.utils.PromptManager;
import com.werb.permissionschecker.PermissionChecker;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.bt_user_login)
    Button bt_user_login;
    @BindView(R.id.login_et_phone)
    EditText login_et_phone;
    @BindView(R.id.login_et_pw)
    EditText login_et_pw;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
//        DaggerCommonComponent.builder()
//                .appComponent(appComponent)
//                .commonModule(new CommonModule())
//                .build()
//                .inject(LoginActivity.this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login;
    }


    @OnClick({R.id.bt_user_login, R.id.tv_forget_pw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_user_login:
                String phone = login_et_phone.getText().toString().trim();
                String pass = login_et_pw.getText().toString().trim();
                String passMd = MD5Util.md5Decode32(pass);
             //   initData(phone, passMd);
                Log.e("tag", "onViewClicked: "+phone+"=="+passMd );
                initData(phone, passMd);
                break;
            case R.id.tv_forget_pw:

                break;

        }
    }


    private boolean canSee = false;
    private boolean isAutoLogin = true;

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

//        StatusBarUtil.setColorDiff(LoginActivity.this,LoginActivity.this.getResources().getColor(R.color.transparent));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        String phone = EnjoyPreference.readString(getApplicationContext(), "user_phone");
        if (phone != null && phone.length() > 0) {
            login_et_phone.setText(phone);
        }
        checkPermission();
        login_et_pw.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()?????????????????????4????????????????????????????????????????????????
                Drawable drawable = login_et_pw.getCompoundDrawables()[2];
                //???????????????????????????????????????
                if (drawable == null)
                    return false;
                //???????????????????????????????????????
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > login_et_pw.getWidth()
                        - login_et_pw.getPaddingRight()
                        - drawable.getIntrinsicWidth()) {
                    if (canSee == false) {
                        //??????????????????????????????????????????
                        login_et_pw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        login_et_pw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_suo, 0, R.drawable.yanjing_pre, 0);
                        canSee = true;
                    } else {
                        //????????????????????????????????????
                        login_et_pw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        login_et_pw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_suo, 0, R.drawable.yanjing_nor, 0);
                        canSee = false;
                    }
                }

                return false;
            }
        });


        login_et_phone.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()?????????????????????4????????????????????????????????????????????????
                Drawable drawable = login_et_phone.getCompoundDrawables()[2];
                //???????????????????????????????????????
                if (drawable == null)
                    return false;
                //???????????????????????????????????????

                if (event.getX() > login_et_phone.getWidth()
                        - login_et_phone.getPaddingRight()
                        - drawable.getIntrinsicWidth()) {
                    login_et_phone.setText("");
                }

                return false;
            }
        });
       /* if(isAutoLogin){ //passMd5 = MD5Util.md5Decode32(pass);
            String pass = EnjoyPreference.readString(getApplicationContext(),"pass");
            if(pass != null && pass.length()>0){
                initData(phone,pass);
            }
        }*/
    }


    public void initDatas(String phone, String passMd5) {
        EnjoyPreference.saveString(getApplicationContext(), "acc_token", "");
        paddMd = passMd5;
        HashMap<String,String> keyMap = new HashMap<>();
        keyMap.put("phone",phone);
        keyMap.put("pass",passMd5);
        try {
            OkHttpUtils.newPost(LoginActivity.this, Constants.newhost+"login/login", keyMap,new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                    Log.e("?????????????????????????????????", "??????????????????");
                }

                @Override
                public void onFailure(String message) {
                    Log.e("tag", "onFailure: "+message );
                    Toast.makeText(LoginActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {

                }

                @Override
                public Object parseNetworkResponse(JSONObject jsonObject) throws
                        Exception {
//                            final byte[] Picture_bt = response.body().bytes();
//                            List<String> head = response.headers("Set-Cookie");
//                            Log.e("?????????????????????---->", "hc--->***:" + head.get(0));
//                            Log.e("?????????????????????---->", "hc--->***:" + head);
                    if (jsonObject.getInt("code") == 0) {
                        getNewUserInfo(jsonObject);
                    }

                    //  h5api_vcodeindex = getH5api_vcodeindex(head.get(0));

                    //return h5api_vcodeindex;
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    NewUserInfo.DataBean.StaffBean newinfos = null;

    public   NewUserInfo.DataBean.StaffBean getNewUserInfo(JSONObject job) throws Exception {

        //JSONObject object = job.getJSONObject("info");
        JSONObject data = job.getJSONObject("data");

        String userToken = data.getString("token");
        JSONObject jsoninfo = data.getJSONObject("staff");
        if (jsoninfo != null) {
            newinfos = JSONUtil.parser(jsoninfo, NewUserInfo.DataBean.StaffBean.class, 0);
        }

                /*info.setAcc_token(userToken);
                Constants.loginUserInfo = info;
                EnjoyPreference.saveString(getApplicationContext(),"user_phone",info.getEmp_phone());
                EnjoyPreference.saveString(getApplicationContext(),"pass",passMd5);
                EnjoyPreference.saveString(getApplicationContext(),"acc_token",userToken);*/

//                boolean isAutologin;
//                    if(isAutologin) {
//                        if(EnjoyPreference.readString(activity,"access_token") != null && EnjoyPreference.readString(activity,"access_token").length()>0){
//                            handleSignInResult(EnjoyPreference.readString(activity,"uid"),EnjoyPreference.readString(activity,"access_token"),true,"1000","");
//                        }
//                    }
/*
        Boolean is_new = job.getBoolean("is_new");
        Boolean need_mod_pass = job.getBoolean("need_mod_pass");*/
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
              /*  if (is_new){
                    Constants.isUpdatePass=true;
                }else {
                    Constants.isUpdatePass=false;
                }
                if (need_mod_pass){
                    Constants.weekPass=true;
                }else {
                    Constants.weekPass=false;
                }*/
                newinfos.setAcc_token(userToken);

                Constants.newloginUserInfo = newinfos;
                EnjoyPreference.saveString(getApplicationContext(), "user_phone", newinfos.getPhone());
                EnjoyPreference.saveString(getApplicationContext(), "pass", paddMd);
                EnjoyPreference.saveString(getApplicationContext(), "acc_token", userToken);
                EnjoyPreference.saveString(getApplicationContext(), "emp_id", newinfos.getId() + "");
                Toast.makeText(getApplicationContext(), "????????????" + newinfos.getName(), Toast.LENGTH_SHORT).show();

                int roleId = newinfos.getRoleId();

                if (roleId != 2 && roleId != 102) {
                    Constants.headMaster = false;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {//?????????
                    Constants.headMaster = true;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                finish();
            }
        });

        return newinfos;
    }

    String paddMd;

    public void initData(String phone, String passMd5) {
        paddMd = passMd5;
        try {
            OkHttpUtils.get(LoginActivity.this, Constants.host + "login/login?" + "phone=" + phone + "&" + "pass=" + passMd5+"&way=2", new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                    Log.e("?????????????????????????????????", "??????????????????");
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(LoginActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {

                }

                @Override
                public Object parseNetworkResponse(JSONObject jsonObject) throws
                        Exception {
//                            final byte[] Picture_bt = response.body().bytes();
//                            List<String> head = response.headers("Set-Cookie");
//                            Log.e("?????????????????????---->", "hc--->***:" + head.get(0));
//                            Log.e("?????????????????????---->", "hc--->***:" + head);
                    if (jsonObject.getInt("err") == 0) {
                        getUserInfo(jsonObject);
                    }
                    //  h5api_vcodeindex = getH5api_vcodeindex(head.get(0));

                    //return h5api_vcodeindex;
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    UserInfo.InfoBean infoBean = null;

    public UserInfo.InfoBean getUserInfo(JSONObject job) throws Exception {

               /* JSONObject object = job.getJSONObject("info");
                if (object != null) {
                    info =  JSONUtil.parser(object, UserInfo.class,0);

                }*/
        String userToken = job.getString("token");
        Boolean is_new = job.getBoolean("is_new");
        Boolean need_mod_pass = job.getBoolean("need_mod_pass");
        JSONObject jsoninfo = job.getJSONObject("info");
        if (jsoninfo != null) {
            infoBean = JSONUtil.parser(jsoninfo, UserInfo.InfoBean.class, 0);
        }
                /*info.setAcc_token(userToken);
                Constants.loginUserInfo = info;
                EnjoyPreference.saveString(getApplicationContext(),"user_phone",info.getEmp_phone());
                EnjoyPreference.saveString(getApplicationContext(),"pass",passMd5);
                EnjoyPreference.saveString(getApplicationContext(),"acc_token",userToken);*/

//                boolean isAutologin;
//                    if(isAutologin) {
//                        if(EnjoyPreference.readString(activity,"access_token") != null && EnjoyPreference.readString(activity,"access_token").length()>0){
//                            handleSignInResult(EnjoyPreference.readString(activity,"uid"),EnjoyPreference.readString(activity,"access_token"),true,"1000","");
//                        }
//                    }
//
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (is_new){
                    Constants.isUpdatePass=true;
                }else {
                    Constants.isUpdatePass=false;
                }
                if (need_mod_pass){
                    Constants.weekPass=true;
                }else {
                    Constants.weekPass=false;
                }
                infoBean.setAcc_token(userToken);
                Constants.loginUserInfo = infoBean;
                EnjoyPreference.saveString(getApplicationContext(), "user_phone", infoBean.getEmp_phone());
                EnjoyPreference.saveString(getApplicationContext(), "pass", paddMd);
                EnjoyPreference.saveString(getApplicationContext(), "acc_token", userToken);
                EnjoyPreference.saveString(getApplicationContext(), "emp_id", infoBean.getEmp_id() + "");
                EnjoyPreference.saveString(getApplicationContext(), "emp_team_id", infoBean.getEmp_team_id() + "");
                EnjoyPreference.saveString(getApplicationContext(), "emp_name", infoBean.getEmp_name() + "");
                Toast.makeText(getApplicationContext(), "????????????" + infoBean.getEmp_name(), Toast.LENGTH_SHORT).show();
                int roleId = infoBean.getEmp_role_id();
                if (roleId != 2 && roleId != 102) {
                    Constants.headMaster = false;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {//?????????
                    Constants.headMaster = true;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                finish();
            }
        });

        return infoBean;
    }


    //??????????????????
    public static final String TAG = "Unity";
    private PermissionChecker permissionChecker;
    static final String[] PERMISSIONS = new String[]{
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.CAMERA,
//            Manifest.permission.READ_SMS,
            //Manifest.permission.READ_CONTACTS //?????????
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,//??????
            Manifest.permission.CALL_PHONE,//?????????
            Manifest.permission.PROCESS_OUTGOING_CALLS,//?????????
            //  Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,//??????????????????
            Manifest.permission.READ_CALL_LOG,//??????????????????
            Manifest.permission.WRITE_CALL_LOG,//??????????????????
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.READ_SMS,
            Manifest.permission.CAMERA,
            /*          <uses-permission android:name="android.permission.READ_PHONE_NUMBERS" />
              <uses-permission android:name="android.permission.READ_SMS" />*/

    };
    static final String[] PERMISSIONS1 = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };
    static final String[] PERMISSIONS2 = new String[]{
            Manifest.permission.READ_PHONE_STATE,
    };
    static final String[] PERMISSIONS3 = new String[]{
            Manifest.permission.RECORD_AUDIO,
    };
    static final String[] PERMISSIONS4 = new String[]{
            Manifest.permission.CALL_PHONE,
    };
    static final String[] PERMISSIONS5 = new String[]{
            Manifest.permission.PROCESS_OUTGOING_CALLS,
    };
    static final String[] PERMISSIONS6 = new String[]{
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
    };
    static final String[] PERMISSIONS7 = new String[]{
            Manifest.permission.READ_CALL_LOG,
    };
    static final String[] PERMISSIONS8 = new String[]{
            Manifest.permission.WRITE_CALL_LOG,
    };
    static final String[] PERMISSIONS9 = new String[]{
            Manifest.permission.READ_PHONE_NUMBERS,
    };
    static final String[] PERMISSIONS10 = new String[]{
            Manifest.permission.READ_SMS,
    };
    static final String[] PERMISSIONS11 = new String[]{
            Manifest.permission.CAMERA,
    };

    public void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            writeExternalStorage();
            permissionChecker = new PermissionChecker(this);
           /* permissionChecker.setTitle(getString(R.string.check_info_title)); // ?????????????????????
            permissionChecker.setMessage(getString(R.string.check_info_message)); // ?????????????????????????????????????????????*/
            if (permissionChecker.isLackPermissions(PERMISSIONS)) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS1, PermissionChecker.PERMISSION_REQUEST_CODE);
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS2, PermissionChecker.PERMISSION_REQUEST_CODE);
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS3, PermissionChecker.PERMISSION_REQUEST_CODE);
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS4, PermissionChecker.PERMISSION_REQUEST_CODE);
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS5, PermissionChecker.PERMISSION_REQUEST_CODE);
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS7, PermissionChecker.PERMISSION_REQUEST_CODE);
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS8, PermissionChecker.PERMISSION_REQUEST_CODE);
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS9, PermissionChecker.PERMISSION_REQUEST_CODE);
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS10, PermissionChecker.PERMISSION_REQUEST_CODE);

                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS11, PermissionChecker.PERMISSION_REQUEST_CODE);
                }
                /*else if(ContextCompat.checkSelfPermission(this, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(this, PERMISSIONS6, PermissionChecker.PERMISSION_REQUEST_CODE);
                }*/
            } else {
                //  StartMainActivity();
            }

        } else {
            // StartMainActivity();
        }

    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
       /* if (permissionChecker.hasAllPermissionsGranted(grantResults)) {
            //?????????????????????????????????????????????
            StartMainActivity();
        }else{*/
        switch (requestCode) {
            case PermissionChecker.PERMISSION_REQUEST_CODE:
                if (!permissionChecker.isLackPermissions(PERMISSIONS)) {
                    //StartMainActivity();
                } else {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(this, PERMISSIONS1, PermissionChecker.PERMISSION_REQUEST_CODE);
                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(this, PERMISSIONS2, PermissionChecker.PERMISSION_REQUEST_CODE);
                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(this, PERMISSIONS3, PermissionChecker.PERMISSION_REQUEST_CODE);
                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(this, PERMISSIONS4, PermissionChecker.PERMISSION_REQUEST_CODE);
                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(this, PERMISSIONS5, PermissionChecker.PERMISSION_REQUEST_CODE);
                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(this, PERMISSIONS7, PermissionChecker.PERMISSION_REQUEST_CODE);
                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(this, PERMISSIONS8, PermissionChecker.PERMISSION_REQUEST_CODE);
                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(this, PERMISSIONS9, PermissionChecker.PERMISSION_REQUEST_CODE);
                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(this, PERMISSIONS10, PermissionChecker.PERMISSION_REQUEST_CODE);

                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(this, PERMISSIONS11, PermissionChecker.PERMISSION_REQUEST_CODE);
                        }
                        /*else if(ContextCompat.checkSelfPermission(this, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) == PackageManager.PERMISSION_DENIED){
                            ActivityCompat.requestPermissions(this, PERMISSIONS6, PermissionChecker.PERMISSION_REQUEST_CODE);
                        }*/
                        //??????????????????*/
                    } else {
                        showWarnDialog();
                    }
                }
                break;
        }
    }

    private void showWarnDialog() {
        View warn_view = View.inflate(this, R.layout.sina_sdk_refuse_permission_warn, null);
        TextView tv_confirm = warn_view.findViewById(R.id.tv_confirm);
        RelativeLayout rl_cancel = warn_view.findViewById(R.id.rl_cancel);
        rl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromptManager.closeCustomDialog();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromptManager.closeCustomDialog();
                if (permissionChecker.isLackPermissions(PERMISSIONS)) {
//                                permissionChecker.requestPermissions();//??????????????????
                    if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS1, PermissionChecker.PERMISSION_REQUEST_CODE);
                    } else if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS2, PermissionChecker.PERMISSION_REQUEST_CODE);
                    } else if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS3, PermissionChecker.PERMISSION_REQUEST_CODE);
                    } else if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS4, PermissionChecker.PERMISSION_REQUEST_CODE);
                    } else if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS5, PermissionChecker.PERMISSION_REQUEST_CODE);
                    } else if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS7, PermissionChecker.PERMISSION_REQUEST_CODE);
                    } else if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS8, PermissionChecker.PERMISSION_REQUEST_CODE);
                    } else if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS9, PermissionChecker.PERMISSION_REQUEST_CODE);
                    } else if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS10, PermissionChecker.PERMISSION_REQUEST_CODE);

                    } else if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS11, PermissionChecker.PERMISSION_REQUEST_CODE);
                    }
                }
            }
        });
        PromptManager.showCustomDialog(this, warn_view, Gravity.CENTER, Gravity.CENTER);
    }
}
