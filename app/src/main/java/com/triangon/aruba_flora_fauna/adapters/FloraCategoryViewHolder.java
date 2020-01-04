package com.triangon.aruba_flora_fauna.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.triangon.aruba_flora_fauna.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FloraCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title;
    ImageView image;
    OnFloraCategoryListener onFloraCategoryListener;

    public FloraCategoryViewHolder(@NonNull View itemView, OnFloraCategoryListener onFloraCategoryListener) {
        super(itemView);
        this.onFloraCategoryListener = onFloraCategoryListener;
        title = itemView.findViewById(R.id.category_title);
        image = itemView.findViewById(R.id.category_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onFloraCategoryListener.onFloraCategoryClick(getAdapterPosition());
    }
}
