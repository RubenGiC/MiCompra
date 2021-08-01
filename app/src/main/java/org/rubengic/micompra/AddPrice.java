package org.rubengic.micompra;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class AddPrice extends AppCompatActivity {

    private EditText ed_price;
    private Spinner sp_market, sp_item;

    private DataBase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_price);

        //create database
        db = new DataBase(getApplicationContext());

        //change toolbar by my personalizate
        Toolbar my_toolbar = findViewById(R.id.custom_toolbar_add_item);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Añadir Producto");

        ed_price = (EditText) findViewById(R.id.t_price);
        sp_market = (Spinner) findViewById(R.id.sp_market);
        sp_item = (Spinner) findViewById(R.id.sp_item);

        ArrayList<String> list_markets = new ArrayList<>();
        ArrayList<String> list_items = new ArrayList<>();

        //indicate using only read data
        SQLiteDatabase db_read = db.getReadableDatabase();

        //access the data markets
        Cursor cursor = db_read.rawQuery("SELECT * FROM "+db.DB_MARKETS_PUBLIC, null);

        //go to all data of the table
        while(cursor.moveToNext()){
            list_markets.add(cursor.getString(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_markets);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_market.setAdapter(adapter);

        //access the data items
        cursor = db_read.rawQuery("SELECT * FROM "+db.DB_ITEMS_PUBLIC, null);

        //go to all data of the table
        while(cursor.moveToNext()){
            list_items.add(cursor.getString(1));
        }

        ArrayAdapter<String> adapter_items = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_items);
        adapter_items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_item.setAdapter(adapter_items);
    }
}
