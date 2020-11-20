package com.example.fiszki;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;

import androidx.annotation.Nullable;

import com.example.fiszki.activityPanel.AdminAddQuestion;
import com.example.fiszki.activityPanel.RepeatBoard;
import com.example.fiszki.db.OptionContract;
import com.example.fiszki.db.QuestionContract;
import com.example.fiszki.db.RepeatQuestionContract;
import com.example.fiszki.db.StatisticContract;
import com.example.fiszki.db.UserContract;
import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;
import com.example.fiszki.entity.RepeatQuestion;
import com.example.fiszki.entity.StatisticEntiti;
import com.example.fiszki.enums.LanguageEnum;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Fiszki.db";
    private static final int DATABASE_VERSION=1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //żeby można był używać internetu do pobierania zdjęcia do pytań
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    //tworzenie tabel
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;

        db.execSQL ("PRAGMA Foreign_keys = ON");

        final String SQL_CREATE_USER_TABLE = UserContract.SQL_CREATE_ENTRIES;
        final String SQL_CREATE_QUESTION_TABLE = QuestionContract.SQL_CREATE_ENTRIES;
        final String SQL_CREATE_OPTION_TABLE = OptionContract.SQL_CREATE_ENTRIES;
        final String SQL_CREATE_STATISTIC_TABLE = StatisticContract.SQL_CREATE_ENTRIES;
        final String SQL_CREATE_REPEAT_TABLE = RepeatQuestionContract.SQL_CREATE_ENTRIES;

        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        db.execSQL(SQL_CREATE_OPTION_TABLE);
        db.execSQL(SQL_CREATE_STATISTIC_TABLE);
        db.execSQL(SQL_CREATE_REPEAT_TABLE);
        fillQuestionsTable();
        fillOptionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserContract.SQL_DELETE_ENTRIES);
        db.execSQL(QuestionContract.SQL_DELETE_ENTRIES);
        db.execSQL(OptionContract.SQL_DELETE_ENTRIES);
        db.execSQL(StatisticContract.SQL_CREATE_ENTRIES);
        db.execSQL(RepeatQuestionContract.SQL_CREATE_ENTRIES);
        onCreate(db);
    }

    private void fillOptionsTable() {
        Option o1ENq1= new Option(1, "You are looking down.", 0, "EN");
        Option o2ENq1= new Option(1, "You look sad.", 1, "EN");
        Option o3ENq1= new Option(1, "You look great", 0, "EN");

        Option o1ENq2= new Option(2, "He acts like a military commander.", 1, "EN");
        Option o2ENq2= new Option(2, "He is a military commander.", 0, "EN");
        Option o3ENq2= new Option(2, "He wants to join the military.", 0, "EN");

        Option o1ENq3= new Option(3, "Feel better and get a positivity stick.", 0, "EN");
        Option o2ENq3= new Option(3, "Smile upwards and maintain a positive attitude.", 0, "EN");
        Option o3ENq3= new Option(3, "Feel better and maintain positive thoughts.", 1, "EN");

        Option o1ENq4= new Option(4, "Things have been happening lately.", 0, "EN");
        Option o2ENq4= new Option(4, "If one more thing happens, it will happen too late.", 0, "EN");
        Option o3ENq4= new Option(4, "Things have happened close together in time.", 1, "EN");

        Option o1ENq5= new Option(5, "Maybe it will change my luck. I knock on wood to give myself luck.", 1, "EN");
        Option o2ENq5= new Option(5, "Maybe it will change my luck and someone's knocking.", 0, "EN");
        Option o3ENq5= new Option(5, "Maybe it will change my luck if I knock on the door.", 0, "EN");

        Option o1ENq6= new Option(6, "Please stop talking about that topic.", 1, "EN");
        Option o2ENq6= new Option(6, "Please lay down.", 0, "EN");
        Option o3ENq6= new Option(6, "Please stop talking and lay down.", 0, "EN");

        Option o1ENq7= new Option(7, "I think I'll wait in line for that job.", 0, "EN");
        Option o2ENq7= new Option(7, "I think I might get that job.", 0, "EN");
        Option o3ENq7= new Option(7, "I think I'll stand and think about the job.", 1, "EN");

        Option o1ENq8= new Option(8, "I'm obeying the new diet.", 1, "EN");
        Option o2ENq8= new Option(8, "I'm sticking to the kitchen counter.", 0, "EN");
        Option o3ENq8= new Option(8, "I'm on a new diet of sticky rice..", 0, "EN");

        Option o1ENq9= new Option(9, "This weather is gloomy.", 0, "EN");
        Option o2ENq9= new Option(9, "I don't like this weather.", 1, "EN");
        Option o3ENq9= new Option(9, "This weather is rainy.", 0, "EN");

        Option o1ENq10= new Option(10, "We need to hurry up, like ducks flying.", 0, "EN");
        Option o2ENq10= new Option(10, "We need to get organized.", 1, "EN");
        Option o3ENq10= new Option(10, "We need to position our ducks.", 0, "EN");

        insertOption(o1ENq1);
        insertOption(o2ENq1);
        insertOption(o3ENq1);
        insertOption(o1ENq2);
        insertOption(o2ENq2);
        insertOption(o3ENq2);
        insertOption(o1ENq3);
        insertOption(o2ENq3);
        insertOption(o3ENq3);
        insertOption(o1ENq4);
        insertOption(o2ENq4);
        insertOption(o3ENq4);
        insertOption(o1ENq5);
        insertOption(o2ENq5);
        insertOption(o3ENq5);
        insertOption(o1ENq6);
        insertOption(o2ENq6);
        insertOption(o3ENq6);
        insertOption(o1ENq7);
        insertOption(o2ENq7);
        insertOption(o3ENq7);
        insertOption(o1ENq8);
        insertOption(o2ENq8);
        insertOption(o3ENq8);
        insertOption(o1ENq9);
        insertOption(o2ENq9);
        insertOption(o3ENq9);
        insertOption(o1ENq10);
        insertOption(o2ENq10);
        insertOption(o3ENq10);

        //pl wersja
        Option o1PLq1= new Option(1, "Ktoś patrzy w dół", 0, "PL");
        Option o2PLq1= new Option(1, "Wyglądasz na smutnego.", 1, "PL");
        Option o3PLq1= new Option(1, "Świetnie wyglądasz", 0, "PL");

        Option o1PLq2= new Option(2, "Ktoś działa jak dowódca w wojsku.", 1, "PL");
        Option o2PLq2= new Option(2, "Ktoś jest dowódca wojskowym", 0, "PL");
        Option o3PLq2= new Option(2, "Ktoś chce wstąpić do wojska", 0, "PL");

        Option o1PLq3= new Option(3, "Czuj sie dobrze i trzymaj się", 0, "PL");
        Option o2PLq3= new Option(3, "Uśmiechnij się i zachowaj pozytywne myślenie", 0, "PL");
        Option o3PLq3= new Option(3, "Życzę ci żebyś poczuł sie lepiej i pozytywnie myślał", 1, "PL");

        Option o1PLq4= new Option(4, "Ostatnio coś sie działo", 0, "PL");
        Option o2PLq4= new Option(4, "Jeśli wydarzy sie jeszcze jedna rzecz, będzie za późno", 0, "PL");
        Option o3PLq4= new Option(4, "Wszystko zdarzyło się w jednym czasie", 1, "PL");

        Option o1PLq5= new Option(5, "Może to da mi szczęście jeśli zapukam w drewno", 1, "PL");
        Option o2PLq5= new Option(5, "Może to zmieni moje szczęście i ktoś sie do mnie odezwie", 0, "PL");
        Option o3PLq5= new Option(5, "Może to zmieni moje szczęście, jeśli zapukam do drzwi.", 0, "PL");

        Option o1PLq6= new Option(6, "Proszę, przestań mówić na ten temat.", 1, "PL");
        Option o2PLq6= new Option(6, "Proszę się położyć.", 0, "PL");
        Option o3PLq6= new Option(6, "Proszę przestań mówić i połóż się.", 0, "PL");

        Option o1PLq7= new Option(7, "Myślę, że będę czekał w kolejce do tej pracy.", 0, "PL");
        Option o2PLq7= new Option(7, "Myślę, że mógłbym dostać tę pracę.", 0, "PL");
        Option o3PLq7= new Option(7, "Myślę, że wstanę i pomyślę o pracy.", 1, "PL");

        Option o1PLq8= new Option(8, "Przestrzegam nowej diety.", 1, "PL");
        Option o2PLq8= new Option(8, "Trzymam się blatu w kuchni", 0, "PL");
        Option o3PLq8= new Option(8, "Jestem na nowej diecie lepkiego ryżu.", 0, "PL");

        Option o1PLq9= new Option(9, "Ta pogoda jest ponura.", 0, "PL");
        Option o2PLq9= new Option(9,  "Nie lubię tej pogody.", 1, "PL");
        Option o3PLq9= new Option(9, "Ta pogoda jest deszczowa.", 0, "PL");

        Option o1PLq10= new Option(10, "Musimy się spieszyć, jak latające kaczki.", 0, "PL");
        Option o2PLq10= new Option(10, "Musimy się zorganizować.", 1, "PL");
        Option o3PLq10= new Option(10, "Musimy ustawić nasze kaczki.", 0, "PL");

        insertOption(o1PLq1);
        insertOption(o2PLq1);
        insertOption(o3PLq1);
        insertOption(o1PLq2);
        insertOption(o2PLq2);
        insertOption(o3PLq2);
        insertOption(o1PLq3);
        insertOption(o2PLq3);
        insertOption(o3PLq3);
        insertOption(o1PLq4);
        insertOption(o2PLq4);
        insertOption(o3PLq4);
        insertOption(o1PLq5);
        insertOption(o2PLq5);
        insertOption(o3PLq5);
        insertOption(o1PLq6);
        insertOption(o2PLq6);
        insertOption(o3PLq6);
        insertOption(o1PLq7);
        insertOption(o2PLq7);
        insertOption(o3PLq7);
        insertOption(o1PLq8);
        insertOption(o2PLq8);
        insertOption(o3PLq8);
        insertOption(o1PLq9);
        insertOption(o2PLq9);
        insertOption(o3PLq9);
        insertOption(o1PLq10);
        insertOption(o2PLq10);
        insertOption(o3PLq10);

    }
    private void fillQuestionsTable() {
        Question q1 = new Question( "You look a little down in the dumps.", AdminAddQuestion.getLogoImage("https://pbs.twimg.com/media/EWIV9OwXYAAghMY.jpg"));
        Question q2 = new Question("He comes across as a military commander.", AdminAddQuestion.getLogoImage("https://image.cnbcfm.com/api/v1/image/103750362-RTR4IZU8.jpg?v=1529452088&w=678&h=381"));
        Question q3 = new Question(  "Just cheer up and stick with a positive attitude.", AdminAddQuestion.getLogoImage("https://post.healthline.com/wp-content/uploads/2019/01/happy_woman-1200x628-facebook.jpg"));
        Question q4 = new Question( "It has been one thing after another lately.", AdminAddQuestion.getLogoImage("https://ohme.pl/portal/wp-content/uploads/2015/12/ogarne-wszystko-760x428.jpg"));
        Question q5 = new Question( "Maybe it will even change my luck, knock on wood.", AdminAddQuestion.getLogoImage("https://1ystw51j9au5396xq33o60xy-wpengine.netdna-ssl.com/wp-content/uploads/2018/04/knock-on-wood-to-sell-home-300x197.jpg"));
        Question q6 = new Question("Please give it a rest.", AdminAddQuestion.getLogoImage("https://memegenerator.net/img/instances/53567119.jpg"));
        Question q7 = new Question(  "I think I stand a change of getting that job.", AdminAddQuestion.getLogoImage("https://cdn.theatlantic.com/thumbor/TVQN19NPLE6Hi8ndXvk79zEjGcY=/1920x1102/media/img/2019/06/14/opener_Web_alt-3/original.jpg"));
        Question q8 = new Question("I'm sticking to the new diet.", AdminAddQuestion.getLogoImage("https://miro.medium.com/max/5632/1*A3Tb73XAyEJUIdyLktp-ug.jpeg"));
        Question q9 = new Question( "This weather leaves a lot to be desired.", AdminAddQuestion.getLogoImage("https://pbs.twimg.com/profile_images/800710559108775936/EulcV2o9_400x400.jpg"));
        Question q10 = new Question("We need to get our ducks in a row.", AdminAddQuestion.getLogoImage("https://www.idioms4you.com/img/angif-get-your-ducks-in-a-row.gif"));

        insertQuestions(q1);
        insertQuestions(q2);
        insertQuestions(q3);
        insertQuestions(q4);
        insertQuestions(q5);
        insertQuestions(q6);
        insertQuestions(q7);
        insertQuestions(q8);
        insertQuestions(q9);
        insertQuestions(q10);

    }
//dodaje jedno pytanie
    public long addQuestion(Question q){
        db=getWritableDatabase();
        return insertQuestions(q);
    }

    //dodaje opcje
    public long addOption(Option o){
        db=getWritableDatabase();
        return insertOption(o);
    }

    private long insertQuestions(Question q){
        ContentValues cv = new ContentValues();
       // cv.put(QuestionContract.QuestionTable._ID, q.getId());
        cv.put(QuestionContract.QuestionTable.COLUMN_QUESTION, q.getName());
        cv.put(QuestionContract.QuestionTable.COLUMN_IMG, q.getName_image());


        return db.insert(QuestionContract.QuestionTable.TABLE_NAME, null, cv);
    }

    private long insertOption(Option o) {
        ContentValues cv= new ContentValues();
        //cv.put(OptionContract.OptionTable.COLUMN_QUESTION_ID, o.getQuestion_id());
        cv.put(OptionContract.OptionTable.COLUMN_OPTION, o.getName());
        cv.put(OptionContract.OptionTable.COLUMN_IS_RIGHT, o.getIs_right());
        cv.put(OptionContract.OptionTable.COLUMN_LANGUAGE, o.getLanguage());

        return db.insert(OptionContract.OptionTable.TABLE_NAME, null, cv);

    }

    public long addStatistic(StatisticEntiti s){
        db=getWritableDatabase();
        return insertStatic(s);
    }

    private long insertStatic(StatisticEntiti s) {
        ContentValues cv = new ContentValues();
        cv.put(StatisticContract.StatisticTable.COLUMN_score, s.getScore());
        cv.put(StatisticContract.StatisticTable.COLUMN_wrong, s.getWrong());
        cv.put(StatisticContract.StatisticTable.COLUMN_LEVEL, s.getDifficulty());
        cv.put(StatisticContract.StatisticTable.COLUMN_date, s.getData());
        return db.insert(StatisticContract.StatisticTable.TABLE_NAME, null, cv);
    }

    public ArrayList<Question> getAllQuestions(){
        ArrayList<Question> questionList= new ArrayList<>();
        db= getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionContract.QuestionTable.TABLE_NAME+";", null);

        if(c.moveToFirst()){
            do{
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionContract.QuestionTable._ID)));
                question.setName(c.getString(c.getColumnIndex(QuestionContract.QuestionTable.COLUMN_QUESTION)));
                question.setName_image(c.getBlob(c.getColumnIndex(QuestionContract.QuestionTable.COLUMN_IMG)));
                questionList.add(question);
            }while ((c.moveToNext()));
        }
        c.close();
        return questionList;
    }

    public Question getQuestionById(long id){
        db=getReadableDatabase();
        String numberQuestion= String.valueOf(id);
        String[] selectionArgs = new String[]{numberQuestion};

        Cursor c = db.rawQuery("SELECT * FROM " + QuestionContract.QuestionTable.TABLE_NAME+" WHERE "+QuestionContract.QuestionTable._ID +" =?;", selectionArgs);
        Question question = new Question();
        if( c != null && c.moveToFirst() ) {

            question.setId(c.getInt(c.getColumnIndex(QuestionContract.QuestionTable._ID)));
            question.setName(c.getString(c.getColumnIndex(QuestionContract.QuestionTable.COLUMN_QUESTION)));
            question.setName_image(c.getBlob(c.getColumnIndex(QuestionContract.QuestionTable.COLUMN_IMG)));
        }
        c.close();
        return  question;
    }

    public Question getQuestionByName(String nameQuestion){
        db=getReadableDatabase();
        String[] selectionArgs = new String[]{nameQuestion};
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionContract.QuestionTable.TABLE_NAME+" WHERE "+QuestionContract.QuestionTable.TABLE_NAME +" =?;", selectionArgs);
        Question question = new Question();
        if( c != null && c.moveToFirst() ) {

            question.setId(c.getInt(c.getColumnIndex(QuestionContract.QuestionTable._ID)));
            question.setName(c.getString(c.getColumnIndex(QuestionContract.QuestionTable.COLUMN_QUESTION)));
            question.setName_image(c.getBlob(c.getColumnIndex(QuestionContract.QuestionTable.COLUMN_IMG)));
        }
        c.close();
        return  question;

    }

//    Wyszukiwanie option z where
    public List<Option> getOptionsToQuiz(long questionNumber, LanguageEnum l){
        List<Option> optionsList= new ArrayList<>();
        db= getReadableDatabase();

        String numberQuestion= String.valueOf(questionNumber);
        String language=l.toString();
        String[] selectionArgs = new String[]{numberQuestion, language};
        Cursor c = db.rawQuery(
                "SELECT * FROM "+ OptionContract.OptionTable.TABLE_NAME + " WHERE "+OptionContract.OptionTable.COLUMN_QUESTION_ID + "=? " +
                        "AND "+ OptionContract.OptionTable.COLUMN_LANGUAGE + "=?", selectionArgs);

        if(c.moveToFirst()){
            do{
                Option question = new Option();
                question.setId(c.getInt(c.getColumnIndex(OptionContract.OptionTable._ID)));
                question.setQuestion_id(c.getInt(c.getColumnIndex(OptionContract.OptionTable.COLUMN_QUESTION_ID)));
                question.setName(c.getString(c.getColumnIndex(OptionContract.OptionTable.COLUMN_OPTION)));
                question.setIs_right(c.getInt(c.getColumnIndex(OptionContract.OptionTable.COLUMN_IS_RIGHT)));
                question.setLanguage(c.getString(c.getColumnIndex(OptionContract.OptionTable.COLUMN_LANGUAGE)));
                optionsList.add(question);
            }while ((c.moveToNext()));
        }
        c.close();
        return optionsList;
    }

    public Option getGoodOptionPL(long questionNumber){
        Option optionPL= new Option();
        db=getReadableDatabase();
        String numberQuestion= String.valueOf(questionNumber);
        String is_right=String.valueOf(1);
        String[] selectionArgs = new String[]{numberQuestion, is_right, "PL"};
        Cursor c= db.rawQuery("SELECT * FROM "+ OptionContract.OptionTable.TABLE_NAME + " WHERE "+OptionContract.OptionTable.COLUMN_QUESTION_ID + "= ?"
                +" AND "+OptionContract.OptionTable.COLUMN_IS_RIGHT+" =?"+" AND "+OptionContract.OptionTable.COLUMN_LANGUAGE+" =?", selectionArgs);

        if(c!=null && c.moveToNext()){
                optionPL.setId(c.getInt(c.getColumnIndex(OptionContract.OptionTable._ID)));
                optionPL.setQuestion_id(c.getInt(c.getColumnIndex(OptionContract.OptionTable.COLUMN_QUESTION_ID)));
                optionPL.setName(c.getString(c.getColumnIndex(OptionContract.OptionTable.COLUMN_OPTION)));
                optionPL.setIs_right(c.getInt(c.getColumnIndex(OptionContract.OptionTable.COLUMN_IS_RIGHT)));
                optionPL.setLanguage(c.getString(c.getColumnIndex(OptionContract.OptionTable.COLUMN_LANGUAGE)));
        }
        c.close();
        return optionPL;
    }

    public Option getGoodOptionEN(long questionNumber){
        Option optionEN= new Option();
        db=getReadableDatabase();
        String numberQuestion= String.valueOf(questionNumber);
        String is_right=String.valueOf(1);
        String[] selectionArgs = new String[]{numberQuestion, is_right, "EN"};
        Cursor c= db.rawQuery("SELECT * FROM "+ OptionContract.OptionTable.TABLE_NAME + " WHERE "+OptionContract.OptionTable.COLUMN_QUESTION_ID + "= ?"
                +" AND "+OptionContract.OptionTable.COLUMN_IS_RIGHT+" =?"+" AND "+OptionContract.OptionTable.COLUMN_LANGUAGE+" =?", selectionArgs);

        if(c!= null && c.moveToNext()){
            optionEN.setId(c.getInt(c.getColumnIndex(OptionContract.OptionTable._ID)));
            optionEN.setQuestion_id(c.getInt(c.getColumnIndex(OptionContract.OptionTable.COLUMN_QUESTION_ID)));
            optionEN.setName(c.getString(c.getColumnIndex(OptionContract.OptionTable.COLUMN_OPTION)));
            optionEN.setIs_right(c.getInt(c.getColumnIndex(OptionContract.OptionTable.COLUMN_IS_RIGHT)));
            optionEN.setLanguage(c.getString(c.getColumnIndex(OptionContract.OptionTable.COLUMN_LANGUAGE)));
        }
        c.close();
        return optionEN;
    }

    public long addQuestionToRepeatTable(long idQuestion){
        db=getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(RepeatQuestionContract.RepeatQuestionTable.COLUMN_question, idQuestion);
        return db.insert(RepeatQuestionContract.RepeatQuestionTable.TABLE_NAME, null, cv);
    }

    public int deleteQuestionFromRepeatTable(long idQuestion){
        db=this.getWritableDatabase();
        String questionNumber=String.valueOf(idQuestion);
        return db.delete(RepeatQuestionContract.RepeatQuestionTable.TABLE_NAME, "question_id = ?", new String[] {questionNumber});
    }

    public List<RepeatQuestion> getAllQuestionFromRepeatTable(){
        db=getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + RepeatQuestionContract.RepeatQuestionTable.TABLE_NAME+";", null);
        List<RepeatQuestion> repeatQuestionList= new ArrayList<>();
        if(c.moveToFirst()){
            do{
                RepeatQuestion repeatQuestion = new RepeatQuestion();
                repeatQuestion.setId(c.getInt(c.getColumnIndex(RepeatQuestionContract.RepeatQuestionTable._ID)));
                repeatQuestion.setQuestion(c.getInt(c.getColumnIndex(RepeatQuestionContract.RepeatQuestionTable.COLUMN_question)));
                repeatQuestionList.add(repeatQuestion);
            }while ((c.moveToNext()));
        }
        c.close();
        return repeatQuestionList;
    }
}
