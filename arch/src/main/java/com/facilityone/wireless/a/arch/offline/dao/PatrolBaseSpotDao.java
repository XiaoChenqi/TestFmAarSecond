package com.facilityone.wireless.a.arch.offline.dao;

import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolBaseSpotEntity;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolSpotEntity;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.a.arch.offline.util.LocationNullUtils;
import com.facilityone.wireless.basiclib.app.FM;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检离线基础数据
 * Date: 2018/10/30 4:56 PM
 */
public class PatrolBaseSpotDao {

    private final DBManager mDbManager;

    public PatrolBaseSpotDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBPATROLBASESPOT (" + //
                "ID INTEGER  ," + // 0: id
                "NAME TEXT," + // 1: name
                "CODE TEXT," + // 2: code
                "QR_CODE TEXT," + // 3: qrCode
                "NFC_TAG TEXT," + // 4: nfcTag
                "SPOT_TYPE TEXT," + // 5: spotType
                "SPOT_LOCATION TEXT," + // 6: spotLocation
                "CITY_ID INTEGER," + // 7: cityId
                "SITE_ID INTEGER," + // 8: siteId
                "BUILDING_ID INTEGER," + // 9: buildingId
                "FLOOR_ID INTEGER," + // 10: floorId
                "ROOM_ID INTEGER," + // 11: roomId
                "DELETED INTEGER," + // 12: deleted
                "PROJECT_ID INTEGER ," + // 13: projectId
                "PRIMARY KEY (ID,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBPATROLBASESPOT SELECT * FROM DBPATROLBASESPOT_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLBASESPOT";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLBASESPOT_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBPATROLBASESPOT RENAME TO DBPATROLBASESPOT_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBPATROLBASESPOT";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addPatrolBaseSpot(List<PatrolBaseSpotEntity> patrolBaseSpotEntities) {
        if (patrolBaseSpotEntities == null || patrolBaseSpotEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBPATROLBASESPOT VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            for (PatrolBaseSpotEntity spotEntity : patrolBaseSpotEntities) {
                Object[] args = {
                        spotEntity.getSpotId()
                        , spotEntity.getName()
                        , spotEntity.getCode()
                        , spotEntity.getQrCode()
                        , spotEntity.getNfcTag()
                        , spotEntity.getSpotType()
                        , spotEntity.getSpotLocation()
                        , spotEntity.getLocation() == null ? null : spotEntity.getLocation().cityId
                        , spotEntity.getLocation() == null ? null : spotEntity.getLocation().siteId
                        , spotEntity.getLocation() == null ? null : spotEntity.getLocation().buildingId
                        , spotEntity.getLocation() == null ? null : spotEntity.getLocation().floorId
                        , spotEntity.getLocation() == null ? null : spotEntity.getLocation().roomId
                        , spotEntity.getDeleted()
                        , projectId };
                mDbManager.insert(args, sql);
            }

//            String delSql = "DELETE FROM DBPATROLBASESPOT WHERE DELETED = ? AND PROJECT_ID = ?";
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

    public PatrolSpotEntity getSpot(Long spotId) {
        Long projectId = FM.getProjectId();
        String sql = "select * from DBPATROLBASESPOT where id = ? and project_id = ?";
        String[] args = { spotId + "", projectId + "" };
        Cursor cursor = mDbManager.query(args, sql);
        PatrolSpotEntity bean = null;
        if (cursor != null) {
            if (cursor.moveToNext()) {
                bean = new PatrolSpotEntity();
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setCode(cursor.getString(cursor.getColumnIndex("CODE")));
                bean.setLocationName(cursor.getString(cursor.getColumnIndex("SPOT_LOCATION")));
                LocationBean locationBean = LocationNullUtils.getNullLocation(cursor);
                bean.setLocation(locationBean);
            }
            cursor.close();
        }

        return bean;
    }
}
