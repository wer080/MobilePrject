package com.example.mpproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.annotation.Nullable;

public class Frag_recipes extends Fragment implements TextWatcher {

    private RecyclerView mRecyclerView;
    private CustomAdapter2 mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText editText;

    public static Frag_recipes instance = new Frag_recipes();

    public static Frag_recipes getInstance(){
        return instance;
    }

    FloatingActionButton fab;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_recipes,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView2);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new CustomAdapter2(GetRecipe.getInstance().recipeInfo, GetRecipe.getInstance().recipeIngredient, GetRecipe.getInstance().recipeProcess, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));

        editText = (EditText)view.findViewById(R.id.search_recipes);
        editText.addTextChangedListener(this);

        fab = view.findViewById(R.id.add_recipe);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Frag_recipeBoard frag_recipeBoard = new Frag_recipeBoard();
                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, frag_recipeBoard).addToBackStack(null).commit();
            }
        });


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
