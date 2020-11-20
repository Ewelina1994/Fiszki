package com.example.fiszki.db;

import android.provider.BaseColumns;

public class RepeatQuestionContract {
    public RepeatQuestionContract() {
    }

    public static class RepeatQuestionTable implements BaseColumns {
        public static final String TABLE_NAME="repeat_question";
        public static final String COLUMN_question="question_id";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RepeatQuestionTable.TABLE_NAME + " (" +
                    RepeatQuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RepeatQuestionTable.COLUMN_question + " INTEGER, "+
                    "FOREIGN KEY (question_id) REFERENCES question (_id));";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RepeatQuestionTable.TABLE_NAME + ";";
}
