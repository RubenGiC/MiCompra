package org.rubengic.micompra;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

    //name db
    private String DB_NAME = "mimarket";

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

        ed_name = (EditText) findViewById(R.id.t_name_item);
        ed_price = (EditText) findViewById(R.id.t_price);
        sp_market = (Spinner) findViewById(R.id.sp_market);

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

        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase db = new DataBase(AddItem.this);
                SQLiteDatabase db_write = db.getWritableDatabase();

                if(db_write != null){
                    ContentValues cv = new ContentValues();
                    cv.put("name", ed_name.getText().toString());
                    cv.put("price", ed_price.getText().toString());
                    cv.put("market", sp_market.getSelectedItem().toString());

                    db_write.insert(DB_NAME,null, cv);
                }
            }
        });
    }
}