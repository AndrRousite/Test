package com.letion.green_dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.letion.green_dao.gen.DaoMaster;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

/**
 * <p>
 *
 * @author wuqi
 * @describe ...
 * @date 2018/9/17 0017
 */
public class DBUpgradeHelper extends DaoMaster.OpenHelper {
    private Class<? extends AbstractDao<?, ?>>[] daoClasses;

    public DBUpgradeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, Class<? extends
            AbstractDao<?, ?>>[] daoClasses) {
        super(context, name, factory);
        this.daoClasses = daoClasses;
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, daoClasses);
    }
}
