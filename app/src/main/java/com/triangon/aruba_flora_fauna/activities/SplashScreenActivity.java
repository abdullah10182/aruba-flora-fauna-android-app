package com.triangon.aruba_flora_fauna.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.triangon.aruba_flora_fauna.BaseActivity;
import com.triangon.aruba_flora_fauna.R;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View easySplashScreenView = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withTargetActivity(FloraCategoryListActivity.class)
                .withSplashTimeOut(1000)
                .withBackgroundResource(android.R.color.white)
                .withLogo(R.drawable.ic_aff_logo_with_text)
                .create();

        setContentView(easySplashScreenView);

    }
}
