package com.facilityone.wireless.a.arch.offline.dao;

import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.offline.model.entity.LocationBuildingEntity;
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
 * description:楼区操作
 * Date: 2018/10/18 3:11 PM
 */
public class BuildingDao {

    private final DBManager mDbManager;

    public BuildingDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBLOCATIONBUILDING (" + //
                "ID INTEGER ," + // 0: id
                "CODE TEXT," + // 1: code
                "NAME TEXT," + // 2: name
                "FULL_NAME TEXT," + // 3: fullName
                "SORT_ID INTEGER," + // 4: sortId
                "SITE_ID INTEGER," + // 5: siteId
                "DELETED INTEGER," + // 6: deleted
                "PROJECT_ID INTEGER," + // 7: projectId
                "PRIMARY KEY (ID,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBLOCATIONBUILDING SELECT * FROM DBLOCATIONBUILDING_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBLOCATIONBUILDING";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBLOCATIONBUILDING_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBLOCATIONBUILDING RENAME TO DBLOCATIONBUILDING_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBLOCATIONBUILDING";
        DBManager.getInstance().delete(null, sql);
    }


    public boolean addSite(List<LocationBuildingEntity> buildingEntities) {
        if (buildingEntities == null || buildingEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBLOCATIONBUILDING VALUES(?,?,?,?,?,?,?,?)";

            for (LocationBuildingEntity buildingEntity : buildingEntities) {
                Object[] args = {
                        buildingEntity.getBuildingId()
                        , buildingEntity.getCode()
                        , buildingEntity.getName()
                        , buildingEntity.getFullName()
                        , buildingEntity.getSort()
                        , buildingEntity.getSiteId()
                        , buildingEntity.getDeleted()
                        , projectId};
                mDbManager.insert(args, sql);
            }

//            String delSql = "DELETE FROM DBLOCATIONBUILDING WHERE DELETED = ? AND PROJECT_ID = ?";
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

    public List<SelectDataBean> queryLocationBuildings(Long parentId) {
        Long projectId = FM.getProjectId();
        List<SelectDataBean> temp = null;
        String sql = "";
        String[] queryArgs = null;
        if (parentId == -1L) {
            sql = "SELECT  DBLOCATIONBUILDING.ID,DBLOCATIONBUILDING.NAME,DBLOCATIONBUILDING.FULL_NAME,DBLOCATIONFLOOR.ID AS FLOOR  FROM DBLOCATIONBUILDING LEFT JOIN DBLOCATIONFLOOR ON DBLOCATIONFLOOR.BUILDING_ID = DBLOCATIONBUILDING.ID AND DBLOCATIONFLOOR.PROJECT_ID = DBLOCATIONBUILDING.PROJECT_ID WHERE  DBLOCATIONBUILDING.PROJECT_ID = ? AND DBLOCATIONBUILDING.DELETED = 0 GROUP BY DBLOCATIONBUILDING.ID ORDER BY DBLOCATIONBUILDING.SORT_ID;";
            queryArgs = new String[]{projectId + ""};
        } else {
            queryArgs = new String[]{projectId + "", parentId + ""};
            sql = "SELECT  DBLOCATIONBUILDING.ID,DBLOCATIONBUILDING.NAME,DBLOCATIONBUILDING.FULL_NAME,DBLOCATIONFLOOR.ID AS FLOOR  FROM DBLOCATIONBUILDING LEFT JOIN DBLOCATIONFLOOR ON DBLOCATIONFLOOR.BUILDING_ID = DBLOCATIONBUILDING.ID AND DBLOCATIONFLOOR.PROJECT_ID = DBLOCATIONBUILDING.PROJECT_ID WHERE  DBLOCATIONBUILDING.PROJECT_ID = ? AND DBLOCATIONBUILDING.DELETED = 0  AND DBLOCATIONBUILDING.SITE_ID = ? GROUP BY DBLOCATIONBUILDING.ID ORDER BY DBLOCATIONBUILDING.SORT_ID;";
        }
        Cursor cursor = mDbManager.query(queryArgs, sql);
        if (cursor != null) {
            temp = new ArrayList<>();
            while (cursor.moveToNext()) {
                SelectDataBean bean = new SelectDataBean();
                bean.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setFullName(cursor.getString(cursor.getColumnIndex("FULL_NAME")));
                Long floor = cursor.getLong(cursor.getColumnIndex("FLOOR"));
                bean.setHaveChild(floor != 0);
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

    public String queryLocationName(Long buildingId) {
        Long projectId = FM.getProjectId();
        String fullName = null;
        String sql = "SELECT * FROM DBLOCATIONBUILDING WHERE PROJECT_ID = ? AND  ID  = ? ";
        String[] queryArgs = {projectId + "", buildingId + ""};
        Cursor cursor = mDbManager.query(queryArgs, sql);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                fullName = cursor.getString(cursor.getColumnIndex("FULL_NAME"));
            }
            cursor.close();
        }
        return fullName;
    }

    public List<SelectDataBean> queryAllLocationBuildings() {
        Long projectId = FM.getProjectId();
        List<SelectDataBean> temp = null;
        String sql = "select * from DBLOCATIONBUILDING WHERE PROJECT_ID = ? AND DELETED = 0  ORDER BY DBLOCATIONBUILDING.SORT_ID";
        String[] queryArgs = {projectId + ""};
        Cursor cursor = DBManager.getInstance().query(queryArgs, sql);
        if (cursor != null) {
            temp = new ArrayList<>();
            while (cursor.moveToNext()) {
                SelectDataBean bean = new SelectDataBean();
                bean.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setParentId(cursor.getLong(cursor.getColumnIndex("SITE_ID")));
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
