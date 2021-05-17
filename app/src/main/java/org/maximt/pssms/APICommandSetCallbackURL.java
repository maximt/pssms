package org.maximt.pssms;

import android.content.Context;
import android.util.Patterns;
import android.webkit.URLUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by maxim on 29.09.17.
 */

public class APICommandSetCallbackURL extends APICommand {
    private static final String TAG = "APICommandSetCallbackURL";

    public APICommandSetCallbackURL(Context context) {
        super(context);
    }

    @Override
    public void run() {
        String url = getParam("url");

        if (!url.isEmpty() && isValidUrl(url)) {
            Config cfg = new Config(getContext());
            cfg.setCallbackURL(url);
            setResult("OK", "saved");
        } else {
            setResult("ERROR", "InvalidArguments");
        }
    }

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return (URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) && m.matches();
    }
}
