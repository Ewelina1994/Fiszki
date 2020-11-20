package com.example.fiszki.activityPanel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import static xdroid.toaster.Toaster.toast;
import static xdroid.toaster.Toaster.toastLong;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.R;
import com.example.fiszki.entity.Question;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AdminAddQuestion extends AppCompatActivity {
    public static final String QUIZ_NR="quiz_nr";
    private EditText questionEditT;
    Button addImageGallery;
    Button addImageInternet;
    Button btnsaveQuestion;
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
        btnsaveQuestion = findViewById(R.id.btnAddQuestion);
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

                showDialogWindow();
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
        }else {
            //przywracanie zmiennych po obróceniu telefonu
        }
    }

    private void showDialogWindow() {
        View view = View.inflate(this, R.layout.path_internet_img_window, null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        //dialog.setTitle("Title");
        TextView text = (TextView) dialog.findViewById(R.id.pathImgTV);

        EditText pathEditText = (EditText) dialog.findViewById(R.id.pathImgET);

        Button addPathBTN=(Button) dialog.findViewById(R.id.addPathImgBTN);
        addPathBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path=pathEditText.getText().toString();
                String treeLatesLetter=path.substring(path.length()-3);
                if(treeLatesLetter.equals("jpg")||treeLatesLetter.equals("png")||treeLatesLetter.equals("gif")){
                    loadImageInternet(path);
                    dialog.cancel();
                }else {
                    //nie działa :(
                    Toast.makeText(AdminAddQuestion.this, R.string.validationPath, Toast.LENGTH_LONG);
                }
            }
        });
        dialog.show();
    }

    private void loadImageInternet(String path) {
        new Pobranie().execute(path);
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

    public class Pobranie extends AsyncTask<String, Integer, String> {

        boolean isGoodPathImg = false;

        @Override
        protected void onPreExecute() {
            progressBarAT.setMax(100);
            progressBarAT.setProgress(0);
            progressBarAT.setVisibility(View.VISIBLE);
            addImageInternet.setEnabled(false);
            btnsaveQuestion.setEnabled(false);
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

                while ((count = bis.read(data, 0, data.length)) != -1) {
                    total += count;
                    buffer.write(data, 0, count);
                    int progress = (int) total * 100 / file_length;
                    publishProgress(progress);
                }
                imgByByte = buffer.toByteArray();
                is.close();
                setImage();
                isGoodPathImg = true;
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

            // imageView.setImageDrawable(Drawable.createFromPath(path));
            addImageInternet.setEnabled(true);
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
