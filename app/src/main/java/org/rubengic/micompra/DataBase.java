package org.rubengic.micompra;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {
    //CREATE THE DB
    private static final String MIMARKET_CREATE_TABLE = "CREATE TABLE mimarket (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price DOUBLE, market TEXT)";
    //name the file
    private static final String BD_NAME = "mimarket.sqlite";
    //number of version
    private static final int VERSION = 1;

    public DataBase(Context context) {
        super(context, BD_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //execute Code SQL
        db.execSQL(MIMARKET_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
