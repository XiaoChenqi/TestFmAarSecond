package com.facilityone.wireless.patrol.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolItemEntity;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolPicEntity;
import com.facilityone.wireless.a.arch.offline.model.service.PatrolDbService;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.basiclib.widget.FullyGridLayoutManager;
import com.facilityone.wireless.patrol.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:检查项
 * Date: 2018/11/8 10:20 AM
 */
public class PatrolItemAdapter extends BaseQuickAdapter<PatrolItemEntity, BaseViewHolder> {
    private BaseFragment mBaseFragment;

    public PatrolItemAdapter(@Nullable List<PatrolItemEntity> data, BaseFragment baseFragment) {
        super(R.layout.item_patrol_item, data);
        mBaseFragment = baseFragment;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final PatrolItemEntity item) {
        if (item == null) {
            return;
        }

        final int position = helper.getLayoutPosition();

        helper.setGone(R.id.question_comment_tv, false);
        helper.setGone(R.id.question_comment_dv, false);
        helper.setGone(R.id.question_input_ll, false);
        helper.setGone(R.id.question_content_rg, false);

        helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.grey_6));

        RadioGroup rg = helper.getView(R.id.question_content_rg);
        rg.removeAllViews();
        rg.clearCheck();
        rg.setTag(position);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == -1) {
                    return;
                }

                int childCount = group.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    ((AppCompatRadioButton) group.getChildAt(i)).setChecked(false);
                }

                AppCompatRadioButton myRadioButton = (AppCompatRadioButton) group.getChildAt(checkedId);
                myRadioButton.setChecked(true);
                int position = (int) group.getTag();
                if (myRadioButton.getText() != null && !myRadioButton.getText().toString().equals(getData().get(position).getSelect())) {
                    if (mContentChangeListener != null) {
                        mContentChangeListener.change();
                    }
                }
                getData().get(position).setSelect(myRadioButton.getText().toString());

                PatrolItemEntity itemEntity = getData().get(position);
                String resultSelect = itemEntity.getSelect();
                String rightValue = itemEntity.getSelectRightValue();
                if (resultSelect != null) {
                    // 如果结果不正确，问题显示为红色
                    if (!TextUtils.isEmpty(rightValue)) {
                        if (rightValue.contains(",")) {
                            String[] rights = rightValue.split(",");
                            boolean exception = true;
                            for (String right : rights) {
                                if (right.equals(resultSelect)) {
                                    exception = false;
                                    break;
                                }
                            }
                            if (exception) {
                                helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.red_ff6666));
                            } else {
                                helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.grey_6));
                            }
                        } else {
                            if (!rightValue.equals(resultSelect)) {
                                helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.red_ff6666));
                            } else {
                                helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.grey_6));
                            }
                        }
                    }
                }
            }
        });

        final EditText inputEt = helper.getView(R.id.question_input_et);
        inputEt.setTag(position);

        if (!TextUtils.isEmpty(item.getComment())) {
            helper.setGone(R.id.question_comment_tv, true);
            helper.setText(R.id.question_comment_tv, item.getComment());
        }

        helper.addOnClickListener(R.id.question_edit_iv);
        helper.addOnClickListener(R.id.question_take_photo_iv);

        //设置检查项内容标题
        helper.setText(R.id.question_title_tv, StringUtils.formatString(item.getContent()) + (TextUtils.isEmpty(item.getUnit()) ? "" : "(" + item.getUnit() + ")"));
        //检查项结果
        switch (item.getResultType()) {
            //输入
            case PatrolDbService.QUESTION_TYPE_INPUT:
                final String input = item.getInput();
                inputEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        final int position = (int) inputEt.getTag();
                        if (!TextUtils.isEmpty(s.toString()) && !StringUtils.isNumerEX(s.toString())) {
                            item.setInput("");
                            if (mContentChangeListener != null) {
                                mContentChangeListener.change();
                            }
                            return;
                        }
                        if (TextUtils.isEmpty(getData().get(position).getInput()) && !TextUtils.isEmpty(s.toString())) {
                            if (mContentChangeListener != null) {
                                mContentChangeListener.change();
                            }
                        }

                        if (!TextUtils.isEmpty(getData().get(position).getInput()) && TextUtils.isEmpty(s.toString())) {
                            if (mContentChangeListener != null) {
                                mContentChangeListener.change();
                            }
                        }
                        if (!TextUtils.isEmpty(getData().get(position).getInput())
                                && !TextUtils.isEmpty(s.toString()) && !s.toString().equals(getData().get(position).getInput())) {
                            if (mContentChangeListener != null) {
                                mContentChangeListener.change();
                            }
                        }

                        getData().get(position).setInput(s.toString());

                        try {
                            PatrolItemEntity patrolItemEntity = getData().get(position);
                            if (!TextUtils.isEmpty(patrolItemEntity.getInput())) {
                                double input = Double.parseDouble(patrolItemEntity.getInput());
                                if (input > patrolItemEntity.getInputUpper() || input < patrolItemEntity.getInputFloor()) {
                                    helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.red_ff6666));
                                } else {
                                    helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.grey_6));
                                }
                            } else {
                                helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.orange_ff9900));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
                if (!TextUtils.isEmpty(input)) {
                    String clearFE = StringUtils.clearFE(input);
                    String subZero = StringUtils.subZeroAndDot(clearFE);
                    if (!TextUtils.isEmpty(subZero)) {
                        inputEt.setText(subZero);
                        inputEt.setSelection(subZero.length());
                    } else {
                        inputEt.setText("");
                        helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.orange_ff9900));
                    }
                } else {
                    inputEt.setText("");
                    helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.orange_ff9900));
                }
//                inputEt.setText(TextUtils.isEmpty(input) ? "" : input);
//                if (!TextUtils.isEmpty(input)) {
//                    inputEt.setSelection(input.length());
//                } else {
//                    helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.orange_ff9900));
//                }
                helper.setGone(R.id.question_input_ll, true);
                // 如果结果不正确，问题显示为红色
                if (item.getInputFloor() != null
                        && item.getInputUpper() != null && !TextUtils.isEmpty(input)) {
                    Double d = 0D;
                    try {
                        d = Double.parseDouble(input);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.d("巡检检查项解析异常");
                    }
                    if (d < item.getInputFloor()
                            || d > item.getInputUpper()) {
                        helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.red_ff6666));
                    }
                }
                break;
            //单选
            case PatrolDbService.QUESTION_TYPE_SINGLE:
                helper.setGone(R.id.question_content_rg, true);
                String[] selectValues = null;
                if (!TextUtils.isEmpty(item.getSelectEnums())) {
                    selectValues = item.getSelectEnums().trim().split(",");
                }
                if (selectValues != null && selectValues.length > 0) {
                    int inputId = -1;
                    int rightId = -1;
                    String select = item.getSelect();
                    String rightValue = item.getSelectRightValue();
                    for (int id = 0; id < selectValues.length; id++) {
                        String str = selectValues[id];
                        View inflate = LayoutInflater.from(mContext).inflate(R.layout.patrol_radiobutton, null);
                        AppCompatRadioButton rb = (AppCompatRadioButton) inflate;
                        rb.setText(str);
                        rb.setId(id);
                        rg.addView(rb, id);

                        if (rightValue != null && rightValue.contains(str) && rightId == -1) {
                            rightId = id;
                        }
                        if (select != null && str.equals(select)) {
                            inputId = id;
                        }
                    }

                    if (inputId != -1) {
                        rg.check(inputId);
                    } else {
                        if (rightId != -1) {
                            item.setSelect(selectValues[rightId]);
                            rg.check(rightId);
                        } else {
                            item.setSelect(selectValues[0]);
                            rg.check(0);
                        }
                    }
                    String resultSelect = item.getSelect();
                    if (resultSelect != null) {
                        // 如果结果不正确，问题显示为红色
                        if (!TextUtils.isEmpty(rightValue)) {
                            if (rightValue.contains(",")) {
                                String[] rights = rightValue.split(",");
                                boolean exception = true;
                                for (String right : rights) {
                                    if (right.equals(item.getSelect())) {
                                        exception = false;
                                        break;
                                    }
                                }
                                if (exception) {
                                    helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.red_ff6666));
                                }
                            } else {
                                if (!rightValue.equals(resultSelect)) {
                                    helper.setTextColor(R.id.question_title_tv, mContext.getResources().getColor(R.color.red_ff6666));
                                }
                            }
                        }
                    }


                }
                break;
            default:
                break;
        }
        final List<PatrolPicEntity> picEntities = item.getPicEntities();
        RecyclerView tvPhoto = helper.getView(R.id.rv_photo);
        if (picEntities != null && picEntities.size() > 0) {
            if (!TextUtils.isEmpty(item.getComment())) {
                helper.setGone(R.id.question_comment_dv, true);
            }
            FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext,
                    FullyGridLayoutManager.SPAN_COUNT,
                    GridLayoutManager.VERTICAL,
                    false);
            tvPhoto.setLayoutManager(manager);
            GridImageAdapter gridImageAdapter = new GridImageAdapter(picEntities, false);
            gridImageAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    //图片
                    List<LocalMedia> medias = new ArrayList<>();
                    for (PatrolPicEntity picEntity : picEntities) {
                        LocalMedia media = new LocalMedia();
                        media.setPath(picEntity.getPath());
                        medias.add(media);
                    }
                    PictureSelector.create(mBaseFragment)
                            .themeStyle(R.style.picture_fm_style)
                            .openExternalPreview(position, medias);
                }
            });
            gridImageAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
                    new FMWarnDialogBuilder(mContext).setIconVisible(false)
                            .setSureBluBg(true)
                            .setTitle(R.string.patrol_remind)
                            .setSure(R.string.patrol_sure)
                            .setTip(R.string.patrol_sure_delete_photo)
                            .addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, View view) {
                                    dialog.dismiss();
                                    GridImageAdapter tempAdapter = (GridImageAdapter) adapter;
                                    PatrolPicEntity pic = tempAdapter.getItem(position);
                                    if (mContentChangeListener != null) {
                                        mContentChangeListener.change();
                                    }
                                    if (pic != null) {
                                        List<Long> picIds = item.getPicIds();
                                        if (picIds == null) {
                                            picIds = new ArrayList<>();
                                            item.setPicIds(picIds);
                                        }
                                        if (pic.getId() != null) {
                                            picIds.add(pic.getId());
                                        }
                                    }
                                    tempAdapter.remove(position);
                                }
                            }).create(R.style.fmDefaultWarnDialog).show();
                }
            });
            tvPhoto.setAdapter(gridImageAdapter);
            tvPhoto.setVisibility(View.VISIBLE);
        } else {
            tvPhoto.setVisibility(View.GONE);
        }


    }


    private OnContentChangeListener mContentChangeListener;

    public void setContentChangeListener(OnContentChangeListener contentChangeListener) {
        mContentChangeListener = contentChangeListener;
    }

    public interface OnContentChangeListener {
        void change();
    }
}
