package com.example.fiszki.activityPanel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.R;
import com.example.fiszki.entity.Question;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AdminAddQuestion extends AppCompatActivity {
    public static final String QUIZ_NR="quiz_nr";
    private EditText questionEditT;
    Button addImageGallery;
    Button addImageInternet;
    Button saveQuestion;
    ProgressBar progressBarAT;
    ImageView imageView;
    QuizDbHelper dbHelper;

    Bitmap bitmap = null;

    byte imgByByte[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        questionEditT = findViewById(R.id.txtQuestion);
        addImageGallery=findViewById(R.id.btnGetImageGallery);
        addImageInternet=findViewById(R.id.btnGetImageInternet);
        saveQuestion = findViewById(R.id.btnAddQuestion);
        progressBarAT=findViewById(R.id.progresBar);
        imageView=findViewById(R.id.imageQuestion);

        addImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImageGallery();
            }
        });
        addImageInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImageInternet();
            }
        });
        saveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestion();
            }
        });

        if (savedInstanceState == null) {
            dbHelper = new QuizDbHelper(this);
        }else {
            //przywracanie zmiennych po obróceniu telefonu
        }
    }

    private void loadImageInternet() {
        new Pobranie().execute("https://upload.wikimedia.org/wikipedia/commons/3/3a/Isolated_oak_at_Backley_Holmes%2C_New_Forest_-_geograph.org.uk_-_469673.jpg");

    }

    private void loadImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,  MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,0);
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);

                    imgByByte = bos.toByteArray();

                    imageView.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

    private void saveQuestion(){
        Question newQuestion=new Question(questionEditT.getText().toString(), imgByByte);
        dbHelper.addQuestion(newQuestion);
        long quiz_nr = newQuestion.getId();

        openNewActivity(quiz_nr);
    }

    private void openNewActivity(long quiz_nr) {
        questionEditT.getText().clear();
        Intent intent = new Intent(this, AddOption.class);
        intent.putExtra(QUIZ_NR, quiz_nr);
        startActivity(intent);
        saveQuestion.setEnabled(false);
    }

    private byte[] getLogoImage(String url) {
        byte[] byteImg = new byte[0];
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            //We create an array of bytes
            byte[] data = new byte[50];
            int current = 0;

            while((current = bis.read(data,0,data.length)) != -1){
                buffer.write(data,0,current);
            }

            //FileOutputStream fos = new FileOutputStream(file);
            byteImg= buffer.toByteArray();
           // fos.write(buffer.toByteArray());
          //  fos.close();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return byteImg;
    }

    public class Pobranie extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            progressBarAT.setMax(100);
            progressBarAT.setProgress(0);
            progressBarAT.setVisibility(View.VISIBLE);
            addImageInternet.setEnabled(false);
            saveQuestion.setEnabled(false);
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings) {
            String path = strings[0];
            int file_length = 0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();
                InputStream is = urlConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] data = new byte[50];
                //We create an array of bytes
                int total = 0;
                int count = 0;

                while((count = bis.read(data,0,data.length)) != -1){
                    total += count;
                    buffer.write(data,0, count);
                    int progress = (int) total * 100 / file_length;
                    publishProgress(progress);
                }
                imgByByte= buffer.toByteArray();
                is.close();
                setImage();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Pobieranie zakończone";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBarAT.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            progressBarAT.setVisibility(View.INVISIBLE);
            Log.v("Ustawiam pasek", String.valueOf(progressBarAT));

           // imageView.setImageDrawable(Drawable.createFromPath(path));
            addImageInternet.setEnabled(true);
            saveQuestion.setEnabled(true);
        }

        private void setImage(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bmp = BitmapFactory.decodeByteArray(imgByByte, 0, imgByByte.length);
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageView.getWidth(), imageView.getHeight(), false));
                }
            });
        }
    }

}
