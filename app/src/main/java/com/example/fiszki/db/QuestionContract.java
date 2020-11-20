package com.example.fiszki.db;

import android.provider.BaseColumns;

import java.sql.Blob;

public class QuestionContract {
    private QuestionContract() {
    }

    public static class QuestionTable implements BaseColumns {
        public static final String TABLE_NAME="question";
        public static final String COLUMN_QUESTION="question";
        public static final String COLUMN_IMG="image";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + QuestionContract.QuestionTable.TABLE_NAME + " (" +
                    QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QuestionTable.COLUMN_QUESTION + " TEXT, "+
                    QuestionTable.COLUMN_IMG + " BLOB);";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserContract.UserTable.TABLE_NAME + ";";
}
