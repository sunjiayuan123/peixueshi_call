package com.peixueshi.crm.di;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.peixueshi.crm.MainActivity;
import com.peixueshi.crm.activity.AddExpressActivity;
import com.peixueshi.crm.activity.DetailBeizhuActivity;
import com.peixueshi.crm.activity.DetailXiangqingActivity;
import com.peixueshi.crm.activity.MineCallHistoryActivity;
import com.peixueshi.crm.activity.MineCreateChanceActivity;
import com.peixueshi.crm.activity.MinehandMasterActivity;
import com.peixueshi.crm.activity.ModifyPassActivity;
import com.peixueshi.crm.activity.OrderActivity;
import com.peixueshi.crm.activity.ShowAllOrderActivity;
import com.peixueshi.crm.activity.ShowExpressInfoActivity;
import com.peixueshi.crm.activity.ShowSearchOrder;
import com.peixueshi.crm.fragment.MainCallFragment;
import com.peixueshi.crm.fragment.MainCenterFragment;
import com.peixueshi.crm.fragment.MainHomeFragment;
import com.peixueshi.crm.fragment.MainMessageFragment;
import com.peixueshi.crm.fragment.MainOrderFragment;
import com.peixueshi.crm.fragment.MainRankingFragment;
import com.peixueshi.crm.fragment.MainRankingFragmentRight;
import com.peixueshi.crm.fragment.OrderWangkeFragment;
import com.peixueshi.crm.fragment.OrderXiezhuFragment;
import com.peixueshi.crm.fragment.OrderXueliFragment;
import com.peixueshi.crm.fragment.OrderZhiquFragment;
import com.peixueshi.crm.fragment.RankingFragment;
import com.peixueshi.crm.newactivity.CreateChanceActivity;
import com.peixueshi.crm.newactivity.NewIndentDetailActivity;
import com.peixueshi.crm.newactivity.PaymentCodeActivity;
import com.peixueshi.crm.newfragment.MainChanceFragment;
import com.peixueshi.crm.newfragment.NewOrderFragment;

import dagger.Component;

@ActivityScope
@Component(modules = CommonModule.class, dependencies = AppComponent.class)
public interface CommonComponent {
    //------Activity--------

    void inject(MainActivity activity);
    void inject(ModifyPassActivity activity);
    void inject(MineCallHistoryActivity mineCallHistoryActivity);
    void inject(MainRankingFragment mainRankingFragment);
    void inject(RankingFragment rankFragment);
    void inject(MainRankingFragmentRight mainRankingFragmentRight);
    void inject(ShowSearchOrder showSearchOrder);
    void inject(CreateChanceActivity createChanceActivity);
    void inject(NewIndentDetailActivity activity);
    void inject(PaymentCodeActivity activity);
    //------Fragment--------

    void inject(MainHomeFragment mainHomeFragment);
    void inject(NewOrderFragment newOrderFragment);
    void inject(MainMessageFragment mainMessFragment);
    void inject(MainOrderFragment mainOrderFragment);
    void inject(MainCenterFragment mainCenterFragment);
    void inject(MainCallFragment mainCallFragment);
    void inject(MainChanceFragment mainChanceFragment);
    void inject(OrderActivity orderActivity);
    void inject(DetailBeizhuActivity detailBeizhuActivity);
    void inject(DetailXiangqingActivity detailXiangqingActivity);
    void inject(MineCreateChanceActivity mineCreateChanceActivity);

    void inject(AddExpressActivity addExpressActivity);
    void inject(ShowExpressInfoActivity showExpressInfoActivity);
    void inject(ShowAllOrderActivity showAllOrderActivity);
    void inject(MinehandMasterActivity minehandMasterActivity);

    void inject(OrderWangkeFragment orderWangkeFragment);
    void inject(OrderXueliFragment orderXueliFragment);
    void inject(OrderXiezhuFragment orderXiezhuFragment);
    void inject(OrderZhiquFragment orderZhiquFragment);
}
