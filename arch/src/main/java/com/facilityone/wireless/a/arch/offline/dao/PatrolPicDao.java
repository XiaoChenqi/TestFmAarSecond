package com.facilityone.wireless.a.arch.offline.dao;

import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolPicEntity;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.basiclib.app.FM;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检检查项图片
 * Date: 2018/11/13 9:40 AM
 */
public class PatrolPicDao {
    private final DBManager mDbManager;

    public PatrolPicDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBPATROLPIC (" + //
                "ID INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: auto id
                "PATH TEXT," + // 1: url
                "TASK_ID INTEGER," + // 2: task Id
                "ITEM_ID INTEGER," + // 3: checkContentId
                "NEED_SYNC INTEGER," + // 4: need_sync
                "PICTURE_ID INTEGER," + // 5: pictureId
                "USER_ID INTEGER," + // 6: userId
                "PROJECT_ID INTEGER)");// 7: projectId
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBPATROLPIC SELECT * FROM DBPATROLPIC_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLPIC";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLPIC_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBPATROLPIC RENAME TO DBPATROLPIC_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBPATROLPIC";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addItemPic(List<PatrolPicEntity> picEntities) {
        if (picEntities == null || picEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBPATROLPIC VALUES(?,?,?,?,?,?,?,?)";

            for (PatrolPicEntity entity : picEntities) {
                Object[] args = {
                        null
                        , entity.getPath()
                        , entity.getTaskId()
                        , entity.getItemId()
                        , true
                        , null
                        , userId
                        , projectId };
                mDbManager.insert(args, sql);
            }

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

    public void deletePic(List<Long> deletedId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<Object> args = new ArrayList<>();
        args.add(projectId);
        args.add(userId);
        StringBuilder sb = new StringBuilder();
        sb.append("delete from DBPATROLPIC WHERE PROJECT_ID = ? AND USER_ID = ? AND  TASK_ID IN (");
        for (Long id : deletedId) {
            sb.append(" ?,");
            args.add(id);
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(");");

        LogUtils.d(sb.toString());

        mDbManager.delete(args.toArray(new Object[args.size()]), sb.toString());
    }

    public List<LocalMedia> getPicList(Long contentResultId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<LocalMedia> temp = new ArrayList<>();
        String sql = "SELECT * FROM DBPATROLPIC WHERE PROJECT_ID = ? AND USER_ID = ? AND ITEM_ID = ? ";
        String[] args = { projectId + "", userId + "", contentResultId + "" };
        LogUtils.d(sql);
        Cursor cursor = mDbManager.query(args, sql);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                LocalMedia bean = new LocalMedia();
                bean.setPath(cursor.getString(cursor.getColumnIndex("PATH")));
                bean.setChecked(cursor.getInt(cursor.getColumnIndex("NEED_SYNC")) != 0);//需要同步
                temp.add(bean);
            }
            cursor.close();
        }
        return temp;
    }

    public List<PatrolPicEntity> getPicSyncList(Long contentResultId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<PatrolPicEntity> temp = new ArrayList<>();
        String sql = "SELECT * FROM DBPATROLPIC WHERE PROJECT_ID = ? AND USER_ID = ? AND ITEM_ID = ? ";
        String[] args = { projectId + "", userId + "", contentResultId + "" };
        LogUtils.d(sql);
        Cursor cursor = mDbManager.query(args, sql);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PatrolPicEntity bean = new PatrolPicEntity();
                bean.setPath(cursor.getString(cursor.getColumnIndex("PATH")));
                bean.setPicId(cursor.getLong(cursor.getColumnIndex("PICTURE_ID")));
                bean.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                temp.add(bean);
            }
            cursor.close();
        }
        return temp;
    }

    public void deleteItemPic(Long contentResultId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        String sql = "delete from DBPATROLPIC WHERE PROJECT_ID = ? AND USER_ID = ? AND ITEM_ID = ?  ";
        Object[] args = { projectId, userId, contentResultId};
        LogUtils.d(sql);

        mDbManager.delete(args, sql);
    }

    public void update(List<PatrolPicEntity> paths) {
        if (paths == null || paths.size() == 0) {
            return;
        }
        for (PatrolPicEntity path : paths) {
            String sql = "update DBPATROLPIC set PICTURE_ID = ? where id = ? ";
            Object[] args = { path.getPicId(), path.getId() };
            mDbManager.update(args, sql);
        }
    }

    public void deleteItemPic(List<Long> picIds) {
        List<Object> args = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("delete from DBPATROLPIC WHERE ID IN (");
        for (Long id : picIds) {
            sb.append(" ?,");
            args.add(id);
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(");");

        LogUtils.d(sb.toString());

        mDbManager.delete(args.toArray(new Object[args.size()]), sb.toString());
    }
}
