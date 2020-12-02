package com.example.fiszki.activityPanel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
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
import com.example.fiszki.FirebaseConfiguration;
import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.R;
import com.example.fiszki.RepeatQuestionDTO;
import com.example.fiszki.entity.RepeatQuestion;
import com.example.fiszki.services.RepeatQuestionService;

import java.util.List;

public class RepeatBoard extends AppCompatActivity {
    ImageView image;
    private TextView questionTextView;
    private TextView answerTextViewEN;
    private TextView answerTextViewPL;
    private CardView cardView;
    private Button buttonBackQuestion;
    private Button buttonAddToReplays;
    private Button buttoinNext;

    boolean is_addQuestion_to_replace_board;
    int currentQuestion;

    RepeatQuestionService repeatQuestionService;
    Converter converter;
    private List<RepeatQuestionDTO> repeatQuestionDTOList;

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

        FirebaseConfiguration firebaseConfiguration= new FirebaseConfiguration(this);
        converter= new Converter(firebaseConfiguration);
        repeatQuestionService= new RepeatQuestionService(firebaseConfiguration);
        repeatQuestionDTOList=repeatQuestionService.getRepeatQuestionDTOList();

        //is_addQuestion_to_replace_board=true;
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
        showQuestion(0);
    }

    private void showPrevioQuestion() {
    }

    private void addOrDeleteQuestion() {
        if(repeatQuestionDTOList.size()!=0){
            RepeatQuestion repeatQuestion=converter.repeatQuestionDTOtoRepeatQuestion(repeatQuestionDTOList.get(currentQuestion));
            //spr czy pytanie jest w tablicy powtórek
            is_addQuestion_to_replace_board=repeatQuestionDTOList.get(currentQuestion).getIsAddToRepeatBoard();
            // jeśli pytanie nie jest dodane do powtórek i chcemy je dodać
            if(is_addQuestion_to_replace_board==false){
                repeatQuestionService.saveQuestionToDBRepeatTable(repeatQuestion);
                is_addQuestion_to_replace_board=true;
                setColorBtnAddToReplace(is_addQuestion_to_replace_board);
                repeatQuestionDTOList.get(currentQuestion).setAddToRepeatBoard(is_addQuestion_to_replace_board);
            }
            //jeśli pytanie jest dodane do powtórek i chcemy usunć z tablicy powtórek
            else {
                repeatQuestionService.deleteQuestionToDBRepeatTable(repeatQuestion);
                is_addQuestion_to_replace_board=false;
                setColorBtnAddToReplace(is_addQuestion_to_replace_board);
                repeatQuestionDTOList.get(currentQuestion).setAddToRepeatBoard(is_addQuestion_to_replace_board);
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
                    is_addQuestion_to_replace_board=repeatQuestionDTOList.get(currentQuestion).getIsAddToRepeatBoard();
                    setColorBtnAddToReplace(is_addQuestion_to_replace_board);
                    //ustaw pola
                    byte[] byteImg = repeatQuestionDTOList.get(currentQuestion).getName_image();
                    Bitmap bmp = BitmapFactory.decodeByteArray(byteImg, 0, byteImg.length);
                    //image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(), image.getHeight(), false));
                    image.setImageBitmap(bmp);
                    questionTextView.setText(repeatQuestionDTOList.get(currentQuestion).getQuestion());
                    answerTextViewEN.setText(repeatQuestionDTOList.get(currentQuestion).getOptionEN());
                    answerTextViewPL.setText(repeatQuestionDTOList.get(currentQuestion).getOptionPL());
                    //spr czy pytanie jest w tablicy powtórek
                    is_addQuestion_to_replace_board=repeatQuestionDTOList.get(currentQuestion).getIsAddToRepeatBoard();
                    setColorBtnAddToReplace(is_addQuestion_to_replace_board);
                }
            }
            //wciśnięty przycisk poprzednie pytanie
            else if(value==-1){
                currentQuestion--;

                setBtnNextandBtnPrevious();

                    byte[] byteImg=repeatQuestionDTOList.get(currentQuestion).getName_image();
                    Bitmap bmp = BitmapFactory.decodeByteArray(byteImg, 0, byteImg.length);
                    //image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(), image.getHeight(), false));
                    image.setImageBitmap(bmp);
                    questionTextView.setText(repeatQuestionDTOList.get(currentQuestion).getQuestion());
                    answerTextViewEN.setText(repeatQuestionDTOList.get(currentQuestion).getOptionEN());
                    answerTextViewPL.setText(repeatQuestionDTOList.get(currentQuestion).getOptionPL());
                //spr czy pytanie jest w tablicy powtórek
                is_addQuestion_to_replace_board=repeatQuestionDTOList.get(currentQuestion).getIsAddToRepeatBoard();
                setColorBtnAddToReplace(is_addQuestion_to_replace_board);

            }
            //nowa katywność żaden przycisk wstecz lub do przodu nie jest kliknięty
            else if(value==0){
                if(repeatQuestionDTOList.get(currentQuestion).getName_image()!=null){
                    byte[] byteImg=repeatQuestionDTOList.get(currentQuestion).getName_image();
                    Bitmap bmp = BitmapFactory.decodeByteArray(byteImg, 0, byteImg.length);
                    //image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(), image.getHeight(), false));
                    image.setImageBitmap(bmp);
                }

                questionTextView.setText(repeatQuestionDTOList.get(currentQuestion).getQuestion());
                answerTextViewEN.setText(repeatQuestionDTOList.get(currentQuestion).getOptionEN());
                answerTextViewPL.setText(repeatQuestionDTOList.get(currentQuestion).getOptionPL());
                //spr czy pytanie jest w tablicy powtórek
                is_addQuestion_to_replace_board=repeatQuestionDTOList.get(currentQuestion).getIsAddToRepeatBoard();
                setColorBtnAddToReplace(is_addQuestion_to_replace_board);

                setBtnNextandBtnPrevious();
            }
        }
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
}
