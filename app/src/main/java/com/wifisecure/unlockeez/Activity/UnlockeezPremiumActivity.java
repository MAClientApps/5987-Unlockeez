package com.wifisecure.unlockeez.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.wifisecure.unlockeez.R;
import com.wifisecure.unlockeez.UnLockeEzMainPageActivity;
import com.wifisecure.unlockeez.Utils;


public class UnlockeezPremiumActivity extends AppCompatActivity {

    private WebView wvMixVideosPro;
    LinearLayout layoutCheckConnection;
    Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlockeez_premuim);
        initView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initView() {
        wvMixVideosPro = findViewById(R.id.wvMixVideosPro);
        layoutCheckConnection = findViewById(R.id.layoutCheckConnection);
        CookieManager.getInstance().setAcceptCookie(true);
        wvMixVideosPro.getSettings().setJavaScriptEnabled(true);
        wvMixVideosPro.getSettings().setUseWideViewPort(true);
        wvMixVideosPro.getSettings().setLoadWithOverviewMode(true);
        wvMixVideosPro.getSettings().setDomStorageEnabled(true);
        wvMixVideosPro.getSettings().setPluginState(WebSettings.PluginState.ON);
        wvMixVideosPro.setWebChromeClient(new WebChromeClient());
        wvMixVideosPro.setVisibility(View.VISIBLE);

        wvMixVideosPro.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                String url = request.getUrl().toString();
                if (!url.startsWith("http")) {
                    startActivity(new Intent(UnlockeezPremiumActivity.this, UnLockeEzMainPageActivity.class));
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        finish();
                        return;
                    } catch (Exception InternalFlow_exception) {
                        finish();
                        return;
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        loadDataView();
    }

    public void checkInternetConnection() {
        layoutCheckConnection.setVisibility(View.VISIBLE);
        btnRetry = findViewById(R.id.btnTryAgain);
        btnRetry.setOnClickListener(view -> {
            layoutCheckConnection.setVisibility(View.GONE);
            loadDataView();
        });
    }

    protected void loadDataView() {
        if (Utils.isNetworkAvailable(this)) {
            wvMixVideosPro.loadUrl(Utils.generatePremiumLink(UnlockeezPremiumActivity.this));
        } else {
            checkInternetConnection();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        wvMixVideosPro.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        wvMixVideosPro.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wvMixVideosPro.loadUrl("about:blank");
    }

    @Override
    public void onBackPressed() {
    }

}
