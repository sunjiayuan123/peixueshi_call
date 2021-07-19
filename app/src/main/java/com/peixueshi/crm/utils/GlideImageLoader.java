package com.peixueshi.crm.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.lzy.imagepicker.loader.ImageLoader;


public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        ArmsUtils.obtainAppComponentFromContext(activity)
                .imageLoader()
                .loadImage(activity,
                        ImageConfigImpl
                                .builder()
                                .url(path)
                                .imageView(imageView)
                                .isCrossFade(true)
                                .build());
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        ArmsUtils.obtainAppComponentFromContext(activity)
                .imageLoader()
                .loadImage(activity,
                        ImageConfigImpl
                                .builder()
                                .url(path)
                                .imageView(imageView)
                                .isCrossFade(true)
                                .build());
    }

    @Override
    public void clearMemoryCache() {
    }
}
