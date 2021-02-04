package com.example.fiszki;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;
import com.example.fiszki.entity.QuestionDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import xdroid.toaster.Toaster;

public final class FirebaseConfiguration {

      static FirebaseDatabase database = FirebaseDatabase.getInstance();
      static long questionNr=10;
      static DatabaseReference myRef = database.getReference().child("question");
      static List<Option> optionList=new ArrayList<>();;
      static List<QuestionDTO> questionDTOList=new ArrayList<>();;
      static List<Integer>keys;

    public interface DataStatus{
        void DataIsLoaded(List<QuestionDTO>questionDTOList, List<Integer>keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public static void readAllQuestions(final DataStatus dataStatus) {
        //ustaw nasłuchacz
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    questionDTOList.clear();

                    keys=new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        List<Option>optionToOneQuestionDTO=new ArrayList<>();

                        Question question= new Question();
                        String name = (String) snapshot.child("name").getValue();
                        question.setName(name);
                        long id_question= Long.parseLong(snapshot.getKey());
                        question.setId(id_question);
                        String imagePath = (String) snapshot.child("image").getValue();
                        if(imagePath!=null){
                            StorageFirebase.getmStorageRef().child(imagePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                        keys.add(Integer.valueOf(snapshot.getKey()));

                        questionDTOList.add(new QuestionDTO(question, optionToOneQuestionDTO));

                    }
                    //ustaw najwyższy key Jako index
                    Collections.sort(keys);
                    questionNr= keys.get(keys.size()-1);

                    dataStatus.DataIsLoaded(questionDTOList, keys);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static List<Integer> getKeys() {
        return keys;
    }

    public static void addQuestion(Question question){
        questionNr++;
        String imageUrlName = saveToStorageImg(question);

        myRef.child(String.valueOf(questionNr)).child("name").setValue(question.getName()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toaster.toast(R.string.confirm_added_question_to_database);
                }
            }
        });
        if(question.getUploadImageUri()!=null){
            myRef.child(String.valueOf(questionNr)).child("image").setValue(imageUrlName);
        }

    }

    public static void addOptions(List<Option> options, String language) {
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
    public static void editOptions(List<Option> options, String language, int key) {
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

    private static AtomicInteger withNrOptionsIsRight(List<Option> options) {
        AtomicInteger withOptionIsRight = new AtomicInteger();
        for (Option option: options) {
            if(option.getIs_right()==1){
                //+ 1 ponieważ indeksowana lista jest od 0 a chcę ustawić od 1 opdpowiedzi aż do 3
                withOptionIsRight.set(options.indexOf(option)+1);
            }
        }
        return withOptionIsRight;
    }

    public static void updateQuestionDTO(Question question_save, int key, List<Option> optionPL, List<Option> optionEN) {
        String imageUrlName = saveToStorageImg(question_save);
        myRef.child(String.valueOf(key)).child("name").setValue(question_save.getName()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toaster.toast(R.string.confirm_added_question_to_database);
                }
            }
        });
        if (question_save.getUploadImageUri() != null) {
            myRef.child(String.valueOf(key)).child("image").setValue(imageUrlName);
        }

        editOptions(optionPL, "PL", key);
        editOptions(optionPL, "EN", key);
    }

    private static String saveToStorageImg(Question question_save) {
        String imageUrl=StorageFirebase.fileuploaderfromUri(question_save.getUploadImageUri(), question_save.getExtensionImg());
        return imageUrl;
    }

    public static boolean deleteIdiom(int key) {
        myRef.child(String.valueOf(key)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toaster.toast(R.string.sucessfull_delete_idiom);
            }
        });
        return true;
        }

    public static List<QuestionDTO>getAllQuestionDTO(){
        return questionDTOList;
    }


}
