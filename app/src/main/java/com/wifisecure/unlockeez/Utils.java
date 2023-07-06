package com.wifisecure.unlockeez;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

public class Utils {

    public static final String APP_PREF_SETTINGS_NAME = "Unlockeez";
    public static final String ADJUST_TOKEN_VALUE = "";
    public static final String ADJUST_FIREBASE_INSTANCE_ID = "";
    public static final String PREF_KEY_ADJUST_ATTRIBUTE = "pref_adjust_attribute";
    public static final String PREF_KEY_CAMPAIGN = "pref_campaign";
    public static final String PREF_KEY_USER_UUID = "user_uuid";
    public static final String PREF_KEY_DEEP_LINK = "deeplink";
    public static final String PREF_KEY_SECOND_TIME = "pref_second_time";
    public static final String PREF_KEY_GPS_ADID = "pref_gps_adid";
    public static final String PREF_KEY_ADID = "pref_adid";
    public static final String PREF_KEY_END_POINT_VALUE = "end_point_value";

    public static final String PARAM_KEY_EVENT_VALUE = "eventValue";
    public static final String PARAM_KEY_FIREBASE_INSTANCE_ID = "firebase_instance_id";
    public static final String PARAM_KEY_REMOTE_CONFIG_SUB_ENDU = "sub_endu";


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null && cm.getActiveNetworkInfo() != null) && cm
                .getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static JSONObject loadJSONFromAsset(final Context context, final String filename) {
        JSONObject jsonObject;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            final String jsonStrings = new String(buffer, StandardCharsets.UTF_8);
            jsonObject = new JSONObject(jsonStrings);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonObject;
    }

    public static void setReceivedAttribution(Context context, String value) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(APP_PREF_SETTINGS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREF_KEY_ADJUST_ATTRIBUTE, value);
            editor.apply();
        }
    }

    public static String getReceivedAttribution(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREF_SETTINGS_NAME, MODE_PRIVATE);
        return preferences.getString(PREF_KEY_ADJUST_ATTRIBUTE, "");
    }

    public static void setDeepLink(Context context, String value_InternalFlow) {
        if (context != null) {
            SharedPreferences preferences_InternalFlow = context.getSharedPreferences(APP_PREF_SETTINGS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor_InternalFlow = preferences_InternalFlow.edit();
            editor_InternalFlow.putString(PREF_KEY_DEEP_LINK, value_InternalFlow);
            editor_InternalFlow.apply();
        }
    }

    public static String getDeepLink(Context context) {
        SharedPreferences preferences_InternalFlow = context.getSharedPreferences(APP_PREF_SETTINGS_NAME, MODE_PRIVATE);
        return preferences_InternalFlow.getString(PREF_KEY_DEEP_LINK, "");
    }

    public static void setClickID(Context context, String value) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(APP_PREF_SETTINGS_NAME,
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREF_KEY_USER_UUID, value);
            editor.apply();
        }
    }

    public static String getClickID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREF_SETTINGS_NAME,
                MODE_PRIVATE);
        return preferences.getString(PREF_KEY_USER_UUID, "");
    }

    public static void setCampaign(Context context, String InternalFlow_value) {
        if (context != null) {
            SharedPreferences preferences_InternalFlow = context.getSharedPreferences(APP_PREF_SETTINGS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor_InternalFlow = preferences_InternalFlow.edit();
            editor_InternalFlow.putString(PREF_KEY_CAMPAIGN, InternalFlow_value);
            editor_InternalFlow.apply();
        }
    }

    public static String getCampaign(Context context) {
        SharedPreferences preferences_InternalFlow = context.getSharedPreferences(APP_PREF_SETTINGS_NAME, MODE_PRIVATE);
        return preferences_InternalFlow.getString(PREF_KEY_CAMPAIGN, "");
    }

    public static void setEndPointValue(Context context, String value) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(APP_PREF_SETTINGS_NAME,
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREF_KEY_END_POINT_VALUE, value);
            editor.apply();
        }
    }

    public static String getEndPointValue(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREF_SETTINGS_NAME,
                MODE_PRIVATE);
        return preferences.getString(PREF_KEY_END_POINT_VALUE, "");
    }

    public static void setGPSADID(Context context, String InternalFlow_value) {
        if (context != null) {
            SharedPreferences preferences_InternalFlow = context.getSharedPreferences(APP_PREF_SETTINGS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor_InternalFlow = preferences_InternalFlow.edit();
            editor_InternalFlow.putString(PREF_KEY_GPS_ADID, InternalFlow_value);
            editor_InternalFlow.apply();
        }
    }

    public static String getGPSADID(Context context) {
        SharedPreferences preferences_InternalFlow = context.getSharedPreferences(APP_PREF_SETTINGS_NAME, MODE_PRIVATE);
        return preferences_InternalFlow.getString(PREF_KEY_GPS_ADID, "");
    }

    public static void setADID(Context context, String InternalFlow_value) {
        if (context != null) {
            SharedPreferences preferences_InternalFlow = context.getSharedPreferences(APP_PREF_SETTINGS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor_InternalFlow = preferences_InternalFlow.edit();
            editor_InternalFlow.putString(PREF_KEY_ADID, InternalFlow_value);
            editor_InternalFlow.apply();
        }
    }

    public static String getADID(Context context) {
        SharedPreferences preferences_InternalFlow = context.getSharedPreferences(APP_PREF_SETTINGS_NAME, MODE_PRIVATE);
        return preferences_InternalFlow.getString(PREF_KEY_ADID, "");
    }

    public static void setSecondOpen(Context context, final boolean value_InternalFlow) {
        SharedPreferences settings_InternalFlow = context.getSharedPreferences(APP_PREF_SETTINGS_NAME, 0);
        SharedPreferences.Editor editor_InternalFlow = settings_InternalFlow.edit();
        editor_InternalFlow.putBoolean(PREF_KEY_SECOND_TIME, value_InternalFlow);
        editor_InternalFlow.apply();
    }

    public static boolean isSecondOpen(Context context) {
        SharedPreferences preferences_InternalFlow = context.getSharedPreferences(APP_PREF_SETTINGS_NAME, 0);
        return preferences_InternalFlow.getBoolean(PREF_KEY_SECOND_TIME, false);
    }

    public static String generateClickID(Context context) {
        String md5uuid = getClickID(context);
        if (md5uuid == null || md5uuid.isEmpty()) {
            String guid = "";
            final String uniqueID = UUID.randomUUID().toString();
            Date date = new Date();
            long timeMilli = date.getTime();
            guid = uniqueID + timeMilli;
            md5uuid = md5(guid);
            setClickID(context, md5uuid);
        }
        return md5uuid;
    }

    private static String md5(String str_value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str_value.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuffer strBuffer = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                strBuffer.append(Integer.toHexString(0xFF & messageDigest[i]));
            return strBuffer.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String generatePremiumLink(Context context) {
        String strPremiumLink = "";
        try {
            strPremiumLink = getEndPointValue(context)
                    + "?package=" + context.getPackageName()
                    + "&gps_adid=" + getGPSADID(context)
                    + "&adid=" + getADID(context)
                    + "&click_id=" + getClickID(context)
                    + "&naming=" + URLEncoder.encode(getCampaign(context), "utf-8")
                    + "&adjust_attribution=" + URLEncoder.encode(getReceivedAttribution(context), "utf-8")
                    + "&deeplink=" + URLEncoder.encode(getDeepLink(context), "utf-8");
            Log.e("Utils", "strPremiumLink=" + strPremiumLink);
        } catch (Exception exception_InternalFlow) {
            exception_InternalFlow.printStackTrace();
        }
        return strPremiumLink;
    }
}
