package com.facilityone.wireless.workorder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.adapter.GridImageAdapter;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.FMFileUtils;
import com.facilityone.wireless.a.arch.utils.PictureSelectorManager;
import com.facilityone.wireless.a.arch.widget.BottomTextListSheetBuilder;
import com.facilityone.wireless.a.arch.widget.EditNumberView;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.basiclib.widget.FullyGridLayoutManager;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderOptService;
import com.facilityone.wireless.workorder.presenter.WorkorderInputPresenter;
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
 * description:处理工单填写内容
 * Date: 2018/7/25 下午4:17
 */
public class WorkorderInputFragment extends BaseFragment<WorkorderInputPresenter> implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, BottomTextListSheetBuilder.OnSheetItemClickListener {

    private RecyclerView mPhotoRv;
    private EditNumberView mNumberView;

    private static final int MAX_PHOTO = 8;

    private Long mWoId;
    private String mWoCode;
    //图片
    private List<LocalMedia> mSelectList;
    private GridImageAdapter mGridImageAdapter;
    //请求
    private WorkorderOptService.WorkorderInputSaveReq mWorkorderInputSaveReq;

    @Override
    public WorkorderInputPresenter createPresenter() {
        return new WorkorderInputPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_input;
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
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mWoId = bundle.getLong(WorkorderInfoFragment.WORKORDER_ID);
            mWoCode = bundle.getString(WorkorderInfoFragment.WORKORDER_CODE,"");
        }
    }

    private void initView() {
        setTitle(R.string.workorder_work_content_title);
        setRightTextButton(R.string.workorder_save,R.id.workorder_input_save_menu_id);
        mNumberView = findViewById(R.id.env_desc);
        mPhotoRv = findViewById(R.id.rv_photo);

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
        if(TextUtils.isEmpty(mNumberView.getDesc()) && mSelectList.size() <= 0) {
            ToastUtils.showShort(R.string.workorder_work_content_tip);
        }else {
            mWorkorderInputSaveReq = new WorkorderOptService.WorkorderInputSaveReq();
            mWorkorderInputSaveReq.woId = mWoId;
            mWorkorderInputSaveReq.workContent = mNumberView.getDesc();
            if (mSelectList.size() > 0) {
                getPresenter().uploadFile(mSelectList);
            } else {
                getPresenter().saveInputContent();
            }
        }
    }

    @Override
    public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
        if (view.getId() == R.id.iv_photo3) {
            List<String> itemList = new ArrayList<>();
            itemList.add(getString(R.string.workorder_select_camera));
            boolean followOrderPhoto = SPUtils.getInstance(SPKey.SP_MODEL).getBoolean(SPKey.FOLLOW_ORDER_SELECT_PHOTO, true);
            if(followOrderPhoto) {
                itemList.add(getString(R.string.workorder_select_photo));
            }
            itemList.add(getString(R.string.workorder_cancel));
            new BottomTextListSheetBuilder(getContext())
                    .setShowTitle(true)
                    .setTitle(R.string.workorder_select_photo_title)
                    .addArrayItem(itemList)
                    .setOnSheetItemClickListener(WorkorderInputFragment.this)
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
        PictureSelector.create(WorkorderInputFragment.this)
                .themeStyle(R.style.picture_fm_style)
                .openExternalPreview(position, mSelectList);
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

    @Override
    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
        String projectName = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.PROJECT_NAME,"");
        String waterMark = projectName + "\r\n" + mWoCode;
        if (position == 0) {
            if (mSelectList.size() < MAX_PHOTO) {
                PictureSelectorManager.camera(WorkorderInputFragment.this, PictureConfig.REQUEST_CAMERA, waterMark);
            } else {
                ToastUtils.showShort(String.format(Locale.getDefault(), getString(R.string.workorder_select_photo_at_most), MAX_PHOTO));
            }
        } else if (position == 1) {
            PictureSelectorManager.MultipleChoose(WorkorderInputFragment.this, MAX_PHOTO, mSelectList, PictureConfig.CHOOSE_REQUEST, waterMark);
        }
        dialog.dismiss();
    }

    public WorkorderOptService.WorkorderInputSaveReq getWorkorderInputSaveReq() {
        return mWorkorderInputSaveReq;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //删除所有照片文件
        FileUtils.deleteAllInDir(FMFileUtils.getPicPath());
    }

    public static WorkorderInputFragment getInstance(Long woId, String woCode) {
        Bundle bundle = new Bundle();
        bundle.putLong(WorkorderInfoFragment.WORKORDER_ID, woId);
        bundle.putString(WorkorderInfoFragment.WORKORDER_CODE, woCode);
        WorkorderInputFragment infoFragment = new WorkorderInputFragment();
        infoFragment.setArguments(bundle);
        return infoFragment;
    }
}
