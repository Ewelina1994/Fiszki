package com.example.fiszki;

import androidx.annotation.NonNull;

import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class FirebaseConfiguration {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static long questionNr=10;
    private DatabaseReference myRef = database.getReference().child("question");

    public FirebaseConfiguration() {

        //ustaw nasłuchacz
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                  //  questionNr=(dataSnapshot.getChildrenCount());
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

    public void getQuestions() {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
               // Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

}
