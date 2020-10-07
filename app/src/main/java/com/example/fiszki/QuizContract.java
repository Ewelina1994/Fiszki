package com.example.fiszki;

import android.provider.BaseColumns;

public final class QuizContract {
    private QuizContract() {
    }

    public static class QuestionTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_QUESTION="question";
        public static final String COLUMN_OPTIONS1="option1";
        public static final String COLUMN_OPTIONS2="option2";
        public static final String COLUMN_OPTIONS3="option3";
        public static final String COLUMN_ANSWER_NR="answer";
        public static final String COLUMN_DEFFICULTY="difficulty";
    }

    public static class StaticTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_static";
        public static final String COLUMN_LEVEL="level";
        public static final String COLUMN_score="score";
        public static final String COLUMN_wrong="wrong";
        public static final String COLUMN_date="date";
    }
}
