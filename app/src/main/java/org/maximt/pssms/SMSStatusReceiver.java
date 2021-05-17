package org.maximt.pssms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class SMSStatusReceiver extends BroadcastReceiver {
    public static final String SMS_SENT = "SMS_SENT";
    public static final String SMS_DELIVERED = "SMS_DELIVERED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Config cfg = new Config(context);
        APICallback callback;

        switch (action) {
            case SMS_SENT:
                callback = new APICallbackSMSSent(cfg.getCallbackUrl(), context);
                break;
            case SMS_DELIVERED:
                callback = new APICallbackSMSDelivered(cfg.getCallbackUrl(), context);
                break;
            default:
                return;
        }

        callback.addParam("from", intent.getStringExtra("number"));
        callback.addParam("part", intent.getStringExtra("part"));
        callback.addParam("parts", intent.getStringExtra("parts"));

        int rc = getResultCode();
        if (rc == Activity.RESULT_OK) {
            callback.addParam("status", "OK");
        } else {
            callback.addParam("status", "ERROR");
            callback.addParam("msg", Integer.toString(rc));
        }

        callback.request();
    }
}
