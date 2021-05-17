package org.maximt.pssms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

/**
 * Created by maxim on 27.09.17.
 */

public class SMSReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Config cfg = new Config(context);
        String url = cfg.getCallbackUrl();

        if (url.isEmpty())
            return;

        Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
        String from = "";

        StringBuilder bodyText = new StringBuilder();
        for (int i = 0; i < pduArray.length; i++) {
            SmsMessage msg = SmsMessage.createFromPdu((byte[]) pduArray[i]);
            bodyText.append(msg.getMessageBody());

            if (from.isEmpty())
                from = msg.getOriginatingAddress();
        }
        String text = bodyText.toString();


        APICallback callback = new APICallbackSMSReceived(url, context);
        callback.addParam("from", from);
        callback.addParam("text", text);

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
