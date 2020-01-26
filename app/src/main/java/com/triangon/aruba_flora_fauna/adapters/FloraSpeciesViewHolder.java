package com.triangon.aruba_flora_fauna.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    RequestManager requestManager;
    ListPreloader.PreloadSizeProvider viewPreloadSizeProvider;

    public FloraSpeciesViewHolder(@NonNull View itemView,
                                  OnFloraSpeciesListener onFloraSpeciesListener,
                                  RequestManager requestManager,
                                  ListPreloader.PreloadSizeProvider viewPreloadSizeProvider) {
        super(itemView);
        this.onFloraSpeciesListener = onFloraSpeciesListener;
        this.requestManager = requestManager;
        this.viewPreloadSizeProvider = viewPreloadSizeProvider;

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

    public void onBind(FloraSpecies floraSpecies) {
        requestManager
                .load(floraSpecies.getMainImage().getImageThumbnail())
                .into(mImage);

        mTitle.setText(floraSpecies.getCommonName());
        mSubTitle.setText(floraSpecies.getPapiamentoName());
        mCategory.setText("Category: " + floraSpecies.getCategoryName());
        mFamily.setText("Family: " + floraSpecies.getFamily());

        //protected
        if(floraSpecies.isProtectedLocally()){
            mProtectedWrapper.setVisibility(View.VISIBLE);
        } else {
            mProtectedWrapper.setVisibility(View.GONE);
        }

        //invasive
        if(floraSpecies.getStatusId() != null && floraSpecies.getStatusId().equals("11")){
            mInvasiveWrapper.setVisibility(View.VISIBLE);
        } else {
            mInvasiveWrapper.setVisibility(View.GONE);
        }

        //endemic
        if(floraSpecies.getStatusId() != null && floraSpecies.getStatusId().equals("13")){
            mEnedmicWrapper.setVisibility(View.VISIBLE);
        } else {
            mEnedmicWrapper.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        onFloraSpeciesListener.onFloraSpeciesClick(getAdapterPosition());
    }
}
