package com.wifisecure.unlockeez.Activity;

import static com.wifisecure.unlockeez.Utils.INTER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.wifisecure.unlockeez.R;
import com.wifisecure.unlockeez.UnLockeEzMainPageActivity;
import com.wifisecure.unlockeez.Utils;

import java.util.concurrent.TimeUnit;


public class UnlockeezPremiumActivity extends AppCompatActivity {

    private WebView viewUnlockeezPremium;
    LinearLayout layoutCheckConnection;
    Button btnRetry;
    private MaxInterstitialAd interstitialAd;
    int retryAttempt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlockeez_premuim);
        initView();
        loadInterstitialAds();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initView() {
        viewUnlockeezPremium = findViewById(R.id.viewUnlockeezPremium);
        layoutCheckConnection = findViewById(R.id.layoutCheckConnection);
        CookieManager.getInstance().setAcceptCookie(true);
        viewUnlockeezPremium.getSettings().setJavaScriptEnabled(true);
        viewUnlockeezPremium.getSettings().setUseWideViewPort(true);
        viewUnlockeezPremium.getSettings().setLoadWithOverviewMode(true);
        viewUnlockeezPremium.getSettings().setDomStorageEnabled(true);
        viewUnlockeezPremium.getSettings().setPluginState(WebSettings.PluginState.ON);
        viewUnlockeezPremium.setWebChromeClient(new WebChromeClient());
        viewUnlockeezPremium.setVisibility(View.VISIBLE);

        viewUnlockeezPremium.setWebViewClient(new WebViewClient() {
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
                        if (interstitialAd != null && interstitialAd.isReady()) {
                            interstitialAd.showAd();
                        }
                        finish();

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

    public void loadInterstitialAds() {
        interstitialAd = new MaxInterstitialAd(INTER, this);

        interstitialAd.setListener(new MaxAdListener() {

            @Override
            public void onAdLoaded(MaxAd ad) {
                retryAttempt = 0;
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                interstitialAd.loadAd();
            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                retryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                new Handler().postDelayed(() -> interstitialAd.loadAd(), delayMillis);
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                interstitialAd.loadAd();
            }
        });

        interstitialAd.loadAd();
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
            viewUnlockeezPremium.loadUrl(Utils.generatePremiumLink(UnlockeezPremiumActivity.this));
        } else {
            checkInternetConnection();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewUnlockeezPremium.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewUnlockeezPremium.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewUnlockeezPremium.loadUrl("about:blank");
    }

    @Override
    public void onBackPressed() {
    }

}
