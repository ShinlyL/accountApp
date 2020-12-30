package com.example.administrator.accountbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2020-10-31 , 0031.
 */

public class MyHelper extends SQLiteOpenHelper {
    public MyHelper(Context context){
        super(context,"data2.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_user(_id integer primary key autoincrement," +
                "account varchar(20),password varchar(20),nickname varchar(20),photo varchar(20))");
        db.execSQL("create table tb_acbook(_id integer primary key autoincrement," +
                "detail varchar(50),flag varchar(20),date varchar(20),money varchar(20),number varchar(20))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
