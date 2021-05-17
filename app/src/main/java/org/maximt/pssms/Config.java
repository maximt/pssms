package org.maximt.pssms;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by maxim on 02.10.17.
 */

public final class Config {
    final static String TAG = "PSSMS_PREFS";
    final static String TAG_CALLBACK_URL = "callback_url";

    private final SharedPreferences m_Prefs;

    public Config(Context context) {
        m_Prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getCallbackUrl() {
        return m_Prefs.getString(TAG_CALLBACK_URL, "");
    }

    public void setCallbackURL(String url) {
        SharedPreferences.Editor e = m_Prefs.edit();
        e.putString(TAG_CALLBACK_URL, url);
        e.apply();
    }

}
