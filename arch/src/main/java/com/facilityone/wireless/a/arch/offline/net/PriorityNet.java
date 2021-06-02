package com.facilityone.wireless.a.arch.offline.net;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.offline.dao.PriorityDao;
import com.facilityone.wireless.a.arch.offline.model.entity.PriorityEntity;
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
 * description:优先级网络请求
 * Date: 2018/10/11 4:53 PM
 */
public class PriorityNet {

    public static void requestPriority(Long preRequestDate, final OnDownloadListener listener) {
        String request = "{\"preRequestDate\":" + preRequestDate + "}";
        OkGo.<BaseResponse<List<PriorityEntity>>>post(FM.getApiHost() + OfflineUri.PRIORITY_URL)
                .isSpliceUrl(true)
                .upJson(request)
                .execute(new FMJsonCallback<BaseResponse<List<PriorityEntity>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<PriorityEntity>>> response) {
                        final List<PriorityEntity> data = response.body().data;
                        
                        Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                                PriorityDao priorityDao = new PriorityDao();
                                boolean add = priorityDao.addPriority(data);
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
                    public void onError(Response<BaseResponse<List<PriorityEntity>>> response) {
                        super.onError(response);
                        if (listener != null) {
                            listener.onError();
                        }
                    }
                });
    }
}
