package com.example.fiszki.db;

import android.provider.BaseColumns;

public class UserContract {

    private UserContract() {
    }

    public static class UserTable implements BaseColumns {
        public static final String TABLE_NAME="user";
        public static final String COLUMN_NICK="nick";
        public static final String COLUMN_LEVEL="level";
        public static final String COLUMN_YEARS_OF_STUDY="years_of_study";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserTable.TABLE_NAME + " (" +
                    UserTable._ID + " INTEGER PRIMARY KEY, " +
                    UserTable.COLUMN_NICK + " TEXT, " +
                    UserTable.COLUMN_LEVEL + " TEXT, " +
                    UserTable.COLUMN_YEARS_OF_STUDY + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserTable.TABLE_NAME + ";";

}
