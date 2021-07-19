package com.peixueshi.crm.newfragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.peixueshi.crm.R;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.fragment.OneFragment;
import com.peixueshi.crm.fragment.OneFragmentHandMaster;
import com.peixueshi.crm.fragment.OneFragmentMasterShouzi;
import com.peixueshi.crm.newactivity.CreateChanceActivity;
import com.peixueshi.crm.service.CommonModel;
import com.peixueshi.crm.ui.adapter.MyViewPagerAdapter;
import com.peixueshi.crm.utils.PromptManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者:孙家圆
 * 描述:
 */
public class MainChanceFragment extends BaseFragment {

    /* @BindView(R.id.call_Start)
     Button call_Start;
     @BindView(R.id.call_Stop)
     Button call_Stop;
 */
    @BindView(R.id.viewpager)
    ViewPager vp;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    /* @BindView(R.id.toolBar)
     Toolbar toolbar;*/
    @BindView(R.id.iv_search_phone)
    ImageView iv_search_phone;
    @BindView(R.id.et_phoe_click)
    EditText et_phoe_click;

    @Inject
    CommonModel commonModel;


    MyViewPagerAdapter adapter;
    @BindView(R.id.create_chance)
    ImageView createChance;
    Unbinder unbinder;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCommonComponent.builder()
                .appComponent(appComponent)
                .commonModule(new CommonModule())
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ArmsUtils.inflate(getActivity(), R.layout.fragment_chance);
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //请求权限
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.MODIFY_PHONE_STATE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CALL_LOG}, 1);
        }
        iv_search_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_phoe_click.getText().toString().trim() == null || et_phoe_click.getText().toString().trim().length() < 11) {
                    PromptManager.showMyToast("请至少输入两位手机号进行查询", getActivity());
                    return;
                }
                int positi = vp.getCurrentItem();


                if (adapter.getItem(positi) instanceof OneChanceFragment) {
                    OneChanceFragment oneFragment = (OneChanceFragment) adapter.getItem(positi);
                    oneFragment.searchInfo(et_phoe_click.getText().toString().trim());
                } else if (adapter.getItem(positi) instanceof OneFragmentMasterShouzi) {
                    OneFragmentMasterShouzi oneFragment = (OneFragmentMasterShouzi) adapter.getItem(positi);
                    oneFragment.searchInfo(et_phoe_click.getText().toString().trim());
                } else if (adapter.getItem(positi) instanceof OneFragmentHandMaster) {
                    OneFragmentHandMaster oneFragment = (OneFragmentHandMaster) adapter.getItem(positi);
                    oneFragment.searchInfo(et_phoe_click.getText().toString().trim());
                }

            }
        });

        et_phoe_click.addTextChangedListener(new TextWatcher() {

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

                if (et_phoe_click.getText().toString().trim() == null || et_phoe_click.getText().toString().trim().length() < 11) {
                    return;
                }
                int positi = vp.getCurrentItem();


                if (adapter.getItem(positi) instanceof OneChanceFragment) {
                    OneChanceFragment oneFragment = (OneChanceFragment) adapter.getItem(positi);
                    oneFragment.searchInfo(et_phoe_click.getText().toString().trim());
                } else if (adapter.getItem(positi) instanceof OneFragmentMasterShouzi) {
                    OneFragmentMasterShouzi oneFragment = (OneFragmentMasterShouzi) adapter.getItem(positi);
                    oneFragment.searchInfo(et_phoe_click.getText().toString().trim());
                } else if (adapter.getItem(positi) instanceof OneFragmentHandMaster) {
                    OneFragmentHandMaster oneFragment = (OneFragmentHandMaster) adapter.getItem(positi);
                    oneFragment.searchInfo(et_phoe_click.getText().toString().trim());
                }
            }


        });
        //设置ToolBar的title颜色
        //toolbar.setTitleTextColor(Color.WHITE);
        //getActivity().setSupportActionBar(toolbar);
        adapter = new MyViewPagerAdapter(getChildFragmentManager());
        if (!Constants.headMaster) {
            adapter.addFragment(OneChanceFragment.newInstance("首咨", true), "首咨");
            adapter.addFragment(OneChanceFragment.newInstance("库存", false), "库存");

            tabLayout.addTab(tabLayout.newTab().setText("首咨"));
            tabLayout.addTab(tabLayout.newTab().setText("库存"));
        } else {
            adapter.addFragment(OneFragmentMasterShouzi.newInstance("首咨", true), "首咨");
            adapter.addFragment(OneFragmentHandMaster.newInstance("未升班", false), "未升班");
            adapter.addFragment(OneFragmentHandMaster.newInstance("已升班", true), "已升班");
            adapter.addFragment(OneFragment.newInstance("库存", false), "库存");

            tabLayout.addTab(tabLayout.newTab().setText("未升班"));
            tabLayout.addTab(tabLayout.newTab().setText("已升班"));
            tabLayout.addTab(tabLayout.newTab().setText("库存"));
            vp.setOffscreenPageLimit(5);
        }


        vp.setAdapter(adapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(vp);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setData(@Nullable Object data) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   /* Intent msg = new Intent(getActivity(), AutoRedialService.class);
                    getActivity().startService(msg);*/
                } else {
                    Toast.makeText(getActivity(), "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @OnClick({R.id.create_chance, R.id.et_phoe_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create_chance:
                Intent intent = new Intent(getContext(), CreateChanceActivity.class);
                startActivity(intent);
                break;
            case R.id.et_phoe_click:
                break;
        }
    }
}
