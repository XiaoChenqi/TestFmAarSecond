package com.facilityone.wireless.patrol.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.ec.module.ISelectDataService;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.ec.selectdata.SelectDataFragment;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.EditNumberView;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.module.PatrolTransmitService;
import com.facilityone.wireless.patrol.presenter.PatrolExceptionPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检异常选择和输入页面
 * Date: 2018/11/9 4:32 PM
 */
public class PatrolExceptionFragment extends BaseFragment<PatrolExceptionPresenter> implements View.OnClickListener {

    private LinearLayout mLlException;
    private EditNumberView mEtDesc;

    private static final String ITEM_COMMENT = "item_comment";
    private static final String ITEM_EXCEPTION = "item_exception";
    private static final int REQUEST_EXCEPTION = 60001;

    private String mComment;
    private String mException;
    private List<SelectDataBean> mSelectDataBeen;

    @Override
    public PatrolExceptionPresenter createPresenter() {
        return new PatrolExceptionPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_patrol_exception;
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
            mComment = bundle.getString(ITEM_COMMENT, "");
            mException = bundle.getString(ITEM_EXCEPTION);
        }

        if (!TextUtils.isEmpty(mException)) {
            String[] exceptions = mException.split("\\|\\|");
            if (exceptions.length == 0) {
                return;
            }
            mSelectDataBeen = new ArrayList<>();
            for (String exception : exceptions) {
                SelectDataBean e = new SelectDataBean();
                e.setId(1L);
                e.setName(exception);
                e.setHaveChild(false);
                mSelectDataBeen.add(e);
            }
        }
    }

    private void initView() {
        setTitle(R.string.patrol_comment_title);
        setRightTextButton(R.string.patrol_sure, R.id.patrol_exception_sure_id);

        mLlException = findViewById(R.id.exception_ll);
        mEtDesc = findViewById(R.id.et_record);
        mLlException.setOnClickListener(this);

        mEtDesc.setDesc(StringUtils.formatString(mComment));
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(mException)) {
            ToastUtils.showShort(R.string.patrol_faq_empty);
        } else {
            SelectDataFragment instance = SelectDataFragment.getInstance(ISelectDataService.DATA_TYPE_PATROL_EXCEPTION, (ArrayList<SelectDataBean>) mSelectDataBeen);
            startForResult(instance, REQUEST_EXCEPTION);
        }
    }

    @Override
    public void onRightTextMenuClick(View view) {
        Bundle bundle = new Bundle();
        String desc = mEtDesc.getDesc();
        bundle.putString(PatrolTransmitService.PATROL_ITEM_EXCEPTION, desc);
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        SelectDataBean bean = data.getParcelable(ISelectDataService.SELECT_OFFLINE_DATA_BACK);
        if (bean != null) {
            String name = bean.getName();
            mEtDesc.setDesc(name);
        }
    }

    public static PatrolExceptionFragment getInstance(String comment, String exception) {
        Bundle bundle = new Bundle();
        bundle.putString(ITEM_COMMENT, comment);
        bundle.putString(ITEM_EXCEPTION, exception);
        PatrolExceptionFragment instance = new PatrolExceptionFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
