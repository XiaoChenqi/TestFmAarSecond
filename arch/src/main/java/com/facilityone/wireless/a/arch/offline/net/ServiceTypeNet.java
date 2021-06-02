package com.facilityone.wireless.a.arch.offline.net;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.offline.dao.ServiceTypeDao;
import com.facilityone.wireless.a.arch.offline.model.entity.ServiceTypeEntity;
import com.facilityone.wireless.a.arch.offline.model.service.OnDownloadListener;
import com.facilityone.wireless.basiclib.app.FM;
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
 * description:服务类型网络请求
 * Date: 2018/10/11 4:53 PM
 */
public class ServiceTypeNet {

    public static void requestServiceType(Long preRequestDate, final OnDownloadListener listener) {
        String request = "{\"preRequestDate\":" + preRequestDate + "}";
        OkGo.<BaseResponse<List<ServiceTypeEntity>>>post(FM.getApiHost() + OfflineUri.SERVICE_TYPE_URL)
                .isSpliceUrl(true)
                .upJson(request)
                .execute(new FMJsonCallback<BaseResponse<List<ServiceTypeEntity>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<ServiceTypeEntity>>> response) {
                        final List<ServiceTypeEntity> data = response.body().data;

                        Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                                ServiceTypeDao serviceTypeDao = new ServiceTypeDao();
                                boolean add = serviceTypeDao.addServiceType(data);
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
                                            listener.onAllSuccess();
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
                    public void onError(Response<BaseResponse<List<ServiceTypeEntity>>> response) {
                        super.onError(response);
                        if (listener != null) {
                            listener.onError();
                        }
                    }
                });
    }
}
