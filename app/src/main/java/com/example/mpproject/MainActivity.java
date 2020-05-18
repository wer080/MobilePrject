package com.example.mpproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity{
    GetData db = new GetData();
    InFieldData getdata = new InFieldData();

    public void SetDataFile(InFieldData d){
        this.getdata = d;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetData.ReturnData callback = new GetData.ReturnData() {
            @Override
            public void receiveData(InFieldData d) {
                SetDataFile(d);
                FilledData(d);
            }
        };
        db.SetOnCB(callback);
        db.GetMainItem(0);
    }
    void FilledData(InFieldData getdata){
        List<String> product_name = new ArrayList<String>();
        List<String> product_species = new ArrayList<String>();
        List<String> product_grade = new ArrayList<String>();
        List<String> product_unit = new ArrayList<String>();
        List<Double> product_price = new ArrayList<Double>();
        List<Double> product_vsYD = new ArrayList<Double>();
        List<String> product_examindate = new ArrayList<String>();

        product_name = getdata.GetProductName();
        product_species = getdata.GetSpecies();
        product_grade = getdata.GetGrade();
        product_unit = getdata.GetUnit();
        product_price = getdata.GetPrice();
        product_vsYD = getdata.GetChangeRate();
        product_examindate = getdata.GetExaminDate();

        for(int i = 0; i < 2; i++) {
            String textSID = "data_name_" + (i + 1);
            int textID = getResources().getIdentifier(textSID, "id", getPackageName());
            TextView text = (TextView) findViewById(textID);
            text.setText(product_name.get(i));

            textSID = "data_species_" + (i + 1);
            textID = getResources().getIdentifier(textSID, "id", getPackageName());
            text = (TextView) findViewById(textID);
            text.setText(product_species.get(i));

            textSID = "data_grade_" + (i + 1);
            textID = getResources().getIdentifier(textSID, "id", getPackageName());
            text = (TextView) findViewById(textID);
            text.setText(product_grade.get(i));

            textSID = "data_unit_" + (i + 1);
            textID = getResources().getIdentifier(textSID, "id", getPackageName());
            text = (TextView) findViewById(textID);
            text.setText(product_unit.get(i));

            textSID = "data_price_" + (i + 1);
            textID = getResources().getIdentifier(textSID, "id", getPackageName());
            text = (TextView) findViewById(textID);
            text.setText(product_price.get(i).toString());

            textSID = "data_vsYD_" + (i + 1);
            textID = getResources().getIdentifier(textSID, "id", getPackageName());
            text = (TextView) findViewById(textID);
            text.setText(product_vsYD.get(i).toString());

            textSID = "data_examindate_" + (i + 1);
            textID = getResources().getIdentifier(textSID, "id", getPackageName());
            text = (TextView) findViewById(textID);
            text.setText(product_examindate.get(i));

            
        }
    }
}
