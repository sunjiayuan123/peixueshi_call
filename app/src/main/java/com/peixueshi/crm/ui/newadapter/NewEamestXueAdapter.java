package com.peixueshi.crm.ui.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.peixueshi.crm.R;

import java.util.List;
import java.util.Map;

public class NewEamestXueAdapter extends RecyclerView.Adapter<NewEamestXueAdapter.ViewHolder> {
    public String TAG = "UserDataZiAdapter";

    private Context activity;
    private int prices;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView exam_type_detail;
        TextView order_number_area;
        TextView xiangmu_detail;
        TextView postion_detail;
        TextView tv_user_name;
        TextView stu_name_detail;
        TextView census_detail;
        TextView remove_pro;
        TextView xue_card_detail;
        public ViewHolder(View view) {
            super(view);
            exam_type_detail = view.findViewById(R.id.exam_type_detail);//考试类型值
            order_number_area = view.findViewById(R.id.order_number_area);//报考区域
            xiangmu_detail = view.findViewById(R.id.xiangmu_detail);//学历名称
            postion_detail = view.findViewById(R.id.postion_detail);//学历层次
            tv_user_name = view.findViewById(R.id.tv_user_name);//专业名称
            stu_name_detail = view.findViewById(R.id.stu_name_detail);//姓名
            census_detail = view.findViewById(R.id.census_detail);//户籍
            remove_pro = view.findViewById(R.id.remove_pro);//价格
            xue_card_detail = view.findViewById(R.id.xue_card_detail);
        }

    }

    List<Map<String, String>> listmap;

    public NewEamestXueAdapter(Context activity, List<Map<String, String>> listmap) {
        this.activity = activity;
        this.listmap = listmap;
    }

    public void setData(List<Map<String, String>> listmaps) {
        this.listmap = listmaps;
        notifyDataSetChanged();
    }

    public int xuePrice(){
        return prices;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.xue_eamest_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Map<String, String> info = listmap.get(position);
        if (info.containsKey("sch")){
            String sch = info.get("sch");
            JSONObject jsonObject1 = JSONObject.parseObject(sch);
            String major = info.get("major");//专业
            JSONObject major_xueli = JSONObject.parseObject(major);

            try {
                String name1 = jsonObject1.getString("name");//学校名称
                int schoolId = jsonObject1.getIntValue("id");
                String name = major_xueli.getString("name");//专业名称
                int price = major_xueli.getIntValue("price");
                int status = major_xueli.getIntValue("status");
                int majorId = major_xueli.getIntValue("id");
                String address = info.get("address");
                String educationType = info.get("educationType");
                String id_card = info.get("id_card");
                String stu_name = info.get("stu_name");
                String Sstatus = info.get("Sstatus");
                holder.xue_card_detail.setText(id_card);
                holder.exam_type_detail.setText(educationType);
                holder.stu_name_detail.setText(stu_name);
                holder.census_detail.setText(address);
                holder.xiangmu_detail.setText(name1);
                holder.tv_user_name.setText(name+"");

                info.put("majorId",majorId+"");
                info.put("majorName", name);
                info.put("mStatus", status+"");
                info.put("address", address);
                info.put("idCard", id_card);

                info.put("sStatus", Sstatus);
                info.put("schoolId", schoolId+"");
                info.put("schoolName", name1);
                if(status == 1){
                    holder.postion_detail.setText("大专");
                }else if(status == 2){
                    holder.postion_detail.setText("中专");
                }else{
                    holder.postion_detail.setText("本科");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            };
        }else {
            String schoolId = info.get("schoolId");

            String schoolName = info.get("schoolName");
            String idCard = info.get("idCard");
            String majorName = info.get("majorName");
            String majorId = info.get("majorId");
            String sStatus = info.get("sStatus");
            String studentName = info.get("studentName");
            String mStatus = info.get("mStatus");
            String price = info.get("newPrice");
            String address = info.get("address");

            holder.xue_card_detail.setText(idCard);
            if (sStatus.equals("1")){//考试类型
                holder.exam_type_detail.setText("网教自考");
            }else if (sStatus.equals("2")){
                holder.exam_type_detail.setText("网教直取");
            }else if (sStatus.equals("3")){
                holder.exam_type_detail.setText("成人教育");
            }
            int status = Integer.valueOf(mStatus);
            if(status == 1){
                holder.postion_detail.setText("大专");
            }else if(status == 2){
                holder.postion_detail.setText("中专");
            }else{
                holder.postion_detail.setText("本科");
            }
            holder.stu_name_detail.setText(studentName);
            holder.xiangmu_detail.setText(schoolName);
            holder.census_detail.setText(address);
            holder.tv_user_name.setText(majorName+"");
            holder.remove_pro.setText("￥"+new Double(price)/new Double(100)+"");
        }

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