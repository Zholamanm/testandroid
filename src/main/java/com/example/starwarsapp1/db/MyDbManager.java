package com.example.starwarsapp1.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.starwarsapp1.adapters.DbModel;

import java.util.ArrayList;
import java.util.List;

public class MyDbManager {
    private Context context;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;

    public MyDbManager(Context context) {
        this.context = context;
        myDbHelper = new MyDbHelper(context);
    }

    public void openDb() {
        db = myDbHelper.getWritableDatabase();
    }
    public void insertToDb(String name, String description, String addinfo, String isFav) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstans.myConstansR.NAME, name);
        cv.put(MyConstans.myConstansR.DESCRIPTION, description);
        cv.put(MyConstans.myConstansR.ADDITIONAL_INFORMATION, addinfo);
        cv.put(MyConstans.myConstansR.FAVOURITE_DATA, isFav);

        // First, check if the data with the same name exists in the database
        String selection = MyConstans.myConstansR.NAME + " = ?";
        String[] selectionArgs = {name};
        Cursor cursor = db.query(
                MyConstans.myConstansR.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            // Data already exists, update the "isFav" value
            int idColumnIndex = cursor.getColumnIndex(MyConstans.myConstansR._ID);
            long id = cursor.getLong(idColumnIndex);
            @SuppressLint("Range") String currentIsFav = cursor.getString(cursor.getColumnIndex(MyConstans.myConstansR.FAVOURITE_DATA));

            if (!currentIsFav.equals(isFav)) {
                ContentValues updateCv = new ContentValues();
                updateCv.put(MyConstans.myConstansR.FAVOURITE_DATA, isFav);
                String updateSelection = MyConstans.myConstansR._ID + " = ?";
                String[] updateSelectionArgs = {String.valueOf(id)};
                db.update(
                        MyConstans.myConstansR.TABLE_NAME,
                        updateCv,
                        updateSelection,
                        updateSelectionArgs
                );
            }
        } else {
            // Data does not exist, insert new data into the database
            db.insert(MyConstans.myConstansR.TABLE_NAME, null, cv);
        }

        cursor.close();
    }

    public List<DbModel> getFromDb() {
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        List<DbModel> tempList = new ArrayList<>();
        Cursor cursor = db.query(MyConstans.myConstansR.TABLE_NAME, null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") long itemId = cursor.getLong(cursor.getColumnIndex(MyConstans.myConstansR._ID));
            @SuppressLint("Range")String name = cursor.getString(cursor.getColumnIndex(MyConstans.myConstansR.NAME));
            @SuppressLint("Range")String description = cursor.getString(cursor.getColumnIndex(MyConstans.myConstansR.DESCRIPTION));
            @SuppressLint("Range")String additionalInfo = cursor.getString(cursor.getColumnIndex(MyConstans.myConstansR.ADDITIONAL_INFORMATION));
            @SuppressLint("Range")String isFav = cursor.getString(cursor.getColumnIndex(MyConstans.myConstansR.FAVOURITE_DATA));
            Log.d("DB_DATA", "Name: " + name + ", isFav: " + isFav);
            tempList.add(new DbModel(itemId, name, description, additionalInfo, isFav));
        }
        cursor.close();
        return tempList;
    }

    @SuppressLint("Range")
    public String getIsFavFromDb(String name) {
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        String[] projection = {
                MyConstans.myConstansR.FAVOURITE_DATA
        };
        String selection = MyConstans.myConstansR.NAME + " = ?";
        String[] selectionArgs = {name};
        Cursor cursor = db.query(
                MyConstans.myConstansR.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        String isFav = "0"; // Default value if not found in the database
        if (cursor.moveToFirst()) {
            isFav = cursor.getString(cursor.getColumnIndex(MyConstans.myConstansR.FAVOURITE_DATA));
        }
        cursor.close();
        return isFav;
    }

    public void updateFavStatus(String name, String isFav) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstans.myConstansR.FAVOURITE_DATA, isFav);
        String selection = MyConstans.myConstansR.NAME + " = ?";
        String[] selectionArgs = {name};
        db.update(
                MyConstans.myConstansR.TABLE_NAME,
                cv,
                selection,
                selectionArgs
        );
    }

    public void closeDb() {
        myDbHelper.close();
    }
}
