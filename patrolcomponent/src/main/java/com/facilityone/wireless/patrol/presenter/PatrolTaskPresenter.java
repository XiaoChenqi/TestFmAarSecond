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
import com.facilityone.wireless.patrol.fragment.PatrolTaskFragment;
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
 * description:
 * Date: 2018/11/6 9:39 AM
 */
public class PatrolTaskPresenter extends BasePresenter<PatrolTaskFragment> {

    //获取巡检任务列表
    public void getDBPatrolTask(final Long time) {
        Observable.create(new ObservableOnSubscribe<List<PatrolTaskEntity>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<PatrolTaskEntity>> emitter) throws Exception {
                PatrolTaskDao dao = new PatrolTaskDao();
                List<PatrolTaskEntity> taskEntities = dao.getTaskList(time);
                emitter.onNext(taskEntities);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<PatrolTaskEntity>>() {
                    @Override
                    public void onNext(@NonNull List<PatrolTaskEntity> taskEntities) {
                        getV().refreshUI(taskEntities);
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
                        getV().getDBPatrolTask(true);
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
                        List<Long> patrolSpotFinishId = new ArrayList<>();
                        if (patrolSpots != null && patrolSpots.size() > 0) {
                            for (PatrolStatusEntity.PatrolSpotsBean patrolSpot : patrolSpots) {
                                patrolSpot.setTaskId(patrolStatusEntity.getPatrolTaskId());
                                if (patrolSpot.getFinished() != null && patrolSpot.getFinished()) {
                                    patrolSpotFinishId.add(patrolSpot.getPatrolSpotId());
                                }
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
                        PatrolSpotDao spotDao = new PatrolSpotDao();
                        List<PatrolSpotEntity> spotList = spotDao.getSpotList(patrolStatusEntity.getPatrolTaskId());
                        if (spotList != null && patrolSpotFinishId.size() > 0 && spotList.size() > 0) {
                            int finished = 0;
                            for (PatrolSpotEntity patrolSpotEntity : spotList) {
                                if (patrolSpotEntity.getCompleted() == DBPatrolConstant.TRUE_VALUE
                                        || patrolSpotEntity.getRemoteCompleted() == DBPatrolConstant.TRUE_VALUE) {
                                    finished++;
                                    continue;
                                }
                                for (Long aLong : patrolSpotFinishId) {
                                    if (aLong != null && aLong.equals(patrolSpotEntity.getPatrolSpotId())) {
                                        finished++;
                                        break;
                                    }
                                }
                            }
                            if (finished == spotList.size()) {
                                entity.setCompleted(DBPatrolConstant.TRUE_VALUE);
                            }
                        }
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
                            taskDao.update(patrolTaskEntity.getStatus(), patrolTaskEntity.getCompleted(), patrolTaskEntity.getTaskId());
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
                        getV().getDBPatrolTask(false);
                        cancel();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        cancel();
                        getV().getDBPatrolTask(true);
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
    public boolean canGo(PatrolTaskEntity patrolTaskEntity, List<PatrolTaskEntity> showEntities) {
        for (PatrolTaskEntity showEntity : showEntities) {
            if (showEntity.getPlanId().equals(patrolTaskEntity.getPlanId())) {
                if (showEntity.getDueStartDateTime() != null &&
                        patrolTaskEntity.getDueStartDateTime() != null &&
                        showEntity.getDueStartDateTime() < patrolTaskEntity.getDueStartDateTime()
                        || (showEntity.getDueStartDateTime() != null &&
                        patrolTaskEntity.getDueStartDateTime() != null &&
                        showEntity.getDueEndDateTime() != null &&
                        patrolTaskEntity.getDueEndDateTime() != null &&
                        showEntity.getDueStartDateTime() <= patrolTaskEntity.getDueStartDateTime()
                        && showEntity.getDueEndDateTime() < patrolTaskEntity.getDueEndDateTime())) {
                    return false;
                }

            }
        }
        return true;
    }
}
