package com.facilityone.wireless.a.arch.ec.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.basiclib.utils.DataUtils;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/6/27 下午6:37
 */
public class AudioAdapter extends BaseQuickAdapter<LocalMedia, BaseViewHolder> {

    private boolean addMenu;
    private Context mContext;

    public AudioAdapter(@Nullable List<LocalMedia> data, Context context) {
        this(data, false, context);
    }

    public AudioAdapter(List<LocalMedia> data, boolean addMenu, Context context) {
        super(R.layout.item_audio, data);
        this.addMenu = addMenu;
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final LocalMedia item) {

        if (addMenu) {
            int px16 = SizeUtils.dp2px(16);
            int px6 = SizeUtils.dp2px(6);
            helper.itemView.setPadding(px16, 0, px16, px6);
        }

        int duration = (int) (item.getDuration() / 1000);
        helper.setText(R.id.tv_duration, duration + "”");

        helper.setGone(R.id.ll_del, !addMenu);

        final int position = helper.getLayoutPosition();

        final ImageView audioIv = helper.getView(R.id.iv_audio);
        audioIv.setImageResource(R.drawable.audio_play_03);

        LinearLayout playLL = helper.getView(R.id.ll_play);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) playLL.getLayoutParams();
        lp.width = DataUtils.getVoiceLineWight(duration);
        playLL.setLayoutParams(lp);
        playLL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnRemoveAudioListener != null) {
                    mOnRemoveAudioListener.onAudioClick(audioIv, item.getPath(), position);
                }
            }
        });

        View view = helper.getView(R.id.ll_del);
        view.setTag(helper.getLayoutPosition());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FMWarnDialogBuilder(mContext).setIconVisible(false)
                        .setSureBluBg(true)
                        .setTitle(R.string.arch_tips)
                        .setSure(R.string.arch_confirm)
                        .setTip(R.string.arch_delete_audio_tip)
                        .addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, View view) {
                                dialog.dismiss();
                                remove(position);
                                if (mOnRemoveAudioListener != null) {
                                    mOnRemoveAudioListener.onRemove(position);
                                }
//                                FileUtils.deleteFile(item.getPath());
                            }
                        }).create(R.style.fmDefaultWarnDialog).show();
            }
        });
    }

    public void setOnRemoveAudioListener(onRemoveAudioListener onRemoveAudioListener) {
        mOnRemoveAudioListener = onRemoveAudioListener;
    }

    private onRemoveAudioListener mOnRemoveAudioListener;

    public interface onRemoveAudioListener {
        void onRemove(int position);

        void onAudioClick(ImageView imageView, String path, int position);
    }
}
