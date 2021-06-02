package com.facilityone.wireless.a.arch.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.utils.NoDoubleClickListener;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:底部为列表切列表内都是文字的菜单
 * Date: 2018/6/7 下午4:03
 */
public class BottomTextListSheetBuilder {

    private Context mContext;
    private List<String> items;
    private String title;
    private boolean isShowTitle;
    private QMUIBottomSheet mDialog;
    private OnSheetItemClickListener mOnSheetItemClickListener;
    private DialogInterface.OnDismissListener mOnBottomDialogDismissListener;
    private ListView mContainerView;
    private TextView mTitleTv;
    private ListAdapter mAdapter;

    public BottomTextListSheetBuilder(Context context) {
        mContext = context;
        items = new ArrayList<>();
        isShowTitle = false;
    }

    public BottomTextListSheetBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public BottomTextListSheetBuilder setTitle(@StringRes int textId) {
        setTitle(mContext.getResources().getString(textId));
        return this;
    }

    public BottomTextListSheetBuilder setShowTitle(boolean showTitle) {
        isShowTitle = showTitle;
        return this;
    }

    /**
     * 添加文本菜单
     *
     * @param text
     * @return
     */
    public BottomTextListSheetBuilder addItem(String text) {
        items.add(text);
        return this;
    }

    public BottomTextListSheetBuilder addItem(@StringRes int textId) {
        items.add(mContext.getResources().getString(textId));
        return this;
    }

    public BottomTextListSheetBuilder addArrayItem(@ArrayRes int arrayTextId) {
        String[] stringArray = mContext.getResources().getStringArray(arrayTextId);
        for (String s : stringArray) {
            items.add(s);
        }
        return this;
    }

    public BottomTextListSheetBuilder addArrayItem(List<String> menu) {
        items.addAll(menu);
        return this;
    }

    public BottomTextListSheetBuilder setOnSheetItemClickListener(OnSheetItemClickListener onSheetItemClickListener) {
        mOnSheetItemClickListener = onSheetItemClickListener;
        return this;
    }

    public QMUIBottomSheet build() {
        mDialog = new QMUIBottomSheet(mContext);
        View contentView = buildViews();
        mDialog.setContentView(contentView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return mDialog;
    }

    protected int getContentViewLayoutId() {
        return R.layout.fm_bottom_sheet_text_list;
    }

    private View buildViews() {
        View wrapperView = View.inflate(mContext, getContentViewLayoutId(), null);
        mContainerView = (ListView) wrapperView.findViewById(R.id.listview);
        mTitleTv = (TextView) wrapperView.findViewById(R.id.title);
        mAdapter = new ListAdapter();
        mContainerView.setAdapter(mAdapter);
        if(isShowTitle) {
            mTitleTv.setVisibility(View.VISIBLE);
            mTitleTv.setText(StringUtils.formatString(title));
        }else {
            mTitleTv.setVisibility(View.GONE);
        }
        return wrapperView;
    }

    private static class ViewHolder {
        TextView textView;
        View placeHolder;
    }

    private class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public String getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final String item = getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.fm_bottom_sheet_text_list_item, parent, false);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.tv_menu);
                holder.placeHolder = convertView.findViewById(R.id.view_placeholder);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.textView.setText(item);

            holder.textView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));

            if ((mContext.getString(R.string.arch_cancel)).equals(item)) {
                holder.placeHolder.setVisibility(View.VISIBLE);
                holder.textView.setTextColor(mContext.getResources().getColor(R.color.bottom_sheet_cancel_grey));
            }else {
                holder.placeHolder.setVisibility(View.GONE);
            }

            holder.textView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    if (mOnSheetItemClickListener != null) {
                        mOnSheetItemClickListener.onClick(mDialog, view, position, item);
                    }
                }
            });

            return convertView;
        }
    }

    public interface OnSheetItemClickListener {
        void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag);
    }

}
