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

import java.util.List;
import java.util.Map;

public class NewXueliRightRecycleView extends RecyclerView.Adapter<NewXueliRightRecycleView.ViewHolder> {

    private List<Map<String,String>> mapInfos;

    private Context activity;
    private int selectedPos = 0;
    private int oldPos = -1;
    private int type = 0;
    public void setData(List<Map<String, String>> listPidInfo) {
        this.mapInfos = listPidInfo;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_banxing;
        TextView tv_dengji;
        TextView tv_price;

        public ViewHolder(View view) {
            super(view);
            tv_banxing = (TextView) view.findViewById(R.id.tv_banxing);
            tv_dengji = (TextView) view.findViewById(R.id.tv_dengji);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
        }

    }

    public NewXueliRightRecycleView(Context activity, List<Map<String,String>> infos) {
        this.activity = activity;
        mapInfos = infos;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_right_xueli_item, parent, false);
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
        String sch = info.get("sch");
        JSONObject jsonObject1 = JSONObject.parseObject(sch);
        String major = info.get("major");//专业
        JSONObject major_xueli = JSONObject.parseObject(major);
        try {
            String name = major_xueli.getString("name");
            String price = major_xueli.getString("price");
            int status = major_xueli.getIntValue("status");
            holder.tv_banxing.setText(name);
            holder.tv_price.setText(price+"");
            if(status == 1){
                holder.tv_dengji.setText("大专");
            }else if(status == 2){
                holder.tv_dengji.setText("中专");
            }else{
                holder.tv_dengji.setText("本科");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        };


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
    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener mListener){
        this.mListener=mListener;
    }
    public String TAG = "UserDataZiAdapter";

    public void refreshItem(int position) {
        if (selectedPos != -1) {
            oldPos  = selectedPos;
        }
        selectedPos = position;
        if (oldPos != -1) {
            notifyItemChanged(oldPos);
        }
        notifyItemChanged(selectedPos);
    }

}