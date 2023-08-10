package com.example.starwarsapp1.db;

import android.provider.BaseColumns;

public final class MyConstans {

    private MyConstans() {}

    public static class myConstansR implements BaseColumns {
        public static final String TABLE_NAME = "my_favourites";
        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String ADDITIONAL_INFORMATION = "addinfo";

        public static final String FAVOURITE_DATA = "isFav";

        public static final String DB_NAME = "favourites_db.db";
        public static final int DB_VERSION = 1;
        public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, " +
                DESCRIPTION + " TEXT, " + FAVOURITE_DATA  + " TEXT, " + ADDITIONAL_INFORMATION + " TEXT)";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}

