package com.peixueshi.crm.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.peixueshi.crm.R;
import com.peixueshi.crm.fragment.OneFragmentRanking;

import java.util.List;
import java.util.Map;

public class UserDataRankingAdapter extends BaseAdapter {

    private boolean isShouZi = false;
    private List<Map<String,String>>  mUserList;

    private Activity activity;
    private OneFragmentRanking oneFragment;

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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item_info, parent, false);
            holder = new ViewHolder(convertView);
           // holder.textView = (TextView)convertView.findViewById(R.id.tv_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        Map<String,String> ziXunUserInfo = mUserList.get(position);
        holder.tv_user_money.setText("成交业绩:"+new Double(Integer.valueOf(ziXunUserInfo.get("sum3")))/new Double(100)+"");
        holder.tv_user_name.setText(ziXunUserInfo.get("name"));
        holder.tv_count.setText(ziXunUserInfo.get("count2")+"单");

        if(position == 0 || position == 1 || position ==2){
            holder.tv_number.setTextColor(activity.getResources().getColor(R.color.white));
            holder.tv_number.setBackgroundResource(R.drawable.rank_third);
        }else{
            holder.tv_number.setTextColor(activity.getResources().getColor(R.color.text_common_balck));
            holder.tv_number.setBackgroundResource(R.color.white);
        }
        holder.tv_number.setText("NO."+(position+1));
        return convertView;
    }


    public void setData(List<Map<String,String>> userInfos) {
        this.mUserList = userInfos;
    }

    static class ViewHolder {
        ImageView iv_call;
        TextView tv_user_name;
        TextView tv_user_money;
        TextView tv_number;
        TextView tv_count;




        public ViewHolder(View view) {
            iv_call = view.findViewById(R.id.iv_user_call);
            tv_user_name =  view.findViewById(R.id.tv_user_name);
            tv_user_money =  view.findViewById(R.id.tv_user_money);
            tv_number =  view.findViewById(R.id.tv_number);
            tv_count =  view.findViewById(R.id.tv_count);

        }

    }

    public UserDataRankingAdapter(Activity activity, List<Map<String,String>>  userInfos, boolean isShouZi, OneFragmentRanking fragment) {
        this.isShouZi = isShouZi;
        this.activity = activity;
        mUserList = userInfos;
        this.oneFragment = fragment;
    }




    public String TAG = "UserDataZiAdapter";


}