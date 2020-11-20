package com.example.fiszki.db;

import android.provider.BaseColumns;

public class OptionContract {
    private OptionContract() {
    }

    public static class OptionTable implements BaseColumns {
        public static final String TABLE_NAME="option";
        public static final String COLUMN_QUESTION_ID="question_id";
        public static final String COLUMN_OPTION="option_text";
        public static final String COLUMN_IS_RIGHT="is_right";
        public static final String COLUMN_LANGUAGE="language";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + OptionTable.TABLE_NAME + " (" +
                    OptionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    OptionTable.COLUMN_QUESTION_ID + " INTEGER, " +
                    OptionTable.COLUMN_OPTION + " TEXT, " +
                    OptionTable.COLUMN_IS_RIGHT + " BOOLEAN, " +
                    OptionTable.COLUMN_LANGUAGE + " TEXT, "+
                    "FOREIGN KEY (question_id) REFERENCES question (_id));";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserContract.UserTable.TABLE_NAME + ";";
}
