package com.peixueshi.crm.ui.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.peixueshi.crm.R;
import com.peixueshi.crm.bean.HelpSignBean;

import java.util.List;

public class NewEamesttXieAdapter extends RecyclerView.Adapter<NewEamesttXieAdapter.ViewHolder> {
    public String TAG = "UserDataZiAdapter";

    private Context activity;
    int prices;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView xie_pro_detail;
        TextView xie_area_detail;
        EditText et_price;
        TextView xie_name_detail;
        TextView xie_card_detail;
        TextView xie_census_detail;
        TextView remove_pro;
        public ViewHolder(View view) {
            super(view);
            xie_pro_detail = view.findViewById(R.id.xie_pro_detail);//项目名称
            xie_area_detail = view.findViewById(R.id.xie_area_detail);//报考区域

            xie_name_detail = view.findViewById(R.id.xie_name_detail);//姓名

            xie_card_detail = view.findViewById(R.id.xie_card_detail);//身份证号

            xie_census_detail = view.findViewById(R.id.xie_census_detail);//户籍

            et_price = view.findViewById(R.id.et_price);//价格
            remove_pro = view.findViewById(R.id.remove_pro);//移除
        }

    }

    List<HelpSignBean> listmap;

    public NewEamesttXieAdapter(Context activity, List<HelpSignBean> listmap) {
        this.activity = activity;
        this.listmap = listmap;
    }

    public void setData(List<HelpSignBean> listmaps) {
        this.listmap = listmaps;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.xie_eamest_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HelpSignBean info = listmap.get(position);
        holder.xie_pro_detail.setText(info.getpName());
        holder.xie_card_detail.setText(info.getIdCard());
        holder.xie_name_detail.setText(info.getStu_name());
        holder.xie_census_detail.setText(info.getAddress());
        holder.remove_pro.setText("￥"+new Double(info.getNewPrice())/new Double(100)+"");
        holder.xie_area_detail.setText(info.getRegistrationArea());

    }

       /* holder.indent_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //已存在定金订单,跳转查看详情
                Intent intent = new Intent(activity, NewIndentDetailActivity.class);
                activity.startActivity(intent);
            }
        });*/

    @Override
    public int getItemCount() {
        return listmap.size();
    }

    public int xiePrice(){
        return this.prices;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int pos);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }
}