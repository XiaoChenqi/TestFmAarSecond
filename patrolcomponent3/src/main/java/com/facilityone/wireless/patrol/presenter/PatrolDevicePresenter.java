package com.facilityone.wireless.patrol.presenter;

import com.facilityone.wireless.a.arch.mvp.BasePresenter;
import com.facilityone.wireless.a.arch.offline.dao.PatrolDeviceDao;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolEquEntity;
import com.facilityone.wireless.a.arch.offline.model.service.PatrolDbService;
import com.facilityone.wireless.patrol.fragment.PatrolDeviceFragment;

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
 * description:巡检设备
 * Date: 2018/11/8 9:42 AM
 */
public class PatrolDevicePresenter extends BasePresenter<PatrolDeviceFragment> {

    public void getDeviceList(final Long spotId) {
        Observable.create(new ObservableOnSubscribe<List<PatrolEquEntity>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<PatrolEquEntity>> emitter) throws Exception {
                PatrolDeviceDao dao = new PatrolDeviceDao();
                List<PatrolEquEntity> entities = dao.getDeviceList(spotId);
                //设置设备的检查数量
                if (entities != null && entities.size() > 0) {
                    PatrolEquEntity equEntity = entities.get(0);
                    if (PatrolDbService.COMPREHENSIVE_EQU_ID == equEntity.getEqId()) {//综合巡检
                        if (entities.size() > 1) {
                            equEntity = entities.get(1);
                        }
                    }

                    if (PatrolDbService.COMPREHENSIVE_EQU_ID != equEntity.getEqId()) {//说明存在不是综合巡检的设备
                        //查看是否需要初始化检查项
                        if (equEntity.getItemUseNumber() == 0 && equEntity.getItemStopNumber() == 0L) {//都是0则需要
                            dao.updateEquItemNumber(entities);
                            //重新获取一次
                            entities = dao.getDeviceList(spotId);
                        }
                    }
                }

                emitter.onNext(entities);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<PatrolEquEntity>>() {
                    @Override
                    public void onNext(@NonNull List<PatrolEquEntity> spotEntities) {
                        getV().refreshUI(spotEntities);
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
}
