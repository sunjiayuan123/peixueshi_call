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

public class PopSelectXueliRecycleAdapter extends RecyclerView.Adapter<PopSelectXueliRecycleAdapter.ViewHolder> {

    private List<Map<String,String>> mapInfos;

    private Context activity;
    private EditText et_count;

    public void setData(List<Map<String, String>> listPidInfo) {
        this.mapInfos = listPidInfo;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_school;
        TextView tv_banxing;
        EditText et_price;
        TextView tv_dengji;

        public ViewHolder(View view) {
            super(view);
            tv_banxing = (TextView) view.findViewById(R.id.tv_banxing);
            tv_school = (TextView) view.findViewById(R.id.tv_school);
            et_price = (EditText) view.findViewById(R.id.et_price);
            tv_dengji = (TextView) view.findViewById(R.id.tv_dengji);
        }

    }

    public PopSelectXueliRecycleAdapter(Context activity, List<Map<String,String>> infos, EditText et_count_money) {
        this.activity = activity;
        mapInfos = infos;
        this.et_count = et_count_money;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_select_xueli_item, parent, false);
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
            holder.tv_banxing.setText(info.get("m_name"));
            holder.tv_school.setText(info.get("s_name"));
            holder.et_price.setText(info.get("c_pay_price"));
        int status = Integer.valueOf(info.get("m_status"));
        if(status == 1){
            holder.tv_dengji.setText("大专");
        }else if(status == 2){
            holder.tv_dengji.setText("中专");
        }else{
            holder.tv_dengji.setText("本科");
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
                    money =0;
                }else{
                    money = Integer.valueOf(holder.et_price.getText().toString().trim());
                }
                    OrderActivity.selectXueli.get(position).put("c_pay_price",money+"");
                    int countPay = 0;
                    for(int i=0;i<OrderActivity.selectXueli.size();i++){
                        countPay = countPay+Integer.valueOf(OrderActivity.selectXueli.get(i).get("c_pay_price"));

                    }
                    OrderActivity.countXueli = countPay;
                    int count = OrderActivity.countWangke+OrderActivity.countXueli+OrderActivity.countXiezhu+OrderActivity.countZhiqu;
                    et_count.setText((count)+"");


            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
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