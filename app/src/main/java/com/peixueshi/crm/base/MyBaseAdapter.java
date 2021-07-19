package com.peixueshi.crm.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * listview的adapter基类
 */
 public class MyBaseAdapter<T> extends BaseAdapter {

    public List<T> list_adapter;

    @Override
    public int getCount() {
        return list_adapter == null ? 0 : list_adapter.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return list_adapter.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public List<T> getList_adapter() {
        if (list_adapter == null)
            list_adapter = new ArrayList<>();
        return list_adapter;
    }

}
