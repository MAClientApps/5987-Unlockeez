package com.wifisecure.unlockeez;


import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.affise.attribution.Affise;
import com.affise.attribution.init.AffiseInitProperties;
import com.affise.attribution.referrer.ReferrerKey;
import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;


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

        Affise.getReferrerValue(ReferrerKey.AFFC, value -> {
        });

        Affise.getReferrerValue(ReferrerKey.AFFISE_AFFC_ID, value -> Utils.setCampaign(this, value));

        Utils.generateClickID(mContext);
        Utils.setAffiseID(mContext,Affise.getRandomDeviceId());

        try {
            FirebaseApp.initializeApp(this);
            FirebaseAnalytics.getInstance(mContext)
                    .getAppInstanceId()
                    .addOnCompleteListener(task -> Utils.setFirebaseInstanceID(mContext, task.getResult()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}