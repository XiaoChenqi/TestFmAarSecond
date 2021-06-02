package com.facilityone.wireless.a.arch.offline.dao;

import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.offline.model.entity.DBPatrolConstant;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolSpotEntity;
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
 * description:巡检点位
 * Date: 2018/10/30 4:56 PM
 */
public class PatrolSpotDao {

    private final DBManager mDbManager;

    public PatrolSpotDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBPATROLSPOT (" + //
                "ID INTEGER," + // 0: patrolSpotId
                "SPOT_ID INTEGER," + // 1: spotId
                "TASK_ID INTEGER," + // 2: taskId
                "COMP_NUMBER INTEGER," + // 3: compNumber
                "EQ_NUMBER INTEGER," + // 4: eqNumber
                "SORT INTEGER," + // 5: sort
                "START_DATE INTEGER," + // 6: startDate
                "END_DATE INTEGER," + // 7: endDate
                "EXCEPTION INTEGER," + // 8: exception
                "COMPLETED INTEGER," + // 9: completed
                "REMOTE_COMPLETED INTEGER," + // 10: remote_completed
                "SYNCED INTEGER," + // 11: synced
                "USER_ID INTEGER," + // 12: userId
                "HANDLER TEXT," + // 13: handler
                "DELETED INTEGER," + // 14: deleted
                "PROJECT_ID INTEGER ," + // 15: projectId
                "PRIMARY KEY (ID,USER_ID,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBPATROLSPOT SELECT * FROM DBPATROLSPOT_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLSPOT";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLSPOT_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBPATROLSPOT RENAME TO DBPATROLSPOT_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBPATROLSPOT";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addPatrolSpot(List<PatrolSpotEntity> spotEntities) {
        if (spotEntities == null || spotEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBPATROLSPOT VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            for (PatrolSpotEntity entity : spotEntities) {
                Object[] args = {
                        entity.getPatrolSpotId()
                        , entity.getSpotId()
                        , entity.getTaskId()
                        , entity.getCompNumber()
                        , entity.getEquNumber()
                        , entity.getSort()
                        , entity.getStartTime()
                        , entity.getEndTime()
                        , DBPatrolConstant.DEFAULT_VALUE
                        , DBPatrolConstant.DEFAULT_VALUE
                        , DBPatrolConstant.DEFAULT_VALUE
                        , DBPatrolConstant.DEFAULT_VALUE
                        , userId
                        , null
                        , entity.getDeleted()
                        , projectId };
                mDbManager.insert(args, sql);
            }

            String delSql = "DELETE FROM DBPATROLSPOT WHERE DELETED = ? AND PROJECT_ID = ?";
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

    public void deleteTaskSpot(List<Long> deletedId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<Object> args = new ArrayList<>();
        args.add(projectId);
        args.add(userId);
        StringBuilder sb = new StringBuilder();
        sb.append("delete from DBPATROLSPOT WHERE PROJECT_ID = ? AND USER_ID = ? AND  TASK_ID IN (");
        for (Long id : deletedId) {
            sb.append(" ?,");
            args.add(id);
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(");");

        LogUtils.d(sb.toString());

        mDbManager.delete(args.toArray(new Object[args.size()]), sb.toString());
    }


    public void updateRemoteCompleted(Long patrolSpotId, String handler, Boolean finished) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<Object> args = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE DBPATROLSPOT SET ");

        if (handler != null) {
            sb.append(" HANDLER = ? ,");
            args.add(handler);
        }

        if (finished != null && finished) {
            sb.append(" REMOTE_COMPLETED = ? ,");
            args.add(DBPatrolConstant.TRUE_VALUE);
        }

        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(" WHERE PROJECT_ID = ? AND USER_ID = ? AND ID = ?;");
        args.add(projectId);
        args.add(userId);
        args.add(patrolSpotId);

        LogUtils.d(sb.toString());

        mDbManager.update(args.toArray(new Object[args.size()]), sb.toString());
    }

    public void update(Long patrolSpotId, int completed, int exception, int needSync, Long startTime, Long endTime) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        String sql = "UPDATE DBPATROLSPOT SET COMPLETED = ?,EXCEPTION = ? ,SYNCED = ? , START_DATE = ?, END_DATE  = ? WHERE PROJECT_ID = ? AND USER_ID = ? AND ID = ?;";
        LogUtils.d(sql);
        Object[] args = { completed, exception, needSync, startTime, endTime, projectId, userId, patrolSpotId };
        mDbManager.update(args, sql);
    }

    public PatrolSpotEntity getSpot(Long id) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        String sql = "select * from DBPATROLSPOT where id = ? and project_id = ? and user_id = ? ";
        String[] args = { id + "", projectId + "", userId + "" };
        Cursor cursor = mDbManager.query(args, sql);
        PatrolSpotEntity bean = null;
        if (cursor != null) {
            if (cursor.moveToNext()) {
                bean = new PatrolSpotEntity();
                bean.setException(cursor.getInt(cursor.getColumnIndex("EXCEPTION")));
                bean.setCompleted(cursor.getInt(cursor.getColumnIndex("COMPLETED")));
                bean.setNeedSync(cursor.getInt(cursor.getColumnIndex("SYNCED")));
                bean.setStartTime(cursor.getLong(cursor.getColumnIndex("START_DATE")));
                bean.setEndTime(cursor.getLong(cursor.getColumnIndex("END_DATE")));
            }
            cursor.close();
        }

        return bean;
    }

    public List<PatrolSpotEntity> getSpotList(Long taskId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<PatrolSpotEntity> temp = new ArrayList<>();

        String sql = "SELECT S.ID,S.SPOT_ID,S.REMOTE_COMPLETED,S.COMP_NUMBER,S.EQ_NUMBER,S.START_DATE,S.END_DATE,S.EXCEPTION,S.COMPLETED,S.SYNCED,S.HANDLER,B.NAME,B.CODE,B.SPOT_LOCATION,B.CITY_ID,B.SITE_ID,B.BUILDING_ID,B.FLOOR_ID,B.ROOM_ID FROM DBPATROLSPOT AS S LEFT JOIN DBPATROLBASESPOT AS B ON S.SPOT_ID = B.ID  AND S.PROJECT_ID = B.PROJECT_ID WHERE S.TASK_ID = ? AND S.PROJECT_ID = ? AND S.USER_ID = ? ORDER BY S.SORT ";
        String[] args = { taskId + "", projectId + "", userId + "" };

        LogUtils.d(sql);

        Cursor cursor = mDbManager.query(args, sql);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PatrolSpotEntity bean = new PatrolSpotEntity();
                bean.setPatrolSpotId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setSpotId(cursor.getLong(cursor.getColumnIndex("SPOT_ID")));
                bean.setCompNumber(cursor.getInt(cursor.getColumnIndex("COMP_NUMBER")));
                bean.setEquNumber(cursor.getInt(cursor.getColumnIndex("EQ_NUMBER")));
                bean.setStartTime(cursor.getLong(cursor.getColumnIndex("START_DATE")));
                bean.setEndTime(cursor.getLong(cursor.getColumnIndex("END_DATE")));
                bean.setException(cursor.getInt(cursor.getColumnIndex("EXCEPTION")));
                bean.setCompleted(cursor.getInt(cursor.getColumnIndex("COMPLETED")));
                bean.setRemoteCompleted(cursor.getInt(cursor.getColumnIndex("REMOTE_COMPLETED")));
                bean.setNeedSync(cursor.getInt(cursor.getColumnIndex("SYNCED")));
                bean.setHandler(cursor.getString(cursor.getColumnIndex("HANDLER")));
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setCode(cursor.getString(cursor.getColumnIndex("CODE")));
                bean.setLocationName(cursor.getString(cursor.getColumnIndex("SPOT_LOCATION")));
                LocationBean locationBean = LocationNullUtils.getNullLocation(cursor);
                bean.setLocation(locationBean);
                temp.add(bean);
            }
            cursor.close();
        }
        return temp;
    }

    public List<PatrolSpotEntity> getSpotList(String code) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<PatrolSpotEntity> temp = new ArrayList<>();

        String sql = "SELECT S.REMOTE_COMPLETED,S.EXCEPTION,S.COMPLETED,S.SYNCED,S.COMP_NUMBER,S.EQ_NUMBER,S.ID,S.HANDLER,B.NAME,B.CODE,B.SPOT_LOCATION ,T.TASK_NAME ,T.DUE_START_DATE_TIME ,T.DUE_END_DATE_TIME,S.TASK_ID,T.PLAN_ID FROM DBPATROLSPOT AS S LEFT JOIN DBPATROLBASESPOT AS B ON S.SPOT_ID = B.ID  AND S.PROJECT_ID = B.PROJECT_ID LEFT JOIN DBPATROLTASK AS T ON  S.TASK_ID = T.ID AND S.PROJECT_ID = T.PROJECT_ID AND S.USER_ID = T.USER_ID WHERE B.CODE = ? AND S.PROJECT_ID = ? AND S.USER_ID = ?  ORDER BY T.STATUS DESC,T.DUE_START_DATE_TIME ASC ,T.DUE_END_DATE_TIME ASC,S.SORT ASC,S.SPOT_ID ASC;";
        String[] args = { code, projectId + "", userId + "" };

        LogUtils.d(sql);

        Cursor cursor = mDbManager.query(args, sql);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PatrolSpotEntity bean = new PatrolSpotEntity();
                bean.setPatrolSpotId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setTaskId(cursor.getLong(cursor.getColumnIndex("TASK_ID")));
                bean.setTaskPlanId(cursor.getLong(cursor.getColumnIndex("PLAN_ID")));
                bean.setTaskDueStartDateTime(cursor.getLong(cursor.getColumnIndex("DUE_START_DATE_TIME")));
                bean.setTaskDueEndDateTime(cursor.getLong(cursor.getColumnIndex("DUE_END_DATE_TIME")));
                bean.setCompNumber(cursor.getInt(cursor.getColumnIndex("COMP_NUMBER")));
                bean.setEquNumber(cursor.getInt(cursor.getColumnIndex("EQ_NUMBER")));
                bean.setException(cursor.getInt(cursor.getColumnIndex("EXCEPTION")));
                bean.setCompleted(cursor.getInt(cursor.getColumnIndex("COMPLETED")));
                bean.setRemoteCompleted(cursor.getInt(cursor.getColumnIndex("REMOTE_COMPLETED")));
                bean.setNeedSync(cursor.getInt(cursor.getColumnIndex("SYNCED")));
                bean.setHandler(cursor.getString(cursor.getColumnIndex("HANDLER")));
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setCode(cursor.getString(cursor.getColumnIndex("CODE")));
                bean.setTaskName(cursor.getString(cursor.getColumnIndex("TASK_NAME")));
                bean.setLocationName(cursor.getString(cursor.getColumnIndex("SPOT_LOCATION")));
                temp.add(bean);
            }
            cursor.close();
        }
        return temp;
    }
}