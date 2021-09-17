package com.peixueshi.crm.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.peixueshi.crm.R;
import com.peixueshi.crm.base.Constants;
import com.peixueshi.crm.ui.widget.CustomDialog;
import com.peixueshi.crm.ui.widget.CustomTransParentDialog;
import com.peixueshi.crm.ui.widget.LoadTransDialog;
import com.peixueshi.crm.ui.widget.TransParentDialog;

import java.text.DecimalFormat;


/**
 * 通用工具类
 * * Created by zhaobaolei on 2018/1/30.
 */

public class PromptManager {
    // 滚动条
    private static ProgressDialog dialog;
    private static long exitTime;
    private static TransParentDialog transparentDialog;
    private static LoadTransDialog loadTransDialog;
    public static CustomDialog myDialog;
    private static CustomTransParentDialog customDialog;
    private static PopupWindow pop_select;
    private static CustomDialog customDialogVideo;
//    protected static AutoHideSoftDialog autoHide;

    /**
     * 隐藏键盘的方法
     *
     * @param context
     */
    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void showProgressDialog(Context context) {
        closeProgressDialog();
        dialog = new ProgressDialog(context);
        dialog.setTitle(R.string.app_name);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("请等候，数据加载中……");
        dialog.show();
    }

    public static void closeProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void showTransParentDialog(Context context) {
        try {
            if (transparentDialog != null && transparentDialog.isShowing()) {
                return;
            }
            transparentDialog = new TransParentDialog(context,
                    R.style.transparentDialog);// 创建Dialog并设置样式主题
            Window win = transparentDialog.getWindow();
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.gravity = Gravity.CENTER;
        /*
         * params.x = -80;//设置x坐标 params.y = -60;//设置y坐标
		 */
            win.setAttributes(params);
            transparentDialog.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
            transparentDialog.show();
        } catch (Exception e) {
        }

    }

    public static void closeTransParentDialog() {
        if (transparentDialog != null && transparentDialog.isShowing()) {
            try {
                transparentDialog.dismiss();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeLoadingDialog() {
        if (loadTransDialog != null && loadTransDialog.isShowing()) {
            try {
                loadTransDialog.dismiss();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 自定义半透明对话框
     *
     * @param context Context
     * @param view    View
     * @param x       int
     * @param y       int
     */
    public static CustomDialog showCustomDialogFalse(Context context, View view, int x, int y) {
        if (myDialog != null && myDialog.isShowing()) {
            try {
                myDialog.dismiss();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        myDialog = new CustomDialog(context, R.style.MyDialogStyle, view);// 创建Dialog并设置样式主题
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        Window win = myDialog.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.x = x;// 设置x坐标呀
        params.y = y;// 设置y坐标
        params.gravity = Gravity.CENTER;
        win.setAttributes(params);
        myDialog.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
        if(view.getParent() != null){
            ((ViewGroup) view.getParent()).removeView(view);
        }
        myDialog.show();
        return myDialog;
    }

    /**
     * 自定义半透明对话框
     *
     * @param context Context
     * @param view    View
     * @param x       int
     * @param y       int
     */
    public static CustomDialog showCustomDialog(Context context, View view, int x, int y) {
        if (myDialog != null && myDialog.isShowing()) {
            try {
                myDialog.dismiss();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        myDialog = new CustomDialog(context, R.style.MyDialogStyle, view);// 创建Dialog并设置样式主题
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        Window win = myDialog.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.x = x;// 设置x坐标呀
        params.y = y;// 设置y坐标
        params.gravity = Gravity.CENTER;
        win.setAttributes(params);
        myDialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
        if(view.getParent() != null){
            ((ViewGroup) view.getParent()).removeView(view);
        }
        myDialog.show();
        return myDialog;
    }




    /**
     * 自定义半透明对话框
     *
     * @param context Context
     * @param view    View
     * @param x       int
     * @param y       int
     */
    public static CustomDialog showCustomDialogVideo(Context context, View view, int x, int y) {
        if (customDialogVideo != null && customDialogVideo.isShowing()) {
            try {
                customDialogVideo.dismiss();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        customDialogVideo = new CustomDialog(context, R.style.MyDialogStyle, view);// 创建Dialog并设置样式主题
        customDialogVideo.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        Window win = customDialogVideo.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.x = x;// 设置x坐标呀
        params.y = y;// 设置y坐标
        params.gravity = Gravity.CENTER;
        win.setAttributes(params);
        customDialogVideo.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
        if(view.getParent() != null){
            ((ViewGroup) view.getParent()).removeView(view);
        }
        customDialogVideo.show();
        return customDialogVideo;
    }



    private static CustomDialog updateDialog;
    public static void showUPdateDialog(Context context, View view, int x, int y) {
        if (updateDialog != null && updateDialog.isShowing()) {
            return;
        }
        updateDialog = new CustomDialog(context, R.style.MyDialogStyle, view);// 创建Dialog并设置样式主题
        updateDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        Window win = updateDialog.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.x = x;// 设置x坐标呀
        params.y = y;// 设置y坐标
        params.gravity = Gravity.CENTER;
        win.setAttributes(params);
        updateDialog.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
        updateDialog.setCancelable(false);
        updateDialog.show();
    }


    public static void closeUpdateDialog() {
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
    }

    /**
     * @param context Context
     * @param view    自定义View布局
     * @param gravity 要显示的位置
     * @param cancel  点击外部是否消失 true消失
     */
    public static synchronized void showCommonDialog(Context context,
                                                     View view, int gravity, boolean cancel) {
        if (customDialog != null && customDialog.isShowing()) {
            return;
        }
        customDialog = new CustomTransParentDialog(context,
                R.style.transparentDialog, view);// 创建Dialog并设置样式主题
        Window win = customDialog.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.gravity = gravity;
        win.setAttributes(params);
        customDialog.setCanceledOnTouchOutside(cancel);// 设置点击Dialog外部任意区域关闭Dialog
        customDialog.show();
    }


    public static void closeCustomDialog() {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
    }




    /**
     * 当判断当前手机没有网络时使用
     */

    public static void showNoNetWork(final Context context) {
        Builder builder = new Builder(context);
        builder.setIcon(R.drawable.button_agree)
                .setTitle(R.string.app_name)
                .setMessage("当前无网络")
                .setPositiveButton("设置", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到系统的网络设置界面
                        Intent intent = new Intent();
                        intent.setClassName("com.android.settings",
                                "com.android.settings.WirelessSettings");
                        context.startActivity(intent);
                    }
                }).setNegativeButton("知道了", null).show();
    }

    /**
     * 退出系统
     */
    public static void showToast(Context context, String msg) {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            exitTime = System.currentTimeMillis();
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            View view = View.inflate(context, R.layout.mytoast, null);
            TextView tv = (TextView) view.findViewById(R.id.toast_tv);
            tv.setText(msg);
            toast.setView(view);
            toast.show();
        }
    }

    public static void showToast(Context context, int msgResId) {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            exitTime = System.currentTimeMillis();
            Toast toast = Toast.makeText(context, msgResId, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 100 / 4);
            View view = View.inflate(context, R.layout.mytoast, null);
            TextView tv = (TextView) view.findViewById(R.id.toast_tv);
            tv.setText(msgResId);
            toast.setView(view);
            toast.show();
        }
    }

    // 当测试阶段时true
    private static final boolean isShow = true;

    /**
     * 测试用 在正式投入市场：删
     */
    public static void showToastTest(Context context, String msg) {
        if (isShow) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 显示错误提示框
     */

    public static void showErrorDialog(Context context, int msg) {
        String str = context.getString(msg);
        new Builder(context).setIcon(R.drawable.button_agree)
                .setTitle(R.string.app_name).setMessage(str)//
                .setNegativeButton("错误提示", null).show();
    }

    private static Toast toast;

    public static void showMyToast(String text, Context context) {
        if (toast != null) {
            return;
        }
        toast = Toast.makeText(context, text,
                Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = View.inflate(context, R.layout.mytoast, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_tv);
        tv.setText(text);
        toast.setView(view);
        toast.show();
        toast = null;
    }

    public static void showMyToast(int id, Context context) {
        Toast toast = Toast.makeText(context, id,
                Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = View.inflate(context, R.layout.mytoast, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_tv);
        tv.setText(id);
        toast.setView(view);
        toast.show();
    }

    /**
     * @param popView
     * @param rl_remen 点击的view
     * @return
     */
    public static synchronized PopupWindow showPopWindow(View popView, View rl_remen) {
        int[] location = new int[2];
        rl_remen.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        int showY = y + rl_remen.getHeight();
        PopupWindow popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, Constants.screenHeight - showY);
        popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//            popupWindow.setOnDismissListener(new PoponDismissListener());
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
//            popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);  //设置动画
        /*if (Build.VERSION.SDK_INT != 24) {
            //只有24这个版本有问题，好像是源码的问题
            popupWindow.showAsDropDown(rl_remen);
        } else {*/
        //7.0 showAsDropDown没卵子用 得这么写
        popupWindow.showAtLocation(rl_remen, Gravity.NO_GRAVITY, 10, showY);//
        return popupWindow;
    }

    public static synchronized void showPopToast(View view, View parent, int x, int y) {
        pop_select = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pop_select.setBackgroundDrawable(new ColorDrawable(0x00000000));
        pop_select.showAtLocation(parent, Gravity.CENTER, x, y);

    }
    public static synchronized void showPopViewbb(View view, View dropof,Activity activity) {
        pop_select = new PopupWindow(view, dropof.getWidth(), 400, true);
        pop_select.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // pop_select.showAsDropDown(dropof, 0, 0);
        pop_select.setAnimationStyle(R.style.PopupWindowAnimStyle);
        if (Build.VERSION.SDK_INT >= 24) {
            int[] point = new int[2];
            dropof.getLocationInWindow(point);
            pop_select.showAtLocation(activity.getWindow().getDecorView(), Gravity.NO_GRAVITY, point[0], point[1] + dropof.getHeight()+20);
        } else {
            pop_select.showAsDropDown(dropof);
        }
       /* if(popDrop==null){
            popDrop=new PopupWindow(activity);
            popDrop.setWidth(dropof.getWidth());
            popDrop.setHeight(280);
            popDrop.setAnimationStyle(R.style.PopupWindowAnimStyle);
            popDrop.setFocusable(true);
            popDrop.setTouchable(true);
            popDrop.setBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(activity,R.color.colorPrimary)));
            View v=new View(activity);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            popDrop.setContentView(v);
        }
        if(!popDrop.isShowing()){
            int[] position=new int[2];
            view.getLocationOnScreen(position);
            popDrop.showAtLocation(view, Gravity.NO_GRAVITY,position[0],
                    position[1]+ view.getHeight());
        }*/

    }
    public static synchronized void showPopView(View view, View dropof,Activity activity) {
        pop_select = new PopupWindow(view, dropof.getWidth(),
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pop_select.setBackgroundDrawable(new ColorDrawable(0x00000000));
       // pop_select.showAsDropDown(dropof, 0, 0);
        pop_select.setAnimationStyle(R.style.PopupWindowAnimStyle);
        if (Build.VERSION.SDK_INT >= 24) {
            int[] point = new int[2];
            dropof.getLocationInWindow(point);
            pop_select.showAtLocation(activity.getWindow().getDecorView(), Gravity.NO_GRAVITY, point[0], point[1] + dropof.getHeight()+20);
        } else {
            pop_select.showAsDropDown(dropof);
        }
       /* if(popDrop==null){
            popDrop=new PopupWindow(activity);
            popDrop.setWidth(dropof.getWidth());
            popDrop.setHeight(280);
            popDrop.setAnimationStyle(R.style.PopupWindowAnimStyle);
            popDrop.setFocusable(true);
            popDrop.setTouchable(true);
            popDrop.setBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(activity,R.color.colorPrimary)));
            View v=new View(activity);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            popDrop.setContentView(v);
        }
        if(!popDrop.isShowing()){
            int[] position=new int[2];
            view.getLocationOnScreen(position);
            popDrop.showAtLocation(view, Gravity.NO_GRAVITY,position[0],
                    position[1]+ view.getHeight());
        }*/

    }


    public static synchronized void showPopViewSize(View view, View dropof,int width,Activity activity) {
        pop_select = new PopupWindow(view, width,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pop_select.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // pop_select.showAsDropDown(dropof, 0, 0);
        pop_select.setAnimationStyle(R.style.PopupWindowAnimStyle);
        if (Build.VERSION.SDK_INT >= 24) {
            int[] point = new int[2];
            if(dropof != null){
                dropof.getLocationInWindow(point);
                pop_select.showAtLocation(activity.getWindow().getDecorView(), Gravity.NO_GRAVITY, point[0], point[1] + dropof.getHeight()+20);
            }
        } else {
            pop_select.showAsDropDown(dropof);
        }
       /* if(popDrop==null){
            popDrop=new PopupWindow(activity);
            popDrop.setWidth(dropof.getWidth());
            popDrop.setHeight(280);
            popDrop.setAnimationStyle(R.style.PopupWindowAnimStyle);
            popDrop.setFocusable(true);
            popDrop.setTouchable(true);
            popDrop.setBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(activity,R.color.colorPrimary)));
            View v=new View(activity);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            popDrop.setContentView(v);
        }
        if(!popDrop.isShowing()){
            int[] position=new int[2];
            view.getLocationOnScreen(position);
            popDrop.showAtLocation(view, Gravity.NO_GRAVITY,position[0],
                    position[1]+ view.getHeight());
        }*/

    }

    public static synchronized void closePopWindow() {
        if (pop_select != null && pop_select.isShowing()) {
            pop_select.dismiss();
        }
    }


    public static void showLoadingDialog(Context context, String text) {
        if (loadTransDialog != null && loadTransDialog.isShowing()) {
            return;
        }
        loadTransDialog = new LoadTransDialog(context,
                R.style.transparentDialog);// 创建Dialog并设置样式主题
        Window win = loadTransDialog.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.gravity = Gravity.CENTER;
        /*
		 * params.x = -80;//设置x坐标 params.y = -60;//设置y坐标
		 */
        win.setAttributes(params);
        loadTransDialog.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
        loadTransDialog.show();
    }


    public static final int defaultBotom = -100;

    /**
     * @param activity      Activity
     * @param attachOnView  显示在这个View的下方
     * @param popView       被显示在PopupWindow上的View
     * @param popShowHeight 被显示在PopupWindow上的View的高度，可以传默认值defaultBotom
     * @param popShowWidth  被显示在PopupWindow上的View的宽度，一般是传attachOnView的getWidth()
     * @return PopupWindow
     */
    public static PopupWindow show(Activity activity, View attachOnView, View popView, final int popShowHeight, final int popShowWidth) {
        if (popView != null && popView.getParent() != null) {
            ((ViewGroup) popView.getParent()).removeAllViews();
        }

        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        int location[] = new int[2];
        int x, y;
        int popHeight = 0, popWidth = 0;

        attachOnView.getLocationOnScreen(location);
        x = location[0];
        y = location[1];

        int h = attachOnView.getHeight();

        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int screenHeight = metric.heightPixels; // 屏幕高度（像素）
//        int screenHeight = DeviceInfoManager.getInstance().getScreenHeight();

        /*if (popShowHeight == defaultBotom) {
            popHeight = screenHeight / 6;
            popHeight = Math.abs(screenHeight - (h + y)) - popHeight;
        } else if (popHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            popHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            popHeight = popShowHeight;
        }

        if (popShowWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
            popWidth = attachOnView.getWidth();
        } else {
            popWidth = popShowWidth;
        }

        popupWindow = new PopupWindow(popView, popWidth, popHeight, true);*/
        final PopupWindow popupWindow = new PopupWindow(activity);
        popupWindow.setWidth(Integer.parseInt(new DecimalFormat("0").format(width)));
        popupWindow.setHeight(Integer.parseInt(new DecimalFormat("0").format(screenHeight * 0.8)));

        //这行代码时用来让PopupWindow点击区域之外消失的，这个应该是PopupWindow的一个bug。
        //但是利用这个bug可以做到点击区域外消失
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());

//        popupWindow.setAnimationStyle(R.style.PopupAnimationDown);
        popupWindow.showAtLocation(attachOnView, Gravity.NO_GRAVITY, x, h + y);
        popupWindow.update();
        return popupWindow;
    }
}
