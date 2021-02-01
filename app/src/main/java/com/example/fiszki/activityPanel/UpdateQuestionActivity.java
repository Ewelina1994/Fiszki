package com.example.fiszki.activityPanel;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fiszki.QuestionDTO;
import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.R;
import com.example.fiszki.StorageFirebase;
import com.example.fiszki.entity.Question;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UpdateQuestionActivity extends AppCompatActivity {
    private EditText questionEditT;
    Button addImageGallery;
    Button btnsaveQuestion;
    ProgressBar progressBarAT;
    ImageView imageView;
    QuizDbHelper dbHelper;

    Uri selectedImage;
    byte imgByByte[];

    QuestionDTO question_save;
    int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        questionEditT = findViewById(R.id.txtQuestion);
        addImageGallery=findViewById(R.id.btnGetImageGallery);
        btnsaveQuestion = findViewById(R.id.btnAddQuestion);
        progressBarAT=findViewById(R.id.progresBar);
        imageView=findViewById(R.id.imageQuestion);

        addImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                choseImageInGallery();
            }
        });

        btnsaveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestion();
            }
        });

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }

        if (savedInstanceState == null) {
            dbHelper = new QuizDbHelper(this);
            question_save=getIntent().getParcelableExtra("question");
            key = getIntent().getIntExtra("key", -1);
            setFieldsUpdate();

        }else {
            //przywracanie zmiennych po obróceniu telefonu
        }
    }

    private void setFieldsUpdate() {
        questionEditT.setText(question_save.getQuestion().getName());
        Uri uriImage=question_save.getQuestion().getUploadImageUri();
        if(uriImage!=null){
            Picasso.get().load(uriImage).into(imageView);
        }
        TextView textView = findViewById(R.id.txtAddQuestion);
        textView.setText(R.string.updateQuestion);
    }


//    private void showDialogWindow() {
//        View view = View.inflate(this, R.layout.path_internet_img_window, null);
//        final Dialog dialog = new Dialog(this);
//        dialog.setContentView(view);
//        //dialog.setTitle("Title");
//        TextView text = (TextView) dialog.findViewById(R.id.pathImgTV);
//
//        EditText pathEditText = (EditText) dialog.findViewById(R.id.pathImgET);
//
//        Button addPathBTN=(Button) dialog.findViewById(R.id.addPathImgBTN);
//        addPathBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String path=pathEditText.getText().toString().trim();
//                String treeLatesLetter=path.substring(path.length()-3);
//                if(treeLatesLetter.equals("jpg")||treeLatesLetter.equals("png")||treeLatesLetter.equals("gif")){
//                    loadImageInternet(path);
//                    dialog.cancel();
//                }else {
//                    //nie działa :(
//                    //Toast toast = Toast.makeText(this, R.string.validationPath, Toast.LENGTH_LONG);
//                }
//            }
//        });
//        dialog.show();
//    }

//    private void loadImageInternet(String path) {
//        new UpdateQuestionActivity.Pobranie().execute(path);
//    }

    private void choseImageInGallery() {
        // Intent intent = new Intent(Intent.ACTION_PICK,  MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // startActivityForResult(intent,0);
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImage = data.getData();
            progressBarAT.setVisibility(View.INVISIBLE);

            imageView.setImageURI(selectedImage);
            deleteOldImageInFirebase();

        }
    }

    private void deleteOldImageInFirebase() {
        Uri deleteImage = question_save.getQuestion().getUploadImageUri();
        StorageFirebase.deleteImage(deleteImage);
    }

    private String getExtension(Uri uri){
        if (uri != null) {

            ContentResolver contentResolver= getContentResolver();
            MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        }else {
            return "";
        }

    }

    private void saveQuestion(){
        question_save.getQuestion().setName(questionEditT.getText().toString().trim());
        question_save.getQuestion().setUploadImageUri(selectedImage);
        question_save.getQuestion().setExtensionImg(getExtension(selectedImage));

        openNewActivity();
    }

    private void openNewActivity() {
        //questionEditT.getText().clear();
       // imageView.setImageDrawable(null);
        Intent intent = new Intent(this, UpdateOptionsActivity.class);
        intent.putExtra("questionDTO", (Parcelable) question_save);
        intent.putExtra("key",  key);
        startActivity(intent);
        // btnsaveQuestion.setEnabled(false);

    }
}
