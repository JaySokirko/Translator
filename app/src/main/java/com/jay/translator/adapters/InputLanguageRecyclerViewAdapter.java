package com.jay.translator.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jay.translator.R;

import java.util.ArrayList;

public class InputLanguageRecyclerViewAdapter extends RecyclerView.Adapter<InputLanguageRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Integer> images;
    private ArrayList<String> names;
    private Context context;
    private ArrayList<Boolean> checked;

    private int selectedPosition = -1;// no selection by default

    private InputLanguageItemClick inputLanguageItemClick;

    public InputLanguageRecyclerViewAdapter(ArrayList<Integer> imagesUrls, ArrayList<String> names, ArrayList<Boolean> checked, Context context, InputLanguageItemClick inputLanguageItemClick) {
        this.images = imagesUrls;
        this.names = names;
        this.context = context;
        this.checked = checked;
        this.inputLanguageItemClick = inputLanguageItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_view, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Glide.with(context)
                .load(images.get(position))
                .into(holder.image);

        holder.name.setText(names.get(position));


        if (selectedPosition == -1) {
            holder.checkBox.setChecked(checked.get(position));
        } else {
            holder.checkBox.setChecked(selectedPosition == position);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();

                inputLanguageItemClick.OnSelectInputLanguage(selectedPosition);
            }
        });
    }


    @Override
    public int getItemCount() {
        return names.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name;
        private CheckBox checkBox;
        private RelativeLayout layout;

        ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.icon_language);
            name = itemView.findViewById(R.id.hint_language);
            checkBox = itemView.findViewById(R.id.check_box);
            layout = itemView.findViewById(R.id.background);
        }
    }
}
