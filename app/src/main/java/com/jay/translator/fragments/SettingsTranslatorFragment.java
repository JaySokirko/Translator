package com.jay.translator.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jay.translator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsTranslatorFragment extends Fragment {


    public SettingsTranslatorFragment() {
        // Required empty public constructor
    }


    public static final String FRAGMENT_SETTINGS = "FRAGMENT_SETTINGS";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = getLayoutInflater().inflate(R.layout.layout_settings, container, false);

        return view;
    }
}
