package com.example.fiszki.activityPanel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import static xdroid.toaster.Toaster.toast;
import static xdroid.toaster.Toaster.toastLong;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.R;
import com.example.fiszki.entity.Question;

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

import xdroid.toaster.Toaster;

public class AdminAddQuestion extends AppCompatActivity implements TextWatcher {

    private EditText questionEditT;
    Button addImageGallery;
    Button btnsaveQuestion;
    ProgressBar progressBarAT;
    ImageView imageView;
    QuizDbHelper dbHelper;

    Uri selectedImage;

    byte imgByByte[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        questionEditT = findViewById(R.id.txtQuestion);
        addImageGallery=findViewById(R.id.btnGetImageGallery);
        btnsaveQuestion = findViewById(R.id.btnAddQuestion);
        progressBarAT=findViewById(R.id.progresBar);
        imageView=findViewById(R.id.imageQuestion);
        questionEditT.addTextChangedListener(this);

        addImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                choseImageInGallery();
            }
        });
//        addImageInternet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                showDialogWindow();
//            }
//        });
        btnsaveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestion();
            }
        });

        btnsaveQuestion.setEnabled(false);

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
        }else {
            //przywracanie zmiennych po obróceniu telefonu
        }
    }

    private void loadImageInternet(String path) {
        new Pobranie().execute(path);
    }

    private void choseImageInGallery() {

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

            }
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
        Question newQuestion=new Question(questionEditT.getText().toString().trim(), selectedImage, getExtension(selectedImage));

        openNewActivity(newQuestion);
    }

    private void openNewActivity(Question saveQuestion) {
        questionEditT.getText().clear();
        imageView.setImageDrawable(null);
        Intent intent = new Intent(this, AddOption.class);
        intent.putExtra("question", (Parcelable) saveQuestion);
        startActivity(intent);
       // btnsaveQuestion.setEnabled(false);

    }

    public static byte[] getLogoImage(String path) {
        byte[] imgByByte = new byte[0];
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
            }
            imgByByte= buffer.toByteArray();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgByByte;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(questionEditT.getText().toString().isEmpty() || questionEditT.getText().toString()==null){
            questionEditT.setError(getText(R.string.nameIsEmpty));
            btnsaveQuestion.setEnabled(false);
        }else if(questionEditT.getText().toString().length()<6){
            questionEditT.setError(getText(R.string.nameIsToSmall));
            btnsaveQuestion.setEnabled(false);
        }else {
            btnsaveQuestion.setEnabled(true);
        }
    }

    public class Pobranie extends AsyncTask<String, Integer, String> {

        boolean isGoodPathImg = false;
        String path= null;
        @Override
        protected void onPreExecute() {
            progressBarAT.setMax(100);
            progressBarAT.setProgress(0);
            progressBarAT.setVisibility(View.VISIBLE);
            //addImageInternet.setEnabled(false);
            btnsaveQuestion.setEnabled(false);
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings) {
            File new_folder;
            path = strings[0];
            int file_length = 0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();
                if (Build.VERSION.SDK_INT > 23) {
                    String path2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator;
                    new_folder = new File(path2, "archiwum");
                    Log.v("Wersja > 23. Jestem i tworze archiwum", String.valueOf(new_folder));
                } else {
                    new_folder = new File("sdcard/archiwum/");
                    Log.v("Wersja >23. Jestem i tworze archiwum", String.valueOf(new_folder));
                }
                if (!new_folder.exists()) {
                    new_folder.mkdirs();
                    Log.v("Folder nie istnieje ale go tworze", String.valueOf(new_folder));
                }
                File input_file = new File(new_folder, "przyroda.jpg");
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;

                OutputStream outputStream = new FileOutputStream(input_file);
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    outputStream.write(data, 0, count);
                    int progress = (int) total * 100 / file_length;
                    publishProgress(progress);
                }
                inputStream.close();
                outputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                setToastWrongPath();
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

            if(Build.VERSION.SDK_INT > 23){
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "/archiwum/przyroda.jpg";
                Log.v("Jestem w folderze", String.valueOf(path));
            } else {
                path = "sdcard/archiwum/przyroda.jpg";
            }
            imageView.setImageDrawable(Drawable.createFromPath(path));

            // imageView.setImageDrawable(Drawable.createFromPath(path));
            //addImageInternet.setEnabled(true);
            btnsaveQuestion.setEnabled(true);
        }

        private void setImage() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bmp = BitmapFactory.decodeByteArray(imgByByte, 0, imgByByte.length);
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageView.getWidth(), imageView.getHeight(), false));
                }
            });
        }

        private void setToastWrongPath() {
            Thread thread = new Thread(){
                public void run(){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AdminAddQuestion.this, "Finco is Daddy", Toast.LENGTH_LONG);
                        }
                    });
                }
            };
            thread.start();
        }

    }
}
