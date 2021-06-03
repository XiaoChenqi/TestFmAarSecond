package com.facilityone.wireless.patrol.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.ec.adapter.FunctionAdapter;
import com.facilityone.wireless.a.arch.ec.module.FunctionService;
import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.offline.dao.OfflineTimeDao;
import com.facilityone.wireless.a.arch.offline.model.service.OfflineService;
import com.facilityone.wireless.a.arch.offline.model.service.OnDownloadListener;
import com.facilityone.wireless.a.arch.offline.model.service.OnPatrolListener;
import com.facilityone.wireless.a.arch.offline.model.service.PatrolDbService;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.module.PatrolConstant;
import com.facilityone.wireless.patrol.presenter.PatrolMenuPresenter;
import com.joanzapata.iconify.widget.IconTextView;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检菜单
 * Date: 2018/10/30 2:53 PM
 */
public class PatrolMenuFragment extends BaseFragment<PatrolMenuPresenter> implements BaseQuickAdapter.OnItemClickListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private IconTextView mItvScan;
    private TextView mTvScanTip;

    private static final int TASK_COUNT = 4;

    private FunctionAdapter mFunctionAdapter;
    private List<FunctionService.FunctionBean> mFunctionBeanList;
    private Long mRequestTime;
    private int finishedTask;
    private boolean saveDataSuccess;
    private float mAllProgress;
    private QMUITipDialog mDialog;
    private float mTempDec;
    private float mTempBaseItem;
    private float mTempBaseSpot;
    private float mTempPatrol;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    @Override
    public PatrolMenuPresenter createPresenter() {
        return new PatrolMenuPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_patrol_menu;
    }

    @Override
    protected int setTitleBar() {
        return R.id.ui_topbar;
    }

    private void initData() {
        mFunctionBeanList = new ArrayList<>();
        Bundle arguments = getArguments();
        if (arguments != null) {
            ArrayList<FunctionService.FunctionBean> bean = (ArrayList<FunctionService.FunctionBean>) arguments.getSerializable(IService.FRAGMENT_CHILD_KEY);
            if (bean != null) {
                mFunctionBeanList.addAll(bean);
            } else {
                ToastUtils.showShort(R.string.patrol_no_function);
            }

            boolean runAlone = arguments.getBoolean(IService.COMPONENT_RUNALONE, false);
            if (runAlone) {
                setSwipeBackEnable(false);
            }
        }
//        if (mFunctionBeanList != null && mFunctionBeanList.size() > 0) {
//            mFunctionBeanList.remove(mFunctionBeanList.size() - 1);//去掉因为一行默认三个多生成的占位
//        }
    }

    private void initView() {
        setTitle(R.string.patrol_name);
        mRecyclerView = findViewById(R.id.recyclerView);
        mItvScan = findViewById(R.id.scan_patrol_itv);
        mTvScanTip = findViewById(R.id.scan_tip_tv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), FunctionService.COUNT));
//        mRecyclerView.addItemDecoration(new GridItemDecoration(getResources().getColor(R.color.grey_d6)));

        mFunctionAdapter = new FunctionAdapter(mFunctionBeanList);
        mRecyclerView.setAdapter(mFunctionAdapter);
        mFunctionAdapter.setOnItemClickListener(this);
        mItvScan.setOnClickListener(this);

        mDialog = initProgressBarLoading();
        mDialog.setCancelable(false);
    }

    private void requestProjectNeedNfc() {
        getPresenter().requestProjectNeedNfc();
    }

    public void refreshScan(boolean needNfc) {
        mItvScan.setVisibility(needNfc ? View.GONE : View.VISIBLE);
        mTvScanTip.setVisibility(needNfc ? View.GONE : View.VISIBLE);
    }

    private void requestData() {
        //此处已改判断是否正在下载 如果正在下载则不在发起新的请求 防止按home键后多次下载
        if (mAllProgress > 0) {
            return;
        }


        if (mDialog != null && !mDialog.isShowing()) {
            setTipView(getString(R.string.patrol_dowmload_progress) + "0%");
            mDialog.show();
        }
        mAllProgress = 0;
        finishedTask = 0;
        saveDataSuccess = true;
        mTempDec = 0F;
        mTempBaseItem = 0F;
        mTempBaseSpot = 0F;
        mTempPatrol = 0F;
        PatrolDbService.downloadPatrolOfflineData(new OnDownloadListener() {
            @Override
            public void onDownload(int max, int progress) {
                if(max == 0) {
                    mAllProgress += 20;
                }else {
                    mAllProgress -= (20 * mTempDec);
                    float p = ((float) progress) / max;
                    mAllProgress += (20 * p);
                    mTempDec = p;
                }
                setTipProgress();
            }

            @Override
            public void onAllSuccess() {
                finishedRequest(0L);
            }

            @Override
            public void onError() {
                saveDataSuccess = false;
            }
        }, new OnPatrolListener() {
            @Override
            public void onDownload(int max, int progress) {
                if(max == 0) {
                    mAllProgress += 20;
                }else {
                    mAllProgress -= (20 * mTempBaseSpot);
                    float p = ((float) progress) / max;
                    mAllProgress += (20 * p);
                    mTempBaseSpot = p;
                }
                setTipProgress();
            }

            @Override
            public void onAllSuccess(Long time) {
                finishedRequest(time);
            }

            @Override
            public void onError() {
                saveDataSuccess = false;
            }
        }, new OnPatrolListener() {
            @Override
            public void onDownload(int max, int progress) {
                if(max == 0) {
                    mAllProgress += 20;
                }else {
                    mAllProgress -= (20 * mTempBaseItem);
                    float p = ((float) progress) / max;
                    mAllProgress += (20 * p);
                    mTempBaseItem = p;
                }
                setTipProgress();
            }

            @Override
            public void onAllSuccess(Long time) {
                finishedRequest(time);
            }

            @Override
            public void onError() {
                saveDataSuccess = false;
            }
        }, new OnPatrolListener() {
            @Override
            public void onDownload(int max, int progress) {
                if(max == 0) {
                    mAllProgress += 40;
                }else {
                    mAllProgress -= (40 * mTempPatrol);
                    float p = ((float) progress) / max;
                    mAllProgress += (40 * p);
                    mTempPatrol = p;
                }
                setTipProgress();
            }

            @Override
            public void onAllSuccess(Long time) {
                finishedRequest(time);
            }

            @Override
            public void onError() {
                saveDataSuccess = false;
            }
        });
    }

    private void setTipProgress() {
        if (mDialog != null && mDialog.isShowing()) {
            if (mAllProgress >= 100) {
                mAllProgress = 99.99f;
            }

            float temp = ((float) Math.round(mAllProgress * 100) / 100);
            setTipView(getString(R.string.patrol_dowmload_progress) + temp + "%");
        }
    }

    private void finishedRequest(Long time) {
        if (time != 0L) {
            if (mRequestTime == null) {
                mRequestTime = time;
            } else if (mRequestTime > time) {
                mRequestTime = time;
            }
        }

        finishedTask++;
        if (finishedTask == TASK_COUNT && saveDataSuccess) {
            OfflineService.addOrUpdateDownloadTime(mRequestTime, OfflineTimeDao.TYPE_OFFLINE_PATROL);
            OfflineService.addOrUpdatePatrolDownloadTime(mRequestTime, FM.getEmId());
            LogUtils.d("巡检离线数据下载完成");
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.setTitle(getString(R.string.patrol_dowmload_progress) + "100%");
                mDialog.dismiss();
            }
            mAllProgress = 0;
            ToastUtils.showShort(R.string.patrol_offline_download_success);
        }

        if (!saveDataSuccess) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mAllProgress = 0;
        }

        LogUtils.d("下载进度:" + finishedTask + "----" + saveDataSuccess + "--" + mAllProgress + "%");
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //获取角标
        getUndoNumber();
        requestData();
        requestProjectNeedNfc();
    }

    private void getUndoNumber() {
        getPresenter().getUndoNumber(FunctionService.UNDO_TYPE_PATROL);
    }

    public List<FunctionService.FunctionBean> getFunctionBeanList() {
        if (mFunctionBeanList == null) {
            return new ArrayList<>();
        }
        return mFunctionBeanList;
    }

    public void updateFunction(List<FunctionService.FunctionBean> functionBeanList) {
        mFunctionAdapter.replaceData(functionBeanList);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        FunctionService.FunctionBean functionBean = mFunctionBeanList.get(position);
        BaseFragment baseFragment = null;
        switch (functionBean.index) {
            case PatrolConstant.PATROL_TASK:
                baseFragment = PatrolTaskFragment.getInstance();
                break;
            case PatrolConstant.PATROL_QUERY:
                baseFragment = PatrolQueryFragment.getInstance();
                break;
        }
        if (baseFragment != null) {
            start(baseFragment);
        }
    }

    @Override
    public void onClick(View v) {
        getPresenter().scan();
    }

    public void scanResult(String spotCode) {
        start(PatrolScanFragment.getInstance(spotCode));
    }

    public static PatrolMenuFragment getInstance(Bundle bundle) {
        PatrolMenuFragment menuFragment = new PatrolMenuFragment();
        menuFragment.setArguments(bundle);
        return menuFragment;
    }
}
