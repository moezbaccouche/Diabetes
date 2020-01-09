package com.example.diabetes.views;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.diabetes.R;
import com.example.diabetes.WekaMain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;

import weka.classifiers.trees.J48;

public class MainActivity extends AppCompatActivity {

    static J48 tree = null;
    final Fragment mTreeFragment = new TreeFragment();
    final Fragment mNewTestFragment = new NewTestFragment();
    final Fragment mNotificationsFragment = new NotificationsFragment();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = mTreeFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(mTreeFragment).commit();
                    active = mTreeFragment;
                    return true;
                case R.id.navigation_dashboard:
                    fm.beginTransaction().hide(active).show(mNewTestFragment).commit();
                    active = mNewTestFragment;
                    return true;
                case R.id.navigation_notifications:
                    fm.beginTransaction().hide(active).show(mNotificationsFragment).commit();
                    active = mNotificationsFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File file = new File("data/data/com.example.diabetes/diab.arff");
        if(!file.exists())
        {
            try {
                file.createNewFile();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        try {
            InputStream is = getAssets().open("file_arff.arff");
            OutputStream os = new FileOutputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            while((size = is.read(buffer)) > 0)
            {
                os.write(buffer, 0, size);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        tree = WekaMain.loadDecisionTree("data/data/com.example.diabetes/diab.arff");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fm.beginTransaction().add(R.id.main_container, mNotificationsFragment, "3").hide(mNotificationsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, mNewTestFragment, "2").hide(mNewTestFragment).commit();
        fm.beginTransaction().add(R.id.main_container, mTreeFragment, "1").commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }



}
