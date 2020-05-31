package com.example.mpproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import javax.annotation.Nullable;


public class Frag_mainlist extends Fragment{

    GetData db = new GetData();
    InFieldData getData = new InFieldData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_mainlist,container,false);


        return view;
    }


}
