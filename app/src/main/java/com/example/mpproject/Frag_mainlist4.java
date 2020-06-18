package com.example.mpproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;


public class Frag_mainlist4 extends Fragment{

    int option;

    public Frag_mainlist4(int opt){
        this.option = opt;
    }

    Bitmap bm;

    GetData db = new GetData();
    InFieldData data_get = new InFieldData();
    CardView cardView;

    public void updateData (int opt){
        this.db = new GetData();
        this.data_get = new InFieldData();
        GetData.ReturnData callBack = new GetData.ReturnData() {
            @Override
            public void receiveData(InFieldData d) {
                SetDataField(d);
                FilledData(d);
            }
        };
        db.SetOnCB(callBack);
        db.GetMainItem(opt);
    }

    public void SetDataField(InFieldData d){this.data_get = d;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_mainlist4,container,false);

        GetData.ReturnData callBack = new GetData.ReturnData() {
            @Override
            public void receiveData(InFieldData d) {
                SetDataField(d);
                FilledData(d);
            }
        };
        db.SetOnCB(callBack);
        db.GetMainItem(option);


        cardView = view.findViewById(R.id.main_cardView4);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView main_card_id = (TextView) getView().findViewById(R.id.main_recipe_id4);
                String position = main_card_id.getText().toString();

                Recipe_contents.getInstance().InitializeList();
                Recipe_contents.getInstance().Extract_Title(position);
                Recipe_contents.getInstance().Extract_Ingredient(position);
                Recipe_contents.getInstance().Extract_Process(position);

                Frag_contents frag_contents = new Frag_contents();

                ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, frag_contents).addToBackStack(null).commit();
            }
        });
        return view;
    }

    void FilledData(InFieldData d) {
        String product_name = data_get.GetProductName().get(3);
        String product_cat = data_get.GetSpecies().get(3);
        String product_grade = data_get.GetGrade().get(3);
        String product_unit = data_get.GetUnit().get(3);
        double product_price = data_get.GetPrice().get(3);
        double product_vs = data_get.GetChangeRate().get(3);
        String string_vs = String.format("%.2f %%", product_vs);
        String product_date = data_get.GetExaminDate().get(3);


        TextView product_name_txt = (TextView) getView().findViewById(R.id.product_name4);
        product_name_txt.setText(product_name);

        TextView product_cat_txt = (TextView) getView().findViewById(R.id.product_cat4);
        product_cat_txt.setText(product_cat);

        TextView product_grade_txt = (TextView) getView().findViewById(R.id.product_grade4);
        product_grade_txt.setText(product_grade);

        TextView product_unit_txt = (TextView) getView().findViewById(R.id.product_unit4);
        product_unit_txt.setText(product_unit);

        TextView product_price_txt = (TextView) getView().findViewById(R.id.product_price4);
        product_price_txt.setText(Double.toString(product_price));

        TextView product_vs_txt = (TextView) getView().findViewById(R.id.product_vs4);
        product_vs_txt.setText(string_vs);

        TextView product_date_txt = (TextView) getView().findViewById(R.id.product_date4);
        product_date_txt.setText(product_date);


        List<String> recipe_code = new ArrayList<String>();
        Log.d("check", ""+GetRecipe.getInstance().recipeIngredient.size());
        for (int i = 0; i < GetRecipe.getInstance().recipeIngredient.size(); i++) {
            if (product_name.contains(GetRecipe.getInstance().recipeIngredient.get(i).GetingName()) || product_cat.contains(GetRecipe.getInstance().recipeIngredient.get(i).GetingName())) {
                recipe_code.add(GetRecipe.getInstance().recipeIngredient.get(i).GetCode());
            }
        }
        Log.d("check", ""+recipe_code.size());

        Random rand = new Random();
        if (recipe_code.size() > 0) {

            int randNum = rand.nextInt(recipe_code.size());
            String selected_code = recipe_code.get(randNum);
            int index = -1;
            for (int i = 0; i < GetRecipe.getInstance().recipeInfo.size(); i++) {
                if (GetRecipe.getInstance().recipeInfo.get(i).GetCode().equals(selected_code)) {
                    index = i;
                }
            }


            if (index > 0) {
                final String img_src = GetRecipe.getInstance().recipeInfo.get(index).GetImg();

                Thread mThread = new Thread() {

                    @Override
                    public void run() {
                        try {
                            URL url = new URL(img_src);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();

                            InputStream is = conn.getInputStream();
                            bm = BitmapFactory.decodeStream(is);
                        } catch (Exception e) {
                            //error occurred
                            Log.d("error", "image load failed");
                        }
                    }
                };
                mThread.start();
                try {
                    mThread.join();
                    TextView main_card_name = (TextView) getView().findViewById(R.id.main_recipe_name4);
                    main_card_name.setText(GetRecipe.getInstance().recipeInfo.get(index).GetName());

                    TextView main_card_id = (TextView) getView().findViewById(R.id.main_recipe_id4);
                    main_card_id.setText(GetRecipe.getInstance().recipeInfo.get(index).GetCode());

                    TextView main_card_info = (TextView) getView().findViewById(R.id.main_recipe_info4);
                    main_card_info.setText(GetRecipe.getInstance().recipeInfo.get(index).GetInfo());

                    ImageView main_card_iv = (ImageView) getView().findViewById(R.id.main_recipe_img4);
                    main_card_iv.setImageBitmap(bm);

                } catch (InterruptedException e) {
                }
            }
        }
    }


}
