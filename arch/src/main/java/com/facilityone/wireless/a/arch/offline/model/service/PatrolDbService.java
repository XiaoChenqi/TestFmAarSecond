package com.facilityone.wireless.a.arch.offline.model.service;

import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.a.arch.offline.dao.OfflinePatrolTimeDao;
import com.facilityone.wireless.a.arch.offline.dao.OfflineTimeDao;
import com.facilityone.wireless.a.arch.offline.dao.PatrolBaseItemDao;
import com.facilityone.wireless.a.arch.offline.dao.PatrolBaseSpotDao;
import com.facilityone.wireless.a.arch.offline.dao.PatrolDeviceDao;
import com.facilityone.wireless.a.arch.offline.dao.PatrolItemDao;
import com.facilityone.wireless.a.arch.offline.dao.PatrolPicDao;
import com.facilityone.wireless.a.arch.offline.dao.PatrolSpotDao;
import com.facilityone.wireless.a.arch.offline.dao.PatrolTaskDao;
import com.facilityone.wireless.a.arch.offline.model.entity.DownloadReq;
import com.facilityone.wireless.a.arch.offline.net.EquNet;
import com.facilityone.wireless.a.arch.offline.net.PatrolBaseItemNet;
import com.facilityone.wireless.a.arch.offline.net.PatrolBaseSpotNet;
import com.facilityone.wireless.a.arch.offline.net.PatrolTaskNet;
import com.facilityone.wireless.basiclib.app.FM;
import com.tencent.wcdb.database.SQLiteDatabase;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DefaultObserver;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检数据库操作
 * Date: 2018/10/30 4:49 PM
 */
public class PatrolDbService {

    public static final long COMPREHENSIVE_EQU_ID = 0L;
    public static final int PATROL_ITEM_STOP = 1;//停运检查项
    public static final int PATROL_ITEM_USE = 2;//在用检查项
    public static final int PATROL_ITEM_NONE = 100;//停运和在用都需要
    // 0-输入 1-选择
    public static final int QUESTION_TYPE_INPUT = 0;
    public static final int QUESTION_TYPE_SINGLE = 1;

    /**
     * 创建表
     *
     * @param db
     */
    public static void createTable(SQLiteDatabase db) {
        PatrolBaseSpotDao.createTable(db);
        PatrolBaseItemDao.createTable(db);
        PatrolTaskDao.createTable(db);
        PatrolSpotDao.createTable(db);
        PatrolDeviceDao.createTable(db);
        PatrolItemDao.createTable(db);
        PatrolPicDao.createTable(db);
        OfflinePatrolTimeDao.createTable(db);
    }

    public static void copyTable(SQLiteDatabase db) {
        PatrolBaseSpotDao.copyTable(db);
        PatrolBaseItemDao.copyTable(db);
        PatrolTaskDao.copyTable(db);
        PatrolSpotDao.copyTable(db);
        PatrolDeviceDao.copyTable(db);
        PatrolItemDao.copyTable(db);
        PatrolPicDao.copyTable(db);
        OfflinePatrolTimeDao.copyTable(db);
    }

    /**
     * 删除表
     *
     * @param db
     */
    public static void deleteTable(SQLiteDatabase db) {
        PatrolBaseSpotDao.dropTable(db);
        PatrolBaseItemDao.dropTable(db);
        PatrolTaskDao.dropTable(db);
        PatrolSpotDao.dropTable(db);
        PatrolDeviceDao.dropTable(db);
        PatrolItemDao.dropTable(db);
        PatrolPicDao.dropTable(db);
        OfflinePatrolTimeDao.dropTable(db);
    }

    public static void deleteTempTable(SQLiteDatabase db) {
        PatrolBaseSpotDao.dropTempTable(db);
        PatrolBaseItemDao.dropTempTable(db);
        PatrolTaskDao.dropTempTable(db);
        PatrolSpotDao.dropTempTable(db);
        PatrolDeviceDao.dropTempTable(db);
        PatrolItemDao.dropTempTable(db);
        PatrolPicDao.dropTempTable(db);
        OfflinePatrolTimeDao.dropTempTable(db);
    }

    public static void renameTable(SQLiteDatabase db) {
        PatrolBaseSpotDao.renameTable(db);
        PatrolBaseItemDao.renameTable(db);
        PatrolTaskDao.renameTable(db);
        PatrolSpotDao.renameTable(db);
        PatrolDeviceDao.renameTable(db);
        PatrolItemDao.renameTable(db);
        PatrolPicDao.renameTable(db);
        OfflinePatrolTimeDao.renameTable(db);
    }

    public static void deleteAllData() {
        PatrolBaseSpotDao.deleteAllData();
        PatrolBaseItemDao.deleteAllData();
        PatrolTaskDao.deleteAllData();
        PatrolSpotDao.deleteAllData();
        PatrolDeviceDao.deleteAllData();
        PatrolItemDao.deleteAllData();
        PatrolPicDao.deleteAllData();
        OfflinePatrolTimeDao.deleteAllData();
        OfflineTimeDao.deleteAllData(OfflineTimeDao.TYPE_OFFLINE_PATROL);
    }

    public static void downloadPatrolOfflineData(
            final OnDownloadListener equListener
            , final OnPatrolListener dbSpotListener
            , final OnPatrolListener dbContentListener
            , final OnPatrolListener taskListener) {

        OfflineService.queryOfflineTime(new DefaultObserver<Long>() {
            @Override
            public void onNext(@NonNull Long aLong) {
                LogUtils.d("PatrolDbService onNext");

                DownloadReq baseSpot = new DownloadReq();
                baseSpot.preRequestDate = aLong;
                PatrolBaseSpotNet.requestPatrolBaseSpot(baseSpot, dbSpotListener);

                DownloadReq baseItem = new DownloadReq();
                baseItem.preRequestDate = aLong;
                PatrolBaseItemNet.requestPatrolBaseItem(baseItem, dbContentListener);

                cancel();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                LogUtils.d("PatrolDbService onError");
                if (dbSpotListener != null) {
                    dbSpotListener.onError();
                }
                if (dbContentListener != null) {
                    dbContentListener.onError();
                }
                if (equListener != null) {
                    equListener.onError();
                }
            }

            @Override
            public void onComplete() {
                LogUtils.d("PatrolDbService onComplete");
            }
        }, OfflineTimeDao.TYPE_OFFLINE_PATROL);

        OfflineService.queryOfflineTime(new DefaultObserver<Long>() {
            @Override
            public void onNext(@NonNull Long aLong) {
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
                cancel();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                LogUtils.d("PatrolDbService onError");
                if (equListener != null) {
                    equListener.onError();
                }
            }

            @Override
            public void onComplete() {
                LogUtils.d("PatrolDbService onComplete");
            }
        }, OfflineTimeDao.TYPE_OFFLINE_EQU);


        OfflineService.queryOfflinePatrolTime(new DefaultObserver<Long>() {
            @Override
            public void onNext(@NonNull Long aLong) {
                DownloadReq taskItem = new DownloadReq();
                taskItem.lastRequestTime = aLong;
                PatrolTaskNet.requestPatrolTask(taskItem, taskListener);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                LogUtils.d("Patrol DbService onError");
                if (taskListener != null) {
                    taskListener.onError();
                }
            }

            @Override
            public void onComplete() {
                LogUtils.d("Patrol DbService onComplete");
            }
        }, FM.getEmId());
    }
}
