package org.rubengic.micompra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddItem extends AppCompatActivity {

    //directory app
    private String APP_DIRECTORY = "ImagesMiCompra";
    //directory media
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    //directory where the image save temporality
    private String TEMPORAL_PICTURE_NAME = "temp.jpg";

    //code image (code respond)
    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;

    //for image
    private ImageView img_v;
    private Bitmap imageBitmap;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    //for information
    private EditText ed_name;
    private EditText ed_price;
    private Spinner sp_market;

    private Toolbar my_toolbar;

    DataBase db;

    //take the picture
    private void dispatchTakePictureIntent() {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), MEDIA_DIRECTORY);
        //create directories
        file.mkdirs();
        //create the path
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;

        File newfile = new File(path);

        //create intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if isn't null the take picture
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //capture and save image
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newfile));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //create image and show
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            img_v.setImageBitmap(imageBitmap);
        }
    }
    //path of the picture
    String currentPhotoPath;
    //save the image
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        //create database
        db = new DataBase(getApplicationContext());

        //change toolbar by my personalizate
        my_toolbar = findViewById(R.id.custom_toolbar_add_item);
        setSupportActionBar(my_toolbar);
        //getSupportActionBar().setTitle("Añadir Producto");

        ed_name = (EditText) findViewById(R.id.t_name_item);
        ed_price = (EditText) findViewById(R.id.t_price);
        sp_market = (Spinner) findViewById(R.id.sp_market);

        ArrayList <String> list_markets = new ArrayList<>();

        //indicate using only read data
        SQLiteDatabase db_read = db.getReadableDatabase();

        Cursor cursor = db_read.rawQuery("SELECT * FROM "+db.DB_MARKETS_PUBLIC, null);

        //go to all data of the table
        while(cursor.moveToNext()){
            list_markets.add(cursor.getString(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list_markets);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_market.setAdapter(adapter);

        //charge the imagen view and button
        img_v = (ImageView) findViewById(R.id.i_image);
        Button b = (Button) findViewById(R.id.b_add_image);
        Button b_add = (Button) findViewById(R.id.b_add_item);

        //action click of the button
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //take a picture
                dispatchTakePictureIntent();
                //options
                //final CharSequence[] options = {"hacer foto", "Elegir foto de galeria", "Cancelar"};
                //Dialog for choose options
               /* final AlertDialog.Builder builder = new AlertDialog.Builder(AddItem.this);
                //add title
                builder.setTitle("Elija una opcion");
                //add the options
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(options[which] == "hacer foto"){
                            openCamera();
                        }else if(options[which] == "Elegir foto de galeria"){
                            //create the intent by access to galery
                            Intent intento = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            //acept all types of images
                            intento.setType("image/*");
                            //choose the app to select the picture
                            startActivityForResult(intento.createChooser(intento, "Selecciona la app de imagenes"), SELECT_PICTURE);

                        }else{
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();*/
            }
        });

        //add in data base the item
        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //access and permission to write data base
                DataBase db = new DataBase(AddItem.this);
                SQLiteDatabase db_write = db.getWritableDatabase();
                SQLiteDatabase db_read = db.getWritableDatabase();

                Cursor exist = db_read.rawQuery("SELECT * FROM "+db.DB_ITEMS_PUBLIC+" WHERE UPPER(name) = UPPER('"+ed_name.getText().toString()+"')", null);

                if(db_write != null && exist.getCount() == 0){
                    //add values
                    ContentValues cv_item = new ContentValues();
                    ContentValues cv_price = new ContentValues();
                    cv_item.put("name", ed_name.getText().toString());
                    cv_price.put("price", ed_price.getText().toString());

                    //insert in database
                    long id_item = db_write.insert(db.DB_ITEMS_PUBLIC,null, cv_item);

                    //convert to id
                    cv_price.put("item", String.valueOf(id_item));
                    Integer id_market = Math.toIntExact(sp_market.getSelectedItemId() + 1);
                    cv_price.put("market", id_market.toString());

                    db_write.insert(db.DB_PRICES_PUBLIC,null, cv_price);
                    //show to add
                    Toast.makeText(AddItem.this, "Añadido Producto", Toast.LENGTH_SHORT).show();
                    //and back to main layout
                    AddItem.super.onBackPressed();
                }else{
                    Toast.makeText(AddItem.this, "El producto "+ed_name.getText().toString()+" ya existe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        my_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItem.super.onBackPressed();
            }
        });
    }
}