package com.am.aucklank;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class TestTableSqlite {
    private SQLiteDatabase mDb = null;
    private DatabaseHelper mDbHelper;
    private final Context ctx;

    private static final String DATABASE_NAME = "mytest.db"; /* database name */
    private static final int DATABASE_VERSION = 1; /* database version */

    //Initialisation Context (初始化上下文)
    public TestTableSqlite(Context ctx) {
        this.ctx = ctx;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context ctx) {
            //Initialize database name, version number (初始化数据库名称，版本号)
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //Create table(创建表)
            db.execSQL(TestTable.SQL_CREATE_TABLE_TEST);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    /**
     * open the database (打开数据库)
     *
     * @return composition.db
     * @throws SQLException
     */
    public TestTableSqlite open() throws SQLException {
        mDbHelper = new DatabaseHelper(ctx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /* close the database (关闭数据库) */
    public void close() {
        mDbHelper.close();
    }

    public void resetDbOpen() {
        if (!mDb.isOpen()) {
            open();
        }
    }

    /**
     * Delete the specified table(删除指定的表)
     */
    public void clearTestRecord(String TabName) {
        String sql = "delete from " + TabName;
        mDb.execSQL(sql);
    }


    private static final String[] testColumns = new String[]{
            TestTable.NAME_COLUMN_TEST_ID,
            TestTable.NAME_COLUMN_TEST_NAME,
            TestTable.NAME_COLUMN_TEST_AGE

    };

    /**
     * Database insertion of a record (数据库插入一条记录)
     *
     * @param entity
     */
    public void insertTestRecord(UserBean entity) {
        ContentValues values = new ContentValues();

        values.put(TestTable.NAME_COLUMN_TEST_NAME, entity.getName());
        values.put(TestTable.NAME_COLUMN_TEST_AGE, entity.getAge());

        long insert = mDb.insert(TestTable.NAME_TABLE_TEST, null, values);
        Log.d("print", "插入结果: " + insert);
    }


    /**
     * Search by gender (根据性别查询)
     *
     * @return
     */
    @SuppressLint("Range")
    public String getAllMan(String name) {
        Cursor mCursor = mDb.query(TestTable.NAME_TABLE_TEST, testColumns, TestTable.NAME_COLUMN_TEST_NAME + "=\"" + name + "\"", null, null, null, null);
        ArrayList<UserBean> list = new ArrayList<UserBean>();
        String pwd = "";
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
               // UserBean entity = new UserBean();
               // entity.setName(mCursor.getString(mCursor.getColumnIndex(TestTable.NAME_COLUMN_TEST_NAME)));
               // entity.setAge(mCursor.getString(mCursor.getColumnIndex(TestTable.NAME_COLUMN_TEST_AGE)));

                pwd = mCursor.getString(mCursor.getColumnIndex(TestTable.NAME_COLUMN_TEST_AGE));
               // list.add(entity);
            }
        }
        mCursor.close();
        System.gc();

        return pwd;
    }
    /**
     * Get all elements (得到所有元素)
     *
     * @return
     */
    @SuppressLint("Range")
    public ArrayList<UserBean> getAllTest() {
        Cursor mCursor = mDb.query(TestTable.NAME_TABLE_TEST, testColumns, null, null, null, null, null);
        ArrayList<UserBean> list = new ArrayList<UserBean>();
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                UserBean entity = new UserBean();
                entity.setName(mCursor.getString(mCursor.getColumnIndex(TestTable.NAME_COLUMN_TEST_NAME)));
                entity.setAge(mCursor.getString(mCursor.getColumnIndex(TestTable.NAME_COLUMN_TEST_AGE)));
                list.add(entity);
            }
        }
        mCursor.close();
        System.gc();

        return list;
    }


}
