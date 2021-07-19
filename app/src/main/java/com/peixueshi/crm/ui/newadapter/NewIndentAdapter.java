package com.peixueshi.crm.ui.newadapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peixueshi.crm.R;
import com.peixueshi.crm.newactivity.NewIndentDetailActivity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class NewIndentAdapter extends RecyclerView.Adapter<NewIndentAdapter.ViewHolder> {
    public String TAG = "UserDataZiAdapter";
    private Context activity;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView indent_mark;
        RelativeLayout indent_rl;
        TextView relevance;
        public ViewHolder(View view) {
            super(view);
            indent_mark = (TextView) view.findViewById(R.id.indent_mark);
            indent_rl = (RelativeLayout) view.findViewById(R.id.indent_rl);
            relevance = (TextView) view.findViewById(R.id.relevance);
        }

    }
    List<Map<String, String>> mapList;

    public NewIndentAdapter(Context activity, List<Map<String, String>> mapList) {
        this.activity = activity;
        this.mapList = mapList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_indent_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, String> stringStringMap = mapList.get(position);
        String billNo = stringStringMap.get("billNo");
        holder.indent_mark.setText("订单号:"+billNo);
        holder.indent_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //已存在定金订单,跳转查看详情
                Intent intent = new Intent(activity, NewIndentDetailActivity.class);
                intent.putExtra("stringStringMap", (Serializable) stringStringMap);
                activity.startActivity(intent);
            }
        });
        if (position==0){
            holder.relevance.setVisibility(View.VISIBLE);
            holder.relevance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String relevance = holder.relevance.getText().toString();
                    int type=0;
                    if (relevance.equals("关联")){
                        holder.relevance.setText("取消关联");
                        type=1;
                    }else {
                        holder.relevance.setText("关联");
                        type=2;
                    }
                    mListener.OnItemClickListener(position,type);
                }
            });
        }else {
            holder.relevance.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface OnItemClickListener{
        void OnItemClickListener(int pos,int type);
    }
    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener mListener){
        this.mListener=mListener;
    }
}