package com.example.mpproject;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class RecipeInfo {
    private String recipe_code;
    private String recipe_name;
    private String recipe_img;
    private String recipe_info;

    public RecipeInfo (String code, String name, String img, String info){
        this.recipe_code = code;
        this.recipe_name = name;
        this.recipe_img = img;
        this.recipe_info = info;
    }


    public void SetCode(String code) {this.recipe_code = code;}
    public void SetImg(String img) {this.recipe_img = img;}
    public void Setname(String name) {this.recipe_name = name;}
    public void Setinfo(String info) {this.recipe_info = info;}
    public String GetCode(){return recipe_code;}
    public String GetImg(){return recipe_img;}
    public String GetName(){return recipe_name;}
    public String GetInfo(){return recipe_info;}
}

class RecipeIngredient{
    private String recipe_code;
    private String recipe_ingName;
    private String recipe_ingAmt;

    public RecipeIngredient(String code, String ingName, String ingAmt){
        this.recipe_code = code;
        this.recipe_ingName = ingName;
        this.recipe_ingAmt = ingAmt;
    }

    public void SetCode(String code) {this.recipe_code = code;}
    public void SetingName(String ingName) {this.recipe_ingName = ingName;}
    public void SetingAmt(String ingAmt) {this.recipe_ingAmt = ingAmt;}
    public String GetCode(){return recipe_code;}
    public String GetingName(){return recipe_ingName;}
    public String GetingAmt(){return recipe_ingAmt;}
}

class RecipeProcess{
    private String recipe_code;
    private String recipe_processNum;
    private String recipe_process;
    private String recipe_processImg;

    public RecipeProcess(String code, String nm, String pc, String pci){
        this.recipe_code = code;
        this.recipe_processNum = nm;
        this.recipe_process = pc;
        this.recipe_processImg = pci;
    }

    public void SetCode(String code) {this.recipe_code = code;}
    public void SetprocessNum(String pNum) {this.recipe_processNum = pNum;}
    public void Setprocess(String p) {this.recipe_process = p;}
    public void SetprocessImg(String pImg) {this.recipe_processImg = pImg;}
    public String GetCode(){return recipe_code;}
    public String GetPNum(){return recipe_processNum;}
    public String GetProcess(){return recipe_process;}
    public String GetPImg(){return recipe_processImg;}
}

public class GetRecipe {

    public static final GetRecipe instance = new GetRecipe();

    public static GetRecipe getInstance(){
        return instance;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<RecipeInfo> recipeInfo = new ArrayList<RecipeInfo>();
    public List<RecipeIngredient> recipeIngredient = new ArrayList<RecipeIngredient>();
    public List<RecipeProcess> recipeProcess = new ArrayList<RecipeProcess>();


    public void GetRecipeInfo() {

        db.collection("recipes_info").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.isSuccessful()) {
                        for(DocumentSnapshot doc : task.getResult()) {
                            String recipe_info_code = doc.get("recipe_code").toString();
                            String recipe_info_name = doc.get("recipe_name").toString();
                            String recipe_info_info = doc.get("recipe_info").toString();
                            String recipe_info_img = doc.get("recipe_img").toString();

                            recipeInfo.add(new RecipeInfo(recipe_info_code, recipe_info_name, recipe_info_img, recipe_info_info));
                        }
                    } else {
                        Log.d("Check", "No such document");
                    }
                 }
            }
        });
    }

    public void GetRecipeAllInfo() {
        db.collection("recipes_ing").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.isSuccessful()) {
                        for(DocumentSnapshot doc : task.getResult()) {
                            String recipe_ing_code = doc.get("recipe_code").toString();
                            String recipe_ing_name = doc.get("recipe_ing_name").toString();
                            String recipe_ing_amt = doc.get("recipe_ing_amount").toString();
                            recipeIngredient.add(new RecipeIngredient(recipe_ing_code, recipe_ing_name, recipe_ing_amt));
                        }
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });

        db.collection("recipes_pr").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.isSuccessful()) {
                        for(DocumentSnapshot doc : task.getResult()) {
                            String recipe_pr_code = doc.get("recipe_code").toString();
                            String recipe_pr_pr = doc.get("recipe_pr").toString();
                            String recipe_pr_img= doc.get("recipe_pr_img").toString();
                            String recipe_pr_num = doc.get("recipe_pr_num").toString();

                            recipeProcess.add(new RecipeProcess(recipe_pr_code, recipe_pr_num, recipe_pr_pr, recipe_pr_img));
                        }
                    } else {
                        Log.d("Check", "No such document");
                    }
                }
            }
        });
    }



}
