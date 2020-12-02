package com.example.fiszki;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;
import com.example.fiszki.entity.RepeatQuestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class FirebaseConfiguration {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static long questionNr=10;
    private DatabaseReference myRef = database.getReference().child("question");
    List<Question> questionList;
    List<Option> optionList;
    List<QuestionDTO> questionDTOList;
    Context context;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public FirebaseConfiguration(Context context) {
        this.context=context;
        questionList=new ArrayList<>();
        optionList= new ArrayList<>();
        questionDTOList= new ArrayList<>();
        //pobierz z pamięci tel informacje o liczbie pytań w bazie
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        questionNr = Long.parseLong(settings.getString("count_question", "10"));

        //ustaw nasłuchacz
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        List<Option>optionToOneQuestionDTO=new ArrayList<>();
                        Option optionPL=new Option();
                        Option optionEN=new Option();
                        Question question= new Question();
                        String name = (String) child.child("name").getValue();
                        question.setName(name);
                        String image = (String) child.child("image").getValue();
                        questionList.add(question);
                        int rightAnswer= child.child("optionPL").child("good_ans").getValue(Integer.class);
                        for(int i=0; i<3; i++){
                            if(rightAnswer==i){
                                optionPL.setIs_right(1);
                                optionEN.setIs_right(1);
                            }else {
                                optionPL.setIs_right(0);
                                optionEN.setIs_right(0);
                            }
                            optionPL.setQuestion_id(Long.parseLong(child.getKey()));
                            optionPL.setLanguage("PL");
                            optionPL.setName(child.child("optionPL").child(String.valueOf(i+1)).getValue(String.class));

                            optionEN.setQuestion_id(Long.parseLong(child.getKey()));
                            optionEN.setLanguage("EN");
                            optionEN.setName(child.child("optionEN").child(String.valueOf(i+1)).getValue(String.class));

                            optionList.add(optionPL);
                            optionList.add(optionEN);
                            optionToOneQuestionDTO.add(optionPL);
                            optionToOneQuestionDTO.add(optionEN);
                        }
                        questionDTOList.add(new QuestionDTO(question, optionToOneQuestionDTO));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addQuestion(Question q, String imageId){
//        Map<String, Object> question = new HashMap<>();
//        question.put("name", q.getName());
//        question.put("image", q.getName_image());

        questionNr++;
        myRef.child(String.valueOf(questionNr)).child("name").setValue(q.getName());
        if(q.getName_image()!=null){
            myRef.child(String.valueOf(questionNr)).child("image").setValue(imageId);
        }

        //zapisz liczbe pytań do pamięci telefonu
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("count_question", String.valueOf(questionNr));
        editor.commit();
    }

    public void addOptionsPL(List<Option> options) {
        AtomicInteger withOptionIsRight = new AtomicInteger();
        options.forEach(o->{
            if(o.getIs_right()==1){
                //+ 1 ponieważ indeksowana lista jest od 0 a chcę ustawić od 1 opdpowiedzi aż do 3
                withOptionIsRight.set(options.indexOf(o)+1);
            }
        });
        for(int i=0; i<options.size(); i++){
            myRef.child(String.valueOf(questionNr)).child("optionPL").child(String.valueOf(i+1)).setValue(options.get(i).getName());
        }
        myRef.child(String.valueOf(questionNr)).child("optionPL").child(String.valueOf(4)).setValue(withOptionIsRight.toString());

    }

    public void addOptionsEN(List<Option> options) {
        AtomicInteger withOptionIsRight = new AtomicInteger();
        options.forEach(o->{
            if(o.getIs_right()==1){
                //+ 1 ponieważ indeksowana lista jest od 0 a chcę ustawić od 1 opdpowiedzi aż do 3
                withOptionIsRight.set(options.indexOf(o)+1);
            }
        });
        for(int i=0; i<options.size(); i++){
            myRef.child(String.valueOf(questionNr)).child("optionEN").child(String.valueOf(i+1)).setValue(options.get(i).getName());
        }
        myRef.child(String.valueOf(questionNr)).child("optionEN").child(String.valueOf(4)).setValue(withOptionIsRight.toString());

    }

    public List<Question> getQuestions() {
        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questionList.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Question question = keyNode.getValue(Question.class);
                    questionList.add(question);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               Log.w("Failed to read value.", error.toException());
            }
        });
        return questionList;
    }

    public Question getQuestionById(long id) {
        return null;
    }

    public Option getGoodOptionPL(long questionNumber) {
        return null;
    }

    public Option getGoodOptionEN(long questionNumber) {
        return null;
    }

    public long addQuestionToRepeatTable(long idQuestion){

        return 1;
    }

    public int deleteQuestionFromRepeatTable(long idQuestion){
        return 1;
    }

    public List<RepeatQuestion> getAllQuestionFromRepeatTable(){
        List<RepeatQuestion> repeatQuestionList= new ArrayList<>();
        return repeatQuestionList;
    }

    public Question getQuestionByName(String nameQuestion) {
        return null;
    }

}
