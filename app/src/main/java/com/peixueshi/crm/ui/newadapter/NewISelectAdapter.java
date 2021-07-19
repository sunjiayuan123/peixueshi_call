package com.peixueshi.crm.ui.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peixueshi.crm.R;

import java.util.List;

public class NewISelectAdapter extends RecyclerView.Adapter<NewISelectAdapter.ViewHolder> {

    public String TAG = "UserDataZiAdapter";
    private Context activity;
    private int selectedPos = -1;
    private int oldPos = -1;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView adult;

        public ViewHolder(View view) {
            super(view);
            adult = (TextView) view.findViewById(R.id.adult);
        }

    }

    public List<String> stringList;

    public NewISelectAdapter(Context activity, List<String> stringList) {
        this.activity = activity;
        this.stringList = stringList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_select_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.adult.setText(stringList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnItemClickListener(position);
            }
        });

        if(selectedPos == position) {
            holder.adult.setTextColor(activity.getResources().getColor(R.color.home_top_blue));
        }else{
            holder.adult.setTextColor(activity.getResources().getColor(R.color.text_common_balck));
        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
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
    public void refreshItem(int position) {
        if (selectedPos != -1) {
            oldPos  = selectedPos;
        }
        selectedPos = position;
        if (oldPos != -1) {
            notifyItemChanged(oldPos);
        }
        notifyItemChanged(selectedPos);
    }
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }
}