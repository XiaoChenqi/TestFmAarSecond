package com.facilityone.wireless.a.arch.offline.dao;

import com.facilityone.wireless.a.arch.ec.module.LocationBean;
import com.facilityone.wireless.a.arch.ec.module.SelectDataBean;
import com.facilityone.wireless.a.arch.offline.model.entity.FlowEntity;
import com.facilityone.wireless.a.arch.offline.model.entity.PriorityEntity;
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
 * description:优先级数据库操作
 * Date: 2018/10/11 4:26 PM
 */
public class PriorityDao {

    private final DBManager mDbManager;

    public PriorityDao() {
        mDbManager = DBManager.getInstance();
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DBPRIORITY (" + //
                "ID INTEGER ," + // 0: id
                "NAME TEXT," + // 1: name
                "DESC TEXT," + // 2: desc
                "COLOR TEXT," + // 3: color
                "DELETED INTEGER," + // 4: deleted
                "PROJECT_ID INTEGER," + // 5: projectId;
                "PRIMARY KEY (ID,PROJECT_ID));"
        );
    }

    // 有多少个新的字段，就要预留多少个' '空值给新字段 ps：上面创建表新字段记得加在最后
    // insert into DBPRIORITY select *, ' ' ， ' ' from DBPRIORITY_TEMP
    public static void copyTable(SQLiteDatabase db) {
        String sql = "INSERT INTO DBPRIORITY SELECT * FROM DBPRIORITY_TEMP";
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPRIORITY";
        db.execSQL(sql);
    }

    public static void dropTempTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS DBPRIORITY_TEMP";
        db.execSQL(sql);
    }

    public static void renameTable(SQLiteDatabase db) {
        String sql = "ALTER TABLE DBPRIORITY RENAME TO DBPRIORITY_TEMP";
        db.execSQL(sql);
    }

    public static void deleteAllData() {
        String sql = "delete from DBPRIORITY";
        DBManager.getInstance().delete(null, sql);
    }

    public boolean addPriority(List<PriorityEntity> priorities) {
        if (priorities == null || priorities.size() == 0) {
            return true;
        }
        Long projectId = FM.getProjectId();
        boolean result = false;
        try {
            mDbManager.beginTransaction();
            String sql = "INSERT OR REPLACE INTO DBPRIORITY VALUES(?,?,?,?,?,?)";

            for (PriorityEntity priority : priorities) {
                Object[] args = {
                        priority.getPriorityId()
                        , priority.getName()
                        , priority.getDesc()
                        , priority.getColor()
                        , priority.getDeleted()
                        , projectId };
                mDbManager.insert(args, sql);
            }

//            String delSql = "DELETE FROM DBPRIORITY WHERE DELETED = ? AND PROJECT_ID = ?";
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

    public List<SelectDataBean> queryFlowPriority(SelectDataBean depSelectData, SelectDataBean serviceTypeSelectData, Long woTypeId, LocationBean locationBean) {
        Long projectId = FM.getProjectId();
        List<SelectDataBean> temp = null;

        FlowDao flowDao = new FlowDao();
        LocationBean location = new LocationBean();
        if (locationBean != null) {
            location.cityId = locationBean.cityId;
            location.siteId = locationBean.siteId;
            location.buildingId = locationBean.buildingId;
            location.floorId = locationBean.floorId;
            location.roomId = locationBean.roomId;
        }
        List<Long> depParentIds = new ArrayList<>();
        Long depId = null;
        if (depSelectData != null) {
            if (depSelectData.getParentIds() != null) {
                depSelectData.getParentIds().add(depSelectData.getId());
                depParentIds.addAll(depSelectData.getParentIds());
            }
            depId = depSelectData.getId();
        }
        List<Long> serviceTypeParentIds = new ArrayList<>();
        Long serviceTypeId = null;
        if (serviceTypeSelectData != null) {
            if (serviceTypeSelectData.getParentIds() != null) {
                serviceTypeSelectData.getParentIds().add(serviceTypeSelectData.getId());
                serviceTypeParentIds.addAll(serviceTypeSelectData.getParentIds());
            }
            serviceTypeId = serviceTypeSelectData.getId();
        }
        List<FlowEntity> ids = flowDao.queryPriorityIds(depId, serviceTypeId, woTypeId, locationBean);
        while (ids == null || ids.size() == 0) {
            if (location.roomId != null) {
                location.roomId = null;
            } else if (location.floorId != null) {
                location.floorId = null;
            } else if (location.buildingId != null) {
                location.buildingId = null;
            } else if (location.siteId != null) {
                location.siteId = null;
            } else {
                location = null;
            }

            if (location == null) {
                if (depParentIds == null || depParentIds.size() == 0) {
                    if (serviceTypeParentIds == null || serviceTypeParentIds.size() == 0) {
                        break;
                    }
                    serviceTypeId = serviceTypeParentIds.get(serviceTypeParentIds.size() - 1);
                    serviceTypeParentIds.remove(serviceTypeParentIds.size() - 1);
                    if (depParentIds == null) {
                        depParentIds = new ArrayList<>();
                    }
                    if (depSelectData != null && depSelectData.getParentIds() != null) {
                        depParentIds.addAll(depSelectData.getParentIds());
                    }
                }

                if (depParentIds.size() > 0) {
                    depId = depParentIds.get(depParentIds.size() - 1);
                    depParentIds.remove(depParentIds.size() - 1);
                } else {
                    depId = null;
                }

                location = new LocationBean();
                if (locationBean != null) {
                    location.cityId = locationBean.cityId;
                    location.siteId = locationBean.siteId;
                    location.buildingId = locationBean.buildingId;
                    location.floorId = locationBean.floorId;
                    location.roomId = locationBean.roomId;
                }

            }

            ids = flowDao.queryPriorityIds(depId, serviceTypeId, woTypeId, location);
        }

        if (ids == null || ids.size() == 0) {
            return null;
        }

        temp = new ArrayList<>();

        for (FlowEntity id : ids) {
            temp.addAll(flowPriority(id, projectId));
        }

        return temp;
    }

    private List<SelectDataBean> flowPriority(FlowEntity flowEntity, Long projectId) {
        List<SelectDataBean> temp = new ArrayList<>();
        String sql = "SELECT * FROM DBPRIORITY WHERE PROJECT_ID = ? AND ID = ? AND DELETED = 0 ";
        String[] queryArgs = { projectId + "", flowEntity.getPriorityId() + "" };
        Cursor cursor = mDbManager.query(queryArgs, sql);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                SelectDataBean bean = new SelectDataBean();
                bean.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setParentId(flowEntity.getWopId());//流程id
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setFullName(bean.getName());
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

    public List<SelectDataBean> queryPriority() {
        return queryPriority(false);
    }

    public List<SelectDataBean> queryPriority(boolean needFilterDelete) {
        Long projectId = FM.getProjectId();
        List<SelectDataBean> temp = null;
        String sql;
        if (needFilterDelete) {
            sql = "SELECT * FROM DBPRIORITY WHERE PROJECT_ID = ? AND DELETED = 0 ";
        } else {
            sql = "SELECT * FROM DBPRIORITY WHERE PROJECT_ID = ? ";
        }

        String[] queryArgs = { projectId + "" };
        Cursor cursor = mDbManager.query(queryArgs, sql);
        if (cursor != null) {
            temp = new ArrayList<>();
            while (cursor.moveToNext()) {
                SelectDataBean bean = new SelectDataBean();
                bean.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                bean.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                bean.setFullName(bean.getName());
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
