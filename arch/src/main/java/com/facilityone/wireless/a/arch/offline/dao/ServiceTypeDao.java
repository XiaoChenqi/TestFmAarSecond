package com.facilityone.wireless.a.arch.offline.dao;

import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.offline.model.entity.ServiceTypeEntity;
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
 * description:服务类型数据库操作
 * Date: 2018/10/11 4:26 PM
 */
public class ServiceTypeDao {

    private final DBManager mDbManager;

    public ServiceTypeDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBSERVICETYPE (" + //
                "ID INTEGER ," + // 0: id
                "FULL_NAME TEXT," + // 1: fullName
                "NAME TEXT," + // 2: name
                "DELETED INTEGER," + // 3: deleted
                "SORT_ID INTEGER," + // 4: sortId
                "DESC TEXT," + // 5: desc
                "PARENT_ID  INTEGER," + // 6: parentId 
                "PROJECT_ID INTEGER," + // 7: projectId
                "PRIMARY KEY (ID,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBSERVICETYPE SELECT * FROM DBSERVICETYPE_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBSERVICETYPE";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBSERVICETYPE_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBSERVICETYPE RENAME TO DBSERVICETYPE_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBSERVICETYPE";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addServiceType(List<ServiceTypeEntity> serviceTypeEntities) {
        if (serviceTypeEntities == null || serviceTypeEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBSERVICETYPE VALUES(?,?,?,?,?,?,?,?)";

            for (ServiceTypeEntity serviceTypeEntity : serviceTypeEntities) {
                Object[] args = {
                        serviceTypeEntity.getServiceTypeId()
                        , serviceTypeEntity.getFullName()
                        , serviceTypeEntity.getName()
                        , serviceTypeEntity.getDeleted()
                        , serviceTypeEntity.getSort()
                        , serviceTypeEntity.getStypeDesc()
                        , serviceTypeEntity.getParentId()
                        , projectId };
                mDbManager.insert(args, sql);
            }

//            String delSql = "DELETE FROM DBSERVICETYPE WHERE DELETED = ? AND PROJECT_ID = ?";
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

    public List<SelectDataBean> queryServiceType() {
        Long projectId = FM.getProjectId();
        List<SelectDataBean> temp = null;
        String sql = "SELECT * FROM DBSERVICETYPE WHERE PROJECT_ID = ? AND DELETED = 0  ORDER BY SORT_ID;";
        String[] queryArgs = { projectId + "" };
        Cursor cursor = mDbManager.query(queryArgs, sql);
        if (cursor != null) {
            temp = new ArrayList<>();
            while (cursor.moveToNext()) {
                SelectDataBean bean = new SelectDataBean();
                bean.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setParentId(cursor.getLong(cursor.getColumnIndex("PARENT_ID")));
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setFullName(cursor.getString(cursor.getColumnIndex("FULL_NAME")));
                bean.setNamePinyin(PinyinUtils.ccs2Pinyin(bean.getName()));
                bean.setNameFirstLetters(PinyinUtils.getPinyinFirstLetters(bean.getName()));
                bean.setFullNamePinyin(PinyinUtils.ccs2Pinyin(bean.getFullName()));
                bean.setFullNameFirstLetters(PinyinUtils.getPinyinFirstLetters(bean.getFullName()));
                temp.add(bean);
            }
            cursor.close();
        }
        if (temp != null) {
            for (SelectDataBean bean : temp) {
                for (SelectDataBean selectDataBean : temp) {
                    if (bean.getId().equals(selectDataBean.getParentId())) {
                        bean.setHaveChild(true);
                        break;
                    }
                }
            }
        }
        return temp;
    }
}
