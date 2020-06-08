package com.example.mpproject;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

public class
GetRecipe {

    public static final GetRecipe instance = new GetRecipe();

    public static GetRecipe getInstance(){
        return instance;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<RecipeInfo> recipeInfo = new ArrayList<RecipeInfo>();
    public List<RecipeIngredient> recipeIngredient = new ArrayList<RecipeIngredient>();
    public List<RecipeProcess> recipeProcess = new ArrayList<RecipeProcess>();


    public void GetRecipeInfo() {
        DocumentReference docRef = db.collection("recipes").document("recipe_Info");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List list = (List) document.getData().get("data");
                        for (int i = 0; i < list.size(); i++) {
                            HashMap inData = (HashMap) list.get(i);
                            recipeInfo.add(new RecipeInfo(inData.get("recipe_code").toString(), inData.get("recipe_name").toString(), inData.get("recipe_img").toString(), inData.get("recipe_info").toString()));
                        }
                    } else {
                        Log.d("Check", "No such document");
                    }
                } else {
                    Log.d("Check", "get failed with ", task.getException());
                }
            }
        });
    }

    public void GetRecipeAllInfo() {
        DocumentReference docRef = db.collection("recipes").document("recipe_Ingredient");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List list = (List) document.getData().get("data");
                        for (int i = 0; i < list.size(); i++) {
                            HashMap inData = (HashMap) list.get(i);
                            recipeIngredient.add(new RecipeIngredient(inData.get("recipe_code").toString(), inData.get("recipe_ing_name").toString(), inData.get("recipe_ing_amount").toString()));
                        }
                    } else {
                        Log.d("Check", "No such document");
                    }
                } else {
                    Log.d("Check", "get failed with ", task.getException());
                }
            }
        });

        DocumentReference docRef2 = db.collection("recipes").document("recipe_Process");
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List list = (List) document.getData().get("data");
                        for (int i = 0; i < list.size(); i++) {
                            HashMap inData = (HashMap) list.get(i);
                            recipeProcess.add(new RecipeProcess(inData.get("recipe_code").toString(), inData.get("recipe_processNumber").toString(), inData.get("recipe_process").toString(), inData.get("recipe_processImg").toString()));
                        }
                    } else {
                        Log.d("Check", "No such document");
                    }
                } else {
                    Log.d("Check", "get failed with ", task.getException());
                }
            }
        });
    }



}
