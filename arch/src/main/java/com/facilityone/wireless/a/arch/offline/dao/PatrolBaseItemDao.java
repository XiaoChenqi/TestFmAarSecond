package com.facilityone.wireless.a.arch.offline.dao;

import com.facilityone.wireless.a.arch.offline.model.entity.PatrolBaseItemEntity;
import com.facilityone.wireless.a.arch.offline.util.DBManager;
import com.facilityone.wireless.basiclib.app.FM;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检离线检查项基础数据
 * Date: 2018/10/30 4:56 PM
 */
public class PatrolBaseItemDao {

    private final DBManager mDbManager;

    public PatrolBaseItemDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS DBPATROLBASEITEM (" + //
                "ID INTEGER  ," + // 0: id
                "CONTENT TEXT," + // 1: content
                "SELECT_ENUMS TEXT," + // 2: selectEnums
                "CONTENT_TYPE INTEGER," + // 3: contentType
                "RESULT_TYPE INTEGER," + // 4: resultType
                "SELECT_RIGHT_VALUE TEXT," + // 5: selectRightValue
                "INPUT_UPPER REAL," + // 6: inputUpper
                "INPUT_FLOOR REAL," + // 7: inputFloor
                "DEFAULT_INPUT_VALUE REAL," + // 8: defaultInputValue
                "DEFAULT_SELECT_VALUE TEXT," + // 9: defaultSelectValue
                "EXCEPTIONS TEXT," + // 10: exceptions
                "UNIT TEXT," + // 11: unit
                "VALID_STATUS INTEGER," + // 12: validStatus
                "DELETED INTEGER," + // 13: deleted
                "PROJECT_ID INTEGER ,"+ // 14: projectId
                "PRIMARY KEY (ID,PROJECT_ID));");
    }

    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBPATROLBASEITEM SELECT * FROM DBPATROLBASEITEM_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLBASEITEM";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPATROLBASEITEM_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBPATROLBASEITEM RENAME TO DBPATROLBASEITEM_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBPATROLBASEITEM";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addPatrolBaseItem(List<PatrolBaseItemEntity> itemEntities) {
        if (itemEntities == null || itemEntities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBPATROLBASEITEM VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            for (PatrolBaseItemEntity entity : itemEntities) {
                Object[] args = {
                        entity.getContentId()
                        , entity.getContent()
                        , entity.getSelectEnums()
                        , entity.getContentType()
                        , entity.getResultType()
                        , entity.getSelectRightValue()
                        , entity.getInputUpper()
                        , entity.getInputFloor()
                        , entity.getDefaultInputValue()
                        , entity.getDefaultSelectValue()
                        , entity.getExceptions()
                        , entity.getUnit()
                        , entity.getValidStatus()
                        , entity.getDeleted()
                        ,projectId };
                mDbManager.insert(args, sql);
            }

//            String delSql = "DELETE FROM DBPATROLBASEITEM WHERE DELETED = ? AND PROJECT_ID = ?";
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
}
