package com.example.mpproject;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class UserRating{
    private String userRating;
    private String recipe_name;

    public UserRating(String name, String rating){
        this.userRating = rating;
        this.recipe_name = name;
    }
    public void SetRate(String rate) {this.userRating = rate;}
    public void SetName(String name) {this.recipe_name = name;}
    public String GetRate(){return userRating;}
    public String GetName(){return recipe_name;}
}

public class GetUserRating {

    public static final GetUserRating instance = new GetUserRating();

    public static GetUserRating getInstance(){
        return instance;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<UserRating> userRatingList = new ArrayList<UserRating>();


    public void GetUserRatingData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();
        this.userRatingList.clear();

        db.collection("user").document("rating").collection(uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.isSuccessful()) {
                        for(DocumentSnapshot doc : task.getResult()) {
                            String rc_code = doc.getId();
                            String rating = doc.get("rating").toString();
                            userRatingList.add(new UserRating(rc_code, rating));
                        }
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });
    }
}
