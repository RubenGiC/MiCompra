package org.rubengic.micompra;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {
    //CREATE THE DB
    private static final String MIMARKET_CREATE_TABLE = "CREATE TABLE mimarket (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
    private static final String MIITEMS_CREATE_TABLE = "CREATE TABLE miitem (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
    private static final String MIPRICES_CREATE_TABLE = "CREATE TABLE miprice (_id INTEGER PRIMARY KEY AUTOINCREMENT, item INTEGER, market INTEGER, price DOUBLE)";
    //name the file
    private static final String BD_NAME = "mimarket.sqlite";
    //number of version
    private static final int VERSION = 1;

    //name db
    public String DB_MARKETS_PUBLIC = "mimarket";
    public String DB_ITEMS_PUBLIC = "miitem";
    public String DB_PRICES_PUBLIC = "miprice";

    public DataBase(Context context) {
        super(context, BD_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //execute Code SQL
        db.execSQL(MIMARKET_CREATE_TABLE);
        db.execSQL(MIITEMS_CREATE_TABLE);
        db.execSQL(MIPRICES_CREATE_TABLE);

        db.execSQL("INSERT INTO mimarket (_id, name) VALUES (1, 'Coviran')");
        db.execSQL("INSERT INTO mimarket (_id, name) VALUES (2, 'Carrefour')");
        db.execSQL("INSERT INTO mimarket (_id, name) VALUES (3, 'Dia')");
        db.execSQL("INSERT INTO mimarket (_id, name) VALUES (4, 'Lidl')");
        db.execSQL("INSERT INTO mimarket (_id, name) VALUES (5, 'Mercadona')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("DROP TABLE IF EXISTS " + DB_ITEMS_PUBLIC);
        db.execSQL("DROP TABLE IF EXISTS " + DB_MARKETS_PUBLIC);
        db.execSQL("DROP TABLE IF EXISTS " + DB_PRICES_PUBLIC);
        onCreate(db);*/
    }
}
