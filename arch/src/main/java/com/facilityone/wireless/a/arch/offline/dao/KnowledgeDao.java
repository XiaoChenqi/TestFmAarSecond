package com.facilityone.wireless.a.arch.offline.dao;


import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.offline.model.entity.KnowledgeEntity;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.PinyinUtils;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: owen.
 * Date: on 2018/12/17 下午6:03.
 * Description:
 * email:
 */

public class KnowledgeDao {
    private DBManager mDbManager;

    public KnowledgeDao() {
        mDbManager = DBManager.getInstance();
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBKNOWLEDGE (" + //
                "TYPE_ID INTEGER ," + // 0: typeId
                "DELETED INTEGER," + // 1: deleted
                "CODE TEXT," + // 2: code
                "NAME TEXT," + // 3: name
                "FULL_NAME TEXT," + // 4: fullName
                "IMAGE_NAME TEXT," + // 5: imageName
                "DESC TEXT," + // 6: desc
                "PARENT_TYPE_ID INTEGER," + // 7: parentTypeId
                "CLASSIFY INTEGER," + // 8: classify
                "SORT INTEGER," + // 9: sort
                "PROJECT_ID INTEGER," + // 10 projectId
                "PRIMARY KEY (TYPE_ID));");

    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBKNOWLEDGE SELECT * FROM DBKNOWLEDGE_TEMP";
        db.execSQL(sql);
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBKNOWLEDGE";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBKNOWLEDGE_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBKNOWLEDGE RENAME TO DBKNOWLEDGE_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBKNOWLEDGE";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addKnowledge(List<KnowledgeEntity> knoEntities) {
        if (knoEntities == null || knoEntities.size() == 0) {
            return true;
        }
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBKNOWLEDGE VALUES(?,?,?,?,?,?,?,?,?,?,?)";

            for (KnowledgeEntity depEntity : knoEntities) {
                Object[] args = {
                        depEntity.getTypeId()
                        , depEntity.getDeleted()
                        , depEntity.getCode()
                        , depEntity.getName()
                        , depEntity.getFullName()
                        , depEntity.getImageName()
                        , depEntity.getDesc()
                        , depEntity.getParentTypeId()
                        , depEntity.getClassify()
                        , depEntity.getSort()
                        , depEntity.getProjectId()};
                mDbManager.insert(args, sql);
            }

            String delSql = "DELETE FROM DBKNOWLEDGE WHERE DELETED = ? ";
            Object[] delArgs = {true};
            mDbManager.delete(delArgs, delSql);

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


    public List<KnowledgeEntity> queryKnowledgeByProject(int classify, boolean isSystem) {
        Long projectId = FM.getProjectId();
        List<KnowledgeEntity> temp = null;
        String sql = "";
        List<String> queryArgs = new ArrayList<>();
        if (isSystem) {
            sql = "SELECT * FROM DBKNOWLEDGE WHERE CLASSIFY = ? AND PARENT_TYPE_ID = -1 ORDER BY SORT;";
            queryArgs.add(classify + "");

        } else {
            sql = "SELECT * FROM DBKNOWLEDGE WHERE PROJECT_ID = ? AND CLASSIFY = ? AND PARENT_TYPE_ID = -1  ORDER BY SORT;";
            queryArgs.add(projectId + "");
            queryArgs.add(classify + "");
        }
        Cursor cursor = mDbManager.query(queryArgs.toArray(new String[queryArgs.size()]), sql);
        if (cursor != null) {
            temp = new ArrayList<>();
            while (cursor.moveToNext()) {
                KnowledgeEntity bean = new KnowledgeEntity();
                bean.setTypeId(cursor.getLong(cursor.getColumnIndex("TYPE_ID")));
                bean.setClassify(cursor.getInt(cursor.getColumnIndex("CLASSIFY")));
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setImageName(cursor.getString(cursor.getColumnIndex("IMAGE_NAME")));
                temp.add(bean);
            }
            cursor.close();
        }
        return temp;
    }

    public List<SelectDataBean> queryknowledgeType() {
        List<SelectDataBean> temp = null;
        String sql = "SELECT * FROM DBKNOWLEDGE WHERE DELETED = 0 ORDER BY SORT;";
        Cursor cursor = mDbManager.query(null, sql);
        if (cursor != null) {
            temp = new ArrayList<>();
            while (cursor.moveToNext()) {
                SelectDataBean bean = new SelectDataBean();
                bean.setId(cursor.getLong(cursor.getColumnIndex("TYPE_ID")));
                bean.setParentId(cursor.getLong(cursor.getColumnIndex("PARENT_TYPE_ID")));
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
