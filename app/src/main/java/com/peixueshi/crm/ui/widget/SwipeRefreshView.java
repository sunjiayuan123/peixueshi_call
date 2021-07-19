package com.peixueshi.crm.ui.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.peixueshi.crm.R;


/**
 * 自定义View继承SwipeRefreshLayout，添加上拉加载更多的布局属性,添加对RecyclerView的支持
 * Created by zhaobaolei.
 */

public class SwipeRefreshView extends SwipeRefreshLayout {

    private static final String TAG = SwipeRefreshView.class.getSimpleName();
    private final int mScaledTouchSlop;
    private final View mFooterView;
    private ListView mListView;
    private ExpandableListView exListView;
    private OnLoadMoreListener mListener;

    public boolean isLoadmoreComplete() {
        return loadmoreComplete;
    }

    public void setLoadmoreComplete(boolean loadmoreComplete) {
        this.loadmoreComplete = loadmoreComplete;
    }

    private boolean loadmoreComplete = false;

    /**
     * 正在加载状态
     */
    private boolean isLoading;
    private RecyclerView mRecyclerView;
    private int mItemCount;

    public SwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充底部加载布局
        mFooterView = View.inflate(context, R.layout.view_footer, null);

        // 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 获取ListView,设置ListView的布局位置
        if (mListView == null || mRecyclerView == null) {
            // 判断容器有多少个孩子
            if (getChildCount() > 0) {
                // 判断第一个孩子是不是ListView
                if (getChildAt(0) instanceof ListView) {
                    if (getChildAt(0) instanceof ExpandableListView) {
                        exListView = (ExpandableListView) getChildAt(0);
                    } else {

                        // 创建ListView对象
                        mListView = (ListView) getChildAt(0);

                        // 设置ListView的滑动监听
                        setListViewOnScroll();
                    }
                } else if (getChildAt(0) instanceof RecyclerView) {
                    // 创建ListView对象
                    mRecyclerView = (RecyclerView) getChildAt(0);

                    // 设置RecyclerView的滑动监听
                    setRecyclerViewOnScroll();
                }
            }
        }
    }


    /**
     * 在分发事件的时候处理子控件的触摸事件
     */
    private float mDownY, mUpY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 移动的起点
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                // 移动的终点
                mUpY = ev.getY();
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断是否满足加载更多条件
     */
    private boolean canLoadMore() {
        // 1. 是上拉状态
        boolean condition1 = (mDownY - mUpY) >= mScaledTouchSlop && mUpY != 0;
        Log.e("mDownY", "-->" + mDownY);
        Log.e("mUpY", "-->" + mUpY);
        Log.e("mScaledTouchSlop", "-->" + mScaledTouchSlop);
        Log.e("condition1", "-->" + condition1);
        if (condition1) {
            Log.e(TAG, "------->  是上拉状态");
        }

        // 2. 当前页面可见的item是最后一个条目,一般最后一个条目位置需要大于第一页的数据长度
        boolean condition2 = false;
        if (mListView != null && mListView.getAdapter() != null) {

            if (mItemCount > 0) {
                Log.e("mItemCount", "--->" + mItemCount);
                if (mListView.getAdapter().getCount() < mItemCount) {
                    // 第一页未满，禁止下拉
                    Log.e("第一页未满，禁止下拉", "--->mListView.getAdapter().getCount() < mItemCount");
                    condition2 = false;
                } else {
                    Log.e("第一页已经满了，并且最后一个可见的item", "--->mListView.getLastVisiblePosition()" + mListView.getLastVisiblePosition());
                    Log.e("第一页已经满了，并且最后一个可见的item", "--->mListView.getAdapter()" + (mListView.getAdapter().getCount() - 1));
                    condition2 = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
                }
            } else {
                // 未设置数据长度，则默认第一页数据不满时也可以上拉
                Log.e("未设置数据长度，数据不满时也可以上拉", "--->mListView.getAdapter().getCount() < mItemCount");
                Log.e("第一页已经满了，并且最后一个可见的item", "--->mListView.getLastVisiblePosition()" + mListView.getLastVisiblePosition());
                Log.e("第一页已经满了，并且最后一个可见的item", "--->mListView.getAdapter()" + (mListView.getAdapter().getCount() - 1));
                condition2 = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
            }

        } else if (exListView != null && exListView.getAdapter() != null) {
            // 未设置数据长度，则默认第一页数据不满时也可以上拉
            condition2 = exListView.getLastVisiblePosition() == (exListView.getAdapter().getCount() - 1);
            Log.e("exlist的最后一个可见的位置", exListView.getLastVisiblePosition() + "");
            Log.e("exlist所有的item", exListView.getAdapter().getCount() + "");
        }
        Log.e("condition2", "-->" + condition2);

        if (condition2) {
            Log.e(TAG, "------->  是最后一个条目");
        }
        // 3. 正在加载状态
        boolean condition3 = !isLoading;
        Log.e("condition3", "-->" + condition3);
        if (condition3) {
            Log.e(TAG, "------->  不是正在加载状态");
        }
        return condition1 && condition2 && condition3;
//        return condition1 && true && condition3;
    }

    public void setItemCount(int itemCount) {
        this.mItemCount = itemCount;
    }

    /**
     * 处理加载数据的逻辑
     */
    private void loadData() {
        Log.e("开始加载","开始加载");
        if (loadmoreComplete) {
            return;
        }
        if (mListener != null) {
            // 设置加载状态，让布局显示出来
            setLoading(true);
            mListener.onLoadMore();
        }

    }

    /**
     * 设置加载状态，是否加载传入boolean值进行判断
     *
     * @param loading
     */
    public void setLoading(boolean loading) {
        // 修改当前的状态
        isLoading = loading;
        Log.e("loading布尔值", isLoading + "");
        if (isLoading) {
            // 显示布局
            if (exListView != null) {
                exListView.addFooterView(mFooterView);
            } else {
                mListView.addFooterView(mFooterView);
            }
        } else {
            // 隐藏布局
            if (exListView != null) {
                exListView.removeFooterView(mFooterView);
            } else {
                mListView.removeFooterView(mFooterView);
            }

            // 重置滑动的坐标
            mDownY = 0;
            mUpY = 0;
        }
    }


    /**
     * 设置ListView的滑动监听
     */
    private void setListViewOnScroll() {

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 移动过程中判断时候能下拉加载更多
//                if (canLoadMore()) {
//                    // 加载数据
//                    loadData();
//                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    /**
     * 设置RecyclerView的滑动监听
     */
    private void setRecyclerViewOnScroll() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 移动过程中判断时候能下拉加载更多
//                if (canLoadMore()) {
//                    // 加载数据
//                    loadData();
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    /**
     * 上拉加载的接口回调
     */

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }
}
