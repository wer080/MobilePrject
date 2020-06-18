package com.example.mpproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public static final MainActivity instance = new MainActivity();

    public static MainActivity getInstance(){
        return instance;
    }


    public FragmentManager fragmentManager = getSupportFragmentManager();
    private mainlist_control frag_mainlist = new mainlist_control();
    private Frag_chart frag_chart = new Frag_chart();
    private Frag_recipes frag_recipes = new Frag_recipes();
    private Frag_Login frag_login = new Frag_Login();
    private Frag_profile frag_profile = new Frag_profile();

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, frag_mainlist).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch(menuItem.getItemId())
                {
                    case R.id.navi_home:
                        transaction.replace(R.id.frameLayout, frag_mainlist);
                        transaction.addToBackStack(null);
                        transaction.commitAllowingStateLoss();
                        break;

                    case R.id.navi_chart:
                        transaction.replace(R.id.frameLayout, frag_chart);
                        transaction.addToBackStack(null);
                        transaction.commitAllowingStateLoss();
                        break;

                    case R.id.navi_recipe:
                        transaction.replace(R.id.frameLayout, frag_recipes);
                        transaction.addToBackStack(null);
                        transaction.commitAllowingStateLoss();
                        break;

                    case R.id.navi_search:
                        if(user != null) {
                            transaction.replace(R.id.frameLayout, frag_profile);
                            transaction.addToBackStack(null);
                            transaction.commitAllowingStateLoss();
                        }else{
                            transaction.replace(R.id.frameLayout, frag_login);
                            transaction.addToBackStack(null);
                            transaction.commitAllowingStateLoss();
                        }
                        break;
                }
                return true;
            }
        });
    }
}
