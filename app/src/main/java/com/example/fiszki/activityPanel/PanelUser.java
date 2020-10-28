package com.example.fiszki.activityPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.fiszki.R;

public class PanelUser extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText imie;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioGroup radioGroup;
    private Spinner spinner;
    private EditText datepicker;
    private ToggleButton togglebtn;
    private CheckBox regulamin;
    private String komunikat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        imie = (EditText) findViewById(R.id.nickStudent);

        spinner= (Spinner) findViewById(R.id.spinner2);

        rb1=(RadioButton)findViewById(R.id.radioButton1);
        rb2=(RadioButton)findViewById(R.id.radioButton2);

        radioGroup=(RadioGroup)findViewById(R.id.poziomRadioGroup);
        datepicker=(EditText) findViewById(R.id.years);
        togglebtn=(ToggleButton) findViewById(R.id.toggleButton);
        regulamin=(CheckBox)findViewById(R.id.regulami);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    getPoziom((String.valueOf(spinner.getSelectedItem())));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
    }
    public void wybierz(View view){
        String s=(String.valueOf(spinner.getSelectedItem()));
        Log.v("wybrany: ", s);
        getPoziom((String.valueOf(spinner.getSelectedItem())));
    }
    public void getPoziom(String wybr_poz){
        switch (wybr_poz){
            case "podstawowy":
                rb1.setText("A1");
                rb2.setText("A2");
                break;

            case "średniozaawansowany":
                rb1.setText("B1");
                rb2.setText("B2");
                break;

            case "zaawansowany":
                rb1.setText("C1");
                rb2.setText("C2");
        }
    }

    public void wyslij(View view){
        RadioButton rb = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        if (rb == null || imie.getText().toString().isEmpty()|| datepicker.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.validationPanelUser, Toast.LENGTH_LONG).show();
        }else {
            komunikat="Imię: "+imie.getText().toString()+
                    "\nJego poziom to: "
                    +spinner.getSelectedItem().toString()+", "
                    +rb.getText().toString()
                    +"\nUczy się od: "
                    +datepicker.getText().toString()
                    +"\nCzy chce być widoczny dla innych użytkowników: "
                    +togglebtn.getText().toString()
                    +"\nCzy akceptuje regulamin: ";
            if(regulamin.isChecked()){
                komunikat+="TAK";
            }
            else {
                komunikat+="NIE";
            }
            komunikat+="\n TAKIE DANE ZOSTAŁY ZAPISANE W BAZIE";
            Toast.makeText(this, komunikat, Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
