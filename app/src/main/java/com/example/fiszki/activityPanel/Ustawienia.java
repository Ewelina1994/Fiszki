package com.example.fiszki.activityPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.fiszki.R;
import com.example.fiszki.TextToSpeachImpl;
import com.example.fiszki.activityPanel.QuizActivity;
import com.example.fiszki.services.QuizService;
import com.gtranslate.parsing.Parse;

public class Ustawienia extends AppCompatActivity {
    String PITCH="pitch";
    String SPEED="speed";
    String NUMBER_OF_QUESTIONS="number";
    int pitch;
    int speed;
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
            seekBarPitch.setMax(2);
            seekBarPitch.setMin((int) 0.1);

            seekBarSpeed.setMin(0);
            seekBarSpeed.setMax(2);

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
        pitch = prefs.getInt(PITCH, 0);
        speed = prefs.getInt(SPEED, 0);
        numbers=prefs.getInt(NUMBER_OF_QUESTIONS, 5);
        seekBarPitch.setProgress(pitch);
        seekBarSpeed.setProgress(speed);
        howManyQuestionET.setText(String.valueOf(numbers));

    }

    private void setting() {
        QuizService.NUMBER_QUESTIONS = Integer.parseInt(howManyQuestionET.getText().toString());
        pitch= seekBarPitch.getProgress();
        speed = seekBarSpeed.getProgress();
        numbers = Integer.parseInt(howManyQuestionET.getText().toString());
        TextToSpeachImpl.PITCH=pitch;
        TextToSpeachImpl.SPEED=speed;

        setToSharedPreference();
        finish();
    }

    private void setToSharedPreference() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PITCH, pitch);
        editor.putInt(SPEED, speed);
        editor.putInt(NUMBER_OF_QUESTIONS, numbers);
        editor.apply();
    }
}
