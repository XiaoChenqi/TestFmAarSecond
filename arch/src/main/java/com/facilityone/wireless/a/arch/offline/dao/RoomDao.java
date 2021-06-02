package com.facilityone.wireless.a.arch.offline.dao;

import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.offline.model.entity.LocationRoomEntity;
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
 * description:房间
 * Date: 2018/10/18 3:11 PM
 */
public class RoomDao {

    private final DBManager mDbManager;

    public RoomDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBLOCATIONROOM (" + //
                "ID INTEGER ," + // 0: id
                "CODE TEXT," + // 1: code
                "NAME TEXT," + // 2: name
                "FULL_NAME TEXT," + // 3: fullName
                "SORT_ID INTEGER," + // 4: sortId
                "FLOOR_ID INTEGER," + // 5: floorId
                "DELETED INTEGER," + // 6: deleted
                "PROJECT_ID INTEGER," + // 7: projectId
                "PRIMARY KEY (ID,PROJECT_ID));"
        );
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBLOCATIONROOM SELECT * FROM DBLOCATIONROOM_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBLOCATIONROOM";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBLOCATIONROOM_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBLOCATIONROOM RENAME TO DBLOCATIONROOM_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBLOCATIONROOM";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addRoom(List<LocationRoomEntity> floorEntities) {
        if (floorEntities == null || floorEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBLOCATIONROOM VALUES(?,?,?,?,?,?,?,?)";

            for (LocationRoomEntity roomEntity : floorEntities) {
                Object[] args = {
                        roomEntity.getRoomId()
                        , roomEntity.getCode()
                        , roomEntity.getName()
                        , roomEntity.getFullName()
                        , roomEntity.getSort()
                        , roomEntity.getFloorId()
                        , roomEntity.getDeleted()
                        , projectId};
                mDbManager.insert(args, sql);
            }

//            String delSql = "DELETE FROM DBLOCATIONROOM WHERE DELETED = ? AND PROJECT_ID = ?";
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

    public List<SelectDataBean> queryLocationRooms(Long parentId) {
        Long projectId = FM.getProjectId();
        List<SelectDataBean> temp = null;
        String sql = "SELECT * FROM DBLOCATIONROOM WHERE DBLOCATIONROOM.PROJECT_ID = ? AND DBLOCATIONROOM.FLOOR_ID = ? AND DELETED = 0  ORDER BY DBLOCATIONROOM.SORT_ID;";
        String[] queryArgs = {projectId + "", parentId + ""};
        Cursor cursor = mDbManager.query(queryArgs, sql);
        if (cursor != null) {
            temp = new ArrayList<>();
            while (cursor.moveToNext()) {
                SelectDataBean bean = new SelectDataBean();
                bean.setId(cursor.getLong(cursor.getColumnIndex("ID")));
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

    public String queryLocationName(Long roomId) {
        Long projectId = FM.getProjectId();
        String fullName = null;
        String sql = "SELECT * FROM DBLOCATIONROOM WHERE PROJECT_ID = ? AND  ID  = ? ";
        String[] queryArgs = {projectId + "", roomId + ""};
        Cursor cursor = mDbManager.query(queryArgs, sql);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                fullName = cursor.getString(cursor.getColumnIndex("FULL_NAME"));
            }
            cursor.close();
        }
        return fullName;
    }

    public List<SelectDataBean> queryAllLocationRooms() {
        Long projectId = FM.getProjectId();
        List<SelectDataBean> temp = null;
        String sql = "SELECT * FROM DBLOCATIONROOM WHERE PROJECT_ID = ? AND DELETED = 0 ORDER BY DBLOCATIONROOM.SORT_ID";
        String[] queryArgs = {projectId + ""};
        Cursor cursor = mDbManager.query(queryArgs, sql);
        if (cursor != null) {
            temp = new ArrayList<>();
            while (cursor.moveToNext()) {
                SelectDataBean bean = new SelectDataBean();
                bean.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setParentId(cursor.getLong(cursor.getColumnIndex("FLOOR_ID")));
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
