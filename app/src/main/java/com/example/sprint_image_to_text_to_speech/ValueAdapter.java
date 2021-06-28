package com.example.sprint_image_to_text_to_speech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.ViewHolder> {

    ArrayList<TextFromImageEntity> localTextFromImageBean;
    private OnClickListener onListener;

    private TextView valueText;
    private Button btnPlaySpeechText;

    public interface OnClickListener {
        void playDbText(int position);
    }

    public void setOnItemClickListener(OnClickListener listener) {
        onListener = listener;
    }

    public ValueAdapter(ArrayList<TextFromImageEntity> localTextFromImageBean) {
        this.localTextFromImageBean = localTextFromImageBean;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView, OnClickListener itemClickListener) {
            super(itemView);


            valueText = itemView.findViewById(R.id.savedtextInDb);
            btnPlaySpeechText = itemView.findViewById(R.id.dbActivityPlayButton);

            btnPlaySpeechText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            itemClickListener.playDbText(pos);
                        }
                    }
                }
            });
        }

        public TextView getValueText() {
            return valueText;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.db_text_from_images_layout, viewGroup, false);

        return new ViewHolder(view, onListener);
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
