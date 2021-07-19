package com.peixueshi.crm.ui.newadapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mf.library.utils.ToastUtils;
import com.peixueshi.crm.R;
import com.peixueshi.crm.activity.AddExpressActivity;
import com.peixueshi.crm.activity.ShowExpressInfoActivity;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.bean.DetailsBean;
import com.peixueshi.crm.bean.NewOrderUserInfo;
import com.peixueshi.crm.newactivity.PaymentCodeActivity;
import com.peixueshi.crm.ui.widget.ExpandableListView;
import com.peixueshi.crm.utils.MatrixToImageWriterWithLogo;
import com.peixueshi.crm.utils.PromptManager;
import com.peixueshi.crm.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class NewUserDataOrderAdapter extends BaseExpandableListAdapter {

    private boolean isShouZi = true;
    private List<NewOrderUserInfo> mUserList;
    private ExpandableListView expandableListView;
    private int screenWidth;

    private Activity activity;
    private int oldGroupPosition;
    private int currentPosition;


    public void setData(List<NewOrderUserInfo> userInfos) {
        this.mUserList = userInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mUserList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mUserList.get(groupPosition).getDetails() != null) {
            return mUserList.get(groupPosition).getDetails().size();
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
       /* ViewGroup.LayoutParams lp = new ExpandableListView.LayoutParams(screenWidth,
                ExpandableListView.LayoutParams.WRAP_CONTENT, 0);
        convertView.setLayoutParams(lp);*/


        NewOrderUserInfo orderUserInfo = mUserList.get(groupPosition);
        String billNo = orderUserInfo.getBillNo();
        String phone = orderUserInfo.getPhone();
        String customerId = orderUserInfo.getCustomerId();
        String notes = orderUserInfo.getNotes();
        if (!TextUtils.isEmpty(phone)) {
            String number = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
            holder.order_phone_number_detail.setText(number);
        }

//        String newNo = billNo.substring(0,6)+"****"+billNo.substring(10,billNo.length());
        holder.order_number_detail.setText(billNo);
        holder.user_id.setText(customerId);
        holder.time.setText(Util.stampToDate(orderUserInfo.getCreatedAt()));
        holder.order_pay_time_detail.setText(Util.stampToDate(orderUserInfo.getCreatedAt()));
        holder.order_pay_remark_detail.setText(notes);
        int billType = orderUserInfo.getBillType();
        int payType = orderUserInfo.getPayType();
        holder.pay_time_lin.setVisibility(View.GONE);
        holder.teacher_lin.setVisibility(View.GONE);
        // 1,网课 2,学历   // 3协助报名       // 4证书    //5 其他

        if (billType == 1) {
            holder.order_pay_leixing_detail.setText("全款");
            holder.tv_order_info.setVisibility(View.VISIBLE);
        } else if (billType == 2) {
            holder.order_pay_leixing_detail.setText("尾款");
            holder.tv_order_info.setVisibility(View.VISIBLE);
        } else if (billType == 3) {
            holder.order_pay_leixing_detail.setText("定金");
            holder.tv_order_info.setVisibility(View.GONE);
        }

        if (payType == 1) {
            holder.order_pay_type_detail.setText("微信");
        } else if (payType == 2) {
            holder.order_pay_type_detail.setText("支付宝");
        } else if (payType == 3) {
            holder.order_pay_type_detail.setText("对公账户");
        } else {
            holder.order_pay_type_detail.setText("其他");
        }
        holder.order_pay_money_detail.setText("￥" + Double.valueOf(orderUserInfo.getTotalPrice()) / Double.valueOf(100));
        int status = orderUserInfo.getStatus();
        if (status == 1) {//isShouZi
            holder.lin_has_pay.setVisibility(View.GONE);
            holder.lin_btn.setVisibility(View.VISIBLE);
            holder.bt_xiangqing.setText("生成支付码");
            holder.tv_zhifu_statue.setText("待支付");
            holder.tv_zhifu_statue.setTextColor(activity.getResources().getColor(R.color.sdk_color_common_red));
            holder.tv_order_info.setVisibility(View.VISIBLE);
            holder.bt_xiangqing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //默认支付宝
                   double price= Double.valueOf(orderUserInfo.getTotalPrice()) / Double.valueOf(100);
                    Intent intent = new Intent(activity, PaymentCodeActivity.class);
                    intent.putExtra("orderId", orderUserInfo.getOrderId());
                    intent.putExtra("phone",orderUserInfo.getPhone());
                    intent.putExtra("price",price);
                    activity.startActivity(intent);
                    //  reqPayByUrls("tool/get_code?order_id=", 2, orderUserInfo.getOrderId() + "", orderUserInfo.getPhone(), "￥" + Double.valueOf(orderUserInfo.getTotalPrice()) / Double.valueOf(100));
                }
            });
        } else if (status == 4) {//已驳回
            holder.bt_xiangqing.setVisibility(View.GONE);
            holder.tv_zhifu_statue.setText("已驳回");
            holder.tv_zhifu_statue.setTextColor(activity.getResources().getColor(R.color.text_common_gray_new));
            holder.tv_order_add.setVisibility(View.GONE);
        } else if (status == 3) {//已关课
            holder.bt_xiangqing.setVisibility(View.GONE);
            holder.tv_zhifu_statue.setText("已分配");
            holder.tv_zhifu_statue.setTextColor(activity.getResources().getColor(R.color.text_common_gray_new));
            holder.tv_order_add.setVisibility(View.GONE);
        } else if (status == 5) {//手动取消
            holder.bt_xiangqing.setVisibility(View.GONE);
            holder.tv_zhifu_statue.setText("手动取消");
            holder.tv_zhifu_statue.setTextColor(activity.getResources().getColor(R.color.text_common_gray_new));
            holder.tv_order_add.setVisibility(View.GONE);
            holder.tv_order_info.setVisibility(View.GONE);
        } else if (status == 6) {//自动取消
            holder.bt_xiangqing.setVisibility(View.GONE);
            holder.tv_zhifu_statue.setText("自动取消");
            holder.tv_zhifu_statue.setTextColor(activity.getResources().getColor(R.color.text_common_gray_new));
            holder.tv_order_add.setVisibility(View.GONE);
            holder.tv_order_info.setVisibility(View.GONE);
        } else if (status == 2) {
            holder.bt_xiangqing.setText("快递信息");
            holder.tv_zhifu_statue.setText("已支付");
            holder.tv_zhifu_statue.setTextColor(activity.getResources().getColor(R.color.home_top_blue));
            holder.tv_order_add.setVisibility(View.VISIBLE);


            holder.bt_xiangqing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ShowExpressInfoActivity.class);
                    intent.putExtra("bill_no", orderUserInfo.getBillNo());
                    activity.startActivity(intent);
                }
            });
            holder.tv_expressage_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, AddExpressActivity.class);//添加快递信息
                    intent.putExtra("bill_no", orderUserInfo.getBillNo());
                    activity.startActivity(intent);
                }
            });
            holder.pay_time_lin.setVisibility(View.VISIBLE);
            holder.order_has_pay_time_detail.setText(Util.stampToDate(orderUserInfo.getPayAt()));//支付时间
            holder.teacher_lin.setVisibility(View.VISIBLE);
            holder.teacher_pay_detail.setText(orderUserInfo.getTeachName());
         //   holder.lin_has_pay.setVisibility(View.VISIBLE);
            holder.lin_btn.setVisibility(View.GONE);
            if (billType==3){//定金
                holder.tv_order_change.setVisibility(View.GONE);
                holder.bt_earnest.setVisibility(View.VISIBLE);//补交定金
                holder.bt_balance.setVisibility(View.VISIBLE);//补交尾款
            }else {
                holder.tv_order_change.setVisibility(View.VISIBLE);
                holder.bt_earnest.setVisibility(View.GONE);
                holder.bt_balance.setVisibility(View.GONE);
            }
        }
        holder.tv_order_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = groupPosition;
                int orderId = orderUserInfo.getOrderId();
                showDialogMessage(activity, "确定取消该订单吗？", true, orderId + "", 2);
            }
        });
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
                    List<DetailsBean> details = mUserList.get(groupPosition).getDetails();
                    //  getOrderDetail(orderUserInfo, groupPosition);
                    orderUserInfo.setDetails(details);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            expandableListView.expandGroup(groupPosition);
                            expandableListView.deferNotifyDataSetChanged();
                        }
                    });
                }
            }
        });
        holder.tv_order_info.setOnClickListener(new View.OnClickListener() {//临时开课
            @Override
            public void onClick(View v) {
                int orderId = orderUserInfo.getOrderId();
                showDialogMessage(activity, "确定对此订单进行临时开课申请吗？", true, orderId + "", 1);
            }
        });
        return convertView;
    }

    public void showDialogMessage(Activity activity, String msg, boolean isOpen, String orderId, int type) {
        View view = View.inflate(activity, R.layout.create_chance_dialog, null);
        TextView textView = view.findViewById(R.id.tv_to_bind_warn);
        textView.setText(msg);
        view.findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptManager.closeCustomDialog();
                if (isOpen == true) {
                    getProvisional(orderId, type);
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
        PromptManager.showCustomDialog(activity, view, Gravity.CENTER, Gravity.CENTER);
    }

    public void getProvisional(String orderId, int type) {
        //type=1临时开课   type=2取消订单
        try {
            String url;
            if (type == 1) {
                url = Constants.allhost + "order/temporary";
            } else {
                url = Constants.allhost + "order/cancel";
            }

            HashMap<String, String> keyMap = new HashMap<String, String>();
            keyMap.put("orderId", orderId + "");

            OkHttpUtils.newPost(activity, url, keyMap, new OkhttpCallback() {
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
                    if (object.has("code")) {
                        int code = object.getInt("code");
                        if (code == 0) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (type == 1) {
                                        ToastUtils.showShort("申请成功");
                                    } else {
                                        ToastUtils.showShort("取消成功");
                                        mUserList.remove(currentPosition);
                                        setData(mUserList);
                                    }

                                }
                            });
                        }
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    int type = 0;
    View payView;
    ImageView iv_show_pay;
    TextView tv_phone;
    byte[] byteArray;
    RadioButton rb_zhifubao;
    RadioGroup rg_pay_select;

    private void reqPayByUrls(String uri_api, int pay_type, String orderId, String phone, String price) {
        try {
            String url = Constants.payhost + uri_api + orderId + "&pay_type=" + pay_type;
            Log.e("tag", "reqPayByUrls: " + url);
            OkHttpUtils.newGet(activity, url, new OkhttpCallback() {
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
                    if (object.has("data")) {
                        url = object.getString("data");
                        if (pay_type == 1) {//聚合
                            url = object.getString("data");
                            byteArray = Base64.decode(url.split(",")[1], Base64.DEFAULT);
                        }
                    } else if (object.has("qrpay")) {//扫呗
                        url = object.getString("qrpay");
                    }

                    if (PromptManager.myDialog == null || !PromptManager.myDialog.isShowing()) {
                        //需要重新显示
                        if (payView != null) {
//                            rg_pay_select.clearCheck();
                            payView = null;
//                            rb_zhifubao.setChecked(true);
                        }
                    }


                    if (payView == null) {
                        payView = View.inflate(activity, R.layout.image_show_dialog, null);
                        iv_show_pay = payView.findViewById(R.id.iv_show_pay);
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
                                //tool/get_code?order_id=203&pay_type=2

                               /* pay_type   1  聚合支付

                                2 支付宝支付  3：扫呗支付   4：微信支付*/
                                if (checkedId == rb_zhifubao.getId()) {
                                    type = 0;//支付宝
                                    reqPayByUrls("tool/get_code?order_id=", 2, orderId, phone, price);
                                } else if (checkedId == rb_weixin.getId()) {
                                    type = 1;//微信
                                    reqPayByUrls("tool/get_code?order_id=", 4, orderId, phone, price);
                                } else if (checkedId == rb_juhe.getId()) {
                                    type = 2;//聚合
                                    reqPayByUrls("tool/get_code?order_id=", 1, orderId, phone, price);
                                } else {
                                    type = 3;//扫呗
                                    reqPayByUrls("tool/get_code?order_id=", 3, orderId, phone, price);
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

                            tv_phone.setText("手机号:" + phone + "   应付金额:" + price);
                            if (PromptManager.myDialog == null || !PromptManager.myDialog.isShowing()) {
                                PromptManager.showCustomDialog(activity, payView, Gravity.CENTER, Gravity.CENTER);
                            }
                            if (byteArray != null) {
                                iv_show_pay.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                                byteArray = null;
                            } else {
                                MatrixToImageWriterWithLogo.createQRImage(finalUrl, iv_show_pay);
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

    private void reqPayByUrl(String uri_api, String orderId, String phone, String price) {
        try {
            String url = Constants.host + uri_api + orderId;
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
                    if (object.has("msg")) {
                        url = object.getString("msg");
                        if (object.has("qrpay")) {//聚合
                            url = object.getString("qrpay");
                            byteArray = Base64.decode(url.split(",")[1], Base64.DEFAULT);
                        }
                    } else if (object.has("qrpay")) {//扫呗
                        url = object.getString("qrpay");
                    }

                    if (PromptManager.myDialog == null || !PromptManager.myDialog.isShowing()) {
                        //需要重新显示
                        if (payView != null) {
//                            rg_pay_select.clearCheck();
                            payView = null;
//                            rb_zhifubao.setChecked(true);
                        }
                    }


                    if (payView == null) {
                        payView = View.inflate(activity, R.layout.image_show_dialog, null);
                        iv_show_pay = payView.findViewById(R.id.iv_show_pay);
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

                                if (checkedId == rb_zhifubao.getId()) {
                                    type = 0;//支付宝
                                    reqPayByUrl("pay/pay_order?order_id=", orderId, phone, price);
                                } else if (checkedId == rb_weixin.getId()) {
                                    type = 1;//微信
                                    reqPayByUrl("pay/wxpay_order?order_id=", orderId, phone, price);
                                } else if (checkedId == rb_juhe.getId()) {
                                    type = 2;//聚合
                                    reqPayByUrl("pay/nowpay_order?order_id=", orderId, phone, price);
                                } else {
                                    type = 3;//扫呗
                                    reqPayByUrl("pay/lcpay_order?order_id=", orderId, phone, price);
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

                            tv_phone.setText("手机号:" + phone + "   应付金额:" + price);
                            if (PromptManager.myDialog == null || !PromptManager.myDialog.isShowing()) {
                                PromptManager.showCustomDialog(activity, payView, Gravity.CENTER, Gravity.CENTER);
                            }
                            if (byteArray != null) {
                                iv_show_pay.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                                byteArray = null;
                            } else {
                                MatrixToImageWriterWithLogo.createQRImage(finalUrl, iv_show_pay);
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


    private void getOrderDetail(NewOrderUserInfo orderUserInfo, int groupPosition) {
        try {
            String url = Constants.host + "order/info";
            HashMap<String, String> keyMap = new HashMap<String, String>();
            keyMap.put("id", orderUserInfo.getOrderId() + "");

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
                    getOuderInfo(object, orderUserInfo, groupPosition);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_order_details_kemu, parent, false);
            holder = new ViewHolderDetail(convertView);
            // holder.textView = (TextView)convertView.findViewById(R.id.tv_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderDetail) convertView.getTag();
        }
       /* ViewGroup.LayoutParams lp = new ExpandableListView.LayoutParams(screenWidth,
                ExpandableListView.LayoutParams.WRAP_CONTENT, 0);
        convertView.setLayoutParams(lp);*/

        holder.postion_detail.setVisibility(View.VISIBLE);
        holder.order_pay_leixing.setVisibility(View.VISIBLE);
        holder.order_pay_money.setVisibility(View.VISIBLE);
        holder.tv_position.setVisibility(View.VISIBLE);

        NewOrderUserInfo orderUserInfo = mUserList.get(groupPosition);
        // 1,网课 2,学历   // 3协助报名       // 4其他
        if (orderUserInfo.getDetails() != null) {
            int orderType = orderUserInfo.getOrderType();
            DetailsBean orderDetail = orderUserInfo.getDetails().get(childPosition);
            holder.rl_pay_info.setVisibility(View.GONE);
            holder.rl_wang.setVisibility(View.GONE);
            holder.rl_xiezhu.setVisibility(View.GONE);
            holder.rl_other.setVisibility(View.GONE);
            if (orderType == 1) {
                holder.rl_wang.setVisibility(View.VISIBLE);
                holder.wang_pro_detail.setText(orderDetail.getProjectName());
                holder.wang_name_detail.setText(orderDetail.getClassName());
                holder.wang_price_detail.setText("￥" + Double.valueOf(orderDetail.getPrice()) / Double.valueOf(100));
            } else if (orderType == 2) {
               /* <span v-if="row.major.status == 1">大专</span>
              <span v-else-if="row.major.status == 2">中专</span>
              <span v-else-if="row.major.status == 3">本科</span>*/
                holder.rl_pay_info.setVisibility(View.VISIBLE);
                holder.exam_type.setVisibility(View.GONE);
                holder.exam_type_detail.setVisibility(View.GONE);
                holder.xiangmu_detail.setText(orderDetail.getSchoolName());
                holder.tv_user_name.setText(orderDetail.getMajorName());
                holder.stu_name_detail.setText(orderDetail.getStudentName());
                holder.census_detail.setText(orderDetail.getAddress());
                holder.tv_position.setText("￥" + Double.valueOf(orderDetail.getPrice()) / Double.valueOf(100));
                holder.xue_card_detail.setText(orderDetail.getIdCard());
                int mStatus = orderDetail.getMStatus();
                if (mStatus==1){
                    holder.postion_detail.setText("大专");
                }else if (mStatus==2){
                    holder.postion_detail.setText("中专");
                } else if (mStatus==3){
                    holder.postion_detail.setText("本科");
                }

            } else if (orderType == 3) {
                holder.rl_xiezhu.setVisibility(View.VISIBLE);
                holder.xie_pro_detail.setText(orderDetail.getProjectName());
                holder.xie_card_detail.setText(orderDetail.getIdCard());
                holder.xie_price_detail.setText("￥" + Double.valueOf(orderDetail.getPrice()) / Double.valueOf(100));
                holder.xie_name_detail.setText(orderDetail.getStudentName());
                holder.xie_census_detail.setText(orderDetail.getAddress());
                holder.xie_area_detail.setText(orderDetail.getOAddress());
            } else {
                holder.rl_other.setVisibility(View.VISIBLE);
                holder.other_pro_detail.setText(orderDetail.getCertName());
                holder.other_name_detail.setText(orderDetail.getStudentName());
                holder.other_census_detail.setText(orderDetail.getAddress());
                holder.other_price_detail.setText("￥" + Double.valueOf(orderDetail.getPrice()) / Double.valueOf(100));
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
        if (oldGroupPosition != -1 && oldGroupPosition != groupPosition) {
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
        TextView bt_xiangqing;//生成支付码
        TextView tv_zhifu_statue;

        TextView tv_order_add;//取消订单
        TextView tv_order_info;//申请临时开课
        TextView time;
        TextView user_id;//客户id
        TextView order_pay_time_detail;//下单时间
        TextView order_pay_remark_detail;//备注
        LinearLayout pay_time_lin;//支付时间
        TextView order_has_pay_time_detail;
        LinearLayout teacher_lin;//班主任
        TextView teacher_pay_detail;
        LinearLayout lin_has_pay;
        TextView tv_expressage_add;
        TextView tv_order_premium;
        TextView bt_earnest;
        TextView bt_balance;
        LinearLayout lin_btn;
        TextView tv_order_change;
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
            user_id = view.findViewById(R.id.user_id);
            order_pay_time_detail = view.findViewById(R.id.order_pay_time_detail);
            order_pay_remark_detail = view.findViewById(R.id.order_pay_remark_detail);
            pay_time_lin = view.findViewById(R.id.pay_time_lin);
            order_has_pay_time_detail = view.findViewById(R.id.order_has_pay_time_detail);

            teacher_lin = view.findViewById(R.id.teacher_lin);
            teacher_pay_detail = view.findViewById(R.id.teacher_pay_detail);
            lin_has_pay = view.findViewById(R.id.lin_has_pay);
            tv_expressage_add = view.findViewById(R.id.tv_order_add);

            tv_order_premium = view.findViewById(R.id.tv_order_premium);
            bt_earnest = view.findViewById(R.id.bt_earnest);
            bt_balance = view.findViewById(R.id.bt_balance);
            lin_btn = view.findViewById(R.id.lin_btn);
             tv_order_change = view.findViewById(R.id.tv_order_change);


        }

    }

    static class ViewHolderDetail {
        TextView xiangmu_detail;
        TextView postion_detail;
        TextView tv_position;
        TextView order_pay_type_detail;
        RelativeLayout rl_order_detail_info;
        ImageView channel_imageview_orientation;

        TextView order_pay_money;
        TextView order_pay_leixing;
        TextView exam_type;
        TextView exam_type_detail;
        TextView enter_area;
        TextView order_number_area;
        TextView tv_xiangmu_name;
        TextView order_pay_type;
        TextView tv_user_name;
        TextView stu_name;
        TextView stu_name_detail;
        TextView census;
        TextView census_detail;
        RelativeLayout rl_xiezhu;
        TextView xie_pro_detail;
        TextView xie_pro;
        TextView xie_name_detail;
        TextView xie_card_detail;
        TextView xie_census_detail;
        TextView xie_price_detail;
        RelativeLayout rl_other;


        TextView other_pro_detail;
        TextView other_name_detail;
        TextView other_census_detail;
        TextView other_price_detail;


        RelativeLayout rl_wang;

        RelativeLayout rl_pay_info;
        TextView wang_pro_detail;
        TextView wang_name_detail;
        TextView wang_price_detail;
        TextView xue_card_detail;
        TextView xie_area_detail;
        public ViewHolderDetail(View view) {
            rl_pay_info = view.findViewById(R.id.rl_pay_info);
            exam_type = view.findViewById(R.id.exam_type);//考试类型
            exam_type_detail = view.findViewById(R.id.exam_type_detail);//考试类型值
            enter_area = view.findViewById(R.id.enter_area);//报考区域
            order_number_area = view.findViewById(R.id.order_number_area);//报考区域
            tv_xiangmu_name = view.findViewById(R.id.tv_xiangmu_name);//学历名称

            xiangmu_detail = view.findViewById(R.id.xiangmu_detail);//学历名称
            order_pay_leixing = view.findViewById(R.id.order_pay_leixing);//学历层次
            postion_detail = view.findViewById(R.id.postion_detail);//学历层次
            order_pay_type = view.findViewById(R.id.order_pay_type);//专业名称
            tv_user_name = view.findViewById(R.id.tv_user_name);//专业名称
            stu_name = view.findViewById(R.id.stu_name);//姓名

            stu_name_detail = view.findViewById(R.id.stu_name_detail);//姓名
            order_pay_money = view.findViewById(R.id.order_pay_money);//下单金额
            tv_position = view.findViewById(R.id.tv_position);//下单金额
            census = view.findViewById(R.id.census);//户籍
            census_detail = view.findViewById(R.id.census_detail);//户籍


            rl_xiezhu = view.findViewById(R.id.rl_xiezhu);//协助报名
            xie_pro_detail = view.findViewById(R.id.xie_pro_detail);//项目名称
            xie_pro = view.findViewById(R.id.xie_pro);//报名区域
            xie_area_detail = view.findViewById(R.id.xie_area_detail);
            xie_name_detail = view.findViewById(R.id.xie_name_detail);//姓名
            xie_card_detail = view.findViewById(R.id.xie_card_detail);//身份证号
            xie_census_detail = view.findViewById(R.id.xie_census_detail);//户籍
            xie_price_detail = view.findViewById(R.id.xie_price_detail);//下单金额


            rl_other = view.findViewById(R.id.rl_other);//其他
            other_pro_detail = view.findViewById(R.id.other_pro_detail);//证书名称
            other_name_detail = view.findViewById(R.id.other_name_detail);//姓名
            other_census_detail = view.findViewById(R.id.other_census_detail);//户籍
            other_price_detail = view.findViewById(R.id.other_price_detail);//下单金额


            rl_wang = view.findViewById(R.id.rl_wang);//网课
            wang_pro_detail = view.findViewById(R.id.wang_pro_detail);//项目名称
            wang_name_detail = view.findViewById(R.id.wang_name_detail);//班型
            wang_price_detail = view.findViewById(R.id.wang_price_detail);//下单金额
            xue_card_detail = view.findViewById(R.id.xue_card_detail);

        }

    }

    public NewUserDataOrderAdapter(Activity activity, List<NewOrderUserInfo> userInfos, boolean isShouZi, ExpandableListView expandableListView) {
        this.isShouZi = isShouZi;
        this.activity = activity;
        mUserList = userInfos;
        this.expandableListView = expandableListView;
        Display display = activity.getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
    }


    public String TAG = "UserDataZiAdapter";

    private void getOuderInfo(JSONObject job, NewOrderUserInfo orderUserInfo, int groupPosition) throws Exception {

        JSONObject object = job.getJSONObject("info");

        JSONArray sigjsonArray = object.getJSONArray("sig");
        JSONArray schjsonArray = object.getJSONArray("sch");
        JSONArray cerjsonArray = object.getJSONArray("cer");
        JSONArray clajsonArray = object.getJSONArray("cla");
               /* if (sigjsonArray != null || schjsonArray != null || cerjsonArray != null || clajsonArray != null) {
                    List<NewOrderUserInfo.DetailsBean> orderDetails = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        if (i == 0) {
                            for (int x = 0; x < sigjsonArray.length(); x++) {
                                JSONObject jsonObject = sigjsonArray.getJSONObject(x);
                                NewOrderUserInfo.DetailsBean orderDetail1 = new NewOrderUserInfo.DetailsBean(jsonObject.getString("sig_pname"), jsonObject.getString("sig_uname"), jsonObject.getString("sig_test_add"),"协助报名");
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
                    orderUserInfo.setDetails(orderDetails);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            expandableListView.expandGroup(groupPosition);
                            expandableListView.deferNotifyDataSetChanged();
                        }
                    });
                }*/
    }
}