package org.maximt.pssms;

import android.content.Context;

/**
 * Created by maxim on 29.09.17.
 */

public class APICommandSendSMS extends APICommand {
    private static final String TAG = "APICommandSendSMS";
    private final SMSSender mSMSSender;

    private String result;

    public APICommandSendSMS(Context context) {
        super(context);
        mSMSSender = new SMSSender(context);
    }

    @Override
    public void run() {
        String to = getParam("to");
        String text = getParam("text");
        if (!(to.isEmpty() || text.isEmpty())) {
            mSMSSender.send(to, text);
            setResult("OK", "Sent");
        } else {
            setResult("ERROR", "InvalidArguments");
        }
    }

}
