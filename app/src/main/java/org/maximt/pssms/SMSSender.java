package org.maximt.pssms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.ArrayList;

/**
 * Created by maxim on 28.09.17.
 */

public class SMSSender {
    private final Context mContext;

    public SMSSender(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public void send(String number, String text) {
        SmsManager smsManager = SmsManager.getDefault();

        ArrayList<String> messageList = smsManager.divideMessage(text);

        ArrayList<PendingIntent> sentIntents = new ArrayList<>(messageList.size());
        ArrayList<PendingIntent> deliveryIntents = new ArrayList<>(messageList.size());


        for (int j = 0; j < messageList.size(); j++) {
            Intent si = new Intent(SMSStatusReceiver.SMS_SENT);
            si.putExtra("number", number);
            si.putExtra("part", Integer.toString(j));
            si.putExtra("parts", Integer.toString(messageList.size()));
            sentIntents.add(PendingIntent.getBroadcast(getContext(), 0, si, PendingIntent.FLAG_UPDATE_CURRENT));

            Intent di = new Intent(SMSStatusReceiver.SMS_DELIVERED);
            di.putExtra("number", number);
            di.putExtra("part", Integer.toString(j));
            di.putExtra("parts", Integer.toString(messageList.size()));
            deliveryIntents.add(PendingIntent.getBroadcast(getContext(), 0, di, PendingIntent.FLAG_UPDATE_CURRENT));
        }

        smsManager.sendMultipartTextMessage(number, null, messageList, sentIntents, deliveryIntents);
    }
}
