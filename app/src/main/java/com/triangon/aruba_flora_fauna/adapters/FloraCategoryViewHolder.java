package com.triangon.aruba_flora_fauna.adapters;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.models.FloraCategory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class FloraCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, RequestListener {
    private static final String TAG = "FloraCategoryViewHolder";
    TextView mTitle;
    ImageView mImage;
    OnFloraCategoryListener onFloraCategoryListener;
    RequestManager requestManager;
    ListPreloader.PreloadSizeProvider viewPreloadSizeProvider;

    public FloraCategoryViewHolder(@NonNull View itemView,
                                   OnFloraCategoryListener onFloraCategoryListener,
                                   RequestManager requestManager,
                                   ListPreloader.PreloadSizeProvider viewPreloadSizeProvider) {
        super(itemView);
        this.onFloraCategoryListener = onFloraCategoryListener;
        this.requestManager = requestManager;
        this.viewPreloadSizeProvider = viewPreloadSizeProvider;

        mTitle = itemView.findViewById(R.id.tv_category_title);
        mImage = itemView.findViewById(R.id.iv_category_image);

        itemView.setOnClickListener(this);
    }

    public void onBind(FloraCategory floraCategory) {
        requestManager
                .load(floraCategory.getCategoryImage().getImageThumbnail())
                .into(mImage);

        mTitle.setText(floraCategory.getName());

    }

    @Override
    public void onClick(View view) {
        onFloraCategoryListener.onFloraCategoryClick(getAdapterPosition());
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
        Log.e(TAG, "Load failed", e);
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
        Log.d(TAG, "onResourceReady: " + resource.toString());
        return false;
    }
}
