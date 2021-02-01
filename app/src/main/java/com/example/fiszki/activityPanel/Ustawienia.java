package com.example.fiszki.activityPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.fiszki.R;
import com.example.fiszki.TextToSpeachImpl;
import com.example.fiszki.activityPanel.QuizActivity;
import com.example.fiszki.services.QuizService;

public class Ustawienia extends AppCompatActivity {

    SeekBar seekBarPitch;
    SeekBar seekBarSpeed;
    EditText howManyQuestionET;
    Button btnSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ustawienia);

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
    }

    private void setting() {
        QuizService.NUMBER_QUESTIONS = Integer.parseInt(howManyQuestionET.getText().toString());
        int pitch= seekBarPitch.getProgress();
        int speed = seekBarSpeed.getProgress();
        TextToSpeachImpl.PITCH=pitch*2/100;
        TextToSpeachImpl.SPEED=speed*2/100;
        finish();
    }
}
