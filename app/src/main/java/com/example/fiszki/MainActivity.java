package com.example.fiszki;

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
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ShareActionProvider shareActionProvider;

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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareIntent("Zapraszamy na stronę uczelni");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user:
            Intent intent= new Intent(this, PanelUser.class);
            startActivity(intent);
                return true;
            case R.id.share:
                Toast.makeText(this, "Udostępnij", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ustawienia:
               // Toast.makeText(this, getAndroidVersion(), Toast.LENGTH_SHORT).show();
               // findViewById(R.id.version).setBackgroundColor(Color.parseColor("#FFFF00"));
                Intent intent2= new Intent(this, Ustawienia.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setShareIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }

    public String getAndroidVersion() {
        String relase = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + relase + ")";
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

                break;
            case R.id.help:

                break;
            case R.id.autor:
            Toast.makeText(this, "by Ewelina Kłobut", LENGTH_LONG).show();
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
