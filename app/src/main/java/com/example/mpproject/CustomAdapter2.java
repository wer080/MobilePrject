package com.example.mpproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.CustomViewHolder> {

    public int recipeNum;
    RecipeInfo recipeInfo;
    RecipeIngredient recipeIngredient;
    RecipeProcess recipeProcess;
    Bitmap bm;


    public CustomAdapter2(RecipeInfo ri, RecipeIngredient rig, RecipeProcess rp) {
        this.recipeInfo = ri;
        this.recipeIngredient = rig;
        this.recipeProcess = rp;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        public RoundedImageView img;
        public TextView recipe_name;
        public TextView recipe_info;
        public TextView recipe_number;

        public CustomViewHolder(View view){
            super(view);

            this.img = (RoundedImageView) view.findViewById(R.id.recipe_img);
            this.recipe_name = (TextView) view.findViewById(R.id.recipe_name);
            this.recipe_info = (TextView) view.findViewById(R.id.recipe_info);
            this.recipe_number = (TextView) view.findViewById(R.id.recipe_id);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(recipe_number.getText().toString());
                    Recipe_contents.instance.InitializeList();
                    Recipe_contents.instance.Extract_Title(position);
                    Recipe_contents.instance.Extract_Ingredient(position);
                    Recipe_contents.instance.Extract_Process(position);

                    Frag_contents frag_contents = new Frag_contents();

                    ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, frag_contents).commit();
                }
            });

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_card, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@Nonnull CustomViewHolder viewHolder, int position){
        final String imageUrl = GetRecipe.getInstance().recipeInfo.GetImg().get(position);

        Thread mThread = new Thread() {

            @Override
            public void run() {
                try{
                    URL url = new URL(imageUrl);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bm = BitmapFactory.decodeStream(is);
                } catch (Exception e){
                    //error occurred
                    Log.d("error", "image load failed");
                }
            }
        };
        mThread.start();

        try{
            mThread.join();
            viewHolder.img.setImageBitmap(bm);
            viewHolder.recipe_name.setText(GetRecipe.getInstance().recipeInfo.GetName().get(position));
            viewHolder.recipe_info.setText(GetRecipe.getInstance().recipeInfo.GetInfo().get(position));
            viewHolder.recipe_number.setText(GetRecipe.getInstance().recipeInfo.GetCode().get(position));

        } catch (InterruptedException e) {
        }

    }

    @Override
    public int getItemCount(){
        return GetRecipe.getInstance().recipeInfo.GetCode().size();
    }

}
