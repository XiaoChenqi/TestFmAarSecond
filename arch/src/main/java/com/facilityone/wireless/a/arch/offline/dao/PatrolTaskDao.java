package com.facilityone.wireless.a.arch.offline.dao;

import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.a.arch.offline.model.entity.DBPatrolConstant;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolTaskEntity;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.basiclib.app.FM;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检任务
 * Date: 2018/10/30 4:56 PM
 */
public class PatrolTaskDao {

    private final DBManager mDbManager;

    public PatrolTaskDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBPATROLTASK (" + //
                "ID INTEGER," + // 0: taskId
                "PLAN_ID INTEGER," + // 1: planId
                "TASK_NAME TEXT," + // 2: taskName
                "SPOT_NUMBER INTEGER," + // 3: spotNumber
                "EQ_NUMBER INTEGER," + // 4: eqNumber
                "DUE_START_DATE_TIME INTEGER," + // 5: dueStartDateTime
                "DUE_END_DATE_TIME INTEGER," + // 6: dueEndDateTime
                "STATUS INTEGER," + // 7: status
                "START_DATE INTEGER," + // 8: startDate
                "END_DATE INTEGER," + // 9: endDate
                "EXCEPTION INTEGER," + // 10: exception
                "COMPLETED INTEGER," + // 11: completed
                "NEED_SYNC INTEGER," + // 12: need_sync
                "UPDATE_TIME INTEGER," + // 13: update time
                "DELETED INTEGER," + // 14: deleted
                "USER_ID INTEGER," + // 15: userId
                "PROJECT_ID INTEGER," + // 16: projectId
                "PRIMARY KEY (ID,USER_ID,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBPATROLTASK SELECT * FROM DBPATROLTASK_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLTASK";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLTASK_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBPATROLTASK RENAME TO DBPATROLTASK_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBPATROLTASK";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addPatrolTask(List<PatrolTaskEntity> taskEntities) {
        if (taskEntities == null || taskEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBPATROLTASK VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            for (PatrolTaskEntity entity : taskEntities) {
                Object[] args = {
                        entity.getTaskId()
                        , entity.getPlanId()
                        , entity.getTaskName()
                        , entity.getSpotNumber()
                        , entity.getEqNumber()
                        , entity.getDueStartDateTime()
                        , entity.getDueEndDateTime()
                        , entity.getStatus()
                        , entity.getStartTime()
                        , entity.getEndTime()
                        , DBPatrolConstant.DEFAULT_VALUE
                        , DBPatrolConstant.DEFAULT_VALUE
                        , DBPatrolConstant.DEFAULT_VALUE
                        , null
                        , entity.getDeleted()
                        , userId
                        , projectId };
                mDbManager.insert(args, sql);
            }

            String delSql = "DELETE FROM DBPATROLTASK WHERE DELETED = ? AND PROJECT_ID = ?";
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

    public List<PatrolTaskEntity> getTaskList(Long time) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<PatrolTaskEntity> temp = new ArrayList<>();
        Cursor cursor = null;
        if (time == null) {
            String sql = "SELECT * FROM DBPATROLTASK WHERE PROJECT_ID = ? AND USER_ID = ? ORDER BY UPDATE_TIME DESC , STATUS DESC , DUE_START_DATE_TIME , DUE_END_DATE_TIME ";
            String[] queryArgs = { projectId + "", userId + "" };
            cursor = mDbManager.query(queryArgs, sql);
        } else {
            String sql = "SELECT * FROM DBPATROLTASK WHERE PROJECT_ID = ? AND USER_ID = ? AND DUE_END_DATE_TIME >= ? ORDER BY UPDATE_TIME DESC , STATUS DESC , DUE_START_DATE_TIME , DUE_END_DATE_TIME ";
            String[] queryArgs = { projectId + "", userId + "", time + "" };
            cursor = mDbManager.query(queryArgs, sql);
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PatrolTaskEntity bean = new PatrolTaskEntity();
                bean.setTaskId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setPlanId(cursor.getLong(cursor.getColumnIndex("PLAN_ID")));
                bean.setTaskName(cursor.getString(cursor.getColumnIndex("TASK_NAME")));
                bean.setSpotNumber(cursor.getInt(cursor.getColumnIndex("SPOT_NUMBER")));
                bean.setEqNumber(cursor.getInt(cursor.getColumnIndex("EQ_NUMBER")));
                bean.setDueStartDateTime(cursor.getLong(cursor.getColumnIndex("DUE_START_DATE_TIME")));
                bean.setDueEndDateTime(cursor.getLong(cursor.getColumnIndex("DUE_END_DATE_TIME")));
                bean.setStartTime(cursor.getLong(cursor.getColumnIndex("START_DATE")));
                bean.setEndTime(cursor.getLong(cursor.getColumnIndex("END_DATE")));
                bean.setStatus(cursor.getInt(cursor.getColumnIndex("STATUS")));
                bean.setException(cursor.getInt(cursor.getColumnIndex("EXCEPTION")));
                bean.setCompleted(cursor.getInt(cursor.getColumnIndex("COMPLETED")));
                bean.setNeedSync(cursor.getInt(cursor.getColumnIndex("NEED_SYNC")));
                temp.add(bean);
            }
            cursor.close();
        }
        return temp;
    }

    public List<Long> getTaskIds() {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<Long> temp = new ArrayList<>();
        String sql = "SELECT * FROM DBPATROLTASK WHERE PROJECT_ID = ? AND USER_ID = ? ORDER BY UPDATE_TIME DESC , STATUS DESC , DUE_START_DATE_TIME , DUE_END_DATE_TIME ;";
        String[] queryArgs = { projectId + "", userId + "" };
        Cursor cursor = mDbManager.query(queryArgs, sql);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex("ID"));
                temp.add(id);
            }
            cursor.close();
        }
        return temp;
    }

    public void deleteTask(List<Long> deletedId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<Object> args = new ArrayList<>();
        args.add(projectId);
        args.add(userId);
        StringBuilder sb = new StringBuilder();
        sb.append("delete from DBPATROLTASK WHERE PROJECT_ID = ? AND USER_ID = ? AND  ID IN (");
        for (Long id : deletedId) {
            sb.append(" ?,");
            args.add(id);
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(");");

        LogUtils.d(sb.toString());

        mDbManager.delete(args.toArray(new Object[args.size()]), sb.toString());

        PatrolSpotDao spotDao = new PatrolSpotDao();
        spotDao.deleteTaskSpot(deletedId);

        PatrolDeviceDao deviceDao = new PatrolDeviceDao();
        deviceDao.deleteTaskDevice(deletedId);

        PatrolItemDao itemDao = new PatrolItemDao();
        itemDao.deleteTaskItem(deletedId);

        PatrolPicDao picDao = new PatrolPicDao();
        picDao.deletePic(deletedId);
    }

    public void update(int status, Long patrolTaskId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        String sql = "UPDATE DBPATROLTASK SET STATUS = ? WHERE PROJECT_ID = ? AND USER_ID = ? AND ID = ?;";
        Object[] queryArgs = { status, projectId, userId, patrolTaskId };

        mDbManager.update(queryArgs, sql);
    }

    public void update(int status, int complete, Long patrolTaskId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        String sql;
        Object[] queryArgs;
        if(complete == DBPatrolConstant.TRUE_VALUE){
            sql = "UPDATE DBPATROLTASK SET STATUS = ? ,COMPLETED = ? WHERE PROJECT_ID = ? AND USER_ID = ? AND ID = ?;";
            queryArgs = new Object[]{ status, DBPatrolConstant.TRUE_VALUE,projectId, userId, patrolTaskId };
        }else{
            sql = "UPDATE DBPATROLTASK SET STATUS = ? WHERE PROJECT_ID = ? AND USER_ID = ? AND ID = ?;";
            queryArgs = new Object[]{ status, projectId, userId, patrolTaskId };
        }

        mDbManager.update(queryArgs, sql);
    }

    public void updateSync(int sync, Long patrolTaskId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        String sql = "UPDATE DBPATROLTASK SET NEED_SYNC = ? WHERE PROJECT_ID = ? AND USER_ID = ? AND ID = ?;";
        Object[] queryArgs = { sync, projectId, userId, patrolTaskId };

        mDbManager.update(queryArgs, sql);
    }

    public void update(Long taskId, int exception, int needSync, int completed, Long startTime, Long endTime, Long updateTime) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        String sql;
        Object[] queryArgs;
        if (updateTime != null) {
            sql = "UPDATE DBPATROLTASK SET EXCEPTION = ?,NEED_SYNC = ?,COMPLETED = ? ,START_DATE = ? , END_DATE = ? , UPDATE_TIME = ?  WHERE PROJECT_ID = ? AND USER_ID = ? AND ID = ?;";
            queryArgs = new Object[]{ exception, needSync, completed, startTime, endTime, updateTime, projectId, userId, taskId };
        } else {
            sql = "UPDATE DBPATROLTASK SET EXCEPTION = ?,NEED_SYNC = ?,COMPLETED = ? ,START_DATE = ? , END_DATE = ?  WHERE PROJECT_ID = ? AND USER_ID = ? AND ID = ?;";
            queryArgs = new Object[]{ exception, needSync, completed, startTime, endTime, projectId, userId, taskId };
        }

        mDbManager.update(queryArgs, sql);
    }

    public PatrolTaskEntity getTask(Long taskId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        String sql = "select * from DBPATROLTASK WHERE PROJECT_ID = ? AND USER_ID = ? AND ID = ?;";
        String[] queryArgs = { projectId + "", userId + "", taskId + "" };
        Cursor cursor = mDbManager.query(queryArgs, sql);
        PatrolTaskEntity bean = null;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                bean = new PatrolTaskEntity();
                bean.setTaskId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setPlanId(cursor.getLong(cursor.getColumnIndex("PLAN_ID")));
                bean.setTaskName(cursor.getString(cursor.getColumnIndex("TASK_NAME")));
                bean.setSpotNumber(cursor.getInt(cursor.getColumnIndex("SPOT_NUMBER")));
                bean.setEqNumber(cursor.getInt(cursor.getColumnIndex("EQ_NUMBER")));
                bean.setDueStartDateTime(cursor.getLong(cursor.getColumnIndex("DUE_START_DATE_TIME")));
                bean.setDueEndDateTime(cursor.getLong(cursor.getColumnIndex("DUE_END_DATE_TIME")));
                bean.setStartTime(cursor.getLong(cursor.getColumnIndex("START_DATE")));
                bean.setEndTime(cursor.getLong(cursor.getColumnIndex("END_DATE")));
                bean.setStatus(cursor.getInt(cursor.getColumnIndex("STATUS")));
                bean.setException(cursor.getInt(cursor.getColumnIndex("EXCEPTION")));
                bean.setCompleted(cursor.getInt(cursor.getColumnIndex("COMPLETED")));
                bean.setNeedSync(cursor.getInt(cursor.getColumnIndex("NEED_SYNC")));
            }
            cursor.close();
        }
        return bean;
    }
}
