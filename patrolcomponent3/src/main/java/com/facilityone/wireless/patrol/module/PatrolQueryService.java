package com.facilityone.wireless.patrol.module;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.ec.module.OrdersBean;
import com.facilityone.wireless.a.arch.ec.module.Page;
import com.facilityone.wireless.patrol.adapter.PatrolQuerySpotAdapter;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:查询相关
 * Date: 2018/11/19 10:28 AM
 */
public class PatrolQueryService {

    //巡检请求
    public static class PatrolQueryReq {
        public Page page;
        public PatrolQueryConditionBean searchCondition;
    }

    public static class PatrolQueryConditionBean {
        public String patrolName;
        public Long workTeamId;
        public Long startDateTime;
        public Long endDateTime;
        public List<Long> taskStatus;
        public List<Long> spotStatus;
        public Integer sort;

        public void reset() {
            patrolName = null;
            workTeamId = null;
            startDateTime = null;
            endDateTime = null;
            taskStatus = null;
            spotStatus = null;
            sort = null;
        }
    }

    //巡检响应
    public static class PatrolQueryResp {
        public Page page;
        public List<PatrolQueryBodyBean> contents;
    }

    public static class PatrolQueryBodyBean {
        public Long patrolTaskId;
        public String patrolName;
        public String laborer;
        public Long dueStartDateTime;
        public Long dueEndDateTime;
        public Integer normalNumber;
        public Integer exceptionNumber;
        public Integer leakNumber;
        public Integer repairNumber;
        public Integer spotNumber;
        public Integer status;
    }

    //巡检查询点位响应
    public static class PatrolQuerySpotResp {
        public Long patrolTaskId;
        public String laborer;
        public Long dueStartDateTime;
        public Long dueEndDateTime;
        public Long actualStartDateTime;
        public Long actualEndDateTime;
        public Integer leakNumber;
        public Integer exceptionNumber;
        public Integer repairNumber;
        public Integer normalNumber;
        public Integer spotNumber;
        public String period;
        public Boolean readonly;
        public List<SpotsBean> spots;
    }

    public static class SpotsBean extends AbstractExpandableItem<EquipmentBean> implements MultiItemEntity {
        public Long patrolTaskSpotId;
        public Long spotId;
        public EquipmentBean synthesized;
        public List<EquipmentBean> equipments;
        public String name;
        public LocationBean locationDetail;
        public boolean hasOrder;
        public boolean hasException;
        public boolean hasLeak;
        public String locationName;

        @Override
        public int getLevel() {
            return 0;
        }

        @Override
        public int getItemType() {
            return PatrolQuerySpotAdapter.TYPE_SPOT;
        }
    }

    public static class EquipmentBean implements MultiItemEntity {
        public Long eqId;
        public Long spotId;
        public Integer exceptionStatus;
        public boolean hasOrder;
        public boolean hasException;
        public boolean hasLeak;
        public String name;
        public String spotLocationName;
        public String sysType;//设备所属系统
        public String code;
        public LocationBean locationBean;

        @Override
        public int getItemType() {
            return PatrolQuerySpotAdapter.TYPE_EQU;
        }
    }

    public static class PatrolQueryEquReq {
        public Long patrolTaskId;
        public Long patrolTaskSpotId;
        public Long equipmentId;
    }

    public static class PatrolQueryEquResp {
        public Long equId;
        public String code;
        public String name;
        public String equipmentSystemName;
        public String sysType;
        public Integer exceptionStatus;
        public String exceptionDesc;
        public List<OrdersBean> orders;
        public List<PatrolQueryItemBean> contents;
    }

    public static class PatrolQueryItemBean {
        public Long patrolTaskSpotResultId;
        public Long contentId;
        public String content;
        public String result;
        public Boolean isException;
        public Boolean processed;
        public String comment;
        public Integer status;
        public String operator;
        public Long operationTime;
        public Integer operationType;
        public List<String> imageIds;
    }
}
