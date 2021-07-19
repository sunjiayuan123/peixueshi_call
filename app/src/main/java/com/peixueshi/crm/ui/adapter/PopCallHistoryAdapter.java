package com.peixueshi.crm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peixueshi.crm.R;
import com.peixueshi.crm.utils.Util;

import java.util.List;
import java.util.Map;

public class PopCallHistoryAdapter extends RecyclerView.Adapter<PopCallHistoryAdapter.ViewHolder> {

    private List<Map<String, String>> mapInfos;

    private Context activity;

    public void setData(List<Map<String, String>> listPidInfo) {
        this.mapInfos = listPidInfo;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_beizhu;
        TextView tv_zuoxi;
        TextView tv_goutong_state;
        TextView tv_beizhu_detail;

        public ViewHolder(View view) {
            super(view);
            tv_beizhu = (TextView) view.findViewById(R.id.tv_beizhu);
            tv_zuoxi = (TextView) view.findViewById(R.id.tv_zuoxi);
            tv_goutong_state = (TextView) view.findViewById(R.id.tv_goutong_state);
            tv_beizhu_detail = (TextView) view.findViewById(R.id.tv_beizhu_detail);
        }

    }

    public PopCallHistoryAdapter(Context activity, List<Map<String, String>> infos) {
        this.activity = activity;
        mapInfos = infos;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_call_history_item, parent, false);
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
        // {"date":{"count":0,"page":0,"total":4,"collation":0,"rules":0,"list":[{"n_id":154980,"n_uid":54,"n_phone":18607191722,"n_note":"阿尼亚","n_state":0,"n_at":1587626647,"emp_id":54,"emp_role_id":1,"emp_team_id":6,"emp_proj_id":1001001,"emp_name":"测药师","emp_phone":"18272636853","emp_tq_acc":9855441,"emp_tq_pw":"tq.cn","emp_start":0,"emp_at":1585465383},{"n_id":154962,"n_uid":54,"n_phone":18607191722,"n_note":"备注","n_state":0,"n_at":1587626560,"emp_id":54,"emp_role_id":1,"emp_team_id":6,"emp_proj_id":1001001,"emp_name":"测药师","emp_phone":"18272636853","emp_tq_acc":9855441,"emp_tq_pw":"tq.cn","emp_start":0,"emp_at":1585465383},{"n_id":154917,"n_uid":54,"n_phone":18607191722,"n_note":"备注也可以","n_state":0,"n_at":1587626429,"emp_id":54,"emp_role_id":1,"emp_team_id":6,"emp_proj_id":1001001,"emp_name":"测药师","emp_phone":"18272636853","emp_tq_acc":9855441,"emp_tq_pw":"tq.cn","emp_start":0,"emp_at":1585465383},{"n_id":154914,"n_uid":54,"n_phone":18607191722,"n_note":"备注也可以","n_state":0,"n_at":1587626426,"emp_id":54,"emp_role_id":1,"emp_team_id":6,"emp_proj_id":1001001,"emp_name":"测药师","emp_phone":"18272636853","emp_tq_acc":9855441,"emp_tq_pw":"tq.cn","emp_start":0,"emp_at":1585465383}]},"err":0}

        Map<String, String> info = mapInfos.get(position);
        holder.tv_beizhu.setText(Util.stampToDate(info.get("n_at")));
        holder.tv_zuoxi.setText("坐席:"+info.get("emp_name"));
        if (info.get("n_state")!=null){
            int checkedId = Integer.valueOf(info.get("n_state"));
            String goutong_type = "";
            if (checkedId == 0) {
                goutong_type = "正常接通";//正常接通
                holder.tv_goutong_state.setTextColor(activity.getResources().getColor(R.color.corner_color));
            } else if (checkedId == 1) {
                goutong_type = "未接通";//1未接通
                holder.tv_goutong_state.setTextColor(activity.getResources().getColor(R.color.orange_color));
            } else if (checkedId == 2) {
                goutong_type = "关/停机";//1关停机
                holder.tv_goutong_state.setTextColor(activity.getResources().getColor(R.color.sdk_color_common_red));
            } else if (checkedId == 3) {
                goutong_type = "空号";//空号
                holder.tv_goutong_state.setTextColor(activity.getResources().getColor(R.color.sdk_color_common_red));
            }
            holder.tv_goutong_state.setText(goutong_type);
        }


        holder.tv_beizhu_detail.setText("备注:"+info.get("n_note"));
        if (mListener != null) {
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

    public interface OnItemClickListener {
        void OnItemClickListener(int pos);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public String TAG = "UserDataZiAdapter";

}