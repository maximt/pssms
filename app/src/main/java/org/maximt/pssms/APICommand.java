package org.maximt.pssms;

import android.content.Context;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by maxim on 29.09.17.
 */

public abstract class APICommand {
    private Context mContext;
    private Map<String, String> mParams;
    private String mResult;

    public APICommand(Context context) {
        mContext = context;
    }

    public APICommand() {

    }

    public Context getContext() {
        return mContext;
    }

    public void clearParams() {
        if (mParams != null) {
            mParams.clear();
        }
    }

    public void addParam(String k, String v) {
        if (mParams == null) {
            mParams = new Hashtable<>();
        }

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

    public abstract void run();

    public void setResult(String status, String message) {
        mResult = String.format("{'status': '%s', 'message': '%s'}", status, message);
    }

    public String getResult() {
        return mResult;
    }
}
