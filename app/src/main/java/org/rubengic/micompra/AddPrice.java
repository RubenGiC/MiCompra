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
        getSupportActionBar().setTitle("Añadir Precio");

        ed_price = (EditText) findViewById(R.id.t_price);
        sp_market = (Spinner) findViewById(R.id.sp_market);
        sp_item = (Spinner) findViewById(R.id.sp_item);

        //list of markets and items
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

        //create adapter to spinner market
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

        //same for items
        ArrayAdapter<String> adapter_items = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_items);
        adapter_items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_item.setAdapter(adapter_items);

        Button b_add_price = (Button) findViewById(R.id.b_add_price);

        //button add price and back
        b_add_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AddPrice.this, "-->"+adapter_items.getItem(sp_item.getSelectedItemPosition()), Toast.LENGTH_SHORT).show();
                if(sp_item.getSelectedItem() == null) {
                    Toast.makeText(AddPrice.this, "Error debe seleccionar un producto.", Toast.LENGTH_SHORT).show();
                }else{
                    insertPrice(db.id_Item(adapter_items.getItem(sp_item.getSelectedItemPosition())), db.id_Market(adapter.getItem(sp_market.getSelectedItemPosition())), ed_price.getText().toString());
                }
            }
        });

        //button back
        my_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPrice.super.onBackPressed();
            }
        });
    }

    public void insertPrice(int item, int market, String price){
        //Toast.makeText(this, String.valueOf(item), Toast.LENGTH_SHORT).show();
        if(!db.existPrice(item, market)){
            try {
                if (price.length() == 0) {
                    Toast.makeText(AddPrice.this, "Error necesita un precio.", Toast.LENGTH_SHORT).show();
                } else if (Float.parseFloat(price) <= 0.0) {
                    Toast.makeText(AddPrice.this, "Error el precio tiene que ser > 0.0.", Toast.LENGTH_SHORT).show();
                } else {
                    //insert in database
                    long id_price = db.insertPrice(price, market, item);

                    //if insert price is correct
                    if (id_price != -1) {

                        //show to add
                        Toast.makeText(this, "Precio Añadido", Toast.LENGTH_SHORT).show();
                        //and back to main layout
                        super.onBackPressed();

                        /*//extrect the id of market
                        Integer id_market = Math.toIntExact(sp_market.getSelectedItemId() + 1);
                        */
                    } else {
                        Toast.makeText(this, "Error al insertar el precio del producto " + sp_item.getSelectedItem(), Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (NumberFormatException e){
                Toast.makeText(this, "Error precio no valido.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "El producto "+sp_item.getSelectedItem()+" ya tiene un precio", Toast.LENGTH_SHORT).show();
        }
    }
}
