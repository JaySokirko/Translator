package com.jay.translator.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private boolean[] checked;

    private int selectedPosition = -1;// no selection by default

    private static ClickListener clickListener;

    public RecyclerViewAdapter(ArrayList<Integer> imagesUrls, ArrayList<String> names, boolean[] checked, Context context) {
        this.images = imagesUrls;
        this.names = names;
        this.context = context;
        this.checked = checked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_view, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Glide.with(context)
                .load(images.get(position))
                .into(holder.image);

        holder.name.setText(names.get(position));

        holder.checkBox.setChecked(selectedPosition == position);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return names.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
