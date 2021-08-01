package org.rubengic.micompra;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddMarket extends AppCompatActivity {

    private EditText ed_name;

    private Toolbar my_toolbar;

    private DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_market);

        //create database
        db = new DataBase(getApplicationContext());

        //change toolbar by my personalizate
        my_toolbar = findViewById(R.id.custom_toolbar_add_item);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Añadir Supermercado");

        ed_name = (EditText) findViewById(R.id.t_name_market);

        Button b_add_market = (Button) findViewById(R.id.b_add_market);

        b_add_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMarket();
            }
        });

        my_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMarket.super.onBackPressed();
            }
        });
    }

    private void insertMarket(){

        //check that it is not empty
        if(!ed_name.getText().toString().isEmpty()){
            //check that it is not in the database
            if(!db.existMarket(ed_name.getText().toString())){
                //and insert the new market
                if(db.insertMarket(ed_name.getText().toString())!=-1){
                    Toast.makeText(AddMarket.this, "Supermercado Añadido", Toast.LENGTH_SHORT).show();
                    //and back to main layout
                    AddMarket.super.onBackPressed();
                }
            }else{
                Toast.makeText(AddMarket.this, "El supermercado ya existe", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(AddMarket.this, "Necesita un nombre para añadir el supermercado", Toast.LENGTH_SHORT).show();
        }
    }
}
