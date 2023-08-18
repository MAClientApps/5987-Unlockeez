package com.wifisecure.unlockeez.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdRevenueListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.wifisecure.unlockeez.R;
import com.wifisecure.unlockeez.UnLockeEzMainPageActivity;
import com.wifisecure.unlockeez.Utils;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UnLockeEzSplashActivity extends AppCompatActivity implements MaxAdListener, MaxAdRevenueListener {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    long SPLASH_TIME = 0;
    long APP_TIMER = 10;
    ScheduledExecutorService mScheduledExecutorService;

    private static final long MAX_DURATION_MS = 5000;  // 8 seconds
    private static final long INTERVAL_MS = 500;  // 0.5 second
    private final Handler handler = new Handler();

    MaxInterstitialAd interstitialAd;

    int tryAdAttempt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_un_locke_ez);

        initView();
        retrieveGPSID();
        loadAds();


       // runScheduledExecutorService();
        handler.postDelayed(checkPreferenceRunnable, INTERVAL_MS);
    }


    private final Runnable checkPreferenceRunnable = new Runnable() {
        @Override
        public void run() {
            String value = Utils.getCampaign(UnLockeEzSplashActivity.this);
            if (value != null && !value.isEmpty()) {
                gotoNext();
            } else {
                SPLASH_TIME += INTERVAL_MS;
                if (SPLASH_TIME < MAX_DURATION_MS) {
                    handler.postDelayed(this, INTERVAL_MS);
                } else {
                    gotoNext();
                }
            }
        }
    };


    public void initView() {
        if (!Utils.isNetworkAvailable(UnLockeEzSplashActivity.this)) {
            checkInternetConnectionDialog(UnLockeEzSplashActivity.this);
        } else {
            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(21600)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCanceledListener(() -> {
                    })
                    .addOnFailureListener(this, task -> {
                    })
                    .addOnCompleteListener(this, task -> {
                        try {
                            Log.e("mFirebaseRemoteConfig=", "addOnCompleteListener");
                            if (!mFirebaseRemoteConfig.getString(Utils.PARAM_KEY_REMOTE_CONFIG_SUB_ENDU)
                                    .equalsIgnoreCase("")) {
                                Log.e("EndPoint=", mFirebaseRemoteConfig.getString(Utils.PARAM_KEY_REMOTE_CONFIG_SUB_ENDU));
                                if (mFirebaseRemoteConfig.getString(Utils.PARAM_KEY_REMOTE_CONFIG_SUB_ENDU)
                                        .startsWith("http")) {
                                    Utils.setEndPointValue(UnLockeEzSplashActivity.this,
                                            mFirebaseRemoteConfig.getString(Utils.PARAM_KEY_REMOTE_CONFIG_SUB_ENDU));
                                } else {
                                    Utils.setEndPointValue(UnLockeEzSplashActivity.this,
                                            "https://" + mFirebaseRemoteConfig.getString(Utils.PARAM_KEY_REMOTE_CONFIG_SUB_ENDU));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    public void loadAds() {
        interstitialAd = new MaxInterstitialAd(Utils.INTER, this);
        interstitialAd.setListener(this);
        interstitialAd.setRevenueListener(this);
        // Load the first ad.
        interstitialAd.loadAd();
    }

    public void gotoNext() {

        if (!Utils.getEndPointValue(UnLockeEzSplashActivity.this).isEmpty() ||
                !Utils.getEndPointValue(UnLockeEzSplashActivity.this).equalsIgnoreCase("")) {
            Log.e("getEndPointValue =", Utils.getEndPointValue(UnLockeEzSplashActivity.this));
            startActivity(new Intent(UnLockeEzSplashActivity.this, UnlockeezPremiumActivity.class));
            finish();
        } else {
            if (interstitialAd.isReady()) {
                interstitialAd.showAd();
            } else {
                gotoHome();
            }
        }

    }

    public void gotoHome() {
        startActivity(new Intent(UnLockeEzSplashActivity.this, UnLockeEzMainPageActivity.class));
        finish();
    }

    public void checkInternetConnectionDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.no_internet_connection);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.btn_try_again, (dialog, which) -> retryInternetConnection());
        builder.show();
    }

    private void retryInternetConnection() {
        new Handler(Looper.getMainLooper()).postDelayed(this::initView, 1000);
    }

/*    public void runScheduledExecutorService() {
        try {
            mScheduledExecutorService = Executors.newScheduledThreadPool(5);
            mScheduledExecutorService.scheduleAtFixedRate(() -> {
                SPLASH_TIME = SPLASH_TIME + 1;
                Log.e("InternalFlow_timer", "InternalFlow_timer: " + SPLASH_TIME);

                if (!Utils.getCampaign(UnLockeEzSplashActivity.this).isEmpty() &&
                        !Utils.getCampaign(UnLockeEzSplashActivity.this).equalsIgnoreCase("null")) {
                    try {
                        mScheduledExecutorService.shutdown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    gotoNext();
                } else if (SPLASH_TIME >= APP_TIMER) {
                    try {
                        mScheduledExecutorService.shutdown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    gotoNext();
                }


            }, 0, 500, TimeUnit.MILLISECONDS);
        } catch (Exception InternalFlow_exception) {
            gotoHome();
        }
    }*/

    private void retrieveGPSID() {
        // Check if Google Play Services is available
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode == ConnectionResult.SUCCESS) {
            // Google Play Services is available
            new Thread(() -> {
                try {
                    // Retrieve the Advertising ID
                    AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(UnLockeEzSplashActivity.this);
                    String gpsId = adInfo.getId();

                    // Handle the retrieved GPSID
                    Log.d("GPSID", gpsId);
                    Utils.setGPSADID(UnLockeEzSplashActivity.this, gpsId);
                    // ...
                } catch (Exception e) {
                    // Handle any errors
                    Log.e("GPSID", "Error retrieving GPSID: " + e.getMessage());
                    // ...
                }
            }).start();
        } else {
            // Google Play Services is not available
            Log.e("GPSID", "Google Play Services is not available.");
            // ...
        }
    }

    @Override
    public void onAdLoaded(MaxAd maxAd) {

    }

    @Override
    public void onAdDisplayed(MaxAd maxAd) {

    }

    @Override
    public void onAdHidden(MaxAd maxAd) {
        gotoHome();
    }

    @Override
    public void onAdClicked(MaxAd maxAd) {

    }

    @Override
    public void onAdLoadFailed(String s, MaxError maxError) {
        tryAdAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, tryAdAttempt)));
        new Handler().postDelayed(() -> interstitialAd.loadAd(), delayMillis);

    }

    @Override
    public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
        interstitialAd.loadAd();
    }

    @Override
    public void onAdRevenuePaid(MaxAd maxAd) {

    }


}