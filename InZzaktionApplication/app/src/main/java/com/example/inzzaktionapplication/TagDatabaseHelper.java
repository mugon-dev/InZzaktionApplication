package com.example.inzzaktionapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

public class TagDatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "tag.db";
    public static final int VERSION = 1;
    public TagDatabaseHelper(@Nullable Context context) {
        super(context,DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        printLog("onCreate() tag 호출");
        String sql = "create table if not exists TAG(TAG_NO integer primary key autoincrement, NOTE_NO integer ,TAG_NM text ,RGB_NO integer ,TAG_POSITION integer)";
        sqLiteDatabase.execSQL(sql);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        printLog("onOpen() 호출");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        printLog(oldVersion+"->"+newVersion);
        if(newVersion>1){
            sqLiteDatabase.execSQL("drop table if exists TAG");
        }
    }
    //로그 찍을 메소드
    private void printLog(String data){
        Log.d("TagDataBaseHelper",data);
    }
}

