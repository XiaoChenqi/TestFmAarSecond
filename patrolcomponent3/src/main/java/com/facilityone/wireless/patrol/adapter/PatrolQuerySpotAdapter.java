package com.facilityone.wireless.patrol.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.module.PatrolConstant;
import com.facilityone.wireless.patrol.module.PatrolQueryService;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检查询点位
 * Date: 2018/11/21 3:16 PM
 */
public class PatrolQuerySpotAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_SPOT = 0;
    public static final int TYPE_EQU = 1;


    public PatrolQuerySpotAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_SPOT, R.layout.item_expandable_spot);
        addItemType(TYPE_EQU, R.layout.item_expandable_equ);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case TYPE_SPOT:
                final PatrolQueryService.SpotsBean spot = (PatrolQueryService.SpotsBean) item;
                helper.setText(R.id.name_tv, StringUtils.formatString(spot.name));
                helper.setGone(R.id.miss_tv, spot.hasLeak);
                helper.setGone(R.id.exception_tv, spot.hasException);
                helper.setGone(R.id.repair_tv, spot.hasOrder);
                if (spot.isExpanded()) {
                    helper.setText(R.id.expand_itv, R.string.icon_arrow_up);
                } else {
                    helper.setText(R.id.expand_itv, R.string.icon_arrow_down);
                }
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (spot.isExpanded()) {
                            collapse(pos, false);
                        } else {
                            expand(pos, false);
                        }
                    }
                });

                break;
            case TYPE_EQU:
                PatrolQueryService.EquipmentBean equ = (PatrolQueryService.EquipmentBean) item;
                if (!TextUtils.isEmpty(equ.code)) {
                    helper.setText(R.id.name_tv, StringUtils.formatString(equ.name) + "(" + equ.code + ")");
                } else {
                    helper.setText(R.id.name_tv, StringUtils.formatString(equ.name));
                }
                helper.setGone(R.id.miss_tv, equ.hasLeak);
                helper.setGone(R.id.exception_tv, equ.hasException);
                helper.setGone(R.id.repair_tv, equ.hasOrder);
                helper.setGone(R.id.stop_tv, equ.exceptionStatus != null && equ.exceptionStatus == PatrolConstant.EQU_STOP);

                helper.addOnClickListener(R.id.equ_ll);
                
                int layoutPosition = helper.getLayoutPosition();
                helper.setGone(R.id.long_view, layoutPosition == getData().size() - 1);
                helper.setGone(R.id.short_view, layoutPosition != getData().size() - 1);

                if (layoutPosition + 1 < getData().size()) {
                    if (getData().get(layoutPosition + 1) instanceof PatrolQueryService.SpotsBean) {
                        helper.setGone(R.id.long_view, true);
                        helper.setGone(R.id.short_view, false);
                    }
                }

                break;
        }
    }
}
