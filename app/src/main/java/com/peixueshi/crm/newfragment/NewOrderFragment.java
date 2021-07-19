package com.peixueshi.crm.newfragment;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.peixueshi.crm.R;
import com.peixueshi.crm.di.CommonModule;
import com.peixueshi.crm.di.DaggerCommonComponent;
import com.peixueshi.crm.ui.adapter.MyViewPagerAdapterOrder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewOrderFragment} interface
 * to handle interaction events.
 * Use the {@link NewOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewOrderFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.iv_search_order)
    ImageView iv_search_order;
    @BindView(R.id.tabLayout_order)
    TabLayout tabLayout_order;
    @BindView(R.id.viewpager_order)
    ViewPager viewpager_order;
    @BindView(R.id.all_order)
    TextView allOrder;
    @BindView(R.id.total_payment)
    TextView totalPayment;
    @BindView(R.id.earnest_payment)
    TextView earnestPayment;
    @BindView(R.id.balance_payment)
    TextView balancePayment;

    @BindView(R.id.et_phoe_click)
    EditText etPhoeClick;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.ed_re)
    RelativeLayout edRe;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyViewPagerAdapterOrder adapter;
    private NewOneFragmentOrder oneFragment1;
    private NewOneFragmentOrder oneFragment2;
    private NewOneFragmentOrder oneFragment3;
    private NewOneFragmentOrder oneFragment4;
    private NewOneFragmentOrder oneFragment5;
    private int position;
    public NewOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewOrderFragment newInstance(String param1, String param2) {
        NewOrderFragment fragment = new NewOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


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
        View view = ArmsUtils.inflate(getActivity(), R.layout.fragment_new_order);
        return view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        adapter = new MyViewPagerAdapterOrder(getChildFragmentManager());


        oneFragment1 = NewOneFragmentOrder.newInstance("待支付", true, 1);
        oneFragment2 = NewOneFragmentOrder.newInstance("已支付", false, 2);
        oneFragment3 = NewOneFragmentOrder.newInstance("退费", false, 3);
        oneFragment4 = NewOneFragmentOrder.newInstance("换课", false, 4);
        oneFragment5 = NewOneFragmentOrder.newInstance("交易关闭", false, 5);
        adapter.addFragment(oneFragment1, "待支付");
        adapter.addFragment(oneFragment2, "已支付");
        adapter.addFragment(oneFragment3, "退费");
        adapter.addFragment(oneFragment4, "换课");
        adapter.addFragment(oneFragment5, "交易关闭");
        // adapter.addFragment(OneFragment.newInstance("制作方式"),"制作方式");
        viewpager_order.setAdapter(adapter);
        tabLayout_order.setTabMode(TabLayout.MODE_FIXED);
        tabLayout_order.addTab(tabLayout_order.newTab().setText("待支付"));
        tabLayout_order.addTab(tabLayout_order.newTab().setText("已支付"));
        tabLayout_order.addTab(tabLayout_order.newTab().setText("退费"));
        tabLayout_order.addTab(tabLayout_order.newTab().setText("换课"));
        tabLayout_order.addTab(tabLayout_order.newTab().setText("交易关闭"));
        // tabLayout.addTab(tabLayout.newTab().setText("制作方式"));
        viewpager_order.setOffscreenPageLimit(5);
        tabLayout_order.setupWithViewPager(viewpager_order);
        //TabLayout绑定标题栏点击适配器
        tabLayout_order.setTabsFromPagerAdapter(adapter);
        etPhoeClick.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = etPhoeClick.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > etPhoeClick.getWidth() - etPhoeClick.getPaddingRight() - drawable.getIntrinsicWidth()) {
                    etPhoeClick.setText("");
                }
                return false;
            }
        });
        etPhoeClick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11) {
                    selectPhopne();
                }
            }
        });
        /*iv_search_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowSearchOrder.class);
                startActivity(intent);
            }
        });*/

       /* new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                tabLayout_order.getTabAt(0).select();
            }

        }.execute();*/
        //adapter.getItem(0);
//        viewpager_order.setOffscreenPageLimit(2);
        //  adapter.notifyDataSetChanged();

        tabLayout_order.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {



            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选择时触发
                TextView title = (TextView) (((LinearLayout) ((LinearLayout) tabLayout_order.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                //  title.setTextSize(18);

                title.setTextAppearance(getActivity(), R.style.TabLayoutTextStyle);
                String contentDescription = tab.getContentDescription().toString();
                selectPhopne();
                setAllOrder();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //未选择是触发
                TextView title = (TextView) (((LinearLayout) ((LinearLayout) tabLayout_order.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                //  title.setTextSize(14);
                title.setTextAppearance(getActivity(), Typeface.NORMAL);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //选中之后再次点击即复选时触发
             /*   if (tab.getText().equals("产品分类")) {
                    mPopupWindow.showAsDropDown(mTabLayout);
                }*/
                Log.e("tag", "initData: " );
               // adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    int selectPayType = 0;

    @OnClick({R.id.tv_search, R.id.iv_search_order, R.id.all_order, R.id.total_payment, R.id.earnest_payment, R.id.balance_payment})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.all_order:
                setAllOrder();
                selectPayType = 0;
                select();
                break;
            case R.id.total_payment:
                setTotalPayment();
                selectPayType = 1;
                select();
                break;
            case R.id.earnest_payment:
                setEarnestPayment();
                selectPayType = 3;
                select();
                break;
            case R.id.balance_payment:
                setBalancePayment();
                selectPayType = 2;
                select();
                break;
            case R.id.iv_search_order://搜索
                edRe.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_search:
                edRe.setVisibility(View.GONE);
                etPhoeClick.setText("");
                selectPhopne();
               /* if (!TextUtils.isEmpty(etPhoeClick.getText().toString())){
                    selectPhopne();
                }*/
               /* Intent intent = new Intent(getActivity(), ShowSearchOrder.class);
                startActivity(intent);*/

                break;
        }

    }

    public void selectPhopne() {
        int positi = viewpager_order.getCurrentItem();
        if (adapter.getItem(positi) instanceof NewOneFragmentOrder) {
            NewOneFragmentOrder oneFragment = (NewOneFragmentOrder) adapter.getItem(positi);
            oneFragment.searchInfo(etPhoeClick.getText().toString(),position);
        }
    }

    public void select() {
        int positi = viewpager_order.getCurrentItem();
        if (adapter.getItem(positi) instanceof NewOneFragmentOrder) {
            NewOneFragmentOrder oneFragment = (NewOneFragmentOrder) adapter.getItem(positi);
            oneFragment.searchInfos(etPhoeClick.getText().toString(),selectPayType);
        }
    }

    public void setAllOrder() {
        allOrder.setBackground(getResources().getDrawable(R.drawable.btn_red_corner));
        allOrder.setTextColor(getResources().getColor(R.color.white));
        totalPayment.setTextColor(getResources().getColor(R.color.btn_gray));
        totalPayment.setBackground(getResources().getDrawable(R.drawable.shape_text_gray));
        earnestPayment.setTextColor(getResources().getColor(R.color.btn_gray));
        earnestPayment.setBackground(getResources().getDrawable(R.drawable.shape_text_gray));
        balancePayment.setTextColor(getResources().getColor(R.color.btn_gray));
        balancePayment.setBackground(getResources().getDrawable(R.drawable.shape_text_gray));
    }

    public void setTotalPayment() {
        totalPayment.setBackground(getResources().getDrawable(R.drawable.btn_red_corner));
        totalPayment.setTextColor(getResources().getColor(R.color.white));
        allOrder.setTextColor(getResources().getColor(R.color.btn_gray));
        allOrder.setBackground(getResources().getDrawable(R.drawable.shape_text_gray));
        earnestPayment.setTextColor(getResources().getColor(R.color.btn_gray));
        earnestPayment.setBackground(getResources().getDrawable(R.drawable.shape_text_gray));
        balancePayment.setTextColor(getResources().getColor(R.color.btn_gray));
        balancePayment.setBackground(getResources().getDrawable(R.drawable.shape_text_gray));
    }

    public void setEarnestPayment() {
        earnestPayment.setBackground(getResources().getDrawable(R.drawable.btn_red_corner));
        earnestPayment.setTextColor(getResources().getColor(R.color.white));
        allOrder.setTextColor(getResources().getColor(R.color.btn_gray));
        allOrder.setBackground(getResources().getDrawable(R.drawable.shape_text_gray));
        totalPayment.setTextColor(getResources().getColor(R.color.btn_gray));
        totalPayment.setBackground(getResources().getDrawable(R.drawable.shape_text_gray));
        balancePayment.setTextColor(getResources().getColor(R.color.btn_gray));
        balancePayment.setBackground(getResources().getDrawable(R.drawable.shape_text_gray));
    }

    public void setBalancePayment() {
        balancePayment.setBackground(getResources().getDrawable(R.drawable.btn_red_corner));
        balancePayment.setTextColor(getResources().getColor(R.color.white));
        allOrder.setTextColor(getResources().getColor(R.color.btn_gray));
        allOrder.setBackground(getResources().getDrawable(R.drawable.shape_text_gray));
        totalPayment.setTextColor(getResources().getColor(R.color.btn_gray));
        totalPayment.setBackground(getResources().getDrawable(R.drawable.shape_text_gray));
        earnestPayment.setTextColor(getResources().getColor(R.color.btn_gray));
        earnestPayment.setBackground(getResources().getDrawable(R.drawable.shape_text_gray));
    }

}
