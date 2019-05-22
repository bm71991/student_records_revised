package com.bm.android.studentinfo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
    public static void loadPhoto(Activity activity, File photoFile, ImageView picture,
                                 int placeHolderId) {
        GlideApp.with(activity)
                .load(photoFile)
                .placeholder(placeHolderId)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(picture);
    }
    public static void loadPhoto(Activity activity, File photoFile, ImageView picture,
                                 ColorDrawable c) {
        GlideApp.with(activity)
                .load(photoFile)
                .placeholder(c)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(picture);
    }
}