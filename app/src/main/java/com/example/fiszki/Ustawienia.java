package com.example.fiszki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Ustawienia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ustawienia);

        //getSupportActionBar().setTitle("Settings");
        if(findViewById(R.id.fragment_ustawienia) != null){
            if(savedInstanceState != null){
                return;
            }
            getFragmentManager().beginTransaction().add(R.id.fragment_ustawienia, new UstawieniaFragment()).commit();
        }
    }
}
