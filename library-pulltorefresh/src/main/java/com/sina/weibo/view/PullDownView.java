
package com.sina.weibo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;
import com.sina.game.library_pulltorefresh.R;
import com.sina.game.library_pulltorefresh.R.dimen;
import com.sina.game.library_pulltorefresh.R.id;
import com.sina.game.library_pulltorefresh.R.string;
import com.sina.weibo.theme.Theme;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PullDownView extends FrameLayout implements OnGestureListener, AnimationListener, IPullDownView {
    protected static SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm");
    public static final int STATE_CLOSE = 1;
    public static final int STATE_OPEN = 2;
    public static final int STATE_OPEN_RELEASE = 3;
    public static final int STATE_OPEN_MAX = 4;
    public static final int STATE_OPEN_MAX_RELEASE = 5;
    public static final int STATE_UPDATE = 6;
    public static final int STATE_UPDATE_SCROLL = 7;
    public static final int STATE_OPEN_MAX_TO_FULL = 8;
    public static final int ARROW_DIRECTION_UP = 1;
    public static final int ARROW_DIRECTION_DOWN = 2;
    private int UPDATE_LENGHT;
    public int mMaxHeight;
    protected String dropDownString;
    protected String releaseUpdateString;
    protected String doingUpdateString;
    protected String hongbaoString;
    protected ImageView mArrow;
    protected ImageView mProgressBar;
    protected FrameLayout mUpdateContent;
    protected LinearLayout mTitleGroup;
    protected TextView mTitle1;
    protected TextView mTitle2;
    private GestureDetector mDetector = new GestureDetector(this);
    protected Animation mAnimationUp;
    protected Animation mAnimationDown;
    protected boolean mIsAutoScroller;
    protected View mAdView;
    protected Flinger mFlinger = new Flinger();
    protected int mPading;
    protected int mState = 1;
    protected IUpdateHandle mUpdateHandle;
    protected Date mDate;
    protected View vUpdateBar;
    private boolean mEnable = true;
    private Drawable mLoadingDrawable;
    protected Drawable mUpArrow;
    protected Drawable mDownArrow;
//    private boolean mNeedAd;
    private Bitmap mAdBmp;
    private long adStartTime = 0L;
    private long adEndTime = 9223372036854775807L;
    protected boolean mIsShowDate = false;
    protected boolean mIsShowStatusIcon = true;
    protected boolean mIsCheckLeftPadding = false;
    private int mArrowDirect = 2;
    protected int MAX_FULL_SCREEN_HEIGHT = 0;
    private OnScrollListener mOnScrollListener;


    public void setDropDownString(String dropDownString) {
        this.dropDownString = dropDownString;
    }

    public void setReleaseUpdateString(String releaseUpdateString) {
        this.releaseUpdateString = releaseUpdateString;
    }

    public void setDoingUpdateString(String doingUpdateString) {
        this.doingUpdateString = doingUpdateString;
    }

    public void setShowDate(boolean isShowDate) {
        this.mIsShowDate = isShowDate;
    }

    public void setShowStatusIcon(boolean isShowStatusIcon) {
        this.mIsShowStatusIcon = isShowStatusIcon;
        if(this.mIsShowStatusIcon) {
            this.mUpdateContent.setVisibility(View.VISIBLE);
        } else {
            this.mUpdateContent.setVisibility(View.GONE);
            this.mArrow.setImageDrawable((Drawable)null);
        }

    }

    public void showWholeLoadingStatus(boolean isShowStatusIcon) {
        this.mIsShowStatusIcon = isShowStatusIcon;
        if(this.mIsShowStatusIcon) {
            this.mUpdateContent.setVisibility(View.VISIBLE);
            this.mTitleGroup.setVisibility(View.VISIBLE);
        } else {
            this.mUpdateContent.setVisibility(View.GONE);
            this.mTitleGroup.setVisibility(View.INVISIBLE);
            this.mArrow.setImageDrawable((Drawable)null);
        }

    }

    public void setCheckLeftPadding(boolean isCheckLeftPadding) {
        this.mIsCheckLeftPadding = isCheckLeftPadding;
    }

    public PullDownView(Context context) {
        super(context);
        this.init();
        this.addUpdateBar();
    }

    private void init() {
        this.UPDATE_LENGHT = this.getContext().getApplicationContext().getResources().getDimensionPixelSize(R.dimen.updatebar_height);
        this.mMaxHeight = this.UPDATE_LENGHT;
        this.setDrawingCacheEnabled(false);
        this.setBackgroundDrawable((Drawable)null);
        this.setClipChildren(false);
        this.mDetector.setIsLongpressEnabled(true);
        Theme theme = Theme.getInstance(this.getContext().getApplicationContext());
        this.initArrow();
        this.dropDownString = this.getContext().getApplicationContext().getResources().getString(R.string.drop_dowm);
        this.releaseUpdateString = this.getContext().getApplicationContext().getResources().getString(R.string.release_update);
        this.doingUpdateString = this.getContext().getApplicationContext().getResources().getString(R.string.doing_update);
        this.hongbaoString = this.getContext().getApplicationContext().getResources().getString(R.string.pull_down_hongbao);
    }

    private void initArrow() {
        Theme theme = Theme.getInstance(this.getContext().getApplicationContext());
        this.mUpArrow = theme.getDrawableFromIdentifier(R.drawable.tableview_pull_refresh_arrow_up);
        this.mDownArrow = theme.getDrawableFromIdentifier(R.drawable.tableview_pull_refresh_arrow_down);
    }

    public PullDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        TypedArray a = context.obtainStyledAttributes(attrs, styleable.PullDownView);
//        if(a != null) {
//            boolean needAD = a.getBoolean(styleable.PullDownView_needAD, false);
//            this.needAD(needAD);
//        }

        this.init();
        this.addUpdateBar();
    }

    public View getContentView() {
        int count = this.getChildCount();
        return count > 1?this.getChildAt(1):null;
    }

//    public void needAD(boolean needAD) {
//        this.mNeedAd = needAD;
//    }

    public void setUpdateHandle(IUpdateHandle handle) {
        this.mUpdateHandle = handle;
    }

    public void setUpdateHandle(UpdateHandle handle) {
        this.mUpdateHandle = handle;
    }

    public void setUpdateLength(int updateLength) {
        this.UPDATE_LENGHT = updateLength;
    }

    public static void onGMTChange() {
        DISPLAY_DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm");
    }

    public void initSkin() {
        Theme theme = Theme.getInstance(this.getContext().getApplicationContext());
        this.mUpArrow = theme.getDrawableFromIdentifier(R.drawable.tableview_pull_refresh_arrow_up);
        this.mDownArrow = theme.getDrawableFromIdentifier(R.drawable.tableview_pull_refresh_arrow_down);
        this.mArrow.setImageDrawable(this.mIsShowStatusIcon?this.mDownArrow:null);
        Drawable loadingDrawable = theme.getDrawableFromIdentifier(R.drawable.tableview_loading);
        loadingDrawable.setBounds(0, 0, loadingDrawable.getIntrinsicWidth(), loadingDrawable.getIntrinsicHeight());
        this.mLoadingDrawable = loadingDrawable;
        this.mProgressBar.setImageDrawable(loadingDrawable);
        this.mTitle1.setTextColor(theme.getColorFromIdentifier(R.color.common_gray_93));
        this.mTitle2.setTextColor(theme.getColorFromIdentifier(R.color.common_gray_93));
    }

    protected void addUpdateBar() {
        this.mAnimationUp = AnimationUtils.loadAnimation(this.getContext().getApplicationContext(), R.anim.rotate_up);
        this.mAnimationUp.setFillAfter(true);
        this.mAnimationUp.setFillBefore(false);
        this.mAnimationUp.setAnimationListener(this);
        this.mAnimationDown = AnimationUtils.loadAnimation(this.getContext().getApplicationContext(), R.anim.rotate_down);
        this.mAnimationDown.setFillAfter(true);
        this.mAnimationDown.setFillBefore(false);
        this.mAnimationDown.setAnimationListener(this);
        this.vUpdateBar = LayoutInflater.from(this.getContext().getApplicationContext()).inflate(R.layout.vw_update_bar, (ViewGroup)null);
        this.vUpdateBar.setVisibility(View.GONE);
        this.addView(this.vUpdateBar);
        this.mUpdateContent = (FrameLayout)this.vUpdateBar.findViewById(id.iv_content);
        this.mArrow = (ImageView)this.vUpdateBar.findViewById(id.iv_arrow);
        this.mArrow.setImageDrawable(this.mIsShowStatusIcon?this.mDownArrow:null);
        this.mProgressBar = (ImageView)this.vUpdateBar.findViewById(id.pb_loading);
        this.mTitleGroup = (LinearLayout)this.findViewById(id.ly_title);
        this.mTitle1 = (TextView)this.findViewById(id.tv_title1);
        this.mTitle2 = (TextView)this.findViewById(id.tv_title2);
        this.mAdView = this.findViewById(id.pulldown_ad);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        TextPaint paint = this.mTitle1.getPaint();
        float textWidth = paint.measureText(this.dropDownString);
        float iconWidth = this.mArrow.getDrawable() == null?-1.0F:(float)this.mArrow.getDrawable().getIntrinsicWidth();
        if(iconWidth > 0.0F || this.mIsCheckLeftPadding) {
            int paddingLeft = this.getMeasuredWidth() - (int)(textWidth + iconWidth + this.getContext().getApplicationContext().getResources().getDimension(dimen.updatebar_icon_space)) >> 1;
            this.vUpdateBar.setPadding(paddingLeft, 0, 0, 0);
        }

    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if(!this.mEnable) {
            return super.dispatchTouchEvent(event);
        } else if(this.mIsAutoScroller) {
            return true;
        } else {
            int action = event.getAction();
            if((action & MotionEvent.ACTION_MASK) != MotionEvent.ACTION_POINTER_DOWN && (action & MotionEvent.ACTION_MASK) != MotionEvent.ACTION_POINTER_UP) {
                boolean retValue = this.mDetector.onTouchEvent(event);
                if(action == 1) {
                    retValue = this.release();
                } else if(action == 3) {
                    retValue = this.release();
                }

                if(this.mState != 6 && this.mState != 7) {
                    if((retValue || this.mState == 2 || this.mState == 4 || this.mState == 5 || this.mState == 3) && this.getContentView().getTop() != 0) {
                        this.fireListViewScrollState(0);
                        event.setAction(3);
                        super.dispatchTouchEvent(event);
                        this.updateView();
                        return true;
                    } else {
                        this.updateView();
                        return super.dispatchTouchEvent(event);
                    }
                } else {
                    this.updateView();
                    return super.dispatchTouchEvent(event);
                }
            } else {
                return true;
            }
        }
    }

    private void fireListViewScrollState(int newState) {
        View contentView = this.getContentView();
        if(contentView instanceof ListView) {
            try {
                Method e = AbsListView.class.getDeclaredMethod("reportScrollStateChange", new Class[]{Integer.TYPE});
                e.setAccessible(true);
                invokeMethod(contentView, e, new Object[]{Integer.valueOf(newState)});
            } catch (NoSuchMethodException var4) {
                var4.printStackTrace();
            } catch (IllegalArgumentException var5) {
                var5.printStackTrace();
            } catch (SecurityException var6) {
                var6.printStackTrace();
            }
        }
    }


    private boolean release() {
        if(this.mPading >= 0) {
            return false;
        } else {
            switch(this.mState) {
                case 2:
                case 3:
                    if(Math.abs(this.mPading) < this.UPDATE_LENGHT) {
                        this.mState = 3;
                    }

                    this.scrollToClose();
                    break;
                case 4:
                case 5:
                    this.mState = 5;
                    this.scrollToUpdate();
                case 6:
                case 7:
                default:
                    break;
                case 8:
                    this.scrollToFullScreen();
            }

            return true;
        }
    }

    protected void scrollToUpdate() {
        this.mFlinger.startUsingDistance(-this.mPading - this.UPDATE_LENGHT, 300);
    }

    protected void scrollToClose() {
        this.mFlinger.startUsingDistance(-this.mPading, 300);
    }

    protected void scrollToFullScreen() {
        this.mFlinger.startUsingDistance(-this.MAX_FULL_SCREEN_HEIGHT - this.mPading, 300);
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(this.mOnScrollListener != null) {
            this.mOnScrollListener.onScroll();
        }

        if(distanceY < 0.0F) {
            distanceY = (float)((double)distanceY * 0.5D);
        }
        AdapterView v = (AdapterView)this.getContentView();
        if(v != null && v.getCount() != 0 && v.getChildCount() != 0) {
            boolean isStart = v.getFirstVisiblePosition() == 0;
            if(isStart) {
                isStart = v.getChildAt(0).getTop() == v.getPaddingTop();
            }

            if((distanceY >= 0.0F || !isStart) && this.mPading >= 0) {
                return false;
            } else if(Math.abs(distanceX) > Math.abs(distanceY) * 2.0F) {
                return false;
            } else {
                boolean r = this.move(distanceY, true);
                return r;
            }
        } else {
            return false;
        }
    }

    public boolean move(float distanceY, boolean isScroll) {
        if(this.mState == 6) {
            if(distanceY < 0.0F) {
                return true;
            }

            if(isScroll) {
                this.mState = 7;
            }
        }

        if(this.mState == 7 && distanceY < 0.0F && -this.mPading >= this.UPDATE_LENGHT) {
            return true;
        } else {
            this.mPading = (int)((float)this.mPading + distanceY);
            if(this.mPading > 0) {
                this.mPading = 0;
            }

            if(!isScroll) {
                if(this.mState == 5) {
                    this.mState = 6;
                    this.updateHandler();
                } else if(this.mState == 6 && this.mPading == 0) {
                    this.mState = 1;
                } else if(this.mState == 3 && this.mPading == 0) {
                    this.mState = 1;
                } else if(this.mState == 7 && this.mPading == 0) {
                    this.mState = 1;
                } else if(this.mState == 8 && this.mPading == 0) {
                    this.mState = 1;
                }

                this.invalidate();
                return true;
            } else {
                Drawable d;
                switch(this.mState) {
                    case 1:
                        if(this.mPading < 0) {
                            long d2 = System.currentTimeMillis();
                            if(d2 < this.adStartTime || d2 > this.adEndTime) {
                                this.clearAd();
                            }

                            this.mState = 2;
                            Drawable d1 = this.mProgressBar.getDrawable();
                            if(d1 instanceof AnimationDrawable) {
                                ((AnimationDrawable)d1).stop();
                            }

                            this.mProgressBar.setVisibility(View.GONE);
                            this.mArrow.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 2:
                        if(Math.abs(this.mPading) >= this.UPDATE_LENGHT) {
                            this.mState = 4;
                            d = this.mProgressBar.getDrawable();
                            if(d instanceof AnimationDrawable) {
                                ((AnimationDrawable)d).stop();
                            }

                            this.mProgressBar.setVisibility(View.GONE);
                            this.mArrow.setVisibility(View.VISIBLE);
                            this.makeArrowUp();
                        } else if(this.mPading == 0) {
                            this.mState = 1;
                        }
                        break;
                    case 3:
                    case 5:
                        if(isScroll) {
                            if(Math.abs(this.mPading) >= this.UPDATE_LENGHT) {
                                this.mState = 4;
                                d = this.mProgressBar.getDrawable();
                                if(d instanceof AnimationDrawable) {
                                    ((AnimationDrawable)d).stop();
                                }

                                this.mProgressBar.setVisibility(View.GONE);
                                this.mArrow.setVisibility(View.VISIBLE);
                                this.makeArrowUp();
                            } else if(Math.abs(this.mPading) < this.UPDATE_LENGHT) {
                                this.mState = 2;
                                d = this.mProgressBar.getDrawable();
                                if(d instanceof AnimationDrawable) {
                                    ((AnimationDrawable)d).stop();
                                }

                                this.mProgressBar.setVisibility(View.GONE);
                                this.mArrow.setVisibility(View.VISIBLE);
                                this.makeArrowDown();
                            } else if(this.mPading == 0) {
                                this.mState = 1;
                            }
                        } else if(this.mPading == 0) {
                            this.mState = 1;
                        }

                        this.invalidate();
                        return true;
                    case 4:
                        if(Math.abs(this.mPading) < this.UPDATE_LENGHT) {
                            this.mState = 2;
                            d = this.mProgressBar.getDrawable();
                            if(d instanceof AnimationDrawable) {
                                ((AnimationDrawable)d).stop();
                            }

                            this.mProgressBar.setVisibility(View.GONE);
                            this.mArrow.setVisibility(View.VISIBLE);
                            this.makeArrowDown();
                        }
                        break;
                    case 6:
                    case 8:
                        if(this.mPading == 0) {
                            this.mState = 1;
                        }

                        this.invalidate();
                        return true;
                    case 7:
                }

                return true;
            }
        }
    }

    protected void updateHandler() {
        if(this.mUpdateHandle != null) {
            this.mUpdateHandle.onUpdate();
        }

    }

    protected void scrollToFullScreenComplete() {
    }

    protected void updateView() {
        View updateBar = this.vUpdateBar;
        View content = this.getContentView();
        if(this.mDate == null) {
            this.mDate = new Date();
        }
        int l;
        int ul;
        Drawable d;
        switch(this.mState) {
            case 1:
                if(updateBar.getVisibility() != View.INVISIBLE) {
                    updateBar.setVisibility(View.INVISIBLE);
                }

                content.offsetTopAndBottom(-content.getTop());
                this.mTitle2.setVisibility(View.GONE);
                break;
            case 2:
            case 3:
                l = content.getTop();
                content.offsetTopAndBottom(-this.mPading - l);
                if(updateBar.getVisibility() != View.VISIBLE) {
                    updateBar.setVisibility(View.VISIBLE);
                }

                ul = updateBar.getTop();
                updateBar.offsetTopAndBottom(-this.mMaxHeight - this.mPading - ul);
                this.mTitle1.setText(this.dropDownString);
                this.mTitle2.setVisibility(View.GONE);
                break;
            case 4:
            case 5:
                l = content.getTop();
                content.offsetTopAndBottom(-this.mPading - l);
                if(updateBar.getVisibility() != View.VISIBLE) {
                    updateBar.setVisibility(View.VISIBLE);
                }

                ul = updateBar.getTop();
                updateBar.offsetTopAndBottom(-this.mMaxHeight - this.mPading - ul);
                this.mTitle1.setText(this.releaseUpdateString);
                if(this.mIsShowDate && this.mState == 5) {
                    if(this.mState == 5 && this.mIsShowStatusIcon) {
                        this.mProgressBar.setVisibility(View.VISIBLE);
                        d = this.mProgressBar.getDrawable();
                        if(d instanceof AnimationDrawable) {
                            ((AnimationDrawable)d).start();
                        }
                    }

                    this.mTitle2.setVisibility(View.VISIBLE);
                    this.mTitle2.setText(this.getContext().getApplicationContext().getString(string.update_time) + ":" + DISPLAY_DATE_FORMAT.format(this.mDate));
                } else {
                    this.mTitle2.setVisibility(View.GONE);
                }
                break;
            case 6:
            case 7:
                this.mTitle1.setText(this.releaseUpdateString);
                l = content.getTop();
                content.offsetTopAndBottom(-this.mPading - l);
                ul = updateBar.getTop();
                if(this.mIsShowStatusIcon) {
                    this.mProgressBar.setVisibility(View.VISIBLE);
                    d = this.mProgressBar.getDrawable();
                    if(d instanceof AnimationDrawable) {
                        ((AnimationDrawable)d).start();
                    }
                }

                if(this.mArrow.getVisibility() != View.GONE) {
                    this.mArrow.setVisibility(View.GONE);
                }

                this.mTitle1.setText(this.doingUpdateString);
                if(this.mIsShowDate) {
                    this.mTitle2.setVisibility(View.VISIBLE);
                    this.mTitle2.setText(this.getContext().getApplicationContext().getString(string.update_time) + ":" + DISPLAY_DATE_FORMAT.format(this.mDate));
                } else {
                    this.mTitle2.setVisibility(View.GONE);
                }

                updateBar.offsetTopAndBottom(-this.mMaxHeight - this.mPading - ul);
                if(updateBar.getVisibility() != View.VISIBLE) {
                    updateBar.setVisibility(View.VISIBLE);
                }
                break;
            case 8:
                l = content.getTop();
                ul = updateBar.getTop();
                this.mTitle1.setText(this.hongbaoString);
                this.makeArrowDown();
                content.offsetTopAndBottom(-this.MAX_FULL_SCREEN_HEIGHT - this.mPading);
                updateBar.offsetTopAndBottom(-this.MAX_FULL_SCREEN_HEIGHT - this.mPading);
                if(this.mState == 8 && this.MAX_FULL_SCREEN_HEIGHT - Math.abs(this.mPading) <= 0) {
                    this.scrollToFullScreenComplete();
                }
        }

        this.mTitleGroup.requestLayout();
        this.invalidate();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.vUpdateBar.layout(0, -this.mMaxHeight - this.mPading, this.getMeasuredWidth(), -this.mPading);
        if(this.getContentView() != null) {
            try {
                this.getContentView().layout(0, -this.mPading, this.getMeasuredWidth(), this.getMeasuredHeight() - this.mPading);
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public void setUpdateDate(Date date) {
        this.mDate = date;
    }

    public void endUpdate(Date date) {
        if(date != null) {
            this.mDate = date;
        }

        if(this.mPading != 0) {
            this.scrollToClose();
        }

        this.mState = 1;
        this.mArrow.clearAnimation();
        this.mArrow.setImageDrawable(this.mIsShowStatusIcon?this.mDownArrow:null);
        this.mArrowDirect = 2;
    }

    public void update() {
        this.mPading = -this.UPDATE_LENGHT;
        this.mState = 7;
        this.postDelayed(new Runnable() {
            public void run() {
                PullDownView.this.updateView();
            }
        }, 10L);
    }

    public void updateWithoutOffset() {
        this.mState = 7;
        this.invalidate();
    }

    public void setEnable(boolean enable) {
        this.mEnable = enable;
        this.invalidate();
    }

    public boolean isEnable() {
        return this.mEnable;
    }


    public void clearAd() {
        this.mAdView.setBackgroundDrawable((Drawable)null);
    }

    private void makeArrowUp() {
        if(this.mArrowDirect != 1) {
            this.mArrow.startAnimation(this.mAnimationUp);
            this.mArrowDirect = 1;
        }
    }

    protected void makeArrowDown() {
        if(this.mArrowDirect != 2) {
            this.mArrow.startAnimation(this.mAnimationDown);
            this.mArrowDirect = 2;
        }
    }

    public void onAnimationStart(Animation animation) {
    }

    public void onAnimationEnd(Animation animation) {
        if(this.mArrowDirect == 1) {
            this.getHandler().postDelayed(new Runnable() {
                public void run() {
                    PullDownView.this.mArrow.clearAnimation();
                    PullDownView.this.mArrow.setImageDrawable(PullDownView.this.mIsShowStatusIcon?PullDownView.this.mUpArrow:null);
                }
            }, 0L);
        } else if(this.mArrowDirect == 2) {
            this.getHandler().postDelayed(new Runnable() {
                public void run() {
                    PullDownView.this.mArrow.clearAnimation();
                    PullDownView.this.mArrow.setImageDrawable(PullDownView.this.mIsShowStatusIcon?PullDownView.this.mDownArrow:null);
                }
            }, 0L);
        }

    }

    public void onAnimationRepeat(Animation animation) {
    }

    public int getUpdateBarVisibility() {
        return this.vUpdateBar.getVisibility();
    }

    public boolean isUpdateBarClosed() {
        return this.mState == 1;
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }

    public interface OnScrollListener {
        void onScroll();
    }

    public interface UpdateHandle extends IUpdateHandle {
    }

    class Flinger implements Runnable {
        private Scroller mScroller = new Scroller(PullDownView.this.getContext().getApplicationContext());
        private int mLastFlingX;
        private boolean mIsStart;

        public Flinger() {
        }

        private void startCommon() {
            PullDownView.this.removeCallbacks(this);
        }

        public void startUsingDistance(int distance, int dur) {
            if(distance == 0) {
                --distance;
            }

            this.startCommon();
            this.mLastFlingX = 0;
            this.mScroller.startScroll(0, 0, -distance, 0, dur);
            PullDownView.this.mIsAutoScroller = true;
            PullDownView.this.post(this);
        }

        public void run() {
            Scroller scroller = this.mScroller;
            boolean more = scroller.computeScrollOffset();
            int x = scroller.getCurrX();
            int delta = this.mLastFlingX - x;
            PullDownView.this.move((float)delta, false);
            PullDownView.this.updateView();
            if(more) {
                this.mLastFlingX = x;
                PullDownView.this.post(this);
            } else {
                PullDownView.this.mIsAutoScroller = false;
                PullDownView.this.removeCallbacks(this);
            }

        }
    }

    public static Object invokeMethod(Object object, Method method, Object... params) {
        if(method == null) {
            return null;
        } else {
            Object result = null;

            try {
                result = method.invoke(object, params);
            } catch (IllegalArgumentException var5) {
            } catch (IllegalAccessException var6) {
            } catch (InvocationTargetException var7) {
            } catch (Exception var8) {
            }
            return result;
        }
    }
}
