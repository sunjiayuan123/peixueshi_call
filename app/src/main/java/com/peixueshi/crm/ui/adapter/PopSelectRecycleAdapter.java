package com.peixueshi.crm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.peixueshi.crm.R;
import com.peixueshi.crm.activity.OrderActivity;

import java.util.List;
import java.util.Map;

public class PopSelectRecycleAdapter extends RecyclerView.Adapter<PopSelectRecycleAdapter.ViewHolder> {

    private List<Map<String,String>> mapInfos;

    private EditText et_count;
    private Context activity;

    public void setData(List<Map<String, String>> listPidInfo) {
        this.mapInfos = listPidInfo;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_banxing;
        EditText et_price;
        TextView tv_kemu;

        public ViewHolder(View view) {
            super(view);
            tv_banxing = (TextView) view.findViewById(R.id.tv_banxing);
            et_price = (EditText) view.findViewById(R.id.et_price);
            tv_kemu = (TextView) view.findViewById(R.id.tv_kemu);
        }

    }

    public PopSelectRecycleAdapter(Context activity, List<Map<String,String>> infos,EditText et_count_money) {
        this.activity = activity;
        mapInfos = infos;
        this.et_count = et_count_money;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_select_item, parent, false);
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
            holder.tv_banxing.setText(info.get("c_name"));
            holder.et_price.setText(info.get("c_pay_price"));//info.get("c_price")
            if(info.containsKey("p_name")){
                holder.tv_kemu.setText(info.get("p_name"));
            }
        if(info.containsKey("pr_name")){
            holder.tv_kemu.setText(info.get("pr_name"));
        }


        if(mListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnItemClickListener(position);
                }
            });
        }
      holder.et_price.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                int money = 0;
                if(holder.et_price.getText().toString().trim().equals("")){
                    money = 0;
                }else{
                    money = Integer.valueOf(holder.et_price.getText().toString().trim());
                }

                    OrderActivity.selectKemu.get(position).put("c_pay_price",money+"");
                    int countPay = 0;
                    for(int i=0;i<OrderActivity.selectKemu.size();i++){
                        countPay = countPay+Integer.valueOf(OrderActivity.selectKemu.get(i).get("c_pay_price"));

                    }
                    OrderActivity.countWangke = countPay;
                    int count = OrderActivity.countWangke+OrderActivity.countXueli+OrderActivity.countXiezhu+OrderActivity.countZhiqu;
                    et_count.setText((count)+"");
                }


        });

    }

    @Override
    public int getItemCount() {
        return mapInfos.size();
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
        void OnItemClickListener(int pos);
    }
    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener mListener){
        this.mListener=mListener;
    }
    public String TAG = "UserDataZiAdapter";

}