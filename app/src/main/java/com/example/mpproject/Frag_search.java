package com.example.mpproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import javax.annotation.Nullable;

public class Frag_search extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_mainlist, container, false);

        Log.d("Test", "" + GetDataToday.getInstance().todayData.GetExaminDate().size());
        return view;
    }
}
