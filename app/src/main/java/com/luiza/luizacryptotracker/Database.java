package com.luiza.luizacryptotracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
/*

public class Database extends SQLiteOpenHelper {



    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
            FeedEntry._ID + " INTEGER PRIMARY KEY," +
            FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
            FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";
    // public static final String BDname = "mdata.db";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS favorite");
        onCreate(db);

    }


    public Boolean Insert_to_favorite(String name, String img, String url, String num, String size) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("img", img);
        contentValues.put("url", url);
        contentValues.put("num", num);
        contentValues.put("size", size);


        long result = db.insert("favorite", null, contentValues);

        if (result == -1)
            return false;
        else
            return true;

    }


    public List getAllList_Favorite() {
        List<listitem_gib> list = new ArrayList<listitem_gib>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from favorite", null);

        if (rs.moveToFirst()) {
            do {
                listitem_gib model = new listitem_gib();
                model.setId(rs.getString(0));
                model.setName(rs.getString(1));
                model.seturl(rs.getString(3));
                model.setimg(rs.getString(2));
                model.setnum(rs.getString(4));
                model.setsize(rs.getString(5));

                list.add(model);
            } while (rs.moveToNext());
        }
        Log.d("rewayat data", list.toString());
        return list;
    }


    public int get_check_List_Favorite(String Title) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from favorite Where name like '" + Title + "'", null);
        rs.moveToFirst();
        int count = rs.getCount();
        return count;
    }


    public Integer DeleteFav(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("favorite", "id = ?", new String[]{id});
    }
}

 */