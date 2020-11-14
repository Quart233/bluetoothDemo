package com.example.bluetoothdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String TAG = "MySQLiteHelper";
    //数据库建表语句

    public static final String sql = new StringBuilder()
            .append("create table FaceProfiles")
            .append("(")
            .append("id integer primary key autoincrement,")
            .append("name text(4),")
            .append("pixelSequence text(5)")
            .append(")")
            .toString();

    public MySQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);//创建数据库调用方法
    }
    /**
     * 第一次创建数据库时调用 在这方法里面可以进行建表
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate: " );
        db.execSQL(sql);
    }
    /**
     * 版本更新的时候调用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade: " );
//        switch (oldVersion){
//            case 1:
//                db.execSQL(sql1);
//                break;
//        }
    }
}

