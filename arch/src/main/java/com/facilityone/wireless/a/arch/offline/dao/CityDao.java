package com.facilityone.wireless.a.arch.offline.dao;

import com.facilityone.wireless.a.arch.offline.model.entity.LocationCityEntity;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.basiclib.app.FM;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:城市操作
 * Date: 2018/10/18 3:11 PM
 */
public class CityDao {

    private final DBManager mDbManager;

    public CityDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBLOCATIONCITY (" + //
                "ID INTEGER," + // 0: id
                "CODE TEXT," + // 1: code
                "NAME TEXT," + // 2: name
                "FULL_NAME TEXT," + // 3: fullName
                "TIME_ZONE INTEGER," + // 4: time_zone
                "SORT INTEGER," + // 5: sort
                "DELETED INTEGER," + // 6: deleted
                "PROJECT_ID INTEGER," + // 7: projectId
                "PRIMARY KEY (ID,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBLOCATIONCITY SELECT * FROM DBLOCATIONCITY_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBLOCATIONCITY";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBLOCATIONCITY_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBLOCATIONCITY RENAME TO DBLOCATIONCITY_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBLOCATIONCITY";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addCity(List<LocationCityEntity> cityEntities) {
        if (cityEntities == null || cityEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBLOCATIONCITY VALUES(?,?,?,?,?,?,?,?)";

            for (LocationCityEntity cityEntity : cityEntities) {
                Object[] args = {
                        cityEntity.getCityId()
                        , cityEntity.getCode()
                        , cityEntity.getName()
                        , cityEntity.getName()
                        , cityEntity.getTimezone()
                        , cityEntity.getSort()
                        , cityEntity.getDeleted()
                        , projectId};
                mDbManager.insert(args, sql);
            }

//            String delSql = "DELETE FROM DBLOCATIONCITY WHERE DELETED = ? AND PROJECT_ID = ?";
//            Object[] delArgs = {true, projectId};
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

    public String queryLocationName(Long cityId) {
        Long projectId = FM.getProjectId();
        String fullName = null;
        String sql = "SELECT * FROM DBLOCATIONCITY WHERE PROJECT_ID = ? AND  ID  = ? ";
        String[] queryArgs = {projectId + "", cityId + ""};
        Cursor cursor = mDbManager.query(queryArgs, sql);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                fullName = cursor.getString(cursor.getColumnIndex("FULL_NAME"));
            }
            cursor.close();
        }
        return fullName;
    }
}
