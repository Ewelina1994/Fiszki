package com.example.fiszki.activityPanel;

import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
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

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        repeatQuestionService= new RepeatQuestionService(dbHelper);
        repeatQuestionDTOList=repeatQuestionService.getRepeatQuestionDTOList();

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
        currentQuestion=0;

        showQuestion();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showQuestion() {
        if (currentQuestion<repeatQuestionDTOList.size()){
            questionTextView.setText(repeatQuestionDTOList.get(currentQuestion).getQuestion());
            answerTextViewEN.setText(repeatQuestionDTOList.get(currentQuestion).getOptionEN());
            answerTextViewPL.setText(repeatQuestionDTOList.get(currentQuestion).getOptionPL());

            currentQuestion++;
        }
    }
}
