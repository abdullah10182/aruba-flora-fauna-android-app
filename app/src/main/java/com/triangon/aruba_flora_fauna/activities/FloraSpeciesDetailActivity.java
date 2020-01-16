package com.triangon.aruba_flora_fauna.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;


import com.bumptech.glide.request.target.Target;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.listeners.OnDismissListener;
import com.stfalcon.imageviewer.listeners.OnImageChangeListener;
import com.stfalcon.imageviewer.loader.ImageLoader;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.adapters.AdditionalImagesRecyclerAdapter;
import com.triangon.aruba_flora_fauna.adapters.OnAdditionalImageListener;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.models.ImageBundle;

import org.sufficientlysecure.htmltextview.HtmlTextView;

public class FloraSpeciesDetailActivity extends AppCompatActivity implements OnAdditionalImageListener {

    private FloraSpecies mSelectedSpecies;
    @BindView(R.id.iv_species_detail_hero_image)
    public ImageView mHeroImage;
    @BindView(R.id.tv_species_detail_description)
    public HtmlTextView mDescription;
    private boolean mImageViewerOpen = false;

    @BindView(R.id.rv_additional_images)
    public RecyclerView mRecyclerView;
    private AdditionalImagesRecyclerAdapter mAdapter;

    private StfalconImageViewer<ImageBundle> mImageViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flora_species_detail);

        ButterKnife.bind(this);

        mSelectedSpecies = getIntent().getExtras().getParcelable("selectedSpecies");
        initToolbar();
        setHeroImage();
        setTextFields();

        if (savedInstanceState != null){
            int currentPosition = 0;
            boolean imageViewerOpen = savedInstanceState.getBoolean("imageViewerOpen");
            currentPosition = savedInstanceState.getInt("currentPosition");
            if(imageViewerOpen)
                showAdditionalImages(currentPosition);
        }

        initRecyclerView();

    }

    private void showAdditionalImages(int currentPosition) {
        ImageView smallImage = null;
        RequestOptions requestOptions = new RequestOptions();

        if(mRecyclerView.getLayoutManager() != null) {
            View row = mRecyclerView.getLayoutManager().findViewByPosition(currentPosition);
            smallImage = row.findViewById(R.id.iv_additional_image);
            requestOptions.placeholder(smallImage.getDrawable());
        } else {
            requestOptions.placeholder(R.drawable.ic_launcher_background);
        }

        mImageViewer = new StfalconImageViewer.Builder<ImageBundle>(FloraSpeciesDetailActivity.this, mSelectedSpecies.getAdditionalImages(), new ImageLoader<ImageBundle>() {
            @Override
            public void loadImage(ImageView imageView, ImageBundle imageUrl) {
                Glide.with(FloraSpeciesDetailActivity.this)
                        .setDefaultRequestOptions(requestOptions)
                        .load(imageUrl.getImageMedium())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                mImageViewerOpen = true;
                                return false;
                            }
                        })
                        .into(imageView);
            }
        }).withTransitionFrom(smallImage)
          .withStartPosition(currentPosition)
          .withImageChangeListener(new OnImageChangeListener() {
              @Override
              public void onImageChange(int position) {
                  requestOptions.placeholder(R.drawable.ic_launcher_background);
                  Toast.makeText(getApplicationContext(), mSelectedSpecies.getAdditionalImages().get(position).getImageTitle(), Toast.LENGTH_LONG).show();
              }
          })
          .withDismissListener(new OnDismissListener() {
              @Override
              public void onDismiss() {
                  mImageViewerOpen = false;
              }
          })
          .show();
    }

    private void setHeroImage() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(mSelectedSpecies.getMainImage().getImageThumbnail())
                .into(mHeroImage);
    }

    private void setTextFields() {
        //Description
        mDescription.setHtml(mSelectedSpecies.getDescription());
    }

    public void initToolbar() {
        Toolbar toolbar  = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mSelectedSpecies.getCommonName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("imageViewerOpen", mImageViewerOpen);
        if(mImageViewerOpen)
            outState.putInt("currentPosition", mImageViewer.currentPosition());
        super.onSaveInstanceState(outState);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new AdditionalImagesRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter.setAdditionalImages(mSelectedSpecies.getAdditionalImages());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAdditionalmageClick(int position) {
        showAdditionalImages(position);
        Toast.makeText(getApplicationContext(), mSelectedSpecies.getAdditionalImages().get(position).getImageTitle(), Toast.LENGTH_LONG).show();

    }
}
