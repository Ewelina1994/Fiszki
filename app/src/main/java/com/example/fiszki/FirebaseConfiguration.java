package com.example.fiszki;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.fiszki.activityPanel.MainActivity;
import com.example.fiszki.activityPanel.UpdateOptionsActivity;
import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class FirebaseConfiguration {

      static FirebaseDatabase database = FirebaseDatabase.getInstance();
      static long questionNr=10;
      static DatabaseReference myRef = database.getReference().child("question");
      static List<Option> optionList=new ArrayList<>();;
      static List<QuestionDTO> questionDTOList=new ArrayList<>();;
      Context context;
     static StorageReference mStorage = FirebaseStorage.getInstance().getReference("Images");
     static SharedPreferences settings;

    public interface DataStatus{
        void DataIsLoaded(List<QuestionDTO>questionDTOList, List<String>keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseConfiguration(Context context) {
        this.context=context;

        //pobierz z pamięci tel informacje o liczbie pytań w bazie
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        questionNr = Long.parseLong(settings.getString("count_question", "10"));
    }

    public static void readAllQuestions(final DataStatus dataStatus) {
        //ustaw nasłuchacz
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    questionDTOList.clear();
                    //jeśli usunę coś z bazy zmien liczbe question
                    questionNr=dataSnapshot.getChildrenCount();
                    //zapisz liczbe pytań do pamięci telefonu
//                    SharedPreferences.Editor editor = settings.edit();
//                    editor.putString("count_question", String.valueOf(questionNr));
//                    editor.commit();
                    List<String>keys=new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        List<Option>optionToOneQuestionDTO=new ArrayList<>();

                        Question question= new Question();
                        String name = (String) snapshot.child("name").getValue();
                        question.setName(name);
                        long id_question= Long.parseLong(snapshot.getKey());
                        question.setId(id_question);
                        String imagePath = (String) snapshot.child("image").getValue();
                        if(imagePath!=null){
                            mStorage.child(imagePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    question.setUploadImageUri(uri);
                                }
                            });
                        }

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
                        keys.add(snapshot.getKey());
                        questionDTOList.add(new QuestionDTO(question, optionToOneQuestionDTO));

                    }
                    dataStatus.DataIsLoaded(questionDTOList, keys);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addQuestion(Question question, Context context){
        context=context;
        questionNr++;
        Context finalContext = context;
        String imageUrlName = saveToStorageImg(question);

        myRef.child(String.valueOf(questionNr)).child("name").setValue(question.getName()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(finalContext, R.string.confirm_added_question_to_database, Toast.LENGTH_LONG);
                }
            }
        });
        if(question.getUploadImageUri()!=null){
            myRef.child(String.valueOf(questionNr)).child("image").setValue(imageUrlName);
        }

        //zapisz liczbe pytań do pamięci telefonu
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("count_question", String.valueOf(questionNr));
        editor.commit();
    }

    public void addOptions(List<Option> options, String language) {
        AtomicInteger withOptionIsRight = withNrOptionsIsRight(options);

        String optionLanguage="";
        if(language.equalsIgnoreCase("PL")){
            optionLanguage="optionPL";
        }else {
            optionLanguage="optionEN";
        }
        for(int i=0; i<options.size(); i++){
            myRef.child(String.valueOf(questionNr)).child(optionLanguage).child(String.valueOf(i+1)).setValue(options.get(i).getName());
        }
        myRef.child(String.valueOf(questionNr)).child(optionLanguage).child("good_ans").setValue(Integer.parseInt(withOptionIsRight.toString()));

    }
    public void editOptions(List<Option> options, String language, int key) {
        AtomicInteger withOptionIsRight = withNrOptionsIsRight(options);

        String optionLanguage="";
        if(language.equalsIgnoreCase("PL")){
            optionLanguage="optionPL";
        }else {
            optionLanguage="optionEN";
        }
        for(int i=0; i<options.size(); i++){
            myRef.child(String.valueOf(key)).child(optionLanguage).child(String.valueOf(i+1)).setValue(options.get(i).getName());
        }
        myRef.child(String.valueOf(key)).child(optionLanguage).child("good_ans").setValue(Integer.parseInt(withOptionIsRight.toString()));

    }

    private AtomicInteger withNrOptionsIsRight(List<Option> options) {
        AtomicInteger withOptionIsRight = new AtomicInteger();
        for (Option option: options) {
            if(option.getIs_right()==1){
                //+ 1 ponieważ indeksowana lista jest od 0 a chcę ustawić od 1 opdpowiedzi aż do 3
                withOptionIsRight.set(options.indexOf(option)+1);
            }
        }
        return withOptionIsRight;
    }

    public void updateQuestionDTO(Question question_save, int key, List<Option>optionPL, List<Option>optionEN) {
        Context finalContext = context;
        String imageUrlName = saveToStorageImg(question_save);
        myRef.child(String.valueOf(key)).child("name").setValue(question_save.getName()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(finalContext, R.string.confirm_added_question_to_database, Toast.LENGTH_LONG);
                }
            }
        });
        if (question_save.getUploadImageUri() != null) {
            myRef.child(String.valueOf(key)).child("image").setValue(imageUrlName);
        }

        editOptions(optionPL, "PL", key);
        editOptions(optionPL, "EN", key);
    }

    private String saveToStorageImg(Question question_save) {
        StorageFirebase storageFirebase= new StorageFirebase();
        String imageUrl=storageFirebase.fileuploaderfromUri(question_save.getUploadImageUri(), question_save.getExtensionImg());
        return imageUrl;
    }

    public static boolean deleteIdiom(int key, Context context) {
        Context finalContext = context;
        myRef.child(String.valueOf(key)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, R.string.sucessfull_delete_idiom, Toast.LENGTH_LONG);
            }
        });
        return true;
        }


    public static List<QuestionDTO>getAllQuestionDTO(){
        return questionDTOList;
    }

    public static List<QuestionDTO> displayAllQuestion(){
        List<QuestionDTO> questionDTOS= new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dt: dataSnapshot.getChildren()){
                        List<Option>optionToOneQuestionDTO=new ArrayList<>();

                        Question question= new Question();
                        String name = (String) dt.child("name").getValue();
                        question.setName(name);
                        long id_question= Long.parseLong(dt.getKey());
                        question.setId(id_question);
                        String imagePath = (String) dt.child("image").getValue();
                        if(imagePath!=null){
                            mStorage.child(imagePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    question.setUploadImageUri(uri);
                                }
                            });
                        }



                        int rightAnswer= dt.child("optionPL").child("good_ans").getValue(Integer.class);
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
                            optionPL.setQuestion_id(Long.parseLong(dt.getKey()));
                            optionPL.setLanguage("PL");
                            optionPL.setName(dt.child("optionPL").child(String.valueOf(i+1)).getValue(String.class));

                            optionEN.setQuestion_id(Long.parseLong(dt.getKey()));
                            optionEN.setLanguage("EN");
                            optionEN.setName(dt.child("optionEN").child(String.valueOf(i+1)).getValue(String.class));

                            optionList.add(optionPL);
                            optionList.add(optionEN);
                            optionToOneQuestionDTO.add(optionPL);
                            optionToOneQuestionDTO.add(optionEN);
                        }
                        questionDTOS.add(new QuestionDTO(question, optionToOneQuestionDTO));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return questionDTOS;
    }

    public static void setFirstQuestion() {
        myRef.child(String.valueOf(1)).child("optionPL").child("good_ans").setValue(2);
    }

}
