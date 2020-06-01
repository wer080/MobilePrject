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

class InFieldTodayData{
    private List<String> examinDate = new ArrayList<String>();
    private List<String> grade = new ArrayList<String>();
    private List<Double> price = new ArrayList<Double>();
    private List<String> product_name = new ArrayList<String>();
    private List<String> species = new ArrayList<String>();
    private List<String> unit = new ArrayList<String>();

    public void SetExaminDate(String val){
        examinDate.add(val);
    }
    public void SetGrade(String val){
        grade.add(val);
    }
    public void SetProductName(String val){
        product_name.add(val);
    }
    public void SetSpecies(String val){
        species.add(val);
    }
    public void SetUnit(String val){
        unit.add(val);
    }
    public void SetPrice(double val){
        price.add(val);
    }

    public List<String> GetExaminDate(){
        return examinDate;
    }
    public List<String> GetGrade(){
        return grade;
    }
    public List<String> GetProductName(){
        return product_name;
    }
    public List<String> GetSpecies(){
        return species;
    }
    public List<String> GetUnit(){
        return unit;
    }
    public List<Double> GetPrice(){
        return price;
    }

}


public class GetDataToday {

    public static final GetDataToday instance = new GetDataToday();

    public static GetDataToday getInstance(){
        return instance;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public InFieldTodayData todayData = new InFieldTodayData();

    public void GetMainItem() {
            DocumentReference docRef = db.collection("today_price_info").document("today_price");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List list = (List) document.getData().get("data");
                            todayData = new InFieldTodayData();
                            for (int i = 0; i < list.size(); i++) {
                                HashMap inData = (HashMap) list.get(i);
                                todayData.SetExaminDate(inData.get("examin_date").toString());
                                todayData.SetGrade(inData.get("grade").toString());
                                todayData.SetPrice((double) inData.get("price"));
                                todayData.SetProductName(inData.get("product_name").toString());
                                todayData.SetSpecies(inData.get("species").toString());
                                todayData.SetUnit(inData.get("unit").toString());
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
