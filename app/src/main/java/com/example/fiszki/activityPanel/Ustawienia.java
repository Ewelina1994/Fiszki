package com.example.fiszki.activityPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.fiszki.R;
import com.example.fiszki.TextToSpeachImpl;
import com.example.fiszki.activityPanel.QuizActivity;
import com.example.fiszki.services.QuizService;
import com.gtranslate.parsing.Parse;

import xdroid.toaster.Toaster;

public class Ustawienia extends AppCompatActivity {
    String PITCH="pitch";
    String SPEED="speed";
    String NUMBER_OF_QUESTIONS="number";
    double pitch=0.0f;
    double speed=0.0f;
    int numbers;

    SeekBar seekBarPitch;
    SeekBar seekBarSpeed;
    EditText howManyQuestionET;
    Button btnSetting;

    SharedPreferences prefs;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ustawienia);
        prefs= getSharedPreferences("shared_Pref", MODE_PRIVATE);

        if(savedInstanceState==null){
            seekBarPitch = findViewById(R.id.seek_bar_pitch);
            seekBarSpeed = findViewById(R.id.seek_bar_speed);
            howManyQuestionET=findViewById(R.id.howManyQuestion);
            btnSetting = findViewById(R.id.settingBtn);

            btnSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setting();
                }
            });

            loadLastSetting();
        }else {
        }

    }

    private void loadLastSetting() {
        int p = prefs.getInt(PITCH, 9);
        int s = prefs.getInt(SPEED, 9);
        numbers=prefs.getInt(NUMBER_OF_QUESTIONS, 5);
        seekBarPitch.setProgress(p);
        seekBarSpeed.setProgress(s);
        howManyQuestionET.setText(String.valueOf(numbers));

    }

    private void setting() {
        numbers = Integer.parseInt(howManyQuestionET.getText().toString());
        if(numbers>20){
            Toast.makeText(this, R.string.valid_to_big_number_setting_question, Toast.LENGTH_LONG);
        }else {
            QuizService.NUMBER_QUESTIONS = Integer.parseInt(howManyQuestionET.getText().toString());
            int progressPitch= seekBarPitch.getProgress();
            pitch=((double)progressPitch+1)/10;
            int progressSpech=seekBarSpeed.getProgress();
            speed = ((double)progressSpech+1)/10;

            TextToSpeachImpl.PITCH= (float) pitch;
            TextToSpeachImpl.SPEED=(float) speed;

            setToSharedPreference(progressPitch, progressSpech);
            finish();
        }

    }

    private void setToSharedPreference(int pitch, int speech) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PITCH, (int) pitch);
        editor.putInt(SPEED, speech);
        editor.putInt(NUMBER_OF_QUESTIONS, numbers);
        editor.apply();
    }
}
