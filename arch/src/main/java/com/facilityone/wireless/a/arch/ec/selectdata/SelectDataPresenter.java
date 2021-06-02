package com.facilityone.wireless.a.arch.ec.selectdata;

import android.text.TextUtils;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.commonpresenter.CommonBasePresenter;
import com.facilityone.wireless.a.arch.ec.module.CommonUrl;
import com.facilityone.wireless.a.arch.ec.module.ISelectDataService;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.ec.module.VisitUserBean;
import com.facilityone.wireless.a.arch.offline.dao.BuildingDao;
import com.facilityone.wireless.a.arch.offline.dao.CityDao;
import com.facilityone.wireless.a.arch.offline.dao.DemandTypeDao;
import com.facilityone.wireless.a.arch.offline.dao.DepDao;
import com.facilityone.wireless.a.arch.offline.dao.EquDao;
import com.facilityone.wireless.a.arch.offline.dao.EquTypeDao;
import com.facilityone.wireless.a.arch.offline.dao.FloorDao;
import com.facilityone.wireless.a.arch.offline.dao.KnowledgeDao;
import com.facilityone.wireless.a.arch.offline.dao.PriorityDao;
import com.facilityone.wireless.a.arch.offline.dao.RoomDao;
import com.facilityone.wireless.a.arch.offline.dao.ServiceTypeDao;
import com.facilityone.wireless.a.arch.offline.dao.SiteDao;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.PinyinUtils;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:数据选择
 * Date: 2018/10/25 11:18 AM
 */
public class SelectDataPresenter extends CommonBasePresenter<SelectDataFragment> {

    private List<SelectDataBean> allData;
    public CompositeDisposable mCompositeDisposable = new CompositeDisposable();//rxjava订阅管理器

    public void filter(int fromType, String curCharacter, List<SelectDataBean> total) {
        if (total == null || curCharacter == null) {
            return;
        }
        curCharacter = curCharacter.replace(" ", "");
        curCharacter = curCharacter.toLowerCase();
        List<SelectDataBean> temp = new ArrayList<>();
        for (SelectDataBean bean : total) {
            bean.setStart(0);
            bean.setEnd(0);
            bean.setSubStart(0);
            bean.setSubEnd(0);
            if (!TextUtils.isEmpty(bean.getName()) && bean.getName().toLowerCase().contains(curCharacter)) {
                temp.add(bean);

                int start = bean.getName().toLowerCase().indexOf(curCharacter);
                int end = start + curCharacter.length();
                bean.setStart(start);
                bean.setEnd(end);

                continue;
            }
            if (!TextUtils.isEmpty(bean.getNameFirstLetters()) && bean.getNameFirstLetters().contains(curCharacter)) {
                temp.add(bean);

                int start = bean.getNameFirstLetters().indexOf(curCharacter);
                int end = start + curCharacter.length();
                bean.setStart(start);
                bean.setEnd(end);

                continue;
            }
            if (!TextUtils.isEmpty(bean.getNamePinyin())) {
                String[] strArr = pinyinToStrArr(bean.getNamePinyin());
                int start = isMatch(strArr, curCharacter);
                if (start != -1) {
                    temp.add(bean);
                    bean.setStart(start);
                    String str = strArr[start];
                    if (str.length() >= curCharacter.length()) {
                        bean.setEnd(start + 1);
                    } else {
                        int end = endIndex(strArr, curCharacter.substring(str.length()), start + 1);
                        bean.setEnd(end);
                    }
                    continue;
                }
            }
            if (fromType == ISelectDataService.DATA_TYPE_EQU || fromType == ISelectDataService.DATA_TYPE_EQU_ALL || fromType == ISelectDataService.DATA_TYPE_LOCATION) {
                if (!TextUtils.isEmpty(bean.getFullName()) && bean.getFullName().toLowerCase().contains(curCharacter)) {
                    temp.add(bean);

                    int start = bean.getFullName().toLowerCase().indexOf(curCharacter);
                    int end = start + curCharacter.length();
                    bean.setSubStart(start);
                    bean.setSubEnd(end);

                    continue;
                }
                if (!TextUtils.isEmpty(bean.getFullNameFirstLetters()) && bean.getFullNameFirstLetters().contains(curCharacter)) {
                    temp.add(bean);

                    int start = bean.getFullNameFirstLetters().indexOf(curCharacter);
                    int end = start + curCharacter.length();
                    bean.setSubStart(start);
                    bean.setSubEnd(end);

                    continue;
                }
                if (!TextUtils.isEmpty(bean.getFullNamePinyin())) {
                    String[] strArr = pinyinToStrArr(bean.getFullNamePinyin());
                    int start = isMatch(strArr, curCharacter);
                    if (start != -1) {
                        temp.add(bean);
                        bean.setSubStart(start);
                        if (strArr[start].length() >= curCharacter.length()) {
                            bean.setSubEnd(start + 1);
                        } else {
                            int end = endIndex(strArr, curCharacter.substring(strArr[start].length()), start + 1);
                            bean.setSubEnd(end);
                        }
                        continue;
                    }
                }
            }

        }
        getV().refreshHaveData(temp, false);

    }

    //获取位置数据
    public void queryLocation(final int level, final Long parentId) {
        Observable.create(new ObservableOnSubscribe<List<SelectDataBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<SelectDataBean>> emitter) throws Exception {
                List<SelectDataBean> locationList = null;

                switch (level) {
                    case ISelectDataService.LOCATION_SITE:
                        SiteDao siteDao = new SiteDao();
                        locationList = siteDao.queryLocationSites(true);
                        break;
                    case ISelectDataService.LOCATION_BUILDING:
                        BuildingDao buildingDao = new BuildingDao();
                        locationList = buildingDao.queryLocationBuildings(parentId);
                        break;
                    case ISelectDataService.LOCATION_FLOOR:
                        FloorDao floorDao = new FloorDao();
                        locationList = floorDao.queryLocationFloors(parentId);
                        break;
                    case ISelectDataService.LOCATION_ROOM:
                        RoomDao roomDao = new RoomDao();
                        locationList = roomDao.queryLocationRooms(parentId);
                        break;
                }
                emitter.onNext(locationList);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SelectDataBean>>() {
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                        mCompositeDisposable.add(mDisposable);
                    }

                    @Override
                    public void onNext(@NonNull List<SelectDataBean> selectDataBeen) {
                        if (!mDisposable.isDisposed()) {
                            getV().setTotal(selectDataBeen);
                            getV().refreshHaveData(selectDataBeen, true);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getV().refreshHaveData(null, true);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void queryDep(final int level, final Long parentId, List<Long> parentIds) {
        if (level == 1) {
            Observable.create(new ObservableOnSubscribe<List<SelectDataBean>>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<List<SelectDataBean>> emitter) throws Exception {
                    List<SelectDataBean> temp = null;
                    DepDao depDao = new DepDao();
                    temp = depDao.queryDepartments();
                    emitter.onNext(temp);
                    emitter.onComplete();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<SelectDataBean>>() {
                        private Disposable mDisposable;

                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            mDisposable = d;
                            mCompositeDisposable.add(mDisposable);
                        }

                        @Override
                        public void onNext(@NonNull List<SelectDataBean> selectDataBeen) {
                            if (!mDisposable.isDisposed()) {
                                allData = selectDataBeen;
                                List<SelectDataBean> t = null;
                                if (selectDataBeen != null) {
                                    t = new ArrayList<>();
                                    for (SelectDataBean bean : selectDataBeen) {
                                        if (bean.getParentId() == 0L) {
                                            bean.getParentIds().add(0l);
                                            t.add(bean);
                                        }
                                    }
                                }
                                getV().setTotal(t);
                                getV().refreshHaveData(t, true);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            getV().refreshHaveData(null, true);
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } else {
            if (allData != null && allData.size() != 0) {
                List<SelectDataBean> t = new ArrayList<>();
                for (SelectDataBean bean : allData) {
                    if (bean.getParentId().equals(parentId)) {
                        if (parentIds != null) {
                            bean.getParentIds().addAll(parentIds);
                        }
                        bean.getParentIds().add(parentId);
                        t.add(bean);
                    }
                }
                getV().setTotal(t);
                getV().refreshHaveData(t, true);
            } else {
                getV().refreshHaveData(null, true);
            }
        }
    }

    public void queryServiceType(int level, Long parentId, List<Long> parentIds) {
        if (level == 1) {
            Observable.create(new ObservableOnSubscribe<List<SelectDataBean>>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<List<SelectDataBean>> emitter) throws Exception {
                    List<SelectDataBean> temp = null;
                    ServiceTypeDao serviceTypeDao = new ServiceTypeDao();
                    temp = serviceTypeDao.queryServiceType();
                    emitter.onNext(temp);
                    emitter.onComplete();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<SelectDataBean>>() {
                        private Disposable mDisposable;

                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            mDisposable = d;
                            mCompositeDisposable.add(mDisposable);
                        }

                        @Override
                        public void onNext(@NonNull List<SelectDataBean> selectDataBeen) {
                            if (!mDisposable.isDisposed()) {
                                allData = selectDataBeen;
                                List<SelectDataBean> t = null;
                                if (selectDataBeen != null) {
                                    t = new ArrayList<>();
                                    for (SelectDataBean bean : selectDataBeen) {
                                        if (bean.getParentId() == 0L) {
                                            bean.getParentIds().add(0l);
                                            t.add(bean);
                                        }
                                    }
                                }
                                getV().setTotal(t);
                                getV().refreshHaveData(t, true);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            getV().refreshHaveData(null, true);
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } else {
            if (allData != null && allData.size() != 0) {
                List<SelectDataBean> t = new ArrayList<>();
                for (SelectDataBean bean : allData) {
                    if (bean.getParentId().equals(parentId)) {
                        if (parentIds != null) {
                            bean.getParentIds().addAll(parentIds);
                        }
                        bean.getParentIds().add(parentId);
                        t.add(bean);
                    }
                }
                getV().setTotal(t);
                getV().refreshHaveData(t, true);
            } else {
                getV().refreshHaveData(null, true);
            }
        }
    }

    public void queryFlowPriority(final SelectDataBean depSelectData, final SelectDataBean serviceTypeSelectData, final Long woTypeId, final LocationBean locationBean) {
        Observable.create(new ObservableOnSubscribe<List<SelectDataBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<SelectDataBean>> emitter) throws Exception {
                List<SelectDataBean> temp = null;
                PriorityDao priorityDao = new PriorityDao();
                temp = priorityDao.queryFlowPriority(depSelectData, serviceTypeSelectData, woTypeId, locationBean);
                emitter.onNext(temp);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SelectDataBean>>() {
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                        mCompositeDisposable.add(mDisposable);
                    }

                    @Override
                    public void onNext(@NonNull List<SelectDataBean> selectDataBeen) {
                        if (!mDisposable.isDisposed()) {
                            getV().setTotal(selectDataBeen);
                            getV().refreshHaveData(selectDataBeen, true);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getV().refreshHaveData(null, true);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public void queryPriority() {
        Observable.create(new ObservableOnSubscribe<List<SelectDataBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<SelectDataBean>> emitter) throws Exception {
                List<SelectDataBean> temp = null;
                PriorityDao priorityDao = new PriorityDao();
                temp = priorityDao.queryPriority();
                emitter.onNext(temp);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SelectDataBean>>() {
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                        mCompositeDisposable.add(mDisposable);
                    }

                    @Override
                    public void onNext(@NonNull List<SelectDataBean> selectDataBeen) {
                        if (!mDisposable.isDisposed()) {
                            getV().setTotal(selectDataBeen);
                            getV().refreshHaveData(selectDataBeen, true);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getV().refreshHaveData(null, true);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void queryEqu(final LocationBean location, final String locationName) {
        Observable.create(new ObservableOnSubscribe<List<SelectDataBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<SelectDataBean>> emitter) throws Exception {
                List<SelectDataBean> temp = null;
                EquDao equDao = new EquDao();
                temp = equDao.queryEqus(location);

                for (SelectDataBean bean : temp) {
                    String tempName = locationName;
                    if (bean.getLocation() != null) {
                        if (bean.getLocation().roomId != null && bean.getLocation().roomId != 0L) {
                            RoomDao dao = new RoomDao();
                            tempName = dao.queryLocationName(bean.getLocation().roomId);
                        } else if (bean.getLocation().floorId != null && bean.getLocation().floorId != 0L) {
                            FloorDao dao = new FloorDao();
                            tempName = dao.queryLocationName(bean.getLocation().floorId);
                        } else if (bean.getLocation().buildingId != null && bean.getLocation().buildingId != 0L) {
                            BuildingDao dao = new BuildingDao();
                            tempName = dao.queryLocationName(bean.getLocation().buildingId);
                        } else if (bean.getLocation().siteId != null && bean.getLocation().siteId != 0L) {
                            SiteDao dao = new SiteDao();
                            tempName = dao.queryLocationName(bean.getLocation().siteId);
                        } else if (bean.getLocation().cityId != null && bean.getLocation().cityId != 0L) {
                            CityDao dao = new CityDao();
                            tempName = dao.queryLocationName(bean.getLocation().cityId);
                        }
                    }
                    bean.setDesc(tempName);
                }

                emitter.onNext(temp);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SelectDataBean>>() {
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                        mCompositeDisposable.add(mDisposable);
                    }

                    @Override
                    public void onNext(@NonNull List<SelectDataBean> selectDataBeen) {
                        if (!mDisposable.isDisposed()) {
                            getV().setTotal(selectDataBeen);
                            getV().refreshHaveData(selectDataBeen, true);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getV().refreshHaveData(null, true);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void queryEqu() {
        Observable.create(new ObservableOnSubscribe<List<SelectDataBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<SelectDataBean>> emitter) throws Exception {
                List<SelectDataBean> temp = null;
                EquDao equDao = new EquDao();
                temp = equDao.queryEqus();
                emitter.onNext(temp);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SelectDataBean>>() {
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                        mCompositeDisposable.add(mDisposable);
                    }

                    @Override
                    public void onNext(@NonNull List<SelectDataBean> selectDataBeen) {
                        if (!mDisposable.isDisposed()) {
                            getV().setTotal(selectDataBeen);
                            getV().refreshHaveData(selectDataBeen, true);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getV().refreshHaveData(null, true);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void queryDemandType(int level, Long parentId) {
        if (level == 1) {
            Observable.create(new ObservableOnSubscribe<List<SelectDataBean>>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<List<SelectDataBean>> emitter) throws Exception {
                    List<SelectDataBean> temp = null;
                    DemandTypeDao demandTypeDao = new DemandTypeDao();
                    temp = demandTypeDao.queryDemandType();
                    emitter.onNext(temp);
                    emitter.onComplete();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<SelectDataBean>>() {
                        private Disposable mDisposable;

                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            mDisposable = d;
                            mCompositeDisposable.add(mDisposable);
                        }

                        @Override
                        public void onNext(@NonNull List<SelectDataBean> selectDataBeen) {
                            if (!mDisposable.isDisposed()) {
                                allData = selectDataBeen;
                                List<SelectDataBean> t = null;
                                if (selectDataBeen != null) {
                                    t = new ArrayList<>();
                                    for (SelectDataBean bean : selectDataBeen) {
                                        if (bean.getParentId() == 0L) {
                                            t.add(bean);
                                        }
                                    }
                                }
                                getV().setTotal(t);
                                getV().refreshHaveData(t, true);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            getV().refreshHaveData(null, true);
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } else {
            if (allData != null && allData.size() != 0) {
                List<SelectDataBean> t = new ArrayList<>();
                for (SelectDataBean bean : allData) {
                    if (bean.getParentId().equals(parentId)) {
                        t.add(bean);
                    }
                }
                getV().setTotal(t);
                getV().refreshHaveData(t, true);
            } else {
                getV().refreshHaveData(null, true);
            }
        }
    }

    public void queryEquType(int level, Long parentId) {
        if (level == 1) {
            Observable.create(new ObservableOnSubscribe<List<SelectDataBean>>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<List<SelectDataBean>> emitter) throws Exception {
                    List<SelectDataBean> temp = null;
                    EquTypeDao equTypeDao = new EquTypeDao();
                    temp = equTypeDao.queryEquType();
                    emitter.onNext(temp);
                    emitter.onComplete();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<SelectDataBean>>() {
                        private Disposable mDisposable;

                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            mDisposable = d;
                            mCompositeDisposable.add(mDisposable);
                        }

                        @Override
                        public void onNext(@NonNull List<SelectDataBean> selectDataBeen) {
                            if (!mDisposable.isDisposed()) {
                                allData = selectDataBeen;
                                List<SelectDataBean> t = null;
                                if (selectDataBeen != null) {
                                    t = new ArrayList<>();
                                    for (SelectDataBean bean : selectDataBeen) {
                                        if (bean.getParentId() == 0L) {
                                            t.add(bean);
                                        }
                                    }
                                }
                                getV().setTotal(t);
                                getV().refreshHaveData(t, true);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            getV().refreshHaveData(null, true);
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } else {
            if (allData != null && allData.size() != 0) {
                List<SelectDataBean> t = new ArrayList<>();
                for (SelectDataBean bean : allData) {
                    if (bean.getParentId().equals(parentId)) {
                        t.add(bean);
                    }
                }
                getV().setTotal(t);
                getV().refreshHaveData(t, true);
            } else {
                getV().refreshHaveData(null, true);
            }
        }
    }

    public void queryKnowledgeType(int level, final Long parentId) {
        if (level == 1) {
            Observable.create(new ObservableOnSubscribe<List<SelectDataBean>>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<List<SelectDataBean>> emitter) throws Exception {
                    List<SelectDataBean> temp = null;
                    KnowledgeDao serviceTypeDao = new KnowledgeDao();
                    temp = serviceTypeDao.queryknowledgeType();
                    emitter.onNext(temp);
                    emitter.onComplete();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<SelectDataBean>>() {
                        private Disposable mDisposable;

                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            mDisposable = d;
                            mCompositeDisposable.add(mDisposable);
                        }

                        @Override
                        public void onNext(@NonNull List<SelectDataBean> selectDataBeen) {
                            if (!mDisposable.isDisposed()) {
                                allData = selectDataBeen;
                                List<SelectDataBean> t = null;
                                if (selectDataBeen != null) {
                                    t = new ArrayList<>();
                                    for (SelectDataBean bean : selectDataBeen) {
                                        if (bean.getParentId().equals(parentId)) {
                                            t.add(bean);
                                        }
                                    }
                                }
                                getV().setTotal(t);
                                getV().refreshHaveData(t, true);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            getV().refreshHaveData(null, true);
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } else {
            if (allData != null && allData.size() != 0) {
                List<SelectDataBean> t = new ArrayList<>();
                for (SelectDataBean bean : allData) {
                    if (bean.getParentId().equals(parentId)) {
                        t.add(bean);
                    }
                }
                getV().setTotal(t);
                getV().refreshHaveData(t, true);
            } else {
                getV().refreshHaveData(null, true);
            }
        }
    }

    /**
     * 查询所有位置信息
     */
    public void queryAllLocation(final boolean showSite) {
        Observable.create(new ObservableOnSubscribe<List<SelectDataBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<SelectDataBean>> emitter) throws Exception {
                SiteDao siteDao = new SiteDao();
                BuildingDao buildingDao = new BuildingDao();
                FloorDao floorDao = new FloorDao();
                RoomDao roomDao = new RoomDao();
                List<SelectDataBean> location = new ArrayList<>();
                List<SelectDataBean> sites = siteDao.queryAllLocationSites();
                if (showSite) {
                    if (sites != null) {
                        for (SelectDataBean site : sites) {
                            LocationBean locationBean = new LocationBean();
                            locationBean.siteId = site.getId();
                            site.setLocation(locationBean);
                        }
                        location.addAll(sites);
                    }
                }

                List<SelectDataBean> buildings = buildingDao.queryAllLocationBuildings();
                if (buildings != null) {
                    for (SelectDataBean building : buildings) {
                        Long parentId = building.getParentId();
                        LocationBean locationBean = new LocationBean();
                        locationBean.siteId = parentId;
                        locationBean.buildingId = building.getId();
                        building.setLocation(locationBean);
                    }
                    location.addAll(buildings);
                }
                List<SelectDataBean> floors = floorDao.queryAllLocationFloors();
                if (floors != null) {
                    for (SelectDataBean floor : floors) {
                        Long parentId = floor.getParentId();
                        if (parentId != null && buildings != null) {
                            for (SelectDataBean building : buildings) {
                                if (parentId.equals(building.getId())) {
                                    LocationBean locationBean = new LocationBean();
                                    LocationBean buildingLocationBean = building.getLocation();
                                    locationBean.siteId = buildingLocationBean.siteId;
                                    locationBean.buildingId = buildingLocationBean.buildingId;
                                    locationBean.floorId = floor.getId();
                                    floor.setLocation(locationBean);
                                    break;
                                }
                            }
                        }
                    }
                    location.addAll(floors);
                }

                List<SelectDataBean> rooms = roomDao.queryAllLocationRooms();
                if (rooms != null) {
                    for (SelectDataBean room : rooms) {
                        Long parentId = room.getParentId();
                        if (parentId != null && floors != null) {
                            for (SelectDataBean floor : floors) {
                                if (parentId.equals(floor.getId())) {
                                    LocationBean locationBean = new LocationBean();
                                    LocationBean floorLocationBean = floor.getLocation();
                                    locationBean.siteId = floorLocationBean.siteId;
                                    locationBean.buildingId = floorLocationBean.buildingId;
                                    locationBean.floorId = floorLocationBean.floorId;
                                    locationBean.roomId = room.getId();
                                    room.setLocation(locationBean);
                                    break;
                                }
                            }
                        }
                    }
                    location.addAll(rooms);
                }
                emitter.onNext(location);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SelectDataBean>>() {
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                        mCompositeDisposable.add(mDisposable);
                    }

                    @Override
                    public void onNext(@NonNull List<SelectDataBean> selectDataBeen) {
                        if (!mDisposable.isDisposed()) {
                            getV().setTotalLocation(selectDataBeen);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getV().setTotalLocation(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getVisitPay() {
        getV().showLoading();
        OkGo.<BaseResponse<List<VisitUserBean>>>post(FM.getApiHost() + CommonUrl.USER_LIST)
                .isSpliceUrl(true)
                .tag(getV())
                .upJson("{}")
                .execute(new FMJsonCallback<BaseResponse<List<VisitUserBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<VisitUserBean>>> response) {
                        //转换
                        List<VisitUserBean> data = response.body().data;
                        if (data == null || data.size() == 0) {
                            getV().refreshHaveData(null, false);
                        } else {
                            List<SelectDataBean> selectDataBeans = new ArrayList<>();
                            for (VisitUserBean datum : data) {
                                SelectDataBean s = new SelectDataBean();
                                s.setId(datum.emId);
                                s.setName(datum.name);
                                s.setFullName(datum.name);
                                s.setLocation(datum.location);
                                s.setStandbyId(datum.orgId);
                                s.setStandbyName(datum.orgName);
                                s.setDesc(datum.phone);
                                s.setLocationName(datum.locationName);
                                s.setNamePinyin(PinyinUtils.ccs2Pinyin(s.getName()));
                                s.setNameFirstLetters(PinyinUtils.getPinyinFirstLetters(s.getName()));

                                selectDataBeans.add(s);
                            }
                            getV().setTotal(selectDataBeans);
                            getV().refreshHaveData(selectDataBeans, false);
                        }
                        getV().dismissLoading();
                    }

                    @Override
                    public void onError(Response<BaseResponse<List<VisitUserBean>>> response) {
                        super.onError(response);
                        getV().refreshHaveData(null, false);
                        getV().dismissLoading();
                    }
                });
    }
}