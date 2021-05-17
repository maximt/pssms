package org.maximt.pssms;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by maxim on 02.10.17.
 */

public abstract class APICallback {
    private final Context mContext;
    private Map<String, String> mParams;
    private final String mUrl;

    public APICallback(String url, String action, Context context) {
        mContext = context;
        mUrl = url;

        addParam("action", action);
    }

    public Context getContext() {
        return mContext;
    }

    public String getUrl() {
        return mUrl;
    }

    public void addParam(String k, String v) {
        if (mParams == null) {
            mParams = new Hashtable<>();
        }
        if (v == null)
            v = "";

        mParams.put(k, v);
    }

    public void removeParam(String k) {
        if (mParams != null) {
            mParams.remove(k);
        }
    }

    public String getParam(String k) {
        if (mParams != null && mParams.containsKey(k)) {
            return mParams.get(k);
        }
        return "";
    }

    private String buildUrl() throws MalformedURLException {
        Uri builtUri = Uri.parse(mUrl);
        Uri.Builder builder = builtUri.buildUpon();

        if (mParams != null) {
            for (String k : mParams.keySet()) {
                String v = mParams.get(k);
                builder.appendQueryParameter(k, v);
            }

        }

        return builder.build().toString();
    }

    public void request() {
        try {
            String url = buildUrl();
            new RequestTask().execute(url);
        } catch (Exception e) {
            Log.e("APICallback", e.getMessage(), e);
        }
    }

    private class RequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String data = "";
            try {
                data = doRequest(urls[0]);
            } catch (IOException ex) {
                Log.e("RequestTask", ex.getMessage(), ex);
            }
            return data;
        }

        @Override
        protected void onPostExecute(String content) {

        }

        private String doRequest(String url) throws IOException {
            HttpURLConnection conn = null;
            try {
                URL u = new URL(url);
                conn = (HttpURLConnection) u.openConnection();

                conn.setRequestMethod("GET");
                conn.setReadTimeout(10000);
                conn.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder buf = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    buf.append(line + "\n");
                }

                return (buf.toString());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

    }
}

