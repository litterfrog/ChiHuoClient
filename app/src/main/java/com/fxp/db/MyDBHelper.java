package com.fxp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fuxinpeng on 2016/3/16.
 */
public class MyDBHelper extends SQLiteOpenHelper{
    private static final String DBNAME = "db_chihuo.db";
    private static final int VERSION = 1;
    private static final String SQL_CREATE_DB="CREATE TABLE users " +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT ," +
            " Email varchar(255) not null , " +
            "PassWD varchar(30) not null, " +
            "Name varchar(50)," +
            "Sex integer)";
    private SQLiteDatabase db;
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public MyDBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }
    public MyDBHelper(Context context, String name) {
        super(context, name, null, VERSION);
    }
    public MyDBHelper(Context context){
        super(context, DBNAME, null, VERSION);
        getConnection();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
        }
    }

    public void getConnection() {
        db = this.getReadableDatabase();
    }

    public Cursor selectCursor(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }

    public int selectCount(String sql, String[] selectionArgs) {
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor != null) {
            int count = cursor.getCount();
            cursor.close();
            return count;
        }
        return 0;
    }

    public List<Map<String, Object>> selectData(String sql,String[] selectionArgs) {
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursorToList(cursor);
    }

    public List<Map<String, Object>> cursorToList(Cursor cursor) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String[] arrCols = cursor.getColumnNames();
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < arrCols.length; i++) {
                int type = cursor.getType(i);
                Object value = null;
                switch (type) {
                    case 1:
                        value = cursor.getInt(i);
                        break;
                    case 2:
                        value = cursor.getFloat(i);
                        break;
                    case 3:
                        value = cursor.getString(i);
                        break;
                    case 4:
                        value = cursor.getBlob(i);
                        break;
                    default:
                        break;
                }
                map.put(arrCols[i], value);
            }
            list.add(map);
        }
        return list;
    }

    public boolean updateData(String sql, Object[] bindArgs) {
        try {
            db.execSQL(sql, bindArgs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void destroy() {
        if (db != null) {
            db.close();
        }
    }
}

