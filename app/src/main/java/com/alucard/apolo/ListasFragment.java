package com.alucard.apolo;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alucard.apolo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListasFragment extends Fragment {


    public ListasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_listas, container, false);
        View view = inflater.inflate(R.layout.fragment_listas, container, false);

        return view;
    }

}
