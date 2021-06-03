package com.facilityone.wireless.workorder.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.commonpresenter.CommonBasePresenter;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.offline.dao.BuildingDao;
import com.facilityone.wireless.a.arch.offline.dao.CityDao;
import com.facilityone.wireless.a.arch.offline.dao.EquDao;
import com.facilityone.wireless.a.arch.offline.dao.FloorDao;
import com.facilityone.wireless.a.arch.offline.dao.RoomDao;
import com.facilityone.wireless.a.arch.offline.dao.SiteDao;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.componentservice.workorder.WorkorderService;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.fragment.WorkorderCreateFragment;
import com.facilityone.wireless.workorder.module.WorkorderCreateService;
import com.facilityone.wireless.workorder.module.WorkorderUrl;
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
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:创建工单
 * Date: 2018/7/4 上午9:21
 */
public class WorkorderCreatePresenter extends CommonBasePresenter<WorkorderCreateFragment> {

    @Override
    public void uploadFileSuccess(List<String> ids, int type) {
        WorkorderCreateService.WorkorderCreateReq request = getV().getRequest();
        if (request.pictures != null) {
            request.pictures.addAll(ids);
        } else {
            request.pictures = ids;
        }
    }

    @Override
    public void uploadFileFinish(int type) {
        createWorkorder(type);
    }

    public void createWorkorder(final int type) {
        WorkorderCreateService.WorkorderCreateReq request = getV().getRequest();
        OkGo.<BaseResponse<Object>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_CREATE_URL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(toJson(request))
                .execute(new FMJsonCallback<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Object>> response) {
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.workorder_submit_success);
                        if (type == WorkorderService.CREATE_ORDER_BY_OTHER
                                || type == WorkorderService.CREATE_ORDER_BY_PATROL_QUERY_REPAIR) {
                            getV().setFragmentResult(ISupportFragment.RESULT_OK, null);
                        }
                        getV().pop();
                    }

                    @Override
                    public void onError(Response<BaseResponse<Object>> response) {
                        super.onError(response);
                        getV().dismissLoading();
                        ToastUtils.showShort(R.string.workorder_submit_failed);
                    }
                });

    }

    public void getEquipmentFromDB(final long equipmentId) {
        Observable.create(new ObservableOnSubscribe<SelectDataBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<SelectDataBean> emitter) throws Exception {

                SelectDataBean selectDataBean = null;
                EquDao equDao = new EquDao();
                selectDataBean = equDao.queryEquById(equipmentId);

                if (selectDataBean != null) {
                    String tempName = "";
                    if (selectDataBean.getLocation() != null) {
                        if (selectDataBean.getLocation().roomId != null) {
                            RoomDao dao = new RoomDao();
                            tempName = dao.queryLocationName(selectDataBean.getLocation().roomId);
                        } else if (selectDataBean.getLocation().floorId != null) {
                            FloorDao dao = new FloorDao();
                            tempName = dao.queryLocationName(selectDataBean.getLocation().floorId);
                        } else if (selectDataBean.getLocation().buildingId != null) {
                            BuildingDao dao = new BuildingDao();
                            tempName = dao.queryLocationName(selectDataBean.getLocation().buildingId);
                        } else if (selectDataBean.getLocation().siteId != null) {
                            SiteDao dao = new SiteDao();
                            tempName = dao.queryLocationName(selectDataBean.getLocation().siteId);
                        } else if (selectDataBean.getLocation().cityId != null) {
                            CityDao dao = new CityDao();
                            tempName = dao.queryLocationName(selectDataBean.getLocation().cityId);
                        }
                    }
                    selectDataBean.setDesc(tempName);
                }


                emitter.onNext(selectDataBean);
                emitter.onComplete();

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<SelectDataBean>() {
                    @Override
                    public void onNext(@NonNull SelectDataBean selectDataBean) {
                        getV().refreshDevice(selectDataBean);
                        cancel();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getV().refreshDevice(null);
                        cancel();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getUserInfo() {
        getV().showLoading();
        super.getUserInfo();
    }

    @Override
    public void getUserInfoSuccess(String toJson) {
        getV().getUserInfoSuccess(toJson);
        getV().dismissLoading();
    }
}
