package com.facilityone.wireless.a.arch.offline.net;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.offline.dao.BuildingDao;
import com.facilityone.wireless.a.arch.offline.dao.CityDao;
import com.facilityone.wireless.a.arch.offline.dao.SiteDao;
import com.facilityone.wireless.a.arch.offline.model.entity.LocationBuildingEntity;
import com.facilityone.wireless.a.arch.offline.model.entity.LocationCSBResp;
import com.facilityone.wireless.a.arch.offline.model.entity.LocationCityEntity;
import com.facilityone.wireless.a.arch.offline.model.entity.LocationSiteEntity;
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
 * description:位置csb网络请求
 * Date: 2018/10/11 4:53 PM
 */
public class CitySiteBuildingNet {

    public static void requestCSB(Long preRequestDate, final OnDownloadListener listener) {
        String request = "{\"preRequestDate\":" + preRequestDate + "}";
        OkGo.<BaseResponse<LocationCSBResp>>post(FM.getApiHost() + OfflineUri.LOCATION_CSB_URL)
                .isSpliceUrl(true)
                .upJson(request)
                .execute(new FMJsonCallback<BaseResponse<LocationCSBResp>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<LocationCSBResp>> response) {
                        final LocationCSBResp data = response.body().data;
                        final List<LocationCityEntity> cities = data.getCity();
                        final List<LocationSiteEntity> sites = data.getSite();
                        final List<LocationBuildingEntity> buildings = data.getBuilding();

                        Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                                CityDao cityDao = new CityDao();
                                cityDao.addCity(cities);

                                SiteDao siteDao = new SiteDao();
                                siteDao.addSite(sites);

                                BuildingDao buildingDao = new BuildingDao();
                                boolean add = buildingDao.addSite(buildings);
                                
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
                    public void onError(Response<BaseResponse<LocationCSBResp>> response) {
                        super.onError(response);
                        if (listener != null) {
                            listener.onError();
                        }
                    }
                });
    }
}
