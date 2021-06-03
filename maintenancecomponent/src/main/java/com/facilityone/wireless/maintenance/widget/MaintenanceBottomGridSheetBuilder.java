package com.facilityone.wireless.maintenance.widget;

import android.content.Context;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facilityone.wireless.a.arch.widget.FMBottomGridSheetBuilder;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * Created by peter.peng on 2018/11/22.
 */

public class MaintenanceBottomGridSheetBuilder extends FMBottomGridSheetBuilder{

    public MaintenanceBottomGridSheetBuilder(Context context) {
        super(context);
    }

    @Override
    protected void addViewsInSection(SparseArray<View> items, LinearLayout parent, int itemWidth) {
        ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
        if(layoutParams != null) {
            layoutParams.height = DensityUtil.dp2px(180);
        }
        parent.setGravity(Gravity.CENTER);
        for (int i = 0; i < items.size(); i++) {
            View itemView = items.get(i);
            setItemWidth(itemView);
            parent.addView(itemView);
        }
    }

    private void setItemWidth(View itemView) {
        LinearLayout.LayoutParams itemLp;
        if (itemView.getLayoutParams() != null) {
            itemLp = (LinearLayout.LayoutParams) itemView.getLayoutParams();
            itemLp.width = 0;
            itemLp.weight = 1;
        } else {
            itemLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1);
            itemView.setLayoutParams(itemLp);
        }
        itemLp.gravity = Gravity.CENTER_VERTICAL;
    }

}
