package com.ribbons.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ribbons.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyRibbonFragment extends Fragment {


    public MyRibbonFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_ribbon, container, false);
    }

}
