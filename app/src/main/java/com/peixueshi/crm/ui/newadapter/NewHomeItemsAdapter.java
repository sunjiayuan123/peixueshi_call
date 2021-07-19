package com.peixueshi.crm.ui.newadapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peixueshi.crm.R;
import com.peixueshi.crm.bean.DetailsBean;
import com.peixueshi.crm.bean.NewOrderUserInfo;

import java.util.List;

public class NewHomeItemsAdapter extends RecyclerView.Adapter<NewHomeItemsAdapter.ViewHolder> {
    public String TAG = "UserDataZiAdapter";
    private Context activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_mark_text;
        RecyclerView recycle_view;


        public ViewHolder(View view) {
            super(view);
            order_mark_text = view.findViewById(R.id.order_mark_text);//订单号
            recycle_view = view.findViewById(R.id.recycle_view);

        }
    }

    List<NewOrderUserInfo> list;

    public NewHomeItemsAdapter(Context activity, List<NewOrderUserInfo> list) {
        this.activity = activity;
        this.list = list;
    }

    public void setData(List<NewOrderUserInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wang_home_details, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    int prices;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewOrderUserInfo newOrderUserInfo = list.get(position);
        int billType = newOrderUserInfo.getOrderType();////
        String billNo = newOrderUserInfo.getBillNo();
        List<DetailsBean> details = newOrderUserInfo.getDetails();
        holder.order_mark_text.setText(billNo);
        NewHomeDetailsAdapter newHomeDetailsAdapter = new NewHomeDetailsAdapter(activity, details,billType);
        holder.recycle_view.setLayoutManager(new LinearLayoutManager(activity));
        holder.recycle_view.setAdapter(newHomeDetailsAdapter);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int wangPrice() {
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