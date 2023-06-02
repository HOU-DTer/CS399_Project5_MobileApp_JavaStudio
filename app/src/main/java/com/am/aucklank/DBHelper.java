package com.am.aucklank;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class DBHelper extends SQLiteOpenHelper {
    //database name (数据库名称)
    private static final String DB_NAME = "final.db";
    //table name (表名称)
    private static final String TBL_NAME = "FINAL";
    //create table SQL language (创建表SQL语句)
    private static final String CREATE_TBL = " create table "
            + " CollTbl(_id integer primary key autoincrement,name text,url text,des text)";
    //SQLiteDatabase instances (实例)
    private SQLiteDatabase db;

    /*
     * Construction method (构造方法)
     */
    public DBHelper(Context c) {
        super(c, DB_NAME, null, 2);
    }

    ;

    /*
     * Create the table (创建表)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TBL);
    }

    /*
     * Insert the method (插入方法)
     */
    public void insert(ContentValues values) {
        //get the database (获得SQLiteDatabase实例)
        SQLiteDatabase db = getWritableDatabase();
        //insert (插入)
        db.insert(TBL_NAME, null, values);
        //close (关闭)
        db.close();
    }

    /*
     * search method (查询方法)
     */
    public Cursor query(String s) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = new String[]{"Incidents", "latitude", "longitude"};
        if (s.equals("")) {
            Log.d("query", "query:null ");
            Cursor c = db.query(TBL_NAME, columns, null, null, null, null, null);
            return c;
        } else {
            Log.d("query", "query: ");
            String[] selectionArgs = new String[]{s};
            Cursor c = db.query(TBL_NAME, columns, "Incidents = ?", selectionArgs, null, null, null);
            return c;
        }


    }

    public Cursor queryDate(int page) {
        Log.d("page", "queryDate:  " + page);
        int i = 10*page;
        String num = i + "";
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TBL_NAME, "latitude=?", new String[]{"Latitude"});
        Cursor cursor1 = db.rawQuery("select MAX(date) FROM "+ TBL_NAME , null);
        String date = "";
        if (cursor1 != null) {
            if (cursor1.moveToFirst()) {
                 date = cursor1.getString(0);
                //@SuppressLint("Range") String date = cursor1.getString(cursor1.getColumnIndex("date"));
                Log.d("page", "queryDate:  " + date);
            }
            cursor1.close();
        }

        String[] columns = new String[]{"date","incidents", "latitude", "longitude","hour","area","severity"};
        db.query(TBL_NAME, columns, null, null, null, null, null);
        Cursor cursor = db.rawQuery(" select * from " + TBL_NAME + " where date between date('" + date + "','start of day','-2 days') and date('" + date + "') ORDER BY date DESC limit " +num + " ,10 ", null);
               return cursor;
    }
    public Cursor queryDateHis(int page) {
        Log.d("page", "queryDate:  " + page);
        int i = 10*page;
        String num = i + "";
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TBL_NAME, "latitude=?", new String[]{"Latitude"});
        Cursor cursor1 = db.rawQuery("select MAX(date) FROM "+ TBL_NAME , null);
        String date = "";
        if (cursor1 != null) {
            if (cursor1.moveToFirst()) {
                date = cursor1.getString(0);
                //@SuppressLint("Range") String date = cursor1.getString(cursor1.getColumnIndex("date"));
                Log.d("page", "queryDate:  " + date);
            }
            cursor1.close();
        }
        String[] columns = new String[]{"date","incidents", "latitude", "longitude","hour","area","severity"};
        db.query(TBL_NAME, columns, null, null, null, null, null);
        Cursor cursor = db.rawQuery(" select * from " + TBL_NAME + " where date between date('" + date + "','start of day','-365 days') and date('" + date + "','start of day','-2 days') ORDER BY date DESC limit " +num + " ,10 ", null);
        return cursor;
    }

    /*
     * delete method (删除方法)
     */
    public void del(int id) {
        if (db == null) {
            //get the instances (获得SQLiteDatabase实例)
            db = getWritableDatabase();
        }
        //delete the table (执行删除)
        db.delete(TBL_NAME, "_id=?", new String[]{String.valueOf(id)});
    }

    /*
     * close the database (关闭数据库)
     */
    public void colse() {
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}