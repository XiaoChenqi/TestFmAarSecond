package com.facilityone.wireless.a.arch.utils;

import android.content.Context;
import android.widget.ImageView;

import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.basiclib.utils.ImageLoadUtils;
import com.youth.banner.loader.ImageLoader;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/6/4 下午3:35
 */
public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        ImageLoadUtils.loadImageView(context,
                ((String) path),
                imageView,
                R.drawable.no_banner,
                R.drawable.no_banner);
    }
}
