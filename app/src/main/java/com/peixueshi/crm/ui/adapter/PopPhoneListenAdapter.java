package com.peixueshi.crm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.peixueshi.crm.R;
import com.peixueshi.crm.utils.Util;

import java.util.List;
import java.util.Map;

public class PopPhoneListenAdapter extends BaseAdapter {

    private List<Map<String,String>> mapInfos;

    private Context activity;

    public void setData(List<Map<String, String>> listPidInfo) {
        this.mapInfos = listPidInfo;
    }

    @Override
    public int getCount() {
        return mapInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mapInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_phone_lister_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        Map<String,String> info = mapInfos.get(position);
        holder.tv_xiangmu.setText(info.get("cord_name"));
        holder.tv_phone.setText(info.get("cstu_phone"));
        holder.tv_lister_time.setText(Util.getTime(Integer.valueOf(info.get("cord_lecture_at")))+"分");
        holder.tv_lister_at.setText(Util.stampToDate(info.get("stuinfo_at")));

        if(mListener!=null){
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClickListener(position);
                }
            });
        }
        return convertView;
    }

    static class ViewHolder{
        TextView tv_phone;
        TextView tv_xiangmu;
        TextView tv_lister_time;
        TextView tv_lister_at;

        public ViewHolder(View view) {
            tv_phone =  view.findViewById(R.id.tv_phone);
            tv_xiangmu =  view.findViewById(R.id.tv_xiangmu);
            tv_lister_time = view.findViewById(R.id.tv_lister_time);
            tv_lister_at =  view.findViewById(R.id.tv_lister_at);
        }

    }

    public PopPhoneListenAdapter(Context activity, List<Map<String,String>> infos) {
        this.activity = activity;
        mapInfos = infos;
    }

  /*  @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_phone_lister_item, parent, false);
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            Map<String,String> info = mapInfos.get(position);
            holder.tv_xiangmu.setText(info.get("cord_name"));
            holder.tv_phone.setText(info.get("cstu_phone"));
            holder.tv_lister_time.setText("听课时长:  "+Util.getTime(Integer.valueOf(info.get("cord_lecture_at")))+"分钟");
            holder.tv_lister_at.setText("听课时间:  "+Util.stampToDate(info.get("stuinfo_at")));

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
*/
    public interface OnItemClickListener{
        void OnItemClickListener(int pos);
    }
    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener mListener){
        this.mListener=mListener;
    }
    public String TAG = "UserDataZiAdapter";

}