package org.rubengic.micompra;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.rubengic.micompra.Models.Items;

import java.util.ArrayList;

public class EraseItem  extends AppCompatActivity {

    private Spinner sp_item;

    private DataBase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erase_item);

        //create database
        db = new DataBase(getApplicationContext());

        //change toolbar by my personalizate
        Toolbar my_toolbar = findViewById(R.id.custom_toolbar_del_item);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Eliminar Producto");

        sp_item = (Spinner) findViewById(R.id.sp_item);

        //list of items
        ArrayList<String> list_items = new ArrayList<>();

        //indicate using only read data
        SQLiteDatabase db_read = db.getReadableDatabase();

        //access the data items
        Cursor cursor = db_read.rawQuery("SELECT * FROM "+db.DB_ITEMS_PUBLIC, null);

        //go to all data of the table
        while(cursor.moveToNext()){
            list_items.add(cursor.getString(1));
        }

        //same for items
        ArrayAdapter<String> adapter_items = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_items);
        adapter_items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_item.setAdapter(adapter_items);

        Button b_erase = (Button) findViewById(R.id.b_del_item);

        //button add price and back
        b_erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = db.id_Item(list_items.get(sp_item.getSelectedItemPosition()));
                if(id != -1) {
                    if(db.eraseItem(id)) {
                        Toast.makeText(EraseItem.this, "Producto borrado", Toast.LENGTH_SHORT).show();
                        EraseItem.super.onBackPressed();
                    }else
                        Toast.makeText(EraseItem.this, "Error no se ha borrado con exito el producto", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EraseItem.this, "Error DELETE ITEM", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //button back
        my_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EraseItem.super.onBackPressed();
            }
        });
    }
}
