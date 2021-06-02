package com.facilityone.wireless.basiclib.icon;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconFontDescriptor;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:引入字体库
 * Date: 2018/3/21 下午3:46
 */
public class FontResModule implements IconFontDescriptor {

    @Override
    public String ttfFileName() {
        return "iconfont.ttf";
    }

    @Override
    public Icon[] characters() {
        return ResIcons.values();
    }
}
