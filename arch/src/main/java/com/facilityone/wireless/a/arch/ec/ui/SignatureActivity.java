package com.facilityone.wireless.a.arch.ec.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.base.BaseMvpActivity;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.module.CommonUrl;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.utils.FMFileUtils;
import com.facilityone.wireless.a.arch.utils.UrlUtils;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.module.DataHolder;
import com.facilityone.wireless.basiclib.utils.FMThreadUtils;
import com.facilityone.wireless.basiclib.utils.ImageLoadUtils;
import com.facilityone.wireless.basiclib.widget.HandWritePaintView;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:客户签字页面
 * Date: 2018/9/18 下午5:01
 */
public class SignatureActivity extends BaseMvpActivity implements View.OnClickListener {

    public static final String SIGN_IMAGE_ID = "sign_image_id";
    private LinearLayout mLlSignature;
    private HandWritePaintView mHpSignature;
    private ImageView mIvSignature;
    private ImageView mIvBackSignature;
    private ImageView mIvClearSignature;

    private static final String SAVE_SERVICE_URL = "save_service_url";//服务器签名地址（不同module地址不同）
    private static final String SIGNATURE_TYPE = "signature_type";//签字类型(客户，主管)
    private static final String SIGNATURE_IMAGE = "signature_image";//签过字的图片id
    private static final String ORDER_ID = "order_id";
    public static final int SIGNATURE_TYPE_CUSTOMER = 1;//客户签字
    public static final int SIGNATURE_TYPE_DIRECTOR = 2;//主管签字
    public static final int SIGNATURE_TYPE_INSPECTION = 3;//承接查验签字

    private int mSignatureType;
    private String mUrl;
    private String mImageId;
    private long mOrderId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    @Override
    public void initTopbar() {

    }

    private void initData() {
        Intent intent = getIntent();
        mImageId = intent.getStringExtra(SIGNATURE_IMAGE);
        mOrderId = intent.getLongExtra(ORDER_ID, -1L);
        mSignatureType = intent.getIntExtra(SIGNATURE_TYPE, SIGNATURE_TYPE_CUSTOMER);
        mUrl = intent.getStringExtra(SAVE_SERVICE_URL);
    }

    private void initView() {
        if (mSignatureType == SIGNATURE_TYPE_CUSTOMER) {
            setTitle(getResources().getString(R.string.arch_sing_customer_tip));
        } else if (mSignatureType == SIGNATURE_TYPE_DIRECTOR) {
            setTitle(getResources().getString(R.string.arch_sing_manager_tip));
        } else if (mSignatureType == SIGNATURE_TYPE_INSPECTION) {
            setTitle(getResources().getString(R.string.arch_sing_tip));
        }
        mLlSignature = (LinearLayout) findViewById(R.id.ll_signature);
        mHpSignature = (HandWritePaintView) findViewById(R.id.hp_signature);
        mIvSignature = (ImageView) findViewById(R.id.iv_signature);
        mIvBackSignature = (ImageView) findViewById(R.id.iv_signature_back);
        mIvClearSignature = (ImageView) findViewById(R.id.iv_signature_clear);
        mIvBackSignature.setOnClickListener(this);
        mIvClearSignature.setOnClickListener(this);
        if (TextUtils.isEmpty(mImageId)) {
            mLlSignature.setVisibility(View.VISIBLE);
            mHpSignature.setVisibility(View.VISIBLE);
            mIvSignature.setVisibility(View.GONE);
            setRightTextButton(getString(R.string.arch_save), R.id.signature_save);
        } else {
            mLlSignature.setVisibility(View.GONE);
            mHpSignature.setVisibility(View.GONE);
            mIvSignature.setVisibility(View.VISIBLE);
            String imagePath = UrlUtils.getImagePath(mImageId);
            ImageLoadUtils.loadImageView(this, imagePath, mIvSignature);
        }
    }

    @Override
    public void onRightTextMenuClick(View view) {
        if (mHpSignature.getSavePath().size()==0){
            ToastUtils.showShort(R.string.arch_sing_no_empty);
           return;
        }
        showLoading();
        FMThreadUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                final String pic = FMFileUtils.getPicPath() + "/" + System.currentTimeMillis() + ".jpg";
                Bitmap bitmap = mHpSignature.getmBitmap();
                byte[] bytes = ConvertUtils.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG);
                final boolean saveStatus = FileIOUtils.writeFileFromBytesByStream(pic, bytes);
                FM.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (saveStatus) {
                            uploadFile(pic);
                        } else {
                            dismissLoading();
                            ToastUtils.showShort(R.string.arch_operate_fail);
                        }
                    }
                });
            }
        });
    }

    /**
     * 存储签名图片到服务器
     *
     * @param pic 签名文件地址
     */
    private void uploadFile(String pic) {
        PostRequest<BaseResponse<List<String>>> request = OkGo.<BaseResponse<List<String>>>post(FM.getApiHost() + CommonUrl.UPLOAD_IMAGE_URL)
                .tag(this)
                .isSpliceUrl(true);

        final File file = new File(pic);
        if (file.exists()) {
            request.params("file-" + file.getName(), file);
        }

        request.execute(new FMJsonCallback<BaseResponse<List<String>>>() {
            @Override
            public void onSuccess(Response<BaseResponse<List<String>>> response) {
                if (mSignatureType == SIGNATURE_TYPE_INSPECTION) {
                    FileUtils.deleteFile(file);
                    DataHolder.DRAW_PATHS.addAll(mHpSignature.getSavePath());
                    DataHolder.DRAW_PATHS_DELETE.clear();
                    DataHolder.DRAW_PATHS_DELETE.addAll(mHpSignature.getSavePath());
                    dismissLoading();
                    List<String> data = response.body().data;
                    Intent intent = null;
                    if (data != null && data.size() > 0) {
                        intent = new Intent();
                        intent.putExtra(SIGN_IMAGE_ID, data.get(0));
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    saveSignature(response, file);
                }

            }

            @Override
            public void onError(Response<BaseResponse<List<String>>> response) {
                super.onError(response);
                ToastUtils.showShort(R.string.arch_operate_fail);
                dismissLoading();
            }

            @Override
            public void onFinish() {
            }
        });
    }

    private void saveSignature(Response<BaseResponse<List<String>>> response, final File file) {
        List<String> data = response.body().data;
        if (data != null && data.size() > 0) {
            String params = "{\"woId\":" + mOrderId
                    + ",\"operateType\":" + mSignatureType
                    + ",\"signImg\":" + "\"" + data.get(0) + "\""
                    + ",\"time\":" + System.currentTimeMillis()
                    + "}";
            OkGo.<BaseResponse<Object>>post(mUrl)
                    .tag(this)
                    .upJson(params)
                    .isSpliceUrl(true)
                    .execute(new FMJsonCallback<BaseResponse<Object>>() {
                        @Override
                        public void onSuccess(Response<BaseResponse<Object>> response) {
                            FileUtils.deleteFile(file);
                            setResult(RESULT_OK);
                            ToastUtils.showShort(R.string.arch_operate_success);
                            dismissLoading();
                            finish();
                        }

                        @Override
                        public void onError(Response<BaseResponse<Object>> response) {
                            super.onError(response);
                            ToastUtils.showShort(R.string.arch_operate_fail);
                            dismissLoading();
                        }
                    });
        } else {
            ToastUtils.showShort(R.string.arch_operate_fail);
            dismissLoading();
        }
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_signature;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_signature_back) {
            mHpSignature.undo();
        } else if (id == R.id.iv_signature_clear) {
            mHpSignature.removeAllPaint();
        }
    }

    public static void startActivityForResult(Activity activity
            , BaseFragment fragment
            , int requestCode
            , String url
            , int type
            , Long orderId
            , String imageId) {
        Intent intent = new Intent(activity, SignatureActivity.class);
        intent.putExtra(SAVE_SERVICE_URL, url);
        intent.putExtra(SIGNATURE_TYPE, type);
        intent.putExtra(ORDER_ID, orderId);
        intent.putExtra(SIGNATURE_IMAGE, imageId);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Activity activity
            , BaseFragment fragment
            , int requestCode
            , int type) {
        Intent intent = new Intent(activity, SignatureActivity.class);
        intent.putExtra(SIGNATURE_TYPE, type);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Activity activity
            , BaseFragment fragment
            , int requestCode
            , int type
            , String imageId) {
        Intent intent = new Intent(activity, SignatureActivity.class);
        intent.putExtra(SIGNATURE_TYPE, type);
        intent.putExtra(SIGNATURE_IMAGE, imageId);
        fragment.startActivityForResult(intent, requestCode);
    }
}
