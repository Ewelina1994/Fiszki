package com.example.fiszki.activityPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fiszki.R;

public class AdminPanel extends AppCompatActivity {
    private Button btnShowAllQuestions, btnAddNewQuestion;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_panel);
        btnShowAllQuestions= findViewById(R.id.btnAllQuestions);
        btnAddNewQuestion=findViewById(R.id.btnAddNew);
        btnShowAllQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToShowIntent();
            }
        });

        btnAddNewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddNewIntent();
            }
        });
    }

    private void goToAddNewIntent() {
        Intent newQuestion= new Intent(this, AdminAddQuestion.class);
        startActivity(newQuestion);
    }

    private void goToShowIntent() {
        Intent showIntent= new Intent(this, QuestionListActivity.class);
        startActivity(showIntent);
    }
}
