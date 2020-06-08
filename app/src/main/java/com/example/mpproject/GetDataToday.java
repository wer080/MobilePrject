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
import java.util.zip.Inflater;

class InFieldTodayData{

    private String examinDate;
    private String grade;
    private double price;
    private String product_name;
    private String species;
    private String unit;

    public InFieldTodayData(String ed, String gd, double pr, String pn, String sp, String un){
        this.examinDate = ed;
        this.grade = gd;
        this.price = pr;
        this.product_name = pn;
        this.species = sp;
        this.unit = un;
    }

    public void SetExaminDate(String val){
        examinDate = val;
    }
    public void SetGrade(String val){
        grade = val;
    }
    public void SetProductName(String val){
        product_name = val;
    }
    public void SetSpecies(String val){
        species = val;
    }
    public void SetUnit(String val){
        unit = val;
    }
    public void SetPrice(double val){
        price = val;
    }

    public String GetExaminDate(){
        return examinDate;
    }
    public String GetGrade(){
        return grade;
    }
    public String GetProductName(){
        return product_name;
    }
    public String GetSpecies(){
        return species;
    }
    public String GetUnit(){
        return unit;
    }
    public double GetPrice(){
        return price;
    }

}


public class GetDataToday {

    public static final GetDataToday instance = new GetDataToday();

    public static GetDataToday getInstance(){
        return instance;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<InFieldTodayData> todayData = new ArrayList<InFieldTodayData>();

    public void GetMainItem() {
            DocumentReference docRef = db.collection("today_price_info").document("today_price");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List list = (List) document.getData().get("data");
                            for (int i = 0; i < list.size(); i++) {
                                HashMap inData = (HashMap) list.get(i);
                                todayData.add(new InFieldTodayData(inData.get("examin_date").toString(), inData.get("grade").toString(), (double)inData.get("price"), inData.get("product_name").toString(), inData.get("species").toString(), inData.get("unit").toString()));
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
