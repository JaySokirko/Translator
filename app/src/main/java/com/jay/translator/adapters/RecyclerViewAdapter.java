package com.jay.translator.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jay.translator.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "TAG";
    private ArrayList<Integer> images;
    private ArrayList<String> names;
    private Context context;

    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    private static ClickListener clickListener;

    public RecyclerViewAdapter(ArrayList<Integer> imagesUrls, ArrayList<String> names, Context context) {
        this.images = imagesUrls;
        this.names = names;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context)
                .load(images.get(position))
                .into(holder.image);

        holder.name.setText(names.get(position));
    }




    @Override
    public int getItemCount() {
        return names.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView name;
        private CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.icon_language);
            name = itemView.findViewById(R.id.hint_language);
            checkBox = itemView.findViewById(R.id.check_box);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RecyclerViewAdapter.clickListener = clickListener;
    }

}
