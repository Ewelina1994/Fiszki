package com.example.fiszki.activityPanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Statystic extends AppCompatActivity {

//    private static final int REQUEST_CODE_QUIZ=1;
//    public static final String EXTRA_DIFFICULTY="extraDifficulty";
//
//    private ArrayList<Integer> wrongQuestion;
//    private ArrayList<Integer> scoreQuestion;
//
//    private int score;
//    private int wrong;
    private SQLiteOpenHelper organizacjaSQLiteOpenHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ListView listViewOrganizacje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statystic);
        listViewOrganizacje=(ListView) findViewById(R.id.listViewOrganizacja);
        try{

            organizacjaSQLiteOpenHelper = new QuizDbHelper(this);
            db= organizacjaSQLiteOpenHelper.getWritableDatabase();
        }
        catch(SQLException e){
            Toast toast= Toast.makeText(this, "Baza danych jest niedostępna", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

//        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> listViewOrganizacje, View view, int position, long id) {
//                Intent intent = new Intent(BazaDanych.this, BazaDanychAktualizacja.class);
//                intent.putExtra(BazaDanychAktualizacja.EXTRA_ORGANIZACJA_ID, (int) id);
//                startActivity(intent);
//            }
//        };

        //listViewOrganizacje.setOnItemClickListener(itemClickListener);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent int1 = new Intent(BazaDanych.this, BazaDanychDopisz.class);
//                startActivity(int1);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cursor = db.query("quiz_static", new String[] {"_id", "level", "score", "wrong", "date"}, null, null, null, null, null);

        if(cursor.moveToFirst()){
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                   R.layout.statistic_adapter,
                    cursor, new String[] {"level", "score", "wrong", "date"},
                    new int[] {R.id.text1, R.id.text2, R.id.text3, R.id.text4}, 0);
            listViewOrganizacje.setAdapter(listAdapter);
        }
    }

}