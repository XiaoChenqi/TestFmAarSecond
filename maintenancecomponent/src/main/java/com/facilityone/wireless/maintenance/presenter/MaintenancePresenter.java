package com.facilityone.wireless.maintenance.presenter;

import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.mvp.BasePresenter;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.maintenance.R;
import com.facilityone.wireless.maintenance.fragment.MaintenanceFragment;
import com.facilityone.wireless.maintenance.model.MaintenanceConstant;
import com.facilityone.wireless.maintenance.model.MaintenanceService;
import com.facilityone.wireless.maintenance.model.MaintenanceUrl;
import com.fm.tool.network.model.BaseResponse;
import com.haibin.calendarview.Calendar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by peter.peng on 2018/11/15.
 */

public class MaintenancePresenter extends BasePresenter<MaintenanceFragment> {
    /**
     * 联网获取维护日历数据
     *
     * @param startTime
     * @param endTime
     * @param calendarSwitchStatus
     */
    public void getMaintenanceCalendarList(final long startTime, final long endTime, final int calendarSwitchStatus) {
        getMaintenanceCalendarList(startTime, endTime, calendarSwitchStatus, false);
    }

    public void getMaintenanceCalendarList(final long startTime, final long endTime, final int calendarSwitchStatus, final boolean day) {
        getV().showLoading();
        final String request = "{\"startTime\":" + startTime + ",\"endTime\":" + endTime + "}";
        String url = FM.getApiHost() + MaintenanceUrl.MAINTENANCE_CALENDAR_URL;

        //TODO xcq 拼参数

        //String url2 = url+"?app_type=android&app_version=0.0.0&current_project=10";


        OkGo.<BaseResponse<List<MaintenanceService.MaintenanceCalendarBean>>>post(url)
                .isSpliceUrl(true)
                .tag(getV())
                .upJson(request)
                .execute(new FMJsonCallback<BaseResponse<List<MaintenanceService.MaintenanceCalendarBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<MaintenanceService.MaintenanceCalendarBean>>> response) {
                        List<MaintenanceService.MaintenanceCalendarBean> data = response.body().data;
                        if (data != null) {
                            getSchemeCalendarMap(startTime, data, calendarSwitchStatus, day);

                        } else {
                            getV().getMaintenanceCalendarListError(day, calendarSwitchStatus);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<List<MaintenanceService.MaintenanceCalendarBean>>> response) {
                        super.onError(response);
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.maintenance_operate_fail);
                        getV().getMaintenanceCalendarListError(day, calendarSwitchStatus);
                    }
                });
    }

    public void getSchemeCalendarMap(final long startTime, final List<MaintenanceService.MaintenanceCalendarBean> data, final int calendarSwitchStatus, final boolean day) {
        if (data == null) {
            getV().getMaintenanceCalendarListError(day, calendarSwitchStatus);
            return;
        }
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Exception {
                Map<String, Calendar> map = null;
                if (getV().isRefreshCalendar()) {
                    Map<Integer, List<MaintenanceService.MaintenanceCalendarBean>> currentMonthData = getV().getCurrentMonthData();
                    currentMonthData.clear();
                    getV().setRefreshCalendar(false);
                    Map<Integer, Integer> tempMap = new LinkedHashMap<Integer, Integer>();
                    for (int i = 1; i <= 31; i++) {
                        tempMap.put(i, 0);
                        currentMonthData.put(i, new ArrayList<MaintenanceService.MaintenanceCalendarBean>());
                    }

                    java.util.Calendar calendarStart = java.util.Calendar.getInstance();
                    calendarStart.setTimeInMillis(startTime);
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    for (MaintenanceService.MaintenanceCalendarBean maintenanceCalendarBean : data) {
                        if (maintenanceCalendarBean.dateTodo != null) {
                            calendar.setTimeInMillis(maintenanceCalendarBean.dateTodo);
                            int curYear = calendar.get(java.util.Calendar.YEAR);
                            int curMonth = calendar.get(java.util.Calendar.MONTH);
                            int startYear = calendarStart.get(java.util.Calendar.YEAR);
                            int startMonth = calendarStart.get(java.util.Calendar.MONTH);
                            if (startYear == curYear && startMonth == curMonth) {
                                int calendarDay = calendar.get(java.util.Calendar.DAY_OF_MONTH);
                                int actualMaximum = calendarStart.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
                                if (calendarDay <= actualMaximum) {
                                    int number = tempMap.get(calendarDay);
                                    tempMap.put(calendarDay, ++number);
                                    currentMonthData.get(calendarDay).add(maintenanceCalendarBean);
                                }

                            }
                        }
                    }

                    map = new HashMap<>();
                    for (int i = 1; i <= 31; i++) {
                        if (tempMap.get(i) > 0) {
                            Calendar schemeCalendar = new Calendar();
                            schemeCalendar.setYear(calendarStart.get(java.util.Calendar.YEAR));
                            schemeCalendar.setMonth(calendarStart.get(java.util.Calendar.MONTH) + 1);
                            schemeCalendar.setDay(i);
                            schemeCalendar.setSchemeColor(ContextCompat.getColor(getV().getContext(), R.color.maintenance_scheme_text_color));//如果单独标记颜色、则会使用这个颜色
                            schemeCalendar.setScheme(tempMap.get(i) + getV().getString(R.string.maintenance_ge_task));
                            map.put(schemeCalendar.toString(), schemeCalendar);
                        }

                    }

                    getV().setmSchemeCalendarMap(map);
                }

                //-------

                List<MaintenanceService.MaintenanceCalendarBean> lastSelectDataList = getV().getLastSelectDataList();
                List<MaintenanceService.MaintenanceCalendarBean> tempDataList = null;
                switch (calendarSwitchStatus) {
                    case MaintenanceConstant.CALENDAR_STATUS_SELECT_DAY:
                        lastSelectDataList.clear();
                        lastSelectDataList.addAll(data);
                        tempDataList = lastSelectDataList;
                        break;
                    case MaintenanceConstant.CALENDAR_STATUS_SWITCH_MONTH:
                        tempDataList = data;
                        break;
                    case MaintenanceConstant.CALENDAR_STATUS_SWITCH_LAST_SELECT_DAY:
                        tempDataList = lastSelectDataList;
                        break;
                }
                if (tempDataList != null) {
                    int finished = 0;
                    int miss = 0;
                    int undo = 0;
                    int doing = 0;

                    List<MaintenanceService.MaintenanceCalendarBean> undoList = new ArrayList<>();
                    List<MaintenanceService.MaintenanceCalendarBean> doingList = new ArrayList<>();
                    List<MaintenanceService.MaintenanceCalendarBean> finishedList = new ArrayList<>();
                    List<MaintenanceService.MaintenanceCalendarBean> missList = new ArrayList<>();

                    for (int i = 0; i < tempDataList.size(); i++) {
                        MaintenanceService.MaintenanceCalendarBean maintenanceCalendarBean = tempDataList.get(i);
                        maintenanceCalendarBean.type = MaintenanceConstant.TYPE_CONTENT;
                        if (maintenanceCalendarBean.status != null) {
                            switch (maintenanceCalendarBean.status) {
                                case MaintenanceConstant.MAINTENANCE_WORKORDER_UNDO:
                                    if (undo == 0) {
                                        MaintenanceService.MaintenanceCalendarBean calendarBean = new MaintenanceService.MaintenanceCalendarBean();
                                        calendarBean.type = MaintenanceConstant.TYPE_TITLE;
                                        calendarBean.title = getV().getString(R.string.maintenance_undo);
                                        undoList.add(calendarBean);
                                    }
                                    undoList.add(maintenanceCalendarBean);
                                    undo++;
                                    break;
                                case MaintenanceConstant.MAINTENANCE_WORKORDER_DOING:
                                    if (doing == 0) {
                                        MaintenanceService.MaintenanceCalendarBean calendarBean = new MaintenanceService.MaintenanceCalendarBean();
                                        calendarBean.type = MaintenanceConstant.TYPE_TITLE;
                                        calendarBean.title = getV().getString(R.string.maintenance_doing);
                                        doingList.add(calendarBean);
                                    }
                                    doingList.add(maintenanceCalendarBean);
                                    doing++;
                                    break;
                                case MaintenanceConstant.MAINTENANCE_WORKORDER_FINISHED:
                                    if (finished == 0) {
                                        MaintenanceService.MaintenanceCalendarBean calendarBean = new MaintenanceService.MaintenanceCalendarBean();
                                        calendarBean.type = MaintenanceConstant.TYPE_TITLE;
                                        calendarBean.title = getV().getString(R.string.maintenance_finish);
                                        finishedList.add(calendarBean);
                                    }
                                    finishedList.add(maintenanceCalendarBean);
                                    finished++;
                                    break;
                                case MaintenanceConstant.MAINTENANCE_WORKORDER_MISS:
                                    if (miss == 0) {
                                        MaintenanceService.MaintenanceCalendarBean calendarBean = new MaintenanceService.MaintenanceCalendarBean();
                                        calendarBean.type = MaintenanceConstant.TYPE_TITLE;
                                        calendarBean.title = getV().getString(R.string.maintenance_leak);
                                        missList.add(calendarBean);
                                    }
                                    missList.add(maintenanceCalendarBean);
                                    miss++;
                                    break;
                            }
                        }
                    }

                    List<MaintenanceService.MaintenanceCalendarBean> listData = getV().getListData();
                    listData.clear();
                    listData.addAll(finishedList);
                    listData.addAll(missList);
                    listData.addAll(undoList);
                    listData.addAll(doingList);

                    getV().setmFinished(finished);
                    getV().setmMiss(miss);
                    getV().setmUndo(undo);
                    getV().setmDoing(doing);
                }

                emitter.onNext(new Object());
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object obj) {
                        cancel();
                        getV().refreshCalendar();
                        getV().refreshView(day, calendarSwitchStatus);
                        getV().dismissLoading();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        cancel();
                        getV().refreshCalendar();
                        getV().getMaintenanceCalendarListError(day, calendarSwitchStatus);
                        getV().dismissLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
