package com.triangon.aruba_flora_fauna.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.triangon.aruba_flora_fauna.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FloraSpeciesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView mTitle;
    ImageView mImage;
    TextView mSubTitle;
    TextView mCategory;
    TextView mFamily;
    LinearLayout mProtectedWrapper;
    LinearLayout mInvasiveWrapper;
    LinearLayout mEnedmicWrapper;
    OnFloraSpeciesListener onFloraSpeciesListener;

    public FloraSpeciesViewHolder(@NonNull View itemView, OnFloraSpeciesListener onFloraSpeciesListener) {
        super(itemView);
        this.onFloraSpeciesListener = onFloraSpeciesListener;
        mTitle = itemView.findViewById(R.id.tv_species_list_title);
        mSubTitle = itemView.findViewById(R.id.tv_species_list_sub_title);
        mCategory = itemView.findViewById(R.id.tv_species_list_category);
        mFamily = itemView.findViewById(R.id.tv_species_list_falmily);
        mImage = itemView.findViewById(R.id.iv_species_list_image);
        mProtectedWrapper = itemView.findViewById(R.id.ll_protected_wrapper);
        mInvasiveWrapper = itemView.findViewById(R.id.ll_invasive_wrapper);
        mEnedmicWrapper = itemView.findViewById(R.id.ll_endemic_wrapper);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onFloraSpeciesListener.onFloraSpeciesClick(getAdapterPosition());
    }
}
