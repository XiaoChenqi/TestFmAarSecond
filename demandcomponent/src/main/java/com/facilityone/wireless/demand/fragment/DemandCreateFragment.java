package com.facilityone.wireless.demand.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cktim.camera2library.Camera2Config;
import com.facilityone.wireless.a.arch.ec.adapter.AudioAdapter;
import com.facilityone.wireless.a.arch.ec.adapter.GridImageAdapter;
import com.facilityone.wireless.a.arch.ec.audio.AudioPlayConnection;
import com.facilityone.wireless.a.arch.ec.audio.AudioPlayManager;
import com.facilityone.wireless.a.arch.ec.audio.AudioPlayService;
import com.facilityone.wireless.a.arch.ec.module.CommonUrl;
import com.facilityone.wireless.a.arch.ec.module.ConstantMeida;
import com.facilityone.wireless.a.arch.ec.module.ISelectDataService;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.ec.module.UserService;
import com.facilityone.wireless.a.arch.ec.selectdata.SelectDataFragment;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.FMFileUtils;
import com.facilityone.wireless.a.arch.utils.PictureSelectorManager;
import com.facilityone.wireless.a.arch.widget.CustomContentItemView;
import com.facilityone.wireless.a.arch.widget.EditNumberView;
import com.facilityone.wireless.a.arch.widget.FMBottomAudioSheetBuilder;
import com.facilityone.wireless.a.arch.widget.FMBottomGridSheetBuilder;
import com.facilityone.wireless.a.arch.widget.FMWarnDialogBuilder;
import com.facilityone.wireless.basiclib.utils.GsonUtils;
import com.facilityone.wireless.basiclib.utils.PermissionHelper;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.basiclib.video.SimplePlayer;
import com.facilityone.wireless.basiclib.widget.FullyGridLayoutManager;
import com.facilityone.wireless.demand.R;
import com.facilityone.wireless.demand.module.DemandCreateService;
import com.facilityone.wireless.demand.presenter.DemandCreatePresenter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:创建需求
 * Date: 2018/6/21 下午4:07
 */
public class DemandCreateFragment extends BaseFragment<DemandCreatePresenter> implements View.OnClickListener,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, PermissionHelper.OnPermissionGrantedListener, PermissionHelper.OnPermissionDeniedListener, FMBottomAudioSheetBuilder.OnAudioFinishListener, AudioAdapter.onRemoveAudioListener {
    private CustomContentItemView mTypeCiv;
    private CustomContentItemView mContactCiv;
    private CustomContentItemView mTelCiv;
    private EditNumberView mNumberView;
    private ImageView mMenuIv;
    private RecyclerView mPhotoRv;
    private TextView mPicTitleTv;
    private RecyclerView mVideoRv;
    private TextView mVideoTitleTv;
    private RecyclerView mAudioRv;
    private TextView mAudioTitleTv;

    private static final int MAX_PHOTO = 1000;
    private static final int REQUEST_DEMAND_TYPE = 5009;
    //图片
    private List<LocalMedia> mSelectList;
    private GridImageAdapter mGridImageAdapter;
    //视频
    private List<LocalMedia> mVideoSelectList;
    private GridImageAdapter mVideoGridImageAdapter;
    //音频
    private List<LocalMedia> mAudioSelectList;
    private AudioAdapter mAudioAdapter;

    private DemandCreateService.DemandCreateReq request;
    private QMUIBottomSheet mAudioDialog;

    @Override
    public DemandCreatePresenter createPresenter() {
        return new DemandCreatePresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_demand_create;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        getPresenter().getUserInfo();
        initRecyclerView();
        initData();
        initAudioPlayService();
    }

    private void initAudioPlayService() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), AudioPlayService.class);
        AudioPlayConnection audioPlayConnection = new AudioPlayConnection();
        getActivity().bindService(intent, audioPlayConnection, Context.BIND_AUTO_CREATE);
    }

    private void initView() {
        setTitle(R.string.demand_create_title);
        setRightTextButton(R.string.demand_submit, R.id.demand_create_upload_id);
        mTypeCiv = findViewById(R.id.civ_type);
        mContactCiv = findViewById(R.id.civ_contact);
        mTelCiv = findViewById(R.id.civ_tel);
        mMenuIv = findViewById(R.id.iv_add_menu);

        mPhotoRv = findViewById(R.id.rv_photo);
        mPicTitleTv = findViewById(R.id.tv_pic);

        mVideoRv = findViewById(R.id.rv_video);
        mVideoTitleTv = findViewById(R.id.tv_video);

        mAudioRv = findViewById(R.id.rv_audio);
        mAudioTitleTv = findViewById(R.id.tv_audio);
        mNumberView = findViewById(R.id.env_desc);

        mMenuIv.setOnClickListener(this);
        mTypeCiv.setOnClickListener(this);

        mTelCiv.getInputEt().setInputType(InputType.TYPE_CLASS_PHONE);

        InputFilter inputFilter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence charSequence, int start, int end, Spanned dest, int dstart, int dend) {
                String regex = "^[0-9\\-]+$";
                boolean isPhone = Pattern.matches(regex, charSequence.toString());
                if (!isPhone) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        mTelCiv.getInputEt().setFilters(new InputFilter[]{ inputFilter });
    }

    private void initRecyclerView() {
        mSelectList = new ArrayList<>();
        mGridImageAdapter = new GridImageAdapter(mSelectList, false);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getContext(),
                FullyGridLayoutManager.SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        mPhotoRv.setLayoutManager(manager);
        mPhotoRv.setAdapter(mGridImageAdapter);
        mGridImageAdapter.setOnItemChildClickListener(this);
        mGridImageAdapter.setOnItemClickListener(this);

        mVideoSelectList = new ArrayList<>();
        mVideoGridImageAdapter = new GridImageAdapter(mVideoSelectList, false);
        FullyGridLayoutManager audioManager = new FullyGridLayoutManager(getContext(),
                FullyGridLayoutManager.SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false);
        mVideoGridImageAdapter.setOnItemChildClickListener(this);
        mVideoGridImageAdapter.setOnItemClickListener(this);
        mVideoRv.setLayoutManager(audioManager);
        mVideoRv.setAdapter(mVideoGridImageAdapter);

        mAudioSelectList = new ArrayList<>();
        mAudioAdapter = new AudioAdapter(mAudioSelectList, getContext());
        mAudioAdapter.setOnRemoveAudioListener(this);
        mAudioRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAudioRv.setAdapter(mAudioAdapter);
    }

    private void initData() {
        request = new DemandCreateService.DemandCreateReq();
    }

    public void refreshUserInfo(String userInfo) {
        if (!TextUtils.isEmpty(userInfo)) {
            UserService.UserInfoBean userBean = GsonUtils.fromJson(userInfo, UserService.UserInfoBean.class);
            if (userBean != null) {
                mContactCiv.setInputText(userBean.name == null ? "" : userBean.name);
                mTelCiv.setInputText(userBean.phone == null ? "" : userBean.phone);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.civ_type) {
            startForResult(SelectDataFragment.getInstance(ISelectDataService.DATA_TYPE_DEMAND_TYPE), REQUEST_DEMAND_TYPE);
        } else if (v.getId() == R.id.iv_add_menu) {
            showBottomMenu();
        }
    }

    @Override
    public void onRightTextMenuClick(View view) {
        //验证参数
        if (validateParams()) {
            request.requester = mContactCiv.getInputText();
            request.contact = mTelCiv.getInputText();
            request.desc = mNumberView.getDesc();
            if (mSelectList.size() > 0) {
                getPresenter().uploadFile(mSelectList, ConstantMeida.IMAGE);
            } else if (mVideoSelectList.size() > 0) {
                getPresenter().uploadFile(mVideoSelectList, CommonUrl.UPLOAD_VIDEO_URL, ConstantMeida.VIDEO);
            } else if (mAudioSelectList.size() > 0) {
                getPresenter().uploadFile(mAudioSelectList, CommonUrl.UPLOAD_VOICE_URL, ConstantMeida.AUDIO);
            } else {
                showLoading();
                getPresenter().createDemand();
            }
        }
    }

    private boolean validateParams() {
        if (TextUtils.isEmpty(mContactCiv.getInputText())) {
            ToastUtils.showShort(R.string.demand_input_person);
            return false;
        }
        if (TextUtils.isEmpty(mTelCiv.getInputText())) {
            ToastUtils.showShort(R.string.demand_input_phone);
            return false;
        }
        if (TextUtils.isEmpty(mTelCiv.getInputText()) || RegexUtils.isTel(mTelCiv.getInputText())) {
            ToastUtils.showShort(R.string.demand_error_mobile_number);
            return false;
        }

        if (TextUtils.isEmpty(mTypeCiv.getTipText())) {
            ToastUtils.showShort(R.string.demand_select_demand_type);
            return false;
        }

        if (TextUtils.isEmpty(mNumberView.getDesc())) {
            ToastUtils.showShort(R.string.demand_input_desc);
            return false;
        }

        return true;
    }

    public DemandCreateService.DemandCreateReq getRequest() {
        return request;
    }

    private void showBottomMenu() {
        final int TAG_MENU_CHOOSE_PIC = 0;
        final int TAG_MENU_CAMERA_PIC = 1;
        final int TAG_MENU_VIDEO = 2;
        final int TAG_MENU_AUDIO = 3;
        FMBottomGridSheetBuilder builder = new FMBottomGridSheetBuilder(getActivity());
        builder.addItem(R.drawable.icon_more_choose_pic_selector, getString(R.string.demand_picture), TAG_MENU_CHOOSE_PIC, FMBottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_camera_pic_selector, getString(R.string.demand_take_a_picture), TAG_MENU_CAMERA_PIC, FMBottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_audio_selector, getString(R.string.demand_audio), TAG_MENU_AUDIO, FMBottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.icon_more_video_selector, getString(R.string.demand_video), TAG_MENU_VIDEO, FMBottomGridSheetBuilder.FIRST_LINE)
                .setIsShowButton(false)
                .setOnSheetItemClickListener(new FMBottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        String projectName = SPUtils.getInstance(SPKey.SP_MODEL).getString(SPKey.PROJECT_NAME, "");
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_MENU_CHOOSE_PIC:
                                PictureSelectorManager.MultipleChoose(DemandCreateFragment.this, MAX_PHOTO, mSelectList, PictureConfig.CHOOSE_REQUEST, projectName);
                                break;
                            case TAG_MENU_CAMERA_PIC:
                                if (mSelectList.size() < MAX_PHOTO) {
                                    PictureSelectorManager.camera(DemandCreateFragment.this, PictureConfig.REQUEST_CAMERA, projectName);
                                } else {
                                    ToastUtils.showShort(String.format(Locale.getDefault(), getString(R.string.demand_select_photo_at_most), MAX_PHOTO));
                                }
                                break;
                            case TAG_MENU_VIDEO:
                                PictureSelectorManager.cameraVideo(DemandCreateFragment.this, PictureSelectorManager.REQUEST_CAMERA_VIDEO);
                                break;
                            case TAG_MENU_AUDIO:
                                PermissionHelper.requestMicrophone(DemandCreateFragment.this, DemandCreateFragment.this);
                                break;
                        }
                    }
                }).build().show();
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
                    showOrHidePhoto();
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    List<LocalMedia> selectCamera = PictureSelector.obtainMultipleResult(data);
                    mSelectList.addAll(selectCamera);
                    mGridImageAdapter.replaceData(mSelectList);
                    showOrHidePhoto();
                    break;
                case PictureSelectorManager.REQUEST_CAMERA_VIDEO:
                    try {
                        String path = data.getStringExtra(Camera2Config.INTENT_PATH_SAVE_VIDEO);
                        LocalMedia media = new LocalMedia();
                        media.setPath(path);
                        String videoType = PictureMimeType.createVideoType(path);
                        media.setPictureType(videoType);
                        int duration = PictureMimeType.getLocalVideoDuration(path);
                        media.setDuration(duration);
                        mVideoSelectList.add(media);
                        mVideoGridImageAdapter.replaceData(mVideoSelectList);
                        showOrHideVideo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void showOrHidePhoto() {
        mPicTitleTv.setVisibility(mSelectList.size() == 0 ? View.GONE : View.VISIBLE);
        mPhotoRv.setVisibility(mSelectList.size() == 0 ? View.GONE : View.VISIBLE);
    }

    private void showOrHideVideo() {
        mVideoRv.setVisibility(mVideoSelectList.size() == 0 ? View.GONE : View.VISIBLE);
        mVideoTitleTv.setVisibility(mVideoSelectList.size() == 0 ? View.GONE : View.VISIBLE);
    }

    private void showOrHideAudio() {
        mAudioRv.setVisibility(mAudioSelectList.size() == 0 ? View.GONE : View.VISIBLE);
        mAudioTitleTv.setVisibility(mAudioSelectList.size() == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == mGridImageAdapter) {
            PictureSelector.create(DemandCreateFragment.this)
                    .themeStyle(R.style.picture_fm_style)
                    .openExternalPreview(position, mSelectList);
        } else {
            // 预览视频
            LocalMedia localMedia = mVideoSelectList.get(position);
            SimplePlayer.startActivity(this, localMedia.getPath());
        }
    }

    public List<LocalMedia> getVideoSelectList() {
        if (mVideoSelectList == null) {
            return new ArrayList<>();
        }
        return mVideoSelectList;
    }

    public List<LocalMedia> getAudioSelectList() {
        if (mAudioSelectList == null) {
            return new ArrayList<>();
        }
        return mAudioSelectList;
    }

    @Override
    public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
        String tip = getString(R.string.demand_sure_delete_video);
        if (adapter == mGridImageAdapter) {
            tip = getString(R.string.demand_sure_delete_photo);
        }
        new FMWarnDialogBuilder(getContext()).setIconVisible(false)
                .setSureBluBg(true)
                .setTitle(R.string.demand_remind)
                .setSure(R.string.demand_sure)
                .setTip(tip)
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
                            } else {
                                if (tempAdapter == mVideoGridImageAdapter) {
                                    path = item.getPath();
                                }
                            }
                        }

                        tempAdapter.remove(position);
                        if (tempAdapter == mGridImageAdapter) {
                            showOrHidePhoto();
                        } else {
                            showOrHideVideo();
                        }

//                        if (!"".equals(path)) {
//                            FileUtils.deleteFile(path);
//                        }
                    }
                }).create(R.style.fmDefaultWarnDialog).show();
    }

    @Override
    public void onSave(LocalMedia media) {
        mAudioSelectList.add(media);
        mAudioAdapter.notifyDataSetChanged();
        showOrHideAudio();
    }

    @Override
    public void onPermissionGranted() {
        FMBottomAudioSheetBuilder audioSheetBuilder = new FMBottomAudioSheetBuilder(getContext());
        mAudioDialog = audioSheetBuilder.build();
        mAudioDialog.show();
        audioSheetBuilder.setOnAudioFinishListener(this);
    }

    @Override
    public void onPermissionDenied() {
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if (mAudioDialog != null && mAudioDialog.isShowing()) {
            LogUtils.d("非正常录音");
            mAudioDialog.cancel();
        }

    }

    @Override
    public void onRemove(int position) {
        showOrHideAudio();
    }

    @Override
    public void onAudioClick(ImageView imageView, String path, int position) {
        if (AudioPlayManager.getAudioPlayService() != null) {
            AudioPlayManager.getAudioPlayService().setImageView(imageView);
            AudioPlayManager.getAudioPlayService().stopPlayVoiceAnimation();
            AudioPlayManager.getAudioPlayService().play(path);
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (data == null || resultCode != RESULT_OK) {
            return;
        }
        SelectDataBean bean = data.getParcelable(ISelectDataService.SELECT_OFFLINE_DATA_BACK);
        switch (requestCode) {
            case REQUEST_DEMAND_TYPE:
                if (bean == null) {
                    mTypeCiv.setTipText("");
                    request.typeId = null;
                } else {
                    mTypeCiv.setTipText(StringUtils.formatString(bean.getFullName()));
                    request.typeId = bean.getId();
                    LogUtils.d("demand type :" + request.typeId);
                }
                break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (AudioPlayManager.getAudioPlayService() != null) {
            if (AudioPlayManager.getAudioPlayService().isPlaying()) {
                AudioPlayManager.getAudioPlayService().stopPlaying();
            }
            AudioPlayManager.getAudioPlayService().stopPlayVoiceAnimation();
            AudioPlayManager.getAudioPlayService().quit();
        }
        //删除所有音频文件
        FileUtils.deleteAllInDir(FMFileUtils.getAudioPath());
        //删除所有视频文件
        FileUtils.deleteAllInDir(FMFileUtils.getVideoPath());
        //删除所有照片文件
        FileUtils.deleteAllInDir(FMFileUtils.getPicPath());
    }

    public static DemandCreateFragment getInstance() {
        return new DemandCreateFragment();
    }
}
