package com.arialyy.frame.http;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.arialyy.frame.cache.CacheUtil;

/**
 * Created by Lyy on 2015/7/7.
 * 加载网络图片
 */
public class ImageLoaderUtil {
    private static final String TAG = "ImageLoaderUtil";
    private RequestQueue mQueue;
    private CacheUtil mCacheUtil;
    private Context mContext;

    public ImageLoaderUtil(Context context) {
        mQueue = Volley.newRequestQueue(context);
//        mCacheUtil = new CacheUtil(context, true);
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 加载网络图片
     *
     * @param url
     * @param img
     * @param defaultImg 默认图片
     * @param errorImg   错误图片
     */
    public void loaderNetImg(String url, ImageView img, int defaultImg, int errorImg) {
        final ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(img, defaultImg, errorImg);
        imageLoader.get(url, imageListener);
    }

    public void loaderNetImg(String url, ImageView img, int defaultImg, int errorImg, int maxWidth, int maxHeight){
        final ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(img, defaultImg, errorImg);
        imageLoader.get(url, imageListener, maxWidth, maxHeight);
    }



}
