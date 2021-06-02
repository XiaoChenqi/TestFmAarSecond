package com.facilityone.wireless.componentservice.common.permissions;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;

import com.facilityone.wireless.a.arch.R;
import com.facilityone.wireless.a.arch.ec.module.FunctionService;
import com.facilityone.wireless.a.arch.ec.module.IService;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.componentservice.asset.AssetService;
import com.facilityone.wireless.componentservice.bulletin.BulletinService;
import com.facilityone.wireless.componentservice.chart.ChartService;
import com.facilityone.wireless.componentservice.contract.ContractService;
import com.facilityone.wireless.componentservice.demand.DemandService;
import com.facilityone.wireless.componentservice.energy.EnergyService;
import com.facilityone.wireless.componentservice.inspection.InspectionService;
import com.facilityone.wireless.componentservice.inventory.InventoryService;
import com.facilityone.wireless.componentservice.knowledge.KnowledgeService;
import com.facilityone.wireless.componentservice.maintenance.MaintenanceService;
import com.facilityone.wireless.componentservice.monitor.MonitorService;
import com.facilityone.wireless.componentservice.patrol.PatrolService;
import com.facilityone.wireless.componentservice.payment.PaymentService;
import com.facilityone.wireless.componentservice.sign.SignService;
import com.facilityone.wireless.componentservice.visitor.VisitorService;
import com.facilityone.wireless.componentservice.workorder.WorkorderService;
import com.luojilab.component.componentlib.router.Router;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:功能模块权限
 * Date: 2018/6/8 下午5:42
 */
public class PermissionsManager {
    //未做数量
    public static final String UNDOORDERNUMBER = "undoOrderNumber";                          //待处理工单数量
    public static final String UNARRANGEORDERNUMBER = "unArrangeOrderNumber";                //待派工工单数量
    public static final String UNAPPROVALORDERNUMBER = "unApprovalOrderNumber";              //待审核工单数量
    public static final String UNARCHIVEDORDERNUMBER = "unArchivedOrderNumber";              //待存档工单数量
    public static final String PATROLTASKNUMBER = "patrolTaskNumber";                        //巡检任务数量
    public static final String UNAPPROVALREQUIREMENTNUMBER = "unApprovalRequirementNumber";  //待审核需求数量
    public static final String UNDOREQUIREMENTNUMBER = "undoRequirementNumber";              //待处理需求数量
    public static final String UNEVALUATEREQUIREMENTNUMBER = "unEvaluateRequirementNumber";  //待评价需求数量
    public static final String TOBEOUTINVENTORYNUMBER = "toBeOutInventoryNumber";            //待出库任务提示
    public static final String UNAPPROVALINVENTORYNUMBER = "unApprovalInventoryNumber";      //待审批预定单数量
    public static final String UNREADBULLETINNUMBER = "unReadBulletinNumber";                //未读公告数量
    public static final String UNPAIDPAYMENTNUMBER = "unPaidPaymentNumber";                  //待缴费单数量
    public static final String PAIDPAYMENTNUMBER = "paidPaymentNumber";                      //已缴费单数量
    public static final String REFUNDPAYMENTNUMBER = "refundPaymentNumber";                  //退款单数量
    public static final String UNDOINSPECTIONNUMBER = "undoInspectionNumber";                 //查验任务数量
    public static final String UNARCHIVEDINSPECTIONNUMBER = "unArchivedInspectionNumber";      //待验证查验任务数量

    public static class HomeFunction {

        private Map<String, FunctionService.FunctionBean> mFunctionBeanMap;
        private List<FunctionService.FunctionBean> mFbs;
        private int mIndex;
        private Router mRouter;

        private HomeFunction() {
            mFunctionBeanMap = new HashMap<>();
            mFbs = new ArrayList<>();
            mRouter = Router.getInstance();
        }

        Map<String, FunctionService.FunctionBean> getFunctionBeanMap() {
            return mFunctionBeanMap;
        }

        public FunctionService.FunctionBean getFunctionBean(String key) {
            return getFunctionBeanMap().get(key);
        }

        /**
         * 是否有某个模块权限
         *
         * @param key
         * @return
         */
        public boolean getFunctionPermission(String key) {
            FunctionService.FunctionBean functionBean = getFunctionBean(key);
            return functionBean != null;
        }

        //静态内部类线程安全模式--懒汉单例模式
        private static class Holder {
            private static final HomeFunction HOME_FUNCTION = new HomeFunction();
        }

        public static HomeFunction getInstance() {
            return Holder.HOME_FUNCTION;
        }

        public List<FunctionService.FunctionBean> show(String opensJson) {
            mFbs.clear();
            mIndex = 0;

            bulletinFunction(opensJson);

            patrolFunction(opensJson);

            requirementFunction(opensJson);

            workOrderFunction(opensJson);

            ppmFunction(opensJson);

            assetFunction(opensJson);

            energyFunction(opensJson);

            inventoryFunction(opensJson);

            signFunction(opensJson);

            contractFunction(opensJson);

            visitorFunction(opensJson);

            paymentFunction(opensJson);

            knowledgeFunction(opensJson);

            inspectionFunction(opensJson);

            chatFunction(opensJson);

            monitoringFunction(opensJson);


            int supplement = mFbs.size() % FunctionService.COUNT;
            if (supplement != 0) {

                int s = FunctionService.COUNT - supplement;

                for (int i = 0; i < s; i++) {
                    FunctionService.FunctionBean bean = new FunctionService.FunctionBean();
                    bean.clickable = false;
                    bean.index = mIndex++;
                    mFbs.add(bean);
                }
            }

            return mFbs;
        }

        private void inspectionFunction(String opensJson) {
            int icons[] = {
                    R.drawable.inspection_task
                    , R.drawable.inspection_archive
                    , R.drawable.inspection_query
            };

            List<ChildMenu> childMenus = new ArrayList<>();

            String[] titles = getStringArray(R.array.home_inspection_menu_child_title);
            String[] permissions = getStringArray(R.array.home_inspection_menu_child_permissions);

            for (int i = 0; i < icons.length; i++) {
                ChildMenu childMenu = new ChildMenu(permissions[i],
                        CommonConstant.MESSAGE_INSPECTION,
                        icons[i],
                        titles[i], null, i);
                if ("m-inspection-task".equals(permissions[i])) {
                    childMenu.jsonObjectKey = UNDOINSPECTIONNUMBER;
                } else if ("m-inspection-archive".equals(permissions[i])) {
                    childMenu.jsonObjectKey = UNARCHIVEDINSPECTIONNUMBER;
                }
                childMenus.add(childMenu);
            }

            String[] child = { UNDOINSPECTIONNUMBER, UNARCHIVEDINSPECTIONNUMBER };
            InspectionService impl = (InspectionService) mRouter.getService(InspectionService.class.getSimpleName());
            homeMenu(opensJson,
                    getString(R.string.home_inspection_permissions),
                    impl,
                    CommonConstant.MESSAGE_INSPECTION,
                    R.drawable.home_function_inspection,
                    getString(R.string.home_inspection_menu_title),
                    child,
                    childMenus);
        }

        private void visitorFunction(String opensJson) {
            int icons[] = {
                    R.drawable.visit_create
                    , R.drawable.visit_list
            };

            List<ChildMenu> childMenus = new ArrayList<>();

            String[] titles = getStringArray(R.array.home_visit_menu_child_title);
            String[] permissions = getStringArray(R.array.home_visit_menu_child_permissions);

            for (int i = 0; i < icons.length; i++) {
                ChildMenu childMenu = new ChildMenu(permissions[i],
                        CommonConstant.MESSAGE_VISITOR,
                        icons[i],
                        titles[i], null, i);
                childMenus.add(childMenu);
            }

            VisitorService impl = (VisitorService) mRouter.getService(VisitorService.class.getSimpleName());

            homeMenu(opensJson,
                    getString(R.string.home_visit_permissions),
                    impl,
                    CommonConstant.MESSAGE_VISITOR,
                    R.drawable.home_function_visitor,
                    getString(R.string.home_visit_menu_title),
                    null,
                    childMenus);
        }

        private void monitoringFunction(String opensJson) {
            MonitorService impl = (MonitorService) mRouter.getService(MonitorService.class.getSimpleName());
            homeMenu(opensJson,
                    getString(R.string.home_monitoring_permissions),
                    impl,
                    CommonConstant.MESSAGE_MONITORING,
                    R.drawable.home_function_smartoperation,
                    getString(R.string.home_monitoring_menu_title),
                    null,
                    null);
        }

        private void paymentFunction(String opensJson) {
            int icons[] = {
                    R.drawable.home_function_epayment_order_create
                    , R.drawable.home_function_epayment_order_unpay
                    , R.drawable.home_function_epayment_order_payed
                    , R.drawable.home_function_epayment_order_query
                    , R.drawable.home_function_epayment_order_refund
                    , R.drawable.home_function_epayment_order_refund_query
            };

            List<ChildMenu> childMenus = new ArrayList<>();

            String[] titles = getStringArray(R.array.home_pay_menu_child_title);
            String[] permissions = getStringArray(R.array.home_pay_menu_child_permissions);

            for (int i = 0; i < icons.length; i++) {
                ChildMenu childMenu = new ChildMenu(permissions[i],
                        CommonConstant.MESSAGE_PAYMENT,
                        icons[i],
                        titles[i], null, i);
                if ("m-payment-unpaid".equals(permissions[i])) {
                    childMenu.jsonObjectKey = UNPAIDPAYMENTNUMBER;
                } else if ("m-payment-paid".equals(permissions[i])) {
                    childMenu.jsonObjectKey = PAIDPAYMENTNUMBER;
                } else if ("m-payment-refund".equals(permissions[i])) {
                    childMenu.jsonObjectKey = REFUNDPAYMENTNUMBER;
                }
                childMenus.add(childMenu);
            }

            String[] child = { UNPAIDPAYMENTNUMBER, PAIDPAYMENTNUMBER, REFUNDPAYMENTNUMBER };
            PaymentService impl = (PaymentService) mRouter.getService(PaymentService.class.getSimpleName());
            homeMenu(opensJson,
                    getString(R.string.home_pay_permissions),
                    impl,
                    CommonConstant.MESSAGE_PAYMENT,
                    R.drawable.home_function_epayment_order,
                    getString(R.string.home_pay_menu_title),
                    child,
                    childMenus);
        }

        private void chatFunction(String opensJson) {
            ChartService impl = (ChartService) mRouter.getService(ChartService.class.getSimpleName());
            homeMenu(opensJson,
                    getString(R.string.home_chart_permissions),
                    impl,
                    CommonConstant.MESSAGE_CHART,
                    R.drawable.home_function_chart,
                    getString(R.string.home_chart_menu_title),
                    null,
                    null);
        }

        private void knowledgeFunction(String opensJson) {
            KnowledgeService impl = (KnowledgeService) mRouter.getService(KnowledgeService.class.getSimpleName());
            homeMenu(opensJson,
                    getString(R.string.home_knowledge_permissions),
                    impl,
                    CommonConstant.MESSAGE_KNOWLEDGE,
                    R.drawable.home_function_knowledge,
                    getString(R.string.home_knowledge_menu_title),
                    null,
                    null);
        }

        private void contractFunction(String opensJson) {
            int icons[] = {
                    R.drawable.home_function_contract_manager
                    , R.drawable.home_function_contract_query
            };

            List<ChildMenu> childMenus = new ArrayList<>();

            String[] titles = getStringArray(R.array.home_contract_menu_child_title);
            String[] permissions = getStringArray(R.array.home_contract_menu_child_permissions);

            for (int i = 0; i < icons.length; i++) {
                ChildMenu childMenu = new ChildMenu(permissions[i],
                        CommonConstant.MESSAGE_CONTRACT,
                        icons[i],
                        titles[i], null, i);
                childMenus.add(childMenu);
            }

            ContractService impl = (ContractService) mRouter.getService(ContractService.class.getSimpleName());
            homeMenu(opensJson,
                    getString(R.string.home_contract_permissions),
                    impl,
                    CommonConstant.MESSAGE_CONTRACT,
                    R.drawable.home_contract_info,
                    getString(R.string.home_contract_menu_title),
                    null,
                    childMenus);
        }

        private void signFunction(String opensJson) {
            SignService impl = (SignService) mRouter.getService(SignService.class.getSimpleName());
            homeMenu(opensJson,
                    getString(R.string.home_sign_permissions),
                    impl,
                    CommonConstant.MESSAGE_SIGN,
                    R.drawable.home_function_sign,
                    getString(R.string.home_sign_menu_title),
                    null,
                    null);
        }

        private void inventoryFunction(String opensJson) {
            int icons[] = {
                    R.drawable.inventory_create
                    , R.drawable.inventory_in
                    , R.drawable.inventory_out
                    , R.drawable.inventory_move
                    , R.drawable.inventory_stock_take
                    , R.drawable.inventory_book
                    , R.drawable.inventory_my_book
                    , R.drawable.inventory_approval_book
                    , R.drawable.inventory_query
            };

            List<ChildMenu> childMenus = new ArrayList<>();

            String[] titles = getStringArray(R.array.home_inventory_menu_child_title);
            String[] permissions = getStringArray(R.array.home_inventory_menu_child_permissions);

            for (int i = 0; i < icons.length; i++) {
                ChildMenu childMenu = new ChildMenu(permissions[i],
                        CommonConstant.MESSAGE_INVENTORY,
                        icons[i],
                        titles[i], null, i);
                if ("m-inventory-out".equals(permissions[i])) {//出库
                    childMenu.jsonObjectKey = TOBEOUTINVENTORYNUMBER;
                } else if ("m-inventory-approval".equals(permissions[i])) {
                    childMenu.jsonObjectKey = UNAPPROVALINVENTORYNUMBER;
                }
                childMenus.add(childMenu);
            }

            String[] child = { TOBEOUTINVENTORYNUMBER, UNAPPROVALINVENTORYNUMBER };

            InventoryService impl = (InventoryService) mRouter.getService(InventoryService.class.getSimpleName());
            homeMenu(opensJson,
                    getString(R.string.home_inventory_permissions),
                    impl,
                    CommonConstant.MESSAGE_INVENTORY,
                    R.drawable.home_function_inventories,
                    getString(R.string.home_inventory_menu_title),
                    child,
                    childMenus);

        }

        private void energyFunction(String opensJson) {
            EnergyService impl = (EnergyService) mRouter.getService(EnergyService.class.getSimpleName());
            homeMenu(opensJson,
                    getString(R.string.home_energy_permissions),
                    impl,
                    CommonConstant.MESSAGE_ENERGY,
                    R.drawable.home_function_energy_new,
                    getString(R.string.home_energy_menu_title),
                    null,
                    null);
        }

        private void assetFunction(String opensJson) {
            AssetService impl = (AssetService) mRouter.getService(AssetService.class.getSimpleName());
            homeMenu(opensJson,
                    getString(R.string.home_asset_permissions),
                    impl,
                    CommonConstant.MESSAGE_ASSET,
                    R.drawable.home_function_assets,
                    getString(R.string.home_asset_menu_title),
                    null,
                    null);
        }

        private void ppmFunction(String opensJson) {
            MaintenanceService impl = (MaintenanceService) mRouter.getService(MaintenanceService.class.getSimpleName());
            homeMenu(opensJson,
                    getString(R.string.home_ppm_permissions),
                    impl,
                    CommonConstant.MESSAGE_MAINTANCE,
                    R.drawable.home_function_maintance,
                    getString(R.string.home_ppm_menu_title),
                    null,
                    null);
        }

        private void workOrderFunction(String opensJson) {
            int icons[] = { R.drawable.home_function_order_create
                    , R.drawable.home_function_order_undo
                    , R.drawable.home_function_order_arrange
                    , R.drawable.home_function_order_approval
                    , R.drawable.home_function_order_verify
                    , R.drawable.home_function_order_archive
                    , R.drawable.home_function_order_query,
            };

            String[] per = getStringArray(R.array.home_workOrder_menu_child_permissions);
            String[] titles = getStringArray(R.array.home_workOrder_menu_child_title);
            List<ChildMenu> childMenus = new ArrayList<>();
            for (int i = 0; i < icons.length; i++) {
                ChildMenu childMenu = new ChildMenu(per[i], CommonConstant.MESSAGE_WORK_ORDER,
                        icons[i], titles[i], null, i);
                if ("m-wo-process".equals(per[i])) {
                    childMenu.jsonObjectKey = UNDOORDERNUMBER;
                } else if ("m-wo-dispach".equals(per[i])) {
                    childMenu.jsonObjectKey = UNARRANGEORDERNUMBER;
                } else if ("m-wo-approval".equals(per[i])) {
                    childMenu.jsonObjectKey = UNAPPROVALORDERNUMBER;
                } else if ("m-wo-close".equals(per[i])) {
                    childMenu.jsonObjectKey = UNARCHIVEDORDERNUMBER;
                }
                childMenus.add(childMenu);
            }

            String[] child = { UNDOORDERNUMBER, UNARRANGEORDERNUMBER, UNAPPROVALORDERNUMBER, UNARCHIVEDORDERNUMBER };

            WorkorderService impl = (WorkorderService) mRouter.getService(WorkorderService.class.getSimpleName());

            homeMenu(opensJson,
                    getString(R.string.home_workOrder_permissions),
                    impl,
                    CommonConstant.MESSAGE_WORK_ORDER,
                    R.drawable.home_function_work_order,
                    getString(R.string.home_workOrder_menu_title),
                    child,
                    childMenus);
        }


        private void requirementFunction(String opensJson) {

            int icons[] = {
                    R.drawable.home_function_service_create,
                    R.drawable.home_function_service_uncheck,
                    R.drawable.home_function_service_unfinish,
                    R.drawable.home_function_service_finish,
                    R.drawable.home_function_service_query,
            };
            String[] per = getStringArray(R.array.home_service_menu_child_permissions);
            String[] titles = getStringArray(R.array.home_service_menu_child_title);
            List<ChildMenu> childMenus = new ArrayList<>();
            for (int i = 0; i < icons.length; i++) {
                ChildMenu childMenu = new ChildMenu(per[i], CommonConstant.MESSAGE_DEMAND,
                        icons[i], titles[i], null, i);
                if ("m-requirement-approval".equals(per[i])) {
                    childMenu.jsonObjectKey = UNAPPROVALREQUIREMENTNUMBER;
                } else if ("m-requirement-process".equals(per[i])) {
                    childMenu.jsonObjectKey = UNDOREQUIREMENTNUMBER;
                } else if ("m-requirement-evaluate".equals(per[i])) {
                    childMenu.jsonObjectKey = UNEVALUATEREQUIREMENTNUMBER;
                }
                childMenus.add(childMenu);
            }

            String[] child = { UNAPPROVALREQUIREMENTNUMBER, UNDOREQUIREMENTNUMBER, UNEVALUATEREQUIREMENTNUMBER };

            DemandService impl = (DemandService) mRouter.getService(DemandService.class.getSimpleName());
            homeMenu(opensJson,
                    getString(R.string.home_service_permissions),
                    impl,
                    CommonConstant.MESSAGE_DEMAND,
                    R.drawable.home_function_service,
                    getString(R.string.home_service_menu_title),
                    child,
                    childMenus);
        }

        private void patrolFunction(String opensJson) {
            int icons[] = {
                    R.drawable.home_function_patrol_task,
                    R.drawable.home_function_patrol_query,
            };
            String[] per = getStringArray(R.array.home_patrol_menu_child_permissions);
            String[] titles = getStringArray(R.array.home_patrol_menu_child_title);
            List<ChildMenu> childMenus = new ArrayList<>();
            for (int i = 0; i < icons.length; i++) {
                ChildMenu childMenu = new ChildMenu(per[i], CommonConstant.MESSAGE_PATROL,
                        icons[i], titles[i], null, i);
                if ("m-patrol-task".equals(per[i])) {
                    childMenu.jsonObjectKey = PATROLTASKNUMBER;
                }
                childMenus.add(childMenu);
            }

            PatrolService impl = (PatrolService) mRouter.getService(PatrolService.class.getSimpleName());

            homeMenu(opensJson,
                    getString(R.string.home_patrol_permissions),
                    impl,
                    CommonConstant.MESSAGE_PATROL,
                    R.drawable.home_function_patrol,
                    getString(R.string.home_patrol_menu_title),
                    new String[]{ PATROLTASKNUMBER },
                    childMenus);
        }

        private void bulletinFunction(String opensJson) {
            BulletinService impl = (BulletinService) mRouter.getService(BulletinService.class.getSimpleName());
            homeMenu(opensJson,
                    getString(R.string.home_bulletin_permissions),
                    impl,
                    CommonConstant.MESSAGE_BULLETIN,
                    R.drawable.home_function_affiche,
                    getString(R.string.home_bulletin_menu_title),
                    new String[]{ UNREADBULLETINNUMBER },
                    null);
        }

        /**
         * @param opensJson      权限json
         * @param strPermissions 主的权限 m-patrol m-wo
         * @param type           消息那边的类型
         * @param icon           icon
         * @param name           title
         * @param undoKey        未做数量计算
         * @param cs             有权限的儿子
         */
        private void homeMenu(String opensJson,
                              String strPermissions,
                              IService service,
                              int type, int icon,
                              String name, String[] undoKey,
                              List<ChildMenu> cs) {

            //TODO xcq
            if(opensJson!=null&&opensJson.contains(strPermissions))
            //if (opensJson.contains(strPermissions))
            {
                FunctionService.FunctionBean bean = getFunctionBean(strPermissions);
                if (bean == null) {
                    bean = new FunctionService.FunctionBean();
                    bean.type = type;
                    bean.imageId = icon;
                    bean.name = name;
                    bean.clickable = true;
                    bean.index = mIndex++;
                    bean.service = service;
                    mFunctionBeanMap.put(strPermissions, bean);
                }

                if (undoKey != null) {
                    Collections.addAll(bean.undoKey, undoKey);
                }

                if (cs != null && cs.size() > 0) {
                    for (ChildMenu c : cs) {
                        childMenu(opensJson, bean, c);
                    }
                    //排序
                    if (bean.childMenu != null && bean.childMenu.size() > 0) {
                        ArrayList<FunctionService.FunctionBean> functionBeanList = new ArrayList<>();
                        for (Map.Entry<String, FunctionService.FunctionBean> beanEntry : bean.childMenu.entrySet()) {
                            functionBeanList.add(beanEntry.getValue());
                        }

                        int supplement = functionBeanList.size() % FunctionService.COUNT;
                        if (supplement != 0) {

                            int s = FunctionService.COUNT - supplement;
                            int size = cs.size();

                            for (int i = 0; i < s; i++) {
                                FunctionService.FunctionBean b = new FunctionService.FunctionBean();
                                b.clickable = false;
                                b.index = size + i;
                                functionBeanList.add(b);
                            }
                        }

                        Collections.sort(functionBeanList);
                        bean.sortChildMenu = functionBeanList;
                    }
                }

                mFbs.add(bean);
            } else {
                //TODO xcq 移除权限
                //mFunctionBeanMap.remove(strPermissions);
            }

        }

        private void homeMenu(String opensJson,
                              String strPermissions,
                              int type, int icon,
                              String name, String[] undoKey,
                              List<ChildMenu> cs) {
            homeMenu(opensJson, strPermissions, null, type, icon, name, undoKey, cs);
        }

        private void childMenu(String opensJson,
                               FunctionService.FunctionBean bean,
                               ChildMenu menu) {
            if (opensJson.contains(menu.strPermissions) && bean.childMenu.get(menu.strPermissions) != null) {
                //json 有 map 有 暂不处理
            } else if (opensJson.contains(menu.strPermissions)) {
                //json 有 map 没有 创建
                FunctionService.FunctionBean child = new FunctionService.FunctionBean();
                child.type = menu.type;
                child.imageId = menu.icon;
                child.name = menu.name;
                child.clickable = true;
                child.service = menu.service;
                child.index = menu.index;
                bean.childMenu.put(menu.strPermissions, child);

            } else {
                //都没有 或json 没有 clear
                bean.childMenu.remove(menu.strPermissions);
                bean.undoKey.remove(menu.jsonObjectKey);
            }

        }

        private String getString(@StringRes int stringId) {
            return FM.getApplication().getResources().getString(stringId);
        }

        private String[] getStringArray(@ArrayRes int stringArrayId) {
            return FM.getApplication().getResources().getStringArray(stringArrayId);
        }

        private static class ChildMenu {
            public String strPermissions;//权限 类似 m-wo-create
            public int type; //类型 CommonConstant.MESSAGE_BULLETIN
            public int icon; //图标
            public String name;//名称
            public String jsonObjectKey; //未读消息key 类似 unArchivedOrderNumber
            public IService service;//需要跳转的fragment
            public int index;

            public ChildMenu(String strPermissions, int type, int icon, String name, String jsonObjectKey, int index) {
                this.strPermissions = strPermissions;
                this.type = type;
                this.icon = icon;
                this.name = name;
                this.jsonObjectKey = jsonObjectKey;
                this.index = index;
            }
        }
    }

    public static boolean hasPermission(Context context, Integer type) {
        PermissionsManager.HomeFunction instance = PermissionsManager.HomeFunction.getInstance();
        boolean has = false;
        if (type == null) {
            return false;
        }
        switch (type) {
            case CommonConstant.MESSAGE_WORK_ORDER:
                has = instance.getFunctionPermission(context.getResources().getString(R.string.home_workOrder_permissions));
                break;
            case CommonConstant.MESSAGE_PATROL:
                has = instance.getFunctionPermission(context.getResources().getString(R.string.home_patrol_permissions));
                break;
            case CommonConstant.MESSAGE_MAINTANCE:
                has = instance.getFunctionPermission(context.getResources().getString(R.string.home_ppm_permissions));
                break;
            case CommonConstant.MESSAGE_ASSET:
                has = instance.getFunctionPermission(context.getResources().getString(R.string.home_asset_permissions));
                break;
            case CommonConstant.MESSAGE_DEMAND:
                has = instance.getFunctionPermission(context.getResources().getString(R.string.home_service_permissions));
                break;
            case CommonConstant.MESSAGE_INVENTORY:
                has = instance.getFunctionPermission(context.getResources().getString(R.string.home_inventory_permissions));
                break;
            case CommonConstant.MESSAGE_CONTRACT:
                has = instance.getFunctionPermission(context.getResources().getString(R.string.home_contract_permissions));
                break;
            case CommonConstant.MESSAGE_MONITORING:
                has = instance.getFunctionPermission(context.getResources().getString(R.string.home_monitoring_permissions));
                break;
            case CommonConstant.MESSAGE_INSPECTION:
                has = instance.getFunctionPermission(context.getResources().getString(R.string.home_inspection_permissions));
                break;
            case CommonConstant.MESSAGE_BULLETIN:
                has = instance.getFunctionPermission(context.getResources().getString(R.string.home_bulletin_permissions));
                break;
        }
        return has;
    }
}
