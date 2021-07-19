package com.peixueshi.crm.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peixueshi.crm.R;

public class MineCenterAchievement extends Fragment {
    private View parentView;
    public static final String TYPE = "type";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_achieve, container, false);
        return parentView;
    }

    public static MineCenterAchievement newInstance(String text, boolean isShouzi) {
        MineCenterAchievement fragment = new MineCenterAchievement();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, text);
        bundle.putBoolean("isShouZi",isShouzi);
        fragment.setArguments(bundle);
        return fragment;
    }
}
