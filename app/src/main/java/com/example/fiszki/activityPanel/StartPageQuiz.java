package com.example.fiszki.activityPanel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fiszki.R;

public class StartPageQuiz extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ=1;
    public static final String EXTRA_DIFFICULTY="extraDifficulty";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighScore";
    public static final String KEY_LASTCORE = "keyLastScore";
    private static final String KEY_TOTAL_QUESTIONS_HIGHT = "keyTotal";
    private static final String KEY_TOTAL_QUESTIONS_LAST = "keyTotal";



    private int highscore;
    private int lastResult;
    private TextView textViewHighScore;
    private TextView textViewLastResult;
    private Spinner spinnerDifficulty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page_quiz);
        textViewHighScore= (TextView) findViewById(R.id.textViewHighScore);
        textViewLastResult=findViewById(R.id.lastResult);
        spinnerDifficulty=findViewById(R.id.spinner_difficulty);

        Resources res = getResources();
        String[] level = res.getStringArray(R.array.poziom);

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, level);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
//        ArrayAdapter adapterDifficulty=ArrayAdapter.createFromResource(
//                this,
//                R.array.level_array,
//                R.layout.color_spinner_layout
//        );
//        adapterDifficulty.setDropDownViewResource(R.layout.spinner_text_view);
//        spinnerDifficulty.setAdapter(adapterDifficulty);

        loadHighScore();
        loadLastResult();
//        dodawanie pytania np z inputów
//        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
//        QuizDbHelper.getInstance(this).addQuestion();
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    public void startQuiz(View v) {
        String diffculty = spinnerDifficulty.getSelectedItem().toString();
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(EXTRA_DIFFICULTY, diffculty);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_QUIZ){
            if(resultCode==RESULT_OK){
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                int totalQuestion = data.getIntExtra(QuizActivity.EXTRA_TOTAL, 0);
                updateLastResult(score, totalQuestion);

                if(score>highscore){
                    updateHighScore(score, totalQuestion);
                }
            }
        }
    }

    private void updateLastResult(int score, int totalQuestion) {
        SharedPreferences prefs= getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        lastResult=prefs.getInt(KEY_LASTCORE, 0);
        //lastResult=prefs.getInt(KEY_LASTCORE, 0);
        lastResult=score;
        textViewLastResult.setText(score+"/"+totalQuestion);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_LASTCORE, score);
        editor.putInt(KEY_TOTAL_QUESTIONS_LAST, totalQuestion);
        editor.apply();
    }

    private void loadHighScore(){
        SharedPreferences prefs= getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        int totalQuestionHight = prefs.getInt(KEY_TOTAL_QUESTIONS_HIGHT, 10);

        textViewHighScore.setText(highscore+"/"+totalQuestionHight);
    }
    private void loadLastResult(){
        SharedPreferences prefs= getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        lastResult = prefs.getInt(KEY_LASTCORE, 0);
        int totalQUESTIONLast = prefs.getInt(KEY_TOTAL_QUESTIONS_LAST, 0);
        textViewLastResult.setText(lastResult+"/"+totalQUESTIONLast);

    }
    private void updateHighScore(int hightScoreNew, int totalQuestionInQuiz) {
        textViewHighScore.setText(hightScoreNew+"/"+totalQuestionInQuiz);

        SharedPreferences prefs=getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, hightScoreNew);
        editor.putInt(KEY_TOTAL_QUESTIONS_HIGHT, totalQuestionInQuiz);
        editor.apply();
    }
}
