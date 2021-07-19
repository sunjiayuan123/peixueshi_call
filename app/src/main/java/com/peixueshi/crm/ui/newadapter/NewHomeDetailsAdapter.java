package com.peixueshi.crm.ui.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peixueshi.crm.R;
import com.peixueshi.crm.bean.DetailsBean;

import java.util.List;

public class NewHomeDetailsAdapter extends RecyclerView.Adapter<NewHomeDetailsAdapter.ViewHolder> {
    private final int billType;
    public String TAG = "UserDataZiAdapter";
    private Context activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_mark_text;
        TextView xiangmu_detail;
        TextView postion_detail;
        TextView tv_position;
        TextView order_pay_type_detail;
        RelativeLayout rl_order_detail_info;
        ImageView channel_imageview_orientation;

        TextView order_pay_money;
        TextView order_pay_leixing;
        TextView exam_type;
        TextView exam_type_detail;
        TextView enter_area;
        TextView order_number_area;
        TextView tv_xiangmu_name;
        TextView order_pay_type;
        TextView tv_user_name;
        TextView stu_name;
        TextView stu_name_detail;
        TextView census;
        TextView census_detail;
        RelativeLayout rl_xiezhu;
        TextView xie_pro_detail;
        TextView xie_pro;
        TextView xie_name_detail;
        TextView xie_card_detail;
        TextView xie_census_detail;
        TextView xie_price_detail;
        RelativeLayout rl_other;


        TextView other_pro_detail;
        TextView other_name_detail;
        TextView other_census_detail;
        TextView other_price_detail;


        RelativeLayout rl_wang;

        RelativeLayout rl_pay_info;
        TextView wang_pro_detail;
        TextView wang_name_detail;
        TextView wang_price_detail;
        TextView xue_card_detail;
        TextView xie_area_detail;

        public ViewHolder(View view) {
            super(view);
            order_mark_text = view.findViewById(R.id.order_mark_text);//订单号

            rl_pay_info = view.findViewById(R.id.rl_pay_info);
            exam_type = view.findViewById(R.id.exam_type);//考试类型
            exam_type_detail = view.findViewById(R.id.exam_type_detail);//考试类型值
            enter_area = view.findViewById(R.id.enter_area);//报考区域
            order_number_area = view.findViewById(R.id.order_number_area);//报考区域
            tv_xiangmu_name = view.findViewById(R.id.tv_xiangmu_name);//学历名称

            xiangmu_detail = view.findViewById(R.id.xiangmu_detail);//学历名称
            order_pay_leixing = view.findViewById(R.id.order_pay_leixing);//学历层次
            postion_detail = view.findViewById(R.id.postion_detail);//学历层次
            order_pay_type = view.findViewById(R.id.order_pay_type);//专业名称
            tv_user_name = view.findViewById(R.id.tv_user_name);//专业名称
            stu_name = view.findViewById(R.id.stu_name);//姓名

            stu_name_detail = view.findViewById(R.id.stu_name_detail);//姓名
            order_pay_money = view.findViewById(R.id.order_pay_money);//下单金额
            tv_position = view.findViewById(R.id.tv_position);//下单金额
            census = view.findViewById(R.id.census);//户籍
            census_detail = view.findViewById(R.id.census_detail);//户籍


            rl_xiezhu = view.findViewById(R.id.rl_xiezhu);//协助报名
            xie_pro_detail = view.findViewById(R.id.xie_pro_detail);//项目名称
            xie_pro = view.findViewById(R.id.xie_pro);//报名区域
            xie_area_detail = view.findViewById(R.id.xie_area_detail);
            xie_name_detail = view.findViewById(R.id.xie_name_detail);//姓名
            xie_card_detail = view.findViewById(R.id.xie_card_detail);//身份证号
            xie_census_detail = view.findViewById(R.id.xie_census_detail);//户籍
            xie_price_detail = view.findViewById(R.id.xie_price_detail);//下单金额


            rl_other = view.findViewById(R.id.rl_other);//其他
            other_pro_detail = view.findViewById(R.id.other_pro_detail);//证书名称
            other_name_detail = view.findViewById(R.id.other_name_detail);//姓名
            other_census_detail = view.findViewById(R.id.other_census_detail);//户籍
            other_price_detail = view.findViewById(R.id.other_price_detail);//下单金额


            rl_wang = view.findViewById(R.id.rl_wang);//网课
            wang_pro_detail = view.findViewById(R.id.wang_pro_detail);//项目名称
            wang_name_detail = view.findViewById(R.id.wang_name_detail);//班型
            wang_price_detail = view.findViewById(R.id.wang_price_detail);//下单金额
            xue_card_detail = view.findViewById(R.id.xue_card_detail);
        }
    }

    List<DetailsBean> list;

    public NewHomeDetailsAdapter(Context activity, List<DetailsBean> list, int billType) {
        this.activity = activity;
        this.list = list;
        this.billType = billType;
    }

    public void setData(List<DetailsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wang_home_details_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    int prices;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetailsBean newOrderUserInfo = list.get(position);
        String projectName = newOrderUserInfo.getProjectName();//
        String className = newOrderUserInfo.getClassName();
        int price = newOrderUserInfo.getPrice();
       String endPrice= "￥" + Double.valueOf(price) / Double.valueOf(100);
        String schoolName = newOrderUserInfo.getSchoolName();
        int mStatus = newOrderUserInfo.getMStatus();
        String majorName = newOrderUserInfo.getMajorName();
        String studentName = newOrderUserInfo.getStudentName();
        String idCard = newOrderUserInfo.getIdCard();
        String address = newOrderUserInfo.getAddress();
        String oAddress = newOrderUserInfo.getOAddress();
        String certName = newOrderUserInfo.getCertName();
        if (billType == 1) {// 1,网课 2,学历   // 3协助报名       // 4证书    //5 其他
            holder.rl_wang.setVisibility(View.VISIBLE);
            holder.wang_pro_detail.setText(projectName);
            holder.wang_name_detail.setText(className);
            holder.wang_price_detail.setText(endPrice);
        }else if (billType==2){
            holder.rl_pay_info.setVisibility(View.VISIBLE);
            holder.xiangmu_detail.setText(schoolName);
            holder.tv_user_name.setText(majorName);
            holder.stu_name_detail.setText(studentName);
            holder.xue_card_detail.setText(idCard);
            holder.census_detail.setText(address);
            holder.tv_position.setText(endPrice);
            if (mStatus==1){
                holder.postion_detail.setText("大专");
            }else if (mStatus==2){
                holder.postion_detail.setText("中专");
            } else if (mStatus==3){
                holder.postion_detail.setText("本科");
            }
        }else if (billType==3){
            holder.rl_xiezhu.setVisibility(View.VISIBLE);
            holder.xie_pro_detail.setText(projectName);
            holder.xie_area_detail.setText(oAddress);
            holder.xie_name_detail.setText(studentName);
            holder.xie_card_detail.setText(idCard);
            holder.xie_census_detail.setText(address);
            holder.xie_price_detail.setText(endPrice);
        }else if (billType==4){
            holder.rl_other.setVisibility(View.VISIBLE);
            holder.other_pro_detail.setText(certName);
            holder.other_name_detail.setText(studentName);
            holder.other_census_detail.setText(address);
            holder.other_price_detail.setText(endPrice);
        }

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