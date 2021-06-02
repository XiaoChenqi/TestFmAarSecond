package com.facilityone.wireless.a.arch.offline.net;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.module.Page;
import com.facilityone.wireless.a.arch.offline.dao.EquDao;
import com.facilityone.wireless.a.arch.offline.model.entity.DownloadReq;
import com.facilityone.wireless.a.arch.offline.model.entity.DownloadResp;
import com.facilityone.wireless.a.arch.offline.model.entity.EquEntity;
import com.facilityone.wireless.a.arch.offline.model.service.OnDownloadListener;
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
 * description: 设备网络请求
 * Date: 2018/10/18 3:24 PM
 */
public class EquNet {

    public static void requestEqu(final DownloadReq req, final OnDownloadListener listener) {
        requestEqu(req, listener, null);
    }

    public static void requestEqu(final DownloadReq req, final OnDownloadListener listener, final OnDownloadListener listener2) {
        OkGo.<BaseResponse<DownloadResp<EquEntity>>>post(FM.getApiHost() + OfflineUri.EQU_URL)
                .upJson(GsonUtils.toJson(req, false))
                .isSpliceUrl(true)
                .execute(new FMJsonCallback<BaseResponse<DownloadResp<EquEntity>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<DownloadResp<EquEntity>>> response) {
                        final DownloadResp<EquEntity> data = response.body().data;
                        if (listener != null && (data == null
                                || data.contents == null
                                || data.contents.size() == 0)) {
                            listener.onDownload(0, 0);
                            listener.onAllSuccess();
                            return;
                        }
                        final List<EquEntity> equEntities = data.contents;
                        final Page page = data.page;
                        Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                                EquDao equDao = new EquDao();
                                boolean add = equDao.addEqu(equEntities);
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
                                            requestEqu(req, listener, listener2);
                                        } else {
                                            if (listener != null) {
                                                listener.onAllSuccess();
                                            }

                                            if (listener2 != null) {
                                                listener2.onAllSuccess();
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
                    public void onError(Response<BaseResponse<DownloadResp<EquEntity>>> response) {
                        super.onError(response);
                        if (listener != null) {
                            listener.onError();
                        }
                    }
                });
    }
}
