package com.peixueshi.crm.ui.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.peixueshi.crm.R;

import java.util.List;
import java.util.Map;

public class NewEamestWangAdapter extends RecyclerView.Adapter<NewEamestWangAdapter.ViewHolder> {
    public String TAG = "UserDataZiAdapter";
    private Context activity;
    private int type;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView indent_mark;
        TextView wang_name_detail;
        EditText et_price;
        TextView remove_pro;

        public ViewHolder(View view) {
            super(view);
            indent_mark = (TextView) view.findViewById(R.id.wang_pro_detail);
            wang_name_detail = (TextView) view.findViewById(R.id.wang_name_detail);
            et_price = view.findViewById(R.id.et_price);
            remove_pro = (TextView) view.findViewById(R.id.remove_pro);
        }

    }

    List<Map<String, String>> listmap;
    List<Map<String, String>> selectMap;

    public NewEamestWangAdapter(Context activity, List<Map<String, String>> listmap) {
        this.activity = activity;
        this.listmap = listmap;
    }

    public void setData(List<Map<String, String>> listmaps,int type) {
        this.listmap = listmaps;
        this.type=type;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wang_eamest_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    int prices;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Map<String, String> info = listmap.get(position);
        if (info.containsKey("class")) {
            String aClass = info.get("class");
            String pro = info.get("pro");
            JSONObject jsonObject1 = JSONObject.parseObject(aClass);
            try {
                String name = jsonObject1.getString("name");
                JSONObject jsonPro = JSONObject.parseObject(pro);
                String name1 = jsonPro.getString("Name");
                int classId = jsonObject1.getIntValue("id");//classId
                String className = jsonObject1.getString("name");
                long projectId = jsonPro.getLongValue("Id");
                String projectName = jsonPro.getString("Name");
                if (type==1){
                    holder.indent_mark.setText(name1);//??????
                    holder.wang_name_detail.setText(name);//??????
                    info.put("classId",classId+"");
                    info.put("className",className+"");
                    info.put("projectId",projectId+"");
                    info.put("projectName",projectName+"");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {

            String className1 = info.get("className");
            String projectName1 = info.get("projectName");
            String price = info.get("newPrice");
            holder.indent_mark.setText(projectName1);//??????
            holder.wang_name_detail.setText(className1);//??????
            holder.remove_pro.setText("???"+new Double(price)/new Double(100)+"");
        }


    }

       /* holder.indent_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //?????????????????????,??????????????????
                Intent intent = new Intent(activity, NewIndentDetailActivity.class);
                activity.startActivity(intent);
            }
        });*/

    @Override
    public int getItemCount() {
        return listmap.size();
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