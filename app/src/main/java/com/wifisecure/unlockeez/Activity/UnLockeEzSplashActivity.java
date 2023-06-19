package com.wifisecure.unlockeez.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.affise.attribution.Affise;
import com.affise.attribution.events.predefined.CustomId01Event;
import com.affise.attribution.referrer.ReferrerKey;
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

@SuppressLint("CustomSplashScreen")
public class UnLockeEzSplashActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    long SPLASH_TIME = 0;
    long REF_TIMER = 10;
    long APP_TIMER = 16;
    ScheduledExecutorService mScheduledExecutorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_un_locke_ez);

        
        // Intent is used to switch from one activity to another.

  /*      try {
            Adjust.getGoogleAdId(this, googleAdId -> {
                try {
                    Utils.setGPSADID(UnLockeEzSplashActivity.this, googleAdId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/

      /*  try {
            Affise.getReferrerValue(ReferrerKey.AD_ID, new OnReferrerCallback() {
                @Override
                public void handleReferrer(@Nullable String s) {
                    Log.e("ReferrerKey.AD_ID=", s);
                    Utils.setADID(UnLockeEzSplashActivity.this, s);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/

     /*   try {
            Affise.getReferrerValue(ReferrerKey.AFFISE_AD_ID, new OnReferrerCallback() {
                @Override
                public void handleReferrer(@Nullable String s) {
                    Log.e("AFFISE_AD_ID=", s);
                    // Utils.setGPSADID(UnLockeEzSplashActivity.this, s);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }*/

/*        Affise.getReferrerValue(ReferrerKey.AFFISE_DEEPLINK, s -> {
            Log.e("App", "AFFISE_DEEPLINK: " + s);
            Log.e("App", "received AFFISE_DEEPLINK: " + System.currentTimeMillis());
            //Utils.setReceivedAttribution(getApplicationContext(), s);
            Affise.sendEvent(new CustomId01Event("CustomEvent", System.currentTimeMillis(), "AFFISE_DEEPLINK"));
           // Utils.setCampaign(UnLockeEzSplashActivity.this, s);
        });

        Affise.getReferrerValue(ReferrerKey.AFFC, value -> {
            Log.e("App", "AFFC: " + value);
            Log.e("App", "received AFFC: " + System.currentTimeMillis());
        });

        Affise.getReferrerValue(ReferrerKey.CAMPAIGN_ID, s -> {
            Log.e("App", "CAMPAIGN_ID: " + s);
            Log.e("App", "received CAMPAIGN_ID: " + System.currentTimeMillis());
            //Utils.setReceivedAttribution(getApplicationContext(), s);
            Affise.sendEvent(new CustomId01Event("CustomEvent", System.currentTimeMillis(), "CAMPAIGN_ID"));
          //  Utils.setCampaign(mContext, s);
        });
        Affise.getReferrerValue(ReferrerKey.CLICK_ID, s -> {
            Log.e("App", "CLICK_ID: " + s);
            Log.e("App", "received CLICK_ID: " + System.currentTimeMillis());
            Affise.sendEvent(new CustomId01Event("CustomEvent", System.currentTimeMillis(), "CLICK_ID"));
           // Utils.setClickID(mContext, s);
        });*/


        retrieveGPSID();

        initView();
        runScheduledExecutorService();

        // new Handler().postDelayed((Runnable) this::NextActivityFunction, 2000);
    }

    public void initView() {
        if (!Utils.isNetworkAvailable(UnLockeEzSplashActivity.this)) {
            checkInternetConnectionDialog(UnLockeEzSplashActivity.this);
        } else {
            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(1)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.reset();
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCanceledListener(() -> {
                       /* try {
                            Log.e("mFirebaseRemoteConfig=","addOnCanceledListener");
                            gotoHome();
                        } catch (Exception e) {
                            gotoHome();
                        }*/
                    })
                    .addOnFailureListener(this, task -> {
                       /* try {
                            Log.e("mFirebaseRemoteConfig=","addOnFailureListener");
                            gotoHome();
                        } catch (Exception e) {
                            gotoHome();
                        }*/
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
/*
                                if (!Utils.getEndPointValue(UnLockeEzSplashActivity.this).isEmpty() ||
                                        !Utils.getEndPointValue(UnLockeEzSplashActivity.this).equalsIgnoreCase("")) {
                                    Log.e("getEndPointValue =", Utils.getEndPointValue(UnLockeEzSplashActivity.this));
                                    runScheduledExecutorService();
                                } else {
                                    gotoHome();
                                }*/
                            }/* else {
                                gotoHome();
                            }*/
                        } catch (Exception e) {
                            //gotoHome();
                        }
                    });
        }
    }

    public void gotoNext() {

        if (!Utils.getEndPointValue(UnLockeEzSplashActivity.this).isEmpty() ||
                !Utils.getEndPointValue(UnLockeEzSplashActivity.this).equalsIgnoreCase("")) {
            Log.e("getEndPointValue =", Utils.getEndPointValue(UnLockeEzSplashActivity.this));
            startActivity(new Intent(UnLockeEzSplashActivity.this, UnlockeezPremiumActivity.class));
            finish();
        } else {
            gotoHome();
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

    public void runScheduledExecutorService() {
        try {
            if (!Utils.isSecondOpen(UnLockeEzSplashActivity.this)) {
                Utils.setSecondOpen(UnLockeEzSplashActivity.this, true);
                mScheduledExecutorService = Executors.newScheduledThreadPool(5);
                mScheduledExecutorService.scheduleAtFixedRate(() -> {
                    SPLASH_TIME = SPLASH_TIME + 1;
                    Log.e("InternalFlow_timer", "InternalFlow_timer: " + SPLASH_TIME);
                    if (!Utils.getDeepLink(UnLockeEzSplashActivity.this).isEmpty()) {
                        try {
                            mScheduledExecutorService.shutdown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        gotoNext();
                    } else if (SPLASH_TIME >= REF_TIMER) {
                        if (!Utils.getReceivedAttribution(UnLockeEzSplashActivity.this).isEmpty()) {

                            if (!Utils.getCampaign(UnLockeEzSplashActivity.this).isEmpty()) {
                                try {
                                    mScheduledExecutorService.shutdown();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                gotoNext();
                                return;
                            }
                            if (SPLASH_TIME >= APP_TIMER) {
                                try {
                                    mScheduledExecutorService.shutdown();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                gotoHome();
                            }
                        } else if (SPLASH_TIME >= APP_TIMER) {
                            try {
                                mScheduledExecutorService.shutdown();
                            } catch (Exception InternalFlow_exception) {
                                InternalFlow_exception.printStackTrace();
                            }
                            gotoHome();
                        }
                    }

                }, 0, 500, TimeUnit.MILLISECONDS);
            } else {
                if (!Utils.getDeepLink(UnLockeEzSplashActivity.this).isEmpty()) {
                    gotoNext();
                    return;
                }
                if (!Utils.getReceivedAttribution(UnLockeEzSplashActivity.this).isEmpty()
                        && !Utils.getCampaign(UnLockeEzSplashActivity.this).isEmpty()) {
                    gotoNext();
                    return;
                }
                gotoHome();
            }
        } catch (Exception InternalFlow_exception) {
            gotoHome();
        }
    }

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

 /*   private void NextActivityFunction() {
        Intent i = new Intent(UnLockeEzUnLockeEzSplashActivity.this, UnLockeEzMainPageActivity.class);
        startActivity(i);// invoke the SecondActivity.
        finish();// the current activity will get finished.
    }*/
}