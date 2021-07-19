package com.peixueshi.crm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.peixueshi.crm.fragment.OrderWangkeFragment;
import com.peixueshi.crm.fragment.OrderXiezhuFragment;
import com.peixueshi.crm.fragment.OrderXueliFragment;
import com.peixueshi.crm.fragment.OrderZhiquFragment;
import com.peixueshi.crm.ui.adapter.PopSelectRecycleAdapter;
import com.peixueshi.crm.ui.adapter.PopSelectXiezhuRecycleAdapter;
import com.peixueshi.crm.ui.adapter.PopSelectXueliRecycleAdapter;
import com.peixueshi.crm.ui.adapter.PopSelectZhiquRecycleAdapter;
import com.peixueshi.crm.utils.ActivityUtils;
import com.peixueshi.crm.utils.PromptManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class OrderActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.frameLayout_wangke_group)
    FrameLayout frameLayout_wangke_group;
    @BindView(R.id.rg_wangke_leixing)
    RadioGroup mRadioGroup;
    @BindView(R.id.rg_pay_type)
    RadioGroup rg_pay_type;
    @BindView(R.id.rb_zhifubao)
    RadioButton rb_zhifubao;
    @BindView(R.id.rg_group_call_mute)
    RadioButton rg_group_call_mute;
    @BindView(R.id.rb_weixin)
    RadioButton rb_weixin;
    @BindView(R.id.rg_pay_leixing_detail)
    RadioGroup rg_pay_leixing_detail;
    @BindView(R.id.rb_quankuan)
    RadioButton rb_quankuan;
    @BindView(R.id.rb_dingjin)
    RadioButton rb_dingjin;
    @BindView(R.id.rb_weikuan)
    RadioButton rb_weikuan;

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.rv_select)
    RecyclerView rv_select;

    @BindView(R.id.rv_select_xueli)
    RecyclerView rv_select_xueli;

    @BindView(R.id.rv_select_xiezhu)
    RecyclerView rv_select_xiezhu;
    @BindView(R.id.rv_select_zhiqu)
    RecyclerView rv_select_zhiqu;

    @BindView(R.id.et_count_money)
    EditText et_count_money;
    @BindView(R.id.tv_clear)
    TextView tv_clear;
    @BindView(R.id.tv_clear_xueli)
    TextView tv_clear_xueli;

    @BindView(R.id.tv_clear_xiezhu)
    TextView tv_clear_xiezhu;
    @BindView(R.id.tv_clear_zhiqu)
    TextView tv_clear_zhiqu;

    @BindView(R.id.ll_wangke_include)
    LinearLayout ll_wangke_include;

    @BindView(R.id.ll_xueli_include)
    LinearLayout ll_xueli_include;

    @BindView(R.id.ll_xiezhu_include)
    LinearLayout ll_xiezhu_include;
    @BindView(R.id.ll_zhiqu_include)
    LinearLayout ll_zhiqu_include;
    @BindView(R.id.ll_submit_order)
    LinearLayout ll_submit_order;
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;

    @BindView(R.id.et_mother_order_id)
    EditText et_mother_order_id;
    @BindView(R.id.et_beizhu)
    EditText et_beizhu;

    OrderWangkeFragment orderWangkeFragment = new OrderWangkeFragment();
    OrderXueliFragment orderXueliFragment = new OrderXueliFragment();
    OrderXiezhuFragment orderXiezhuFragment = new OrderXiezhuFragment();
    OrderZhiquFragment orderZhiquFragment = new OrderZhiquFragment();


    public static List<Map<String, String>> selectKemu = new ArrayList<>();//网课
    public static List<Map<String, String>> selectXueli = new ArrayList<>();//学历
    public static List<Map<String, String>> selectXiezhu = new ArrayList<>();//协助报名
    public static List<Map<String, String>> selectZhiqu = new ArrayList<>();//直取

    int payType = 2;
    int type;
    //接收子Fragment传递信息
    public void SendMessageValue(Map<String,String> mapInfo,int type) {
      //  ll_wangke_include.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(et_phone_number.getText().toString())) {
            if(et_phone_number.getText().toString().length() != 11){
                PromptManager.showMyToast("请确认手机号是否正确",OrderActivity.this);
                return;
            }else {
                getPhoneInfo(et_phone_number.getText().toString(),OrderActivity.this,mapInfo,type);//检测手机号在库存中是否存在
            }
        }else{
            PromptManager.showMyToast("请先输入手机号",OrderActivity.this);
            return;
        }


    }


    public  void getPhoneInfo(String phone, Activity activity,Map<String,String> mapInfo,int type) {
        try {
            String  reqUrl = Constants.host+"work/p_get?phone="+phone;
            OkHttpUtils.get(activity, reqUrl,new OkhttpCallback()
            {

                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(activity, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject jsonObject) throws
                        Exception {
                    getPhoneHistory(jsonObject,activity,mapInfo,type);
                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }


    private int zid;
    private  void getPhoneHistory(JSONObject job,Activity activity,Map<String,String> mapInfo,int type) {

                mapInfo.put("c_pay_price","0");//设置默认价格为0
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int fage = 0;
                        try {
                            fage = job.getInt("fage");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //0 无， 1 库存， 2 首资， 3 班主任 4 已成单
                        if(fage == 0){//无库存,弹框
                            View view = View.inflate(activity, R.layout.create_chance_dialog,null);
                            view.findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PromptManager.closeCustomDialog();
                                    Intent intent = new Intent(activity,MineCreateChanceActivity.class);
                                    activity.startActivity(intent);
                                }
                            });
                            view.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PromptManager.closeCustomDialog();
                                }
                            });
                            view.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PromptManager.closeCustomDialog();
                                }
                            });
                            PromptManager.showCustomDialog(activity,view, Gravity.CENTER,Gravity.CENTER);
                        }else {
                            try {

                                if(fage == 1){
                                     zid =  job.getJSONObject("using").getInt("usi_to_uid");
                                }else if(fage == 2){
                                    if(job.getJSONObject("work_first").has("wf_uid") && job.getJSONObject("work_first").getInt("wf_uid") != 0){
                                        zid =  job.getJSONObject("work_first").getInt("wf_uid");
                                    }else if(job.getJSONObject("firl").has("net_uid")){
                                        zid =  job.getJSONObject("firl").getInt("net_uid");
                                    }
                                }else if(fage == 3 || fage == 4){
                                    zid =  job.getJSONObject("user").getInt("emp_id");
                                }
                                String name = job.getJSONObject("user").getString("emp_name");
                                if(zid == 0){//该手机号未被认领，请先认领
                                   showDialogMessage(activity,"该手机号尚未被认领，请先搜索认领。",true);
                                    return;
                                }else if(Constants.loginUserInfo != null  && zid != Constants.loginUserInfo.emp_id){
                                    showDialogMessage(activity,"该手机号在"+name+"坐席名下,下单后业绩属于该坐席",false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(type == 0){
                                mapInfo.put("u_phone", et_phone_number.getText().toString());
                                selectKemu.add(mapInfo);
                                ll_wangke_include.setVisibility(View.VISIBLE);
                                updateSelect();

                            }else if(type == 1){
                                mapInfo.put("u_phone",et_phone_number.getText().toString());
                                ll_xueli_include.setVisibility(View.VISIBLE);
                                selectXueli.add(mapInfo);
                                updateSelectXueli();

                            }else if(type == 2){
                                mapInfo.put("u_phone", et_phone_number.getText().toString());
                                ll_xiezhu_include.setVisibility(View.VISIBLE);
                                selectXiezhu.add(mapInfo);
                                updateSelectBaoming();

                            }else if(type == 3){
                                mapInfo.put("u_phone", et_phone_number.getText().toString());
                                ll_zhiqu_include.setVisibility(View.VISIBLE);
                                selectZhiqu.add(mapInfo);
                                updateSelectZhiqu();
                            }
                        }

                    }
                });


    }

    public void showDialogMessage(Activity activity,String msg,boolean isOpen){
        View view = View.inflate(activity, R.layout.create_chance_dialog,null);
        TextView textView = view.findViewById(R.id.tv_to_bind_warn);
        textView.setText(msg);
        view.findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptManager.closeCustomDialog();
                if(isOpen == true){
                    Intent intent = new Intent(activity,MineCreateChanceActivity.class);
                    activity.startActivity(intent);
                }
            }
        });
        view.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptManager.closeCustomDialog();
            }
        });
        view.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptManager.closeCustomDialog();
            }
        });
        PromptManager.showCustomDialog(activity,view, Gravity.CENTER,Gravity.CENTER);
    }
    //网课
    public static int countWangke = 0;
    private void updateSelect() {
        countWangke = 0;
        popSelectRecycleAdapter.setData(selectKemu);
        popSelectRecycleAdapter.notifyDataSetChanged();
        for(Map<String, String> select:selectKemu){
//            countWangke = countWangke + Integer.valueOf(select.get("c_price"));
            countWangke = countWangke + Integer.valueOf(select.get("c_pay_price"));//实际支付金额，默认为0
        }

        int countMoney = countZhiqu+countXueli+countWangke+countXiezhu;
        et_count_money.setText(countMoney+"");
    }

    //学历
    public static int countXueli = 0;
    private void updateSelectXueli() {
        countXueli = 0;
        popSelectXueliRecycleAdapter.setData(selectXueli);
        popSelectXueliRecycleAdapter.notifyDataSetChanged();

        for(Map<String, String> select:selectXueli){
            countXueli = countXueli + Integer.valueOf(select.get("c_pay_price"));
        }
        int countMoney = countZhiqu+countXueli+countWangke+countXiezhu;
        et_count_money.setText(countMoney+"");
    }

    //协助报名
    public static int countXiezhu = 0;
    private void updateSelectBaoming() {
        countXiezhu = 0;
        popSelectXiezhuRecycleAdapter.setData(selectXiezhu);
        popSelectXiezhuRecycleAdapter.notifyDataSetChanged();
        for(Map<String, String> select:selectXiezhu){
            countXiezhu = countXiezhu + Integer.valueOf(select.get("c_pay_price"));
        }
        int countMoney = countZhiqu+countXueli+countWangke+countXiezhu;
        et_count_money.setText(countMoney+"");
    }
    //直取
    public static int countZhiqu = 0;
    private void updateSelectZhiqu() {
        countZhiqu = 0;
        popSelectZhiquRecycleAdapter.setData(selectZhiqu);
        popSelectZhiquRecycleAdapter.notifyDataSetChanged();

        for(Map<String, String> select:selectZhiqu){
            countZhiqu = countZhiqu + Integer.valueOf(select.get("c_pay_price"));
        }
        int countMoney = countZhiqu+countXueli+countWangke+countXiezhu;
        et_count_money.setText(countMoney+"");
    }


    //定义一个回调接口
    public interface CallBackValue{
        public void SendMessageValue(Map<String, String> mapInfo);
    }
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    PopSelectRecycleAdapter popSelectRecycleAdapter;
    PopSelectXueliRecycleAdapter popSelectXueliRecycleAdapter;
    PopSelectXiezhuRecycleAdapter popSelectXiezhuRecycleAdapter;
    PopSelectZhiquRecycleAdapter popSelectZhiquRecycleAdapter;
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.order_activity;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if(!TextUtils.isEmpty(getIntent().getStringExtra("phone"))){
            et_phone_number.setText(getIntent().getStringExtra("phone"));
        }

        ll_submit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_phone_number.getText().toString().length() == 0 || TextUtils.isEmpty(et_phone_number.getText().toString().trim())){
                    PromptManager.showMyToast("请输入手机号",OrderActivity.this);
                    return;
                }
                if(TextUtils.isEmpty(et_count_money.getText().toString().trim()) || et_count_money.getText().toString().equals("0")){
                    PromptManager.showMyToast("总金额不能为0",OrderActivity.this);
                    return;
                }

                if(selectKemu.size() == 0 && selectXueli.size() == 0 && selectXiezhu.size() ==0 &&  selectZhiqu.size() == 0){
                    PromptManager.showMyToast("请先选择项目和填写资料",OrderActivity.this);
                    return;
                }
                if(et_mother_order_id.getText().toString().trim() != null && et_mother_order_id.getText().toString().length()>0){//有订单号了，不用取订单,直接走添加接口
                    if(selectKemu.size()>0){
                        for(int i=0;i<selectKemu.size();i++){
                            Map<String,String> mapInfo = selectKemu.get(i);
//                            int price = Integer.valueOf(mapInfo.get("c_price"))*100;
                            int price = Integer.valueOf(mapInfo.get("c_pay_price"))*100;
                            wangkeOrder(et_mother_order_id.getText().toString(),mapInfo.get("p_id"),mapInfo.get("p_name"),mapInfo.get("c_id"),mapInfo.get("c_name"),price+"");
                        }

                    } if(selectXueli.size() >0){
                        for(int i=0;i<selectXueli.size();i++) {
                            Map<String, String> mapInfo = selectXueli.get(i);
                            int price = Integer.valueOf(mapInfo.get("c_pay_price"))*100;
                            xueliOrder(et_mother_order_id.getText().toString(),mapInfo.get("u_name"),mapInfo.get("u_number"),mapInfo.get("u_add"),mapInfo.get("s_id"),mapInfo.get("s_name"),mapInfo.get("m_id"),mapInfo.get("m_name"),mapInfo.get("m_status"),mapInfo.get("u_type"),price+"");
                        }
                    }
                   if(selectXiezhu.size() >0){
                        for(int i=0;i<selectXiezhu.size();i++) {
                            Map<String, String> mapInfo = selectXiezhu.get(i);
                            int price = Integer.valueOf(mapInfo.get("c_pay_price"))*100;
                            xieZhuOrder(et_mother_order_id.getText().toString(),"证书名称",mapInfo.get("p_id"),mapInfo.get("p_name"),mapInfo.get("u_name"),mapInfo.get("u_number"),mapInfo.get("u_add"),mapInfo.get("u_city"),price+"");
                        }

                    } if(selectZhiqu.size() >0){
                        for(int i=0;i<selectZhiqu.size();i++) {
                            Map<String, String> mapInfo = selectZhiqu.get(i);
                            int price = Integer.valueOf(mapInfo.get("c_pay_price"))*100;
                            zhiquOrder(et_mother_order_id.getText().toString(),mapInfo.get("c_name"),mapInfo.get("u_name"),mapInfo.get("u_number"),mapInfo.get("u_add"),price+"");
                        }

                    }
                }else{
                    int price = Integer.valueOf(et_count_money.getText().toString())*100;
                    getOrderIdAndSubmit(et_mother_order_id.getText().toString(),et_phone_number.getText().toString(),type+"",price+"",payType+"",et_beizhu.getText().toString(),zid+"");
                }
            }
        });


        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_select.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        rv_select.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_select_xueli.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        rv_select_xueli.setLayoutManager(layoutManager2);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_select_xiezhu.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        rv_select_xiezhu.setLayoutManager(layoutManager3);

        LinearLayoutManager layoutManager4 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_select_zhiqu.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        rv_select_zhiqu.setLayoutManager(layoutManager4);




        //网课
        popSelectRecycleAdapter = new PopSelectRecycleAdapter(getApplicationContext(),selectKemu,et_count_money);
        rv_select.setAdapter(popSelectRecycleAdapter);

        //学历
        popSelectXueliRecycleAdapter = new PopSelectXueliRecycleAdapter(getApplicationContext(),selectXueli,et_count_money);
        rv_select_xueli.setAdapter(popSelectXueliRecycleAdapter);

        //协助报名
        popSelectXiezhuRecycleAdapter = new PopSelectXiezhuRecycleAdapter(getApplicationContext(),selectXiezhu,et_count_money);
        rv_select_xiezhu.setAdapter(popSelectXiezhuRecycleAdapter);

        //直取
        popSelectZhiquRecycleAdapter = new PopSelectZhiquRecycleAdapter(getApplicationContext(),selectZhiqu,et_count_money);
        rv_select_zhiqu.setAdapter(popSelectZhiquRecycleAdapter);

        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioGroup.check(R.id.rb_wangke);
        rg_pay_leixing_detail.check(R.id.rb_quankuan);
        rg_pay_type.check(R.id.rb_zhifubao);
        rg_pay_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == rb_zhifubao.getId()){
                    payType = 2;
                }else if (checkedId==rb_weixin.getId()){
                    payType = 1;
                }else{
                    payType = 3;
                }
 /*<el-radio :label="2">支付宝</el-radio>
                <el-radio :label="1">微信</el-radio>
                <el-radio :label="4">聚合</el-radio>
                <el-radio :label="3">其他</el-radio>
                <el-radio :label="5">扫呗</el-radio>  */
            }
        });
        rg_pay_leixing_detail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == rb_quankuan.getId()){
                    type = 0;//全款
                    et_mother_order_id.setVisibility(View.GONE);
                }else if(checkedId == rb_dingjin.getId()){
                    type = 2;//定金
                    et_mother_order_id.setVisibility(View.VISIBLE);
                }else{
                    type = 1;//尾款
                    et_mother_order_id.setVisibility(View.VISIBLE);
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectKemu.clear();
                countWangke = 0;
                ll_wangke_include.setVisibility(View.GONE);
                updateSelect();
            }
        });

        tv_clear_xueli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectXueli.clear();
                countXueli = 0;
                ll_xueli_include.setVisibility(View.GONE);
                updateSelectXueli();
            }
        });

        tv_clear_xiezhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectXiezhu.clear();
                countXiezhu = 0;
                ll_xiezhu_include.setVisibility(View.GONE);
                updateSelectBaoming();
            }
        });
        tv_clear_zhiqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectZhiqu.clear();
                countZhiqu = 0;
                ll_zhiqu_include.setVisibility(View.GONE);
                updateSelectZhiqu();
            }
        });
    }


    /**
     * 添加网课
     * @param oid 账单ID
     * @param pid 	项目ID
     * @param pname 项目名称
     * @param cid 班型ID
     * @param cname 	班型名称
     * @param price 	出售价格（ 单位/分 ）
     */
    private void wangkeOrder(String oid,String pid,String pname,String cid,String cname,String price) {
        try {
            String url = null;
            url = Constants.host+"order/add_class?oid="+oid+"&pid="+pid+"&pname="+pname+"&cid="+cid+"&cname="+cname+"&price="+price;
            OkHttpUtils.get(OrderActivity.this, url, new OkhttpCallback()
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
                public Object parseNetworkResponse(JSONObject job) throws
                        Exception {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PromptManager.showMyToast("添加网课成功",OrderActivity.this);
                                    selectKemu.clear();
                                    countWangke = 0;
                                    ll_wangke_include.setVisibility(View.GONE);
                                    updateSelect();
                                    finish();
                                }
                            });

                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    /**
     * 添加协助报名
     * @param oid 账单ID
     * @param name 证书名称
     * @param pid 项目ID
     * @param pname 项目名字
     * @param uname 学员姓名
     * @param number 学员身份证号
     * @param domicile 学员户籍地址
     * @param test_add 	取证地
     * @param price 出售价格（ 单位/分 ）
     */
    private void xieZhuOrder(String oid,String name,String pid,String pname,String uname,String number,String domicile,String test_add,String price) {
        try {
            String url = null;
            url = Constants.host+"order/add_sign?oid="+oid+"&name="+name+"&pid="+pid+"&pname="+pname+"&uname="+uname+"&number_id="+number+"&domicile="+domicile+"&test_add="+test_add+"&price="+price;
            OkHttpUtils.get(OrderActivity.this, url, new OkhttpCallback()
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
                public Object parseNetworkResponse(JSONObject job) throws
                        Exception {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PromptManager.showMyToast("添加成功",OrderActivity.this);
                                selectXiezhu.clear();
                                countXiezhu = 0;
                                ll_xiezhu_include.setVisibility(View.GONE);
                                updateSelectBaoming();
                                finish();
                            }
                        });

                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 添加直取证书
     * @param oid 账单ID
     * @param name 证书名称
     * @param uname 学员姓名
     * @param number_id 	学员身份证号
     * @param domicile 学员户籍地址
     * @param price 出售价格（ 单位/分 ）
     */
    private void zhiquOrder(String oid,String name,String uname,String number_id,String domicile,String price) {
        try {
            String url = null;
            url = Constants.host+"order/add_certi?oid="+oid+"&name="+name+"&uname="+uname+"&number_id="+number_id+"&domicile="+domicile+"&price="+price;
            OkHttpUtils.get(OrderActivity.this, url, new OkhttpCallback()
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
                public Object parseNetworkResponse(JSONObject job) throws
                        Exception {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PromptManager.showMyToast("添加成功",OrderActivity.this);
                                selectZhiqu.clear();
                                countZhiqu = 0;
                                ll_zhiqu_include.setVisibility(View.GONE);
                                updateSelectZhiqu();
                                finish();
                            }
                        });

                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 添加学历
     * @param oid 账单ID
     * @param uname 学员姓名
     * @param number 学员身份证号
     * @param domicile 学员户籍地址
     * @param sid 学校ID
     * @param sname 学校名称
     * @param ssion_id 专业ID
     * @param ssion_name 专业名称
     * @param sc_number 学历 1 中专 2 大专 3 本科
     * @param test_type 考试类型 1 网教自己考，2 网教直取，3 成考
     * @param price 出售价格（ 单位/分 ）
     */

    private void xueliOrder(String oid,String uname,String number,String domicile,String sid,String sname,String ssion_id,String ssion_name,String sc_number,String test_type,String price) {
        try {
            String url = null;
            url = Constants.host+"order/add_school?oid="+oid+"&uname="+uname+"&number_id="+number+"&domicile="+domicile+"&sid="+sid+"&sname="+sname+"&ssion_id="+ssion_id+"&ssion_name="+ssion_name+"&test_type="+test_type+"&sc_number="+sc_number+"&price="+price;
            OkHttpUtils.get(OrderActivity.this, url, new OkhttpCallback()
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
                public Object parseNetworkResponse(JSONObject job) throws
                        Exception {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PromptManager.showMyToast("添加学历成功",OrderActivity.this);
                                selectXueli.clear();
                                countXueli = 0;
                                ll_xueli_include.setVisibility(View.GONE);
                                updateSelectXueli();
                                finish();
                            }
                        });

                    return null;
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    private void getOrderIdAndSubmit(String mother,String phone,String type,String real,String pay_type,String note,String zid) {//手机号在坐席下的id
            try {
                String url = null;
                    url = Constants.host+"order/place?mother="+mother+"&phone="+phone+"&type="+type+"&real="+real+"&pay_type="+pay_type+"&note="+note+"&uid="+zid;
                    OkHttpUtils.get(OrderActivity.this, url, new OkhttpCallback()
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


    private void getOrderInfo(JSONObject job) throws Exception {
               String billNumber = job.getString("id");
                if (billNumber != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(selectKemu.size()>0){
                                for(int i=0;i<selectKemu.size();i++){
                                    Map<String,String> mapInfo = selectKemu.get(i);
                                    int price = Integer.valueOf(mapInfo.get("c_pay_price"))*100;
                                    String pname = "";
                                    if(mapInfo.containsKey("p_name")){
                                        pname = mapInfo.get("p_name");
                                    }else{
                                        pname = mapInfo.get("pr_name");
                                    }
                                    String p_id = "";
                                    if(mapInfo.containsKey("p_id")){
                                        p_id = mapInfo.get("p_id");
                                    }else{
                                        p_id = mapInfo.get("pr_id");
                                    }

                                    wangkeOrder(billNumber,p_id,pname,mapInfo.get("c_id"),mapInfo.get("c_name"),price+"");
                                }

                            }
                            if(selectXueli.size() >0){
                                for(int i=0;i<selectXueli.size();i++) {
                                    Map<String, String> mapInfo = selectXueli.get(i);
//                                    int price = Integer.valueOf(mapInfo.get("m_zprice"))*100;
                                    int price = Integer.valueOf(mapInfo.get("c_pay_price"))*100;
                                    xueliOrder(billNumber,mapInfo.get("u_name"),mapInfo.get("u_number"),mapInfo.get("u_add"),mapInfo.get("s_id"),mapInfo.get("s_name"),mapInfo.get("m_id"),mapInfo.get("m_name"),mapInfo.get("m_status"),mapInfo.get("u_type"),price+"");
                                }
                            }
                             if(selectXiezhu.size() >0){
                                for(int i=0;i<selectXiezhu.size();i++) {
                                    Map<String, String> mapInfo = selectXiezhu.get(i);
//                                    int price = Integer.valueOf(mapInfo.get("c_price"))*100;
                                    int price = Integer.valueOf(mapInfo.get("c_pay_price"))*100;
                                    String pname = "";
                                    if(mapInfo.containsKey("p_name")){
                                        pname = mapInfo.get("p_name");
                                    }else{
                                        pname = mapInfo.get("pr_name");
                                    }

                                    String p_id = "";
                                    if(mapInfo.containsKey("p_id")){
                                        p_id = mapInfo.get("p_id");
                                    }else{
                                        p_id = mapInfo.get("pr_id");
                                    }
                                    xieZhuOrder(billNumber,"证书名称",p_id,pname,mapInfo.get("u_name"),mapInfo.get("u_number"),mapInfo.get("u_add"),mapInfo.get("u_city"),price+"");
                                }

                            }
                             if(selectZhiqu.size() >0){
                                for(int i=0;i<selectZhiqu.size();i++) {
                                    Map<String, String> mapInfo = selectZhiqu.get(i);
                                    int price = Integer.valueOf(mapInfo.get("c_pay_price"))*100;
                                    zhiquOrder(billNumber,mapInfo.get("c_name"),mapInfo.get("u_name"),mapInfo.get("u_number"),mapInfo.get("u_add"),price+"");
                                }

                            }
                        }
                    });
                }

    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_wangke:
                ActivityUtils.addOrShowFragmentToActivity(getSupportFragmentManager(),
                        orderWangkeFragment, R.id.frameLayout_wangke_group);
                break;
            case R.id.rb_xueli:
                ActivityUtils.addOrShowFragmentToActivity(getSupportFragmentManager(),
                        orderXueliFragment, R.id.frameLayout_wangke_group);
                break;
            case R.id.rb_xiezhu:
                ActivityUtils.addOrShowFragmentToActivity(getSupportFragmentManager(),
                        orderXiezhuFragment, R.id.frameLayout_wangke_group);
                break;
            case R.id.rb_zhiqu:
                ActivityUtils.addOrShowFragmentToActivity(getSupportFragmentManager(),
                        orderZhiquFragment, R.id.frameLayout_wangke_group);
                break;
            default:
        }
    }
}
