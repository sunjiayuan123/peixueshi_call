package com.peixueshi.crm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peixueshi.crm.R;

import java.util.List;
import java.util.Map;

public class PopPhoneHistoryAdapter extends RecyclerView.Adapter<PopPhoneHistoryAdapter.ViewHolder> {

    private List<Map<String,String>> mapInfos;

    private Context activity;

    public void setData(List<Map<String, String>> listPidInfo) {
        this.mapInfos = listPidInfo;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_xiangmu;
        TextView tv_xiaozu;
        TextView tv_zuoxi;

        public ViewHolder(View view) {
            super(view);
            tv_xiangmu = (TextView) view.findViewById(R.id.tv_xiangmu);
            tv_xiaozu = (TextView) view.findViewById(R.id.tv_xiaozu);
            tv_zuoxi = (TextView) view.findViewById(R.id.tv_zuoxi);
        }

    }

    public PopPhoneHistoryAdapter(Context activity, List<Map<String,String>> infos) {
        this.activity = activity;
        mapInfos = infos;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_phone_history_item, parent, false);
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
            holder.tv_xiangmu.setText(info.get("pr_name"));
            holder.tv_xiaozu.setText(info.get("te_name"));
            holder.tv_zuoxi.setText(info.get("emp_name"));

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

}