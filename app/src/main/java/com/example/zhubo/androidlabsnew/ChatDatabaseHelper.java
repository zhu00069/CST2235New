package com.example.zhubo.androidlabsnew;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    //database name
    public static final String DATABASE_NAME = "Messages.db";
    //table name
    public static final String TABLE_NAME ="table_msg";
    //database version
    private static final int DATABASE_VERSION = 5;
    public static final String KEY_ID ="id";
    public static final String KEY_MESSAGE = "message";  //a column for MESSAGE as strings.

    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //create a table with a column for id of integers that autoincrement, and a column for MESSAGE as strings.
    // It is best to define the Column names as final static String variables: KEY_ID, KEY_MESSAGE.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE + " TEXT);";
        try {
            Log.i("ChatDatabaseHelper", query);
            db.execSQL(query);
        } catch (SQLException e) {
            Log.e("ChatDatabaseHelper", e.getMessage());
        }
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + " newVersion= " + newVer);
    }
}
