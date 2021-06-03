package com.facilityone.wireless.patrol.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolPicEntity;
import com.facilityone.wireless.basiclib.utils.ImageLoadUtils;
import com.facilityone.wireless.basiclib.widget.FullyGridLayoutManager;
import com.facilityone.wireless.patrol.R;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:全局统一的图片显示
 * Date: 2018/6/25 上午11:43
 */
public class GridImageAdapter extends BaseQuickAdapter<PatrolPicEntity, BaseViewHolder> {

    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private boolean addMenu;//是否显示删除按钮(同时也会显示出共xxx张这种字样如果条件允许)
    private boolean pic;//是否显示选择或拍照按钮
    private int max;//最多选择几张

    public GridImageAdapter(@Nullable List<PatrolPicEntity> data, boolean addMenu) {
        super(R.layout.gv_image_item, data);
        this.addMenu = addMenu;
    }

    public GridImageAdapter(@Nullable List<PatrolPicEntity> data,
                            boolean addMenu, boolean pic, int max) {
        super(R.layout.gv_image_item, data);
        this.addMenu = addMenu;
        this.pic = pic;
        this.max = max;
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
    protected void convert(BaseViewHolder helper, PatrolPicEntity item) {
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
        } else if (addMenu && position == FullyGridLayoutManager.SPAN_COUNT - 1 && item.getTotal() != 0) {
            helper.setImageResource(R.id.iv_photo2, R.color.grey_d6);
            helper.setGone(R.id.ll_del, false);
            helper.setGone(R.id.tv_tip, true);
            helper.setText(R.id.tv_tip, String.format(mContext.getString(R.string.patrol_total), item.getTotal()));
        } else {
            helper.setGone(R.id.ll_del, !addMenu);
            helper.addOnClickListener(R.id.ll_del);
            String path = item.getPath();

            Log.i("原图地址::", item.getPath());
            ImageView view = helper.getView(R.id.iv_photo);
            ImageView view2 = helper.getView(R.id.iv_photo2);
            if (addMenu) {
                ImageLoadUtils.loadImageView(mContext,path,view2,R.color.grey_f6,R.drawable.default_small_image);
            } else {
                ImageLoadUtils.loadImageView(mContext,path,view,R.color.grey_f6,R.drawable.default_small_image);
            }
        }
    }
}
