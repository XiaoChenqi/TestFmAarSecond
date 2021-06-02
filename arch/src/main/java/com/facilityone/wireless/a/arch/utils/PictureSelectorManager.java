package com.facilityone.wireless.a.arch.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.cktim.camera2library.Camera2Config;
import com.cktim.camera2library.camera.Camera2RecordActivity;
import com.facilityone.wireless.a.arch.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:拍照或者选择照片
 * Date: 2018/6/14 下午2:33
 */
public class PictureSelectorManager {
    public static final int REQUEST_CAMERA_VIDEO = 10001;
    //最大录制时长
    private static final int VIDEO_SECOND = 8;

    //拍照
    public static void camera(Fragment fragment, int requestCode,boolean enableCrop) {
        camera(fragment, requestCode, null,enableCrop);
    }

    public static void camera(Fragment fragment, int requestCode,String waterMark) {
        camera(fragment, requestCode, waterMark,false);
    }

    public static void camera(Fragment fragment, int requestCode) {
        camera(fragment, requestCode, null,false);
    }

    //拍照
    public static void camera(Fragment fragment, int requestCode, String waterMark,boolean enableCrop) {
        PictureSelector.create(fragment)
                .openCamera(PictureMimeType.ofImage())
                .compress(enableCrop)
                .cropCompressQuality(70)
                .waterMark(waterMark)
                .minimumCompressSize(500)// 小于500kb的图片不压缩 
                .theme(R.style.picture_fm_style)
                .compressSavePath(FMFileUtils.getPicPath())
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .enableCrop(enableCrop)//是否裁剪
                .withAspectRatio(1, 1)//裁剪比例
                .freeStyleCropEnabled(false)//裁剪框是否可拖拽
//                .circleDimmedLayer(true)//是否圆形裁剪
                .showCropFrame(false)//是否显示裁剪矩形边框
                .showCropGrid(false)//是否显示裁剪矩形网格
                .rotateEnabled(false)//裁剪是否可旋转图片
                .scaleEnabled(false)//裁剪是否可放大缩小图片
                .forResult(requestCode);
    }

    //录制视频
    public static void cameraVideo(Fragment fragment, int requestCode) {
//        PictureSelector.create(fragment)
//                .openCamera(PictureMimeType.ofVideo())
//                .videoQuality(0)
//                .setOutputCameraPath(FMFileUtils.getVideoPath())
//                .recordVideoSecond(VIDEO_SECOND)
//                .theme(R.style.picture_fm_style)
//                .forResult(requestCode);

        //配置Camera2相关参数，
        Camera2Config.RECORD_ASC = true;
        Camera2Config.RECORD_MAX_TIME = 8;
        Camera2Config.RECORD_MIN_TIME = 2;
        Camera2Config.RECORD_PROGRESS_VIEW_COLOR = R.color.colorAccent;
        Camera2Config.PREVIEW_MAX_HEIGHT = 1300;
        Camera2Config.ENABLE_CAPTURE = false;
        Camera2Config.PATH_SAVE_VIDEO = FMFileUtils.getVideoPath();

        Intent intent = new Intent(fragment.getActivity(),Camera2RecordActivity.class);
        fragment.startActivityForResult(intent,requestCode);
    }

    //单选
    public static void singleChoose(Fragment fragment, int requestCode) {
        singleChoose(fragment,requestCode,false);
    }

    public static void singleChoose(Fragment fragment, int requestCode,boolean enableCrop) {
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())
                .compress(true)
                .theme(R.style.picture_fm_style)
                .compressSavePath(FMFileUtils.getPicPath())
                .isCamera(false)
                .selectionMode(PictureConfig.SINGLE)
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .enableCrop(enableCrop)//是否裁剪
                .withAspectRatio(1, 1)//裁剪比例
                .freeStyleCropEnabled(false)//裁剪框是否可拖拽
//                .circleDimmedLayer(true)//是否圆形裁剪
                .showCropFrame(false)//是否显示裁剪矩形边框
                .showCropGrid(false)//是否显示裁剪矩形网格
                .rotateEnabled(false)//裁剪是否可旋转图片
                .scaleEnabled(false)//裁剪是否可放大缩小图片
                .forResult(requestCode);
    }

    //多选 不允许重复选择
    public static void MultipleChoose(Fragment fragment, int max, List<LocalMedia> selectList, int requestCode) {
        MultipleChoose(fragment, max, selectList, requestCode, null);
    }

    //多选 不允许重复选择
    public static void MultipleChoose(Fragment fragment, int max, List<LocalMedia> selectList, int requestCode, String waterMark) {
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())
                .compress(true)
                .isCamera(false)
                .minimumCompressSize(200)// 小于500kb的图片不压缩
                .theme(R.style.picture_fm_style)
                .compressSavePath(FMFileUtils.getPicPath())
                .maxSelectNum(max)
                .waterMark(waterMark)
                .selectionMode(PictureConfig.MULTIPLE)
                .selectionMedia(selectList)
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .forResult(requestCode);
    }

    public static void MultipleChoose(Fragment fragment, int max, int requestCode) {
        MultipleChoose(fragment, max, requestCode, null);
    }

    public static void MultipleChoose(Fragment fragment, int max, int requestCode, String waterMark) {
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())
                .compress(true)
                .isCamera(false)
                .minimumCompressSize(500)// 小于500kb的图片不压缩
                .theme(R.style.picture_fm_style)
                .compressSavePath(FMFileUtils.getPicPath())
                .maxSelectNum(max)
                .waterMark(waterMark)
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .selectionMode(PictureConfig.MULTIPLE)
                .forResult(requestCode);
    }
}
