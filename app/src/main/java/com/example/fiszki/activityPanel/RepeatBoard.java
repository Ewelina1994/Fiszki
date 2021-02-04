package com.example.fiszki.activityPanel;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.fiszki.Converter;
import com.example.fiszki.entity.QuestionDTO;
import com.example.fiszki.R;
import com.example.fiszki.TextToSpeachImpl;
import com.example.fiszki.services.RepeatQuestionService;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class RepeatBoard extends AppCompatActivity {
    ImageView image;
    private TextView questionTextView;
    private TextView answerTextViewEN;
    private TextView answerTextViewPL;
    private CardView cardView;
    private Button buttonBackQuestion;
    private Button buttonAddToReplays;
    private Button buttoinNext;
    private Button btnGiveVoice;

    TextToSpeachImpl textToSpeach;

    boolean is_addQuestion_to_replace_board;
    int currentQuestion;

    Converter converter;
    private List<QuestionDTO>repeatQuestionDTOList;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replace_board);
        image = findViewById(R.id.imageView2);
        int img=R.drawable.def_as_;
        image.setImageResource(img);
        questionTextView = findViewById(R.id.questionTxt);
        answerTextViewEN = findViewById(R.id.answerTxt);
        answerTextViewPL=findViewById(R.id.sentenceTxt);
        cardView=findViewById(R.id.card_view_question);
        buttonBackQuestion=findViewById(R.id.backQuestion);
        buttonAddToReplays=findViewById(R.id.btnAddToReplays);
        buttoinNext=findViewById(R.id.btnNextQuestion);
        btnGiveVoice=findViewById(R.id.speakBtn);

        converter= new Converter();
        repeatQuestionDTOList= RepeatQuestionService.getAllQuestionOnRepeatBoard();

        currentQuestion=0;

        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_MOVE){
                    showQuestion(1);
                }
//                if(event.getAction()==MotionEvent.ACTION_DOWN){
//                    showQuestion();
//                }

                return true;
            }
        });

        buttonBackQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestion(-1);
            }
        });
        buttonAddToReplays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrDeleteQuestion();
            }
        });

        buttoinNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestion(1);
            }
        });
        btnGiveVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giveVoice(questionTextView.getText().toString()
                );
            }
        });
        initialTextToSpech();
        showQuestion(0);

    }


    private void addOrDeleteQuestion() {
        if(repeatQuestionDTOList.size()!=0){
            //RepeatQuestion repeatQuestion=converter.repeatQuestionDTOtoRepeatQuestion(repeatQuestionDTOList.get(currentQuestion));
            QuestionDTO repeatQuestion=repeatQuestionDTOList.get(currentQuestion);
            //spr czy pytanie jest w tablicy powtórek
            is_addQuestion_to_replace_board=RepeatQuestionService.isAddQuestionToRepeatBoard(repeatQuestionDTOList.get(currentQuestion));
            // jeśli pytanie nie jest dodane do powtórek i chcemy je dodać
            if(is_addQuestion_to_replace_board==false){
                //RepeatQuestionService.addQuestionToRepeatBoard(repeatQuestion);
                //repeatQuestionDTOList.saveQuestionToDBRepeatTable(repeatQuestion);
                is_addQuestion_to_replace_board=true;
                setColorBtnAddToReplace(is_addQuestion_to_replace_board);
                repeatQuestionDTOList.get(currentQuestion).setIs_added_to_repaet_board(is_addQuestion_to_replace_board);
            }
            //jeśli pytanie jest dodane do powtórek i chcemy usunć z tablicy powtórek
            else {
                //RepeatQuestionService.deleteQuestionToRepeatBoard(repeatQuestion);
                is_addQuestion_to_replace_board=false;
                setColorBtnAddToReplace(is_addQuestion_to_replace_board);
                repeatQuestionDTOList.get(currentQuestion).setIs_added_to_repaet_board(is_addQuestion_to_replace_board);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showQuestion(int value) {

        if (currentQuestion<repeatQuestionDTOList.size() && currentQuestion>=0){
            buttonBackQuestion.setEnabled(true);
            //wciśnięty przycisk nast pytanie
            if(value==1){
                currentQuestion++;

                if(currentQuestion==repeatQuestionDTOList.size()) {
                    finishQuiz();
                }
                else {
                   setBtnNextandBtnPrevious();

                    //spr czy pytanie jest w tablicy powtórek
                    is_addQuestion_to_replace_board=repeatQuestionDTOList.get(currentQuestion).isIs_added_to_repaet_board();
                    setColorBtnAddToReplace(is_addQuestion_to_replace_board);
                    //ustaw pola
                    //byte[] byteImg = repeatQuestionDTOList.get(currentQuestion).getName_image();
                    //Bitmap bmp = BitmapFactory.decodeByteArray(byteImg, 0, byteImg.length);
                    //image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(), image.getHeight(), false));
                    //image.setImageBitmap(bmp);
                    questionTextView.setText(repeatQuestionDTOList.get(currentQuestion).getQuestion().getName());
                    setImageFromUri();
                    giveVoice(repeatQuestionDTOList.get(currentQuestion).getQuestion().getName().toString());
                    repeatQuestionDTOList.get(currentQuestion).getOptions().forEach(option->{
                        if(option.getIs_right()==1 && option.getLanguage().equalsIgnoreCase("EN")){
                            answerTextViewEN.setText(option.getName());
                        }
                        if(option.getIs_right()==1 && option.getLanguage().equalsIgnoreCase("PL")){
                            answerTextViewPL.setText(option.getName());
                        }
                    });

                    //spr czy pytanie jest w tablicy powtórek
                    is_addQuestion_to_replace_board=repeatQuestionDTOList.get(currentQuestion).isIs_added_to_repaet_board();
                    setColorBtnAddToReplace(is_addQuestion_to_replace_board);
                }
            }
            //wciśnięty przycisk poprzednie pytanie
            else if(value==-1){
                currentQuestion--;

                setBtnNextandBtnPrevious();

                    //byte[] byteImg=repeatQuestionDTOList.get(currentQuestion).getName_image();
                    //Bitmap bmp = BitmapFactory.decodeByteArray(byteImg, 0, byteImg.length);
                    //image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(), image.getHeight(), false));
                    //image.setImageBitmap(bmp);
                String idiom = repeatQuestionDTOList.get(currentQuestion).getQuestion().getName();
                    questionTextView.setText(idiom);
                setImageFromUri();
                giveVoice(idiom.toString());
                repeatQuestionDTOList.get(currentQuestion).getOptions().forEach(option->{
                    if(option.getIs_right()==1 && option.getLanguage().equalsIgnoreCase("EN")){
                        answerTextViewEN.setText(option.getName());
                    }
                    if(option.getIs_right()==1 && option.getLanguage().equalsIgnoreCase("PL")){
                        answerTextViewPL.setText(option.getName());
                    }
                });
                //spr czy pytanie jest w tablicy powtórek
                is_addQuestion_to_replace_board=repeatQuestionDTOList.get(currentQuestion).isIs_added_to_repaet_board();
                setColorBtnAddToReplace(is_addQuestion_to_replace_board);

            }
            //nowa katywność żaden przycisk wstecz lub do przodu nie jest kliknięty
            else if(value==0){
//                if(repeatQuestionDTOList.get(currentQuestion).getName_image()!=null){
//                    byte[] byteImg=repeatQuestionDTOList.get(currentQuestion).getName_image();
//                    Bitmap bmp = BitmapFactory.decodeByteArray(byteImg, 0, byteImg.length);
//                    //image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(), image.getHeight(), false));
//                    image.setImageBitmap(bmp);
//                }

                questionTextView.setText(repeatQuestionDTOList.get(currentQuestion).getQuestion().getName());
                setImageFromUri();
                repeatQuestionDTOList.get(currentQuestion).getOptions().forEach(option->{
                    if(option.getIs_right()==1 && option.getLanguage().equalsIgnoreCase("EN")){
                        answerTextViewEN.setText(option.getName());
                    }
                    if(option.getIs_right()==1 && option.getLanguage().equalsIgnoreCase("PL")){
                        answerTextViewPL.setText(option.getName());
                    }
                });
                //spr czy pytanie jest w tablicy powtórek
                is_addQuestion_to_replace_board=repeatQuestionDTOList.get(currentQuestion).isIs_added_to_repaet_board();
                setColorBtnAddToReplace(is_addQuestion_to_replace_board);

                setBtnNextandBtnPrevious();
            }
        }else{
            //jeśłi jest 0 pytań na liście powtórek ustaw inny layout
            setContentView(R.layout.no_question_in_repeat_board);
        }

    }

    private void setImageFromUri() {
        Uri uriImage=repeatQuestionDTOList.get(currentQuestion).getQuestion().getUploadImageUri();
        Picasso.get().load(uriImage).into(image);
    }

    private void setBtnNextandBtnPrevious() {
        //jeśli nie ma wcześniejszego pytania
        if(currentQuestion==0){
            buttonBackQuestion.setEnabled(false);
        }
        //jeśli ostatnie pyrtanie ustaw przycisk next na koniec
        if(currentQuestion==repeatQuestionDTOList.size()-1) {
            buttoinNext.setText(R.string.finishRepeatBoard);
        }
        if(currentQuestion!=repeatQuestionDTOList.size()-1) {
            buttoinNext.setText(R.string.nextQuestion);
        }
    }

    private void finishQuiz() {
        finish();
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

    private void giveVoice(String text) {
        textToSpeach.audio(text);
    }
}
