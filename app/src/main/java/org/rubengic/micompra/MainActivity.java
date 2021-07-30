package org.rubengic.micompra;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.rubengic.micompra.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private FloatingActionButton fab_add;

    private ArrayList<Items> listItems;
    private RecyclerView rv_listItems;

    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //create database, the list of items and the recycler view
        db = new DataBase(getApplicationContext());
        listItems = new ArrayList<>();
        rv_listItems = (RecyclerView) findViewById(R.id.rv_list_items);

        //manage layout
        rv_listItems.setLayoutManager(new LinearLayoutManager(this));

        //go to access to the database
        SelectListItems();

        //create the adapter
        ListaItems adapter = new ListaItems(listItems);
        //and add the adapter to the recycler view
        rv_listItems.setAdapter(adapter);

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Añadiendo nuevo producto", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                //add action add new item
                Intent i= new Intent(getApplicationContext(), AddItem.class);
                startActivity(i);
            }
        });
    }

    //method that queries the data from the database
    private void SelectListItems(){
        //indicate using only read data
        SQLiteDatabase db_read = db.getReadableDatabase();

        Items item = null;

        Cursor cursor = db_read.rawQuery("SELECT * FROM "+db.DB_PRICES_PUBLIC, null);

        //Toast.makeText(this, "prices --> "+cursor.getCount(), Toast.LENGTH_SHORT).show();

        String name_item = "", market = "";
        byte [] image = new byte[0];

        //go to all data of the table
        while(cursor.moveToNext()){

            //create item
            //item = new Items(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3));
            Cursor cursor_item = db_read.rawQuery("SELECT * FROM "+db.DB_ITEMS_PUBLIC+" WHERE _id = "+cursor.getInt(1), null);
            while(cursor_item.moveToNext()) {
                name_item = cursor_item.getString(1);
                image = cursor_item.getBlob(3);
                /*Toast.makeText(this, "1 --> " + cursor_item.getCount(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "2 --> " + cursor_item.getInt(cursor_item.getColumnIndex("_id")), Toast.LENGTH_SHORT).show();*/
            }
            //Toast.makeText(this, "2 --> "+name_item, Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "--> "+cursor_item.getString(1), Toast.LENGTH_SHORT).show();
            Cursor cursor_market = db_read.rawQuery("SELECT * FROM "+db.DB_MARKETS_PUBLIC+" WHERE _id = "+cursor.getInt(2), null);

            while(cursor_market.moveToNext()) {
                market = cursor_market.getString(1);
            }

            //Toast.makeText(this, "2 --> "+market, Toast.LENGTH_SHORT).show();

            item = new Items(cursor.getInt(0), name_item, cursor.getDouble(3), market, image);

            //and add to the list
            listItems.add(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}