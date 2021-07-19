package com.peixueshi.crm.ui.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peixueshi.crm.R;

import java.util.List;
import java.util.Map;

public class PopXueliLeftRecycleView extends RecyclerView.Adapter<PopXueliLeftRecycleView.ViewHolder> {

    private List<Map<String,String>> mapInfos;

    private Context activity;

    public void setData(List<Map<String, String>> listPidInfo) {
        this.mapInfos = listPidInfo;
    }
    private int selectedPos = 0;
    private int oldPos = -1;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_left_name;

        public ViewHolder(View view) {
            super(view);
            tv_left_name = (TextView) view.findViewById(R.id.tv_left_name);
        }

    }

    public PopXueliLeftRecycleView(Context activity, List<Map<String,String>> infos) {
        this.activity = activity;
        mapInfos = infos;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_left_item, parent, false);
        ViewHolder holder = new ViewHolder(view);


        /*holder.iv_call.setOnClickListener(new View.OnClickListener() {
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
        holder.tv_left_name.setText(info.get("name"));

        if(mListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClickListener(position);
                }
            });
        }

        if(selectedPos == position) {
            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.line_color));
        }else{
            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.white));
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