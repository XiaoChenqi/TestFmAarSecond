package com.facilityone.wireless.a.arch.ec.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.ImageLoadUtils;
import com.facilityone.wireless.basiclib.widget.FullyGridLayoutManager;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:全局统一的图片显示
 * Date: 2018/6/25 上午11:43
 */
public class GridImageAdapter extends BaseQuickAdapter<LocalMedia, BaseViewHolder> {

    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private boolean addMenu;//是否显示删除按钮
    private boolean showTip;//是否显示出共xxx张这种字样 true：显示 false：不显示，
    private boolean pic;//是否显示选择或拍照按钮
    private int max;//最多选择几张
    private boolean isVideo;//详情显示音频全部

    public GridImageAdapter(@Nullable List<LocalMedia> data, boolean addMenu) {
        super(R.layout.gv_image_item, data);
        this.addMenu = addMenu;
        this.showTip = addMenu;
        isVideo = false;
    }

    public GridImageAdapter(@Nullable List<LocalMedia> data, boolean addMenu, boolean isVideo) {
        super(R.layout.gv_image_item, data);
        this.addMenu = addMenu;
        this.showTip = addMenu;
        this.isVideo = isVideo;
    }

    public GridImageAdapter(@Nullable List<LocalMedia> data,
                            boolean addMenu, boolean pic, int max) {
        super(R.layout.gv_image_item, data);
        this.addMenu = addMenu;
        this.showTip = addMenu;
        this.pic = pic;
        this.max = max;
        isVideo = false;
    }

    @Override
    public int getItemCount() {
        if (pic) {
            if (getData().size() < max) {
                return getData().size() + 1;
            } else {
                return getData().size();
            }
        } else {
            return super.getItemCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (pic) {
            if (isShowAddItem(position)) {
                return TYPE_CAMERA;
            } else {
                return super.getItemViewType(position);
            }
        } else {
            return super.getItemViewType(position);
        }
    }

    private boolean isShowAddItem(int position) {
        int size = mData.size() == 0 ? 0 : mData.size();
        return position == size;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void convert(BaseViewHolder helper, LocalMedia item) {
        int position = helper.getLayoutPosition();
        helper.setGone(R.id.tv_tip, false);
        helper.setGone(R.id.iv_photo, !addMenu);
        helper.setGone(R.id.iv_photo2, addMenu);
        helper.setGone(R.id.iv_photo3, false);
        if (pic && getItemViewType(position) == TYPE_CAMERA) {
            helper.setGone(R.id.iv_photo3, true);
            helper.setImageResource(R.id.iv_photo3, R.drawable.take_photo_selector);
            helper.setGone(R.id.ll_del, false);
            helper.setGone(R.id.tv_tip, true);
            helper.addOnClickListener(R.id.iv_photo3);
        } else if (showTip && position == FullyGridLayoutManager.SPAN_COUNT - 1 && item.getDuration() != -1L) {
            helper.setImageResource(R.id.iv_photo2, R.color.grey_d6);
            helper.setGone(R.id.ll_del, false);
            helper.setGone(R.id.tv_tip, true);
            helper.setText(R.id.tv_tip, String.format(mContext.getString(R.string.arch_total), item.getDuration()));
        } else {
            helper.setGone(R.id.ll_del, ((!isVideo) && (!addMenu)));
            helper.addOnClickListener(R.id.ll_del);
            String path = "";
            if (item.isCut() && !item.isCompressed()) {
                path = item.getCutPath();
            } else if (item.isCompressed() || (item.isCut() && item.isCompressed())) {
                path = item.getCompressPath();
            } else {
                path = item.getPath();
            }

            // 图片
            if (item.isCompressed()) {
                Log.i("compress image result:", new File(item.getCompressPath()).length() / 1024 + "k");
                Log.i("压缩地址::", item.getCompressPath());
            }
            //视频显示时间
            int pictureType = PictureMimeType.isPictureType(item.getPictureType());
            long duration = item.getDuration();
            helper.setGone(R.id.tv_duration, pictureType == PictureConfig.TYPE_VIDEO);
            helper.setText(R.id.tv_duration, DateUtils.timeParse(duration));

            Log.i("原图地址::", item.getPath());
            ImageView view = helper.getView(R.id.iv_photo);
            ImageView view2 = helper.getView(R.id.iv_photo2);
            if (addMenu) {
                ImageLoadUtils.loadImageView(mContext, path, view2, R.color.grey_f6, R.drawable.default_small_image);
            } else {
                ImageLoadUtils.loadImageView(mContext, path, view, R.color.grey_f6, R.drawable.default_small_image);
            }
        }
    }

    public void setShowTip(boolean showTip) {
        this.showTip = showTip;
    }
}
