package com.facilityone.wireless.a.arch.offline.dao;

import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.basiclib.app.FM;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:数据库请求网络时间戳--巡检任务
 * Date: 2018/10/18 9:42 AM
 */
public class OfflinePatrolTimeDao {

    private final DBManager mDbManager;

    public OfflinePatrolTimeDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBOFFLINEPATROLTIME (" + //
                "EMID INTEGER ," + // 0: em id
                "TIME INTEGER ," + // 1: time
                "PROJECT_ID INTEGER ," +  // 1: projectId;
                "PRIMARY KEY (EMID,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBOFFLINEPATROLTIME SELECT * FROM DBOFFLINEPATROLTIME_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBOFFLINEPATROLTIME";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBOFFLINEPATROLTIME_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBOFFLINEPATROLTIME RENAME TO DBOFFLINEPATROLTIME_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBOFFLINEPATROLTIME";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addOfflineTime(Long time, Long emid) {
        if (time == null) {
            return false;
        }
        Long projectId = FM.getProjectId();
        String sql = "INSERT OR REPLACE INTO DBOFFLINEPATROLTIME VALUES(?,?,?)";
        Object[] args = {
                emid, time, projectId };
        return execSQL(args, sql);
    }

    public boolean updateOfflineTime(Long time, Long emid) {
        if (time == null) {
            return false;
        }
        Long projectId = FM.getProjectId();
        String sql = "UPDATE DBOFFLINEPATROLTIME SET TIME = ? WHERE PROJECT_ID = ? AND EMID = ? ;";
        Object[] args = {
                time, projectId, emid };
        return execSQL(args, sql);
    }

    public Long getOfflineTime(Long emid) {
        Long projectId = FM.getProjectId();
        if (projectId == null || emid == null) {
            return 0L;
        }
        String sql = "SELECT * FROM DBOFFLINEPATROLTIME WHERE PROJECT_ID = ? AND EMID = ? ;";
        String[] args = { projectId.toString(), emid + "" };
        Cursor cursor = mDbManager.query(args, sql);
        if (cursor.moveToFirst()) {
            return cursor.getLong(cursor.getColumnIndex("TIME"));
        }
        return 0L;
    }

    private boolean execSQL(Object[] args, String sql) {
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            mDbManager.execSQL(args, sql);
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
}
