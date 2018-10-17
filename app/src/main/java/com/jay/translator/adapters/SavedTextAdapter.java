package com.jay.translator.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jay.translator.OnSwipeTouchListener;
import com.jay.translator.R;

import static android.support.constraint.Constraints.TAG;

public class SavedTextAdapter extends BaseAdapter {

    private Context context;
    private String[] date;
    private String[] inputText;
    private String[] outputText;

    public SavedTextAdapter(Context context, String[] date, String[] inputText, String[] outputText) {
        this.context = context;
        this.date = date;
        this.inputText = inputText;
        this.outputText = outputText;
    }

    @Override
    public int getCount() {
        return date.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.saved_text_list_view, parent, false);

            viewHolder.date = convertView.findViewById(R.id.saved_text_date);
            viewHolder.inputText = convertView.findViewById(R.id.input_text);
            viewHolder.outputText = convertView.findViewById(R.id.output_text);

            viewHolder.layout = convertView.findViewById(R.id.translated_text);

            viewHolder.delete = convertView.findViewById(R.id.delete_translated_text);

            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.date.setText(date[position]);
        viewHolder.inputText.setText(inputText[position]);
        viewHolder.outputText.setText(outputText[position]);

        return convertView;
    }


    class ViewHolder {

        TextView date;
        TextView inputText;
        TextView outputText;
        FrameLayout layout;
        ImageView delete;
    }
}
