package com.example.mpproject;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> implements Filterable {

    List<InFieldTodayData> usingData;
    List<InFieldTodayData> unFilteredlist = GetDataToday.getInstance().todayData;
    List<InFieldTodayData> filteredList;

    public CustomAdapter(List<InFieldTodayData> d) {
        this.usingData = d;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView product_name;
        public TextView product_cat;
        public TextView product_grade;
        public TextView product_unit;
        public TextView product_price;

        public CustomViewHolder(View view){
            super(view);

            this.product_name = (TextView)view.findViewById(R.id.prd_name);
            this.product_cat = (TextView)view.findViewById(R.id.prd_cat);
            this.product_grade = (TextView)view.findViewById(R.id.prd_grade);
            this.product_unit = (TextView)view.findViewById(R.id.prd_unit);
            this.product_price = (TextView)view.findViewById(R.id.prd_price);

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.today_data_list, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@Nonnull CustomViewHolder viewHolder, int position){

        if(filteredList == null){
            viewHolder.product_name.setText(GetDataToday.getInstance().todayData.get(position).GetProductName());
            viewHolder.product_cat.setText(GetDataToday.getInstance().todayData.get(position).GetSpecies());
            viewHolder.product_grade.setText(GetDataToday.getInstance().todayData.get(position).GetGrade());
            viewHolder.product_unit.setText(GetDataToday.getInstance().todayData.get(position).GetUnit());

            double val = GetDataToday.getInstance().todayData.get(position).GetPrice();
            String price = String.format("%.0f", val);
            viewHolder.product_price.setText(price);

        } else if(filteredList.size() == 0){
            viewHolder.product_name.setText(GetDataToday.getInstance().todayData.get(position).GetProductName());
            viewHolder.product_cat.setText(GetDataToday.getInstance().todayData.get(position).GetSpecies());
            viewHolder.product_grade.setText(GetDataToday.getInstance().todayData.get(position).GetGrade());
            viewHolder.product_unit.setText(GetDataToday.getInstance().todayData.get(position).GetUnit());

            double val = GetDataToday.getInstance().todayData.get(position).GetPrice();
            String price = String.format("%.0f", val);
            viewHolder.product_price.setText(price);
        } else {
            viewHolder.product_name.setText(filteredList.get(position).GetProductName());
            viewHolder.product_cat.setText(filteredList.get(position).GetSpecies());
            viewHolder.product_grade.setText(filteredList.get(position).GetGrade());
            viewHolder.product_unit.setText(filteredList.get(position).GetUnit());


            double val = filteredList.get(position).GetPrice();
            String price = String.format("%.0f", val);
            viewHolder.product_price.setText(price);
        }


    }


    @Override
    public int getItemCount(){
        if(filteredList == null || filteredList.size() == 0){
            return GetDataToday.getInstance().todayData.size();
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
                    List<InFieldTodayData> filteringList = new ArrayList<InFieldTodayData>();
                    for (InFieldTodayData name : unFilteredlist) {
                        if (name.GetProductName().toLowerCase().contains(charString.toLowerCase())) {
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
                filteredList = (ArrayList<InFieldTodayData>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
