package com.am.aucklank;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

public class MyApplication extends Application {

    public static DbManager db;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        try {
            this.initDb();
        } catch (DbException e) {
            throw new RuntimeException(e);
        }

    }

    protected void initDb() throws DbException {
        //Initialisation of local data (本地数据的初始化)
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("auckland") //Set the database name (设置数据库名)
                .setDbVersion(1) //Set the database version, which will be checked each time the application is launched(设置数据库版本,每次启动应用时将会检查该版本号)
                //If the database version is found to be lower than the value set here, a database upgrade will be performed and the DbUpgradeListener will be triggered.(发现数据库版本低于这里设置的值将进行数据库升级并触发DbUpgradeListener)
                .setAllowTransaction(true)//Set whether to turn on transactions, default is false to turn off transactions(设置是否开启事务,默认为false关闭事务)
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {

                    }

                })//Setting the Listener on database creation(设置数据库创建时的Listener)
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    }
                });//Set up a Listener for database upgrades, where you can perform relevant changes to the database tables, such as adding fields to alter statements, etc.(设置数据库升级时的Listener,这里可以执行相关数据库表的相关修改,比如alter语句增加字段等)
        //.setDbDir(null);//Set the directory where the database.db file is stored, the default is the databases directory under the package name(设置数据库.db文件存放的目录,默认为包名下databases目录下)
        db = x.getDb(daoConfig);
    }
}
