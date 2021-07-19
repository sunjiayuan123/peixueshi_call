package com.peixueshi.crm.ui.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.peixueshi.crm.R;
import com.peixueshi.crm.bean.CertificateBean;
import com.peixueshi.crm.newactivity.NewAddOrderActivity;

import java.util.List;

public class NewSelectOtherAdapter extends RecyclerView.Adapter<NewSelectOtherAdapter.ViewHolder> {
    public String TAG = "UserDataZiAdapter";

    private Context activity;
    private int prices;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView xie_pro_detail;
        EditText et_price;
        TextView xie_name_detail;
        TextView xie_card_detail;
        TextView xie_census_detail;
        TextView remove_pro;
        public ViewHolder(View view) {
            super(view);
            xie_pro_detail = view.findViewById(R.id.other_pro_detail);//证书名称

            xie_name_detail = view.findViewById(R.id.other_name_detail);//姓名

            xie_card_detail = view.findViewById(R.id.card_name_detail);//身份证号

            xie_census_detail = view.findViewById(R.id.other_census_detail);//户籍

            et_price = view.findViewById(R.id.et_price);//价格
            remove_pro = view.findViewById(R.id.remove_pro);//移除
        }

    }

    List<CertificateBean> listmap;

    public int otherPrice(){
        return prices;
    }
    public NewSelectOtherAdapter(Context activity,List<CertificateBean> listmap) {
        this.activity = activity;
        this.listmap = listmap;
    }

    public void setData(List<CertificateBean> listmaps) {
        this.listmap = listmaps;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_select_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CertificateBean info = listmap.get(position);
        holder.xie_pro_detail.setText(info.getName());
        holder.xie_card_detail.setText(info.getIdCard());
        holder.xie_name_detail.setText(info.getStu_name());
        holder.xie_census_detail.setText(info.getAddress());
        if (TextUtils.isEmpty(info.getNewPrice())){
            info.setNewPrice("0");
            listmap.set(position, info);
            NewAddOrderActivity.setOtherPrice(listmap);
        }

       // holder.xie_area_detail.setText(info.getRegistrationArea());


        holder.remove_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnItemClickListener(position);
            }
        });
        holder.et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CertificateBean info = listmap.get(position);
                info.setNewPrice(s.toString());
                listmap.set(position, info);
                //setData(listmap);
                NewAddOrderActivity.setOtherPrice(listmap);
            }
        });
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