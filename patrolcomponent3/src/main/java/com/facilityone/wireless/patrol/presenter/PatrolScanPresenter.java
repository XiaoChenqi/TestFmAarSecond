package com.facilityone.wireless.patrol.presenter;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.mvp.BasePresenter;
import com.facilityone.wireless.a.arch.offline.dao.PatrolDeviceDao;
import com.facilityone.wireless.a.arch.offline.dao.PatrolSpotDao;
import com.facilityone.wireless.a.arch.offline.dao.PatrolTaskDao;
import com.facilityone.wireless.a.arch.offline.model.entity.DBPatrolConstant;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolSpotEntity;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolTaskEntity;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.patrol.fragment.PatrolScanFragment;
import com.facilityone.wireless.patrol.module.PatrolConstant;
import com.facilityone.wireless.patrol.module.PatrolStatusEntity;
import com.facilityone.wireless.patrol.module.PatrolStatusReq;
import com.facilityone.wireless.patrol.module.PatrolUrl;
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
 * description:巡检扫一扫 nfc
 * Date: 2018/11/16 10:19 AM
 */
public class PatrolScanPresenter extends BasePresenter<PatrolScanFragment> {

    //根据点位查询点位以及点位相关的任务
    public void getSpotList(final String code) {
        getV().showLoading();
        Observable.create(new ObservableOnSubscribe<List<PatrolSpotEntity>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<PatrolSpotEntity>> emitter) throws Exception {
                PatrolSpotDao spotDao = new PatrolSpotDao();
                List<PatrolSpotEntity> temp = spotDao.getSpotList(code);
                emitter.onNext(temp);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<PatrolSpotEntity>>() {
                    @Override
                    public void onNext(@NonNull List<PatrolSpotEntity> patrolSpotEntities) {
                        cancel();
                        getV().refreshUI(patrolSpotEntities);
                        getV().dismissLoading();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        cancel();
                        getV().error();
                        getV().dismissLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getServicePatrolTask() {
        Observable.create(new ObservableOnSubscribe<List<Long>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Long>> emitter) throws Exception {
                PatrolTaskDao dao = new PatrolTaskDao();
                List<Long> taskIds = dao.getTaskIds();
                emitter.onNext(taskIds);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<Long>>() {
                    @Override
                    public void onNext(@NonNull List<Long> ids) {
                        if (ids == null) {
                            getV().refreshUI(null);
                        } else {
                            getServicePatrolTask(ids);
                        }
                        cancel();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getV().error();
                        cancel();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getServicePatrolTask(final List<Long> ids) {
        PatrolStatusReq request = new PatrolStatusReq(ids);
        OkGo.<BaseResponse<List<PatrolStatusEntity>>>post(FM.getApiHost() + PatrolUrl.PATROL_TASK_STATUS)
                .tag(getV())
                .upJson(toJson(request))
                .isSpliceUrl(true)
                .execute(new FMJsonCallback<BaseResponse<List<PatrolStatusEntity>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<PatrolStatusEntity>>> response) {
                        updateTaskInfo(response.body().data, ids);
                    }

                    @Override
                    public void onError(Response<BaseResponse<List<PatrolStatusEntity>>> response) {
                        super.onError(response);
                        getV().getSpotList();
                    }
                });
    }

    private void updateTaskInfo(final List<PatrolStatusEntity> data, final List<Long> ids) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Exception {
                List<Long> deletedId = new ArrayList<>();
                List<PatrolTaskEntity> patrolTaskEntities = new ArrayList<>();
                List<PatrolStatusEntity.PatrolSpotsBean> patrolSpotsBeen = new ArrayList<>();
                List<PatrolStatusEntity.PatrolEquBean> patrolEquBeen = new ArrayList<>();
                if (data != null && data.size() > 0) {
                    String emName = FM.getEmName();
                    for (PatrolStatusEntity patrolStatusEntity : data) {
                        if (patrolStatusEntity.getDeleted() != null && patrolStatusEntity.getDeleted()) {
                            deletedId.add(patrolStatusEntity.getPatrolTaskId());
                            continue;
                        }

                        boolean del = true;
                        for (Long id : ids) {
                            if (patrolStatusEntity.getPatrolTaskId() != null &&
                                    id.equals(patrolStatusEntity.getPatrolTaskId())) {
                                del = false;
                                break;
                            }

                        }
                        if (del) {
                            deletedId.add(patrolStatusEntity.getPatrolTaskId());
                            continue;
                        }

                        if (patrolStatusEntity.getStatus() != null && patrolStatusEntity.getStatus() > PatrolConstant.PATROL_STATUS_ING) {
                            deletedId.add(patrolStatusEntity.getPatrolTaskId());
                            continue;
                        }

                        List<PatrolStatusEntity.PatrolSpotsBean> patrolSpots = patrolStatusEntity.getPatrolSpots();
//                        boolean taskCompleted = true;
                        if (patrolSpots != null && patrolSpots.size() > 0) {
                            for (PatrolStatusEntity.PatrolSpotsBean patrolSpot : patrolSpots) {
                                patrolSpot.setTaskId(patrolStatusEntity.getPatrolTaskId());
//                                if (patrolSpot.getFinished() != null && !patrolSpot.getFinished()) {
//                                    taskCompleted = false;
//                                }
                                if (patrolStatusEntity.getHandler() != null && patrolStatusEntity.getHandler()) {
                                    patrolSpot.setHandler(emName);
                                }
                            }
                            patrolSpotsBeen.addAll(patrolSpots);
                        }
                        PatrolTaskEntity entity = new PatrolTaskEntity();
                        entity.setTaskId(patrolStatusEntity.getPatrolTaskId());
                        if (patrolStatusEntity.getStatus() != null && patrolStatusEntity.getStatus() == PatrolConstant.PATROL_STATUS_ING) {
                            entity.setStatus(patrolStatusEntity.getStatus());
                        }
//                        entity.setCompleted(taskCompleted ? DBPatrolConstant.TRUE_VALUE : DBPatrolConstant.FALSE_VALUE);
                        patrolTaskEntities.add(entity);
                    }

                    for (PatrolStatusEntity.PatrolSpotsBean patrolSpotsBean : patrolSpotsBeen) {
                        List<PatrolStatusEntity.PatrolEquBean> equipments = patrolSpotsBean.getEquipments();
                        if (equipments != null && equipments.size() > 0) {
                            for (PatrolStatusEntity.PatrolEquBean equipment : equipments) {
                                equipment.setSpotId(patrolSpotsBean.getPatrolSpotId());
                            }
                            patrolEquBeen.addAll(equipments);
                        }
                    }
                }

                if (deletedId.size() > 0) {
                    PatrolTaskDao taskDao = new PatrolTaskDao();
                    taskDao.deleteTask(deletedId);
                }

                if (patrolTaskEntities.size() > 0) {
                    PatrolTaskDao taskDao = new PatrolTaskDao();
                    for (PatrolTaskEntity patrolTaskEntity : patrolTaskEntities) {
                        if (patrolTaskEntity.getStatus() != null) {
                            taskDao.update(patrolTaskEntity.getStatus(), patrolTaskEntity.getTaskId());
                        }
                    }
                }

                if (patrolSpotsBeen.size() > 0) {
                    PatrolSpotDao spotDao = new PatrolSpotDao();
                    for (PatrolStatusEntity.PatrolSpotsBean patrolSpotsBean : patrolSpotsBeen) {
                        if ((patrolSpotsBean.getFinished() != null && patrolSpotsBean.getFinished()) || patrolSpotsBean.getHandler() != null) {
                            spotDao.updateRemoteCompleted(patrolSpotsBean.getPatrolSpotId(), patrolSpotsBean.getHandler(), patrolSpotsBean.getFinished());
                        }
                    }
                }

                if (patrolEquBeen.size() > 0) {
                    PatrolDeviceDao deviceDao = new PatrolDeviceDao();
                    for (PatrolStatusEntity.PatrolEquBean equBean : patrolEquBeen) {
                        if (equBean.getFinished() != null && equBean.getFinished()) {
                            deviceDao.updateRemoteCompleted(DBPatrolConstant.TRUE_VALUE, equBean.getEqId(), equBean.getSpotId());
                        }
                    }
                }


                emitter.onNext(true);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean b) {
                        getV().getSpotList();
                        cancel();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        cancel();
                        getV().getSpotList();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 是否有比这个任务更早的同计划任务
     *
     * @param patrolTaskEntity
     * @param showEntities
     * @return
     */
    public boolean canGo(PatrolSpotEntity patrolTaskEntity, List<PatrolSpotEntity> showEntities) {
        for (PatrolSpotEntity showEntity : showEntities) {
            if (showEntity.getTaskPlanId().equals(patrolTaskEntity.getTaskPlanId())) {
                if (showEntity.getTaskDueStartDateTime() != null &&
                        patrolTaskEntity.getTaskDueStartDateTime() != null &&
                        showEntity.getTaskDueStartDateTime() < patrolTaskEntity.getTaskDueStartDateTime()
                        || (showEntity.getTaskDueStartDateTime() != null &&
                        patrolTaskEntity.getTaskDueStartDateTime() != null &&
                        showEntity.getTaskDueEndDateTime() != null &&
                        patrolTaskEntity.getTaskDueEndDateTime() != null &&
                        showEntity.getTaskDueStartDateTime() <= patrolTaskEntity.getTaskDueStartDateTime()
                        && showEntity.getTaskDueEndDateTime() < patrolTaskEntity.getTaskDueEndDateTime())) {
                    return false;
                }

            }
        }
        return true;
    }

    public void setTaskSpotDb(final Long taskId) {
        if (taskId == null) {
            return;
        }

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Exception {
                PatrolSpotDao dao = new PatrolSpotDao();
                List<PatrolSpotEntity> entities = dao.getSpotList(taskId);

                if (entities != null && entities.size() > 0) {
                    boolean exception = false;
                    boolean sync = false;
                    int defaultCount = 0;
                    int count = 0;
                    Long startTime = entities.get(0).getStartTime();
                    Long endTime = entities.get(0).getEndTime();

                    for (PatrolSpotEntity entity : entities) {
                        if (entity.getException() == DBPatrolConstant.TRUE_VALUE) {
                            exception = true;
                        }

                        if (entity.getCompleted() == DBPatrolConstant.TRUE_VALUE || entity.getRemoteCompleted() == DBPatrolConstant.TRUE_VALUE) {
                            count++;
                        }

                        if (entity.getNeedSync() == DBPatrolConstant.TRUE_VALUE) {
                            sync = true;
                        }

                        if (entity.getNeedSync() == DBPatrolConstant.DEFAULT_VALUE) {
                            defaultCount++;
                        }

                        if (entity.getStartTime() != 0) {
                            if (startTime == 0) {
                                startTime = entity.getStartTime();
                            } else if (startTime > entity.getStartTime()) {
                                startTime = entity.getStartTime();
                            }
                        }

                        if (entity.getEndTime() != 0) {
                            if (endTime == 0) {
                                endTime = entity.getEndTime();
                            } else if (endTime < entity.getEndTime()) {
                                endTime = entity.getEndTime();
                            }
                        }

                    }

                    PatrolTaskDao taskDao = new PatrolTaskDao();
                    int e = exception ? DBPatrolConstant.TRUE_VALUE : DBPatrolConstant.FALSE_VALUE;
                    int s = sync ? DBPatrolConstant.TRUE_VALUE : DBPatrolConstant.FALSE_VALUE;
                    s = defaultCount == entities.size() ? DBPatrolConstant.DEFAULT_VALUE : s;
                    int c = count == entities.size() ? DBPatrolConstant.TRUE_VALUE : DBPatrolConstant.FALSE_VALUE;
                    if (c != DBPatrolConstant.TRUE_VALUE) {
                        endTime = 0L;
                    }
                    Long updateTime = null;
                    if (sync) {
                        updateTime = System.currentTimeMillis();
                    }

                    taskDao.update(taskId, e, s, c, startTime, endTime, updateTime);
                }

                emitter.onNext(true);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean b) {
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
}
