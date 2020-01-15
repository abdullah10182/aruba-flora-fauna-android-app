package com.triangon.aruba_flora_fauna.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.triangon.aruba_flora_fauna.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FloraCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView mTitle;
    ImageView mImage;
    OnFloraCategoryListener onFloraCategoryListener;

    public FloraCategoryViewHolder(@NonNull View itemView, OnFloraCategoryListener onFloraCategoryListener) {
        super(itemView);
        this.onFloraCategoryListener = onFloraCategoryListener;
        mTitle = itemView.findViewById(R.id.tv_category_title);
        mImage = itemView.findViewById(R.id.iv_category_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onFloraCategoryListener.onFloraCategoryClick(getAdapterPosition());
    }
}
