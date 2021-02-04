package com.example.fiszki.activityPanel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fiszki.entity.QuestionDTO;
import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.TextToSpeachImpl;
import com.example.fiszki.enums.LanguageEnum;
import com.example.fiszki.services.QuizService;
import com.example.fiszki.R;
import com.example.fiszki.entity.StatisticEntiti;
import com.example.fiszki.entity.Option;
import com.example.fiszki.enums.DifficultyEnum;
import com.example.fiszki.services.RepeatQuestionService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    public static final String EXTRA_TOTAL = "extraTotal";
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_WRONG = "keyWrong";
    private static final String KEY_QUESTION_COUNT="keyQuestionCount" ;
    private static final String KEY_QUESTION_COUNT_TOTAL="keyQuestionCountTotal" ;
    private static final String KEY_MILIS_LEFT = "keyMiliLeft";
    private static final String KEY_ANSWER = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";
    private static final String EXTRA_DIFFICULTY = "extraDifficulty";
    private static final String BUTTON_NEXT_NAME = "buttonNextName";
    private static final long COUNTDOWN_IN_MILLIS = 30000;

    private TextView countQuestion;
    private TextView textViewQuestion;
    private TextView textViewCorect;
    private TextView textViewWrong;
    private TextView textDifficulty;
    private TextView textViewCountDown;

    private TextView textViewQuestion1;
    private TextView textViewQuestion2;
    private TextView textViewQuestion3;
    private LinearLayout linearayoutCardView;
    private List<CardView> cardsView;

    private Button buttonAddToReplays;
    private Button buttoinNext;

    private ArrayList<QuestionDTO> questionList;
    private int questionCount;
    private int questionCountTotal;
    private QuestionDTO currentQuestion;


    private int score=0;
    private int wrong=0;
    private String date;

    boolean is_addQuestion_to_replace_board;

    private boolean ansewered;
    private Option clickedAnswer;

    private long backPressTime;

    //    private Drawable textColorDefault;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    //przychodzi z podrzednej aktywnosci
    String difficulty;
    DifficultyEnum difficultyEnum;
    List<Option>listOptionsInQuiz;

    Button speakButton;
    TextToSpeachImpl textToSpeach;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        countQuestion = findViewById(R.id.txtTotalQuestion);
        textViewQuestion = (TextView) findViewById(R.id.txtQuestion);
        textViewCorect = findViewById(R.id.txtCore);
        textViewCountDown = findViewById(R.id.txtViewTimer);
        textViewWrong = findViewById(R.id.txtWrong);
        textDifficulty = findViewById(R.id.txtdifficulty);
        linearayoutCardView = (LinearLayout) findViewById(R.id.cardViewGroup);

        textViewQuestion1 = findViewById(R.id.textViewQuestion1);
        textViewQuestion2 = findViewById(R.id.textViewQuestion2);
        textViewQuestion3 = findViewById(R.id.textViewQuestion3);
        buttoinNext = findViewById(R.id.btnNextQuestion);
        buttonAddToReplays = findViewById(R.id.btnAddToReplays);

        clickedAnswer=new Option();
        speakButton = findViewById(R.id.speakBtn);
        textColorDefaultCd = textViewCountDown.getTextColors();

        Intent intent = getIntent();

        // difficulty = intent.getStringExtra(Quiz.EXTRA_DIFFICULTY);
        difficulty = intent.getStringExtra(QuizActivity.EXTRA_DIFFICULTY);
        textDifficulty.setText(getString(R.string.difficulty) +": " + difficulty);

        //inicjalizacja textToSpeach
        initialTextToSpech();

        //inicjalizacja array cardViews
        initialCardsView();

        //pobranie randomowych pytań z QuizService

        if (savedInstanceState == null) {
            QuizDbHelper dbHelper = new QuizDbHelper(this);
            difficultyEnum = DifficultyEnum.valueOf(DifficultyEnum.class, difficulty);

            questionList= (ArrayList<QuestionDTO>) QuizService.getRandomQuestionInQuiz(this);

            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);


            //set Event to cardView
            setSingleEvent();

            showNextQuestion();

            //ustawienie dla poziomu trudnego nie wyświetlanai textu pytania
            if(difficultyEnum==DifficultyEnum.Zaawansowany){
                final ViewGroup viewGroup= findViewById(R.id.questionLayout);
                viewGroup.removeAllViews();
                viewGroup.addView(View.inflate(this, R.layout.hard_difficulty_change_question_view, null));
            }

            buttoinNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //JEŚLI nie jest nic zaznaczone
                    if (!ansewered) {
                        //jeśli zaznaczona odp nie zostanie kliknieta czyli na nowo zainicjowana zmienna
//                        if (!(clickedAnswer.getIs_right() ==1)) //?
//                        {
                        if (clickedAnswer != null) {
                            checkAnswer();
                        }else {
                             Toast.makeText(QuizActivity.this, R.string.valid_check_answer, Toast.LENGTH_LONG).show();

                        }
                    }
                    //JEŚŁI ODP została zaznaczona
                    else {
                        // checkAnswer();
                        showNextQuestion();
                    }
                }
            });
            buttonAddToReplays.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(RepeatQuestionService.isAddQuestionToRepeatBoard(currentQuestion)){
                        currentQuestion.setIs_added_to_repaet_board(false);
                        is_addQuestion_to_replace_board=false;
                        RepeatQuestionService.deleteQuestionToRepeatBoard(currentQuestion);
                        setColorBtnAddToReplace(is_addQuestion_to_replace_board);
                    }else {
                        currentQuestion.setIs_added_to_repaet_board(true);
                        is_addQuestion_to_replace_board=true;
                        RepeatQuestionService.addQuestionToRepeatBoard(currentQuestion);
                        setColorBtnAddToReplace(is_addQuestion_to_replace_board);
                    }
                }
            });

            speakButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    giveVoice();
                }
            });
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);

            questionCountTotal = savedInstanceState.getInt(KEY_QUESTION_COUNT_TOTAL);
            questionCount = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCount - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            wrong=savedInstanceState.getInt(KEY_WRONG);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILIS_LEFT);
            ansewered = savedInstanceState.getBoolean(KEY_ANSWER);
            buttoinNext.setText(savedInstanceState.getString(BUTTON_NEXT_NAME));

            if (!ansewered) {
                startCountDown();
            } else {
                updateCountDownText();
                showSolution();
            }
        }
    }

    private void initialCardsView() {
        cardsView=new ArrayList<>();
        cardsView.add(findViewById(R.id.cardView1));
        cardsView.add(findViewById(R.id.cardView2));
        cardsView.add(findViewById(R.id.cardView3));
        for(CardView card: cardsView){
            card.setRadius(25.0f);
        }
    }

    private List<Option> getOptionByLevel(QuestionDTO questionDTO) {
        // daj dla konkretnego poziomu opcje

        List<Option> newOptionList=new ArrayList<>();
        for (Option option: questionDTO.getOptions()) {
            if(option.getLanguage().equals(checkWhatLanguage(difficultyEnum).toString())){
                newOptionList.add(option);
            }
        }
        return newOptionList;

    }
    public LanguageEnum checkWhatLanguage(DifficultyEnum difficulty) {
        switch (difficulty) {
            case Łatwy:
                return LanguageEnum.PL;
            case Średni:
                return  LanguageEnum.EN;
            default:
                return LanguageEnum.EN;
        }
    }

    private void initialTextToSpech() {
        textToSpeach = new TextToSpeachImpl(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeach.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("textToSpeek", "Language not supported");
                    } else {

                    }
                } else {
                    Log.e("textToSpeek", "Initialization faild");
                }
            }
        });
    }

    private void giveVoice() {
        String text = textViewQuestion.getText().toString();
        textToSpeach.audio(text);
    }


    private void setSingleEvent() {
        //ustawiamy dla każdego cardView nasłuch
        for (int i = 0; i < cardsView.size(); i++) {
            final int index=i;
            cardsView.get(i).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @SuppressLint({"ResourceAsColor", "ResourceType"})
                @Override
                public void onClick(View v) {
                    //spr czy czas już nie minął czy nie udzieliłes odp
                    if(!ansewered){
                        clickedAnswer=listOptionsInQuiz.get(index);
                        //dla każdej karty ustawiamy kolor żeby przy podwójnym kliknięciu nie było 2 razy zaznaczonej karty
                        cardsView.forEach(card->{
                            if(card==cardsView.get(index)){
                                card.setBackgroundColor(Color.parseColor("#E78230"));

                            }else {
                                card.setBackgroundColor(Color.parseColor("#CCDAE7"));
                            }
                        });
                    }
                }
            });
        }
    }

    private void showNextQuestion() {
        //ustaw kliknieto odp na null
        clickedAnswer=null;
        if (questionCount < questionCountTotal) {
            cleanColorCardView();
            currentQuestion = questionList.get(questionCount);

            listOptionsInQuiz=getOptionByLevel(currentQuestion);
            Collections.shuffle(listOptionsInQuiz);

            //currentQuestion.setOptions(listOptionsInQuiz);

            textViewQuestion.setText(currentQuestion.getQuestion().getName());
            textViewQuestion1.setText(listOptionsInQuiz.get(0).getName());
            textViewQuestion2.setText(listOptionsInQuiz.get(1).getName());
            textViewQuestion3.setText(listOptionsInQuiz.get(2).getName());

            //daj głos do pytania automatycznie ale nie działa :(
            giveVoice();

            questionCount++;
            countQuestion.setText(getString(R.string.question) + ": " + questionCount + "/" + questionCountTotal);
            //ustawienie że nie została wybrana żadna odpowiedz
            ansewered = false;
            buttoinNext.setText(R.string.confirm);
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();

            //spr czy pytanie jest w tablicy powtórek
            is_addQuestion_to_replace_board=currentQuestion.isIs_added_to_repaet_board();
            setColorBtnAddToReplace(is_addQuestion_to_replace_board);
        } else {
            finishQuiz();
        }
    }

    private void cleanColorCardView() {
        for(CardView card: cardsView){
            card.setBackgroundColor(Color.parseColor("#CCDAE7"));
        }
    }

    private void setColorBtnAddToReplace(boolean is_addQuestion) {
        GradientDrawable bgShape1 = (GradientDrawable) buttonAddToReplays.getBackground();
        if(is_addQuestion==true){
            bgShape1.setColor(Color.parseColor("#E78230"));
            buttonAddToReplays.setText(R.string.addedToReplace);
        }else {
            bgShape1.setColor(Color.parseColor("#C0C0C0"));
            buttonAddToReplays.setText(R.string.no_added_to_replace);
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minuts = (int) ((timeLeftInMillis / 1000) / 60);
        int sekunds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minuts, sekunds);
        textViewCountDown.setText(timeFormatted);
        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        ansewered = true;

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        //jeśli poprawna odp
        if (clickedAnswer!=null
                && clickedAnswer.getIs_right()==1) {
            score++;
            String scoreTxt = getString(R.string.score);
            textViewCorect.setText(scoreTxt+": " + score);}
        //jeśli nie poprawna odp
        else {
            wrong++;
            String wrongTxt = getString(R.string.wrong);
            textViewWrong.setText(wrongTxt +": " + wrong);
        }
        showSolution();
    }

    private void showSolution() {
        String goodOption=null;
        for (Option option : listOptionsInQuiz) {
            if (option.getIs_right() == 1) {
                goodOption = option.getName();
            }
        }

        for(int i=0; i<listOptionsInQuiz.size(); i++){
            String textInCardView=((TextView) cardsView.get(i).getChildAt(0)).getText().toString();
            if(textInCardView.equalsIgnoreCase(goodOption)){
                cardsView.get(i).setBackgroundColor(Color.parseColor("#2AFF3F"));
            }else {
                cardsView.get(i).setBackgroundColor(Color.parseColor("#FF0A0E"));
            }
        }


        if (questionCount < questionCountTotal) {
            buttoinNext.setText(R.string.nextQuestion);
        } else {
            buttoinNext.setText(R.string.finishQuiz);
        }
    }

    private void finishQuiz() {

        //dodawanie statystyki do bazy
        Date nowDate = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        date = sdf1.format(nowDate);

        StatisticEntiti statisticEntiti = new StatisticEntiti(score, wrong, difficulty, date);
        QuizDbHelper.getInstance(this).addStatistic(statisticEntiti);


        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        resultIntent.putExtra(EXTRA_TOTAL, questionCountTotal);
        setResult(RESULT_OK, resultIntent);

        showSummary();
    }

    private void showSummary() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.your_score) + "\n" + score + "/" + questionCountTotal);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                finish();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        TextView messageText = (TextView) alertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        messageText.setTextSize(25f);
    }

    //po kliknięciu przycisku wstecz spr czy w czasie 2 sekund był 2 raz kliknięty przycisk wstecz jesli tak to konczy quiz
    @Override
    public void onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, R.string.valid_exit_in_quiz, Toast.LENGTH_LONG).show();
        }
        backPressTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (textToSpeach != null) {
            textToSpeach.stop();
            textToSpeach.shutdown();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(textToSpeach!=null){
            textToSpeach.stop();
            textToSpeach.shutdown();
        }
    }

}

