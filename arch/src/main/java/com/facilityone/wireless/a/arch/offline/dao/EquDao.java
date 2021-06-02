package com.facilityone.wireless.a.arch.offline.dao;

import com.facilityone.wireless.a.arch.ec.module.EquBean;
import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.offline.model.entity.EquEntity;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.a.arch.offline.util.LocationNullUtils;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.PinyinUtils;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:设备
 * Date: 2018/10/18 3:11 PM
 */
public class EquDao {

    private final DBManager mDbManager;

    public EquDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBEQU (" + //
                "ID INTEGER," + // 0: id
                "CODE TEXT," + // 1: code
                "NAME TEXT," + // 2: name
                "QRCODE TEXT," + // 3: qrcode
                "DELETED INTEGER," + // 4: deleted
                "EQU_TYPE_ID INTEGER," + // 5: equ_type_id
                "CITY_ID INTEGER," + // 6: cityId
                "SITE_ID INTEGER," + // 7: siteId
                "BUILDING_ID INTEGER," + // 8: buildingId
                "FLOOR_ID INTEGER," + // 9: floorId
                "ROOM_ID INTEGER," + // 10: roomId
                "PROJECT_ID INTEGER," + // 11: project_id
                "PRIMARY KEY (ID,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBEQU SELECT * FROM DBEQU_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBEQU";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBEQU_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBEQU RENAME TO DBEQU_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBEQU";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addEqu(List<EquEntity> equEntities) {
        if (equEntities == null || equEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBEQU VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

            for (EquEntity equEntity : equEntities) {
                Object[] args = {
                        equEntity.getEqId()
                        , equEntity.getCode()
                        , equEntity.getName()
                        , equEntity.getQrcode()
                        , equEntity.getDeleted()
                        , equEntity.getEquSystem()
                        , equEntity.getPosition() == null ? null : equEntity.getPosition().cityId
                        , equEntity.getPosition() == null ? null : equEntity.getPosition().siteId
                        , equEntity.getPosition() == null ? null : equEntity.getPosition().buildingId
                        , equEntity.getPosition() == null ? null : equEntity.getPosition().floorId
                        , equEntity.getPosition() == null ? null : equEntity.getPosition().roomId
                        , projectId };
                mDbManager.insert(args, sql);
            }

//            String delSql = "DELETE FROM DBEQU WHERE DELETED = ? AND PROJECT_ID = ?";
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

    public List<SelectDataBean> queryEqus(LocationBean locationBean) {
        Long projectId = FM.getProjectId();
        List<String> args = new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT * FROM DBEQU WHERE PROJECT_ID = ? AND DELETED = 0 ");
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
        }

        if (locationBean.buildingId != null) {
            sb.append(" AND BUILDING_ID = ? ");
            args.add(locationBean.buildingId + "");
        }

        if (locationBean.floorId != null) {
            sb.append(" AND FLOOR_ID = ? ");
            args.add(locationBean.floorId + "");
        }

        if (locationBean.roomId != null) {
            sb.append(" AND ROOM_ID = ? ");
            args.add(locationBean.roomId + "");
        }

        sb.append("ORDER BY ID;");

        Cursor cursor = mDbManager.query(args.toArray(new String[args.size()]), sb.toString());
        List<SelectDataBean> temp = null;
        if (cursor != null) {
            temp = new ArrayList<>();
            while (cursor.moveToNext()) {
                SelectDataBean bean = new SelectDataBean();
                bean.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setFullName(cursor.getString(cursor.getColumnIndex("CODE")));
                LocationBean location = LocationNullUtils.getNullLocation(cursor);
                bean.setLocation(location);
                bean.setHaveChild(false);
                bean.setNamePinyin(PinyinUtils.ccs2Pinyin(bean.getName()));
                bean.setNameFirstLetters(PinyinUtils.getPinyinFirstLetters(bean.getName()));
                bean.setFullNamePinyin(PinyinUtils.ccs2Pinyin(bean.getFullName()));
                bean.setFullNameFirstLetters(PinyinUtils.getPinyinFirstLetters(bean.getFullName()));
                temp.add(bean);
            }
            cursor.close();
        }

        return temp;
    }

    public List<SelectDataBean> queryEqus() {
        Long projectId = FM.getProjectId();
        String[] args = new String[]{ projectId + "" };
        String sql = "SELECT * FROM DBEQU WHERE PROJECT_ID = ? AND DELETED = 0 ORDER BY ID;";
        Cursor cursor = mDbManager.query(args, sql);
        List<SelectDataBean> temp = null;
        if (cursor != null) {
            temp = new ArrayList<>();
            while (cursor.moveToNext()) {
                SelectDataBean bean = new SelectDataBean();
                bean.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setFullName(cursor.getString(cursor.getColumnIndex("CODE")));
                LocationBean location = LocationNullUtils.getNullLocation(cursor);
                bean.setLocation(location);
                bean.setHaveChild(false);
                bean.setNamePinyin(PinyinUtils.ccs2Pinyin(bean.getName()));
                bean.setNameFirstLetters(PinyinUtils.getPinyinFirstLetters(bean.getName()));
                bean.setFullNamePinyin(PinyinUtils.ccs2Pinyin(bean.getFullName()));
                bean.setFullNameFirstLetters(PinyinUtils.getPinyinFirstLetters(bean.getFullName()));
                temp.add(bean);
            }
            cursor.close();
        }

        return temp;
    }

    public SelectDataBean queryEquById(Long equipmentId) {

        SelectDataBean bean = null;
        if (equipmentId != null) {
            Long projectId = FM.getProjectId();
            String sql = "SELECT * FROM DBEQU WHERE PROJECT_ID = ? AND ID = ?;";
            String[] args = { projectId + "", equipmentId + "" };

            Cursor cursor = mDbManager.query(args, sql);

            if (cursor != null && cursor.moveToNext()) {
                bean = new SelectDataBean();
                bean.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setFullName(cursor.getString(cursor.getColumnIndex("CODE")));
                LocationBean location = LocationNullUtils.getNullLocation(cursor);
                bean.setLocation(location);
                bean.setHaveChild(false);
                cursor.close();
            }
        }

        return bean;
    }

    public EquBean getDevice(Long eqId) {
        Long projectId = FM.getProjectId();
        String sql = "SELECT D.ID,D.CODE,D.NAME,D.EQU_TYPE_ID,D.CITY_ID,D.SITE_ID,D.BUILDING_ID,D.FLOOR_ID,D.ROOM_ID,T.NAME AS TYPE_NAME,T.FULL_NAME FROM DBEQU AS D LEFT JOIN DBEQUTYPE AS T ON D.EQU_TYPE_ID = T.ID AND D.PROJECT_ID = T.PROJECT_ID  WHERE D.PROJECT_ID = ? AND D.ID = ?;";
        String[] args = { projectId + "", eqId + "" };

        EquBean bean = null;
        Cursor cursor = mDbManager.query(args, sql);
        if (cursor != null && cursor.moveToNext()) {
            bean = new EquBean();
            bean.id = cursor.getLong(cursor.getColumnIndex("ID"));
            bean.name = cursor.getString(cursor.getColumnIndex("NAME"));
            bean.typeName = cursor.getString(cursor.getColumnIndex("TYPE_NAME"));
            bean.typeFullName = cursor.getString(cursor.getColumnIndex("FULL_NAME"));
            bean.code = cursor.getString(cursor.getColumnIndex("CODE"));
            bean.typeId = cursor.getLong(cursor.getColumnIndex("EQU_TYPE_ID"));
            bean.locationBean = LocationNullUtils.getNullLocation(cursor);
            cursor.close();
        }
        return bean;
    }

}
