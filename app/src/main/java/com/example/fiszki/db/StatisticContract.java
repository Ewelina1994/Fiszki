package com.example.fiszki.db;

import android.provider.BaseColumns;

public class StatisticContract {
    private StatisticContract() {
    }

    public static class StatisticTable implements BaseColumns {
        public static final String TABLE_NAME="statistic";
        public static final String COLUMN_score="score";
        public static final String COLUMN_wrong="wrong";
        public static final String COLUMN_LEVEL="level";
        public static final String COLUMN_date="date";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + StatisticContract.StatisticTable.TABLE_NAME + " (" +
                    StatisticContract.StatisticTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    StatisticContract.StatisticTable.COLUMN_score + " INTEGER, " +
                    StatisticContract.StatisticTable.COLUMN_wrong + " INTEGER, " +
                    StatisticContract.StatisticTable.COLUMN_LEVEL + " TEXT, " +
                    StatisticContract.StatisticTable.COLUMN_date + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StatisticContract.StatisticTable.TABLE_NAME + ";";
}
