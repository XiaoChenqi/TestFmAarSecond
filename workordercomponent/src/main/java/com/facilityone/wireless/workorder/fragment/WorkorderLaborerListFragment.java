package com.facilityone.wireless.workorder.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.widget.SearchBox;
import com.facilityone.wireless.basiclib.utils.PinyinUtils;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.adapter.WorkorderLaborerListAdapter;
import com.facilityone.wireless.workorder.module.WorkorderLaborerService;
import com.facilityone.wireless.workorder.presenter.WorkorderLaborerPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/7/17 下午3:00
 */
public class WorkorderLaborerListFragment extends BaseFragment<WorkorderLaborerPresenter> implements BaseQuickAdapter.OnItemClickListener, SearchBox.OnSearchBox {

    private RecyclerView mRecyclerView;
    private SearchBox mSearchBox;
    private LinearLayout mLLTitle;
    private View mViewLine;

    public static final String WORKORDER_LABORER_LIST = "workorder_laborer_list";
    private static final String TITLE_STR = "title_str";
    private static final String SHOW_TITLE = "show_title";

    private WorkorderLaborerListAdapter mAdapter;
    private List<WorkorderLaborerService.WorkorderLaborerBean> mWorkorderLaborerBeanList;
    private List<WorkorderLaborerService.WorkorderLaborerBean> mShowWorkorderLaborerBeanList;
    private boolean showTitle;
    private String mTitle;

    @Override
    public WorkorderLaborerPresenter createPresenter() {
        return new WorkorderLaborerPresenter();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_workorder_laborer_list;
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
        mShowWorkorderLaborerBeanList = new ArrayList<>();
        Bundle arguments = getArguments();
        if (arguments != null) {
            showTitle = arguments.getBoolean(SHOW_TITLE, false);
            mTitle = arguments.getString(TITLE_STR,"");
            mWorkorderLaborerBeanList = arguments.getParcelableArrayList(WORKORDER_LABORER_LIST);
        }
        if (mWorkorderLaborerBeanList != null) {
            for (int i = 0; i < mWorkorderLaborerBeanList.size(); i++) {
                WorkorderLaborerService.WorkorderLaborerBean workorderLaborerBean = mWorkorderLaborerBeanList.get(i);
                workorderLaborerBean.namePinyin = PinyinUtils.ccs2Pinyin(workorderLaborerBean.name);
                workorderLaborerBean.nameFirstLetters = PinyinUtils.getPinyinFirstLetters(workorderLaborerBean.name);
            }
            mShowWorkorderLaborerBeanList.addAll(mWorkorderLaborerBeanList);
        }
    }

    private void initView() {

        setTitle(mTitle);
        setRightTextButton(R.string.workorder_confirm, R.id.workorder_laborer_list_menu_id);

        mRecyclerView = findViewById(R.id.recyclerView);
        mSearchBox = findViewById(R.id.search_box);
        mLLTitle = findViewById(R.id.person_search_rl);
        mViewLine = findViewById(R.id.top_view_line);

        mLLTitle.setVisibility(showTitle ? View.VISIBLE : View.GONE);
        mViewLine.setVisibility(showTitle ? View.VISIBLE : View.GONE);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new WorkorderLaborerListAdapter(getContext(), showTitle, mShowWorkorderLaborerBeanList);
        mAdapter.setEmptyView(getNoDataView((ViewGroup) mRecyclerView.getParent()));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
        mSearchBox.setOnSearchBox(this);
    }

    @Override
    public void onRightTextMenuClick(View view) {
        boolean checked = false;
        for (WorkorderLaborerService.WorkorderLaborerBean workorderLaborerBean : mShowWorkorderLaborerBeanList) {
            if (workorderLaborerBean.checked) {
                checked = true;
                break;
            }
        }

        if (!checked) {
            ToastUtils.showShort(R.string.workorder_no_select);
            return;
        }
        Bundle bundle = new Bundle();
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mShowWorkorderLaborerBeanList.get(position).checked = !mShowWorkorderLaborerBeanList.get(position).checked;
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void onSearchTextChanged(String curCharacter) {
        for (WorkorderLaborerService.WorkorderLaborerBean workorderLaborerBean : mWorkorderLaborerBeanList) {
            workorderLaborerBean.start = 0;
            workorderLaborerBean.end = 0;
        }
        if (!TextUtils.isEmpty(curCharacter)) {
            curCharacter = curCharacter.replace(" ","");
            curCharacter = curCharacter.toLowerCase();
            mShowWorkorderLaborerBeanList.clear();
            if (mWorkorderLaborerBeanList != null) {
                for (WorkorderLaborerService.WorkorderLaborerBean workorderLaborerBean : mWorkorderLaborerBeanList) {
                    if (!TextUtils.isEmpty(workorderLaborerBean.name) && workorderLaborerBean.name.toLowerCase().contains(curCharacter)) {
                        mShowWorkorderLaborerBeanList.add(workorderLaborerBean);

                        int start = workorderLaborerBean.name.toLowerCase().indexOf(curCharacter);
                        int end = start + curCharacter.length();
                        workorderLaborerBean.start = start;
                        workorderLaborerBean.end = end;

                        continue;
                    }
                    if (!TextUtils.isEmpty(workorderLaborerBean.nameFirstLetters) && workorderLaborerBean.nameFirstLetters.contains(curCharacter)) {
                        mShowWorkorderLaborerBeanList.add(workorderLaborerBean);

                        int start = workorderLaborerBean.nameFirstLetters.indexOf(curCharacter);
                        int end = start + curCharacter.length();
                        workorderLaborerBean.start = start;
                        workorderLaborerBean.end = end;

                        continue;
                    }
                    if(!TextUtils.isEmpty(workorderLaborerBean.namePinyin)) {
                        String[] strArr = getPresenter().pinyinToStrArr(workorderLaborerBean.namePinyin);
                        int start = getPresenter().isMatch(strArr, curCharacter);
                        if(start != -1) {
                            mShowWorkorderLaborerBeanList.add(workorderLaborerBean);
                            workorderLaborerBean.start = start;
                            if(strArr[start].length() >= curCharacter.length()) {
                                workorderLaborerBean.end = start + 1;
                            }else {
                                workorderLaborerBean.end = getPresenter().endIndex(strArr, curCharacter.substring(strArr[start].length()), start + 1);
                            }
                            continue;
                        }
                    }
                }
            }

        } else {
            mShowWorkorderLaborerBeanList.clear();
            if (mWorkorderLaborerBeanList != null) {
                mShowWorkorderLaborerBeanList.addAll(mWorkorderLaborerBeanList);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public static WorkorderLaborerListFragment getInstance(ArrayList<WorkorderLaborerService.WorkorderLaborerBean> bs,String title) {
        return getInstance(bs, title,true);
    }

    public static WorkorderLaborerListFragment getInstance(ArrayList<WorkorderLaborerService.WorkorderLaborerBean> bs, String title,boolean showTitle) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(WORKORDER_LABORER_LIST, bs);
        bundle.putString(TITLE_STR,title);
        bundle.putBoolean(SHOW_TITLE, showTitle);
        WorkorderLaborerListFragment instance = new WorkorderLaborerListFragment();
        instance.setArguments(bundle);
        return instance;
    }
}
