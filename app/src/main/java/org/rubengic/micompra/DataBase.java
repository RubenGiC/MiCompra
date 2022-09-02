package org.rubengic.micompra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import org.rubengic.micompra.Models.ModelImage;

import java.io.ByteArrayOutputStream;

public class DataBase extends SQLiteOpenHelper {
    //CREATE THE DB
    private static final String MIMARKET_CREATE_TABLE = "CREATE TABLE mimarket (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
    private static final String MIITEMS_CREATE_TABLE = "CREATE TABLE miitem (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, path TEXT)";
    private static final String MIPRICES_CREATE_TABLE = "CREATE TABLE miprice (_id INTEGER PRIMARY KEY AUTOINCREMENT, item INTEGER, market INTEGER, price DOUBLE)";
    //name the file
    protected static final String BD_NAME = "mimarket.sqlite";
    //number of version
    private static final int VERSION = 5;

    //necessary to save image
    private ByteArrayOutputStream ob_byte_array_os;
    private byte[] imageInBytes;

    Context context;

    //name db
    public String DB_MARKETS_PUBLIC = "mimarket";
    public String DB_ITEMS_PUBLIC = "miitem";
    public String DB_PRICES_PUBLIC = "miprice";

    public DataBase(Context context) {
        super(context, BD_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //execute Code SQL
            db.execSQL(MIMARKET_CREATE_TABLE);
            db.execSQL(MIITEMS_CREATE_TABLE);
            db.execSQL(MIPRICES_CREATE_TABLE);

            db.execSQL("INSERT INTO mimarket (_id, name) VALUES (1, 'Coviran')");
            db.execSQL("INSERT INTO mimarket (_id, name) VALUES (2, 'Carrefour')");
            db.execSQL("INSERT INTO mimarket (_id, name) VALUES (3, 'Dia')");
            db.execSQL("INSERT INTO mimarket (_id, name) VALUES (4, 'Lidl')");
            db.execSQL("INSERT INTO mimarket (_id, name) VALUES (5, 'Mercadona')");
        }catch (Exception e){
            Toast.makeText(context, "Error DATABASE: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_ITEMS_PUBLIC);
        db.execSQL("DROP TABLE IF EXISTS " + DB_MARKETS_PUBLIC);
        db.execSQL("DROP TABLE IF EXISTS " + DB_PRICES_PUBLIC);
        onCreate(db);
    }

    public void storeImage(ModelImage m_image){
        try{
            //access database write
            SQLiteDatabase db_write = this.getWritableDatabase();
            //create image bitmap
            Bitmap imageToStoreBitmap=m_image.getBitmap_img();

            ob_byte_array_os = new ByteArrayOutputStream();
            //and compress in format JPEG
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,ob_byte_array_os);

            //transform the image content in bytes
            imageInBytes = ob_byte_array_os.toByteArray();
            //and create the content to save in database
            ContentValues cv_item = new ContentValues();

            cv_item.put("image_name", m_image.getName_img());
            cv_item.put("image", imageInBytes);

            long id_item = db_write.insert(DB_ITEMS_PUBLIC,null, cv_item);

        }catch (Exception e){
            Toast.makeText(context, "Error IMAGE STORE: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public long insertItem(String name, String path){//ModelImage m_image

        try{
            //access database write
            SQLiteDatabase db_write = this.getWritableDatabase();

            //and create the content to save in database
            ContentValues cv_item = new ContentValues();

            cv_item.put("name", name);
            cv_item.put("path", path);

            long id_item = db_write.insert(DB_ITEMS_PUBLIC,null, cv_item);

            return id_item;

        }catch (Exception e){
            Toast.makeText(context, "Error INSERT ITEM: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return -1;
    }

    public long insertPrice(String item, String market, String price){

        try{
            //access database write
            SQLiteDatabase db_write = this.getWritableDatabase();

            //and create the content to save in database
            ContentValues cv_price = new ContentValues();


            cv_price.put("item", item);
            cv_price.put("market", market);
            cv_price.put("price", price);

            long id_price = db_write.insert(DB_PRICES_PUBLIC,null, cv_price);

            return id_price;

        }catch (Exception e){
            Toast.makeText(context, "Error INSERT PRICE: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return -1;
    }

    public long insertMarket(String name){

        try{
            //access database write
            SQLiteDatabase db_write = this.getWritableDatabase();

            //and create the content to save in database
            ContentValues cv_market = new ContentValues();


            cv_market.put("name", name);

            long id_market = db_write.insert(DB_MARKETS_PUBLIC,null, cv_market);

            return id_market;

        }catch (Exception e){
            Toast.makeText(context, "Error INSERT MARKET: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return -1;
    }

    public long insertPrice(String price, int id_market, int id_item){

        try{
            //access database write
            SQLiteDatabase db_write = this.getWritableDatabase();

            //and create the content to save in database
            ContentValues cv_price = new ContentValues();


            cv_price.put("item", String.valueOf(id_item));
            cv_price.put("market", String.valueOf(id_market));
            cv_price.put("price", price);

            long id_price = db_write.insert(DB_PRICES_PUBLIC,null, cv_price);

            return id_price;

        }catch (Exception e){
            Toast.makeText(context, "Error INSERT PRICE: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return -1;
    }

    public boolean existPrice(int item, int market){
        try{
            SQLiteDatabase db_read = getWritableDatabase();

            //search the item and market in the table miprices
            Cursor exist = db_read.rawQuery("SELECT * FROM "+DB_PRICES_PUBLIC+" WHERE item = "+item+" and market = "+market, null);

            //if not foud return false else return true
            if(exist.getCount() == 0)
                return false;
        }catch (Exception e){
            Toast.makeText(context, "Error EXIST PRICE: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public boolean existItem(String name){
        try{
            SQLiteDatabase db_read = getWritableDatabase();

            Cursor exist = db_read.rawQuery("SELECT * FROM "+DB_ITEMS_PUBLIC+" WHERE UPPER(name) = UPPER('"+name+"')", null);
            if(exist.getCount() == 0)
                return false;
        }catch (Exception e){
            Toast.makeText(context, "Error EXIST ITEM: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public boolean existMarket(String name){
        try{
            SQLiteDatabase db_read = getWritableDatabase();

            Cursor exist = db_read.rawQuery("SELECT * FROM "+DB_MARKETS_PUBLIC+" WHERE UPPER(name) = UPPER('"+name+"')", null);
            if(exist.getCount() == 0)
                return false;
        }catch (Exception e){
            Toast.makeText(context, "Error EXIST ITEM: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public int id_Item(String name){
        try{
            SQLiteDatabase db_read = getWritableDatabase();

            Cursor item = db_read.rawQuery("SELECT * FROM "+DB_ITEMS_PUBLIC+" WHERE UPPER(name) = UPPER('"+name+"')", null);

            if(item.getCount() != 0)
                while(item.moveToNext()) {
                    return item.getInt(0);
                }
        }catch (Exception e){
            Toast.makeText(context, "Error RETURN ID ITEM: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return -1;
    }

    public int id_Market(String name){
        try{
            SQLiteDatabase db_read = getWritableDatabase();

            Cursor market = db_read.rawQuery("SELECT * FROM "+DB_MARKETS_PUBLIC+" WHERE UPPER(name) = UPPER('"+name+"')", null);

            if(market.getCount() != 0)
                while(market.moveToNext()) {
                    return market.getInt(0);
                }
        }catch (Exception e){
            Toast.makeText(context, "Error RETURN ID MARKET: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return -1;
    }

    public boolean erasePrice(long id){
        try {
            //access database write
            SQLiteDatabase db_write = this.getWritableDatabase();

            db_write.execSQL("DELETE FROM " + DB_PRICES_PUBLIC + " WHERE _id=" + id);
        }catch (Exception e){
            Toast.makeText(context, "Error DELETE PRICE: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public boolean eraseItem(long id){

        try {
            //access database write
            SQLiteDatabase db_write = this.getWritableDatabase();

            db_write.execSQL("DELETE FROM " + DB_PRICES_PUBLIC + " WHERE item=" + id);

            db_write.execSQL("DELETE FROM " + DB_ITEMS_PUBLIC + " WHERE _id=" + id);
            return true;
        }catch (Exception e){
            Toast.makeText(context, "Error DELETE ITEM: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public boolean eraseMarket(long id){

        try{
            //access database write
            SQLiteDatabase db_write = this.getWritableDatabase();

            db_write.execSQL("DELETE FROM "+DB_PRICES_PUBLIC+" WHERE market="+id);

            db_write.execSQL("DELETE FROM "+DB_MARKETS_PUBLIC+" WHERE _id="+id);
            return true;
        }catch (Exception e){
            Toast.makeText(context, "Error DELETE MARKET: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}
