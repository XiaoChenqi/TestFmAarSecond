package com.facilityone.wireless.a.arch.offline.dao;

import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.offline.model.entity.EquTypeEntity;
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
 * description:设备类型
 * Date: 2018/10/18 12:33 PM
 */
public class EquTypeDao {
    private final DBManager mDbManager;

    public EquTypeDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBEQUTYPE (" + //
                "ID INTEGER," + // 0: id
                "CODE TEXT," + // 1: code
                "DESC TEXT," + // 2: desc
                "FULL_NAME TEXT," + // 3: fullName
                "NAME TEXT," + // 4: name
                "LEVEL INTEGER," + // 5: level
                "DELETED INTEGER," + // 6: deleted
                "PARENT_ID INTEGER," + // 7: parentId
                "PROJECT_ID INTEGER," +// 8: projectId
                "PRIMARY KEY (ID,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBEQUTYPE SELECT * FROM DBEQUTYPE_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBEQUTYPE";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBEQUTYPE_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBEQUTYPE RENAME TO DBEQUTYPE_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBEQUTYPE";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addEquType(List<EquTypeEntity> equTypeEntities) {
        if (equTypeEntities == null || equTypeEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBEQUTYPE VALUES(?,?,?,?,?,?,?,?,?)";

            for (EquTypeEntity equTypeEntity : equTypeEntities) {
                Object[] args = {
                        equTypeEntity.getEquSysId()
                        , equTypeEntity.getEquSysCode()
                        , equTypeEntity.getEquSysDescription()
                        , equTypeEntity.getEquSysFullName()
                        , equTypeEntity.getEquSysName()
                        , equTypeEntity.getLevel()
                        , equTypeEntity.getDeleted()
                        , equTypeEntity.getEquSysParentSystemId()
                        , projectId };
                mDbManager.insert(args, sql);
            }

//            String delSql = "DELETE FROM DBEQUTYPE WHERE DELETED = ? AND PROJECT_ID = ?";
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

    public List<SelectDataBean> queryEquType() {
        Long projectId = FM.getProjectId();
        List<SelectDataBean> temp = null;
        String sql = "SELECT * FROM DBEQUTYPE WHERE PROJECT_ID = ? AND DELETED = 0  ORDER BY ID ASC ;";
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
