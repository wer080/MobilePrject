package com.example.mpproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import javax.annotation.Nullable;

public class Frag_mainlist2 extends Fragment{

    GetData db = new GetData();
    InFieldData data_get = new InFieldData();

    public void SetDataField(InFieldData d){this.data_get = d;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_mainlist2,container,false);

        GetData.ReturnData callBack = new GetData.ReturnData() {
            @Override
            public void receiveData(InFieldData d) {
                SetDataField(d);
                FilledData(d);
            }
        };
        db.SetOnCB(callBack);
        db.GetMainItem(0);

        return view;
    }

    void FilledData(InFieldData d) {
        String product_name = data_get.GetProductName().get(1);
        String product_cat = data_get.GetSpecies().get(1);
        String product_grade = data_get.GetGrade().get(1);
        String product_unit = data_get.GetUnit().get(1);
        double product_price = data_get.GetPrice().get(1);
        double product_vs = data_get.GetChangeRate().get(1);
        String string_vs = String.format("%.2f %%", product_vs);
        String product_date = data_get.GetExaminDate().get(1);


        TextView product_name_txt = (TextView) getView().findViewById(R.id.product_name2);
        product_name_txt.setText(product_name);

        TextView product_cat_txt = (TextView) getView().findViewById(R.id.product_cat2);
        product_cat_txt.setText(product_cat);

        TextView product_grade_txt = (TextView) getView().findViewById(R.id.product_grade2);
        product_grade_txt.setText(product_grade);

        TextView product_unit_txt = (TextView) getView().findViewById(R.id.product_unit2);
        product_unit_txt.setText(product_unit);

        TextView product_price_txt = (TextView) getView().findViewById(R.id.product_price2);
        product_price_txt.setText(Double.toString(product_price));

        TextView product_vs_txt = (TextView) getView().findViewById(R.id.product_vs2);
        product_vs_txt.setText(string_vs);

        TextView product_date_txt = (TextView) getView().findViewById(R.id.product_date2);
        product_date_txt.setText(product_date);
    }


}
