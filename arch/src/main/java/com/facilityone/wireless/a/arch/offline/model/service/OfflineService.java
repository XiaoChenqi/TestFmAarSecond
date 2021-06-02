package com.facilityone.wireless.a.arch.offline.model.service;

import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.a.arch.offline.dao.BuildingDao;
import com.facilityone.wireless.a.arch.offline.dao.CityDao;
import com.facilityone.wireless.a.arch.offline.dao.DemandTypeDao;
import com.facilityone.wireless.a.arch.offline.dao.DepDao;
import com.facilityone.wireless.a.arch.offline.dao.EquDao;
import com.facilityone.wireless.a.arch.offline.dao.EquTypeDao;
import com.facilityone.wireless.a.arch.offline.dao.FloorDao;
import com.facilityone.wireless.a.arch.offline.dao.FlowDao;
import com.facilityone.wireless.a.arch.offline.dao.KnowledgeDao;
import com.facilityone.wireless.a.arch.offline.dao.OfflinePatrolTimeDao;
import com.facilityone.wireless.a.arch.offline.dao.OfflineTimeDao;
import com.facilityone.wireless.a.arch.offline.dao.PriorityDao;
import com.facilityone.wireless.a.arch.offline.dao.RoomDao;
import com.facilityone.wireless.a.arch.offline.dao.ServiceTypeDao;
import com.facilityone.wireless.a.arch.offline.dao.SiteDao;
import com.facilityone.wireless.a.arch.offline.model.entity.DownloadReq;
import com.facilityone.wireless.a.arch.offline.net.CitySiteBuildingNet;
import com.facilityone.wireless.a.arch.offline.net.DemandTypeNet;
import com.facilityone.wireless.a.arch.offline.net.DepNet;
import com.facilityone.wireless.a.arch.offline.net.EquNet;
import com.facilityone.wireless.a.arch.offline.net.EquTypeNet;
import com.facilityone.wireless.a.arch.offline.net.FloorNet;
import com.facilityone.wireless.a.arch.offline.net.FlowNet;
import com.facilityone.wireless.a.arch.offline.net.PriorityNet;
import com.facilityone.wireless.a.arch.offline.net.RoomNet;
import com.facilityone.wireless.a.arch.offline.net.ServiceTypeNet;
import com.tencent.wcdb.database.SQLiteDatabase;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:离线下载服务
 * Date: 2018/10/11 4:55 PM
 */
public class OfflineService {

    /**
     * 创建表
     *
     * @param db
     */
    public static void createTable(SQLiteDatabase db) {
        PriorityDao.createTable(db);
        OfflineTimeDao.createTable(db);
        DepDao.createTable(db);
        EquTypeDao.createTable(db);
        EquDao.createTable(db);
        ServiceTypeDao.createTable(db);
        DemandTypeDao.createTable(db);
        FlowDao.createTable(db);
        CityDao.createTable(db);
        SiteDao.createTable(db);
        BuildingDao.createTable(db);
        FloorDao.createTable(db);
        RoomDao.createTable(db);
        KnowledgeDao.createTable(db);
    }

    /**
     * copy数据
     *
     * @param db
     */
    public static void copyTable(SQLiteDatabase db) {
        PriorityDao.copyTable(db);
        OfflineTimeDao.copyTable(db);
        DepDao.copyTable(db);
        EquTypeDao.copyTable(db);
        EquDao.copyTable(db);
        ServiceTypeDao.copyTable(db);
        DemandTypeDao.copyTable(db);
        FlowDao.copyTable(db);
        CityDao.copyTable(db);
        SiteDao.copyTable(db);
        BuildingDao.copyTable(db);
        FloorDao.copyTable(db);
        RoomDao.copyTable(db);
        KnowledgeDao.copyTable(db);
    }

    /**
     * 删除表
     *
     * @param db
     */
    public static void deleteTable(SQLiteDatabase db) {
        PriorityDao.dropTable(db);
        OfflineTimeDao.dropTable(db);
        DepDao.dropTable(db);
        EquTypeDao.dropTable(db);
        EquDao.dropTable(db);
        ServiceTypeDao.dropTable(db);
        DemandTypeDao.dropTable(db);
        FlowDao.dropTable(db);
        CityDao.dropTable(db);
        SiteDao.dropTable(db);
        BuildingDao.dropTable(db);
        FloorDao.dropTable(db);
        RoomDao.dropTable(db);
        KnowledgeDao.dropTable(db);
    }

    /**
     * 删除临时表
     *
     * @param db
     */
    public static void deleteTempTable(SQLiteDatabase db) {
        PriorityDao.dropTempTable(db);
        OfflineTimeDao.dropTempTable(db);
        DepDao.dropTempTable(db);
        EquTypeDao.dropTempTable(db);
        EquDao.dropTempTable(db);
        ServiceTypeDao.dropTempTable(db);
        DemandTypeDao.dropTempTable(db);
        FlowDao.dropTempTable(db);
        CityDao.dropTempTable(db);
        SiteDao.dropTempTable(db);
        BuildingDao.dropTempTable(db);
        FloorDao.dropTempTable(db);
        RoomDao.dropTempTable(db);
        KnowledgeDao.dropTempTable(db);
    }

    /**
     * 重命名表名称
     *
     * @param db
     */
    public static void renameTable(SQLiteDatabase db) {
        PriorityDao.renameTable(db);
        OfflineTimeDao.renameTable(db);
        DepDao.renameTable(db);
        EquTypeDao.renameTable(db);
        EquDao.renameTable(db);
        ServiceTypeDao.renameTable(db);
        DemandTypeDao.renameTable(db);
        FlowDao.renameTable(db);
        CityDao.renameTable(db);
        SiteDao.renameTable(db);
        BuildingDao.renameTable(db);
        FloorDao.renameTable(db);
        RoomDao.renameTable(db);
        KnowledgeDao.renameTable(db);
    }

    public static void deleteAllData() {
        PriorityDao.deleteAllData();
        OfflineTimeDao.deleteAllData(OfflineTimeDao.TYPE_OFFLINE_BASE);
        OfflineTimeDao.deleteAllData(OfflineTimeDao.TYPE_KNOWLEDGE);
        DepDao.deleteAllData();
        EquTypeDao.deleteAllData();
        EquDao.deleteAllData();
        ServiceTypeDao.deleteAllData();
        DemandTypeDao.deleteAllData();
        FlowDao.deleteAllData();
        CityDao.deleteAllData();
        SiteDao.deleteAllData();
        BuildingDao.deleteAllData();
        FloorDao.deleteAllData();
        RoomDao.deleteAllData();
        KnowledgeDao.deleteAllData();
        //因为清除了设备所以巡检离线设备时间戳也要致为0
        OfflineTimeDao.deleteAllData(OfflineTimeDao.TYPE_OFFLINE_EQU);
    }

    /**
     * 更新此次请求时间戳
     *
     * @param time
     */
    public static void addOrUpdateDownloadTime(Long time, int type) {
        final OfflineTimeDao dao = new OfflineTimeDao();
        Long offlineTime = dao.getOfflineTime(type);
        if (offlineTime == 0L) {
            dao.addOfflineTime(time, type);
        } else {
            dao.updateOfflineTime(time, type);
        }
    }

    public static void addOrUpdatePatrolDownloadTime(Long time, Long emid) {
        final OfflinePatrolTimeDao dao = new OfflinePatrolTimeDao();
        final Long offlineTime = dao.getOfflineTime(emid);
        if (offlineTime == 0L) {
            dao.addOfflineTime(time, emid);
        } else {
            dao.updateOfflineTime(time, emid);
        }
    }

    public static void queryOfflineTime(DefaultObserver<Long> observer, final int type) {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {
                final OfflineTimeDao dao = new OfflineTimeDao();
                final Long offlineTime = dao.getOfflineTime(type);
                emitter.onNext(offlineTime);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void queryOfflinePatrolTime(DefaultObserver<Long> observer, final Long emid) {
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {
                final OfflinePatrolTimeDao dao = new OfflinePatrolTimeDao();
                final Long offlineTime = dao.getOfflineTime(emid);
                emitter.onNext(offlineTime);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void downloadOfflineData(
            final OnDownloadListener equListener
            , final OnDownloadListener equTypeListener
            , final OnDownloadListener csbTypeListener
            , final OnDownloadListener floorListener
            , final OnDownloadListener roomListener
            , final OnDownloadListener depListener
            , final OnDownloadListener priorityListener
            , final OnDownloadListener flowListener
            , final OnDownloadListener serviceTypeListener
            , final OnDownloadListener demandTypeListener
    ) {

        queryOfflineTime(new DefaultObserver<Long>() {
            @Override
            public void onNext(@NonNull Long aLong) {
                LogUtils.d("OfflineService onNext");

                PriorityNet.requestPriority(aLong, priorityListener);
                DepNet.requestDep(aLong, depListener);
                ServiceTypeNet.requestServiceType(aLong, serviceTypeListener);
                DemandTypeNet.requestDemandType(aLong, demandTypeListener);
                CitySiteBuildingNet.requestCSB(aLong, csbTypeListener);

                DownloadReq equTypeReq = new DownloadReq();
                equTypeReq.preRequestDate = aLong;
                EquTypeNet.requestEquType(equTypeReq, equTypeListener);

                DownloadReq flowReq = new DownloadReq();
                flowReq.preRequestDate = aLong;
                FlowNet.requestFlow(flowReq, flowListener);

                DownloadReq equReq = new DownloadReq();
                equReq.preRequestDate = aLong;
                EquNet.requestEqu(equReq, equListener, new OnDownloadListener() {
                    @Override
                    public void onDownload(int max, int progress) {
                        
                    }

                    @Override
                    public void onAllSuccess() {
                        OfflineService.addOrUpdateDownloadTime(System.currentTimeMillis(), OfflineTimeDao.TYPE_OFFLINE_EQU);
                    }

                    @Override
                    public void onError() {

                    }
                });

                DownloadReq floorReq = new DownloadReq();
                floorReq.preRequestDate = aLong;
                FloorNet.requestFloor(floorReq, floorListener);

                DownloadReq roomReq = new DownloadReq();
                roomReq.preRequestDate = aLong;
                RoomNet.requestRoom(roomReq, roomListener);

                cancel();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                LogUtils.d("OfflineService onError");
            }

            @Override
            public void onComplete() {
                LogUtils.d("OfflineService onComplete");
            }
        }, OfflineTimeDao.TYPE_OFFLINE_BASE);
    }
}
