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
    private List<String> recipe_code = new ArrayList<String>();
    private List<String> recipe_name = new ArrayList<String>();
    private List<String> recipe_img = new ArrayList<String>();
    private List<String> recipe_info = new ArrayList<String>();
    public void SetCode(String code) {this.recipe_code.add(code);}
    public void SetImg(String img) {this.recipe_img.add(img);}
    public void Setname(String name) {this.recipe_name.add(name);}
    public void Setinfo(String info) {this.recipe_info.add(info);}
    public List<String> GetCode(){return recipe_code;}
    public List<String> GetImg(){return recipe_img;}
    public List<String> GetName(){return recipe_name;}
    public List<String> GetInfo(){return recipe_info;}
}

class RecipeIngredient{
    private List<String> recipe_code = new ArrayList<String>();
    private List<String> recipe_ingName = new ArrayList<String>();
    private List<String> recipe_ingAmt = new ArrayList<String>();
    public void SetCode(String code) {this.recipe_code.add(code);}
    public void SetingName(String ingName) {this.recipe_ingName.add(ingName);}
    public void SetingAmt(String ingAmt) {this.recipe_ingAmt.add(ingAmt);}
    public List<String> GetCode(){return recipe_code;}
    public List<String> GetingName(){return recipe_ingName;}
    public List<String> GetingAmt(){return recipe_ingAmt;}
}

class RecipeProcess{
    private List<String> recipe_code = new ArrayList<String>();
    private List<String> recipe_processNum = new ArrayList<String>();
    private List<String> recipe_process = new ArrayList<String>();
    private List<String> recipe_processImg = new ArrayList<String>();
    public void SetCode(String code) {this.recipe_code.add(code);}
    public void SetprocessNum(String pNum) {this.recipe_processNum.add(pNum);}
    public void Setprocess(String p) {this.recipe_process.add(p);}
    public void SetprocessImg(String pImg) {this.recipe_processImg.add(pImg);}
    public List<String> GetCode(){return recipe_code;}
    public List<String> GetPNum(){return recipe_processNum;}
    public List<String> GetProcess(){return recipe_process;}
    public List<String> GetPImg(){return recipe_processImg;}
}

public class GetRecipe {

    public static final GetRecipe instance = new GetRecipe();

    public static GetRecipe getInstance(){
        return instance;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public RecipeInfo recipeInfo = new RecipeInfo();
    public RecipeIngredient recipeIngredient = new RecipeIngredient();
    public RecipeProcess recipeProcess = new RecipeProcess();


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
                            recipeInfo.SetCode(inData.get("recipe_code").toString());
                            recipeInfo.SetImg(inData.get("recipe_img").toString());
                            recipeInfo.Setinfo(inData.get("recipe_info").toString());
                            recipeInfo.Setname(inData.get("recipe_name").toString());
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
                            recipeIngredient.SetCode(inData.get("recipe_code").toString());
                            recipeIngredient.SetingName(inData.get("recipe_ing_name").toString());
                            recipeIngredient.SetingAmt(inData.get("recipe_ing_amount").toString());
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
                            recipeProcess.SetCode(inData.get("recipe_code").toString());
                            recipeProcess.Setprocess(inData.get("recipe_process").toString());
                            recipeProcess.SetprocessNum(inData.get("recipe_processNumber").toString());
                            recipeProcess.SetprocessImg(inData.get("recipe_processImg").toString());
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
