package com.facilityone.wireless.a.arch.offline.util;

import android.content.Context;

import com.facilityone.wireless.a.arch.offline.model.service.OfflineService;
import com.facilityone.wireless.a.arch.offline.model.service.PatrolDbService;
import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteOpenHelper;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:数据库创建以及升级
 * Date: 2018/10/11 3:54 PM
 */
public class FMDBHelper extends SQLiteOpenHelper {

    //数据库名称
    private static final String DB_NAME = "fm.db";
    //版本号
    private static final int DB_VERSION = 5;

    public FMDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        OfflineService.createTable(db);
        PatrolDbService.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //https://www.jianshu.com/p/124f12d39320
        //1：rename旧表名称把account改成account_temp
        //2：创建新表，表名称和原表名称一致也叫account或者其他名字account_v1
        //3：复制旧表account_temp(改名字后的表)数据到新表account_v1 中
        //4：Drop掉旧表
        if(newVersion > oldVersion){
            //升级数据库前备份数据
            //1：重命名表名称
            OfflineService.renameTable(db);
            PatrolDbService.renameTable(db);
            //2：创建新表
            OfflineService.createTable(db);
            PatrolDbService.createTable(db);
            //3：复制旧表数据到新表
            OfflineService.copyTable(db);
            PatrolDbService.copyTable(db);
            //4:删除旧表
            OfflineService.deleteTempTable(db);
            PatrolDbService.deleteTempTable(db);
        }
    }
}
