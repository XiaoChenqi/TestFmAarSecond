package com.facilityone.wireless;


import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.a.arch.BaseApplication;
import com.facilityone.wireless.a.arch.BuildConfig;
import com.facilityone.wireless.a.arch.Facility;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.app.FMChannel;
import com.facilityone.wireless.componentservice.app.AppService;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import java.util.HashMap;

public class InitApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //阿里云崩溃分析
        //initHa();
        //保存不同渠道的参数
        saveChannelParam();
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);
        //Router.getInstance().addService(AppService.class.getSimpleName(),new AppServiceImpl());
        UIRouter.getInstance().registerUI("app");
        //装载需要的组件
        Router.registerComponent("com.facilityone.wireless.demand.applike.DemandApplike");
        Router.registerComponent("com.facilityone.wireless.workorder.applike.WorkorderApplike");
        Router.registerComponent("com.facilityone.wireless.energy.applike.EnergyApplike");
        Router.registerComponent("com.facilityone.wireless.asset.applike.AssetApplike");
        Router.registerComponent("com.facilityone.wireless.contract.applike.ContractApplike");
        Router.registerComponent("com.facilityone.wireless.bulletin.applike.BulletinApplike");
        Router.registerComponent("com.facilityone.wireless.maintenance.applike.MaintenanceApplike");
        Router.registerComponent("com.facilityone.wireless.chart.applike.ChartApplike");
        Router.registerComponent("com.facilityone.wireless.patrol.applike.PatrolApplike");
        Router.registerComponent("com.facilityone.wireless.sign.applike.SignApplike");
        Router.registerComponent("com.facilityone.wireless.payment.applike.PaymentApplike");
        Router.registerComponent("com.facilityone.wireless.inventory.applike.InventoryApplike");
        Router.registerComponent("com.facilityone.wireless.visitor.applike.VisitorApplike");
        Router.registerComponent("com.facilityone.wireless.knowledge.applike.KnowledgeApplike");
        Router.registerComponent("com.facilityone.wireless.monitor.applike.MonitorApplike");
        Router.registerComponent("com.facilityone.wireless.inspection.applike.InspectionApplike");
        //MobSDK.init(this);
        Facility.init(this, BuildConfig.SERVER_URL, BuildConfig.DEBUG);
        DBManager.getInstance();
        //阿里云注册
        //initCloudChannel();
    }
//    private void initCloudChannel() {
//        PushServiceFactory.init(this);
//        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
//        pushService.register(this, new CommonCallback() {
//            @Override
//            public void onSuccess(String response) {
//                LogUtils.d("init cloudchannel success:" + pushService.getDeviceId());
//            }
//
//            @Override
//            public void onFailed(String errorCode, String errorMessage) {
//                LogUtils.d("init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
//            }
//        });
//        // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
//        MiPushRegister.register(this, BuildConfig.MI_APPID, BuildConfig.MI_APPKEY);
//        // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
//        HuaWeiRegister.register(this);
//    }

    /**
     * 不同渠道参数不同（向下传递）
     */
    private void saveChannelParam() {
        HashMap<Object, Object> fmConfigs = FM.getFMConfigs();
        fmConfigs.put(FMChannel.CHANNEL_APP_SERVER, BuildConfig.SERVER_URL);
        fmConfigs.put(FMChannel.CHANNEL_APP_KEY, BuildConfig.APP_KEY);
        fmConfigs.put(FMChannel.CHANNEL_APP_SECRET, BuildConfig.APP_SECRET);
        fmConfigs.put(FMChannel.CHANNEL_UMENG_KEY, BuildConfig.UMENG_CHANNEL_KEY);
        fmConfigs.put(FMChannel.CHANNEL_UMENG_VALUE, BuildConfig.UMENG_CHANNEL_VALUE);
        fmConfigs.put(FMChannel.CHANNEL_UPDATE_APP_KEY, BuildConfig.UPDATE_APP_KEY);
        fmConfigs.put(FMChannel.CHANNEL_UPDATE_CHANNEL, BuildConfig.UPDATE_CHANNEL);
        fmConfigs.put(FMChannel.CHANNEL_CUSTOMER_CODE, BuildConfig.CUSTOMER_CODE);
        fmConfigs.put(FMChannel.CHANNEL_QQZONE_KEY, BuildConfig.QQZONE_KEY);
        fmConfigs.put(FMChannel.CHANNEL_QQZONE_SECRET, BuildConfig.QQZONE_SECRET);
        fmConfigs.put(FMChannel.CHANNEL_DING_DING_SECRET, BuildConfig.DING_DING_SECRET);
        fmConfigs.put(FMChannel.CHANNEL_WEI_XIN_KEY, BuildConfig.WEI_XIN_KEY);
        fmConfigs.put(FMChannel.CHANNEL_WEI_XIN_SECRET, BuildConfig.WEI_XIN_SECRET);
    }


}
