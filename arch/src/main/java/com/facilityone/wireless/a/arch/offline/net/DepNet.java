package com.facilityone.wireless.a.arch.offline.net;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.offline.dao.DepDao;
import com.facilityone.wireless.a.arch.offline.model.entity.DepEntity;
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
 * description: 部门网络请求
 * Date: 2018/10/18 3:24 PM
 */
public class DepNet {

    public static void requestDep(Long preRequestDate, final OnDownloadListener listener) {
        String request = "{\"preRequestDate\":" + preRequestDate + "}";
        OkGo.<BaseResponse<List<DepEntity>>>post(FM.getApiHost() + OfflineUri.DEP_URL)
                .upJson(request)
                .isSpliceUrl(true)
                .execute(new FMJsonCallback<BaseResponse<List<DepEntity>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<DepEntity>>> response) {
                        final List<DepEntity> data = response.body().data;

                        Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                                DepDao depDao = new DepDao();
                                boolean add = depDao.addDep(data);
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
                    public void onError(Response<BaseResponse<List<DepEntity>>> response) {
                        super.onError(response);
                        if (listener != null) {
                            listener.onError();
                        }
                    }
                });
    }
}
