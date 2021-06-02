package com.facilityone.wireless.componentservice.common.empty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.luojilab.component.componentlib.router.ui.UIRouter;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:其他组件单独使用的时候的初始化(登录等)
 * Date: 2018/10/15 5:23 PM
 */
public class EmptyFragment extends BaseFragment<EmptyPresenter> {

    private Button mBtn;

    private static final String MENU_TYPE = "menu_type";
    private int mType = -1;

    @Override
    public EmptyPresenter createPresenter() {
        return new EmptyPresenter(mType);
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_empty;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mType = arguments.getInt(MENU_TYPE, -1);
        }
        mBtn = findViewById(R.id.btn);

        //TODO xcq
//        showLoading();
//        getPresenter().logon("wangan", "111111");
        showLogonButton();
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO xcq

                showLoading();
                getPresenter().logon("wangan", "111111");

            }
        });
    }

    public void goFragment(Bundle bundle) {
        if (mOnGoFragmentListener != null) {
            mOnGoFragmentListener.goFragment(bundle);
        }
    }

    public void showLogonButton() {
        mBtn.setVisibility(View.VISIBLE);
    }

    public void setOnGoFragmentListener(OnGoFragmentListener onGoFragmentListener) {
        mOnGoFragmentListener = onGoFragmentListener;
    }

    private OnGoFragmentListener mOnGoFragmentListener;

    public interface OnGoFragmentListener {
        void goFragment(Bundle bundle);
    }

    public static EmptyFragment getInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(MENU_TYPE, type);
        EmptyFragment fragment = new EmptyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
