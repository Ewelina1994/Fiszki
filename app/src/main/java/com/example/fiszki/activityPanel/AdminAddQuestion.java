package com.example.fiszki.activityPanel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.R;
import com.example.fiszki.entity.Question;

public class AdminAddQuestion extends AppCompatActivity {
    private EditText questionEditT;
    Button saveQuestion;
    QuizDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        questionEditT = findViewById(R.id.txtQuestion);
        saveQuestion = findViewById(R.id.btnAddQuestion);
        saveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestion();
            }
        });

        if (savedInstanceState == null) {
            dbHelper = new QuizDbHelper(this);
        }else {
            //przywracanie zmiennych po obróceniu telefonu
        }
    }

    private void saveQuestion(){
        Question newQuestion=new Question(questionEditT.getText().toString());
        dbHelper.addQuestion(newQuestion);
    }

}
