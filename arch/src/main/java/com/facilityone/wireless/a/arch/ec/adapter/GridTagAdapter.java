package com.facilityone.wireless.a.arch.ec.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.ec.module.AttachmentBean;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:标签适配器
 * Date: 2018/7/10 下午5:11
 */
public class GridTagAdapter extends BaseQuickAdapter<AttachmentBean, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener {

    private Context mContext;
    private List<AttachmentBean> data;
    private boolean single;//单选 比如不限

    public GridTagAdapter(Context context, List<AttachmentBean> data) {
        this(context, data, false);
    }


    public GridTagAdapter(Context context, List<AttachmentBean> data, boolean single) {
        super(R.layout.grid_tag_layout, data);
        mContext = context;
        this.data = data;
        this.single = single;
        setOnItemClickListener(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, AttachmentBean item) {
        helper.setText(R.id.filter_condition_content_tv, item.name);
        helper.setGone(R.id.check_img, item.check);
        if (item.check) {
            helper.setBackgroundRes(R.id.filter_tag_ll, R.drawable.tag_checked_bg);
            helper.setTextColor(R.id.filter_condition_content_tv, mContext.getResources().getColor(R.color.colorPrimarySelect));
        } else {
            helper.setBackgroundRes(R.id.filter_tag_ll, R.drawable.tag_normal_bg);
            helper.setTextColor(R.id.filter_condition_content_tv, mContext.getResources().getColor(R.color.grey_6));
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (!single) {
            data.get(position).check = !data.get(position).check;
            if (position == 0) {
                for (int i = 0, size = data.size(); i < size; i++) {
                    data.get(i).check = false;
                }
                data.get(0).check = true;
            } else {
                if (!data.get(position).check) {
                    data.get(position).check = false;
                    int count = 0;
                    for (AttachmentBean attachmentBean : data) {
                        if (attachmentBean.check) {
                            count++;
                        }
                    }
                    if (count == 0) {
                        data.get(0).check = true;
                    }
                } else {
                    data.get(position).check = true;
                    if (data.get(0).check) {
                        data.get(0).check = false;
                    }
                }
            }
        } else { //单选，不管有多少标签，一次只选一个
            if (position == 0) {
                for (int i = 0, size = data.size(); i < size; i++) {
                    data.get(i).check = false;
                }
                data.get(0).check = true;
            } else {
                if (data.get(position).check) {
                    data.get(position).check = false;
                    data.get(0).check = true;
                } else {
                    for (AttachmentBean datum : data) {
                        datum.check = false;
                    }
                    data.get(position).check = true;
                }
            }

        }

        notifyDataSetChanged();
    }
}
