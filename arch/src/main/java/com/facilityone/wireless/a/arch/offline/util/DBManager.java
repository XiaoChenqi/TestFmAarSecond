package com.facilityone.wireless.a.arch.offline.util;

import android.content.ContentValues;

import com.facilityone.wireless.basiclib.app.FM;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/10/11 4:00 PM
 */
public class DBManager {

    private FMDBHelper mDbHelper;

    private SQLiteDatabase mDb;

    private DBManager() {
        mDbHelper = new FMDBHelper(FM.getApplication());
        mDb = mDbHelper.getWritableDatabase();
    }

    private static final class SingleHolder {
        private static final DBManager instance = new DBManager();
    }

    public static DBManager getInstance() {
        return SingleHolder.instance;
    }

    public void beginTransaction() {
        mDb.beginTransaction();
    }

    public void endTransaction() {
        mDb.endTransaction();
    }

    public void setTransactionSuccessful() {
        mDb.setTransactionSuccessful();
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

    /**
     * String sql = "INSERT OR REPLACE INTO DBOFFLINETIME VALUES(?,?)";
     * Object[] args = {time, projectId };
     *
     * @param args
     * @param sql
     */
    public void insert(Object[] args, String sql) {
        mDb.execSQL(sql, args);
    }

    /**
     * 实例化常量值
     * ContentValues cValue = new ContentValues();
     * 添加用户名
     * cValue.put("sname","xiaoming");
     * 添加密码
     * cValue.put("snumber","01005");
     *
     * @param values    ContentValues
     * @param tableName 表
     */
    public void insert(ContentValues values, String tableName) {
        mDb.insert(tableName, null, values);
    }

    /**
     * @param args Object[] args = {6};
     * @param sql  String sql = "delete from stu_table where _id = ?";
     */
    public void delete(Object[] args, String sql) {
        mDb.execSQL(sql, args);
    }

    /**
     * @param whereClause 删除条件 String whereClause = "id=?";
     * @param whereArgs   删除条件参数 String[] whereArgs = {String.valueOf(2)};
     * @param tableName   需要删除数据的表
     */
    public void delete(String whereClause, String[] whereArgs, String tableName) {
        mDb.delete(tableName, whereClause, whereArgs);
    }

    /**
     * @param args Object[] args = {654321,1};
     * @param sql  String sql = "update stu_table set snumber = ? where id = ?";
     */
    public void update(Object[] args, String sql) {
        mDb.execSQL(sql, args);
    }

    public void update(ContentValues values, String whereClause, String[] whereArgs, String tableName) {
        mDb.update(tableName, values, whereClause, whereArgs);
    }

    public Cursor query(String[] args, String sql) {
        return mDb.rawQuery(sql, args);
    }

    public void execSQL(Object[] args, String sql) {
        mDb.execSQL(sql, args);
    }
}
