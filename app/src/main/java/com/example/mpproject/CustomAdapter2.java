package com.example.mpproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.CustomViewHolder> implements Filterable {

    Context mcontext;
    public int recipeNum;
    List<RecipeInfo> recipeInfo;
    List<RecipeIngredient> recipeIngredient;
    List<RecipeProcess> recipeProcess;
    Bitmap bm;

    List<RecipeInfo> unFilteredlist = GetRecipe.getInstance().recipeInfo;
    List<RecipeInfo> filteredList;


    public CustomAdapter2(List<RecipeInfo> ri, List<RecipeIngredient> rig, List<RecipeProcess> rp, Context context) {
        this.recipeInfo = ri;
        this.recipeIngredient = rig;
        this.recipeProcess = rp;
        this.mcontext =context;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        public RoundedImageView img;
        public TextView recipe_name;
        public TextView recipe_info;
        public TextView recipe_number;

        public CustomViewHolder(View view){
            super(view);

            this.img = (RoundedImageView) view.findViewById(R.id.recipe_img);
            this.recipe_name = (TextView) view.findViewById(R.id.recipe_name);
            this.recipe_info = (TextView) view.findViewById(R.id.recipe_info);
            this.recipe_number = (TextView) view.findViewById(R.id.recipe_id);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String position = recipe_number.getText().toString();
                    Recipe_contents.getInstance().InitializeList();
                    Recipe_contents.getInstance().Extract_Title(position);
                    Recipe_contents.getInstance().Extract_Ingredient(position);
                    Recipe_contents.getInstance().Extract_Process(position);

                    Frag_contents frag_contents = new Frag_contents();

                    ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, frag_contents).addToBackStack(null).commit();
                }
            });

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_card, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@Nonnull CustomViewHolder viewHolder, int position){

        if(filteredList == null) {
            final String imageUrl = GetRecipe.getInstance().recipeInfo.get(position).GetImg();

                Thread mThread = new Thread() {

                    @Override
                    public void run() {
                        try {
                            URL url = new URL(imageUrl);
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
                    viewHolder.img.setImageBitmap(bm);
                    viewHolder.recipe_name.setText(GetRecipe.getInstance().recipeInfo.get(position).GetName());
                    viewHolder.recipe_info.setText(GetRecipe.getInstance().recipeInfo.get(position).GetInfo());
                    viewHolder.recipe_number.setText(GetRecipe.getInstance().recipeInfo.get(position).GetCode());

                } catch (InterruptedException e) {
                }
        } else if (filteredList.size() == 0){

            final String imageUrl = GetRecipe.getInstance().recipeInfo.get(position).GetImg();

                    Thread mThread = new Thread() {

                        @Override
                        public void run() {
                            try {
                                URL url = new URL(imageUrl);
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
                        viewHolder.img.setImageBitmap(bm);
                        viewHolder.recipe_name.setText(GetRecipe.getInstance().recipeInfo.get(position).GetName());
                        viewHolder.recipe_info.setText(GetRecipe.getInstance().recipeInfo.get(position).GetInfo());
                        viewHolder.recipe_number.setText(GetRecipe.getInstance().recipeInfo.get(position).GetCode());

                    } catch (InterruptedException e) {
                    }
        } else {
            final String imageUrl = filteredList.get(position).GetImg();

                Thread mThread = new Thread() {

                    @Override
                    public void run() {
                        try {
                            URL url = new URL(imageUrl);
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
                    viewHolder.img.setImageBitmap(bm);
                    viewHolder.recipe_name.setText(filteredList.get(position).GetName());
                    viewHolder.recipe_info.setText(filteredList.get(position).GetInfo());
                    viewHolder.recipe_number.setText(filteredList.get(position).GetCode());

                } catch (InterruptedException e) {
                }
        }
    }

    @Override
    public int getItemCount(){
        if(filteredList == null || filteredList.size() == 0){
            return GetRecipe.getInstance().recipeInfo.size();
        } else {
            return filteredList.size();
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty() || charString == "검색어를 입력하세요.") {
                    filteredList = unFilteredlist;
                } else {
                    List<RecipeInfo> filteringList = new ArrayList<RecipeInfo>();
                    for (RecipeInfo name : unFilteredlist) {
                        if (name.GetName().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(name);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<RecipeInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }


}
