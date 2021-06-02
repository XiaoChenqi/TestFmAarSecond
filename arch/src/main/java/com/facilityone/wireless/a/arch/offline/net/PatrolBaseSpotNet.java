package com.facilityone.wireless.a.arch.offline.net;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.module.Page;
import com.facilityone.wireless.a.arch.offline.dao.PatrolBaseSpotDao;
import com.facilityone.wireless.a.arch.offline.model.entity.DownloadReq;
import com.facilityone.wireless.a.arch.offline.model.entity.DownloadResp;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolBaseSpotEntity;
import com.facilityone.wireless.a.arch.offline.model.service.OnPatrolListener;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.GsonUtils;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

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
 * description: 巡检基础点位
 * Date: 2018/10/18 3:24 PM
 */
public class PatrolBaseSpotNet {

    public static void requestPatrolBaseSpot(final DownloadReq req, final OnPatrolListener listener) {
        OkGo.<BaseResponse<DownloadResp<PatrolBaseSpotEntity>>>post(FM.getApiHost() + OfflineUri.PATROL_BASE_SPOT_URL)
                .upJson(GsonUtils.toJson(req, false))
                .isSpliceUrl(true)
                .execute(new FMJsonCallback<BaseResponse<DownloadResp<PatrolBaseSpotEntity>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<DownloadResp<PatrolBaseSpotEntity>>> response) {
                        final DownloadResp<PatrolBaseSpotEntity> data = response.body().data;
                        if (listener != null && (data == null
                                || data.contents == null
                                || data.contents.size() == 0)) {
                            listener.onDownload(0, 0);
                            listener.onAllSuccess(data == null ? 0 : data.requestTime);
                            return;
                        }
                        final List<PatrolBaseSpotEntity> spotEntities = data.contents;
                        final Page page = data.page;
                        Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                                PatrolBaseSpotDao dao = new PatrolBaseSpotDao();
                                boolean add = dao.addPatrolBaseSpot(spotEntities);
                                emitter.onNext(add);
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
                                            requestPatrolBaseSpot(req, listener);
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
                    public void onError(Response<BaseResponse<DownloadResp<PatrolBaseSpotEntity>>> response) {
                        super.onError(response);
                        if (listener != null) {
                            listener.onError();
                        }
                    }
                });
    }
}
