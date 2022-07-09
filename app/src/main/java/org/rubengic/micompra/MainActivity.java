package org.rubengic.micompra;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.rubengic.micompra.Models.Items;
import org.rubengic.micompra.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab_add, fab_add_item, fab_add_price, fab_add_market;

    private ArrayList<Items> listItems;
    private RecyclerView rv_listItems;

    private boolean isFABOpen=false;

    private Animation fromBottom, toBottom, rotateOpen, rotateClose;

    private ListaItems adapter;

    private DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //create database, the list of items and the recycler view
        db = new DataBase(getApplicationContext());
        listItems = new ArrayList<>();
        rv_listItems = findViewById(R.id.rv_list_items);

        //manage layout
        rv_listItems.setLayoutManager(new LinearLayoutManager(this));

        loadList();

        setSupportActionBar(binding.toolbar);

        fab_add = findViewById(R.id.fab_add);
        fab_add_item = findViewById(R.id.fab_add_item);
        fab_add_price = findViewById(R.id.fab_add_price);
        fab_add_market = findViewById(R.id.fab_add_market);

        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_animation);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_animation);

        adapter.setOnItemListener(new ListaItems.OnItemListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                Intent i= new Intent(getApplicationContext(), ItemInfo.class);
                i.putExtra("id",adapter.getItem(position).getIdItem());
                startActivity(i);
            }

            @Override
            public void OnItemLongClickListener(View view, int position) {

                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setTitle("Borrar precio");

                ad.setMessage("¿Seguro que quieres borrar el precio "+adapter.getItem(position).getPrice()+"€, del producto "+adapter.getItem(position).getName()+", del supermercado "+adapter.getItem(position).getMarket()+"?");

                ad.setNegativeButton("No", null);

                ad.setPositiveButton("Si", (dialog, which) -> {
                    db.erasePrice(adapter.getItem(position).getId());
                    Toast.makeText(MainActivity.this, "Precio eliminado", Toast.LENGTH_SHORT).show();
                    loadList();
                });

                AlertDialog dialog = ad.create();

                dialog.show();
            }
        });

        //show or hidden the options to add
        fab_add.setOnClickListener(variable -> showOrHiddeFABS());

        fab_add_item.setOnClickListener(v -> {
            //add action add new item
            Intent i= new Intent(getApplicationContext(), AddItem.class);
            startActivity(i);
        });
        fab_add_market.setOnClickListener(v -> {
            //add action add new item
            Intent i= new Intent(getApplicationContext(), AddMarket.class);
            startActivity(i);
        });
        fab_add_price.setOnClickListener(v -> {
                //add action add new item
                Intent i= new Intent(getApplicationContext(), AddPrice.class);
                startActivity(i);
        });

    }

    //activate the animation of the float buttons
    private void showOrHiddeFABS() {
        setVisibility();
        setAnimation();
        isFABOpen=!isFABOpen;
    }

    //active or deactivate visibility
    private void setVisibility() {
        if(!isFABOpen){
            fab_add_item.setVisibility(View.VISIBLE);
            fab_add_price.setVisibility(View.VISIBLE);
            fab_add_market.setVisibility(View.VISIBLE);
        }else{
            fab_add_item.setVisibility(View.INVISIBLE);
            fab_add_price.setVisibility(View.INVISIBLE);
            fab_add_market.setVisibility(View.INVISIBLE);
        }
    }
    //show one or another animation
    private void setAnimation() {
        if(!isFABOpen){

            fab_add_item.startAnimation(fromBottom);
            fab_add_price.startAnimation(fromBottom);
            fab_add_market.startAnimation(fromBottom);

            fab_add.startAnimation(rotateOpen);
        }else{
            fab_add_item.startAnimation(toBottom);
            fab_add_price.startAnimation(toBottom);
            fab_add_market.startAnimation(toBottom);
            fab_add.startAnimation(rotateClose);
        }
    }

    //method that queries the data from the database
    private void SelectListItems(){

        if(!listItems.isEmpty())
            listItems.clear();

        //indicate using only read data
        SQLiteDatabase db_read = db.getReadableDatabase();

        Items item;

        String name_item, market = "";
        byte [] image;
        double price_value = 0.0;
        int id_item, id_price = -1;

        Cursor cursor_items = db_read.rawQuery("SELECT * FROM "+db.DB_ITEMS_PUBLIC, null);

        //go to all data of the table
        while(cursor_items.moveToNext()){

            //count the number of prices of this item
            Cursor cursor_prices_count = db_read.rawQuery("SELECT * FROM "+db.DB_PRICES_PUBLIC+" WHERE item = "+cursor_items.getInt(0), null);

            if(cursor_prices_count.getCount()>0) {
                //access the lowest price of this item
                Cursor cursor_prices = db_read.rawQuery("SELECT DISTINCT item, MIN(price), market, _id FROM "+db.DB_PRICES_PUBLIC+" WHERE item = "+cursor_items.getInt(0)+" ORDER BY price ASC", null);

                //add the name, image and id of item
                name_item = cursor_items.getString(1);
                image = cursor_items.getBlob(3);
                id_item = cursor_items.getInt(0);

                while (cursor_prices.moveToNext()) {

                    id_price = cursor_prices.getInt(3);
                    price_value = cursor_prices.getDouble(1);

                    Cursor cursor_market = db_read.rawQuery("SELECT * FROM "+db.DB_MARKETS_PUBLIC+" WHERE _id = "+cursor_prices.getInt(2), null);

                    while(cursor_market.moveToNext())
                        market = cursor_market.getString(1);

                    //close cursor_market
                    cursor_market.close();
                }
                //create the object item
                item = new Items(id_price, name_item, price_value, market, image, id_item);

                //and add to the list
                listItems.add(item);

                //close cursors
                cursor_prices.close();
            }
            //close cursors
            cursor_items.close();
            cursor_prices_count.close();

        }
    }

    //action to the activiti to select item to erase
    public void openDeleteItem(){
        Intent i= new Intent(getApplicationContext(), EraseItem.class);
        startActivity(i);
    }
    //action to the activiti to select market to erase
    public void openDeleteMarket(){
        Intent i= new Intent(getApplicationContext(), EraseMarket.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.erase_item:
                openDeleteItem();
                return true;
            case R.id.erase_market:
                openDeleteMarket();
                return true;
            case R.id.shopping_list:
                Toast.makeText(this, "Muestra la lista completa", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.copy_sql:
                String path_db = getDatabasePath(DataBase.BD_NAME).toString();//Error in read database dont find it
                //copy the local data base in storage
                if(copyFile(path_db,getApplicationContext().getExternalFilesDir(null).getPath()+"Android/data"+getPackageName()+"db_mimarket.sqlite")) {
                    Toast.makeText(this, "Copia creada", Toast.LENGTH_SHORT).show();
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean copyFile(String from, String to){
        boolean result = false;
        try{
            //obtengo el directorio a crear
            File dir = new File(to.substring(0,to.lastIndexOf('/')));
            dir.mkdir();
            //y el archivo a crear
            File tof = new File(dir, to.substring(to.lastIndexOf('/')+1));
            int byteread;
            //compruebo si existe ya el archivo
            File oldfile = new File(from);
            if(oldfile.exists()){
                //creamos los flujos de lectura y escritura
                InputStream inStream = new FileInputStream(from);
                FileOutputStream fs = new FileOutputStream(tof);
                byte[] buffer = new byte[1024];
                //recorremos el contenido
                while((byteread = inStream.read(buffer)) != -1){
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();//cerramos el flujo de lectura
                fs.close();//cerramos el flujo de escritura
            }
            result = true;
            /* TERMINAR COPIA BASE DE DATOS
            https://es.stackoverflow.com/questions/333989/como-hacer-una-copia-de-una-base-de-datos-sqlite-y-compartirla
            https://es.stackoverflow.com/questions/79010/como-copiar-una-base-de-datos-sqlite-o-archivo-desde-el-almacenamiento-interno
             */
        }catch(Exception e){
            Log.e("copyFile","Error copiando el archivo: "+e.getMessage());
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    public void loadList(){
        SelectListItems();
        //create the adapter
        adapter = new ListaItems(listItems);
        //and add the adapter to the recycler view
        rv_listItems.setAdapter(adapter);
    }
}