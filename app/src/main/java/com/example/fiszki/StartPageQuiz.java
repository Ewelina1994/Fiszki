package com.example.fiszki;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class StartPageQuiz extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ=1;
    public static final String EXTRA_DIFFICULTY="extraDifficulty";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighScore";
    public static final String KEY_LASTCORE = "keyLastScore";


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

        String[] difficultyLevels=Question.getAllDifficultyLevels();

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
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
        Intent intent = new Intent(this, Quiz.class);
        intent.putExtra(EXTRA_DIFFICULTY, diffculty);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_QUIZ){
            if(resultCode==RESULT_OK){
                int score = data.getIntExtra(Quiz.EXTRA_SCORE, 0);
                int wrong = data.getIntExtra(Quiz.EXTRA_WRONG, 0);
                updateLastResult(score);

                if(score>highscore){
                    updateHighScore(score);
                }
            }
        }
    }

    private void updateLastResult(int score) {
        SharedPreferences prefs= getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        lastResult=prefs.getInt(KEY_LASTCORE, 0);
        textViewLastResult.setText(score+"/5");

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_LASTCORE, lastResult);
        editor.apply();
    }

    private void loadHighScore(){
        SharedPreferences prefs= getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighScore.setText(highscore+"/5");
    }
    private void loadLastResult(){
        SharedPreferences prefs= getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        lastResult = prefs.getInt(KEY_LASTCORE, 0);
        textViewLastResult.setText(lastResult+"/5");

//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putInt(KEY_LASTCORE, lastResult);
//        editor.apply();
    }
    private void updateHighScore(int hightScoreNew) {
        highscore=hightScoreNew;
        textViewHighScore.setText(highscore+"/5");

        SharedPreferences prefs=getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }
}
