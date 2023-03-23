package com.improve.mp3player;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

   ArrayList<String> list;
   Context mContext;

    public MusicAdapter(ArrayList<String> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_music, parent, false);

        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MusicViewHolder holder, int position) {

        String filePath = list.get(position);
        Log.e("File path : ", filePath);
        final String title = filePath.substring(filePath.lastIndexOf("/") + 1);
        holder.textViewFileName.setText(title);
        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, MusicActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("filePath", filePath);
            intent.putExtra("position", position);
            intent.putExtra("list", list);
            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFileName;
        private CardView cardView;


        public MusicViewHolder(View itemView) {
            super(itemView);
            textViewFileName = itemView.findViewById(R.id.textViewFileName);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
