package com.triangon.aruba_flora_fauna.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
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
    private boolean mImageViewerOpen = false;

    @BindView(R.id.tv_species_detail_description)
    public HtmlTextView mDescription;

    @BindView(R.id.tv_common_name)
    public TextView mCommonName;
    @BindView(R.id.tv_papiamento_name)
    public TextView mPapiamentoName;
    @BindView(R.id.tv_scientific_name)
    public TextView mScientificName;

    @BindView(R.id.tv_category_name)
    public TextView mCategoryName;
    @BindView(R.id.tv_family_name)
    public TextView mFamilyName;
    @BindView(R.id.tv_geo_origin_name)
    public TextView mGeoOriginName;
    @BindView(R.id.tv_protected_locally_name)
    public TextView mProtectedName;

    @BindView(R.id.tv_more_info)
    public TextView mMoreInfo;

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
        initMoreInfoLink();

        if (savedInstanceState != null){
            int currentPosition = 0;
            boolean imageViewerOpen = savedInstanceState.getBoolean("imageViewerOpen");
            currentPosition = savedInstanceState.getInt("currentPosition");
            if(imageViewerOpen)
                showAdditionalImages(currentPosition);
        }

        initRecyclerView();

    }

    private void initMoreInfoLink() {
        mMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("moreInfoLink", mSelectedSpecies.getMoreInfoLink());
                startActivity(intent);
            }
        });
    }

    private void showAdditionalImages(int currentPosition) {
        ImageView smallImage = null;
        RequestOptions requestOptions = new RequestOptions();

        if(mRecyclerView.getLayoutManager() != null) {
            View row = mRecyclerView.getLayoutManager().findViewByPosition(currentPosition);
            smallImage = row.findViewById(R.id.iv_additional_image);
            requestOptions.placeholder(smallImage.getDrawable());
        } else {
            requestOptions.placeholder(R.drawable.aff_logo_grey);
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
                  requestOptions.placeholder(R.drawable.aff_logo_grey);
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
                .placeholder(R.drawable.aff_logo_grey);
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(mSelectedSpecies.getMainImage().getImageThumbnail())
                .into(mHeroImage);
    }

    private void setTextFields() {
        //names
        if(mSelectedSpecies.getCommonName() != null)
            mCommonName.setText(mSelectedSpecies.getCommonName());
        if(mSelectedSpecies.getPapiamentoName() != null)
            mPapiamentoName.setText(mSelectedSpecies.getPapiamentoName());
        if(mSelectedSpecies.getScientificName() != null)
            mScientificName.setText(mSelectedSpecies.getScientificName());
        //description
        if(mSelectedSpecies.getDescription() != null)
            mDescription.setHtml(mSelectedSpecies.getDescription());
        //info
        if(mSelectedSpecies.getCategoryName() != null)
            mCategoryName.setText(mSelectedSpecies.getCategoryName());
        if(mSelectedSpecies.getFamily() != null)
            mFamilyName.setText(mSelectedSpecies.getFamily());
        if(mSelectedSpecies.getStatusName() != null)
            mGeoOriginName.setText(mSelectedSpecies.getStatusName());
        if(mSelectedSpecies.isProtectedLocally())
            mProtectedName.setText("Yes");
        else
            mProtectedName.setText("No");
        if(mSelectedSpecies.getMoreInfoLink() != null)
            mMoreInfo.setText(mSelectedSpecies.getMoreInfoLink());
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
        mRecyclerView.setNestedScrollingEnabled(false);
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
