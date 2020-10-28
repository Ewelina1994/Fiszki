package com.example.fiszki.activityPanel;

import android.content.Intent;
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

import com.example.fiszki.QuestionDTO;
import com.example.fiszki.R;
import com.example.fiszki.entity.Option;
import com.example.fiszki.services.RepeatBoardService;

import java.util.ArrayList;
import java.util.List;

import static com.example.fiszki.activityPanel.QuizActivity.repeatBoardService;

public class RepeatBoard extends AppCompatActivity {
    ImageView image;
    private TextView questionTextView;
    private TextView answerTextView;
    private TextView sentence;
    private CardView cardView;

    int currentQuestion;

    private List<QuestionDTO> questionList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replace_board);
        image = findViewById(R.id.imageView2);
        int img=R.drawable.def_as_;
        image.setImageResource(img);
        questionTextView = findViewById(R.id.questionTxt);
        answerTextView = findViewById(R.id.answerTxt);
        sentence = findViewById(R.id.sentenceTxt);
        cardView=findViewById(R.id.card_view_question);
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

        RepeatBoardService boardRepeat= repeatBoardService;
        questionList = boardRepeat.getAll();

        showQuestion();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showQuestion() {
        if (currentQuestion<questionList.size()){
            questionTextView.setText(questionList.get(currentQuestion).getQuestion().getQuestion());

            questionList.get(currentQuestion).getOptions().forEach(option -> {
                        if (option.getIs_right() == 1) {
                            answerTextView.setText(option.getOption());
                        }
                    }
            );
            currentQuestion++;
        }
    }
}
