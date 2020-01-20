package com.triangon.aruba_flora_fauna.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.models.ImageBundle;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdditionalImagesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ImageBundle> mAdditionalImages;
    private OnAdditionalImageListener mOnAdditionalImageListener;

    public AdditionalImagesRecyclerAdapter(OnAdditionalImageListener mOnAdditionalImageListener) {
        this.mOnAdditionalImageListener = mOnAdditionalImageListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_additional_image_list_item, parent, false);
        return new AdditionalImagesViewHolder(view, mOnAdditionalImageListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.aff_logo_grey).error(R.drawable.aff_logo_grey);

        Glide.with(holder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(mAdditionalImages.get(position).getImageSmall())
                .into(((AdditionalImagesViewHolder)holder).mImage);

    }

    @Override
    public int getItemCount() {
        if(mAdditionalImages != null) {
            return mAdditionalImages.size();
        }

        return 0;
    }

    public void setAdditionalImages(List<ImageBundle> additionalImages) {
        mAdditionalImages = additionalImages;
        notifyDataSetChanged();
    }

    public List<ImageBundle> getAdditionalImages() {
        return mAdditionalImages;
    }
}
