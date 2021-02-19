package com.example.fiszki.activityPanel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.fiszki.FirebaseConfiguration;
import com.example.fiszki.R;
import com.example.fiszki.activityPanel.ui.login.LoginActivity;
import com.example.fiszki.entity.QuestionDTO;
import com.example.fiszki.services.QuizService;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ikona
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        int image = R.drawable.idiom;
        imageView.setImageResource(image);

        //menu boczne
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //załąduj pytanai do bazy
        if(FirebaseConfiguration.getAllQuestionDTO().isEmpty()){
            FirebaseConfiguration.readAllQuestions(new FirebaseConfiguration.DataStatus() {
                @Override
                public void DataIsLoaded(List<QuestionDTO> questionDTOList, List<Integer> keys) {
                    //QuizService.setQuestionDTOList(questionDTOList);
                }

                @Override
                public void DataIsInserted() {

                }

                @Override
                public void DataIsUpdated() {

                }

                @Override
                public void DataIsDeleted() {

                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

          return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ustawienia:
                Intent intent2= new Intent(this, Ustawienia.class);
                startActivity(intent2);
                return true;
            case R.id.admin:
                Intent intent3=new Intent(MainActivity.this, AdminPanel.class);
                startActivity(intent3);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.quiz:
                Intent intent= new Intent(this, StartPageQuiz.class);
                startActivity(intent);
                break;
            case R.id.statistic:
                Intent intent1= new Intent(this, Statystic.class);
                startActivity(intent1);
                break;
            case R.id.repeat:
                Intent intent2= new Intent(this, RepeatBoard.class);
                startActivity(intent2);
                break;

            default:
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//przycisk wstecz
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
