package com.example.fiszki;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.example.fiszki.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuizIdiom.db";
    private static final int DATABASE_VERSION=1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " + QuestionTable.TABLE_NAME + " (" +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTIONS1 + " TEXT, " +
                QuestionTable.COLUMN_OPTIONS2 + " TEXT, " +
                QuestionTable.COLUMN_OPTIONS3 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionTable.COLUMN_DEFFICULTY + " TEXT" +
                ")";
        final String SQL_CREATE_QUESTIONS_STATISTIC = "CREATE TABLE " + StaticTable.TABLE_NAME + " (" +
                StaticTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StaticTable.COLUMN_LEVEL + " TEXT, " +
                StaticTable.COLUMN_score + " INT, " +
                StaticTable.COLUMN_wrong + " INT,"+
                StaticTable.COLUMN_date + " TEXT"+
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_STATISTIC);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+QuestionTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+StaticTable.TABLE_NAME);
        onCreate(db);
    }
    private void fillQuestionsTable() {
        Question q1= new Question("You look a little down in the dumps.", "You are looking down.", "You look sad.", "You look great", 2, Question.DIFFICULTY_EASY);
        insertQuestions(q1);
        Question q2= new Question("He comes across as a military commander.", "He acts like a military commander.", "He is a military commander.", "He wants to join the military.", 1, Question.DIFFICULTY_EASY);
        insertQuestions(q2);
        Question q3= new Question("Just cheer up and stick with a positive attitude.", "Feel better and get a positivity stick.", "Smile upwards and maintain a positive attitude.", "Feel better and maintain positive thoughts.", 3, Question.DIFFICULTY_MEDIUM);
        insertQuestions(q3);
        Question q4= new Question("It has been one thing after another lately.", "Things have been happening lately.", "If one more thing happens, it will happen too late.", "Things have happened close together in time.", 3, Question.DIFFICULTY_HARD);
        insertQuestions(q4);
        Question q5= new Question("Maybe it will even change my luck, knock on wood.", "Maybe it will change my luck. I knock on wood to give myself luck.", "Maybe it will change my luck and someone's knocking.", "Maybe it will change my luck if I knock on the door.", 1, Question.DIFFICULTY_HARD);
        insertQuestions(q5);
        Question q6= new Question("Please give it a rest.", "Please stop talking about that topic.", "Please lay down.", "Please stop talking and lay down.", 1, Question.DIFFICULTY_EASY);
        insertQuestions(q6);
        Question q7= new Question("I think I stand a change of getting that job.", "I think I'll wait in line for that job.", "I think I might get that job.", "I think I'll stand and think about the job.", 3, Question.DIFFICULTY_EASY);
        insertQuestions(q7);
        Question q8= new Question("I'm sticking to the new diet.", "I'm obeying the new diet.", "I'm sticking to the kitchen counter.", "I'm on a new diet of sticky rice..", 1, Question.DIFFICULTY_EASY);
        insertQuestions(q8);
        Question q9= new Question("This weather leaves a lot to be desired.", "This weather is gloomy.", "I don't like this weather.", "This weather is rainy.", 2, Question.DIFFICULTY_MEDIUM);
        insertQuestions(q9);
        Question q10= new Question("We need to get our ducks in a row.", "We need to hurry up, like ducks flying.", "We need to get organized.", "We need to position our ducks.", 2, Question.DIFFICULTY_MEDIUM);
        insertQuestions(q10);
        Question q11= new Question("Thanks for taking a rain check.", "Thanks for meeting me on such a rainy day.", "Thanks for writing me a check while it's raining.", "Thanks for agreeing to postpone our appointment.", 3, Question.DIFFICULTY_MEDIUM);
        insertQuestions(q11);
        Question q12= new Question("We don't want our team to come under fire.", "We don't want our team to get destroyed.", "We don't want our team to get assaulted.", "We don't want our team to get public criticism.", 3, Question.DIFFICULTY_MEDIUM);
        insertQuestions(q12);
        Question q13= new Question("My friend and I are going to hit the road this summer.", "My friend and I are going to help repair the road.", "My friend and I are going on a long walk this summer.", "My friend and I are going to travel from town to town this summer.", 2, Question.DIFFICULTY_HARD);
        insertQuestions(q13);
        Question q14= new Question("For crying out loud!", "I am crying!", "I am crazy!", "I am frustrated!", 3, Question.DIFFICULTY_HARD);
        insertQuestions(q14);
        Question q15= new Question("I won't beat around the bush.", "I won't walk around a bush.", "I won't avoid the subject.", "I won't avoid the bush.", 2, Question.DIFFICULTY_HARD);
        insertQuestions(q15);

    }
//dodaje jedno pytanie
    public void addQuestion(Question q){
        db=getWritableDatabase();
        insertQuestions(q);
    }
//dodaje liste pytan do bazy
    public void addQuestions(List<Question> questions){
        db=getWritableDatabase();

        for(Question question: questions){
            insertQuestions(question);
        }
    }
    private void insertQuestions(Question q){
        ContentValues cv = new ContentValues();
        cv.put(QuestionTable.COLUMN_QUESTION, q.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTIONS1, q.getOptions1());
        cv.put(QuestionTable.COLUMN_OPTIONS2, q.getOptions2());
        cv.put(QuestionTable.COLUMN_OPTIONS3, q.getOptions3());
        cv.put(QuestionTable.COLUMN_ANSWER_NR, q.getAnswerNr());
        cv.put(QuestionTable.COLUMN_DEFFICULTY, q.getDifficulty());

        db.insert(QuestionTable.TABLE_NAME, null, cv);
    }

    public void addStatistic(StatisticEntiti s){
        db=getWritableDatabase();
        insertStatic(s);
    }

    private void insertStatic(StatisticEntiti s) {
        ContentValues cv = new ContentValues();
        cv.put(StaticTable.COLUMN_score, s.getScore());
        cv.put(StaticTable.COLUMN_wrong, s.getWrong());
        cv.put(StaticTable.COLUMN_LEVEL, s.getDifficulty());
        cv.put(StaticTable.COLUMN_date, s.getData());
        db.insert(StaticTable.TABLE_NAME, null, cv);
    }

    public ArrayList<Question> getAllQuestions(){
        ArrayList<Question> questionList= new ArrayList<>();
        db= getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ QuestionTable.TABLE_NAME, null);

        if(c.moveToFirst()){
            do{
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOptions1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTIONS1)));
                question.setOptions2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTIONS2)));
                question.setOptions3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTIONS3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DEFFICULTY)));
                questionList.add(question);
            }while ((c.moveToNext()));
        }
        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(String difficult){
        ArrayList<Question> questionList= new ArrayList<>();
        db= getReadableDatabase();

        String[] selectionArgs = new String[]{difficult};
        Cursor c = db.rawQuery("SELECT * FROM "+ QuestionTable.TABLE_NAME + " WHERE "+QuestionTable.COLUMN_DEFFICULTY + "=?", selectionArgs);

        if(c.moveToFirst()){
            do{
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOptions1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTIONS1)));
                question.setOptions2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTIONS2)));
                question.setOptions3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTIONS3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DEFFICULTY)));
                questionList.add(question);
            }while ((c.moveToNext()));
        }
        c.close();
        return questionList;
    }
}
