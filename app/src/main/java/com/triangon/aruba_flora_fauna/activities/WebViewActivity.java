package com.triangon.aruba_flora_fauna.activities;

import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ButterKnife.bind(this);

        String moreInfoLink = getIntent().getExtras().getString("moreInfoLink");
        mWebview.setWebViewClient(new WebViewClient());
        mWebview.loadUrl(moreInfoLink);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
}
