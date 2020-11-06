package com.example.fiszki.activityPanel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.R;
import com.example.fiszki.RepeatQuestionDTO;
import com.example.fiszki.services.RepeatQuestionService;

import java.util.List;

public class RepeatBoard extends AppCompatActivity {
    ImageView image;
    private TextView questionTextView;
    private TextView answerTextViewEN;
    private TextView answerTextViewPL;
    private CardView cardView;
    private Button buttonAddToReplays;
    private Button buttoinNext;

    boolean is_addQuestion_to_replace_board;
    int currentQuestion;

    RepeatQuestionService repeatQuestionService;

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
        buttonAddToReplays=findViewById(R.id.btnAddToReplays);
        buttoinNext=findViewById(R.id.btnNextQuestion);

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        repeatQuestionService= new RepeatQuestionService(dbHelper);
        repeatQuestionDTOList=repeatQuestionService.getRepeatQuestionDTOList();

        is_addQuestion_to_replace_board=true;
        currentQuestion=0;

        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_MOVE){
                    showQuestion();
                }
//                if(event.getAction()==MotionEvent.ACTION_DOWN){
//                    showQuestion();
//                }

                return true;
            }
        });

        buttonAddToReplays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //jeśli pytanie nie jest dodane do powtórek i chcemy je dodać
//                if(is_addQuestion_to_replace_board==false){
//                    repeatQuestionService.saveQuestionToDBRepeatTable(repeatQuestionDTOList.get(currentQuestion));
//                    is_addQuestion_to_replace_board=true;
//                    setColorBtnAddToReplace(is_addQuestion_to_replace_board);
//
//                }
//                //jeśli pytanie jest dodane do powtórek i chcemy usunć z tablicy powtórek
//                else {
//                    repeatQuestionService.deleteQuestionToDBRepeatTable(currentQuestion);
//                    is_addQuestion_to_replace_board=false;
//                    setColorBtnAddToReplace(is_addQuestion_to_replace_board);
//
//                }

            }
        });

        buttoinNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestion();
            }
        });

        showQuestion();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showQuestion() {
        if (currentQuestion<repeatQuestionDTOList.size()){
            byte[] byteImg=repeatQuestionDTOList.get(currentQuestion).getName_image();
            Bitmap bmp = BitmapFactory.decodeByteArray(byteImg, 0, byteImg.length);
            //image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(), image.getHeight(), false));
            image.setImageBitmap(bmp);
            questionTextView.setText(repeatQuestionDTOList.get(currentQuestion).getQuestion());
            answerTextViewEN.setText(repeatQuestionDTOList.get(currentQuestion).getOptionEN());
            answerTextViewPL.setText(repeatQuestionDTOList.get(currentQuestion).getOptionPL());

            currentQuestion++;
        }
    }
}
