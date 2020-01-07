package com.triangon.aruba_flora_fauna.adapters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.models.FloraCategory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

public class FloraCategoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FloraCategory> mFloraCategories;
    private OnFloraCategoryListener mOnFloraCategoryListener;

    public FloraCategoryRecyclerAdapter(OnFloraCategoryListener mOnFloraCategoryListener) {
        this.mOnFloraCategoryListener = mOnFloraCategoryListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_flora_category_list_item, parent, false);
        return new FloraCategoryViewHolder(view, mOnFloraCategoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);


        ((FloraCategoryViewHolder)holder).title.setText(mFloraCategories.get(position).getName());

        Glide.with(holder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(mFloraCategories.get(position).getCategoryImage().getImageThumbnail())
                .into(((FloraCategoryViewHolder)holder).image);

    }

    @Override
    public int getItemCount() {
        if(mFloraCategories != null) {
            return mFloraCategories.size();
        }

        return 0;
    }

    public void setFloraCategories(List<FloraCategory> floraCategories) {
        mFloraCategories = floraCategories;
        notifyDataSetChanged();
    }

    public List<FloraCategory> getFloraCategories() {
        return mFloraCategories;
    }
}
