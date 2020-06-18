package com.example.mpproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startLoading(intent);

    }

    private void startLoading(final Intent intent) {
        Toast.makeText(getBaseContext(), "DB로 부터 데이터 수신 중입니다.", Toast.LENGTH_LONG).show();
        GetDataToday.getInstance().GetMainItem();
        GetRecipe.getInstance().GetRecipeInfo();
        GetRecipe.getInstance().GetRecipeAllInfo();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            GetUserRating.getInstance().GetUserRatingData();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 4000);


    }

}
