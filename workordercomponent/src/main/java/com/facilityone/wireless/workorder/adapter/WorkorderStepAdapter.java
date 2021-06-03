package com.facilityone.wireless.workorder.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.ec.adapter.GridImageAdapter;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.UrlUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.basiclib.widget.FullyGridLayoutManager;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/12 下午5:07
 */
public class WorkorderStepAdapter extends BaseQuickAdapter<WorkorderService.StepsBean, BaseViewHolder> {

    private Context mContext;
    private BaseFragment mBaseFragment;

    public WorkorderStepAdapter(@Nullable List<WorkorderService.StepsBean> data
            , Context context
            , BaseFragment baseFragment) {
        super(R.layout.fragment_workorder_step_item, data);
        mContext = context;
        mBaseFragment = baseFragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkorderService.StepsBean item) {
        int layoutPosition = helper.getLayoutPosition();
        helper.setGone(R.id.view_line, layoutPosition == getData().size() - 1);
        helper.setGone(R.id.view_dash, layoutPosition != getData().size() - 1);

        helper.setText(R.id.steps_sort_tv, mContext.getString(R.string.workorder_step) + item.sort);
        helper.setText(R.id.steps_name_tv, StringUtils.formatString(item.step));
        helper.setText(R.id.steps_team_name_tv, StringUtils.formatString(item.workTeamName));
        helper.setText(R.id.steps_describe_tv, StringUtils.formatString(item.comment));
        if (item.finished == null || !item.finished) {
            helper.setText(R.id.steps_status_tv, R.string.workorder_unfinish);
            helper.setTextColor(R.id.steps_status_tv, mContext.getResources().getColor(R.color.workorder_red));
        } else {
            helper.setText(R.id.steps_status_tv, R.string.workorder_finished);
            helper.setTextColor(R.id.steps_status_tv, mContext.getResources().getColor(R.color.workorder_green));
        }

        helper.addOnClickListener(R.id.ll_content);
        
        RecyclerView tvPhoto = helper.getView(R.id.rv_step_photo);
        if (item.photos != null && item.photos.size() > 0) {
            FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext,
                    FullyGridLayoutManager.SPAN_COUNT,
                    GridLayoutManager.VERTICAL,
                    false);
            tvPhoto.setLayoutManager(manager);
            List<LocalMedia> mLocalMedias = new ArrayList<>();
            final List<LocalMedia> tem = new ArrayList<>();
            for (String image : item.photos) {
                LocalMedia media = new LocalMedia();
                media.setPath(UrlUtils.getImagePath(image));
                media.setDuration(item.photos.size());
                media.setPictureType(PictureMimeType.JPEG);
                tem.add(media);
            }
            if (tem.size() > FullyGridLayoutManager.SPAN_COUNT) {
                List<LocalMedia> localMedias = tem.subList(0, FullyGridLayoutManager.SPAN_COUNT);
                mLocalMedias.addAll(localMedias);
            } else {
                if (tem.size() == FullyGridLayoutManager.SPAN_COUNT) {
                    LocalMedia localMedia = tem.get(FullyGridLayoutManager.SPAN_COUNT - 1);
                    localMedia.setDuration(-1L);
                }
                mLocalMedias.addAll(tem);
            }
            GridImageAdapter gridImageAdapter = new GridImageAdapter(mLocalMedias, true);
            gridImageAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    //图片
                    PictureSelector.create(mBaseFragment)
                            .themeStyle(R.style.picture_fm_style)
                            .openExternalPreview(position, tem);
                }
            });
            tvPhoto.setAdapter(gridImageAdapter);
            tvPhoto.setVisibility(View.VISIBLE);
        } else {
            tvPhoto.setVisibility(View.GONE);
        }
    }
}
