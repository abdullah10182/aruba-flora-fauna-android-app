package com.triangon.aruba_flora_fauna.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.triangon.aruba_flora_fauna.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FloraSpeciesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title;
    ImageView image;
    OnFloraSpeciesListener onFloraSpeciesListener;

    public FloraSpeciesViewHolder(@NonNull View itemView, OnFloraSpeciesListener onFloraSpeciesListener) {
        super(itemView);
        this.onFloraSpeciesListener = onFloraSpeciesListener;
        title = itemView.findViewById(R.id.category_title);
        image = itemView.findViewById(R.id.category_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onFloraSpeciesListener.onFloraSpeciesClick(getAdapterPosition());
    }
}
