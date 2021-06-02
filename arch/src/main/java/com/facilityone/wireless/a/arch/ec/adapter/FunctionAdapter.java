package com.facilityone.wireless.a.arch.ec.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.ec.module.FunctionService;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/6/4 下午4:00
 */
public class FunctionAdapter extends BaseItemDraggableAdapter<FunctionService.FunctionBean, BaseViewHolder> {

    public FunctionAdapter(List<FunctionService.FunctionBean> bean) {
        super(R.layout.item_function, bean);
    }

    @Override
    protected void convert(BaseViewHolder helper, FunctionService.FunctionBean item) {
        helper.setText(R.id.tv_function_name, item.name);
        if (item.imageId != 0) {
            helper.setImageResource(R.id.iv_function_icon, item.imageId);
        }

        helper.getView(R.id.work_list_item_rl).setEnabled(item.clickable);

        int undoNum = item.undoNum;
        helper.setVisible(R.id.iv_function_undo_num, undoNum > 0);
        if (undoNum > 0) {
            String str = String.valueOf(undoNum);
            if (undoNum > 99) {
                str = "99+";
            }
            helper.setText(R.id.iv_function_undo_num, str);
        }
    }
}
