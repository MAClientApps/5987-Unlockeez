package com.wifisecure.unlockeez;


import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.affise.attribution.Affise;
import com.affise.attribution.init.AffiseInitProperties;
import com.affise.attribution.referrer.ReferrerKey;
import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.wifisecure.unlockeez.Activity.UnLockeEzSplashActivity;


public class App extends Application {

    Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            builder.detectFileUriExposure();
            StrictMode.setVmPolicy(builder.build());
        }

        try {
            mContext = this;
        } catch (Exception e) {
            e.printStackTrace();
        }

        AppLovinSdk.getInstance(this).setMediationProvider(AppLovinMediationProvider.MAX);
        AppLovinSdk.initializeSdk(this, appLovinSdkConfiguration -> {
        });

        AffiseInitProperties properties = new AffiseInitProperties(
                "236", //Change to your app id
                "d097d619-1622-404b-af90-c8ae7e2756df"//Change to your secretId
        );
        Affise.init(this, properties);
        Affise.setTrackingEnabled(true);



        Utils.generateClickID(mContext);
        Affise.getReferrer(s -> {
            Log.e("App", "getReferrer: " + s);
        });
        Affise.getReferrerValue(ReferrerKey.AFFISE_DEEPLINK, s -> {
            Log.e("App", "AFFISE_DEEPLINK: " + s);
        });
        Affise.getReferrerValue(ReferrerKey.AFFISE_AD_ID, value -> {
            Log.e("App", "AFFISE_AD_ID: " + value);
        });
        Affise.getReferrerValue(ReferrerKey.AFFC, value -> {
            Log.e("App", "AFFC: " + value);
        });
        Affise.getReferrerValue(ReferrerKey.AFFISE_AFFC_ID, value -> {
            Log.e("App", "AFFISE_AFFC_ID: " + value);
            Utils.setCampaign(this, value);
        });
        Affise.getReferrerValue(ReferrerKey.AD_ID, s -> {
            Log.e("App", "AD_ID: " + s);
        });
        Affise.getReferrerValue(ReferrerKey.CAMPAIGN_ID, s -> {
            Log.e("App", "CAMPAIGN_ID: " + s);
        });
        Affise.getReferrerValue(ReferrerKey.CLICK_ID, s -> {
            Log.e("App", "CLICK_ID: " + s);
            Utils.setClickID(this, s);
        });
        Log.e("App", "RandomUserId: " + Affise.getRandomUserId());


        try {
            FirebaseApp.initializeApp(this);
            FirebaseAnalytics.getInstance(mContext)
                    .getAppInstanceId()
                    .addOnCompleteListener(task -> {
                        Utils.setFirebaseInstanceID(mContext, task.getResult());
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}