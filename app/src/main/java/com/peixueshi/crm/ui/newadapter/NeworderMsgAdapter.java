package com.peixueshi.crm.ui.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peixueshi.crm.R;
import com.peixueshi.crm.utils.Util;

import java.util.List;
import java.util.Map;

public class NeworderMsgAdapter extends RecyclerView.Adapter<NeworderMsgAdapter.ViewHolder> {
    public String TAG = "UserDataZiAdapter";
    private Context activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView xiangmu_detail;
        TextView type;
        TextView postion_detail;
        TextView tv_user_name;
        TextView tv_position;
        TextView zftime;
        TextView xdtime;
        TextView bz_text;

        public ViewHolder(View view) {
            super(view);
            xiangmu_detail = (TextView) view.findViewById(R.id.xiangmu_detail);//订单号
            type = (TextView) view.findViewById(R.id.type);//支付状态
            postion_detail = (TextView) view.findViewById(R.id.postion_detail);//支付类型
            tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);//下单金额
            tv_position = (TextView) view.findViewById(R.id.tv_position);//支付方式
            zftime = (TextView) view.findViewById(R.id.zftime);//支付时间
            xdtime = (TextView) view.findViewById(R.id.xdtime);//下单时间
            bz_text = (TextView) view.findViewById(R.id.bz_text);//备注
        }

    }

    List< Map<String, String>> list;

    public NeworderMsgAdapter(Context activity, List<Map<String, String>> list) {
        this.activity = activity;
        this.list = list;
    }

    public void setData(List<Map<String, String>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wang_order_msg_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    int prices;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, String> stringStringMap = list.get(position);
        String billNo = stringStringMap.get("billNo");//订单号
        holder.xiangmu_detail.setText(billNo);
        String status = stringStringMap.get("status");//支付状态
        // 1 未支付 2 已支付 3 已分配 4已驳回 5手动取消 6自动取消
        if (status.equals("1")){// 1 未支付
            holder.type.setText("未支付");
        }else if (status.equals("2")){//2 已支付
            holder.type.setText("已支付");
        }else if (status.equals("3")){//3 已分配

        }else if (status.equals("4")){//4已驳回

        }else if (status.equals("5")){//5手动取消

        }else if (status.equals("6")){//6自动取消

        }
        String billType = stringStringMap.get("billType");//支付类型
        // 1全款 2尾款 3定金
        if (billType.equals("1")){//1全款
            holder.postion_detail.setText("全款");
        }else if (billType.equals("2")){//2尾款
            holder.postion_detail.setText("尾款");
        }else if (billType.equals("3")){//3定金
            holder.postion_detail.setText("定金");
        }


        String totalPrice = stringStringMap.get("totalPrice");//下单金额
        holder.tv_user_name.setText("￥"+new Double(totalPrice)/new Double(100)+"");

        String payType = stringStringMap.get("payType");//支付方式
        // 1微信  2支付宝  3其他  4聚合  5扫呗  6通联
        if (payType.equals("1")){// 1微信
            holder.tv_position.setText("微信");
        }else if (payType.equals("2")){//2支付宝
            holder.tv_position.setText("支付宝");
        }else if (payType.equals("3")){//3其他
            holder.tv_position.setText("其他");
        }else if (payType.equals("4")){// 4聚合
            holder.tv_position.setText("聚合");
        }else if (payType.equals("5")){// 5扫呗
            holder.tv_position.setText("扫呗");
        }else if (payType.equals("6")){//6通联
            holder.tv_position.setText("通联");
        }
        String payAt = stringStringMap.get("payAt");//支付时间
        Log.e("tag", "onBindViewHolder: "+payAt );
        if (!payAt.equals("0")){
            holder.zftime.setText(Util.stampToDate(payAt));
        }

        String createdAt = stringStringMap.get("createdAt");//下单时间
        holder.xdtime.setText(Util.stampToDate(createdAt));

        String notes = stringStringMap.get("notes");//备注
        holder.bz_text.setText(notes);


        /*Map<String, String> info = listmap.get(position);
        if (info.containsKey("class")) {
            String aClass = info.get("class");
            String pro = info.get("pro");
            JSONObject jsonObject1 = JSONObject.parseObject(aClass);
            try {
                String name = jsonObject1.getString("name");
                JSONObject jsonPro = JSONObject.parseObject(pro);
                String name1 = jsonPro.getString("Name");
                holder.indent_mark.setText(name1);//项目
                holder.wang_name_detail.setText(name);//班型


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        holder.remove_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnItemClickListener(position);
            }
        });
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Map<String, String> info = listmap.get(position);
                info.put("newPrice",holder.et_price.getText().toString());
                listmap.set(position, info);
                Log.e("tag", "afterTextChanged: "+listmap.get(position).get("price") );
                //setData(listmap);
                 NewAddOrderActivity.setWangPrice(listmap);
            }
        };
        holder.et_price.addTextChangedListener(watcher);*/
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