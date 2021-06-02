package com.facilityone.wireless.a.arch.offline.dao;

import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.offline.model.entity.LocationSiteEntity;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.PinyinUtils;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:区域操作
 * Date: 2018/10/18 3:11 PM
 */
public class SiteDao {

    private final DBManager mDbManager;

    public SiteDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBLOCATIONSITE (" + //
                "ID INTEGER," + // 0: id
                "CODE TEXT," + // 1: code
                "NAME TEXT," + // 2: name
                "FULL_NAME TEXT," + // 3: fullName
                "SORT_ID INTEGER," + // 4: sortId
                "CITY_ID INTEGER," + // 5: cityId
                "DELETED INTEGER," + // 6: deleted
                "PROJECT_ID INTEGER," + // 7: projectId
                "PRIMARY KEY (ID,PROJECT_ID));"
        );

    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBLOCATIONSITE SELECT * FROM DBLOCATIONSITE_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBLOCATIONSITE";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBLOCATIONSITE_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBLOCATIONSITE RENAME TO DBLOCATIONSITE_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBLOCATIONSITE";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addSite(List<LocationSiteEntity> siteEntities) {
        if (siteEntities == null || siteEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBLOCATIONSITE VALUES(?,?,?,?,?,?,?,?)";

            for (LocationSiteEntity siteEntity : siteEntities) {
                Object[] args = {
                        siteEntity.getSiteId()
                        , siteEntity.getCode()
                        , siteEntity.getName()
                        , siteEntity.getName()
                        , siteEntity.getSort()
                        , siteEntity.getCityId()
                        , siteEntity.getDeleted()
                        , projectId };
                mDbManager.insert(args, sql);
            }

//            String delSql = "DELETE FROM DBLOCATIONSITE WHERE DELETED = ? AND PROJECT_ID = ?";
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

    public List<SelectDataBean> queryLocationSites() {
        return queryLocationSites(false);
    }

    public List<SelectDataBean> queryLocationSites(boolean needFilterDelete) {
        Long projectId = FM.getProjectId();
        List<SelectDataBean> temp = null;
        String sql;
        String[] queryArgs = { projectId + "" };
        if (needFilterDelete) {
            sql = "SELECT DBLOCATIONSITE.ID,DBLOCATIONSITE.NAME,DBLOCATIONSITE.FULL_NAME,DBLOCATIONBUILDING.ID AS BUILDING FROM DBLOCATIONSITE LEFT JOIN DBLOCATIONBUILDING ON DBLOCATIONSITE.ID = DBLOCATIONBUILDING.SITE_ID AND DBLOCATIONSITE.PROJECT_ID = DBLOCATIONBUILDING.PROJECT_ID WHERE  DBLOCATIONSITE.PROJECT_ID = DBLOCATIONBUILDING.PROJECT_ID AND DBLOCATIONSITE.PROJECT_ID = ? AND DBLOCATIONSITE.DELETED = 0  GROUP BY DBLOCATIONSITE.ID ORDER BY DBLOCATIONSITE.SORT_ID";
        } else {
            sql = "SELECT DBLOCATIONSITE.ID,DBLOCATIONSITE.NAME,DBLOCATIONSITE.FULL_NAME,DBLOCATIONBUILDING.ID AS BUILDING FROM DBLOCATIONSITE LEFT JOIN DBLOCATIONBUILDING ON DBLOCATIONSITE.ID = DBLOCATIONBUILDING.SITE_ID AND DBLOCATIONSITE.PROJECT_ID = DBLOCATIONBUILDING.PROJECT_ID WHERE  DBLOCATIONSITE.PROJECT_ID = DBLOCATIONBUILDING.PROJECT_ID AND DBLOCATIONSITE.PROJECT_ID = ? GROUP BY DBLOCATIONSITE.ID ORDER BY DBLOCATIONSITE.SORT_ID";
        }
        Cursor cursor = mDbManager.query(queryArgs, sql);
        if (cursor != null) {
            temp = new ArrayList<>();
            while (cursor.moveToNext()) {
                SelectDataBean bean = new SelectDataBean();
                bean.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setFullName(cursor.getString(cursor.getColumnIndex("FULL_NAME")));
                Long building = cursor.getLong(cursor.getColumnIndex("BUILDING"));
                bean.setHaveChild(building != 0);
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

    public String queryLocationName(Long siteId) {
        Long projectId = FM.getProjectId();
        String fullName = null;
        String sql = "SELECT * FROM DBLOCATIONSITE WHERE PROJECT_ID = ? AND  ID  = ? ";
        String[] queryArgs = { projectId + "", siteId + "" };
        Cursor cursor = mDbManager.query(queryArgs, sql);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                fullName = cursor.getString(cursor.getColumnIndex("FULL_NAME"));
            }
            cursor.close();
        }
        return fullName;
    }


    public List<SelectDataBean> queryAllLocationSites() {
        Long projectId = FM.getProjectId();
        List<SelectDataBean> temp = null;
        String sql = "SELECT * FROM DBLOCATIONSITE WHERE PROJECT_ID = ? AND DELETED = 0  ORDER BY DBLOCATIONSITE.SORT_ID";
        String[] queryArgs = { projectId + "" };
        Cursor cursor = mDbManager.query(queryArgs, sql);
        if (cursor != null) {
            temp = new ArrayList<>();
            while (cursor.moveToNext()) {
                SelectDataBean bean = new SelectDataBean();
                bean.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setParentId(cursor.getLong(cursor.getColumnIndex("CITY_ID")));
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setFullName(cursor.getString(cursor.getColumnIndex("FULL_NAME")));
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
}
