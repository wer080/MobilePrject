package com.example.mpproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import javax.annotation.Nonnull;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    InFieldTodayData usingData;

    public CustomAdapter(InFieldTodayData d) {
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
        viewHolder.product_name.setText(GetDataToday.getInstance().todayData.GetProductName().get(position));
        viewHolder.product_cat.setText(GetDataToday.getInstance().todayData.GetSpecies().get(position));
        viewHolder.product_grade.setText(GetDataToday.getInstance().todayData.GetGrade().get(position));
        viewHolder.product_unit.setText(GetDataToday.getInstance().todayData.GetUnit().get(position));

        double val = GetDataToday.getInstance().todayData.GetPrice().get(position);
        String price = String.format("%.0f", val);
        viewHolder.product_price.setText(price);
    }

    @Override
    public int getItemCount(){
        return GetDataToday.getInstance().todayData.GetExaminDate().size();
    }


}
