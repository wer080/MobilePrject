package com.example.mpproject;

import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class Frag_contents extends Fragment {

    FirebaseFirestore db ;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    private LinearLayout container;
    ImageView food_img;
    TextView recipe_name;
    TextView ingridentView;
    List<TextView> process_list;
    List<ImageView> process_img_list;
    RatingBar user_rating;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.recipe_process, container, false);

        food_img = (ImageView) view.findViewById(R.id.food_img);
        food_img.setImageBitmap(Recipe_contents.instance.food_img);

        recipe_name = (TextView) view.findViewById(R.id.food_name);
        recipe_name.setText(Recipe_contents.instance.recipe_nm);


        String ingredient = "";
        for(int i = 0; i < Recipe_contents.instance.ing_name.size(); i++){
            if((i+1) % 4 != 0){
                ingredient = ingredient + Recipe_contents.instance.ing_name.get(i) + "(" + Recipe_contents.instance.ing_amt.get(i) + "),    ";
            }
            else{
                ingredient = ingredient + "\n";
            }
        }

        ingridentView = (TextView) view.findViewById(R.id.food_ingredient);
        ingridentView.setText(ingredient);

        container = (LinearLayout) view.findViewById(R.id.process_contents);
        process_list = new ArrayList<TextView>();
        process_img_list = new ArrayList<ImageView>();
        for(int i = 0; i < Recipe_contents.instance.pr_num.size(); i++){
            process_list.add(new TextView(getContext()));
            if(Recipe_contents.instance.pr_img.get(i) != null){
                process_img_list.add(new ImageView(getContext()));
            }
        }

        int count = 0;

        for(int i = 0; i < process_list.size(); i++){
            String text = "<" + Recipe_contents.instance.pr_num.get(i) + ">\n"
                    + Recipe_contents.instance.pr_pr.get(i);

            process_list.get(i).setText(text);
            LinearLayout.LayoutParams params  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 10;
            params.bottomMargin = 10;
            container.addView(process_list.get(i), params);

            if(Recipe_contents.instance.pr_img.get(i) != null){
                process_img_list.get(count).setImageBitmap(Recipe_contents.instance.pr_img.get(i));
                process_img_list.get(count).setScaleType(ImageView.ScaleType.FIT_CENTER);
                LinearLayout.LayoutParams params2  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params2.width = 1000;
                params2.height = 400;
                params2.gravity = Gravity.CENTER;
                process_img_list.get(count).setLayoutParams(params2);

                container.addView(process_img_list.get(count));
                count++;
            }
        }

        user_rating = (RatingBar) view.findViewById(R.id.user_rating);
        user_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                user_rating.setRating(rating);
                firebaseAuth = FirebaseAuth.getInstance();
                user = firebaseAuth.getCurrentUser();

                if(user != null) {
                    db = FirebaseFirestore.getInstance();
                    String uid = user.getUid();
                    String recipe_nm_string = "";
                    for (int i = 0; i < GetRecipe.getInstance().recipeInfo.size(); i++) {
                        if (GetRecipe.getInstance().recipeInfo.get(i).GetName().equals(recipe_name.getText())) {
                            recipe_nm_string = recipe_name.getText().toString();
                        }
                    }

                    String user_rating = "" + rating;

                    Map<String, Object> ratingData = new HashMap<>();
                    ratingData.put("rating", user_rating);

                    db.collection("user").document("rating").collection(uid).document(recipe_nm_string).set(ratingData);
                } else {
                    Toast.makeText(getContext(), "로그인 후 이용해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }
}
