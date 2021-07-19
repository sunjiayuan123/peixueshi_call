package com.mf.library.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mf.library.R;

import java.lang.ref.WeakReference;

/**
 * 吐司相关工具类
 */
public final class

ToastUtils {

    private static final int DEFAULT_COLOR = 0x12000000;
    private static Toast sToast;
    private static int gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private static int xOffset = 0;
    private static int yOffset = (int) (64 * ContextHolder.getContext().getResources().getDisplayMetrics().density + 0.5);
    private static int backgroundColor = DEFAULT_COLOR;
    private static int bgResource = -1;
    private static int messageColor = DEFAULT_COLOR;
    private static WeakReference<View> sViewWeakReference;
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static boolean isCustom = false;
    private static Context mContext;
    private static ImageView mIv;
    private static Toast mToast;

    private TextView mTextView;
    private String message;

//    private ToastUtils() {
//        throw new UnsupportedOperationException("u can't instantiate me...");
//    }

    public ToastUtils(Context context, int layoutId, String msg) {
        message = msg;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //自定义布局
        View view = inflater.inflate(layoutId, null);
        //自定义toast文本
        mTextView = view.findViewById(R.id.toast_msg);
        mTextView.setText(msg);
        Log.i("ToastUtil", "Toast start...");
        if (mToast == null) {
            mToast = new Toast(context);
            Log.i("ToastUtil", "Toast create...");
        }
        //设置toast居中显示
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setView(view);
    }

    /**
     * 设置吐司位置
     *
     * @param gravity 位置
     * @param xOffset x偏移
     * @param yOffset y偏移
     */
    public static void setGravity(final int gravity, final int xOffset, final int yOffset) {
        ToastUtils.gravity = gravity;
        ToastUtils.xOffset = xOffset;
        ToastUtils.yOffset = yOffset;
    }

    /**
     * 设置吐司view
     *
     * @param layoutId 视图
     */
    public static void setView(@LayoutRes final int layoutId) {
        LayoutInflater inflate = (LayoutInflater) ContextHolder.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sViewWeakReference = new WeakReference<>(inflate.inflate(layoutId, null));
    }

    /**
     * 获取吐司view
     *
     * @return view
     */
    public static View getView() {
        if (sViewWeakReference != null) {
            final View view = sViewWeakReference.get();
            if (view != null) {
                return view;
            }
        }
        if (sToast != null) {
            return sToast.getView();
        }
        return null;
    }

    /**
     * 设置吐司view
     *
     * @param view 视图
     */
    public static void setView(final View view) {
        sViewWeakReference = view == null ? null : new WeakReference<>(view);
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor 背景色
     */
    public static void setBgColor(@ColorInt final int backgroundColor) {
        ToastUtils.backgroundColor = backgroundColor;
    }

    /**
     * 设置背景资源
     *
     * @param bgResource 背景资源
     */
    public static void setBgResource(@DrawableRes final int bgResource) {
        ToastUtils.bgResource = bgResource;
    }

    /**
     * 设置消息颜色
     *
     * @param messageColor 颜色
     */
    public static void setMessageColor(@ColorInt final int messageColor) {
        ToastUtils.messageColor = messageColor;
    }

    /**
     * 安全地显示短时吐司
     *
     * @param text 文本
     */
    public static void showShortSafe(@NonNull final CharSequence text) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(text, Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     */
    public static void showShortSafe(@StringRes final int resId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(resId, Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showShortSafe(@StringRes final int resId, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(resId, Toast.LENGTH_SHORT, args);
            }
        });
    }

    /**
     * 安全地显示短时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showShortSafe(final String format, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(format, Toast.LENGTH_SHORT, args);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param text 文本
     */
    public static void showLongSafe(@NonNull final CharSequence text) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(text, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     */
    public static void showLongSafe(@StringRes final int resId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(resId, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showLongSafe(@StringRes final int resId, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(resId, Toast.LENGTH_LONG, args);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showLongSafe(final String format, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(format, Toast.LENGTH_LONG, args);
            }
        });
    }

    /**
     * 显示短时吐司
     *
     * @param text 文本
     */
    public static void showShort(@NonNull final CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时吐司
     *
     * @param resId 资源Id
     */
    public static void showShort(@StringRes final int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showShort(@StringRes final int resId, final Object... args) {
        show(resId, Toast.LENGTH_SHORT, args);
    }

    /**
     * 显示短时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showShort(final String format, final Object... args) {
        show(format, Toast.LENGTH_SHORT, args);
    }

    /**
     * 显示长时吐司
     *
     * @param text 文本
     */
    public static void showLong(@NonNull final CharSequence text) {
        show(text, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时吐司
     *
     * @param resId 资源Id
     */
    public static void showLong(@StringRes final int resId) {
        show(resId, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showLong(@StringRes final int resId, final Object... args) {
        show(resId, Toast.LENGTH_LONG, args);
    }

    /**
     * 显示长时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showLong(final String format, final Object... args) {
        show(format, Toast.LENGTH_LONG, args);
    }

    /**
     * 显示吐司
     *
     * @param resId    资源Id
     * @param duration 显示时长
     */
    private static void show(@StringRes final int resId, final int duration) {
        show(ContextHolder.getContext().getResources().getText(resId).toString(), duration);
    }

    /**
     * 显示吐司
     *
     * @param resId    资源Id
     * @param duration 显示时长
     * @param args     参数
     */
    private static void show(@StringRes final int resId, final int duration, final Object... args) {
        show(String.format(ContextHolder.getContext().getResources().getString(resId), args), duration);
    }

    /**
     * 显示吐司
     *
     * @param format   格式
     * @param duration 显示时长
     * @param args     参数
     */
    private static void show(final String format, final int duration, final Object... args) {
        show(String.format(format, args), duration);
    }

    /**
     * 显示吐司
     *
     * @param text     文本
     * @param duration 显示时长
     */
    private static void show(final CharSequence text, final int duration) {
        cancel();
        if (sViewWeakReference != null) {
            final View view = sViewWeakReference.get();
            if (view != null) {
                sToast = new Toast(ContextHolder.getContext());
                if (isCustom) {
                    sToast.setView(view);
                }
                sToast.setDuration(duration);
            }
        }
        if (!isCustom) {
            if (messageColor != DEFAULT_COLOR) {
                SpannableString spannableString = new SpannableString(text);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(messageColor);
                spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sToast = Toast.makeText(ContextHolder.getContext(), spannableString, duration);
            } else {
                sToast = Toast.makeText(ContextHolder.getContext(), text, duration);
            }
        }
        View view = sToast.getView();
        if (bgResource != -1) {
            view.setBackgroundResource(bgResource);
        } else if (backgroundColor != DEFAULT_COLOR) {
            view.setBackgroundColor(backgroundColor);
        }
        sToast.setGravity(gravity, xOffset, yOffset);
        sToast.show();
    }

    /**
     * 显示带图片文字的吐司
     *
     * @param text
     * @param resImag
     */
    public static void showImage(int resImag, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }

        LinearLayout view = (LinearLayout) mToast.getView();
        if (mIv == null) {
            mIv = new ImageView(mContext);
        }
        mIv.setImageResource(resImag);
        if (view.getChildCount() != 1) {
            view.removeViewAt(1);
        }
        view.addView(mIv);
        mToast.show();
    }

    /**
     * 取消吐司显示
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

    /**
     * 自定义居中显示toast
     */
    public void show() {
        mToast.show();
        Log.i("ToastUtil", "Toast show...");
    }
}
