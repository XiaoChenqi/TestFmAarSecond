package com.facilityone.wireless.basiclib.icon;

import com.joanzapata.iconify.Icon;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:设置字体对应值
 * Date: 2018/3/21 下午3:47
 */
public enum ResIcons implements Icon {
    icon_scan('\ue600'),
    icon_camera('\ue636'),
    icon_down('\ue601'),
    icon_right('\ue602'),
    icon_clock('\ue603'),
    icon_arrow_up('\ue604'),
    icon_check('\ue605'),
    icon_warn('\ue606'),
    icon_back('\ue607'),
    icon_arrow_down('\ue615'),
    icon_pic('\ue619'),
    icon_share('\ue61d'),
    icon_add('\ue634'),
    icon_input('\ue647'),
    icon_call('\ue668'),
    icon_more('\ue6a7');

    private char character;

    ResIcons(char character) {
        this.character = character;
    }

    @Override
    public String key() {
        return name().replace('_', '-');
    }

    @Override
    public char character() {
        return character;
    }
}
