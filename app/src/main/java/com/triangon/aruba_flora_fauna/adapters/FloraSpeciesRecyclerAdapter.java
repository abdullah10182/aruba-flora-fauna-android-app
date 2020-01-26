package com.triangon.aruba_flora_fauna.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class FloraSpeciesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ListPreloader.PreloadModelProvider<String> {

    private List<FloraSpecies> mFloraSpecies;
    private OnFloraSpeciesListener mOnFloraSpeciesListener;
    private RequestManager requestManager;
    private ListPreloader.PreloadSizeProvider preloadSizeProvider;

    public FloraSpeciesRecyclerAdapter(OnFloraSpeciesListener mOnFloraSpeciesListener,
                                       RequestManager requestManager,
                                       ListPreloader.PreloadSizeProvider viewPreloadSizeProvider) {
        this.mOnFloraSpeciesListener = mOnFloraSpeciesListener;
        this.requestManager = requestManager;
        this.preloadSizeProvider = viewPreloadSizeProvider;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_flora_species_list_item, parent, false);
        return new FloraSpeciesViewHolder(view, mOnFloraSpeciesListener, requestManager, preloadSizeProvider);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FloraSpeciesViewHolder)holder).onBind(mFloraSpecies.get(position));
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

    @NonNull
    @Override
    public List<String> getPreloadItems(int position) {
        String url = "";
        if(mFloraSpecies.get(position).getId() != null)
            url = mFloraSpecies.get(position).getMainImage().getImageThumbnail();
        if(TextUtils.isEmpty(url)){
            return Collections.emptyList();
        }
        return Collections.singletonList(url);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull String item) {
        return requestManager.load(item);
    }
}
