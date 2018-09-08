package com.letion.green_dao;

import android.app.Application;

import com.letion.green_dao.gen.DaoMaster;
import com.letion.green_dao.gen.DaoSession;

/**
 * <p>
 *
 * @author wuqi
 * @describe ...
 * @date 2018/9/8 0008
 */
public class DaoManager {
    private static DaoSession daoSession;

    private DaoManager(Application application) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(application
                .getApplicationContext(),
                "lenve.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
    }

    public static synchronized DaoSession getInstance(Application application) {
        if (daoSession == null) {
            new DaoManager(application);
        }
        return daoSession;

    }

}
