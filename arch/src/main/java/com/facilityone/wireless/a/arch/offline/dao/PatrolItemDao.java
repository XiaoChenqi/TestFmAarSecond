package com.facilityone.wireless.a.arch.offline.dao;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.facilityone.wireless.a.arch.offline.model.entity.PatrolItemEntity;
import com.facilityone.wireless.a.arch.offline.model.service.PatrolDbService;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.basiclib.app.FM;
import com.facilityone.wireless.basiclib.utils.StringUtils;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检检查项
 * Date: 2018/10/30 4:56 PM
 */
public class PatrolItemDao {

    private final DBManager mDbManager;

    public PatrolItemDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBPATROLITEM (" + //
                "ID INTEGER," + // 0: id
                "CONTENT_ID INTEGER," + // 1: contentId
                "EQU INTEGER," + // 2: equ id
                "SPOT_ID INTEGER," + // 3: spot id
                "TASK_ID INTEGER," + // 4: task id
                "SELECT_VALUE TEXT," + // 5: select
                "INPUT_VALUE TEXT," + // 6: input
                "COMMENT TEXT," + // 7: comment
                "SORT INTEGER," + // 8: sort
                "COMPLETED INTEGER," + // 9: completed
                "USER_ID INTEGER," + // 10: userId
                "DELETED INTEGER," + // 11: deleted
                "PROJECT_ID INTEGER ," + // 12: projectId
                "PRIMARY KEY (ID,USER_ID,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBPATROLITEM SELECT * FROM DBPATROLITEM_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLITEM";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLITEM_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBPATROLITEM RENAME TO DBPATROLITEM_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBPATROLITEM";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addPatrolItem(List<PatrolItemEntity> itemEntities) {
        if (itemEntities == null || itemEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBPATROLITEM VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

            for (PatrolItemEntity entity : itemEntities) {
                Object[] args = {
                        entity.getContentResultId()
                        , entity.getContentId()
                        , entity.getEqId()
                        , entity.getSpotId()
                        , entity.getTaskId()
                        , entity.getSelect()
                        , entity.getInput()
                        , entity.getComment()
                        , entity.getSort()
                        , entity.getCompleted()
                        , userId
                        , entity.getDeleted()
                        , projectId };
                mDbManager.insert(args, sql);
            }

            String delSql = "DELETE FROM DBPATROLITEM WHERE DELETED = ? AND PROJECT_ID = ?";
            Object[] delArgs = { true, projectId };
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

    public void deleteTaskItem(List<Long> deletedId) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<Object> args = new ArrayList<>();
        args.add(projectId);
        args.add(userId);
        StringBuilder sb = new StringBuilder();
        sb.append("delete from DBPATROLITEM WHERE PROJECT_ID = ? AND USER_ID = ? AND  TASK_ID IN (");
        for (Long id : deletedId) {
            sb.append(" ?,");
            args.add(id);
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(");");

        LogUtils.d(sb.toString());

        mDbManager.delete(args.toArray(new Object[args.size()]), sb.toString());
    }

    public List<PatrolItemEntity> getItemList(Long eqId, Long spotId, Boolean deviceStatus) {
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        List<PatrolItemEntity> temp = new ArrayList<>();
        String sql;
        String[] args;
        if (eqId == PatrolDbService.COMPREHENSIVE_EQU_ID) {
            deviceStatus = null;
        }
        if (deviceStatus == null) {
            sql = "SELECT C.ID,C.CONTENT_ID,C.EQU,C.SPOT_ID,C.TASK_ID,C.SELECT_VALUE,C.INPUT_VALUE,C.COMMENT,C.COMPLETED,B.CONTENT,B.SELECT_ENUMS,B.CONTENT_TYPE,B.RESULT_TYPE,B.INPUT_UPPER,B.INPUT_FLOOR,B.DEFAULT_INPUT_VALUE,B.DEFAULT_SELECT_VALUE,B.EXCEPTIONS,B.UNIT,B.SELECT_RIGHT_VALUE,B.VALID_STATUS FROM DBPATROLITEM AS C LEFT JOIN DBPATROLBASEITEM AS B ON C.CONTENT_ID = B.ID  AND C.PROJECT_ID = C.PROJECT_ID WHERE C.EQU = ? AND C.SPOT_ID = ? AND C.PROJECT_ID = ? AND C.USER_ID = ? ORDER BY C.SORT;";
            args = new String[]{ eqId + "", spotId + "", projectId + "", userId + "" };
        } else {
            sql = "SELECT C.ID,C.CONTENT_ID,C.EQU,C.SPOT_ID,C.TASK_ID,C.SELECT_VALUE,C.INPUT_VALUE,C.COMMENT,C.COMPLETED,B.CONTENT,B.SELECT_ENUMS,B.CONTENT_TYPE,B.RESULT_TYPE,B.INPUT_UPPER,B.INPUT_FLOOR,B.DEFAULT_INPUT_VALUE,B.DEFAULT_SELECT_VALUE,B.EXCEPTIONS,B.UNIT,B.SELECT_RIGHT_VALUE,B.VALID_STATUS FROM DBPATROLITEM AS C LEFT JOIN DBPATROLBASEITEM AS B ON C.CONTENT_ID = B.ID  AND C.PROJECT_ID = C.PROJECT_ID WHERE C.EQU = ? AND C.SPOT_ID = ? AND C.PROJECT_ID = ? AND C.USER_ID = ? AND (B.VALID_STATUS = ?  OR B.VALID_STATUS = ? )  ORDER BY C.SORT;";
            args = new String[]{ eqId + "", spotId + "", projectId + "", userId + "", PatrolDbService.PATROL_ITEM_NONE + "", deviceStatus ? PatrolDbService.PATROL_ITEM_USE + "" : PatrolDbService.PATROL_ITEM_STOP + "" };
        }

        LogUtils.d(sql);

        Cursor cursor = mDbManager.query(args, sql);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PatrolItemEntity bean = new PatrolItemEntity();
                bean.setContentResultId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setContentId(cursor.getLong(cursor.getColumnIndex("CONTENT_ID")));
                bean.setEqId(cursor.getLong(cursor.getColumnIndex("EQU")));
                bean.setSpotId(cursor.getLong(cursor.getColumnIndex("SPOT_ID")));
                bean.setTaskId(cursor.getLong(cursor.getColumnIndex("TASK_ID")));
                bean.setSelect(cursor.getString(cursor.getColumnIndex("SELECT_VALUE")));
                bean.setInput(cursor.getString(cursor.getColumnIndex("INPUT_VALUE")));
                bean.setComment(cursor.getString(cursor.getColumnIndex("COMMENT")));
                bean.setCompleted(cursor.getInt(cursor.getColumnIndex("COMPLETED")) != 0);
                bean.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
                bean.setSelectEnums(cursor.getString(cursor.getColumnIndex("SELECT_ENUMS")));
                bean.setSelectRightValue(cursor.getString(cursor.getColumnIndex("SELECT_RIGHT_VALUE")));
                bean.setContentType(cursor.getInt(cursor.getColumnIndex("CONTENT_TYPE")));
                bean.setResultType(cursor.getInt(cursor.getColumnIndex("RESULT_TYPE")));
                bean.setInputUpper(StringUtils.string2Double(cursor.getString(cursor.getColumnIndex("INPUT_UPPER"))));
                bean.setInputFloor(StringUtils.string2Double(cursor.getString(cursor.getColumnIndex("INPUT_FLOOR"))));

                String defaultInputValue = cursor.getString(cursor.getColumnIndex("DEFAULT_INPUT_VALUE"));
                if(!TextUtils.isEmpty(defaultInputValue)) {
                    bean.setDefaultInputValue(Double.parseDouble(defaultInputValue));
                }
                bean.setDefaultSelectValue(cursor.getString(cursor.getColumnIndex("DEFAULT_SELECT_VALUE")));
                bean.setExceptions(cursor.getString(cursor.getColumnIndex("EXCEPTIONS")));
                bean.setUnit(cursor.getString(cursor.getColumnIndex("UNIT")));
                bean.setValidStatus(cursor.getInt(cursor.getColumnIndex("VALID_STATUS")));
                temp.add(bean);
            }
            cursor.close();
        }
        return temp;
    }

    public boolean updatePatrolItem(List<PatrolItemEntity> itemEntities) {
        if (itemEntities == null || itemEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        boolean result = false;
        try {
            String sql = "UPDATE DBPATROLITEM SET SELECT_VALUE = ?,INPUT_VALUE = ?,COMMENT = ? ,COMPLETED = ? WHERE  ID = ? AND  USER_ID = ?  AND PROJECT_ID = ?;";
            for (PatrolItemEntity entity : itemEntities) {
                Object[] args = {
                        entity.getSelect()
                        , entity.getInput()
                        , entity.getComment()
                        , true
                        , entity.getContentResultId()
                        , userId
                        , projectId };
                mDbManager.insert(args, sql);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public int getMissItemList(Long eqId, Long spotId, boolean deviceStatus) {
        int miss = 0;
        Long projectId = FM.getProjectId();
        Long userId = FM.getEmId();
        String sql = "SELECT COUNT(*) AS MISS FROM DBPATROLITEM AS C LEFT JOIN DBPATROLBASEITEM AS B ON C.CONTENT_ID = B.ID  AND C.PROJECT_ID = C.PROJECT_ID WHERE C.EQU = ? AND C.SPOT_ID = ? AND C.PROJECT_ID = ? AND C.USER_ID = ? AND (B.VALID_STATUS = ?  OR B.VALID_STATUS = ? ) AND ((B.RESULT_TYPE = 1 AND (C.SELECT_VALUE IS NULL OR C.SELECT_VALUE = '')) OR (B.RESULT_TYPE = 0 AND (C.INPUT_VALUE = '' OR C.INPUT_VALUE IS NULL) )) ORDER BY C.SORT";
        String[] args = new String[]{ eqId + "", spotId + "", projectId + "", userId + "", PatrolDbService.PATROL_ITEM_NONE + "", deviceStatus ? PatrolDbService.PATROL_ITEM_USE + "" : PatrolDbService.PATROL_ITEM_STOP + "" };

        LogUtils.d(sql);

        Cursor cursor = mDbManager.query(args, sql);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                miss = cursor.getInt(cursor.getColumnIndex("MISS"));
            }
            cursor.close();
        }
        return miss;
    }
}
