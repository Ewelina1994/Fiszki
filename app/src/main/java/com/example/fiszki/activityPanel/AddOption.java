package com.example.fiszki.activityPanel;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fiszki.FirebaseConfiguration;
import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.R;
import com.example.fiszki.TranslateGoogle;
import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javazoom.jl.decoder.JavaLayerException;

public class AddOption extends AppCompatActivity {

    private TextView optionNumber;
    private EditText optionPLEditText;
    private EditText optionENEditText;
    CheckBox checkBoxIsRightAnswer;
    CheckBox checkBoxTranslate;
    int is_Right;
    Question question_save;
    List<Option> optionListPL;
    List<Option> optionListEN;
    int option_nr;

    Button saveOption;
    Button btnBack;
    QuizDbHelper dbHelper;

    List<Integer> listRightAnswer;
    boolean is_edit_Option;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_option_admin_panel);
        optionNumber = (TextView) findViewById(R.id.option_number);
        optionPLEditText = (EditText) findViewById(R.id.option_et_PL);
        optionENEditText = (EditText) findViewById(R.id.option_et_EN);
        checkBoxIsRightAnswer =(CheckBox) findViewById(R.id.checkBoxGoodOption);
        checkBoxTranslate = findViewById(R.id.checkBoxTranslate);
        //ustawienei ze wpisujemy 1 opcje do pytania
        option_nr=1;
        //lista do której będziemy zapisywać zaznaczone checkboxy tak żeby ktoś 2 razy nie zaznaczył że odp jest prawidłowa
        listRightAnswer= new ArrayList<>();
        saveOption = (Button) findViewById(R.id.btnAddOption);
        btnBack=(Button) findViewById(R.id.btnPrevius);
        optionListPL= new ArrayList<>();
        optionListEN= new ArrayList<>();
        saveOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //saveOption.setEnabled(false);
                saveOption();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backToPreviusOption();
            }
        });
        checkBoxTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    translateOptionToEn();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }
        });

        if (savedInstanceState == null) {
            dbHelper = new QuizDbHelper(this);
                question_save=getIntent().getParcelableExtra("question");

            }
        else {
        //po odwróceniu ekranu
        }
    }

    private void translateOptionToEn() throws IOException, JavaLayerException {
        TranslateGoogle translateGoogle= new TranslateGoogle();
        String resultat = translateGoogle.translatePolishToEnglish(optionPLEditText.getText().toString());
        optionENEditText.setText(resultat);
    }


    private void backToPreviusOption() {
        if(option_nr!=1){
            option_nr--;
            displayTextAndCheckboxCurrentOption();

            optionNumber.setText("Option number: "+option_nr);
            is_edit_Option = true;
        }
    }

    private void displayTextAndCheckboxCurrentOption() {
        if(option_nr<= optionListPL.size()){
            optionPLEditText.setText(optionListPL.get(option_nr-1).getName());
            optionENEditText.setText(optionListEN.get(option_nr-1).getName());
            //ustaw checkbox
            if(optionListPL.get(option_nr-1).getIs_right()==1){
                checkBoxIsRightAnswer.setChecked(true);
            }else {
                checkBoxIsRightAnswer.setChecked(false);
            }
        }else {
            clearInput();
        }
    }

    private void saveOption(){

        if(option_nr<=3){
            //jeśli checkbox jest zaznaczony ustaw wartość na 1
            if(checkBoxIsRightAnswer.isChecked()){
                is_Right=1;
            }else {
                is_Right=0;
            }
            //jeśliwcześneijsza opcja została ustawiona na poprawną odpowiedz a user drugi raz prubuję ystawić nast opcję na prawdziwą
            if(is_Right==1 && listRightAnswer.contains(is_Right)){
                Toast.makeText(this, R.string.validator_only_once_good_option_save, Toast.LENGTH_LONG).show();
            }
            //jeśłi w ostatnim pytaniu lista z odpowiedziami nie będzie zawierała odpowiedzi zaznaczonej jako odp poprawna
             else if(option_nr==3 && !listRightAnswer.contains(1) && !checkBoxIsRightAnswer.isChecked()){
                saveOption.setEnabled(true);
                Toast.makeText(this, R.string.no_answer_is_marked_as_correct, Toast.LENGTH_LONG).show();
            } else{
                saveOption.setEnabled(false);
                Option optionPL= new Option(option_nr, question_save.getId(), optionPLEditText.getText().toString(), is_Right, "PL");
                Option optionEN= new Option(question_save.getId(), optionENEditText.getText().toString(), is_Right, "EN");

                //spr czy już opdpowiedz istnieje na liście a tylko ją edytujemy
                optionListPL.forEach(o->{
                    if(o.getId()==option_nr){
                        is_edit_Option=true;
                    }

                });
                 if(is_edit_Option){
                     optionListPL.set(option_nr-1, optionPL);
                     optionListEN.set(option_nr-1, optionEN);
                     listRightAnswer.set(option_nr-1, is_Right);

                 }else{
                     optionListPL.add(optionPL);
                     optionListEN.add(optionEN);
                     listRightAnswer.add(is_Right);
                 }

                if(option_nr==3) {
                    saveToDatabase();
                    finish();
                }else if(is_edit_Option){
                    updateOptionNr();
                    displayTextAndCheckboxCurrentOption();
                    is_edit_Option=false;
                    saveOption.setEnabled(true);
                }else {
                    updateOptionNr();
                    clearInput();
                }
            }
        }else {
            finish();
        }
    }


    private void saveToDatabase() {
        FirebaseConfiguration.addQuestion(question_save);


        FirebaseConfiguration.addOptions(optionListPL, "PL");
        FirebaseConfiguration.addOptions(optionListEN, "EN");

    }

    private void updateOptionNr() {
        option_nr++;
        optionNumber.setText("Option number: "+option_nr);
    }

    private void clearInput() {
        optionPLEditText.getText().clear();
        optionENEditText.getText().clear();
        checkBoxIsRightAnswer.setChecked(false);
        saveOption.setEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
