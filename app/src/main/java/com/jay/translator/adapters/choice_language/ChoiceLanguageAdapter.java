package com.jay.translator.adapters.choice_language;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jay.translator.R;

import java.util.ArrayList;

public class ChoiceLanguageAdapter extends BaseAdapter {


    private Context context;
    private String[] values;
    private int[] images;


    public ChoiceLanguageAdapter(Context context, String[] values, int[] images) {
        this.context = context;
        this.values = values;
        this.images = images;
    }


    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_view_choice_language, parent, false);

            viewHolder.txtName = convertView.findViewById(R.id.text_view_choice_language_list_view);
            viewHolder.icon = convertView.findViewById(R.id.image_view_choice_language_list_view);

            convertView.setTag(viewHolder);
        }else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(values[position]);
        viewHolder.icon.setImageResource(images[position]);

        return convertView;
    }


    private static class ViewHolder {

        TextView txtName;
        ImageView icon;
        RadioButton radioButton;
    }
}
