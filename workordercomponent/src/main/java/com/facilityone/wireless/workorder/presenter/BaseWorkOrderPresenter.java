package com.facilityone.wireless.workorder.presenter;

import com.facilityone.wireless.a.arch.base.FMJsonCallback;
import com.facilityone.wireless.a.arch.ec.commonpresenter.CommonBasePresenter;
import com.facilityone.wireless.a.arch.ec.module.AttachmentBean;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.mvp.BaseFragment;
import com.facilityone.wireless.a.arch.offline.dao.PriorityDao;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.workorder.R;
import com.facilityone.wireless.workorder.module.WorkorderService;
import com.facilityone.wireless.workorder.module.WorkorderUrl;
import com.fm.tool.network.model.BaseResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Date: 2019/3/1 3:47 PM
 */
public class BaseWorkOrderPresenter<V extends BaseFragment> extends CommonBasePresenter<V> {

    public void queryPriority() {
        queryPriority(false);
    }

    public void queryPriority(final boolean needFilterDelete) {
        Observable.create(new ObservableOnSubscribe<List<SelectDataBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<SelectDataBean>> emitter) throws Exception {
                List<SelectDataBean> temp = null;
                PriorityDao priorityDao = new PriorityDao();
                temp = priorityDao.queryPriority(needFilterDelete);
                emitter.onNext(temp);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<SelectDataBean>>() {
                    @Override
                    public void onNext(@NonNull List<SelectDataBean> temp) {
                        if (temp != null && temp.size() > 0) {
                            Map<Long, String> p = new HashMap<>();
                            for (SelectDataBean selectDataBean : temp) {
                                Long id = selectDataBean.getId();
                                String name = selectDataBean.getName();
                                if (id != null) {
                                    p.put(id, name);
                                }
                            }
                            setPriority(p);
                        }
                        List<AttachmentBean> as = new ArrayList<>();
                        AttachmentBean bb = new AttachmentBean();
                        bb.value = -1L;
                        bb.name = getV().getString(R.string.workorder_unlimited);
                        bb.check = true;
                        as.add(bb);
                        if (temp != null) {
                            for (SelectDataBean bean : temp) {
                                AttachmentBean b = new AttachmentBean();
                                b.value = bean.getId();
                                b.name = bean.getName();
                                as.add(b);
                            }
                        }
                        getPriority(as);
                        cancel();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        List<AttachmentBean> as = new ArrayList<>();
                        AttachmentBean bb = new AttachmentBean();
                        bb.value = -1L;
                        bb.name = getV().getString(R.string.workorder_unlimited);
                        bb.check = true;
                        as.add(bb);
                        getPriority(as);
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
    
    public void getPriority(List<AttachmentBean> as){
        
    }
    
    public void setPriority(Map<Long, String> p){
        
    }


    /**
     * 联网获取工单物资预定记录列表数据
     *
     * @param woId
     */
    public void getWorkorderReserveRecordList(long woId) {
        String request = "{\"woId\":" + woId + "}";
        OkGo.<BaseResponse<List<WorkorderService.WorkorderReserveRocordBean>>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_RESERVE_RECORD_LIST_URL)
                .isSpliceUrl(true)
                .tag(getV())
                .upJson(request)
                .execute(new FMJsonCallback<BaseResponse<List<WorkorderService.WorkorderReserveRocordBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<WorkorderService.WorkorderReserveRocordBean>>> response) {
                        List<WorkorderService.WorkorderReserveRocordBean> data = response.body().data;
                        if (data != null) {
                            getWorkOrderMaterialSuccess(data);
                        } else {
                            getWorkOrderMaterialError();
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<List<WorkorderService.WorkorderReserveRocordBean>>> response) {
                        super.onError(response);
                        getWorkOrderMaterialError();
                    }
                });
    }
    
    public void getWorkOrderMaterialSuccess(List<WorkorderService.WorkorderReserveRocordBean> data){
        
    }
    
    public void getWorkOrderMaterialError(){
        
    }

    /**
     * 获取工单信息
     *
     * @param woId
     */
    public void getWorkorderInfo(final Long woId) {
        getV().showLoading();
        String json = "{\"woId\":" + woId + "}";
        OkGo.<BaseResponse<WorkorderService.WorkorderInfoBean>>post(FM.getApiHost() + WorkorderUrl.WORKORDER_INFO_URL)
                .tag(getV())
                .isSpliceUrl(true)
                .upJson(json)
                .execute(new FMJsonCallback<BaseResponse<WorkorderService.WorkorderInfoBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<WorkorderService.WorkorderInfoBean>> response) {
                        getV().dismissLoading();
                        WorkorderService.WorkorderInfoBean data = response.body().data;
                        if(data != null) {
                            getWorkorderInfoSuccess(woId,data);
                        }else {
                            getWorkorderInfoError();
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<WorkorderService.WorkorderInfoBean>> response) {
                        super.onError(response);
                        getV().dismissLoading();
                        getWorkorderInfoError();
                    }
                });
    }


    public void getWorkorderInfoSuccess(Long woId, WorkorderService.WorkorderInfoBean data) {

    }

    public void getWorkorderInfoError() {

    }
}
