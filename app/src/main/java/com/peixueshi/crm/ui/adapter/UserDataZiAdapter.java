package com.peixueshi.crm.ui.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.peixueshi.crm.R;
import com.peixueshi.crm.bean.ZiXunUserInfo;
import java.util.List;

public class UserDataZiAdapter extends RecyclerView.Adapter<UserDataZiAdapter.ViewHolder> {

    private boolean isShouZi = true;
    private List<ZiXunUserInfo> mUserList;

    private Activity activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_call;
        TextView tv_user_mumber;
        TextView tv_xiangmu;

        public ViewHolder(View view) {
            super(view);
            iv_call = (ImageView) view.findViewById(R.id.iv_user_call);
            tv_user_mumber = (TextView) view.findViewById(R.id.tv_user_number);
            tv_xiangmu = (TextView) view.findViewById(R.id.tv_user_xinagmu);
        }

    }

    public UserDataZiAdapter(Activity activity, List<ZiXunUserInfo> userInfos, boolean isShouZi) {
        this.isShouZi = isShouZi;
        this.activity = activity;
        mUserList = userInfos;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zixun_item_info, parent, false);
        ViewHolder holder = new ViewHolder(view);

        holder.iv_call.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                ZiXunUserInfo info = mUserList.get(position);
                Toast.makeText(view.getContext(), "you call " + info.getWf_phone(), Toast.LENGTH_SHORT).show();
                String number;
                if (isShouZi) {
                    number = info.getWf_phone();
                } else {
                    number = info.getWp_phone();
                }

                checkDualSim(number);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ZiXunUserInfo ziXunUserInfo = mUserList.get(position);
        if (isShouZi) {
            String number = ziXunUserInfo.getWf_phone().substring(0,ziXunUserInfo.getWf_phone().length()-4)+"****";
            holder.tv_user_mumber.setText(number);
            holder.tv_xiangmu.setText(ziXunUserInfo.getWf_p_name());
        } else {
            String number = ziXunUserInfo.getWp_phone().substring(0,ziXunUserInfo.getWp_phone().length()-4)+"****";
            holder.tv_user_mumber.setText(number);
            holder.tv_xiangmu.setText(ziXunUserInfo.getWp_pname());
        }

    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }



    public String TAG = "UserDataZiAdapter";

    @RequiresApi(api = Build.VERSION_CODES.M)
    private int checkDualSim(String phoneNumber) {
        phoneNumber = "18613869712";
        int simNumber = 0;
        SubscriptionManager sm = SubscriptionManager.from(activity);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity,"no call phone permission",Toast.LENGTH_SHORT).show();
            return 0;
        }
        List<SubscriptionInfo> subs = sm.getActiveSubscriptionInfoList();
        if (subs == null) {
            Log.d(TAG, "checkDualSim: " + "no sim");
            return simNumber;
        }
        if (subs.size() > 1) {
            simNumber = 2;
            callPhone(true,phoneNumber);
            Log.d(TAG, "checkDualSim: " + "two sims");
        } else {
            Log.d(TAG, "checkDualSim: " + "one sim");
            callPhone(false,phoneNumber);
            simNumber = 1;
        }
        for (SubscriptionInfo s: subs) {
            Log.d(TAG, "checkDualSim: " + "simInfo:" + subs.toString());
        }
        return simNumber;
    }



    //根据上述情况，初始化UI和拨打电话，尤其注意sim2的打电话情况
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void callPhone(boolean isDualSim, String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity,"no call phone permission",Toast.LENGTH_SHORT).show();
            Log.d(TAG, "callPhone: " + "no call phone permission");
            return;
        }
        if (!isDualSim) {
            //单卡
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            activity.startActivity(intent);
            return;
        }
        TelecomManager telecomManager = (TelecomManager)activity.getSystemService(Context.TELECOM_SERVICE);
        if(telecomManager != null) {
           /* List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();
            Log.d(TAG, "callPhone: " + phoneAccountHandleList);
            Log.d(TAG, "callPhone: " + phoneAccountHandleList.get(1).toString());
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            int number = Math.random()>0.5?1:0;
            intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandleList.get(Math.random()>0.5?1:0));
            activity.startActivity(intent);*/

            //@param slotId      0:卡1  1:卡2
            PhoneAccountHandle phoneAccountHandle = getPhoneAccountHandle(Math.random()>0.5?1:0);//slotId
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }

    /**
     这一块首先获取手机中所有sim卡 PhoneAccountHandle 每一个 PhoneAccountHandle 表示一个sim卡, 然后根据 slotId 判断所指定的sim卡并返回此 PhoneAccountHandle (这里5.1 和 6.0需要区分对待)
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
                    Toast.makeText(activity, "请允许拨号权限后再试", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(activity, "请允许拨号权限后再试", Toast.LENGTH_SHORT).show();
                        }
                        SubscriptionInfo info = sm.getActiveSubscriptionInfoForSimSlotIndex(slotId);
                        if (info != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (TextUtils.equals(info.getIccId(), handle.getId())) {
                                    Log.e(TAG, "getPhoneAccountHandle for slot" + slotId + " " + handle);
                                    return handle;
                                }
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                if (TextUtils.equals(info.getSubscriptionId() + "", handle.getId())) {
                                    Log.e(TAG, "getPhoneAccountHandle for slot" + slotId + " " + handle);
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

}