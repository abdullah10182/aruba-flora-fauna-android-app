package com.triangon.aruba_flora_fauna.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.models.FloraCategory;

import java.util.List;

import androidx.annotation.NonNull;
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

        ((FloraCategoryViewHolder)holder).mTitle.setText(mFloraCategories.get(position).getName());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.aff_logo_grey).error(R.drawable.aff_logo_grey);

        Glide.with(holder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(mFloraCategories.get(position).getCategoryImage().getImageThumbnail())
                .into(((FloraCategoryViewHolder)holder).mImage);

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
