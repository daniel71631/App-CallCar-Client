package com.example.daniel.try_loggin;

import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by daniel on 2017/7/31.
 */

public class SQLitecarstore extends SQLiteOpenHelper {

    private final static String DB_NAME = "db_carstore.db";
    private final static int VERSION = 1;
    public SQLiteDatabase dbcarstore;

    public SQLitecarstore(Context context) {
        super(context, DB_NAME, null, VERSION);
        dbcarstore = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists tb_carstore(_id integer primary key autoincrement , username , phonenumber)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists tb_carstore");
            onCreate(db);
        }

    }

    public Cursor selectCusor(String sql, String[] selectionArgs) {
        return dbcarstore.rawQuery(sql, selectionArgs);
    }

    public int selectCount(String sql, String[] selectionArgs) {
        Cursor cursor = dbcarstore.rawQuery(sql, selectionArgs);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        } else {
            return 0;
        }
    }

    public List<Map<String, Object>> selectList(String sql, String[] selectionArgs) {
        Cursor cursor = dbcarstore.rawQuery(sql, selectionArgs);
        return cursorToList(cursor);
    }

    public List<Map<String, Object>> cursorToList(Cursor cursor) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String[] arrColumnName = cursor.getColumnNames();
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();

            for (int i = 0; i < arrColumnName.length; i++) {
                Object cols_value = cursor.getString(i);
                map.put(arrColumnName[i], cols_value);
            }
            list.add(map);
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public boolean execData(String sql, Object[] bindArgs) {
        try {
            dbcarstore.execSQL(sql, bindArgs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void destroy() {
        if (dbcarstore != null) {
            dbcarstore.close();
        }
    }

}
