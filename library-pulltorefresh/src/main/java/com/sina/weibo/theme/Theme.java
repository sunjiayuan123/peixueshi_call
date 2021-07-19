package com.sina.weibo.theme;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class Theme {
    private static Theme mInstance;
    protected Context context;


    private Theme(Context paramContext) {
        this.context = paramContext.getApplicationContext();
    }

    public static synchronized Theme getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new Theme(context);
        } else if(context.getApplicationContext() != mInstance.context) {
            mInstance.context = context;
        }

        return mInstance;
    }

    public Drawable getDrawableFromIdentifier(int drawableId){
        return context.getResources().getDrawable(drawableId);
    }

    public int getColorFromIdentifier(int colorId){
        return context.getResources().getColor(colorId);
    }

}
