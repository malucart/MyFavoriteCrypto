package com.luiza.luizacryptotracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME_DATABASE = "FavoriteDB";
    private static final String FAVORITE_TABLE = "favDB";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String SYMBOL = "symbol";
    private static final String LOGO_URL = "logoURL";
    private static final String PRICE = "price";
    private static final String ONE_HOUR = "oneHour";
    private static final String TWENTY_FOUR_HOUR = "twentyFourHour";
    private static final String ONE_WEEK = "oneWeek";
    private static final String FAV_STATUS = "favStatus";
    private static String CREATE_FAVORITE_TABLE = "CREATE TABLE " + FAVORITE_TABLE + "(" +
            ID + " TEXT, " + NAME + " TEXT, " + SYMBOL + " TEXT, " +
            LOGO_URL + " TEXT, " + PRICE + " TEXT, " + ONE_HOUR + " TEXT, " +
            TWENTY_FOUR_HOUR + " TEXT, " + ONE_WEEK + " TEXT, " + FAV_STATUS + " TEXT)";

    public DatabaseHandler(Context context) {
        super(context, NAME_DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_FAVORITE_TABLE);
    }

    // necessary to not get an error to declare the class as an abstract
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // create empty table
    public void insertEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // enter your value
        for (int i = 1; i < 100; i++) {
            cv.put(ID, i);
            cv.put(FAV_STATUS, "0");

            db.insert(FAVORITE_TABLE,null, cv);
        }
    }

    // insert data into database
    public void insertDataIntoDatabase(CryptoModel model) {
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // cv.put(ID, id);
        cv.put(NAME, model.getName());
        cv.put(SYMBOL, model.getSymbol());
        cv.put(LOGO_URL, model.getLogoURL());
        cv.put(PRICE, model.getPrice());
        cv.put(ONE_HOUR, model.getOneHour());
        cv.put(TWENTY_FOUR_HOUR, model.getTwentyFourHour());
        cv.put(ONE_WEEK, model.getOneWeek());
        cv.put(FAV_STATUS, model.getFavStatus());
        db.insert(FAVORITE_TABLE,null, cv);
        Log.d("FavDB Status", model.getSymbol() + ", favStatus - " + model.getFavStatus() + " - . " + cv);

    }

    public ArrayList<CryptoModel> getFavListFromDatabase()
    {
        ArrayList<CryptoModel> favList = new ArrayList<CryptoModel>();

        // IDEA:
        // 1. Read all the favorites from the SQLite database using the query command and write the data
        // into the "favList" object and return it
        // 2. Only update the database when you first fetch the data from your online database (when you open app)
        // and when you close the app (or after a "time") so we save bandwidth

        SQLiteDatabase db = this.getReadableDatabase();


        return favList;
    }

    // read all data
    public Cursor readAllFavorite(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + FAVORITE_TABLE + " WHERE " + ID + " = " + id + "";
        return db.rawQuery(sql,null,null);
    }

    // remove line from database
    public void removeFavorite(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + FAVORITE_TABLE + " SET  " + FAV_STATUS + " = '0' WHERE " + ID + " = " + id + "";
        db.execSQL(sql);
        Log.d("remove", id.toString());
    }

    /*
    // select all favorite list
    public Cursor selectAllFavoriteList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + FAVORITE_TABLE + " WHERE " + FAV_STATUS + " = '1'";
        return db.rawQuery(sql,null,null);
    }
     */
}
