package com.example.fiszki.activityPanel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fiszki.FirebaseConfiguration;
import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.R;
import com.example.fiszki.StorageFirebase;
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
    List<Option> optionListPL;
    List<Option> optionListEN;
    int option_nr;

    Button saveOption;
    Button btnBack;
    QuizDbHelper dbHelper;

    List<Integer> listRightAnswer;
    boolean is_edit_Option;

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

        if (savedInstanceState == null) {
            dbHelper = new QuizDbHelper(this);
            question_save=getIntent().getParcelableExtra("question");

        }else {
            //przywracanie zmiennych po obróceniu telefonu
        }
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
        optionPLEditText.setText(optionListPL.get(option_nr-1).getName());
        optionENEditText.setText(optionListEN.get(option_nr-1).getName());
        //ustaw checkbox
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
                Toast.makeText(this, "You cannot set the answer to true a second time", Toast.LENGTH_LONG).show();
            }
            //jeśłi w ostatnim pytaniu lista z odpowiedziami nie będzie zawierała odpowiedzi zaznaczonej jako odp poprawna
             else if(option_nr==3 && !listRightAnswer.contains(1) && !checkBox.isChecked()){
                saveOption.setEnabled(true);
                Toast.makeText(this, R.string.no_answer_is_marked_as_correct, Toast.LENGTH_LONG).show();
            } else{
                saveOption.setEnabled(false);
                Option optionPL= new Option(question_save.getId(), optionPLEditText.getText().toString(), is_Right, "PL");
                Option optionEN= new Option(question_save.getId(), optionENEditText.getText().toString(), is_Right, "EN");
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
        StorageFirebase storageFirebase= new StorageFirebase();
        String imageUrl=storageFirebase.fileuploaderfromUri(question_save.getUploadImageUri(), question_save.getExtensionImg());
        FirebaseConfiguration firebaseConfiguration= new FirebaseConfiguration(this);
        firebaseConfiguration.addQuestion(question_save, imageUrl, this);



        firebaseConfiguration.addOptionsPL(optionListPL);
        firebaseConfiguration.addOptionsEN(optionListEN);

    }

    private void updateOptionNr() {
        option_nr++;
        optionNumber.setText("Option number: "+option_nr);
    }

    private void clearInput() {
        optionPLEditText.getText().clear();
        optionENEditText.getText().clear();
        checkBox.setChecked(false);
        saveOption.setEnabled(true);
    }
}
