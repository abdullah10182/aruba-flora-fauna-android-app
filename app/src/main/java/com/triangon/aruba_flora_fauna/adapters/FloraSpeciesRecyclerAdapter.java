package com.triangon.aruba_flora_fauna.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FloraSpeciesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FloraSpecies> mFloraSpecies;
    private OnFloraSpeciesListener mOnFloraSpeciesListener;

    public FloraSpeciesRecyclerAdapter(OnFloraSpeciesListener mOnFloraSpeciesListener) {
        this.mOnFloraSpeciesListener = mOnFloraSpeciesListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_flora_species_list_item, parent, false);
        return new FloraSpeciesViewHolder(view, mOnFloraSpeciesListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);


        ((FloraSpeciesViewHolder)holder).title.setText(mFloraSpecies.get(position).getCommonName());

        Glide.with(holder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(mFloraSpecies.get(position).getMainImage().getImageThumbnail())
                .into(((FloraSpeciesViewHolder)holder).image);

    }

    @Override
    public int getItemCount() {
        if(mFloraSpecies != null) {
            return mFloraSpecies.size();
        }

        return 0;
    }

    public void setmFloraSpecies(List<FloraSpecies> floraSpecies) {
        mFloraSpecies = floraSpecies;
        notifyDataSetChanged();
    }

    public List<FloraSpecies> getFloraSpecies() {
        return mFloraSpecies;
    }
}
