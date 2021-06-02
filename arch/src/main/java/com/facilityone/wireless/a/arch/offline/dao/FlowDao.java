package com.facilityone.wireless.a.arch.offline.dao;

import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.offline.model.entity.FlowEntity;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.basiclib.app.FM;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:流程
 * Date: 2018/10/18 3:11 PM
 */
public class FlowDao {

    private final DBManager mDbManager;
    public static final long REPORT_ORDER_TYPE_HUNHE = 3; // 混合

    public FlowDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBFLOW (" + //
                "ID INTEGER ," + // 0: id
                "TYPE INTEGER," + // 1: type
                "DELETED INTEGER," + // 2: deleted
                "DEP_ID INTEGER," + // 3: depId
                "SERVICE_TYPE_ID INTEGER," + // 4: service_type_id
                "PRIORITY_ID INTEGER," + // 5: priorityId
                "CITY_ID INTEGER," + // 6: cityId
                "SITE_ID INTEGER," + // 7: siteId
                "BUILDING_ID INTEGER," + // 8: buildingId
                "FLOOR_ID INTEGER," + // 9: floorId
                "ROOM_ID INTEGER," + // 10: roomId
                "PROJECT_ID INTEGER," + // 11: projectId
                "PRIMARY KEY (ID,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBFLOW SELECT * FROM DBFLOW_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBFLOW";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBFLOW_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBFLOW RENAME TO DBFLOW_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBFLOW";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addFlow(List<FlowEntity> flowEntities) {
        if (flowEntities == null || flowEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBFLOW VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

            for (FlowEntity flowEntity : flowEntities) {
                Object[] args = {
                        flowEntity.getWopId()
                        , flowEntity.getType()
                        , flowEntity.getDeleted()
                        , flowEntity.getOrganizationId()
                        , flowEntity.getServiceTypeId()
                        , flowEntity.getPriorityId()
                        , flowEntity.getPosition() == null ? null : flowEntity.getPosition().cityId
                        , flowEntity.getPosition() == null ? null : flowEntity.getPosition().siteId
                        , flowEntity.getPosition() == null ? null : flowEntity.getPosition().buildingId
                        , flowEntity.getPosition() == null ? null : flowEntity.getPosition().floorId
                        , flowEntity.getPosition() == null ? null : flowEntity.getPosition().roomId
                        , projectId };
                mDbManager.insert(args, sql);
            }

//            String delSql = "DELETE FROM DBFLOW WHERE DELETED = ? AND PROJECT_ID = ?";
//            Object[] delArgs = { true, projectId };
//            mDbManager.delete(delArgs, delSql);

            result = true;
            mDbManager.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            mDbManager.endTransaction();
        }
        return result;
    }

    public List<FlowEntity> queryPriorityIds(Long depId, Long serviceTypeId, Long woTypeId, LocationBean locationBean) {
        Long projectId = FM.getProjectId();
        List<String> args = new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT * FROM DBFLOW WHERE PROJECT_ID = ?  AND DELETED = 0 ");
        args.add(projectId + "");
        
        if (locationBean == null) {
            return null;
        }


        if (locationBean.cityId != null) {
            sb.append(" AND CITY_ID = ? ");
            args.add(locationBean.cityId + "");
        }

        if (locationBean.siteId != null) {
            sb.append(" AND SITE_ID = ? ");
            args.add(locationBean.siteId + "");
        }else {
            sb.append(" AND SITE_ID is null ");
        }

        if (locationBean.buildingId != null) {
            sb.append(" AND BUILDING_ID = ? ");
            args.add(locationBean.buildingId + "");
        }else {
            sb.append(" AND BUILDING_ID is null ");
        }

        if (locationBean.floorId != null) {
            sb.append(" AND FLOOR_ID = ? ");
            args.add(locationBean.floorId + "");
        }else {
            sb.append(" AND FLOOR_ID is null ");
        }

        if (locationBean.roomId != null) {
            sb.append(" AND ROOM_ID = ? ");
            args.add(locationBean.roomId + "");
        }else {
            sb.append(" AND ROOM_ID is null ");
        }

        if (depId != null && depId != 0L) {
            sb.append(" AND DEP_ID = ? ");
            args.add(depId + "");
        }else {
            sb.append(" AND DEP_ID is null ");
        }
        
        if (serviceTypeId != null && serviceTypeId != 0L) {
            sb.append(" AND SERVICE_TYPE_ID = ? ");
            args.add(serviceTypeId + "");
        }else {
            sb.append(" AND SERVICE_TYPE_ID is null ");
        }
        

        sb.append(" AND TYPE = ? ");
        args.add(woTypeId + "");
        
        sb.append("ORDER BY PRIORITY_ID;");

        LogUtils.e(sb.toString(),args.toArray(new String[args.size()]));

        Cursor cursor = null;
        cursor = mDbManager.query(args.toArray(new String[args.size()]), sb.toString());
        if(cursor == null || cursor.getCount() <= 0) {
            args.set(args.size() - 1,REPORT_ORDER_TYPE_HUNHE + "");
            cursor = mDbManager.query(args.toArray(new String[args.size()]), sb.toString());
        }
        List<FlowEntity> ids = null;
        if (cursor != null) {
            ids = new ArrayList<>();
            while (cursor.moveToNext()) {
                FlowEntity flowEntity = new FlowEntity();
                flowEntity.setWopId(cursor.getLong(cursor.getColumnIndex("ID")));
                flowEntity.setPriorityId(cursor.getLong(cursor.getColumnIndex("PRIORITY_ID")));
                ids.add(flowEntity);
            }
            cursor.close();
        }

        return ids;
    }
}
