package com.example.mpproject;

import android.util.Log;
import android.widget.Switch;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InFieldData{
    private List<String> examinDate = new ArrayList<String>();
    private List<String> grade = new ArrayList<String>();
    private List<Double> price = new ArrayList<Double>();
    private List<String> product_name = new ArrayList<String>();
    private List<String> species = new ArrayList<String>();
    private List<String> unit = new ArrayList<String>();
    private List<Double> changeRate = new ArrayList<Double>();

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
    public void SetChangeRate(double val){
        changeRate.add(val);
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
    public List<Double> GetChangeRate(){
        return changeRate;
    }

}

public class GetData{
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ReturnData rd;

    public interface ReturnData {
        void receiveData(InFieldData d);
    }

    public void SetOnCB(ReturnData callback){
        rd = callback;
    }


    public void GetMainItem(int option) {
        if (option == 0) {
            DocumentReference docRef = db.collection("best_item_yesterday").document("yd_best");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List list = (List) document.getData().get("data");
                            InFieldData data = new InFieldData();
                            for (int i = 0; i < list.size(); i++) {
                                HashMap inData = (HashMap) list.get(i);
                                data.SetExaminDate(inData.get("examin_date").toString());
                                data.SetChangeRate((double) inData.get("vsYD"));
                                data.SetGrade(inData.get("grade").toString());
                                data.SetPrice((double) inData.get("price"));
                                data.SetProductName(inData.get("product_name").toString());
                                data.SetSpecies(inData.get("species").toString());
                                data.SetUnit(inData.get("unit").toString());
                            }
                            rd.receiveData(data);
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
}
