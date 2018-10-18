package com.jay.translator.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jay.translator.OnSwipeTouchListener;
import com.jay.translator.R;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class SavedTextAdapter extends RecyclerView.Adapter<SavedTextAdapter.ViewHolder> {

    private ArrayList<String> dates;
    private ArrayList<String> inputTexts;
    private ArrayList<String> translatedTexts;
    private Context context;
    private boolean isClicked = true;

    public SavedTextAdapter(ArrayList<String> date, ArrayList<String> inputText, ArrayList<String> translatedText, Context context) {
        this.dates = date;
        this.inputTexts = inputText;
        this.translatedTexts = translatedText;
        this.context = context;
    }


    @NonNull
    @Override
    public SavedTextAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_text_list_view,
                parent, false);

        return new ViewHolder(view);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final SavedTextAdapter.ViewHolder holder, int position) {

        holder.date.setText(dates.get(position));
        holder.inputText.setText(inputTexts.get(position));
        holder.translatedText.setText(translatedTexts.get(position));


        holder.touchLayout.setOnTouchListener(new OnSwipeTouchListener(context) {

            @Override
            public void onClick() {
                super.onClick();

                if (isClicked) {

                    holder.layout.animate()
                            .translationX(-(context.getResources()
                                    .getDimension(R.dimen.standard_77)))
                            .start();
                } else {

                    holder.layout.animate()
                            .translationX(0)
                            .start();
                }

                isClicked = !isClicked;
                Log.d(TAG, "onClick: ");
            }
        });



        final int pos = position;
        holder.deleteRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dates.remove(pos);
                inputTexts.remove(pos);
                translatedTexts.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, dates.size());
            }
        });




    }

    @Override
    public int getItemCount() {
        return dates.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView inputText;
        private TextView translatedText;
        private FrameLayout layout;
        private ImageView deleteRow;
        private FrameLayout touchLayout;

        ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.saved_text_date);
            inputText = itemView.findViewById(R.id.input_text);
            translatedText = itemView.findViewById(R.id.output_text);
            layout = itemView.findViewById(R.id.translated_text);
            deleteRow = itemView.findViewById(R.id.delete_translated_text);
            touchLayout = itemView.findViewById(R.id.touch_listener);
        }
    }
}
