package com.triangon.aruba_flora_fauna.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.triangon.aruba_flora_fauna.BaseActivity;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.models.FloraCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class FloraCategoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ListPreloader.PreloadModelProvider<String> {

    private static final int LOADING_TYPE = 1;
    private static final int CATEGORY_TYPE = 2;
    private static final int EXHAUSTED_TYPE = 3;

    private List<FloraCategory> mFloraCategories;
    private OnFloraCategoryListener mOnFloraCategoryListener;
    private RequestManager requestManager;
    private ListPreloader.PreloadSizeProvider  preloadSizeProvider;

    public FloraCategoryRecyclerAdapter(OnFloraCategoryListener mOnFloraCategoryListener,
                                        RequestManager requestManager,
                                        ListPreloader.PreloadSizeProvider  viewPreloadSizeProvider) {
        this.mOnFloraCategoryListener = mOnFloraCategoryListener;
        this.requestManager = requestManager;
        this.preloadSizeProvider = viewPreloadSizeProvider;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_flora_category_list_item, parent, false);
        return new FloraCategoryViewHolder(view, mOnFloraCategoryListener, requestManager, preloadSizeProvider);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);

        if(itemViewType == CATEGORY_TYPE) {
            ((FloraCategoryViewHolder)holder).onBind(mFloraCategories.get(position));
        }
        //TODO else itemViewType == other type to reuse recycle view

//                Glide.with(holder.itemView.getContext())
//                .load(mFloraCategories.get(position).getCategoryImage().getImageThumbnail())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .preload();
//
//
//        Glide.with(holder.itemView.getContext())
//                .load(mFloraCategories.get(position).getCategoryImage().getImageThumbnail())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(((FloraCategoryViewHolder)holder).mImage);


    }

    @Override
    public int getItemCount() {
        if(mFloraCategories != null) {
            return mFloraCategories.size();
        }

        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(mFloraCategories.get(position).getCategoryImage() != null){
            return CATEGORY_TYPE;
        }
        else if(mFloraCategories.get(position).getName().equals("LOADING...")){
            return LOADING_TYPE;
        }
//        else if(mFloraCategories.get(position).getName().equals("EXHAUSTED...")) {
//            return EXHAUSTED_TYPE;
//        }
        else return EXHAUSTED_TYPE;
    }

    public void setFloraCategories(List<FloraCategory> floraCategories) {
        mFloraCategories = floraCategories;
        notifyDataSetChanged();
    }

    public List<FloraCategory> getFloraCategories() {
        return mFloraCategories;
    }

    public void displayOnlyLoading(){
        clearRecipesList();
        FloraCategory floraCategory = new FloraCategory();
        floraCategory.setName("LOADING...");
        mFloraCategories.add(floraCategory);
        notifyDataSetChanged();
    }


    private void clearRecipesList(){
        if(mFloraCategories == null){
            mFloraCategories = new ArrayList<>();
        }
        else {
            mFloraCategories.clear();
        }
        notifyDataSetChanged();
    }

    private boolean isLoading(){
        if(mFloraCategories != null){
            if(mFloraCategories.size() > 0){
                if(mFloraCategories.get(mFloraCategories.size() - 1).getName().equals("LOADING...")){
                    return true;
                }
            }
        }
        return false;
    }

    public void hideLoading(){
        if(isLoading()) {
            if (mFloraCategories.get(0).getName().equals("LOADING...")) {
                mFloraCategories.remove(mFloraCategories.size() - 1);
            }
        }
        notifyDataSetChanged();
    }

    public void setQueryExhausted(){
        hideLoading();
        FloraCategory exhaustedRecipe = new FloraCategory();
        exhaustedRecipe.setName("NO RESULTS...");
        mFloraCategories.add(exhaustedRecipe);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public List<String> getPreloadItems(int position) {
        String url = "";
        if(mFloraCategories.get(position).getId() != null)
            url = mFloraCategories.get(position).getCategoryImage().getImageThumbnail();
        if(TextUtils.isEmpty(url)){
            return Collections.emptyList();
        }
        return Collections.singletonList(url);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull String item) {
        return requestManager.load(item) ;
    }
}
