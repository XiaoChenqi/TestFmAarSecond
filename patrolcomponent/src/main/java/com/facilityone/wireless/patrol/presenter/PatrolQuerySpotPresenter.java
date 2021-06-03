package com.facilityone.wireless.patrol.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.mvp.BasePresenter;
import com.facilityone.wireless.a.arch.offline.dao.EquDao;
import com.facilityone.wireless.a.arch.offline.dao.PatrolBaseSpotDao;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolSpotEntity;
import com.facilityone.wireless.a.arch.offline.model.service.PatrolDbService;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.patrol.R;
import com.facilityone.wireless.patrol.fragment.PatrolQuerySpotFragment;
import com.facilityone.wireless.patrol.module.PatrolQueryService;
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
 * description:巡检查询点位
 * Date: 2018/11/21 9:39 AM
 */
public class PatrolQuerySpotPresenter extends BasePresenter<PatrolQuerySpotFragment> {
    public void requestData(Long taskId) {
        getV().showLoading();
        String json = "{\"patrolTaskId\":" + taskId + "}";
        OkGo.<BaseResponse<PatrolQueryService.PatrolQuerySpotResp>>post(FM.getApiHost() + PatrolUrl.PATROL_TASK_QUERY_SPOT)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(json)
                .execute(new FMJsonCallback<BaseResponse<PatrolQueryService.PatrolQuerySpotResp>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<PatrolQueryService.PatrolQuerySpotResp>> response) {
                        if (response.body().data != null) {
                            getV().refreshUI(response.body().data);
                        } else {
                            getV().dismissLoading();
                            ToastUtils.showShort(R.string.patrol_get_data_error);
                            getV().pop();
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<PatrolQueryService.PatrolQuerySpotResp>> response) {
                        super.onError(response);
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.patrol_get_data_error);
                        getV().pop();
                    }
                });

    }

    public void delData(final List<PatrolQueryService.SpotsBean> spots) {
        Observable.create(new ObservableOnSubscribe<List<MultiItemEntity>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<MultiItemEntity>> emitter) throws Exception {
                List<MultiItemEntity> list = new ArrayList<>();
                EquDao equDao = new EquDao();
                PatrolBaseSpotDao spotDao = new PatrolBaseSpotDao();
                if (spots != null) {
                    for (PatrolQueryService.SpotsBean spot : spots) {
                        PatrolSpotEntity spotDb = spotDao.getSpot(spot.spotId);
                        if (spotDb != null) {
                            spot.locationName = spotDb.getLocationName();
                            spot.name = spotDb.getName();
                            spot.locationDetail = spotDb.getLocation();
                        }
                        if (spot.synthesized != null) {
                            spot.synthesized.spotId = spot.patrolTaskSpotId;
                            spot.synthesized.eqId = PatrolDbService.COMPREHENSIVE_EQU_ID;
                            spot.synthesized.name = getV().getString(R.string.patrol_task_spot_content);
                            spot.synthesized.spotLocationName = spot.locationName;
                            spot.synthesized.locationBean = spot.locationDetail;
                            spot.addSubItem(spot.synthesized);
                            if (spot.synthesized.hasOrder) {
                                spot.hasOrder = true;
                            }
                            if (spot.synthesized.hasException) {
                                spot.hasException = true;
                            }
                            if (spot.synthesized.hasLeak) {
                                spot.hasLeak = true;
                            }
                        }

                        if (spot.equipments != null) {
                            for (PatrolQueryService.EquipmentBean equipment : spot.equipments) {
                                SelectDataBean device = equDao.queryEquById(equipment.eqId);
                                equipment.name = device.getName();
                                equipment.code = device.getFullName();
                                equipment.locationBean = spot.locationDetail;
                                equipment.spotLocationName = spot.locationName;
                                equipment.spotId = spot.patrolTaskSpotId;
                                if (equipment.hasOrder) {
                                    spot.hasOrder = true;
                                }
                                if (equipment.hasException) {
                                    spot.hasException = true;
                                }
                                if (equipment.hasLeak) {
                                    spot.hasLeak = true;
                                }
                                spot.addSubItem(equipment);
                            }
                        }

                        list.add(spot);
                    }
                }

                emitter.onNext(list);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<MultiItemEntity>>() {
                    @Override
                    public void onNext(@NonNull List<MultiItemEntity> spotEntities) {
                        cancel();
                        getV().refAdapter(spotEntities);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        cancel();
                        getV().refAdapter(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
