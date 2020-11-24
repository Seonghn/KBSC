package com.example.kbkbkb;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import com.broooapps.graphview.CurveGraphView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 오른쪽 하단 버튼 (은행 선택용) - 추후 보강
//        final AlertDialog.Builder oDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
//        FloatingActionButton fab = findViewById(R.id.entry);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final CharSequence[] oItems = {"국민", "우리", "신한", "카카오뱅크", "농협", "농협", "농협"};
//                oDialog.setTitle("은행 선택")
//                        .setItems(oItems, new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                if ("국민".equals(oItems[which])) {
//                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.youtube.com")));
//                                } else if ("우리".equals(oItems[which])) {
//                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.youtube.com")));
//                                } else if ("신한".equals(oItems[which])) {
//                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.youtube.com")));
//                                } else if ("카카오뱅크".equals(oItems[which])) {
//                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.youtube.com")));
//                                } else if ("농협".equals(oItems[which])) {
//                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.youtube.com")));
//                                }
//
//                            }
//                        })
//                        .setCancelable(false)
//                        .show();
//
//            }
//        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_sever_com, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}