package org.rubengic.micompra;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.rubengic.micompra.Models.Items;
import org.rubengic.micompra.Models.Prices;

import java.util.ArrayList;

public class ItemInfo extends AppCompatActivity {

    private ArrayList<Prices> listPrices;
    private RecyclerView rv_listPrices;

    private ListPrices adapter;

    private DataBase db;

    private ImageView img_v;
    private TextView tv_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        int id_item = getIntent().getExtras().getInt("id");

        //create database
        db = new DataBase(getApplicationContext());

        //indicate using only read data
        SQLiteDatabase db_read = db.getReadableDatabase();

        Items item = new Items();

        Cursor cursor = db_read.rawQuery("SELECT * FROM "+db.DB_ITEMS_PUBLIC+" WHERE _id="+id_item, null);

        //go to all data of the table
        while(cursor.moveToNext()){
            item = new Items(cursor.getInt(0),cursor.getString(1),0.0,null,cursor.getString(2),cursor.getInt(0));
        }

        //change toolbar by my personalizate
        Toolbar my_toolbar = findViewById(R.id.custom_toolbar_del_item);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle(item.getName());

        tv_name = (TextView) findViewById(R.id.tv_name);
        img_v = (ImageView) findViewById(R.id.iv_item);

        tv_name.setText(item.getName());

        Bitmap bm_img = BitmapFactory.decodeFile(item.getPathImage());

        img_v.setImageBitmap(bm_img);

        /*if(item.getPathImage() != null)
            img_v.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));*/

        //create the list of items and the recycler view
        listPrices = new ArrayList<>();
        rv_listPrices = (RecyclerView) findViewById(R.id.rv_listPrices);

        //manage layout
        rv_listPrices.setLayoutManager(new LinearLayoutManager(this));

        loadList(id_item);

        my_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInfo.super.onBackPressed();
            }
        });
    }

    //method that queries the data from the database
    private void SelectListItems(int id_item){

        if(!listPrices.isEmpty())
            listPrices.clear();

        //indicate using only read data
        SQLiteDatabase db_read = db.getReadableDatabase();

        Prices price = null;

        Cursor cursor = db_read.rawQuery("SELECT * FROM "+db.DB_PRICES_PUBLIC+" WHERE item="+id_item, null);

        String market = "", value = "";

        //go to all data of the table
        while(cursor.moveToNext()){

            Cursor cursor_market = db_read.rawQuery("SELECT * FROM "+db.DB_MARKETS_PUBLIC+" WHERE _id = "+cursor.getInt(2), null);

            while(cursor_market.moveToNext()) {
                market = cursor_market.getString(1);
            }

            price = new Prices(cursor.getInt(0), id_item, cursor.getDouble(3), market);

            //and add to the list
            listPrices.add(price);
        }
    }

    public void loadList(int id){
        SelectListItems(id);
        //create the adapter
        adapter = new ListPrices(listPrices);
        //and add the adapter to the recycler view
        rv_listPrices.setAdapter(adapter);
    }
}
