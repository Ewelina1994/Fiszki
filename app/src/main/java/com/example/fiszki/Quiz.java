package com.example.fiszki;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.text.AutoText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Quiz extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    public static final String EXTRA_WRONG = "extraWrong";

    private static final long COUNTDOWN_IN_MILLIS=30000;

    private static final String KEY_SCORE="keyScore";
    private static final String KEY_QUESTION_COUNT="keyQuestionCount";
    private static final String KEY_MILIS_LEFT="keyMiliLeft";
    private static final String KEY_ANSWER="keyAnswered";
    private static final String KEY_QUESTION_LIST="keyQuestionList";

    private TextView countQuestion;
    private TextView textViewQuestion;
    private TextView textViewCorect;
    private TextView textViewWrong;
    private TextView textDifficulty;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;

    private TextView textViewQuestion1;
    private TextView textViewQuestion2;
    private TextView textViewQuestion3;
    private LinearLayout linearayoutCardView;

    private Button buttonAddToReplays;
    private Button buttoinNext;

    private ArrayList<Question> questionList;
    private int questionCount;
    private int questionCountTotal;
    private Question currentQuestion;


    private int score;
    private int wrong;
    private String date;

    private boolean ansewered;
    private int clickedAswer=-1;

    private long backPressTime;

//    private Drawable textColorDefault;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    //przychodzi z podrzednej aktywnosci
    String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        countQuestion= findViewById(R.id.txtTotalQuestion);
        textViewQuestion=(TextView) findViewById(R.id.txtQuestion);
        textViewCorect=findViewById(R.id.txtCore);
        textViewQuestionCount=findViewById(R.id.txtCore);
        textViewCountDown=findViewById(R.id.txtViewTimer);
        textViewWrong=findViewById(R.id.txtWrong);
        textDifficulty=findViewById(R.id.txtdifficulty);
        linearayoutCardView=(LinearLayout) findViewById(R.id.cardViewGroup);

        textViewQuestion1=findViewById(R.id.textViewQuestion1);
        textViewQuestion2=findViewById(R.id.textViewQuestion2);
        textViewQuestion3=findViewById(R.id.textViewQuestion3);
        buttoinNext=findViewById(R.id.btnNextQuestion);
        buttonAddToReplays=findViewById(R.id.btnAddToReplays);

        //textColorDefault = textViewQuestion1.getBackground();
        textColorDefaultCd = textViewCountDown.getTextColors();

        Intent intent = getIntent();
         difficulty=intent.getStringExtra(StartPageQuiz.EXTRA_DIFFICULTY);

        textDifficulty.setText("Difficulty: "+difficulty);

        if(savedInstanceState==null) {
            QuizDbHelper dbHelper = new QuizDbHelper(this);
            questionList = dbHelper.getQuestions(difficulty);
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        }else{
            questionList=savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);

            questionCountTotal=questionList.size();
            questionCount=savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion=questionList.get(questionCount-1);
            score=savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis=savedInstanceState.getInt(KEY_MILIS_LEFT);
            ansewered=savedInstanceState.getBoolean(KEY_ANSWER);

            if(!ansewered){
                startCountDown();
            }else {
                updateCountDownText();
                showSolution();
            }
        }
        //set Event to cardView
        setSingleEvent(linearayoutCardView);

        buttoinNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //JEŚLI nie jest nic zaznaczone
                if(!ansewered){
                    //jeśli zaznaczona odp nie zostanie kliknieta czyli na nowo zainicjowana zmienna
                    if(clickedAswer!=-1){
                       checkAnswer();
                       clickedAswer=-1;
                    }else {
                        Toast.makeText(Quiz.this, "Please select answer", Toast.LENGTH_LONG).show();
                    }
                }
                //JEŚŁI ODP = TRUE
                else {
                    showNextQuestion();
                }
            }
        });
    }

    private void setSingleEvent(LinearLayout linearayoutCardView) {
        for(int i=0; i<linearayoutCardView.getChildCount(); i++){
            final CardView cardView= (CardView) linearayoutCardView.getChildAt(i);
            final int finalI=i;
            final int finalI1 = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @SuppressLint({"ResourceAsColor", "ResourceType"})
                @Override
                public void onClick(View v) {
                    cardView.getChildAt(0).setBackgroundColor(0xff0000ff);
                    clickedAswer=finalI+1;
                }
            });
        }
    }

    private void showNextQuestion() {
        textViewQuestion1.setBackgroundColor(R.drawable.bg2);
        textViewQuestion2.setBackgroundColor(R.drawable.bg2);
        textViewQuestion3.setBackgroundColor(R.drawable.bg2);
        linearayoutCardView.clearFocus();


        Log.v("Quest count: ", String.valueOf(questionCount));
        if(questionCount<questionCountTotal){
            Log.v("Quest count: ", String.valueOf(questionCount));
            currentQuestion=questionList.get(questionCount);

            textViewQuestion.setText(currentQuestion.getQuestion());
            textViewQuestion1.setText(currentQuestion.getOptions1());
            textViewQuestion2.setText(currentQuestion.getOptions2());
            textViewQuestion3.setText(currentQuestion.getOptions3());

            questionCount++;
            countQuestion.setText("Question: "+questionCount+"/"+questionCountTotal);
            //ustawienie że nie została wybrana żadna odpowiedz
            ansewered=false;
            buttoinNext.setText("Confirm");
            timeLeftInMillis=COUNTDOWN_IN_MILLIS;
            startCountDown();
        }else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer=new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis=0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minuts= (int) ((timeLeftInMillis/1000)/60);
        int sekunds=(int) (timeLeftInMillis/1000)%60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minuts, sekunds);
        textViewCountDown.setText(timeFormatted);
        if(timeLeftInMillis<10000){
            textViewCountDown.setTextColor(Color.RED);
        }else{
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        ansewered=true;

        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
        if(clickedAswer==currentQuestion.getAnswerNr()){
            score++;
            textViewCorect.setText("Correct: "+ score);
        }else {
            wrong++;
            textViewWrong.setText("Wrong: "+ wrong);
        }
        showSolution();
    }

    private void showSolution() {
         CardView cardView1= (CardView) linearayoutCardView.getChildAt(0);
         CardView cardView2= (CardView) linearayoutCardView.getChildAt(1);
         CardView cardView3= (CardView) linearayoutCardView.getChildAt(2);
        cardView1.getChildAt(0).setBackgroundColor(Color.parseColor("#FF0A0E"));
        cardView2.getChildAt(0).setBackgroundColor(Color.parseColor("#FF0A0E"));
        cardView3.getChildAt(0).setBackgroundColor(Color.parseColor("#FF0A0E"));

        switch (currentQuestion.getAnswerNr()){
            case 1:
                cardView1.getChildAt(0).setBackgroundColor(Color.parseColor("#2AFF3F"));
                break;
            case 2:
                cardView2.getChildAt(0).setBackgroundColor(Color.parseColor("#2AFF3F"));
                break;
            case 3:
                cardView3.getChildAt(0).setBackgroundColor(Color.parseColor("#2AFF3F"));
                break;
        }

        if(questionCount<questionCountTotal){
            buttoinNext.setText("Next");
        }else {
            buttoinNext.setText("Finish");
        }
    }

    private void finishQuiz() {

        //dodawanie statystyki do bazy
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);

        Date nowDate = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        date= sdf1.format(nowDate);

        StatisticEntiti statisticEntiti= new StatisticEntiti(score, wrong, difficulty, date);
        QuizDbHelper.getInstance(this).addStatistic(statisticEntiti);


        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        resultIntent.putExtra(EXTRA_WRONG, wrong);
        setResult(RESULT_OK, resultIntent);


        finish();
    }

    //po kliknięciu przycisku wstecz spr czy w czasie 2 sekund był 2 raz kliknięty przycisk wstecz jesli tak to konczy quiz
    @Override
    public void onBackPressed(){
        if(backPressTime+2000>System.currentTimeMillis()){
            finishQuiz();
        }else{
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_LONG).show();
        }
        backPressTime=System.currentTimeMillis();
    }

    //finsz suma wyników
//    public void openWindowSum(){
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("Your score: \n"+score+"/"+questionCountTotal);
//        alertDialogBuilder.setCancelable(false);
//        alertDialogBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                Intent intent = new Intent(this,resultIntent.class);
//                startActivity(intent);
//            }
//        });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCount);
        outState.putLong(KEY_MILIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWER, ansewered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Your score: \n"+score+"/"+questionCountTotal);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra(EXTRA_SCORE, score);
//                resultIntent.putExtra(EXTRA_WRONG, wrong);
//                setResult(RESULT_OK, resultIntent);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

