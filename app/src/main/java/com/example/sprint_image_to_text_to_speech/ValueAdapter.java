package com.example.sprint_image_to_text_to_speech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.ViewHolder> {

    ArrayList<TextFromImageEntity> localTextFromImageBean;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView valueText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            valueText = itemView.findViewById(R.id.savedtextInDb);
        }

        public TextView getValueText() {
            return valueText;
        }
    }

    public ValueAdapter(ArrayList<TextFromImageEntity> localTextFromImageBean) {
        this.localTextFromImageBean = localTextFromImageBean;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.db_text_from_images_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ValueAdapter.ViewHolder holder, int position) {

        holder.getValueText().setText(localTextFromImageBean.get(position).getTextFromImage());
    }

    @Override
    public int getItemCount() {
        return localTextFromImageBean.size();
    }
}
