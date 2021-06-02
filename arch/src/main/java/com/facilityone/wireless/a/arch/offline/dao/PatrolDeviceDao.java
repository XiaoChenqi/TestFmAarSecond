package com.facilityone.wireless.a.arch.offline.dao;

import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.offline.model.entity.DBPatrolConstant;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolEquEntity;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolItemEntity;
import com.facilityone.wireless.a.arch.offline.model.service.PatrolDbService;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.a.arch.offline.util.LocationNullUtils;
import com.facilityone.wireless.basiclib.app.FM;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检设备
 * Date: 2018/10/30 4:56 PM
 */
public class PatrolDeviceDao {

    private final DBManager mDbManager;

    public PatrolDeviceDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBPATROLEQU (" + //
                "ID ," + // 0: equId
                "SPOT_ID INTEGER," + // 1: patrolSpotId
                "TASK_ID INTEGER," + // 2: taskId
                "SORT INTEGER," + // 3: sort
                "EXCEPTION INTEGER," + // 4: exception
                "REMOTE_COMPLETED INTEGER," + // 5: remote completed
                "COMPLETED INTEGER," + // 6: completed
                "CHECK_NUMBER_STOP INTEGER," + // 7: checkNumberStop
                "CHECK_NUMBER_USE INTEGER," + // 8: checkNumberUse
                "DEVICE_STATUS INTEGER," + // 9: device_status
                "MISS INTEGER," + // 10: miss
                "USER_ID INTEGER," + // 11: userId
                "DELETED INTEGER," + // 12: deleted
                "PROJECT_ID INTEGER ," + // 13: projectId
                "PRIMARY KEY (ID,SPOT_ID,USER_ID,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBPATROLEQU SELECT * FROM DBPATROLEQU_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLEQU";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLEQU_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBPATROLEQU RENAME TO DBPATROLEQU_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBPATROLEQU";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addPatrolEqu(List<PatrolEquEntity> equEntities) {
        if (equEntities == null || equEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBPATROLEQU VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            for (PatrolEquEntity entity : equEntities) {
                Object[] args = {
                        entity.getEqId()
                        , entity.getSpotId()
                        , entity.getTaskId()
                        , entity.getSort()
                        , DBPatrolConstant.DEFAULT_VALUE
                        , DBPatrolConstant.DEFAULT_VALUE
                        , DBPatrolConstant.DEFAULT_VALUE
                        , entity.getItemStopNumber()
                        , entity.getItemUseNumber()
                        , true
                        , false
                        , userId
                        , entity.getDeleted()
                        , projectId };
                mDbManager.insert(args, sql);
            }

            String delSql = "DELETE FROM DBPATROLEQU WHERE DELETED = ? AND PROJECT_ID = ?";
            Object[] delArgs = { true, projectId };
            mDbManager.delete(delArgs, delSql);

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

    public void deleteTaskDevice(List<Long> deletedId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<Object> args = new ArrayList<>();
        args.add(projectId);
        args.add(userId);
        StringBuilder sb = new StringBuilder();
        sb.append("delete from DBPATROLEQU WHERE PROJECT_ID = ? AND USER_ID = ? AND  TASK_ID IN (");
        for (Long id : deletedId) {
            sb.append(" ?,");
            args.add(id);
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(");");

        LogUtils.d(sb.toString());

        mDbManager.delete(args.toArray(new Object[args.size()]), sb.toString());
    }

    public void updateRemoteCompleted(int value, Long eqId, Long spotId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        String sql = "UPDATE DBPATROLEQU SET REMOTE_COMPLETED = ? WHERE PROJECT_ID = ? AND USER_ID = ? AND ID = ? AND SPOT_ID = ? ;";
        Object[] queryArgs = { value, projectId, userId, eqId, spotId };

        mDbManager.update(queryArgs, sql);
    }

    private void updateItemNumber(int stop, int use, Long eqId, Long spotId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        String sql = "UPDATE DBPATROLEQU SET  CHECK_NUMBER_STOP = ? , CHECK_NUMBER_USE = ? WHERE PROJECT_ID = ? AND USER_ID = ? AND ID = ? AND SPOT_ID = ? ;";
        Object[] queryArgs = { stop, use, projectId, userId, eqId, spotId };

        mDbManager.update(queryArgs, sql);
    }

    public void updateStatus(Long eqId, Long spotId, boolean deviceStatus, boolean miss, int exception, int completed) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        String sql = "UPDATE DBPATROLEQU SET  DEVICE_STATUS = ?,MISS = ?,EXCEPTION = ? ,COMPLETED = ? WHERE PROJECT_ID = ? AND USER_ID = ? AND ID = ? AND SPOT_ID = ? ;";
        Object[] queryArgs = { deviceStatus, miss, exception, completed, projectId, userId, eqId, spotId };

        mDbManager.update(queryArgs, sql);
    }

    public List<PatrolEquEntity> getDeviceList(Long spotId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<PatrolEquEntity> temp = new ArrayList<>();

        String sql = "SELECT E.ID,E.MISS,E.REMOTE_COMPLETED,E.DEVICE_STATUS,E.SPOT_ID,E.EXCEPTION,E.COMPLETED,E.CHECK_NUMBER_STOP,E.CHECK_NUMBER_USE,B.NAME,B.CODE,B.CITY_ID,B.SITE_ID,B.BUILDING_ID,B.FLOOR_ID,B.ROOM_ID  FROM DBPATROLEQU AS E LEFT JOIN DBEQU AS B ON E.ID = B.ID  AND E.PROJECT_ID = B.PROJECT_ID WHERE E.SPOT_ID = ? AND E.PROJECT_ID = ? AND E.USER_ID = ? ORDER BY E.SORT";
        String[] args = { spotId + "", projectId + "", userId + "" };

        LogUtils.d(sql);

        Cursor cursor = mDbManager.query(args, sql);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PatrolEquEntity bean = new PatrolEquEntity();
                bean.setEqId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setSpotId(cursor.getLong(cursor.getColumnIndex("SPOT_ID")));
                bean.setItemStopNumber(cursor.getInt(cursor.getColumnIndex("CHECK_NUMBER_STOP")));
                bean.setItemUseNumber(cursor.getInt(cursor.getColumnIndex("CHECK_NUMBER_USE")));
                bean.setException(cursor.getInt(cursor.getColumnIndex("EXCEPTION")));
                bean.setCompleted(cursor.getInt(cursor.getColumnIndex("COMPLETED")));
                bean.setRemoteCompleted(cursor.getInt(cursor.getColumnIndex("REMOTE_COMPLETED")));
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setCode(cursor.getString(cursor.getColumnIndex("CODE")));
                bean.setDeviceStatus(cursor.getInt(cursor.getColumnIndex("DEVICE_STATUS")) != 0);
                bean.setMiss(cursor.getInt(cursor.getColumnIndex("MISS")) != 0);
                bean.setOriginalDeviceStatus(bean.isDeviceStatus());
                LocationBean locationBean = LocationNullUtils.getNullLocation(cursor);
                bean.setLocation(locationBean);
                temp.add(bean);
            }
            cursor.close();
        }
        return temp;
    }

    public void updateEquItemNumber(List<PatrolEquEntity> equEntities) {
        PatrolItemDao itemDao = new PatrolItemDao();
        for (PatrolEquEntity equEntity : equEntities) {
            if (PatrolDbService.COMPREHENSIVE_EQU_ID != equEntity.getEqId()) {
                List<PatrolItemEntity> itemEntities = itemDao.getItemList(equEntity.getEqId(), equEntity.getSpotId(), null);
                int use = 0;
                int stop = 0;
                for (PatrolItemEntity itemEntity : itemEntities) {
                    Integer validStatus = itemEntity.getValidStatus();
                    if (validStatus != null) {
                        switch (validStatus) {
                            case PatrolDbService.PATROL_ITEM_NONE:
                                use++;
                                stop++;
                                break;
                            case PatrolDbService.PATROL_ITEM_STOP:
                                stop++;
                                break;
                            case PatrolDbService.PATROL_ITEM_USE:
                                use++;
                                break;
                        }
                    }
                }
                equEntity.setItemUseNumber(use);
                equEntity.setItemStopNumber(stop);
                updateItemNumber(equEntity.getItemStopNumber(), equEntity.getItemUseNumber(), equEntity.getEqId(), equEntity.getSpotId());
            }
        }


    }
}
