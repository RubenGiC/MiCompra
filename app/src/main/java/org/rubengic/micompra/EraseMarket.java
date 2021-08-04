package org.rubengic.micompra;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class EraseMarket extends AppCompatActivity {
    private Spinner sp_market;

    private DataBase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erase_market);

        //create database
        db = new DataBase(getApplicationContext());

        //change toolbar by my personalizate
        Toolbar my_toolbar = findViewById(R.id.custom_toolbar_del_market);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Eliminar Supermercado");

        sp_market = (Spinner) findViewById(R.id.sp_market);

        //list of items
        ArrayList<String> list_markets = new ArrayList<>();

        //indicate using only read data
        SQLiteDatabase db_read = db.getReadableDatabase();

        //access the data items
        Cursor cursor = db_read.rawQuery("SELECT * FROM "+db.DB_MARKETS_PUBLIC, null);

        //go to all data of the table
        while(cursor.moveToNext()){
            list_markets.add(cursor.getString(1));
        }

        //same for items
        ArrayAdapter<String> adapter_markets = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_markets);
        adapter_markets.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_market.setAdapter(adapter_markets);

        Button b_erase = (Button) findViewById(R.id.b_del_market);

        //button add price and back
        b_erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = db.id_Market(list_markets.get(sp_market.getSelectedItemPosition()));
                if(id != -1) {
                    if(db.eraseMarket(id)) {
                        Toast.makeText(EraseMarket.this, "Supermercado borrado", Toast.LENGTH_SHORT).show();
                        EraseMarket.super.onBackPressed();
                    }else
                        Toast.makeText(EraseMarket.this, "Error no se ha borrado con exito el supermercado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EraseMarket.this, "Error DELETE MARKET", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //button back
        my_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EraseMarket.super.onBackPressed();
            }
        });
    }
}
