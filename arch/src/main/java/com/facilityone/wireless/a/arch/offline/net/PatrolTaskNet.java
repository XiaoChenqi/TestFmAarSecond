package com.facilityone.wireless.a.arch.offline.net;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.module.Page;
import com.facilityone.wireless.a.arch.offline.dao.PatrolDeviceDao;
import com.facilityone.wireless.a.arch.offline.dao.PatrolItemDao;
import com.facilityone.wireless.a.arch.offline.dao.PatrolSpotDao;
import com.facilityone.wireless.a.arch.offline.dao.PatrolTaskDao;
import com.facilityone.wireless.a.arch.offline.model.entity.DownloadReq;
import com.facilityone.wireless.a.arch.offline.model.entity.DownloadResp;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolEquEntity;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolItemEntity;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolSpotEntity;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolTaskEntity;
import com.facilityone.wireless.a.arch.offline.model.service.OnPatrolListener;
import com.facilityone.wireless.a.arch.offline.model.service.PatrolDbService;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.GsonUtils;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

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
 * description: 巡检任务
 * Date: 2018/10/18 3:24 PM
 */
public class PatrolTaskNet {

    public static void requestPatrolTask(final DownloadReq req, final OnPatrolListener listener) {
        OkGo.<BaseResponse<DownloadResp<PatrolTaskEntity>>>post(FM.getApiHost() + OfflineUri.PATROL_TASK_URL)
                .upJson(GsonUtils.toJson(req, false))
                .isSpliceUrl(true)
                .execute(new FMJsonCallback<BaseResponse<DownloadResp<PatrolTaskEntity>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<DownloadResp<PatrolTaskEntity>>> response) {
                        final DownloadResp<PatrolTaskEntity> data = response.body().data;
                        if (listener != null && (data == null
                                || data.contents == null
                                || data.contents.size() == 0)) {
                            listener.onDownload(0, 0);
                            listener.onAllSuccess(data == null ? 0L : data.requestTime);
                            return;
                        }
                        final List<PatrolTaskEntity> taskEntityList = data.contents;
                        final Page page = data.page;
                        Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                                //拆分
                                breakUp(taskEntityList);
                                emitter.onNext(true);
                                emitter.onComplete();
                            }
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<Boolean>() {
                                    @Override
                                    public void onNext(@NonNull Boolean aBoolean) {
                                        if (listener != null) {
                                            listener.onDownload(page.getTotalPage(), page.getPageNumber() + 1);
                                        }
                                        if (page.haveNext()) {
                                            req.page.setPageNumber(page.getPageNumber() + 1);
                                            requestPatrolTask(req, listener);
                                        } else {
                                            if (listener != null) {
                                                listener.onAllSuccess(data.requestTime);
                                            }
                                        }
                                        cancel();
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        cancel();
                                    }

                                    @Override
                                    public void onComplete() {
                                    }
                                });
                    }

                    @Override
                    public void onError(Response<BaseResponse<DownloadResp<PatrolTaskEntity>>> response) {
                        super.onError(response);
                        if (listener != null) {
                            listener.onError();
                        }
                    }
                });
    }

    private static void breakUp(List<PatrolTaskEntity> taskEntityList) {
        final List<PatrolSpotEntity> patrolSpotEntities = new ArrayList<>();
        final List<PatrolEquEntity> patrolEquEntities = new ArrayList<>();
        final List<PatrolItemEntity> patrolItemEntities = new ArrayList<>();

        for (PatrolTaskEntity patrolTaskEntity : taskEntityList) {
            List<PatrolSpotEntity> spots = patrolTaskEntity.getSpots();
            int equCount = 0;
            if (spots != null) {
                for (PatrolSpotEntity spot : spots) {
                    int equNumber = spot.getEquipments() == null ? 0 : spot.getEquipments().size();
                    equCount += equNumber;
                    spot.setTaskId(patrolTaskEntity.getTaskId());
                    spot.setCompNumber(spot.getContents() == null ? 0 : spot.getContents().size());
                    spot.setEquNumber(equNumber);
                    spot.setDeleted(patrolTaskEntity.getDeleted());
                }
                patrolSpotEntities.addAll(spots);
            }
            patrolTaskEntity.setEqNumber(equCount);
        }

        for (PatrolSpotEntity patrolSpotEntity : patrolSpotEntities) {
            List<PatrolItemEntity> contents = patrolSpotEntity.getContents();
            if (contents != null && contents.size() > 0) {
                for (PatrolItemEntity content : contents) {
                    content.setTaskId(patrolSpotEntity.getTaskId());
                    content.setSpotId(patrolSpotEntity.getPatrolSpotId());
                    content.setDeleted(patrolSpotEntity.getDeleted());
                    content.setEqId(PatrolDbService.COMPREHENSIVE_EQU_ID);
                }
                PatrolEquEntity equEntity = new PatrolEquEntity();
                equEntity.setTaskId(patrolSpotEntity.getTaskId());
                equEntity.setSpotId(patrolSpotEntity.getPatrolSpotId());
                equEntity.setEqId(PatrolDbService.COMPREHENSIVE_EQU_ID);
                equEntity.setItemUseNumber(contents.size());
                equEntity.setSort(0);
                equEntity.setDeleted(patrolSpotEntity.getDeleted());
                patrolItemEntities.addAll(contents);
                patrolEquEntities.add(equEntity);
            }

            List<PatrolEquEntity> equipments = patrolSpotEntity.getEquipments();
            if (equipments != null && equipments.size() > 0) {
                for (PatrolEquEntity equEntity : equipments) {
                    equEntity.setTaskId(patrolSpotEntity.getTaskId());
                    equEntity.setSpotId(patrolSpotEntity.getPatrolSpotId());
                    equEntity.setDeleted(patrolSpotEntity.getDeleted());
                }
                patrolEquEntities.addAll(equipments);
            }
        }

        for (PatrolEquEntity patrolEquEntity : patrolEquEntities) {
            List<PatrolItemEntity> contents = patrolEquEntity.getContents();
            if (contents != null) {
                for (PatrolItemEntity content : contents) {
                    content.setTaskId(patrolEquEntity.getTaskId());
                    content.setSpotId(patrolEquEntity.getSpotId());
                    content.setEqId(patrolEquEntity.getEqId());
                    content.setDeleted(patrolEquEntity.getDeleted());
                }

                patrolItemEntities.addAll(contents);
            }
        }

        //任务
        PatrolTaskDao taskDao = new PatrolTaskDao();
        taskDao.addPatrolTask(taskEntityList);
        //点位
        PatrolSpotDao spotDao =new PatrolSpotDao();
        spotDao.addPatrolSpot(patrolSpotEntities);
        //设备
        PatrolDeviceDao deviceDao = new PatrolDeviceDao();
        deviceDao.addPatrolEqu(patrolEquEntities);
        //检查项
        PatrolItemDao itemDao = new PatrolItemDao();
        itemDao.addPatrolItem(patrolItemEntities);
    }
}
