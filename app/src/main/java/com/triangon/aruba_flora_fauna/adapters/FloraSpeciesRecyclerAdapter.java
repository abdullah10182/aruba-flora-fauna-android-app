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


        ((FloraSpeciesViewHolder)holder).mTitle.setText(mFloraSpecies.get(position).getCommonName());
        ((FloraSpeciesViewHolder)holder).mSubTitle.setText(mFloraSpecies.get(position).getPapiamentoName());
        ((FloraSpeciesViewHolder)holder).mCategory.setText("Category: " + mFloraSpecies.get(position).getCategoryName());
        ((FloraSpeciesViewHolder)holder).mFamily.setText("Family: " + mFloraSpecies.get(position).getFamily());

        Glide.with(holder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(mFloraSpecies.get(position).getMainImage().getImageThumbnail())
                .into(((FloraSpeciesViewHolder)holder).mImage);

        //protected
        if(mFloraSpecies.get(position).isProtectedLocally()){
            ((FloraSpeciesViewHolder)holder).mProtectedWrapper.setVisibility(View.VISIBLE);
        } else {
            ((FloraSpeciesViewHolder)holder).mProtectedWrapper.setVisibility(View.GONE);
        }

        //invasive
        if(mFloraSpecies.get(position).getStatusId() != null && mFloraSpecies.get(position).getStatusId().equals("11")){
            ((FloraSpeciesViewHolder)holder).mInvasiveWrapper.setVisibility(View.VISIBLE);
        } else {
            ((FloraSpeciesViewHolder)holder).mInvasiveWrapper.setVisibility(View.GONE);
        }

        //endemic
        if(mFloraSpecies.get(position).getStatusId() != null && mFloraSpecies.get(position).getStatusId().equals("13")){
            ((FloraSpeciesViewHolder)holder).mEnedmicWrapper.setVisibility(View.VISIBLE);
        } else {
            ((FloraSpeciesViewHolder)holder).mEnedmicWrapper.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if(mFloraSpecies != null) {
            return mFloraSpecies.size();
        }

        return 0;
    }

    public void setFloraSpecies(List<FloraSpecies> floraSpecies) {
        mFloraSpecies = floraSpecies;
        notifyDataSetChanged();
    }

    public List<FloraSpecies> getFloraSpecies() {
        return mFloraSpecies;
    }
}
