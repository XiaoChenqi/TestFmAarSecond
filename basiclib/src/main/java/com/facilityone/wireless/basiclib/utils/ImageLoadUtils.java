package com.facilityone.wireless.basiclib.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:图片加载工具
 * Date: 2018/3/13 下午2:52
 */
public class ImageLoadUtils {

    public static void loadImageView(Context mContext, Object path, ImageView mImageView) {
//        RequestOptions options = new RequestOptions()
//                .placeholder(R.mipmap.ic_launcher)    //加载成功之前占位图
//                .error(R.mipmap.ic_launcher)    //加载错误之后的错误图
//                .override(400, 400)    //指定图片的尺寸
        //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//                .fitCenter()
        //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
//                .centerCrop()
//                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
//                .skipMemoryCache(true)    //跳过内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.ALL)    //缓存所有版本的图像
//                .diskCacheStrategy(DiskCacheStrategy.NONE)    //跳过磁盘缓存
//                .diskCacheStrategy(DiskCacheStrategy.DATA)    //只缓存原来分辨率的图片
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);    //只缓存最终的图片
        Glide.with(mContext)
                .load(path)
                .into(mImageView);
    }

    //加载本地图片
    public static void loadImageView(Context mContext, File file, ImageView mImageView) {
        if (file.exists()) {
            loadImageView(mContext, file, mImageView);
        }
    }

    //设置加载中以及加载失败图片
    public static void loadImageView(Context mContext, String path, ImageView mImageView, int loadingImage, int errorImageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(loadingImage)
                .error(errorImageView)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(path)
                .apply(options)
                .into(mImageView);
    }
    //设置加载中以及加载失败图片
    public static void loadImageView(Context mContext, int drawable, ImageView mImageView, int loadingImage, int errorImageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(loadingImage)
                .error(errorImageView)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(drawable)
                .apply(options)
                .into(mImageView);
    }

    public static void loadImageView(Context mContext, File file, ImageView mImageView, int loadingImage, int errorImageView) {
        if (file.exists()) {
            loadImageView(mContext, file, mImageView, loadingImage, errorImageView);
        }
    }

    //加载指定大小
    public static void loadImageViewSize(Context mContext, String path, int width, int height, ImageView mImageView) {
        RequestOptions options = new RequestOptions()
                .override(width, height);
        Glide.with(mContext)
                .load(path)
                .apply(options)
                .into(mImageView);
    }


    //设置加载中以及加载失败图片并且指定大小
    public static void loadImageViewLodingSize(Context mContext, String path, int width, int height, ImageView mImageView, int loadingImage, int errorImageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .override(width, height)
                .placeholder(loadingImage)
                .error(errorImageView)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(path)
                .apply(options)
                .into(mImageView);
    }
}
