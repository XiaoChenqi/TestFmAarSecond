package com.facilityone.wireless.workorder.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.ec.adapter.AttachmentAdapter;
import com.facilityone.wireless.a.arch.ec.adapter.GridImageAdapter;
import com.facilityone.wireless.a.arch.ec.commonpresenter.CommonBasePresenter;
import com.facilityone.wireless.a.arch.ec.module.AttachmentBean;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.UrlUtils;
import com.facilityone.wireless.basiclib.utils.DateUtils;
import com.facilityone.wireless.basiclib.utils.ImageLoadUtils;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.basiclib.widget.FullyGridLayoutManager;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderHelper;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/12 下午5:07
 */
public class WorkorderHistoryAdapter extends BaseQuickAdapter<WorkorderService.HistoriesBean, BaseViewHolder> {

    private Map<Integer, String> workStatusMap;
    private Context mContext;
    private boolean justShowOne;
    private BaseFragment mBaseFragment;
    private CommonBasePresenter commonBasePresenter;

    public WorkorderHistoryAdapter(@Nullable List<WorkorderService.HistoriesBean> data,
                                   Context context,
                                   BaseFragment baseFragment,
                                   CommonBasePresenter commonBasePresenter) {
        this(data, context, false, baseFragment, commonBasePresenter);
    }

    public WorkorderHistoryAdapter(@Nullable List<WorkorderService.HistoriesBean> data,
                                   Context context,
                                   boolean justShowOne,
                                   BaseFragment baseFragment,
                                   CommonBasePresenter commonBasePresenter) {
        super(R.layout.fragment_workorder_history_item, data);
        workStatusMap = WorkorderHelper.getHistoryRecorderMap(context);
        mContext = context;
        this.justShowOne = justShowOne;
        mBaseFragment = baseFragment;
        this.commonBasePresenter = commonBasePresenter;
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkorderService.HistoriesBean item) {
        int layoutPosition = helper.getLayoutPosition();
        if (!justShowOne) {
            helper.setGone(R.id.view_his_line, layoutPosition == getData().size() - 1);
            helper.setGone(R.id.view_his_dash, layoutPosition != getData().size() - 1);
        } else {
            helper.setGone(R.id.view_his_line, false);
            helper.setGone(R.id.view_his_dash, true);
        }

        helper.setGone(R.id.ll_quick_show, justShowOne);
        helper.addOnClickListener(R.id.ll_quick_show);
        helper.setText(R.id.tv_his_name, StringUtils.formatString(item.handler));
        if (item.step != null) {
            helper.setText(R.id.tv_his_status, StringUtils.formatString(workStatusMap.get(item.step)));
        }
        if (item.operationDate != null) {
            helper.setText(R.id.tv_his_time, TimeUtils.millis2String(item.operationDate, DateUtils.SIMPLE_DATE_FORMAT_ALL));
        }
        helper.setGone(R.id.tv_his_desc, !TextUtils.isEmpty(item.content));
        helper.setText(R.id.tv_his_desc, StringUtils.formatString(item.content));
        if (item.handlerImgId != null) {
            CircleImageView mCVProfile = helper.getView(R.id.profile);
            ImageLoadUtils.loadImageView(mContext, UrlUtils.getImagePath(item.handlerImgId), mCVProfile, R.drawable.user_default_head, R.drawable.user_default_head);
        }

        RecyclerView tvPhoto = helper.getView(R.id.rv_his_photo);
        if (item.pictures != null && item.pictures.size() > 0) {
            FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext,
                    FullyGridLayoutManager.SPAN_COUNT,
                    GridLayoutManager.VERTICAL,
                    false);
            tvPhoto.setLayoutManager(manager);
            List<LocalMedia> mLocalMedias = new ArrayList<>();
            final List<LocalMedia> tem = new ArrayList<>();
            for (String image : item.pictures) {
                LocalMedia media = new LocalMedia();
                media.setPath(UrlUtils.getImagePath(image));
                media.setDuration(item.pictures.size());
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

        RecyclerView tvAttachment = helper.getView(R.id.rv_his_attachment);
        if (item.attachment != null && item.attachment.size() > 0) {
            final List<AttachmentBean> attachmentBeanList = new ArrayList<>();
            for (AttachmentBean attachmentBean : item.attachment) {
                attachmentBean.url = UrlUtils.getAttachmentPath(attachmentBean.src);
                attachmentBeanList.add(attachmentBean);
            }
            AttachmentAdapter attachmentAdapter = new AttachmentAdapter(attachmentBeanList, false);
            attachmentAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    //附件
                    AttachmentBean attachmentBean = attachmentBeanList.get(position);
                    commonBasePresenter.openAttachment(attachmentBean.url, attachmentBean.name, mContext);
                }
            });
            tvAttachment.setAdapter(attachmentAdapter);
            tvAttachment.setVisibility(View.VISIBLE);
        } else {
            tvAttachment.setVisibility(View.GONE);
        }

        helper.setGone(R.id.view_space, ((item.attachment != null && item.attachment.size() > 0)
                || (item.pictures != null && item.pictures.size() > 0)
                || !TextUtils.isEmpty(item.content)));


    }
}
