package com.SKa2or.ChargeList.unit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shuo on 2016/4/7.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public final static String TABLE_USERS_NAME = "Users";
    public final static String TABLE_Records_NAME = "Records";
    public final static String CREATE_TABLE_USERS = "CREATE TABLE Users(" +
            "_id integer primary key autoincrement," +
            "name text," +
            "amount real," +
            "isCurrent integer)";
    public final static String CREATE_TABLE_RECORDS = "CREATE TABLE Records(" +
            "_id integer primary key autoincrement," +
            "note text," +
            "recordType integer," +
            "sum real," +
            "date integer," +
            "userId integer)";
    public MyDatabaseHelper(Context context) {
        super(context, "Data.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_RECORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
