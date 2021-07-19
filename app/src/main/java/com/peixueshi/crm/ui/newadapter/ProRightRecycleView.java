package com.peixueshi.crm.ui.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.peixueshi.crm.R;
import com.peixueshi.crm.ui.adapter.PopLeftRecycleView;


import java.util.List;
import java.util.Map;

public class ProRightRecycleView extends RecyclerView.Adapter<ProRightRecycleView.ViewHolder> {

    private List<Map<String,String>> mapInfos;

    private Context activity;

    public void setData(List<Map<String, String>> listPidInfo) {
        this.mapInfos = listPidInfo;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_banxing;
        TextView tv_price;

        public ViewHolder(View view) {
            super(view);
            tv_banxing = (TextView) view.findViewById(R.id.tv_banxing);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
        }

    }

    public ProRightRecycleView(Context activity, List<Map<String,String>> infos) {
        this.activity = activity;
        mapInfos = infos;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_right_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

       /* holder.iv_call.setOnClickListener(new View.OnClickListener() {
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
        });*/
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

          Map<String,String> info = mapInfos.get(position);
        if (info.containsKey("class")){
            String aClass = info.get("class");
            JSONObject jsonObject1 = JSONObject.parseObject(aClass);
            try {
                String name = jsonObject1.getString("name");
                int price = jsonObject1.getIntValue("price");
                holder.tv_banxing.setText(name);
                holder.tv_price.setText(price+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
           /* holder.tv_banxing.setText(info.get("name"));
            holder.tv_price.setText(info.get("price"));*/
           if(mListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClickListener(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mapInfos.size();
    }

    public interface OnItemClickListener{
        void OnItemClickListener(int pos);
    }
    private PopLeftRecycleView.OnItemClickListener mListener;
    public void setOnItemClickListener(PopLeftRecycleView.OnItemClickListener mListener){
        this.mListener=mListener;
    }
    public String TAG = "UserDataZiAdapter";

}