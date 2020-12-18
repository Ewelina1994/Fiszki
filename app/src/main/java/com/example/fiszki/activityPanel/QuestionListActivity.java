package com.example.fiszki.activityPanel;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiszki.FirebaseConfiguration;
import com.example.fiszki.QuestionDTO;
import com.example.fiszki.R;
import com.example.fiszki.RecyclerViewConfig;

import java.util.List;

public class QuestionListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_all_question);
        mRecyclerView=findViewById(R.id.recyclerId);
        new FirebaseConfiguration(this).readAllQuestions(new FirebaseConfiguration.DataStatus() {
            @Override
            public void DataIsLoaded(List<QuestionDTO> questionDTOList, List<String> keys) {
                new RecyclerViewConfig().setConfig(mRecyclerView, QuestionListActivity.this, questionDTOList, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }

}
