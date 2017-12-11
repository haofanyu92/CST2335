package com.example.haofa.androidlabs;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by haofa on 2017-11-23.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "data";
    protected static final String TABLE_NAME = "chat";
    protected static final int VERSION_NUM = 2;
    protected static final String KEY_ID = "ID";
    protected static final String KEY_MESSAGE = "Message";


    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*String CREATE_TABLE = "CREATE TABLE" + TABLE_NAME
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_MESSAGE + " TEXT "
                + ")";*/
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_MESSAGE + " TEXT " + ")";
        db.execSQL(CREATE_TABLE);
        /*db.execSQL("CREATE TABLE " + TABLE_NAME
                + "(KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,KEY_MESSAGE STRING)" );*/

        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {

        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);

    }
}
