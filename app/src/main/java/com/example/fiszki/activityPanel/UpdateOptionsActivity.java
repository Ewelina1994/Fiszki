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
import com.example.fiszki.entity.QuestionDTO;
import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.R;
import com.example.fiszki.entity.Option;

import java.util.ArrayList;
import java.util.List;

public class UpdateOptionsActivity extends AppCompatActivity {
    private TextView optionNumber;
    private EditText optionPLEditText;
    private EditText optionENEditText;
    CheckBox checkBox;
    int is_Right;
    List<Option> optionListPL;
    List<Option> optionListEN;
    int option_nr;

    Button saveOption;
    Button btnBack;
    QuizDbHelper dbHelper;

    List<Integer> listRightAnswer;
    boolean is_edit_Option;

    QuestionDTO question_save;
    int key;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_option_admin_panel);
        optionNumber = (TextView) findViewById(R.id.option_number);
        optionPLEditText = (EditText) findViewById(R.id.option_et_PL);
        optionENEditText = (EditText) findViewById(R.id.option_et_EN);
        checkBox=(CheckBox) findViewById(R.id.checkBoxGoodOption);
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
                saveOption2();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backToPreviusOption();
            }
        });

        if (savedInstanceState == null) {
            dbHelper = new QuizDbHelper(this);
            question_save=getIntent().getParcelableExtra("questionDTO");
            key = getIntent().getIntExtra("key", -1);
            initialzeListsToComeIntent();

        }
        else {
            //po odwróceniu ekranu
        }
    }

    private void initialzeListsToComeIntent() {
        //ustawienie list opcji po polski i po angielsku
        question_save.getOptions().forEach(o->{
            if(o.getLanguage().equalsIgnoreCase("PL")){
                optionListPL.add(o);
            }else {
                optionListEN.add(o);
            }
        });

        //ustawienie listy poprawnych odpowiedzi
        optionListPL.forEach(o->{
            listRightAnswer.add(o.getIs_right());
        });

         //ustawienie strony editText
        setFields();

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
                checkBox.setChecked(true);
            }else {
                checkBox.setChecked(false);
            }
        }else {
//            clearInput();
            saveOption.setEnabled(true);
        }
    }
    private void saveOption2(){
        if(option_nr<=3) {

            //jeśli checkbox jest zaznaczony ustaw wartość na 1
            if(checkBox.isChecked()){
                is_Right=1;
            }else {
                is_Right=0;
            }

                saveOption.setEnabled(false);
                Option optionPL= new Option(question_save.getQuestion().getId(), optionPLEditText.getText().toString(), is_Right, "PL");
                Option optionEN= new Option(question_save.getQuestion().getId(), optionENEditText.getText().toString(), is_Right, "EN");

                optionListPL.set(option_nr-1, optionPL);
                optionListEN.set(option_nr-1, optionEN);
                listRightAnswer.set(option_nr-1, is_Right);

                if(option_nr==3) {
                    saveToDatabase();
                }else if(is_edit_Option){
                    updateOptionNr();
                    displayTextAndCheckboxCurrentOption();
                    is_edit_Option=false;
                    saveOption.setEnabled(true);
                }else {
                    updateOptionNr();
                    saveOption.setEnabled(true);
                    setFields();
                }

        }else {
            finish();
        }
    }

    private void setFields() {
        optionPLEditText.setText(optionListPL.get(option_nr-1).getName());
        optionENEditText.setText(optionListEN.get(option_nr-1).getName());
        if(optionListPL.get(option_nr-1).getIs_right()==1){
            checkBox.setChecked(true);

        }else {
            checkBox.setChecked(false);
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
                Toast.makeText(this, R.string.validator_only_once_good_option_save, Toast.LENGTH_LONG).show();
            }
            //jeśłi w ostatnim pytaniu lista z odpowiedziami nie będzie zawierała odpowiedzi zaznaczonej jako odp poprawna
            else if(option_nr==3 && !listRightAnswer.contains(1) && !checkBox.isChecked()){
                saveOption.setEnabled(true);
                Toast.makeText(this, R.string.no_answer_is_marked_as_correct, Toast.LENGTH_LONG).show();
            } else{
                saveOption.setEnabled(false);
                Option optionPL= new Option(option_nr, question_save.getQuestion().getId(), optionPLEditText.getText().toString(), is_Right, "PL");
                Option optionEN= new Option(question_save.getQuestion().getId(), optionENEditText.getText().toString(), is_Right, "EN");

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
//                    clearInput();
                    saveOption.setEnabled(true);
                }
            }
        }else {
            finish();
        }
    }


    private void saveToDatabase() {
        //jeśłi na liścei poprawnych odpodiwdzi znajduję się więcej niż raz poprawna odpowiedź
        Integer howMuchRightAnswer= (int) listRightAnswer.stream().filter(item -> item.equals(1)).count();
        if(howMuchRightAnswer>1){
            saveOption.setEnabled(true);
            Toast.makeText(this, R.string.validator_only_once_good_option_save, Toast.LENGTH_LONG).show();
        }

        //jeśłi w ostatnim pytaniu lista z odpowiedziami nie będzie zawierała odpowiedzi zaznaczonej jako odp poprawna
        else if(!listRightAnswer.contains(1) && !checkBox.isChecked()){
            saveOption.setEnabled(true);
            Toast.makeText(this, R.string.no_answer_is_marked_as_correct, Toast.LENGTH_LONG).show();
        }
        //zapisz do bazy danych
        else{
            List<Option> allOptions=new ArrayList<>();
            allOptions.addAll(optionListEN);
            allOptions.addAll(optionListPL);
            question_save.setOptions(allOptions);

        FirebaseConfiguration.updateQuestionDTO(question_save.getQuestion(), key, optionListPL, optionListEN);

            finish();
        }

    }

    private void updateOptionNr() {
        option_nr++;
        optionNumber.setText("Option number: "+option_nr);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
