package com.facilityone.wireless.a.arch.offline.dao;

import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.basiclib.app.FM;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:数据库请求网络时间戳
 * Date: 2018/10/18 9:42 AM
 */
public class OfflineTimeDao {

    public static final int TYPE_OFFLINE_BASE = 1;//基础离线数据
    public static final int TYPE_OFFLINE_PATROL = 2;//巡检离线数据
    public static final int TYPE_KNOWLEDGE = 3;//获取知识库
    public static final int TYPE_OFFLINE_EQU = 4;//设备相关（巡检设备需要和离线数据同步时间戳）

    private final DBManager mDbManager;

    public OfflineTimeDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBOFFLINETIME (" + //
                "TYPE INTEGER ," + // 0: type
                "TIME INTEGER ," + // 1: time
                "PROJECT_ID INTEGER ," +  // 1: projectId;
                "PRIMARY KEY (TYPE,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBOFFLINETIME SELECT * FROM DBOFFLINETIME_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBOFFLINETIME";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBOFFLINETIME_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBOFFLINETIME RENAME TO DBOFFLINETIME_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData(int type) {
        String sql = "delete from DBOFFLINETIME WHERE TYPE = " + type;
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addOfflineTime(Long time, int type) {
        if (time == null) {
            return false;
        }
        Long projectId = FM.getProjectId();
        String sql = "INSERT OR REPLACE INTO DBOFFLINETIME VALUES(?,?,?)";
        Object[] args = {
                type, time, projectId };
        return execSQL(args, sql);
    }

    public boolean updateOfflineTime(Long time, int type) {
        if (time == null) {
            return false;
        }
        Long projectId = FM.getProjectId();
        String sql = "UPDATE DBOFFLINETIME SET TIME = ? WHERE PROJECT_ID = ? AND TYPE = ? ;";
        Object[] args = {
                time, projectId, type };
        return execSQL(args, sql);
    }

    public Long getOfflineTime(int type) {
        Long projectId = FM.getProjectId();
        if (projectId == null) {
            return 0L;
        }
        String sql = "SELECT * FROM DBOFFLINETIME WHERE PROJECT_ID = ? AND TYPE = ? ;";
        String[] args = { projectId.toString(), type + "" };
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
