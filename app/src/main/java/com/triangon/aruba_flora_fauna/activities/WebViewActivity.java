package com.triangon.aruba_flora_fauna.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.triangon.aruba_flora_fauna.R;

public class WebViewActivity extends AppCompatActivity {
    @BindView(R.id.wv_more_info)
    public WebView mWebview;
    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ButterKnife.bind(this);

        initWebView();
        initToolbar("");
    }

    private void initWebView() {
        String moreInfoLink = getIntent().getExtras().getString("moreInfoLink");
        mWebview.setWebViewClient(new WebViewClient());
        mWebview.loadUrl(moreInfoLink);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    public void initToolbar(String title) {
        mToolbar  = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
