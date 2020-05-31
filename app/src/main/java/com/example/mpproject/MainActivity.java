package com.example.mpproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity{

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private mainlist_control frag_mainlist = new mainlist_control();
    private Frag_chart frag_chart = new Frag_chart();
    private Frag_recipes frag_recipes = new Frag_recipes();
    private Frag_search frag_search = new Frag_search();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, frag_mainlist).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch(menuItem.getItemId())
                {
                    case R.id.navi_home:
                        transaction.replace(R.id.frameLayout, frag_mainlist).commitAllowingStateLoss();
                        break;

                    case R.id.navi_chart:
                        transaction.replace(R.id.frameLayout, frag_chart).commitAllowingStateLoss();
                        break;

                    case R.id.navi_recipe:
                        transaction.replace(R.id.frameLayout, frag_recipes).commitAllowingStateLoss();
                        break;

                    case R.id.navi_search:
                        transaction.replace(R.id.frameLayout, frag_search).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });



    }
}
