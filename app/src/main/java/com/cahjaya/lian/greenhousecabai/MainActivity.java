package com.cahjaya.lian.greenhousecabai;


import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Receiver";
    private static final int PERMISSION_REQUEST_CODE = 200;
    private View view;
    ViewGroup flc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
           getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark)); //status bar or the time bar at the top
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        flc = (ViewGroup)findViewById(R.id.fl_container);
        // inisialisasi BottomNavigaionView
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(onNavigationItemSelected(bottomNavigationView.getMenu().findItem(R.id.home_menu))){
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_container
                            , new FirstFragment())
                    .commit();
        }
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", false);

        if (firstStart) {
            showStartDialog();
        }
        if (!checkPermission()) {
            requestPermission();
        } else {
            Log.e("permission", "already granted");
        }
    };


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getSupportFragmentManager();
        int id = item.getItemId();

        if (id == R.id.home_menu) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_container
                            , new FirstFragment())
                    .commit();

        }else if (id == R.id.grafik_menu) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_container
                            , new SecondFragment())
                    .commit();

        }else if (id == R.id.setting_menu) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_container
                            , new ThirdFragment())
                    .commit();
        }

        return true;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }
    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("One Time Dialog")
                .setMessage("This should only be shown once")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }
}
