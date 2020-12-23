package com.example.localinzzaktionapplication.databaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class WriteDatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "write.db";
    public static final int VERSION = 1;
    public WriteDatabaseHelper(@Nullable Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        printLog("onCreate() note 호출");
        String sql = "create table if not exists NOTE("
                +"NOTE_NO integer primary key autoincrement,"
                +"MEMBER_NO  integer,"
                +"NOTE_TITLE  text,"
                +"NOTE_CONTENT text,"
                +"NOTE_PHOTO text,"
                +"NOTE_SHARE text)";
        sqLiteDatabase.execSQL(sql);
    }
/*+"RGST_DT DATE DEFAULT (datetime('now','localtime')),"*/
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        printLog("onOpen() 호출");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        printLog(oldVersion+"->"+newVersion);
        if(newVersion>1){
            sqLiteDatabase.execSQL("drop table if exists movie");
        }
    }
    //로그 찍을 메소드
    private void printLog(String data){
        Log.d("TagDataBaseHelper",data);
    }
}

