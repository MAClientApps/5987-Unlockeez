package com.wifisecure.unlockeez;


import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.affise.attribution.Affise;
import com.affise.attribution.BuildConfig;
import com.affise.attribution.events.predefined.CustomId01Event;
import com.affise.attribution.init.AffiseInitProperties;
import com.affise.attribution.referrer.AffiseReferrerData;
import com.affise.attribution.referrer.OnReferrerCallback;
import com.affise.attribution.referrer.ReferrerKey;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

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


/*        AdjustConfig adjustConfig = new AdjustConfig(this, Utils.ADJUST_TOKEN_VALUE,
                AdjustConfig.ENVIRONMENT_PRODUCTION);
        Adjust.addSessionCallbackParameter(Utils.PREF_KEY_USER_UUID,
                Utils.generateClickID(getApplicationContext()));

        adjustConfig.setOnAttributionChangedListener(attribution -> {
            Log.e("App", "attribution: " + attribution.toString());
            Utils.setReceivedAttribution(getApplicationContext(),
                    attribution.toString());
            Utils.setCampaign(mContext, attribution.campaign);
        });

        adjustConfig.setOnDeeplinkResponseListener(deeplink -> {
            Log.e("App", "deeplink: " + deeplink.toString());
            Utils.setDeepLink(getApplicationContext(), deeplink.toString());
            return false;
        });

        try {
            FirebaseAnalytics.getInstance(mContext)
                    .getAppInstanceId()
                    .addOnCompleteListener(task -> {
                        AdjustEvent adjustEvent_InternalFlow = new AdjustEvent(Utils.ADJUST_FIREBASE_INSTANCE_ID);
                        adjustEvent_InternalFlow.addCallbackParameter(Utils.PARAM_KEY_EVENT_VALUE,
                                task.getResult());
                        adjustEvent_InternalFlow.addCallbackParameter(Utils.PREF_KEY_USER_UUID,
                                Utils.generateClickID(getApplicationContext()));
                        Adjust.addSessionCallbackParameter(Utils.PARAM_KEY_FIREBASE_INSTANCE_ID,
                                task.getResult());
                        Adjust.trackEvent(adjustEvent_InternalFlow);
                        Adjust.sendFirstPackages();
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

        AffiseInitProperties properties = new AffiseInitProperties(
                "236", //Change to your app id
                !BuildConfig.DEBUG, //Add your custom rule to determine if this is a production build
                null, //Change to your partParamName
                null, //Change to your partParamNameToken
                null, //Change to your appToken
                "d097d619-1622-404b-af90-c8ae7e2756df", //Change to your secretId
                Collections.emptyList(), // Types to handles interception of clicks on activity
                false // Affise metrics
        );
        Affise.init(this, properties);
        Affise.setTrackingEnabled(true);


        // Affise.sendEvent(new AddToCartEvent(items, System.currentTimeMillis(), "groceries"));
        Affise.sendEvent(new CustomId01Event("gdg", System.currentTimeMillis(), ""));


        Affise.registerDeeplinkCallback(deeplink -> {
            Log.e("App", "deeplink: " + deeplink.getQueryParameter("screen"));
            Utils.setDeepLink(getApplicationContext(), deeplink.toString());
            Log.e("App", "received deeplink: " + System.currentTimeMillis());
            Affise.sendEvent(new CustomId01Event("CustomEvent", System.currentTimeMillis(), "deeplink"));
            return false;
        });

        Affise.getReferrerValue(ReferrerKey.AFFISE_DEEPLINK, s -> {
            Log.e("App", "AFFISE_DEEPLINK: " + s);
            Log.e("App", "received AFFISE_DEEPLINK: " + System.currentTimeMillis());
            //Utils.setReceivedAttribution(getApplicationContext(), s);
           // Affise.sendEvent(new CustomId01Event("CustomEvent", System.currentTimeMillis(), "AFFISE_DEEPLINK"));
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
           // Affise.sendEvent(new CustomId01Event("CustomEvent", System.currentTimeMillis(), "CAMPAIGN_ID"));
            //  Utils.setCampaign(mContext, s);
        });
        Affise.getReferrerValue(ReferrerKey.CLICK_ID, s -> {
            Log.e("App", "CLICK_ID: " + s);
            Log.e("App", "received CLICK_ID: " + System.currentTimeMillis());
           // Affise.sendEvent(new CustomId01Event("CustomEvent", System.currentTimeMillis(), "CLICK_ID"));
            // Utils.setClickID(mContext, s);
        });


    }


}