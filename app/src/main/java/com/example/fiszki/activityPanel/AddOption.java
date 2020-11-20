package com.example.fiszki.activityPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.R;
import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;

import java.util.ArrayList;
import java.util.List;

public class AddOption extends AppCompatActivity {
    private TextView optionNumber;
    private EditText optionPLEditText;
    private EditText optionENEditText;
    CheckBox checkBox;
    int is_Right;
    Question question_save;
    List<Option> optionList;
    int option_nr;

    Button saveOption;
    QuizDbHelper dbHelper;

    List<Integer> listRightAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_option_admin_panel);
        optionNumber = (TextView) findViewById(R.id.option_number);
        optionPLEditText = (EditText) findViewById(R.id.option_et_PL);
        optionENEditText = (EditText) findViewById(R.id.option_et_EN);
        checkBox=(CheckBox) findViewById(R.id.checkBox);
        //ustawienei ze wpisujemy 1 opcje do pytania
        option_nr=1;
        //lista do której będziemy zapisywać zaznaczone checkboxy tak żeby ktoś 2 razy nie zaznaczył że odp jest prawidłowa
        listRightAnswer= new ArrayList<>();
        saveOption = (Button) findViewById(R.id.btnAddOption);
        optionList= new ArrayList<>();
        saveOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //saveOption.setEnabled(false);
                saveOption();
            }
        });

        if (savedInstanceState == null) {
            dbHelper = new QuizDbHelper(this);
            question_save=getIntent().getParcelableExtra("question");

        }else {
            //przywracanie zmiennych po obróceniu telefonu
        }
    }

    private void saveOption(){

        if(option_nr<=3){
            //jeśli checkbox jest zaznaczony ustaw wartość na 1
            if(checkBox.isChecked()){
                is_Right=1;
            }else {
                is_Right=0;
            }
            //jeśliwcześneijsza opcja została ustawiona na poprawną odpowiedz a user drugi raz prubuję ystawić nast opcję na prawdziwą
            if(is_Right==1 && listRightAnswer.contains(is_Right)){
                Toast.makeText(this, "You cannot set the answer to true a second time", Toast.LENGTH_LONG).show();
            }else {
                saveOption.setEnabled(false);
                Option optionPL= new Option(optionPLEditText.getText().toString(), is_Right, "PL");
                //dbHelper.addOption(optionPL);
                optionList.add(optionPL);

                Option optionEN= new Option(optionENEditText.getText().toString(), is_Right, "EN");
                //dbHelper.addOption(optionEN);
                optionList.add(optionEN);

                addCheckboxAnswerttOlIST();

                if(option_nr==3){
                    saveToDatabase();
                    finish();
                }else{
                    clearInput();
                    updateOptionNr();
                }
            }


        }else {
            finish();
        }
    }

    private void saveToDatabase() {
        //wstawienie do bazy i pobranei nowo wygenerowanegoo klucza id
        Long idQuestion=dbHelper.addQuestion(question_save);

        optionList.forEach(option->{
            option.setQuestion_id(idQuestion);
            dbHelper.addOption(option);
        });
    }

    private void updateOptionNr() {
        option_nr++;
        optionNumber.setText("Option number: "+option_nr);
    }

    private void addCheckboxAnswerttOlIST() {
        listRightAnswer.add(is_Right);
    }

    private void clearInput() {
        optionPLEditText.getText().clear();
        optionENEditText.getText().clear();
        checkBox.setChecked(false);
        saveOption.setEnabled(true);
    }
}
