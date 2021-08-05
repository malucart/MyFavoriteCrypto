package com.luiza.luizacryptotracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.luiza.luizacryptotracker.model.CryptoModel;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    private static final String TAG = "DatabaseHandler:";
    private static final String NAME_DATABASE = "MyFavoriteDB";
    private static final String FAVORITE_TABLE = "myFavDB";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String SYMBOL = "symbol";
    private static final String LOGO_URL = "logoURL";
    private static final String PRICE = "price";
    private static final String ONE_HOUR = "oneHour";
    private static final String TWENTY_FOUR_HOUR = "twentyFourHour";
    private static final String ONE_WEEK = "oneWeek";
    private static final String OBJECT_ID = "objectId";
    private static final String CREATE_FAVORITE_TABLE = "CREATE TABLE " + FAVORITE_TABLE + " (" +
            ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, " + SYMBOL + " TEXT, " + LOGO_URL +
            " TEXT, " + PRICE + " TEXT, " + ONE_HOUR + " TEXT, " + TWENTY_FOUR_HOUR + " TEXT, " +
            ONE_WEEK + " TEXT, " + OBJECT_ID + " TEXT)";

    private static boolean isFirstInit = true;

    public static SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME_DATABASE, null, VERSION);

        // method to execute sql query
        if (isFirstInit) {
            SQLiteDatabase db = this.getWritableDatabase();

            // This will pull the remote database, if we want to plan offline support,
            // we can just not call db.delete whenever we detect no internet access
            db.delete(FAVORITE_TABLE, null, null);
            isFirstInit = false;
        }
    }

    // method returns an Instance of the Database
    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_FAVORITE_TABLE);
    }

    // necessary to not get an error to declare the class as an abstract
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void cleanDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FAVORITE_TABLE, null, null);
    }

    // insert data into database
    public void insertDataIntoDatabase(@NonNull CryptoModel model) {
        // writes data in the database
        SQLiteDatabase db = this.getWritableDatabase();
        // creates a variable for content values
        ContentValues cv = new ContentValues();
        // passing all values along with its key and value pair
        cv.put(NAME, model.getName());
        cv.put(SYMBOL, model.getSymbol());
        cv.put(LOGO_URL, model.getLogoURL());
        cv.put(PRICE, model.getPrice().toString());
        cv.put(ONE_HOUR, model.getOneHour().toString());
        cv.put(TWENTY_FOUR_HOUR, model.getTwentyFourHour().toString());
        cv.put(ONE_WEEK, model.getOneWeek().toString());
        cv.put(OBJECT_ID, model.getObjectId());
        // after adding all values it passes content values to the table
        db.insert(FAVORITE_TABLE,null, cv);
        // closing database after adding into the database
        db.close();
    }

    // read data from the database
    public ArrayList<CryptoModel> getFavListFromDatabase() {
        // database for reading our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // creates a cursor with query to read data from the database
        // rawQuery reads queries
        Cursor cursor = db.rawQuery("select * from " + FAVORITE_TABLE, null);
        // creates a new array list
        ArrayList<CryptoModel> favList = new ArrayList<>();
        // moving the cursor to first position
        if (cursor.moveToFirst()) {
            do {
                CryptoModel aux = new CryptoModel(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getDouble(7));
                aux.setObjectId(cursor.getString(8));
                // adds the data from cursor to the array list created
                favList.add(aux);
            } while (cursor.moveToNext());
            // moving the cursor to next
        }

        // closes the cursor and returns the array list
        cursor.close();
        db.close();
        return favList;
    }

    // remove from database
    public void removeFavorite(String symbol) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FAVORITE_TABLE, "SYMBOL = ?", new String[] {symbol});
        db.close();
    }
}
