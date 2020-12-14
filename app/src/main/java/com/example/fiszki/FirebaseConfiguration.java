package com.example.fiszki;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;
import com.example.fiszki.entity.RepeatQuestion;
import com.example.fiszki.enums.LanguageEnum;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class FirebaseConfiguration {

     static FirebaseDatabase database = FirebaseDatabase.getInstance();
     static long questionNr=10;
     static DatabaseReference myRef = database.getReference().child("question");
     static List<Option> optionList;
     static List<QuestionDTO> questionDTOList;
     static Context context;
    static StorageReference mStorage = FirebaseStorage.getInstance().getReference("Images");

    public FirebaseConfiguration(Context context) {
        this.context=context;
        optionList= new ArrayList<>();
        questionDTOList= new ArrayList<>();
        //pobierz z pamięci tel informacje o liczbie pytań w bazie
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        questionNr = Long.parseLong(settings.getString("count_question", "10"));
        //ustaw nasłuchacz
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    //jeśli usunę coś z bazy zmien liczbe question
                    questionNr=dataSnapshot.getChildrenCount();
                    //zapisz liczbe pytań do pamięci telefonu
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("count_question", String.valueOf(questionNr));
                    editor.commit();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        List<Option>optionToOneQuestionDTO=new ArrayList<>();

                        Question question= new Question();
                        String name = (String) snapshot.child("name").getValue();
                        question.setName(name);
                        long id_question= Long.parseLong(snapshot.getKey());
                        question.setId(id_question);
                        String imagePath = (String) snapshot.child("image").getValue();
                        mStorage.child(imagePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                question.setUploadImageUri(uri);
                            }
                        });


                        int rightAnswer= snapshot.child("optionPL").child("good_ans").getValue(Integer.class);
                        for(int i=0; i<3; i++){
                            Option optionPL=new Option();
                            Option optionEN=new Option();
                            if(rightAnswer==i+1){
                                optionPL.setIs_right(1);
                                optionEN.setIs_right(1);
                            }else {
                                optionPL.setIs_right(0);
                                optionEN.setIs_right(0);
                            }
                            optionPL.setQuestion_id(Long.parseLong(snapshot.getKey()));
                            optionPL.setLanguage("PL");
                            optionPL.setName(snapshot.child("optionPL").child(String.valueOf(i+1)).getValue(String.class));

                            optionEN.setQuestion_id(Long.parseLong(snapshot.getKey()));
                            optionEN.setLanguage("EN");
                            optionEN.setName(snapshot.child("optionEN").child(String.valueOf(i+1)).getValue(String.class));

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

    public static void addQuestion(Question q, String imageUrl, Context context){
//        Map<String, Object> question = new HashMap<>();
//        question.put("name", q.getName());
//        question.put("image", q.getName_image());
        context=context;
        questionNr++;
        myRef.child(String.valueOf(questionNr)).child("name").setValue(q.getName());
        if(q.getUploadImageUri()!=null){
            myRef.child(String.valueOf(questionNr)).child("image").setValue(imageUrl);
        }

        //zapisz liczbe pytań do pamięci telefonu
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("count_question", String.valueOf(questionNr));
        editor.commit();
    }

    public static void addOptionsPL(List<Option> options) {
        AtomicInteger withOptionIsRight = new AtomicInteger();
        for (Option option: options) {
            if(option.getIs_right()==1){
                //+ 1 ponieważ indeksowana lista jest od 0 a chcę ustawić od 1 opdpowiedzi aż do 3
                withOptionIsRight.set(options.indexOf(option)+1);
            }
        }
        for(int i=0; i<options.size(); i++){
            myRef.child(String.valueOf(questionNr)).child("optionPL").child(String.valueOf(i+1)).setValue(options.get(i).getName());
        }
        myRef.child(String.valueOf(questionNr)).child("optionPL").child("good_ans").setValue(Integer.parseInt(withOptionIsRight.toString()));

    }

    public static void addOptionsEN(List<Option> options) {
        AtomicInteger withOptionIsRight = new AtomicInteger();
        for (Option option: options) {
            if(option.getIs_right()==1){
                //+ 1 ponieważ indeksowana lista jest od 0 a chcę ustawić od 1 opdpowiedzi aż do 3
                withOptionIsRight.set(options.indexOf(option)+1);
            }
        }
        for(int i=0; i<options.size(); i++){
            myRef.child(String.valueOf(questionNr)).child("optionEN").child(String.valueOf(i+1)).setValue(options.get(i).getName());
        }
        myRef.child(String.valueOf(questionNr)).child("optionEN").child("good_ans").setValue(Integer.parseInt(withOptionIsRight.toString()));

    }

//    public static List<Question> getQuestions() {
        // Read from the database
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//               // questionList.clear();
//                List<String> keys = new ArrayList<>();
//                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
//                    Question question = keyNode.getValue(Question.class);
//                   // questionList.add(question);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//               Log.w("Failed to read value.", error.toException());
//            }
//        });
//
//            return questionDTOList;
//    }

    public static List<QuestionDTO>getAllQuestionDTO(){

        return questionDTOList;
    }

    public static Question getQuestionById(long id) {
        return null;
    }

    public static Option getGoodOptionPL(long questionNumber) {
        return null;
    }

    public static Option getGoodOptionEN(long questionNumber) {
        return null;
    }

    public static long addQuestionToRepeatTable(long idQuestion){

        return 1;
    }

    public static int deleteQuestionFromRepeatTable(long idQuestion){
        return 1;
    }

    public static List<RepeatQuestion> getAllQuestionFromRepeatTable(){
        List<RepeatQuestion> repeatQuestionList= new ArrayList<>();
        return repeatQuestionList;
    }

    public static Question getQuestionByName(String nameQuestion) {
        return null;
    }

    public static List<Option> getOptionsToQuiz(long questionNumber, LanguageEnum l) {
        List<Option> optionsList = new ArrayList<>();
        for (Option option: optionList) {
            if(option.getQuestion_id()==questionNumber && option.getLanguage()==l.toString()){
                optionsList.add(option);
            }
        }
        return optionsList;
    }

}
