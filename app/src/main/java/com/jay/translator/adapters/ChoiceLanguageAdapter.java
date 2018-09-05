package com.jay.translator.adapters;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jay.translator.R;
import com.jay.translator.ViewSettings;

import java.util.ArrayList;

public class ChoiceLanguageAdapter extends BaseAdapter {


    private Context context;
    private String[] values;
    private int[] images;
    private int[] flags;

    private AnimationDrawable animationDrawable;

    public ChoiceLanguageAdapter(Context context, String[] values, int[] images, int[] flags) {
        this.context = context;
        this.values = values;
        this.images = images;
        this.flags = flags;
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
            viewHolder.icFlag = convertView.findViewById(R.id.flag_list_view);

            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(values[position]);
        viewHolder.icon.setImageResource(images[position]);
        viewHolder.icFlag.setImageResource(flags[position]);

//        Animation anim = AnimationUtils.loadAnimation(context,R.anim.grid_item_animation_on_start);
//        anim.setStartOffset(position * 100);
//        convertView.setAnimation(anim);
//        anim.start();

        return convertView;
    }


    private static class ViewHolder {

        TextView txtName;
        ImageView icon;
        ImageView icFlag;
    }
}
