package com.triangon.aruba_flora_fauna.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.triangon.aruba_flora_fauna.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdditionalImagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mImage;
    OnAdditionalImageListener onAdditionalImageListener;

    public AdditionalImagesViewHolder(@NonNull View itemView, OnAdditionalImageListener onAdditionalImageListener) {
        super(itemView);
        this.onAdditionalImageListener = onAdditionalImageListener;
        mImage = itemView.findViewById(R.id.iv_additional_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onAdditionalImageListener.onAdditionalmageClick(getAdapterPosition());
    }
}
