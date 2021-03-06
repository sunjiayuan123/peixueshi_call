package com.peixueshi.crm.ui.newadapter;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.peixueshi.crm.R;
import com.peixueshi.crm.activity.DetailBeizhuActivity;
import com.peixueshi.crm.app.inter.OkhttpCallback;
import com.peixueshi.crm.app.utils.OkHttpUtils;
import com.peixueshi.crm.app.utils.TkyOkHttpUtils;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.newactivity.NewAddOrderActivity;
import com.peixueshi.crm.newactivity.NewHomeDetailsActivity;
import com.peixueshi.crm.newfragment.OneChanceFragment;
import com.peixueshi.crm.utils.EnjoyPreference;
import com.peixueshi.crm.utils.JSONUtil;
import com.peixueshi.crm.utils.PhoneInfoUtils;
import com.peixueshi.crm.utils.PromptManager;


import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserChanceZiListAdapter extends BaseAdapter {

    private boolean isShouZi = true;
    private List<Map<String, String>> mUserList;

    private Activity activity;
    private OneChanceFragment oneFragment;

    private int time = -1;
    private int TIME_COUNT_DEFAULT = 20;//??????
    private int COUN_CALL = 20;//?????????
    ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            if (time <= 0) {
                mHandler2.sendEmptyMessage(1);
            } else {
                mHandler2.sendEmptyMessage(2);
                mHandler2.postDelayed(this, 1000);
            }
        }
    };
    private Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    tv_next_get_time.setText("00:00");
                    break;
                case 2:
                    PromptManager.showMyToast("???" + time + "??????????????????", activity);
                    break;
                default:
            }

        }
    };
    public static String localCallNumber;

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chance_item_info, parent, false);
            holder = new ViewHolder(convertView);
            // holder.textView = (TextView)convertView.findViewById(R.id.tv_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Map<String, String> chanceUserInfo = mUserList.get(position);
        String workPool = chanceUserInfo.get("workPool");
        com.alibaba.fastjson.JSONObject ziXunUserInfo = com.alibaba.fastjson.JSONObject.parseObject(workPool);
        String phone = ziXunUserInfo.getString("phone");
        String pName = ziXunUserInfo.getString("pName");
        String id = ziXunUserInfo.getString("id");
        if (isShouZi) {
            if (!TextUtils.isEmpty(phone)) {

                String number = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
                holder.tv_user_mumber.setText(number);
            }
            holder.tv_xiangmu.setText(pName);
            holder.customer_id.setText(id);

            holder.iv_copy_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label", phone);
                    cm.setPrimaryClip(mClipData);
                    PromptManager.showMyToast(phone + "?????????", activity);
                }
            });

        } else {
            if (!TextUtils.isEmpty(phone)) {
                String number = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
                holder.tv_user_mumber.setText(number);
            }
            holder.tv_xiangmu.setText(pName);
            holder.customer_id.setText(id);
            holder.iv_copy_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label", phone);
                    cm.setPrimaryClip(mClipData);
                    PromptManager.showMyToast(phone + "?????????", activity);
                }
            });
        }


        holder.iv_call.setOnClickListener(new View.OnClickListener() {//???????????????
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Map<String, String> infos = mUserList.get(position);
                String workPool = infos.get("workPool");
                com.alibaba.fastjson.JSONObject ziXunUserInfo = com.alibaba.fastjson.JSONObject.parseObject(workPool);
                // Toast.makeText(view.getContext(), "you call " + info.getWf_phone(), Toast.LENGTH_SHORT).show();
                String wf_id;
              /*  if (isShouZi) {
                    wf_id = ziXunUserInfo.getString("uId");
                    getRealPhone("goals/fild_first", wf_id, 1);
                } else {
                    wf_id = ziXunUserInfo.getString("uId");
                    getRealPhone("goals/fild_pool", wf_id, 1);
                }*/

                checkDualSim(ziXunUserInfo.getString("phone"));
            }
        });

        holder.bt_beizhu.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Map<String, String> info = mUserList.get(position);
                String workPool = info.get("workPool");
                com.alibaba.fastjson.JSONObject ziXunUserInfo = com.alibaba.fastjson.JSONObject.parseObject(workPool);
                String wf_id;
              /*  if (isShouZi) {
                    wf_id = ziXunUserInfo.getString("uId");
                    getRealPhone("goals/fild_first", wf_id, 2);
                } else {
                    wf_id =ziXunUserInfo.getString("uId");
                    getRealPhone("goals/fild_pool", wf_id, 2);
                }*/
                String csId = ziXunUserInfo.getString("id");
                String pId = ziXunUserInfo.getString("pId");
                String bId = ziXunUserInfo.getString("bId");
                if (isShouZi) {
                    beizhu("cmc/stuInfo?", csId, pId, bId, true);
                } else {
                    beizhu("cmc/stuInfo?", csId, pId, bId, false);
                }
            }
        });

        holder.bt_huiku.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Map<String, String> info = mUserList.get(position);
                String workPool = info.get("workPool");
                com.alibaba.fastjson.JSONObject ziXunUserInfo = com.alibaba.fastjson.JSONObject.parseObject(workPool);
                String uId = ziXunUserInfo.getString("id");
                if (isShouZi) {
                    huiku("cmc/del_work?", uId, true, position);
                } else {
                    huiku("cmc/del_work?", uId, false, position);
                }
                if (oneFragment.recycle_view != null && oneFragment.recycle_view.getLastVisiblePosition() >= mUserList.size() - 1) {
                    oneFragment.adapter.notifyDataSetChanged();
                    oneFragment.pullDownView.endUpdate(new Date());
                    oneFragment.recycle_view.onLoadMoreComplete(false);
                }
            }
        });

        holder.bt_xiangqing.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Map<String, String> info = mUserList.get(position);
                String workPool = info.get("workPool");
                com.alibaba.fastjson.JSONObject ziXunUserInfo = com.alibaba.fastjson.JSONObject.parseObject(workPool);
                String csid = ziXunUserInfo.getString("id");
                String phone = ziXunUserInfo.getString("phone");
                ziXunUserInfo.put("isShouZi", isShouZi);
                Intent intent = new Intent(activity, NewHomeDetailsActivity.class);
                intent.putExtra("csid",csid);
                intent.putExtra("phone",phone);
                activity.startActivity(intent);
            }
        });
        holder.place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//??????
                Map<String, String> info = mUserList.get(position);
                String workPool = info.get("workPool");
                com.alibaba.fastjson.JSONObject ziXunUserInfo = com.alibaba.fastjson.JSONObject.parseObject(workPool);
                ziXunUserInfo.put("isShouZi", isShouZi);
                Intent intent = new Intent(activity, NewAddOrderActivity.class);
                intent.putExtra("info", (Serializable) info);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }

    /**
     * ??????
     *
     * @param url
     * @param
     */
    private void beizhu(String url, String csId, String pId, String bId, boolean isShouZi) {
        Intent intent = new Intent(activity, DetailBeizhuActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("csId", csId);
        intent.putExtra("bId", bId);
        intent.putExtra("pId", pId);
        intent.putExtra("isShouZi", isShouZi);
        activity.startActivity(intent);
    }


    public void setData(List<Map<String, String>> userInfos) {
        this.mUserList = userInfos;
    }

    static class ViewHolder {
        ImageView iv_call;
        TextView tv_user_mumber;
        TextView tv_xiangmu;

        TextView bt_beizhu;
        TextView bt_xiangqing;
        TextView bt_huiku;
        ImageView iv_copy_phone;
        TextView customer_id;
        TextView place_order;

        public ViewHolder(View view) {
            iv_call = view.findViewById(R.id.iv_user_call);
            tv_user_mumber = view.findViewById(R.id.tv_user_number);
            tv_xiangmu = view.findViewById(R.id.tv_user_xinagmu);
            bt_beizhu = view.findViewById(R.id.bt_beizhu);
            bt_xiangqing = view.findViewById(R.id.bt_xiangqing);
            bt_huiku = view.findViewById(R.id.bt_huiku);
            iv_copy_phone = view.findViewById(R.id.iv_copy_phone);
            customer_id = view.findViewById(R.id.customer_id);
            place_order = view.findViewById(R.id.place_order);
        }

    }

    public UserChanceZiListAdapter(Activity activity, List<Map<String, String>> userInfos, boolean isShouZi, OneChanceFragment fragment) {
        this.isShouZi = isShouZi;
        this.activity = activity;
        mUserList = userInfos;
        this.oneFragment = fragment;
    }


    /**
     * ????????????????????????
     */
    private void getRealPhone(String urls, String work_id, int check) {
        try {
            String url = null;
            url = Constants.host + urls + "?work_id=" + work_id;
            OkHttpUtils.get(activity, url, new OkhttpCallback() {
                @Override
                public void onBefore() {
                    super.onBefore();
                }

                @Override
                public void onFailure(String message) {
                    Log.e("tag", "onFailure:4 " + message);
                    Toast.makeText(activity, message,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onGetResult(Object object) {
                }

                @Override
                public Object parseNetworkResponse(JSONObject object) throws
                        Exception {
                    int err = object.getInt("err");
                    if (err == 0) {
                        String phone = object.getString("phone");
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (check == 1) {//????????????
                                    checkDualSim(phone);
                                } else if (check == 2) {//??????
                                    /*if (isShouZi) {
                                        beizhu("other/firs_record?", work_id, phone, true);
                                    } else {
                                        beizhu("work/pool_record?", work_id, phone, false);
                                    }*/
                                }
                            }
                        });
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public String TAG = "UserDataZiAdapter";

    @RequiresApi(api = Build.VERSION_CODES.M)
    private int checkDualSim(String phoneNumber) {
      /*  if(Constants.isUpdatingFile){
//                ???????????????????????????
            if(MainActivity.mainContext != null){
                Toast.makeText(MainActivity.mainContext, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
            }
            return 0;
        }*/
        if (time > 0) {
            return 0;
        }
        //phoneNumber = "18613869712";
        int simNumber = 0;
        SubscriptionManager sm = SubscriptionManager.from(activity);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "no call phone permission", Toast.LENGTH_SHORT).show();
            return 0;
        }
        List<SubscriptionInfo> subs = sm.getActiveSubscriptionInfoList();
        if (subs == null) {
            Log.d(TAG, "checkDualSim: " + "no sim");
            PromptManager.showMyToast("no sim", activity);
            return simNumber;
        }
        if (subs.size() > 1) {
            simNumber = 2;
            callPhone(true, phoneNumber);
            Log.d(TAG, "checkDualSim: " + "two sims");
        } else {
            Log.d(TAG, "checkDualSim: " + "one sim");
            callPhone(false, phoneNumber);
            simNumber = 1;
        }
        for (SubscriptionInfo s : subs) {
            Log.d(TAG, "checkDualSim: " + "simInfo:" + subs.toString());
        }
        return simNumber;
    }


    //??????????????????????????????UI??????????????????????????????sim2??????????????????
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void callPhone(boolean isDualSim, String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "no call phone permission", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "callPhone: " + "no call phone permission");
            return;
        }

        checkIsCall(phoneNumber, isDualSim);//?????????
        //Tky????????????
//        callTky();
    }


    private void callTky() {
        String url = Constants.hosttky + "thirdparty/api/v1/makecall/external";
        try {
            HashMap<String, String> paramMap = new HashMap();
            paramMap.put("caller", "18613869712");
            paramMap.put("callee", "18710226070");
//            paramMap.put("originNumber","18710226070");
            TkyOkHttpUtils.postJson(activity, url, paramMap, new OkhttpCallback() {
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
                    if (object.getJSONObject("data") != null) {
                        String uuid = object.getJSONObject("data").getString("uuid");//callid????????????????????????
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    PhoneAccountHandle phoneAccountHandle;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkIsCall(String callNumber, boolean isDualSim) {
//        String callNumber = "18613869712";
        //??????????????????
        if (Constants.localCallNumber != null) {
            if (Constants.localCallNumber.contains("+")) {
                Constants.localCallNumber = Constants.localCallNumber.replace("+", "");
            }
            if (Constants.localCallNumber.contains("86")) {
                if (Constants.localCallNumber.substring(0, 2).equals("86")) {
                    Constants.localCallNumber = Constants.localCallNumber.substring(2, Constants.localCallNumber.length());
                }
            }
        }

        if (!isDualSim) {
            String number = new PhoneInfoUtils(activity).getNativePhoneNumber();//
            Log.i("UserDataZiListAdapter", number + "msg" + "iccid" + new PhoneInfoUtils(activity).getIccid());
            Constants.iccID = new PhoneInfoUtils(activity).getIccid();
            if (!TextUtils.isEmpty(number)) {
                if (number.contains("+")) {
                    Constants.localCallNumber = number.replace("+", "");
                    number = Constants.localCallNumber;
                }
                if (number.contains("86")) {
                    if (number.substring(0, 2).equals("86")) {
                        Constants.localCallNumber = number.substring(2, Constants.localCallNumber.length());
                        number = Constants.localCallNumber;
                    }
                }
            }

//                        getLeastCallHistory(callNumber,isDualSim);
            if (TextUtils.isEmpty(EnjoyPreference.readString(activity, Constants.iccID)) || EnjoyPreference.readString(activity, Constants.iccID).equals("0")) {
                //???????????????????????????????????????????????????
                getLeastCallHistory(callNumber, isDualSim);
            } else {
                String count = EnjoyPreference.readString(activity, Constants.iccID);
                if (Integer.valueOf(count) < COUN_CALL) {//+1
                    int countNum = Integer.valueOf(count) + 1;
                    EnjoyPreference.saveString(activity, Constants.iccID, countNum + "");
                    Constants.localCallNumber = number;

                    time = TIME_COUNT_DEFAULT;
                    mExecutorService.submit(runnable);//???????????????
                    Log.i("UserDataAdapter", "position" + 2);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + callNumber));
                    Constants.isShouZi = isShouZi;
                    activity.startActivity(intent);
                } else {
                    //?????????????????????????????????????????????
                    getLeastCallHistory(callNumber, isDualSim);
                }
            }
        } else {
            TelecomManager telecomManager = (TelecomManager) activity.getSystemService(Context.TELECOM_SERVICE);
            if (telecomManager != null) {

                //@param slotId      0:???1  1:???2
                int numb = new Random().nextInt(2);
                phoneAccountHandle = getPhoneAccountHandle(numb);//slotId
                Log.i("UserDataZiListAdapter", Constants.localCallNumber + "msg");
                if (!TextUtils.isEmpty(Constants.localCallNumber)) {
                    if (Constants.localCallNumber.contains("+")) {
                        Constants.localCallNumber = Constants.localCallNumber.replace("+", "");
                    }
                    if (Constants.localCallNumber.contains("86")) {
                        if (Constants.localCallNumber.substring(0, 2).equals("86")) {
                            Constants.localCallNumber = Constants.localCallNumber.substring(2, Constants.localCallNumber.length());
                        }
                    }
                }
                if (TextUtils.isEmpty(EnjoyPreference.readString(activity, Constants.iccID)) || EnjoyPreference.readString(activity, Constants.iccID).equals("0")) {
//                                TextUtils.isEmpty(EnjoyPreference.readString(activity,Constants.localCallNumber)) || (Constants.localCallNumber != null && Constants.localCallNumber.equals("0"))
                    //???????????????????????????????????????????????????
                    getLeastCallHistory(callNumber, isDualSim);
                } else {
                    String count = EnjoyPreference.readString(activity, Constants.iccID);
                    if (Integer.valueOf(count) < COUN_CALL) {//+1
                        int countNum = Integer.valueOf(count) + 1;
                        EnjoyPreference.saveString(activity, Constants.iccID, countNum + "");

                        time = TIME_COUNT_DEFAULT;
                        mExecutorService.submit(runnable);//???????????????
                        Log.i("UserDataAdapter", "position" + 3);
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callNumber));
                        intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Constants.isShouZi = isShouZi;
                        activity.startActivity(intent);
                    } else {
                        //?????????????????????????????????????????????
                        getLeastCallHistory(callNumber, isDualSim);
                    }
                }

            }
        }
               /* }else{
                    //?????????????????????????????????????????????
                    getLeastCallHistory(callNumber,isDualSim);
                }*/
//            }
//        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLeastCallHistory(final String phoneNumber, boolean isDualSim) {
        try {
            if (Constants.localCallNumber != null) {
                if (Constants.localCallNumber.contains("+")) {
                    Constants.localCallNumber = Constants.localCallNumber.replace("+", "");
                }
                if (Constants.localCallNumber.contains("86")) {
                    if (Constants.localCallNumber.substring(0, 2).equals("86")) {
                        Constants.localCallNumber = Constants.localCallNumber.substring(2, Constants.localCallNumber.length());
                    }
                }

            }
           /* if(phoneNumber != null){
                if(phoneNumber.contains("+")){
                    phoneNumber = phoneNumber.replace("+","");
                }

                if(phoneNumber.contains("86")){
                    if(phoneNumber.substring(0,2).equals("86")){
                        phoneNumber =  phoneNumber.substring(2,phoneNumber.length());
                    }
                }
            }*/
            String reqUrl = Constants.host + "work/call_list";
            HashMap<String, String> reqMap = new HashMap<>();
            reqMap.put("page", "1");
            reqMap.put("count", "100");
            reqMap.put("at", "1");
            reqMap.put("call_type", "0");
//            reqMap.put("s_phone",Constants.localCallNumber);
            reqMap.put("p_number", Constants.iccID);
            OkHttpUtils.post(null, reqUrl, reqMap, new OkhttpCallback() {
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
                    JSONObject obj = object.getJSONObject("date");
                    List<Map<String, String>> listMap = new ArrayList<>();
                    Object obj2 = obj.get("list");
                    if (!obj2.toString().equals("null")) {
                        listMap = JSONUtil.getListMap(obj.getJSONArray("list"));
                    }
                    if (listMap != null && listMap.size() > COUN_CALL) {
                        Message msg = Message.obtain();
                        msg.arg1 = 1001;
                        mHandler.sendMessage(msg);
                        EnjoyPreference.saveString(activity, Constants.iccID, listMap.size() + "");
                    } else {
                        EnjoyPreference.saveString(activity, Constants.iccID, (listMap.size() + 1) + "");
                        if (!isDualSim) {
                            String number = new PhoneInfoUtils(activity).getNativePhoneNumber();
                            if (!TextUtils.isEmpty(number)) {
                                if (number.contains("+")) {
                                    Constants.localCallNumber = number.replace("+", "");
                                    number = Constants.localCallNumber;
                                }
                                if (number.contains("86")) {
                                    if (number.substring(0, 2).equals("86")) {
                                        Constants.localCallNumber = number.substring(2, Constants.localCallNumber.length());
                                        number = Constants.localCallNumber;
                                    }
                                }
                            }
                            Constants.localCallNumber = number;
                            time = TIME_COUNT_DEFAULT;
                            mExecutorService.submit(runnable);//???????????????
                            Log.i("UserDataAdapter", "position" + 4);
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phoneNumber));
                            Constants.isShouZi = isShouZi;
                            activity.startActivity(intent);
                        } else {
                            TelecomManager telecomManager = (TelecomManager) activity.getSystemService(Context.TELECOM_SERVICE);
                            if (telecomManager != null) {

                                //@param slotId      0:???1  1:???2
//                                        double idCard = Math.random();idCard>0.7?1:0
                                       /* int numb = new Random().nextInt(2);
                                        PhoneAccountHandle phoneAccountHandle = getPhoneAccountHandle(numb);//slotId*/
                                time = TIME_COUNT_DEFAULT;
                                mExecutorService.submit(runnable);//???????????????
                                Log.i("UserDataAdapter", "position" + 1);
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                                intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Constants.isShouZi = isShouZi;
                                activity.startActivity(intent);
                            }
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

  /*  Handler handlerThree=new Handler(Looper.getMainLooper());
                                    handlerThree.post(new Runnable(){
        public void run(){
            PromptManager.showMyToast(Constants.localCallNumber+"??????????????????????????????24???,???????????????",CallService.this);
        }
    });*/

    Handler mHandler = new Handler() {

        //handleMessage????????????????????????
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1001) {

                View view = View.inflate(activity, R.layout.call_warn_dialog, null);
                TextView tv_to_bind_warn = view.findViewById(R.id.tv_to_bind_warn);
                if (Constants.localCallNumber != null) {
                    tv_to_bind_warn.setText(Constants.localCallNumber + "????????????????????????????????????????????????" + COUN_CALL + "?????????????????????????????????????????????!");
                } else {
                    tv_to_bind_warn.setText(Constants.iccID + "????????????????????????????????????????????????" + COUN_CALL + "?????????????????????????????????????????????!");
                }

                view.findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PromptManager.closeCustomDialog();

                    }
                });
                view.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PromptManager.closeCustomDialog();
                    }
                });

                PromptManager.showCustomDialog(activity, view, Gravity.CENTER, Gravity.CENTER);
                //PromptManager.showMyToast(Constants.localCallNumber+"??????????????????????????????24???,???????????????",CallService.this);
            }
        }
    };

    /**
     * ????????????????????????????????????sim??? PhoneAccountHandle ????????? PhoneAccountHandle ????????????sim???, ???????????? slotId ??????????????????sim??????????????? PhoneAccountHandle (??????5.1 ??? 6.0??????????????????)
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private PhoneAccountHandle getPhoneAccountHandle(int slotId) {
        TelecomManager tm = (TelecomManager) activity.getSystemService(Context.TELECOM_SERVICE);
        //PhoneAccountHandle api>5.1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (tm != null) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(activity, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                }
                List<PhoneAccountHandle> handles = tm.getCallCapablePhoneAccounts();

//            List<PhoneAccountHandle> handles = (List<PhoneAccountHandle>) ReflectUtil.invokeMethod(tm, "getCallCapablePhoneAccounts");
                SubscriptionManager sm = SubscriptionManager.from(activity);
                if (handles != null) {
                    for (PhoneAccountHandle handle : handles) {
                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Toast.makeText(activity, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                        SubscriptionInfo info = sm.getActiveSubscriptionInfoForSimSlotIndex(slotId);
                        if (info != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (TextUtils.equals(info.getIccId(), handle.getId())) {
                                    localCallNumber = info.getNumber();
                                   /* if(localCallNumber == null){
                                        localCallNumber = info.getIccId();
                                    }*/
                                    Constants.localCallNumber = localCallNumber;
                                    Constants.iccID = info.getIccId();
                                    Log.e(TAG, "getPhoneAccountHandle for slot" + slotId + " " + handle + Constants.localCallNumber);
                                    return handle;
                                }
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                if (TextUtils.equals(info.getSubscriptionId() + "", handle.getId())) {
                                    localCallNumber = info.getNumber();
//                                    getSimSerialNumber
                                  /*  if(localCallNumber == null){
                                        localCallNumber = info.getIccId();
                                    }*/
                                    Constants.localCallNumber = localCallNumber;
                                    Constants.iccID = info.getIccId();
//                                    Constants.localCallNumber = tm.getLine1Number(handle);//?????????????????????????????????
                                    Log.e(TAG, "getPhoneAccountHandle for slot" + slotId + " " + handle + Constants.localCallNumber);
                                    return handle;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * @param url
     * @param w_id
     */

    private void huiku(String url, String w_id, boolean isShou, int position) {
        int logic = 1;
        if (isShou){
            logic=1;
        }else {
            logic=2;
        }
        String reqUrl = Constants.allhost + url + "cs_id=" + w_id + "&logic=" + logic;
        OkHttpUtils.newGet(activity, reqUrl, new OkhttpCallback() {
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
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "????????????",
                                Toast.LENGTH_SHORT).show();
                        oneFragment.countNumber--;
                        oneFragment.tv_count_number.setText("???" + oneFragment.countNumber + "???");
                        //?????????????????????
                        oneFragment.userInfos.remove(position);
                        oneFragment.adapter.setData(oneFragment.userInfos);
                        oneFragment.adapter.notifyDataSetChanged();

                        if (mUserList.size() == 0 && oneFragment.recycle_view != null) {
                            oneFragment.recycle_view.setVisibility(View.GONE);
                            oneFragment.ll_refresh.setVisibility(View.VISIBLE);
                        } else {
                            if (oneFragment.ll_refresh != null) {
                                oneFragment.ll_refresh.setVisibility(View.GONE);
                                oneFragment.recycle_view.setVisibility(View.VISIBLE);
                            }
                        }
                        oneFragment.adapter.notifyDataSetChanged();


                    }
                });

                return null;
            }
        });

    }
}