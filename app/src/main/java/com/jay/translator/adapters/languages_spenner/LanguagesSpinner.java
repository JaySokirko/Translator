package com.jay.translator.adapters.languages_spenner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jay.translator.R;

import java.util.ArrayList;

public class LanguagesSpinner extends ArrayAdapter<Language> {


    public LanguagesSpinner(@NonNull Context context, ArrayList<Language> languageList) {
        super(context, 0, languageList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate( R.layout.langeuges_spinner,parent,false);
        }

        TextView country = convertView.findViewById(R.id.spinner_language);

        Language currentItem = getItem(position);

        if (currentItem != null) {
            country.setText(currentItem.getCountryName());
        }

        return convertView;
    }
}
