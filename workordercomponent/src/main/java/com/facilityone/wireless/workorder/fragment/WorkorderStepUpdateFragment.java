package com.facilityone.wireless.workorder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.adapter.GridImageAdapter;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.PictureSelectorManager;
import com.facilityone.wireless.a.arch.widget.BottomTextListSheetBuilder;
import com.facilityone.wireless.a.arch.widget.CustomContentItemView;
import com.facilityone.wireless.a.arch.widget.EditNumberView;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.basiclib.widget.FullyGridLayoutManager;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.presenter.WorkorderStepUpdatePresenter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:工单-维护步骤更新
 * Date: 2018/9/25 4:06 PM
 */
public class WorkorderStepUpdateFragment extends BaseFragment<WorkorderStepUpdatePresenter> implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, BottomTextListSheetBuilder.OnSheetItemClickListener {

    private CustomContentItemView mEtWorkTeam;
    private CustomContentItemView mEtStep;
    private EditNumberView mEtDesc;
    private Switch mSwitch;
    private RecyclerView mPhotoRv;

    private static final String WORKORDER_ID = "workorder_id";
    private static final String WORKORDER_STEP = "workorder_tool";

    private static final int MAX_PHOTO = 8;
    //图片
    private List<LocalMedia> mSelectList;
    private GridImageAdapter mGridImageAdapter;
    private Long mWoId;
    private WorkorderService.StepsBean mStepsBean;
    private WorkorderService.WorkorderStepUpdateReq mRequest;

    @Override
    public WorkorderStepUpdatePresenter createPresenter() {
        return new WorkorderStepUpdatePresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_step_update;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initRecyclerView();
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mWoId = arguments.getLong(WORKORDER_ID);
            mStepsBean = arguments.getParcelable(WORKORDER_STEP);
        }
    }

    private void initView() {
        if (mStepsBean != null) {
            setTitle(getString(R.string.workorder_step) + mStepsBean.sort);
        } else {
            setTitle(R.string.workorder_menu_step);
        }
        setRightTextButton(R.string.workorder_save, R.id.workorder_step_save_menu_id);

        mEtWorkTeam = findViewById(R.id.et_work_team_step);
        mEtStep = findViewById(R.id.step_civ);
        mEtDesc = findViewById(R.id.desc_step_env);
        mSwitch = findViewById(R.id.finish_switch);
        mPhotoRv = findViewById(R.id.rv_photo);

        if (mStepsBean != null) {
            mEtWorkTeam.setTipText(StringUtils.formatString(mStepsBean.workTeamName));
            mEtStep.setTipText(StringUtils.formatString(mStepsBean.step));
            mSwitch.setChecked(mStepsBean.finished == null ? false : mStepsBean.finished);
        }
    }

    private void initRecyclerView() {
        mSelectList = new ArrayList<>();
        mGridImageAdapter = new GridImageAdapter(mSelectList, false, true, MAX_PHOTO);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getContext(),
                FullyGridLayoutManager.SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        mPhotoRv.setLayoutManager(manager);
        mPhotoRv.setAdapter(mGridImageAdapter);
        mGridImageAdapter.setOnItemChildClickListener(this);
        mGridImageAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onRightTextMenuClick(View view) {
        if (mSwitch.isChecked()) {
            showLoading();
            mRequest = new WorkorderService.WorkorderStepUpdateReq();
            mRequest.woId = mWoId;
            mRequest.finished = true;
            mRequest.stepId = mStepsBean == null ? null : mStepsBean.stepId;
            mRequest.comment = mEtDesc.getDesc().toString();
            mStepsBean.comment = mRequest.comment;
            if (mStepsBean != null) {
                mStepsBean.finished = true;
            }
            if (mSelectList.size() > 0) {
                getPresenter().uploadFile(mSelectList);
            } else {
                getPresenter().updateStep();
            }
        } else {
            ToastUtils.showShort(R.string.workorder_is_completed);
        }
    }

    @Override
    public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
        if (view.getId() == R.id.iv_photo3) {
            new BottomTextListSheetBuilder(getContext())
                    .setShowTitle(true)
                    .setTitle(R.string.workorder_select_photo_title)
                    .addItem(R.string.workorder_select_camera)
                    .addItem(R.string.workorder_select_photo)
                    .addItem(R.string.workorder_cancel)
                    .setOnSheetItemClickListener(WorkorderStepUpdateFragment.this)
                    .build()
                    .show();
        } else if (view.getId() == R.id.ll_del) {
            new FMWarnDialogBuilder(getContext()).setIconVisible(false)
                    .setSureBluBg(true)
                    .setTitle(R.string.workorder_tip_title)
                    .setSure(R.string.workorder_confirm)
                    .setTip(R.string.workorder_delete_picture)
                    .addOnBtnSureClickListener(new FMWarnDialogBuilder.OnBtnClickListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, View view) {
                            dialog.dismiss();
                            GridImageAdapter tempAdapter = (GridImageAdapter) adapter;
                            String path = "";
                            LocalMedia item = tempAdapter.getItem(position);
                            if (item != null) {
                                if (item.isCut() && !item.isCompressed()) {
                                    path = item.getCutPath();
                                } else if (item.isCompressed() || (item.isCut() && item.isCompressed())) {
                                    path = item.getCompressPath();
                                }
                            }

                            tempAdapter.remove(position);

//                            if (!"".equals(path)) {
//                                FileUtils.deleteFile(path);
//                            }
                        }
                    }).create(R.style.fmDefaultWarnDialog).show();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        PictureSelector.create(WorkorderStepUpdateFragment.this)
                .themeStyle(R.style.picture_fm_style)
                .openExternalPreview(position, mSelectList);
    }

    @Override
    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
        if (position == 0) {
            if (mSelectList.size() < MAX_PHOTO) {
                PictureSelectorManager.camera(WorkorderStepUpdateFragment.this, PictureConfig.REQUEST_CAMERA);
            } else {
                ToastUtils.showShort(String.format(Locale.getDefault(), getString(R.string.workorder_select_photo_at_most), MAX_PHOTO));
            }
        } else if (position == 1) {
            PictureSelectorManager.MultipleChoose(WorkorderStepUpdateFragment.this, MAX_PHOTO, mSelectList, PictureConfig.CHOOSE_REQUEST);
        }
        dialog.dismiss();
    }

    public WorkorderService.WorkorderStepUpdateReq getRequest() {
        return mRequest;
    }

    public void setPhoto(List<String> photo) {
        if (mStepsBean != null && photo != null) {
            if (mStepsBean.photos != null) {
                mStepsBean.photos.addAll(photo);
            } else {
                mStepsBean.photos = photo;
            }
        }
    }

    public void setBundle() {
        setFragmentResult(RESULT_OK, null);
        pop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    mSelectList.clear();
                    mSelectList.addAll(selectList);
                    mGridImageAdapter.replaceData(mSelectList);
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    List<LocalMedia> selectCamera = PictureSelector.obtainMultipleResult(data);
                    mSelectList.addAll(selectCamera);
                    mGridImageAdapter.replaceData(mSelectList);
                    break;
            }
        }
    }

    public static WorkorderStepUpdateFragment getInstance(WorkorderService.StepsBean step
            , Long woId) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WORKORDER_STEP, step);
        bundle.putLong(WORKORDER_ID, woId);
        WorkorderStepUpdateFragment fragment = new WorkorderStepUpdateFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
