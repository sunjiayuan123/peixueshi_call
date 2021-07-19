package com.peixueshi.crm.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peixueshi.crm.R;
import com.peixueshi.crm.activity.AddExpressActivity;
import com.peixueshi.crm.activity.ShowExpressInfoActivity;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.OrderDetail;
import com.peixueshi.crm.bean.OrderUserInfo;
import com.peixueshi.crm.ui.widget.ExpandableListView;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.MatrixToImageWriterWithLogo;
import com.peixueshi.crm.utils.PromptManager;
import com.peixueshi.crm.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserDataOrderAdapter extends BaseExpandableListAdapter {

    private boolean isShouZi = true;
    private List<OrderUserInfo> mUserList;
    private ExpandableListView expandableListView;
    private int screenWidth;

    private Activity activity;
    private int oldGroupPosition;


    public void setData(List<OrderUserInfo> userInfos) {
        this.mUserList = userInfos;
    }

    @Override
    public int getGroupCount() {
        return mUserList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mUserList.get(groupPosition).getOrders() != null) {
            return mUserList.get(groupPosition).getOrders().size();
        }
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mUserList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_info, parent, false);
            holder = new ViewHolder(convertView);
            // holder.textView = (TextView)convertView.findViewById(R.id.tv_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ViewGroup.LayoutParams lp = new ExpandableListView.LayoutParams(screenWidth,
                ExpandableListView.LayoutParams.WRAP_CONTENT, 0);
        convertView.setLayoutParams(lp);



        OrderUserInfo orderUserInfo = mUserList.get(groupPosition);
        String billNo = orderUserInfo.getOrd_bill_no();
        String phone =orderUserInfo.getOrd_phone();
        String number = phone.substring(0,3)+"****"+phone.substring(7,phone.length());
//        String newNo = billNo.substring(0,6)+"****"+billNo.substring(10,billNo.length());
        holder.order_number_detail.setText(billNo);
        holder.order_phone_number_detail.setText(number);
        holder.time.setText(Util.stampToDate(orderUserInfo.getOrd_at()));
        if (orderUserInfo.getOrd_bill_type() == 0) {
            holder.order_pay_leixing_detail.setText("全款");
        } else if (orderUserInfo.getOrd_bill_type() == 1) {
            holder.order_pay_leixing_detail.setText("尾款");
        } else if (orderUserInfo.getOrd_bill_type() == 2) {
            holder.order_pay_leixing_detail.setText("定金");
        }

        if (orderUserInfo.getOrd_bill_type() == 1) {
            holder.order_pay_type_detail.setText("微信");
        } else if (orderUserInfo.getOrd_bill_type() == 2) {
            holder.order_pay_type_detail.setText("支付宝");
        } else if (orderUserInfo.getOrd_bill_type() == 3) {
            holder.order_pay_type_detail.setText("对公账户");
        } else {
            holder.order_pay_type_detail.setText("其他");
        }
        holder.order_pay_money_detail.setText("￥" + Double.valueOf(orderUserInfo.getOrd_real_price()) / Double.valueOf(100));

        if(orderUserInfo.getOrd_state() == 0){//isShouZi
            holder.bt_xiangqing.setText("生成支付码");
            holder.tv_zhifu_statue.setText("未支付");
            holder.tv_order_add.setVisibility(View.GONE);
            holder.tv_order_info.setVisibility(View.GONE);
            holder.bt_xiangqing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reqPayByUrl("pay/pay_order?order_id=",orderUserInfo.getOrd_id()+"",orderUserInfo.getOrd_phone(),"￥" + Double.valueOf(orderUserInfo.getOrd_real_price()) / Double.valueOf(100));
                }
            });
        }else if(orderUserInfo.getOrd_state() == 3){//已驳回
            holder.bt_xiangqing.setVisibility(View.GONE);
            holder.tv_zhifu_statue.setText("已驳回");
            holder.tv_order_add.setVisibility(View.GONE);
            holder.tv_order_info.setVisibility(View.GONE);
        }else if(orderUserInfo.getOrd_state() == 4){//已关课
            holder.bt_xiangqing.setVisibility(View.GONE);
            holder.tv_zhifu_statue.setText("已关课");
            holder.tv_order_add.setVisibility(View.GONE);
            holder.tv_order_info.setVisibility(View.GONE);
        }
        else {
            holder.bt_xiangqing.setText("快递信息");
            holder.tv_zhifu_statue.setText("已支付");
            holder.tv_order_add.setVisibility(View.VISIBLE);
            holder.tv_order_info.setVisibility(View.GONE);
            holder.tv_order_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, AddExpressActivity.class);
                    intent.putExtra("bill_no",orderUserInfo.getOrd_bill_no());
                    activity.startActivity(intent);
                }
            });
           /* holder.tv_order_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ShowAllOrderActivity.class);
                    activity.startActivity(intent);
                }
            });*/
            holder.bt_xiangqing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ShowExpressInfoActivity.class);
                    intent.putExtra("bill_no",orderUserInfo.getOrd_bill_no());
                    activity.startActivity(intent);
                }
            });
        }

        if (isExpanded) {
            holder.channel_imageview_orientation
                    .setBackgroundResource(R.drawable.icon_down_hover);
        } else {
            holder.channel_imageview_orientation
                    .setBackgroundResource(R.drawable.icon_down_normal);
        }

        holder.rl_order_detail_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    expandableListView.collapseGroup(groupPosition);
                } else {
                    getOrderDetail(orderUserInfo,groupPosition);
                }
            }
        });
        return convertView;
    }

    int type = 0;
    View payView;
    ImageView iv_show_pay;
    TextView tv_phone;
    byte[] byteArray;
    RadioButton rb_zhifubao;
    RadioGroup rg_pay_select;
    private void reqPayByUrl(String uri_api, String orderId,String phone,String price) {
        try {
            String url = Constants.host + uri_api +orderId;
            Log.e("tag", "reqPayByUrl: "+url);
            OkHttpUtils.get(activity, url, new OkhttpCallback() {
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
                public Object parseNetworkResponse(JSONObject object) throws
                        Exception {
                    String url = null;
                    if(object.has("msg")){
                         url = object.getString("msg");
                         if(object.has("qrpay")){//聚合
                            url = object.getString("qrpay");
                            byteArray =  Base64.decode(url.split(",")[1], Base64.DEFAULT);
                         }
                    }else if(object.has("qrpay")){//扫呗
                        url = object.getString("qrpay");
                    }

                    if (PromptManager.myDialog == null || !PromptManager.myDialog.isShowing()) {
                        //需要重新显示
                        if(payView != null){
//                            rg_pay_select.clearCheck();
                            payView = null;
//                            rb_zhifubao.setChecked(true);
                        }
                    }


                    if(payView == null){
                        payView = View.inflate(activity, R.layout.image_show_dialog, null);
                        iv_show_pay= payView.findViewById(R.id.iv_show_pay);
                        tv_phone = payView.findViewById(R.id.tv_phone_number);

                        rg_pay_select = payView.findViewById(R.id.rg_pay_select);
                        rb_zhifubao = payView.findViewById(R.id.rb_zhifubao);
                        RadioButton rb_weixin = payView.findViewById(R.id.rb_weixin);
                        RadioButton rb_juhe = payView.findViewById(R.id.rb_juhe);
                        RadioButton rb_saobai = payView.findViewById(R.id.rb_saobai);

                        rb_zhifubao.setChecked(true);
                        rg_pay_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {

                                if(checkedId == rb_zhifubao.getId()){
                                    type = 0;//支付宝
                                    reqPayByUrl("pay/pay_order?order_id=",orderId,phone,price);
                                }else if(checkedId == rb_weixin.getId()){
                                    type = 1;//微信
                                    reqPayByUrl("pay/wxpay_order?order_id=",orderId,phone,price);
                                }else if(checkedId == rb_juhe.getId()){
                                    type = 2;//聚合
                                    reqPayByUrl("pay/nowpay_order?order_id=",orderId,phone,price);
                                }
                                else{
                                    type = 3;//扫呗
                                    reqPayByUrl("pay/lcpay_order?order_id=",orderId,phone,price);
                                }
                            }
                        });
                    }


                    String finalUrl = url;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            payView.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PromptManager.closeCustomDialog();
                                }
                            });

                            tv_phone.setText("手机号:"+phone+"   应付金额:"+price);
                            if (PromptManager.myDialog == null || !PromptManager.myDialog.isShowing()) {
                                PromptManager.showCustomDialog(activity, payView, Gravity.CENTER, Gravity.CENTER);
                            }
                            if(byteArray != null){
                                iv_show_pay.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                                byteArray = null;
                            }else{
                                MatrixToImageWriterWithLogo.createQRImage(finalUrl,iv_show_pay);
                            }

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






    private void getOrderDetail(OrderUserInfo orderUserInfo,int groupPosition) {
        try {
            String url = Constants.host + "order/info";
            HashMap<String, String> keyMap = new HashMap<String, String>();
            keyMap.put("id", orderUserInfo.getOrd_id() + "");

            OkHttpUtils.post(activity, url, keyMap, new OkhttpCallback() {
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
                public Object parseNetworkResponse(JSONObject object) throws
                        Exception {
                    getOuderInfo(object, orderUserInfo,groupPosition);
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        ViewHolderDetail holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_kemu, parent, false);
            holder = new ViewHolderDetail(convertView);
            // holder.textView = (TextView)convertView.findViewById(R.id.tv_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderDetail) convertView.getTag();
        }
        ViewGroup.LayoutParams lp = new ExpandableListView.LayoutParams(screenWidth,
                ExpandableListView.LayoutParams.WRAP_CONTENT, 0);
        convertView.setLayoutParams(lp);

        holder.postion_detail.setVisibility(View.VISIBLE);
        holder.order_pay_leixing.setVisibility(View.VISIBLE);
        holder.order_pay_money.setVisibility(View.VISIBLE);
        holder.tv_position.setVisibility(View.VISIBLE);

        OrderUserInfo orderUserInfo = mUserList.get(groupPosition);
        if(orderUserInfo.getOrders() != null){
            OrderDetail orderDetail = orderUserInfo.getOrders().get(childPosition);
            holder.xiangmu_detail.setText(orderDetail.sch_ssion_name);
            holder.postion_detail.setText(" "+orderDetail.getSch_domicile());
            holder.tv_user_name.setText(orderDetail.sch_uname);
            holder.tv_position.setText(orderDetail.getSch_domicile());
            if(orderDetail.getStudy_type() == null){
                holder.order_number.setText("学历信息");
            }else{
                holder.order_number.setText(orderDetail.getStudy_type());
            }
            if(orderDetail.getStudy_type() != null && orderDetail.getStudy_type().equals("网课信息")){
                holder.order_pay_type.setText("班         型:");
                //网课不显示区域
                holder.postion_detail.setVisibility(View.GONE);
                holder.order_pay_leixing.setVisibility(View.GONE);
                holder.order_pay_money.setVisibility(View.GONE);
                holder.tv_position.setVisibility(View.GONE);
            }else if(orderDetail.getStudy_type() != null){
                holder.order_pay_type.setText("姓         名:");
            }
        }

       /* TextView textView = null;
        if (view == null) {
            textView = new TextView(activity);
        } else {
            textView = (TextView) view;
        }
        textView.setText("fffffffffffff");
        textView.setTextSize(20);
        textView.setPadding(70, 0, 0, 40);*/
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        if(oldGroupPosition != -1 && oldGroupPosition != groupPosition) {
            expandableListView.collapseGroup(oldGroupPosition);
        }
        oldGroupPosition = groupPosition;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    static class ViewHolder {
        TextView order_number_detail;
        TextView order_phone_number_detail;
        TextView order_pay_leixing_detail;
        TextView order_pay_money_detail;
        TextView order_pay_type_detail;
        RelativeLayout rl_order_detail_info;
        ImageView channel_imageview_orientation;
        TextView bt_xiangqing;
        TextView tv_zhifu_statue;

        TextView tv_order_add;
        TextView tv_order_info;
        TextView time;

        public ViewHolder(View view) {
            order_number_detail = view.findViewById(R.id.order_number_detail);
            order_phone_number_detail = view.findViewById(R.id.order_phone_number_detail);
            order_pay_leixing_detail = view.findViewById(R.id.order_pay_leixing_detail);
            order_pay_money_detail = view.findViewById(R.id.order_pay_money_detail);
            order_pay_type_detail = view.findViewById(R.id.order_pay_type_detail);
            channel_imageview_orientation = view.findViewById(R.id.channel_imageview_orientation);
            bt_xiangqing = view.findViewById(R.id.bt_xiangqing);
            tv_zhifu_statue = view.findViewById(R.id.tv_zhifu_statue);
            rl_order_detail_info = view.findViewById(R.id.rl_order_detail_info);
            time = view.findViewById(R.id.time);

            tv_order_add = view.findViewById(R.id.tv_order_cancel);
            tv_order_info = view.findViewById(R.id.tv_order_info);
        }

    }

    static class ViewHolderDetail {
        TextView xiangmu_detail;
        TextView postion_detail;
        TextView tv_user_name;
        TextView tv_position;
        TextView order_pay_type_detail;
        RelativeLayout rl_order_detail_info;
        ImageView channel_imageview_orientation;
        TextView order_number;
        TextView order_pay_type;

        TextView order_pay_money;
        TextView order_pay_leixing;

        public ViewHolderDetail(View view) {
            xiangmu_detail = view.findViewById(R.id.xiangmu_detail);
            postion_detail = view.findViewById(R.id.postion_detail);
            tv_user_name = view.findViewById(R.id.tv_user_name);
            tv_position = view.findViewById(R.id.tv_position);
            order_number = view.findViewById(R.id.order_number);
            order_pay_type = view.findViewById(R.id.order_pay_type);
            order_pay_money = view.findViewById(R.id.order_pay_money);
            order_pay_leixing = view.findViewById(R.id.order_pay_leixing);
        }

    }

    public UserDataOrderAdapter(Activity activity, List<OrderUserInfo> userInfos, boolean isShouZi, ExpandableListView expandableListView) {
        this.isShouZi = isShouZi;
        this.activity = activity;
        mUserList = userInfos;
        this.expandableListView = expandableListView;
        Display display = activity.getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
    }


    public String TAG = "UserDataZiAdapter";

    private void getOuderInfo(JSONObject job, OrderUserInfo orderUserInfo,int groupPosition) throws Exception {

                JSONObject object = job.getJSONObject("info");

                JSONArray sigjsonArray = object.getJSONArray("sig");
                JSONArray schjsonArray = object.getJSONArray("sch");
                JSONArray cerjsonArray = object.getJSONArray("cer");
                JSONArray clajsonArray = object.getJSONArray("cla");
                if (sigjsonArray != null || schjsonArray != null || cerjsonArray != null || clajsonArray != null) {
                    List<OrderDetail> orderDetails = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        if (i == 0) {
                            for (int x = 0; x < sigjsonArray.length(); x++) {
                                JSONObject jsonObject = sigjsonArray.getJSONObject(x);
                                OrderDetail orderDetail1 = new OrderDetail(jsonObject.getString("sig_pname"), jsonObject.getString("sig_uname"), jsonObject.getString("sig_test_add"),"协助报名");
                                orderDetails.add(orderDetail1);
                            }
                        } else if (i == 1) {
                            List<OrderDetail> schorderDetail = JSONUtil.parserArrayList(schjsonArray, OrderDetail.class, 0);
                            orderDetails.addAll(schorderDetail);
                        } else if (i == 2) {
                            for (int x = 0; x < cerjsonArray.length(); x++) {
                                JSONObject jsonObject = cerjsonArray.getJSONObject(x);
                                OrderDetail orderDetail1 = new OrderDetail(jsonObject.getString("cer_name"), jsonObject.getString("cer_uname"), jsonObject.getString("cer_domicile"),"直取证书");
                                orderDetails.add(orderDetail1);
                            }
                        } else {
                            for (int x = 0; x < clajsonArray.length(); x++) {
                                JSONObject jsonObject = clajsonArray.getJSONObject(x);
                                OrderDetail orderDetail1 = new OrderDetail(jsonObject.getString("cla_pname"), jsonObject.getString("cla_cname"), "","网课信息");
                                orderDetails.add(orderDetail1);
                            }
                        }

                    }
                    orderUserInfo.setOrders(orderDetails);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            expandableListView.expandGroup(groupPosition);
                            expandableListView.deferNotifyDataSetChanged();
                        }
                    });
                }
    }
}