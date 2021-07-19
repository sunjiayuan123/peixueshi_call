package com.mf.library.utils;

import android.content.Context;

/**
 *
 */

public class ContextHolder {
    private static Context context;

    public static void initContext(Context applicationContext) {
        context = applicationContext;
    }

    public static Context getContext() {
        return context;
    }
}
