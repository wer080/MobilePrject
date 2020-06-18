package com.example.mpproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.annotation.Nullable;

public class Frag_chart extends Fragment implements TextWatcher {

    private RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_chart,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new CustomAdapter(GetDataToday.getInstance().todayData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));

        TextView examin_data = (TextView)view.findViewById(R.id.show_date);
        examin_data.setText(GetDataToday.getInstance().todayData.get(0).GetExaminDate());

        editText = (EditText)view.findViewById(R.id.to_search);
        editText.addTextChangedListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
        mAdapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable){

    }


}
