package com.example.mpproject;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Recipe_contents {

    String recipe_nm;
    Bitmap food_img;
    List<String> ing_name = new ArrayList<String>();
    List<String> ing_amt = new ArrayList<String>();
    List<String> pr_pr = new ArrayList<String>();
    List<String> pr_num = new ArrayList<String>();
    List<Bitmap> pr_img = new ArrayList<Bitmap>();
    Bitmap temp;

    public static Recipe_contents instance = new Recipe_contents();


    public Recipe_contents GetInstance(){
        return instance;
    }

    public void InitializeList(){
        this.ing_name.clear();
        this.ing_amt.clear();
        this.pr_pr.clear();
        this.pr_num.clear();
        this.pr_img.clear();
    }

    public void Extract_Process(int position){
        for(int i = 0; i < GetRecipe.getInstance().recipeProcess.size(); i++){
            if(Integer.parseInt(GetRecipe.getInstance().recipeProcess.get(i).GetCode()) == position)
            {
                final String imageUrl = GetRecipe.getInstance().recipeProcess.get(i).GetPImg();
                System.out.println(imageUrl);
                if(imageUrl != null) {
                    Thread mThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(imageUrl);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setDoInput(true);
                                conn.connect();

                                InputStream is = conn.getInputStream();
                                temp = BitmapFactory.decodeStream(is);
                            } catch (Exception e) {
                                //error occurred
                                temp = null;
                                Log.d("error", "process image load failed");
                            }
                        }
                    };
                    mThread.start();
                    try {
                        mThread.join();
                        pr_pr.add(GetRecipe.getInstance().recipeProcess.get(i).GetProcess());
                        pr_num.add(GetRecipe.getInstance().recipeProcess.get(i).GetPNum());
                        pr_img.add(temp);
                    } catch (InterruptedException e) {
                    }
                } else{
                    pr_pr.add(GetRecipe.getInstance().recipeProcess.get(i).GetProcess());
                    pr_num.add(GetRecipe.getInstance().recipeProcess.get(i).GetPNum());
                }
            }
        }
    }

    public void Extract_Title(int position){
        for(int i = 0; i < GetRecipe.getInstance().recipeInfo.size(); i++)
        {
            if(Integer.parseInt(GetRecipe.getInstance().recipeInfo.get(i).GetCode()) == position)
            {
                final String imageUrl = GetRecipe.getInstance().recipeInfo.get(i).GetImg();
                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        try{
                            URL url = new URL(imageUrl);
                            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();

                            InputStream is = conn.getInputStream();
                            food_img = BitmapFactory.decodeStream(is);
                        } catch (Exception e){
                            //error occurred
                            Log.d("error", "recipe image load failed");
                        }
                    }
                };

                mThread.start();

                try{
                    mThread.join();
                    recipe_nm = GetRecipe.getInstance().recipeInfo.get(i).GetName();
                } catch (InterruptedException e) {
                }

                break;
            }
        }
    }

    public void Extract_Ingredient(int position){
        for(int i = 0; i < GetRecipe.getInstance().recipeIngredient.size(); i++)
        {
            if(Integer.parseInt(GetRecipe.getInstance().recipeIngredient.get(i).GetCode()) == position) {
                ing_name.add(GetRecipe.getInstance().recipeIngredient.get(i).GetingName());
                ing_amt.add(GetRecipe.getInstance().recipeIngredient.get(i).GetingAmt());
            }
        }
    }
}
